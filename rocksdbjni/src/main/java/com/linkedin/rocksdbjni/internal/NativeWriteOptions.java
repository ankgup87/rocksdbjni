package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniField;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;

/**
 * Provides a java interface to the C++ leveldb::WriteOptions class.
 */
@JniClass(name = "rocksdb::WriteOptions", flags = { STRUCT, CPP })
public class NativeWriteOptions
{

  @JniField
  boolean sync;

  @JniField
  boolean disableWAL;

  public boolean sync()
  {
    return sync;
  }

  public boolean disableWAL()
  {
    return disableWAL;
  }

  public NativeWriteOptions sync(boolean sync)
  {
    this.sync = sync;
    return this;
  }

  public NativeWriteOptions disableWAL(boolean disableWAL)
  {
    this.disableWAL = disableWAL;
    return this;
  }
}
