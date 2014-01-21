package com.linkedin.rocksdbjni.internal;

import com.linkedin.rocksdbjni.DBSnapshot;

public class ReadOptions
{
  private boolean verifyChecksums = false;
  private boolean fillCache = true;
  private DBSnapshot snapshot;

  public DBSnapshot snapshot()
  {
    return snapshot;
  }

  public ReadOptions snapshot(DBSnapshot snapshot)
  {
    this.snapshot = snapshot;
    return this;
  }

  public boolean fillCache()
  {
    return fillCache;
  }

  public ReadOptions fillCache(boolean fillCache)
  {
    this.fillCache = fillCache;
    return this;
  }

  public boolean verifyChecksums()
  {
    return verifyChecksums;
  }

  public ReadOptions verifyChecksums(boolean verifyChecksums)
  {
    this.verifyChecksums = verifyChecksums;
    return this;
  }
}
