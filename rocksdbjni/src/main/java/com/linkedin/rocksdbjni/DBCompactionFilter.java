package com.linkedin.rocksdbjni;

public interface DBCompactionFilter
{
  public boolean filter(byte[] key, byte[] existingValue);

  public String name();
}
