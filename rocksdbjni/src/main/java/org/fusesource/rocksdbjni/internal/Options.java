package org.fusesource.rocksdbjni.internal;


/**
 * @author: aigupta
 */
/**
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DBComparator;
import org.iq80.leveldb.Logger;


public class Options {

  private boolean createIfMissing = true;
  private boolean errorIfExists;
  private int writeBufferSize = 4 << 20;

  private int maxOpenFiles = 1000;
  private int maxWriteBufferNumber = 2;

  private int blockRestartInterval = 16;
  private int blockSize = 4 * 1024;
  private CompressionType compressionType = CompressionType.SNAPPY;
  private boolean verifyChecksums = true;
  private boolean paranoidChecks = false;
  private DBComparator comparator;
  private Logger logger = null;
  private long cacheSize;

  private CompactionFilter compactionFilter;
  private MergeOperator _mergeOperator;
  private boolean disableSeekCompaction = false;
  private int numLevels = 7;
  private int level0FileNumCompactionTrigger = 4;
  private int level0StopWritesTrigger = 12;
  private int targetFileSizeBase = 2 * 1048576;
  private int maxBytesForLevelBase = 10 * 1048576;
  private int sourceCompactionFactor = 1;
  private int maxGrandparentOverlapFactor = 10;
  private int maxBackgroundCompactions = 1;
  private boolean disableAutoCompactions = false;
  private long deleteObsoleteFilesPeriodMicros = 6 * 60 * 60 * 1000000;
  private int level0SlowdownWritesTrigger = 8;
  private FilterPolicy filterPolicy;

  static void checkArgNotNull(Object value, String name) {
    if(value==null) {
      throw new IllegalArgumentException("The "+name+" argument cannot be null");
    }
  }

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

  public int maxBytesForLevelBase()
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

  public Options deleteObsoleteFilesPeriodMicros(long deleteObsoleteFilesPeriodMicros)
  {
    this.deleteObsoleteFilesPeriodMicros = deleteObsoleteFilesPeriodMicros;
    return this;
  }

  public long deleteObsoleteFilesPeriodMicros()
  {
    return deleteObsoleteFilesPeriodMicros;
  }

  public Options maxBytesForLevelBase(int maxBytesForLevelBase)
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
    checkArgNotNull(compressionType, "compressionType");
    this.compressionType = compressionType;
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


  public long cacheSize() {
    return cacheSize;
  }

  public Options cacheSize(long cacheSize) {
    this.cacheSize = cacheSize;
    return this;
  }

  public FilterPolicy filterPolicy() {
    return filterPolicy;
  }

  public Options filterPolicy(FilterPolicy filterPolicy) {
    this.filterPolicy = filterPolicy;
    return this;
  }

  public DBComparator comparator() {
    return comparator;
  }

  public Options comparator(DBComparator comparator) {
    this.comparator = comparator;
    return this;
  }

  public MergeOperator mergeOperator() {
    return _mergeOperator;
  }

  public Options mergeOperator(MergeOperator mergeOperator) {
    _mergeOperator = mergeOperator;
    return this;
  }

  public CompactionFilter compactionFilter() {
    return compactionFilter;
  }

  public Options compactionFilter(CompactionFilter compactionFilter) {
    compactionFilter = compactionFilter;
    return this;
  }

  public Logger logger() {
    return logger;
  }

  public Options logger(Logger logger) {
    this.logger = logger;
    return this;
  }

  public boolean paranoidChecks() {
    return paranoidChecks;
  }

  public Options paranoidChecks(boolean paranoidChecks) {
    this.paranoidChecks = paranoidChecks;
    return this;
  }
}


