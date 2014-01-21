package com.linkedin.rocksdbjni;

public class DBRange
{

  final private byte[] start;
  final private byte[] limit;

  public DBRange(byte[] start, byte[] limit)
  {
    Utils.checkArgNotNull(start, "start");
    Utils.checkArgNotNull(limit, "limit");
    this.limit = limit;
    this.start = start;
  }

  public byte[] limit()
  {
    return limit;
  }

  public byte[] start()
  {
    return start;
  }

}
