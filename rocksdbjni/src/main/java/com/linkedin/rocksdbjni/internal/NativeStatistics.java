package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

// Ideally we should be mapping this to rocksdb::Statistics and use
// std::shared_ptr<Statistics> CreateDBStatistics(); from statistics.h
// But we are not doing so, because there is a bug in casting shared_ptr 
// to and from the native system.
@JniClass(name = "rocksdb::DBStatistics", flags = { CPP })
public class NativeStatistics
{

  static
  {
    NativeDB.LIBRARY.load();
  }

  @JniMethod(flags = { CPP_NEW })
  public static final native long create();

  @JniMethod(flags = { CPP_DELETE })
  public static final native void delete(long self);

  @JniMethod(flags = { CPP_METHOD })
  public static final native long getTickerCount(long self, @JniArg(cast = "rocksdb::Tickers") int tickerType);

  @JniMethod(flags = { CPP_METHOD })
  public static final native void histogramData(long self,
                                                @JniArg(cast = "rocksdb::Histograms") int histogramType,
                                                NativeHistogramData histogramData);
}
