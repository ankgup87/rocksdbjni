package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

@JniClass(name = "rocksdb::Env", flags = { CPP })
public class NativeEnv
{

  static
  {
    NativeDB.LIBRARY.load();
  }

  @JniMethod(cast = "rocksdb::Env*", accessor = "rocksdb::Env::Default")
  public static final native long Default();

  @JniMethod(flags = { CPP_METHOD }, accessor = "rocksdb::Env::SetBackgroundThreads")
  public static final native void setNumBackgroundThreads(long self, int num, @JniArg(cast="rocksdb::Env::Priority") int pri);
}
