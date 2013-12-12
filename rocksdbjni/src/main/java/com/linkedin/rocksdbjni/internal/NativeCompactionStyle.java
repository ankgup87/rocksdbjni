package com.linkedin.rocksdbjni.internal;


/**
 * @author: aigupta
 */
public enum  NativeCompactionStyle
{
  kCompactionStyleLevel(0x0), kCompactionStyleUniversal(0x1);

  static final int t = kCompactionStyleLevel.value;
  final int value;

  NativeCompactionStyle(int value) {
    this.value = value;
  }
}
