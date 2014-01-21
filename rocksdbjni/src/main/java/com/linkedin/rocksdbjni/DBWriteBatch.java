package com.linkedin.rocksdbjni;

import java.io.Closeable;

public interface DBWriteBatch extends Closeable
{

  public DBWriteBatch put(byte[] key, byte[] value);

  public DBWriteBatch delete(byte[] key);
}
