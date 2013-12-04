
package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniField;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;

/**
 * Provides a java interface to the C++ leveldb::ReadOptions class.
 *
 * @author Ankit Gupta
 */
@JniClass(name="rocksdb::ReadOptions", flags={STRUCT, CPP})
public class NativeReadOptions {

  @JniField
  private boolean verify_checksums = false;

  @JniField
  private boolean fill_cache = true;

  @JniField(cast="const rocksdb::Snapshot*")
  private long snapshot=0;

  public boolean fillCache() {
    return fill_cache;
  }

  public NativeReadOptions fillCache(boolean fill_cache) {
    this.fill_cache = fill_cache;
    return this;
  }

  public NativeSnapshot snapshot() {
    if( snapshot == 0 ) {
      return null;
    } else {
      return new NativeSnapshot(snapshot);
    }
  }

  public NativeReadOptions snapshot(NativeSnapshot snapshot) {
    if( snapshot==null ) {
      this.snapshot = 0;
    } else {
      this.snapshot = snapshot.pointer();
    }
    return this;
  }

  public boolean verifyChecksums() {
    return verify_checksums;
  }

  public NativeReadOptions verifyChecksums(boolean verify_checksums) {
    this.verify_checksums = verify_checksums;
    return this;
  }
}