package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniField;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;

@JniClass(name="rocksdb::HistogramData", flags={STRUCT})
public class NativeHistogramData {
  public double median;
  public double percentile95;
  public double percentile99;
  public double average;
  public double standard_deviation;

  public double getMedian() {
    return median;
  }

  public double getPercentile95() {
    return percentile95;
  }

  public double getPercentile99() {
    return percentile99;
  }

  public double getAverage() {
    return average;
  }

  public double getStandardDeviation() {
    return standard_deviation;
  }
}
