package com.linkedin.rocksdbjni.internal;

/**
 * Provides a java interface to the C++ leveldb::Snapshot class.
 */
public class NativeSnapshot extends NativeObject
{

  NativeSnapshot(long self)
  {
    super(self);
  }

}
