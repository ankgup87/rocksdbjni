package org.fusesource.rocksdbjni.internal;


import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniField;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ArgFlag.CRITICAL;
import static org.fusesource.hawtjni.runtime.ArgFlag.NO_IN;
import static org.fusesource.hawtjni.runtime.ArgFlag.NO_OUT;
import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;
import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;
import static org.fusesource.hawtjni.runtime.FieldFlag.POINTER_FIELD;
import static org.fusesource.hawtjni.runtime.MethodFlag.CONSTANT_INITIALIZER;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_DELETE;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_NEW;


/**
 * @author: aigupta
 */
public abstract class NativeFilterPolicy extends NativeObject
{
  @JniClass(name="JNIBloomFilter", flags={CPP, STRUCT})
  private static class FilterJNI
  {
    static {
      NativeDB.LIBRARY.load();
      init();
    }

    @JniMethod(flags={CPP_NEW})
    public static final native long create();
    @JniMethod(flags={CPP_DELETE})
    public static final native void delete(long ptr);

    public static final native void memmove (
        @JniArg(cast="void *") long dest,
        @JniArg(cast="const void *", flags={NO_OUT, CRITICAL}) FilterJNI src,
        @JniArg(cast="size_t") long size);

    public static final native void memmove (
        @JniArg(cast="void *", flags={NO_IN, CRITICAL}) FilterJNI dest,
        @JniArg(cast="const void *") long src,
        @JniArg(cast="size_t") long size);

    @JniField(cast="rocksdb::FilterPolicy*", flags={POINTER_FIELD})
    long filterPolicy;

    @JniField(cast="jint", flags={POINTER_FIELD})
    int bits_per_key;

    @JniMethod(flags={CONSTANT_INITIALIZER})
    private static final native void init();

    @JniMethod(cast="rocksdb::FilterPolicy *", accessor="rocksdb::NewBloomFilterPolicy")
    public static final native long NewBloomFilterPolicy(
        @JniArg int bits_per_key);

    @JniField(flags={CONSTANT}, accessor="sizeof(struct JNIBloomFilter)")
    static int SIZEOF;

    @JniField(cast="const char *")
    long name;
  }

  private long globalRef;
  private NativeBuffer name_buffer;

  public NativeFilterPolicy(int bits_per_key)
  {
    super(FilterJNI.create());
    try
    {
      name_buffer = NativeBuffer.create(name());
      globalRef = NativeDB.DBJNI.NewGlobalRef(this);
      if( globalRef==0 ) {
        throw new RuntimeException("jni call failed: NewGlobalRef");
      }

      FilterJNI filterJNI = new FilterJNI();
      filterJNI.filterPolicy = 0;
      filterJNI.bits_per_key = bits_per_key;
      filterJNI.name = name_buffer.pointer();

      FilterJNI.memmove(self, filterJNI, FilterJNI.SIZEOF);
    }
    catch (RuntimeException e) {
      delete();
      throw e;
    }
  }

  public void delete() {
    if( globalRef!=0 ) {
      NativeDB.DBJNI.DeleteGlobalRef(globalRef);
      globalRef = 0;
    }
  }

  public abstract String name();
}
