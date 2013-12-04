package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.*;

import static org.fusesource.hawtjni.runtime.FieldFlag.*;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;
import static org.fusesource.hawtjni.runtime.ArgFlag.*;
import static org.fusesource.hawtjni.runtime.ClassFlag.*;


/**
 * @author Ankit Gupta
 */
public abstract class NativeCompactionFilter extends NativeObject
{
  @JniClass(name="JNICompactionFilter", flags={STRUCT, CPP})
  static public class CompactionFilterJNI {

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
        @JniArg(cast="const void *", flags={NO_OUT, CRITICAL}) CompactionFilterJNI src,
        @JniArg(cast="size_t") long size);

    public static final native void memmove (
        @JniArg(cast="void *", flags={NO_IN, CRITICAL}) CompactionFilterJNI dest,
        @JniArg(cast="const void *") long src,
        @JniArg(cast="size_t") long size);

    @JniField(cast="jobject", flags={POINTER_FIELD})
    long target;

    @JniField(cast="jmethodID", flags={POINTER_FIELD})
    long filter_method;

    @JniField(cast="const char *")
    long name;

    @JniMethod(flags={CONSTANT_INITIALIZER})
    private static final native void init();

    @JniField(flags={CONSTANT}, accessor="sizeof(struct JNICompactionFilter)")
    static int SIZEOF;

  }

  private NativeBuffer name_buffer;
  private long globalRef;

  public NativeCompactionFilter() {
    super(CompactionFilterJNI.create());
    try {
      name_buffer = NativeBuffer.create(name());
      globalRef = NativeDB.DBJNI.NewGlobalRef(this);
      if( globalRef==0 ) {
        throw new RuntimeException("jni call failed: NewGlobalRef");
      }
      CompactionFilterJNI struct = new CompactionFilterJNI();
      struct.filter_method = NativeDB.DBJNI.GetMethodID(this.getClass(), "filter", "(JJ)Z");
      if( struct.filter_method==0 ) {
        throw new RuntimeException("jni call failed: GetMethodID");
      }
      struct.target = globalRef;
      struct.name = name_buffer.pointer();
      CompactionFilterJNI.memmove(self, struct, CompactionFilterJNI.SIZEOF);

    } catch (RuntimeException e) {
      delete();
      throw e;
    }
  }

  NativeCompactionFilter(long ptr) {
    super(ptr);
  }

  public void delete() {
    if( name_buffer!=null ) {
      name_buffer.delete();
      name_buffer = null;
    }
    if( globalRef!=0 ) {
      NativeDB.DBJNI.DeleteGlobalRef(globalRef);
      globalRef = 0;
    }
  }

  private boolean filter(long ptr1, long ptr2) {
    NativeSlice s1 = new NativeSlice();
    s1.read(ptr1, 0);
    NativeSlice s2 = new NativeSlice();
    s2.read(ptr2, 0);
    return filter(s1.toByteArray(), s2.toByteArray());
  }

  public abstract boolean filter(byte[] key, byte[] existingValue);
  public abstract String name();
}