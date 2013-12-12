package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniField;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.FieldFlag.SHARED_PTR;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_NEW;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_DELETE;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_METHOD;

@JniClass(name="rocksdb::DBStatistics", flags={CPP})
public class NativeStatistics {

  static {
    NativeDB.LIBRARY.load();
  }
  
  @JniMethod(flags={CPP_NEW})
  public static final native long create();

  @JniMethod(flags={CPP_DELETE})
  public static final native void delete(long self);

  @JniMethod(flags={CPP_METHOD})
  public static final native long getTickerCount(long self, @JniArg(cast="rocksdb::Tickers") int tickerType);
}
