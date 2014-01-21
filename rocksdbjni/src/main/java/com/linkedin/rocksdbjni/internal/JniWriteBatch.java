package com.linkedin.rocksdbjni.internal;

import com.linkedin.rocksdbjni.DBWriteBatch;

public class JniWriteBatch implements DBWriteBatch
{

  private final NativeWriteBatch writeBatch;

  JniWriteBatch(NativeWriteBatch writeBatch)
  {
    this.writeBatch = writeBatch;
  }

  public void close()
  {
    writeBatch.delete();
  }

  public DBWriteBatch put(byte[] key, byte[] value)
  {
    writeBatch.put(key, value);
    return this;
  }

  public DBWriteBatch delete(byte[] key)
  {
    writeBatch.delete(key);
    return this;
  }

  public NativeWriteBatch writeBatch()
  {
    return writeBatch;
  }
}
