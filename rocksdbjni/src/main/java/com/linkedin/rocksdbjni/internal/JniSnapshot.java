package com.linkedin.rocksdbjni.internal;

import com.linkedin.rocksdbjni.DBSnapshot;

public class JniSnapshot implements DBSnapshot
{

  private final NativeDB db;
  private final NativeSnapshot snapshot;

  JniSnapshot(NativeDB db, NativeSnapshot snapshot)
  {
    this.db = db;
    this.snapshot = snapshot;
  }

  public void close()
  {
    db.releaseSnapshot(snapshot);
  }

  NativeSnapshot snapshot()
  {
    return snapshot;
  }
}
