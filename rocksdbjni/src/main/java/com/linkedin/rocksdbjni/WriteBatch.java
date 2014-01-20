package com.linkedin.rocksdbjni;

import java.io.Closeable;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public interface WriteBatch extends Closeable
{

  public WriteBatch put(byte[] key, byte[] value);

  public WriteBatch delete(byte[] key);
}
