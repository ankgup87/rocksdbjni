/*
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 *
 *     http://fusesource.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *    * Neither the name of FuseSource Corp. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.fusesource.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniField;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;
import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;
import static org.fusesource.hawtjni.runtime.FieldFlag.FIELD_SKIP;
import static org.fusesource.hawtjni.runtime.FieldFlag.POINTER_FIELD;
import static org.fusesource.hawtjni.runtime.FieldFlag.SHARED_PTR;
import static org.fusesource.hawtjni.runtime.MethodFlag.CONSTANT_INITIALIZER;

/**
 * Provides a java interface to the C++ leveldb::Options class.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
@JniClass(name="rocksdb::Options", flags={STRUCT, CPP})
public class NativeOptions {

  static {
    NativeDB.LIBRARY.load();
    init();
  }

  @JniMethod(flags={CONSTANT_INITIALIZER})
  private static final native void init();

  @JniField(flags={CONSTANT}, cast="Env*", accessor="rocksdb::Env::Default()")
  private static long DEFAULT_ENV;

  private boolean create_if_missing = false;
  private boolean error_if_exists = false;
  private boolean paranoid_checks = false;
  private boolean disable_seek_compaction = false;
  @JniField(cast="size_t")
  private long write_buffer_size = 4 << 20;
  @JniField(cast="size_t")
  private long block_size = 4086;
  private int max_open_files = 1000;
  private int block_restart_interval = 16;
  private int num_levels = 7;
  private int level0_file_num_compaction_trigger = 4;
  private int level0_stop_writes_trigger = 12;
  private int target_file_size_base = 2 * 1048576;
  private int max_bytes_for_level_base = 10 * 1048576;
  private int source_compaction_factor = 1;
  private int max_grandparent_overlap_factor = 10;
  private int max_background_compactions = 1;
  private boolean disable_auto_compactions = false;
  @JniField(cast="uint64_t")
  private long delete_obsolete_files_period_micros = 6 * 60 * 60 * 1000000;
  private int level0_slowdown_writes_trigger = 8;
  private int max_write_buffer_number = 2;

  @JniField(flags={FIELD_SKIP})
  private NativeComparator comparatorObject = NativeComparator.BYTEWISE_COMPARATOR;
  @JniField(cast="const rocksdb::Comparator*")
  private long comparator = comparatorObject.pointer();

  @JniField(flags={FIELD_SKIP})
  private NativeLogger infoLogObject = null;
  @JniField(flags={SHARED_PTR}, cast="rocksdb::Logger*")
  private long info_log = 0;

  @JniField(cast="rocksdb::Env*")
  private long env = DEFAULT_ENV;
  @JniField(flags={SHARED_PTR}, cast="rocksdb::Cache*")
  private long block_cache = 0;
  @JniField(flags={FIELD_SKIP})
  private NativeCache cache;

  @JniField(cast="rocksdb::CompressionType")
  private int compression = NativeCompressionType.kSnappyCompression.value;

  @JniField(flags={SHARED_PTR}, cast="rocksdb::MergeOperator*")
  private long merge_operator = 0;
  @JniField(flags={FIELD_SKIP})
  private NativeMergeOperator _mergeOperator = null;

  @JniField(cast="rocksdb::CompactionFilter*")
  private long compaction_filter = 0;
  @JniField(flags={FIELD_SKIP})
  private NativeCompactionFilter _compactionFilter = null;

  public NativeOptions createIfMissing(boolean value) {
    this.create_if_missing = value;
    return this;
  }
  public boolean createIfMissing() {
    return create_if_missing;
  }

  public NativeOptions errorIfExists(boolean value) {
    this.error_if_exists = value;
    return this;
  }
  public boolean errorIfExists() {
    return error_if_exists;
  }

  public NativeOptions level0SlowdownWritesTrigger(int value) {
    this.level0_slowdown_writes_trigger = value;
    return this;
  }
  public int level0SlowdownWritesTrigger() {
    return level0_slowdown_writes_trigger;
  }

  public NativeOptions disableAutoCompactions(boolean value) {
    this.disable_auto_compactions = value;
    return this;
  }
  public boolean disableAutoCompactions() {
    return disable_auto_compactions;
  }

  public NativeOptions deleteObsoleteFilesPeriodMicros(long value) {
    this.delete_obsolete_files_period_micros = value;
    return this;
  }
  public long deleteObsoleteFilesPeriodMicros() {
    return delete_obsolete_files_period_micros;
  }

  public NativeOptions paranoidChecks(boolean value) {
    this.paranoid_checks = value;
    return this;
  }
  public boolean paranoidChecks() {
    return paranoid_checks;
  }

  public NativeOptions disableSeekCompaction(boolean value) {
    this.disable_seek_compaction = value;
    return this;
  }
  public boolean disableSeekCompaction() {
    return disable_seek_compaction;
  }

  public NativeOptions writeBufferSize(long value) {
    this.write_buffer_size = value;
    return this;
  }
  public long writeBufferSize() {
    return write_buffer_size;
  }

  public NativeOptions maxOpenFiles(int value) {
    this.max_open_files = value;
    return this;
  }
  public int maxOpenFiles() {
    return max_open_files;
  }

  public NativeOptions maxWriteBufferNumber(int value) {
    this.max_write_buffer_number = value;
    return this;
  }
  public int maxWriteBufferNumber() {
    return max_write_buffer_number;
  }

  public NativeOptions maxGrandparentOverlapFactor(int value) {
    this.max_grandparent_overlap_factor = value;
    return this;
  }
  public int maxGrandparentOverlapFactor() {
    return max_grandparent_overlap_factor;
  }

  public NativeOptions maxBackgroundCompactions(int value) {
    this.max_background_compactions = value;
    return this;
  }
  public int maxBackgroundCompactions() {
    return max_background_compactions;
  }

  public NativeOptions sourceCompactionFactor(int value) {
    this.source_compaction_factor = value;
    return this;
  }
  public int sourceCompactionFactor() {
    return source_compaction_factor;
  }

  public NativeOptions maxBytesForLevelBase(int value) {
    this.max_bytes_for_level_base = value;
    return this;
  }
  public int maxBytesForLevelBase() {
    return max_bytes_for_level_base;
  }

  public NativeOptions level0StopWritesTrigger(int value) {
    this.level0_stop_writes_trigger = value;
    return this;
  }

  public int level0StopWritesTrigger() {
    return level0_stop_writes_trigger;
  }

  public NativeOptions targetFileSizeBase(int value) {
    this.target_file_size_base = value;
    return this;
  }

  public int targetFileSizeBase() {
    return target_file_size_base;
  }

  public NativeOptions level0FileNumCompactionTrigger(int value) {
    this.level0_file_num_compaction_trigger = value;
    return this;
  }
  public int level0FileNumCompactionTrigger() {
    return level0_file_num_compaction_trigger;
  }

  public NativeOptions numLevels(int value) {
    this.num_levels = value;
    return this;
  }
  public int numLevels() {
    return num_levels;
  }

  public NativeOptions blockRestartInterval(int value) {
    this.block_restart_interval = value;
    return this;
  }
  public int blockRestartInterval() {
    return block_restart_interval;
  }

  public NativeOptions blockSize(long value) {
    this.block_size = value;
    return this;
  }
  public long blockSize() {
    return block_size;
  }

//    @JniField(cast="Env*")
//    private long env = DEFAULT_ENV;

  public NativeComparator comparator() {
    return comparatorObject;
  }

  public NativeOptions comparator(NativeComparator comparator) {
    if( comparator==null ) {
      throw new IllegalArgumentException("comparator cannot be null");
    }
    this.comparatorObject = comparator;
    this.comparator = comparator.pointer();
    return this;
  }

  public NativeLogger infoLog() {
    return infoLogObject;
  }

  public NativeOptions infoLog(NativeLogger logger) {
    this.infoLogObject = logger;
    if( logger ==null ) {
      this.info_log = 0;
    } else {
      this.info_log = logger.pointer();
    }
    return this;
  }

  public NativeMergeOperator mergeOperator()
  {
    return _mergeOperator;
  }

  public NativeOptions mergeOperator(NativeMergeOperator nativeMergeOperator) {
    _mergeOperator = nativeMergeOperator;
    if( _mergeOperator ==null ) {
      this.merge_operator = 0;
    } else {
      this.merge_operator = _mergeOperator.pointer();
    }
    return this;
  }

  public NativeCompactionFilter nativeCompactionFilter()
  {
    return _compactionFilter;
  }

  public NativeOptions compactionFilter(NativeCompactionFilter nativeCompactionFilter) {
    _compactionFilter = nativeCompactionFilter;
    if( _compactionFilter ==null ) {
      this.compaction_filter = 0;
    } else {
      this.compaction_filter = _compactionFilter.pointer();
    }
    return this;
  }

  public NativeCompressionType compression() {
    if(compression == NativeCompressionType.kNoCompression.value) {
      return NativeCompressionType.kNoCompression;
    } else if(compression == NativeCompressionType.kSnappyCompression.value) {
      return NativeCompressionType.kSnappyCompression;
    } else {
      return NativeCompressionType.kSnappyCompression;
    }
  }

  public NativeOptions compression(NativeCompressionType compression) {
    this.compression = compression.value;
    return this;
  }

  public NativeCache cache() {
    return cache;
  }

  public NativeOptions cache(NativeCache cache) {
    this.cache = cache;
    if( cache!=null ) {
      this.block_cache = cache.pointer();
    } else {
      this.block_cache = 0;
    }
    return this;
  }
}
