
package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_DELETE;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_METHOD;

/**
 * Provides a java interface to the C++ leveldb::Status class.
 *
 * @author Ankit Gupta
 */
class NativeStatus extends NativeObject{

  @JniClass(name="rocksdb::Status", flags={CPP})
  static class StatusJNI {
    static {
      NativeDB.LIBRARY.load();
    }

    @JniMethod(flags={CPP_DELETE})
    public static final native void delete(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean ok(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsCorruption(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsNotSupported(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsInvalidArgument(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsIOError(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsMergeInProgress(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsIncomplete(
        long self);

    @JniMethod(flags={CPP_METHOD})
    public static final native boolean IsNotFound(
        long self);

    @JniMethod(copy="std::string", flags={CPP_METHOD})
    public static final native long ToString(
        long self);
  }

  public NativeStatus(long self) {
    super(self);
  }

  public void delete() {
    assertAllocated();
    StatusJNI.delete(self);
    self = 0;
  }

  public boolean isOk() {
    assertAllocated();
    return StatusJNI.ok(self);
  }

  public boolean isCorruption() {
    assertAllocated();
    return StatusJNI.IsCorruption(self);
  }

  public boolean isNotSupported() {
    assertAllocated();
    return StatusJNI.IsNotSupported(self);
  }

  public boolean isInvalidArgument() {
    assertAllocated();
    return StatusJNI.IsInvalidArgument(self);
  }

  public boolean isIOError() {
    assertAllocated();
    return StatusJNI.IsIOError(self);
  }

  public boolean isMergeInProcess() {
    assertAllocated();
    return StatusJNI.IsMergeInProgress(self);
  }

  public boolean isIncomplete() {
    assertAllocated();
    return StatusJNI.IsIncomplete(self);
  }

  public boolean isNotFound() {
    assertAllocated();
    return StatusJNI.IsNotFound(self);
  }

  public String toString() {
    assertAllocated();
    long strptr = StatusJNI.ToString(self);
    if( strptr==0 ) {
      return null;
    } else {
      NativeStdString rc = new NativeStdString(strptr);
      try {
        return rc.toString();
      } finally {
        rc.delete();
      }
    }
  }

}
