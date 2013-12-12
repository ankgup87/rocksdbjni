package com.linkedin.rocksdbjni.internal;


/**
 * @author: aigupta
 */
public enum CompactionStyle
{
  LEVEL(0x00),
  UNIVERSAL(0x01);

  public static CompactionStyle getCompressionTypeByPersistentId(int persistentId) {
    for (CompactionStyle compactionStyle : CompactionStyle.values()) {
      if (compactionStyle.persistentId == persistentId) {
        return compactionStyle;
      }
    }
    throw new IllegalArgumentException("Unknown persistentId " + persistentId);
  }

  private final int persistentId;

  CompactionStyle(int persistentId)
  {
    this.persistentId = persistentId;
  }

  public int persistentId()
  {
    return persistentId;
  }
}
