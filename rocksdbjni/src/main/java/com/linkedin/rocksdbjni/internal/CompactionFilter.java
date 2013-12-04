package com.linkedin.rocksdbjni.internal;


/**
 * @author: Ankit Gupta
 */
public interface CompactionFilter
{
  public boolean filter(byte[] key, byte[] existingValue);
  public String name();
}
