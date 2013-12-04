
package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ArgFlag.BY_VALUE;
import static org.fusesource.hawtjni.runtime.ArgFlag.NO_OUT;
import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

/**
 * Provides a java interface to the C++ leveldb::WriteBatch class.
 *
 * @author Ankit Gupta
 */
public class NativeWriteBatch extends NativeObject {

  @JniClass(name="rocksdb::WriteBatch", flags={CPP})
  private static class WriteBatchJNI {
    static {
      NativeDB.LIBRARY.load();
    }

    @JniMethod(flags={CPP_NEW})
    public static final native long create();
    @JniMethod(flags={CPP_DELETE})
    public static final native void delete(
        long self);

    @JniMethod(flags={CPP_METHOD})
    static final native void Put(
        long self,
        @JniArg(flags={BY_VALUE, NO_OUT}) NativeSlice key,
        @JniArg(flags={BY_VALUE, NO_OUT}) NativeSlice value
    );

    @JniMethod(flags={CPP_METHOD})
    static final native void Merge(
        long self,
        @JniArg(flags={BY_VALUE, NO_OUT}) NativeSlice key,
        @JniArg(flags={BY_VALUE, NO_OUT}) NativeSlice value
    );

    @JniMethod(flags={CPP_METHOD})
    static final native void Delete(
        long self,
        @JniArg(flags={BY_VALUE, NO_OUT}) NativeSlice key
    );

    @JniMethod(flags={CPP_METHOD})
    static final native void PutLogData(
        long self,
        @JniArg(flags={BY_VALUE, NO_OUT}) NativeSlice key
    );

    @JniMethod(flags={CPP_METHOD})
    static final native void Clear(
        long self
    );

  }

  public NativeWriteBatch() {
    super(WriteBatchJNI.create());
  }

  public void delete() {
    assertAllocated();
    WriteBatchJNI.delete(self);
    self = 0;
  }

  public void put(byte[] key, byte[] value) {
    NativeDB.checkArgNotNull(key, "key");
    NativeDB.checkArgNotNull(value, "value");
    NativeBuffer keyBuffer = NativeBuffer.create(key);
    try {
      NativeBuffer valueBuffer = NativeBuffer.create(value);
      try {
        put(keyBuffer, valueBuffer);
      } finally {
        valueBuffer.delete();
      }
    } finally {
      keyBuffer.delete();
    }
  }

  public void merge(byte[] key, byte[] value) {
    NativeDB.checkArgNotNull(key, "key");
    NativeDB.checkArgNotNull(value, "value");
    NativeBuffer keyBuffer = NativeBuffer.create(key);
    try {
      NativeBuffer valueBuffer = NativeBuffer.create(value);
      try {
        merge(keyBuffer, valueBuffer);
      } finally {
        valueBuffer.delete();
      }
    } finally {
      keyBuffer.delete();
    }
  }

  public void putLogData(byte[] blob) {
    NativeDB.checkArgNotNull(blob, "blob");
    NativeBuffer blobBuffer = NativeBuffer.create(blob);
    try {
      putLogData(blobBuffer);
    } finally {
      blobBuffer.delete();
    }
  }

  private void putLogData(NativeBuffer blobBuffer) {
    putLogData(new NativeSlice(blobBuffer));
  }

  private void merge(NativeBuffer keyBuffer, NativeBuffer valueBuffer) {
    merge(new NativeSlice(keyBuffer), new NativeSlice(valueBuffer));
  }

  private void put(NativeBuffer keyBuffer, NativeBuffer valueBuffer) {
    put(new NativeSlice(keyBuffer), new NativeSlice(valueBuffer));
  }

  private void put(NativeSlice keySlice, NativeSlice valueSlice) {
    assertAllocated();
    WriteBatchJNI.Put(self, keySlice, valueSlice);
  }

  private void merge(NativeSlice keySlice, NativeSlice valueSlice) {
    assertAllocated();
    WriteBatchJNI.Merge(self, keySlice, valueSlice);
  }

  private void putLogData(NativeSlice blobSlice) {
    assertAllocated();
    WriteBatchJNI.PutLogData(self, blobSlice);
  }


  public void delete(byte[] key) {
    NativeDB.checkArgNotNull(key, "key");
    NativeBuffer keyBuffer = NativeBuffer.create(key);
    try {
      delete(keyBuffer);
    } finally {
      keyBuffer.delete();
    }
  }

  private void delete(NativeBuffer keyBuffer) {
    delete(new NativeSlice(keyBuffer));
  }

  private void delete(NativeSlice keySlice) {
    assertAllocated();
    WriteBatchJNI.Delete(self, keySlice);
  }

  public void clear() {
    assertAllocated();
    WriteBatchJNI.Clear(self);
  }

}
