package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

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

  @JniMethod(flags={CPP_METHOD})
  public static final native void histogramData(long self, @JniArg(cast="rocksdb::Histograms") int histogramType, NativeHistogramData histogramData);
}
