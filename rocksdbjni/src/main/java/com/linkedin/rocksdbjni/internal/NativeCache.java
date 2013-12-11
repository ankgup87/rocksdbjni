
package com.linkedin.rocksdbjni.internal;

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
import static org.fusesource.hawtjni.runtime.FieldFlag.SHARED_PTR;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

/**
 * Provides a java interface to the C++ leveldb::Cache class.
 *
 * @author Ankit Gupta
 */
public class NativeCache extends NativeObject {

  @JniClass(name="JNILRUCache", flags={CPP, STRUCT})
  private static class CacheJNI {
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
        @JniArg(cast="const void *", flags={NO_OUT, CRITICAL}) CacheJNI src,
        @JniArg(cast="size_t") long size);

    public static final native void memmove (
        @JniArg(cast="void *", flags={NO_IN, CRITICAL}) CacheJNI dest,
        @JniArg(cast="const void *") long src,
        @JniArg(cast="size_t") long size);

    @JniField(cast="rocksdb::Cache*", flags={SHARED_PTR})
    long lruCache;

    @JniField(cast="jlong")
    long size;

    @JniMethod(flags={CONSTANT_INITIALIZER})
    private static final native void init();

    @JniMethod(cast="rocksdb::Cache *", accessor="rocksdb::NewLRUCache", flags={SHARED_PTR_CAST})
    public static final native long NewLRUCache(
        @JniArg(cast="size_t") long capacity);

    @JniField(flags={CONSTANT}, accessor="sizeof(struct JNILRUCache)")
    static int SIZEOF;
  }

  private volatile long globalRef;

  public NativeCache(long capacity, int numShardBits) {
    super(CacheJNI.create());
    try
    {
      globalRef = NativeDB.DBJNI.NewGlobalRef(this);
      if( globalRef==0 ) {
        throw new RuntimeException("jni call failed: NewGlobalRef");
      }

      CacheJNI cacheJNI = new CacheJNI();
      cacheJNI.lruCache = 0;
      cacheJNI.size = capacity;

      CacheJNI.memmove(self, cacheJNI, CacheJNI.SIZEOF);
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
}
