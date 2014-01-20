package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

@JniClass(name = "rocksdb::HdfsEnv", flags = { CPP })
public class NativeHdfsEnv extends NativeEnv
{

  static
  {
    NativeDB.LIBRARY.load();
  }

  // The arg String will be typecasted to char*. If this ran into a bug,
  // we can map a java NativeString class to C++ string class.
  // NativeString.java will have a constructor that will accept a String object.
  // It will be mapped to std::string class's constructor that accepts a char* and returns
  // std::string pointer.
  // Pass this pointer to create() method below and typecast the pointer to char*.
  @JniMethod(flags = { CPP_NEW })
  public static final native long create(String fsname);

  @JniMethod(flags = { CPP_DELETE })
  public static final native void delete(long self);
}
