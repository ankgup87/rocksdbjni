package com.linkedin.rocksdbjni;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Range
{

  final private byte[] start;
  final private byte[] limit;

  public byte[] limit()
  {
    return limit;
  }

  public byte[] start()
  {
    return start;
  }

  public Range(byte[] start, byte[] limit)
  {
    Utils.checkArgNotNull(start, "start");
    Utils.checkArgNotNull(limit, "limit");
    this.limit = limit;
    this.start = start;
  }

}
