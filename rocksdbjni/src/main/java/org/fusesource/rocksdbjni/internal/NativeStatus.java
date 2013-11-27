/*
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 *
 *     http://fusesource.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *    * Neither the name of FuseSource Corp. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.fusesource.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_DELETE;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_METHOD;

/**
 * Provides a java interface to the C++ leveldb::Status class.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
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
