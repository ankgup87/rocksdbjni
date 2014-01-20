package com.linkedin.rocksdbjni;

import java.util.Comparator;

/**
 */
public interface DBComparator extends Comparator<byte[]>
{

  public String name();

  /**
   * If <code>start < limit</code>, returns a short key in [start,limit). Simple comparator
   * implementations should return start unchanged,
   * 
   * @param start
   * @param limit
   * @return
   */
  byte[] findShortestSeparator(byte[] start, byte[] limit);

  /**
   * returns a 'short key' where the 'short key' >= key. Simple comparator implementations should
   * return key unchanged,
   * 
   * @param key
   */
  byte[] findShortSuccessor(byte[] key);

}
