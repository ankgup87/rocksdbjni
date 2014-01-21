package com.linkedin.rocksdbjni.internal;

import com.linkedin.rocksdbjni.DBCompactionFilter;
import com.linkedin.rocksdbjni.DBComparator;
import com.linkedin.rocksdbjni.DBFilterPolicy;
import com.linkedin.rocksdbjni.DBLogger;
import com.linkedin.rocksdbjni.DBMergeOperator;
import com.linkedin.rocksdbjni.Utils;
import com.linkedin.rocksdbjni.types.CompactionStyle;
import com.linkedin.rocksdbjni.types.CompressionType;

public class Options
{

  private boolean createIfMissing = true;
  private boolean errorIfExists;
  private int writeBufferSize = 4 << 20;

  private int maxOpenFiles = 1000;
  private int maxWriteBufferNumber = 2;

  private int blockRestartInterval = 16;
  private int blockSize = 4 * 1024;
  private CompressionType compressionType = CompressionType.NONE;
  private boolean verifyChecksums = true;
  private boolean paranoidChecks = false;
  private DBComparator comparator;
  private DBLogger logger = null;
  private NativeCache nativeCache;

  private DBCompactionFilter compactionFilter;
  private DBMergeOperator _mergeOperator;
  private boolean disableSeekCompaction = false;
  private int numLevels = 7;
  private int level0FileNumCompactionTrigger = 4;
  private int level0StopWritesTrigger = 12;
  private int targetFileSizeBase = 2 * 1048576;
  private long maxBytesForLevelBase = 10 * 1048576;
  private int sourceCompactionFactor = 1;
  private int maxGrandparentOverlapFactor = 10;
  private int maxBackgroundCompactions = 1;
  private boolean disableAutoCompactions = false;
  private long deleteObsoleteFilesPeriodMicros = 6 * 60 * 60 * 1000000;
  private int level0SlowdownWritesTrigger = 8;
  private long maxManifestFileSize = Integer.MAX_VALUE - 1;
  private DBFilterPolicy filterPolicy;
  private CompactionStyle _compactionStyle = CompactionStyle.LEVEL;
  private long envPtr = 0;

  public boolean createIfMissing()
  {
    return createIfMissing;
  }

  public Options createIfMissing(boolean createIfMissing)
  {
    this.createIfMissing = createIfMissing;
    return this;
  }

  public boolean errorIfExists()
  {
    return errorIfExists;
  }

  public Options disableSeekCompaction(boolean disableSeekCompaction)
  {
    this.disableSeekCompaction = disableSeekCompaction;
    return this;
  }

  public boolean disableSeekCompaction()
  {
    return disableSeekCompaction;
  }

  public Options disableAutoCompactions(boolean disableAutoCompactions)
  {
    this.disableAutoCompactions = disableAutoCompactions;
    return this;
  }

  public boolean disableAutoCompactions()
  {
    return disableAutoCompactions;
  }

  public Options errorIfExists(boolean errorIfExists)
  {
    this.errorIfExists = errorIfExists;
    return this;
  }

  public int writeBufferSize()
  {
    return writeBufferSize;
  }

  public Options writeBufferSize(int writeBufferSize)
  {
    this.writeBufferSize = writeBufferSize;
    return this;
  }

  public int maxOpenFiles()
  {
    return maxOpenFiles;
  }

  public Options maxOpenFiles(int maxOpenFiles)
  {
    this.maxOpenFiles = maxOpenFiles;
    return this;
  }

  public long maxBytesForLevelBase()
  {
    return maxBytesForLevelBase;
  }

  public Options level0SlowdownWritesTrigger(int level0SlowdownWritesTrigger)
  {
    this.level0SlowdownWritesTrigger = level0SlowdownWritesTrigger;
    return this;
  }

  public int level0SlowdownWritesTrigger()
  {
    return level0SlowdownWritesTrigger;
  }

  public Options maxManifestFileSize(long maxManifestFileSize)
  {
    this.maxManifestFileSize = maxManifestFileSize;
    return this;
  }

  public long maxManifestFileSize()
  {
    return maxManifestFileSize;
  }

  public Options deleteObsoleteFilesPeriodMicros(long deleteObsoleteFilesPeriodMicros)
  {
    this.deleteObsoleteFilesPeriodMicros = deleteObsoleteFilesPeriodMicros;
    return this;
  }

  public long deleteObsoleteFilesPeriodMicros()
  {
    return deleteObsoleteFilesPeriodMicros;
  }

  public Options maxBytesForLevelBase(long maxBytesForLevelBase)
  {
    this.maxBytesForLevelBase = maxBytesForLevelBase;
    return this;
  }

  public int sourceCompactionFactor()
  {
    return sourceCompactionFactor;
  }

  public Options sourceCompactionFactor(int sourceCompactionFactor)
  {
    this.sourceCompactionFactor = sourceCompactionFactor;
    return this;
  }

  public int maxBackgroundCompactions()
  {
    return maxBackgroundCompactions;
  }

  public Options maxBackgroundCompactions(int maxBackgroundCompactions)
  {
    this.maxBackgroundCompactions = maxBackgroundCompactions;
    return this;
  }

  public int maxGrandparentOverlapFactor()
  {
    return maxGrandparentOverlapFactor;
  }

  public Options maxGrandparentOverlapFactor(int maxGrandparentOverlapFactor)
  {
    this.maxGrandparentOverlapFactor = maxGrandparentOverlapFactor;
    return this;
  }

  public int numLevels()
  {
    return numLevels;
  }

  public Options numLevels(int numLevels)
  {
    this.numLevels = numLevels;
    return this;
  }

  public int level0FileNumCompactionTrigger()
  {
    return level0FileNumCompactionTrigger;
  }

  public Options level0FileNumCompactionTrigger(int level0FileNumCompactionTrigger)
  {
    this.level0FileNumCompactionTrigger = level0FileNumCompactionTrigger;
    return this;
  }

  public int targetFileSizeBase()
  {
    return targetFileSizeBase;
  }

  public Options targetFileSizeBase(int targetFileSizeBase)
  {
    this.targetFileSizeBase = targetFileSizeBase;
    return this;
  }

  public int level0StopWritesTrigger()
  {
    return level0StopWritesTrigger;
  }

  public Options level0StopWritesTrigger(int level0StopWritesTrigger)
  {
    this.level0StopWritesTrigger = level0StopWritesTrigger;
    return this;
  }

  public int maxWriteBufferNumber()
  {
    return maxWriteBufferNumber;
  }

  public Options maxWriteBufferNumber(int maxWriteBufferNumber)
  {
    this.maxWriteBufferNumber = maxWriteBufferNumber;
    return this;
  }

  public int blockRestartInterval()
  {
    return blockRestartInterval;
  }

  public Options blockRestartInterval(int blockRestartInterval)
  {
    this.blockRestartInterval = blockRestartInterval;
    return this;
  }

  public int blockSize()
  {
    return blockSize;
  }

  public Options blockSize(int blockSize)
  {
    this.blockSize = blockSize;
    return this;
  }

  public CompressionType compressionType()
  {
    return compressionType;
  }

  public Options compressionType(CompressionType compressionType)
  {
    Utils.checkArgNotNull(compressionType, "compressionType");
    this.compressionType = compressionType;
    return this;
  }

  public CompactionStyle compactionStyle()
  {
    return _compactionStyle;
  }

  public Options compactionStyle(CompactionStyle compactionStyle)
  {
    Utils.checkArgNotNull(compactionStyle, "CompactionStyle");
    _compactionStyle = compactionStyle;
    return this;
  }

  public boolean verifyChecksums()
  {
    return verifyChecksums;
  }

  public Options verifyChecksums(boolean verifyChecksums)
  {
    this.verifyChecksums = verifyChecksums;
    return this;
  }

  public NativeCache nativeCache()
  {
    return nativeCache;
  }

  public Options nativeCache(NativeCache nativeCache)
  {
    this.nativeCache = nativeCache;
    return this;
  }

  public DBFilterPolicy filterPolicy()
  {
    return filterPolicy;
  }

  public Options filterPolicy(DBFilterPolicy filterPolicy)
  {
    this.filterPolicy = filterPolicy;
    return this;
  }

  public DBComparator comparator()
  {
    return comparator;
  }

  public Options comparator(DBComparator comparator)
  {
    this.comparator = comparator;
    return this;
  }

  public DBMergeOperator mergeOperator()
  {
    return _mergeOperator;
  }

  public Options mergeOperator(DBMergeOperator mergeOperator)
  {
    _mergeOperator = mergeOperator;
    return this;
  }

  public DBCompactionFilter compactionFilter()
  {
    return compactionFilter;
  }

  public Options compactionFilter(DBCompactionFilter compactionFilter)
  {
    this.compactionFilter = compactionFilter;
    return this;
  }

  public DBLogger logger()
  {
    return logger;
  }

  public Options logger(DBLogger logger)
  {
    this.logger = logger;
    return this;
  }

  public boolean paranoidChecks()
  {
    return paranoidChecks;
  }

  public Options paranoidChecks(boolean paranoidChecks)
  {
    this.paranoidChecks = paranoidChecks;
    return this;
  }

  public long envPtr()
  {
    return envPtr;
  }

  public Options envPtr(long ptr)
  {
    envPtr = ptr;
    return this;
  }
}
