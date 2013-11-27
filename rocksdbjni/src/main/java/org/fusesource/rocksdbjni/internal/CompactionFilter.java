package org.fusesource.rocksdbjni.internal;


/**
 * @author: aigupta
 */
public interface CompactionFilter
{
  public boolean filter(byte[] key, byte[] existingValue);
  public String name();
}
