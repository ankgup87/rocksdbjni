/*
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 *
 *     http://fusesource.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *    * Neither the name of FuseSource Corp. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import static org.fusesource.hawtjni.runtime.FieldFlag.SHARED_PTR;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

/**
 * Provides a java interface to the C++ leveldb::Cache class.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
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

    @JniField(cast="jlong", flags={POINTER_FIELD})
    long size;

    @JniField(cast="jint", flags={POINTER_FIELD})
    long num_shard_bits;

    @JniMethod(flags={CONSTANT_INITIALIZER})
    private static final native void init();

    @JniMethod(cast="rocksdb::Cache *", accessor="rocksdb::NewLRUCache", flags={SHARED_PTR_CAST})
    public static final native long NewLRUCache(
        @JniArg(cast="size_t") long capacity);

    @JniField(flags={CONSTANT}, accessor="sizeof(struct JNILRUCache)")
    static int SIZEOF;
  }

  private long globalRef;

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
      cacheJNI.num_shard_bits = numShardBits;

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
