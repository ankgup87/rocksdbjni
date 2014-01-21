package com.linkedin.rocksdbjni.internal;

/**
 * Provides a java interface to the C++ leveldb::CompressionType enum.
 */
public enum NativeCompressionType
{
  kNoCompression(0x0), kSnappyCompression(0x1);

  static final int t = kNoCompression.value;
  final int value;

  NativeCompressionType(int value)
  {
    this.value = value;
  }
}
