package com.linkedin.rocksdbjni.internal;

import com.linkedin.rocksdbjni.Snapshot;

/**
 * @author Ankit Gupta
 */
public class JniSnapshot implements Snapshot
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
