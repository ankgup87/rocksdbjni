
package com.linkedin.rocksdbjni;

import java.util.List;
import com.linkedin.rocksdbjni.internal.*;
import com.linkedin.rocksdbjni.internal.DB;
import com.linkedin.rocksdbjni.internal.DBFactory;
import com.linkedin.rocksdbjni.internal.Options;
import org.iq80.leveldb.*;

import java.io.*;



public class JniDBFactory implements DBFactory
{
  public static final JniDBFactory factory = new JniDBFactory();
  static {
    NativeDB.LIBRARY.load();
  }

  public static final String VERSION;
  static {
    String v="unknown";
    InputStream is = JniDBFactory.class.getResourceAsStream("version.txt");
    try {
      v = new BufferedReader(new InputStreamReader(is, "UTF-8")).readLine();
    } catch (Throwable e) {
    } finally {
      try {
        is.close();
      } catch (Throwable e) {
      }
    }
    VERSION = v;
  }

  public static byte[] bytes(String value) {
    if( value == null) {
      return null;
    }
    try {
      return value.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String asString(byte value[]) {
    if( value == null) {
      return null;
    }
    try {
      return new String(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  static private class OptionsResourceHolder {

    NativeCache cache = null;
    NativeComparator comparator=null;
    NativeLogger logger=null;
    NativeOptions options;
    NativeMergeOperator nativeMergeOperator = null;
    NativeCompactionFilter nativeCompactionFilter = null;
    NativeFilterPolicy nativeFilterPolicy = null;
    long envPtr = 0;

    public void init(Options value) {

      options = new NativeOptions();
      options.blockRestartInterval(value.blockRestartInterval());
      options.blockSize(value.blockSize());
      options.createIfMissing(value.createIfMissing());
      options.errorIfExists(value.errorIfExists());
      options.maxOpenFiles(value.maxOpenFiles());
      options.paranoidChecks(value.paranoidChecks());
      options.writeBufferSize(value.writeBufferSize());
      options.disableSeekCompaction(value.disableSeekCompaction());
      options.disableAutoCompactions(value.disableAutoCompactions());
      options.maxOpenFiles(value.maxOpenFiles());
      options.maxBytesForLevelBase(value.maxBytesForLevelBase());

      options.level0SlowdownWritesTrigger(value.level0SlowdownWritesTrigger());
      options.deleteObsoleteFilesPeriodMicros(value.deleteObsoleteFilesPeriodMicros());
      options.sourceCompactionFactor(value.sourceCompactionFactor());
      options.maxBackgroundCompactions(value.maxBackgroundCompactions());
      options.maxGrandparentOverlapFactor(value.maxGrandparentOverlapFactor());
      options.numLevels(value.numLevels());

      options.level0FileNumCompactionTrigger(value.level0FileNumCompactionTrigger());
      options.targetFileSizeBase(value.targetFileSizeBase());
      options.level0StopWritesTrigger(value.level0StopWritesTrigger());
      options.maxWriteBufferNumber(value.maxWriteBufferNumber());
      options.envPtr(value.envPtr());

      switch(value.compressionType()) {
        case NONE:
          options.compression(NativeCompressionType.kNoCompression);
          break;
        case SNAPPY:
          options.compression(NativeCompressionType.kSnappyCompression);
          break;
      }

      switch (value.compactionStyle())
      {
        case LEVEL:
          options.compactionStyle(NativeCompactionStyle.kCompactionStyleLevel);
          break;
        case UNIVERSAL:
          options.compactionStyle(NativeCompactionStyle.kCompactionStyleUniversal);
          break;
      }

      if(value.nativeCache() != null)
      {
        if(!value.nativeCache().isAllocated())
        {
          throw new RuntimeException("Cache is not allocated");
        }
        cache = value.nativeCache();
        options.cache(value.nativeCache());
      }


      final FilterPolicy filterPolicy = value.filterPolicy();
      if(filterPolicy != null)
      {
        nativeFilterPolicy = new NativeFilterPolicy(filterPolicy.bitsPerKey())
        {
          @Override
          public String name()
          {
            return filterPolicy.name();
          }
        };
        options.filterPolicy(nativeFilterPolicy);
      }

      final DBComparator userComparator = value.comparator();
      if(userComparator!=null) {
        comparator = new NativeComparator() {
          @Override
          public int compare(byte[] key1, byte[] key2) {
            return userComparator.compare(key1, key2);
          }

          @Override
          public String name() {
            return userComparator.name();
          }
        };
        options.comparator(comparator);
      }

      final Logger userLogger = value.logger();
      if(userLogger!=null) {
        logger = new NativeLogger() {
          @Override
          public void log(String message) {
            userLogger.log(message);
          }
        };
        options.infoLog(logger);
      }

      final MergeOperator mergeOperator = value.mergeOperator();
      if(mergeOperator != null)
      {
        nativeMergeOperator = new NativeMergeOperator()
        {
          @Override
          public String name()
          {
            return mergeOperator.name();
          }

          @Override
          public byte[] fullMerge(byte[] key, byte[] existing_value, List<byte[]> operandList)
          {
            return mergeOperator.fullMerge(key, existing_value, operandList);
          }

          @Override
          public byte[] partialMerge(byte[] key, byte[] left_operand, byte[] right_operand)
          {
            return mergeOperator.partialMerge(key, left_operand, right_operand);
          }
        };

        options.mergeOperator(nativeMergeOperator);
      }

      final CompactionFilter compactionFilter = value.compactionFilter();
      if(compactionFilter != null)
      {
        nativeCompactionFilter = new NativeCompactionFilter()
        {
          @Override
          public boolean filter(byte[] key, byte[] existingValue)
          {
            return compactionFilter.filter(key, existingValue);
          }

          @Override
          public String name()
          {
            return compactionFilter.name();
          }
        };

        options.compactionFilter(nativeCompactionFilter);
      }
    }
    public void close() {
      if(cache!=null) {
        cache.delete();
      }
      if(comparator!=null){
        comparator.delete();
      }
      if(logger!=null) {
        logger.delete();
      }
    }
  }

  public DB open(File path, Options options) throws IOException {
    NativeDB db=null;
    OptionsResourceHolder holder = new OptionsResourceHolder();
    try {
      holder.init(options);
      db = NativeDB.open(holder.options, path);
    } finally {
      // if we could not open up the DB, then clean up the
      // other allocated native resouces..
      if(db==null) {
        holder.close();
      }
    }
    return (DB) new JniDB(db, holder.cache, holder.comparator, holder.logger, holder.options.statisticsPtr(), holder.options.envPtr());
  }

  public void destroy(File path, Options options) throws IOException {
    OptionsResourceHolder holder = new OptionsResourceHolder();
    try {
      holder.init(options);
      NativeDB.destroy(path, holder.options);
    } finally {
      holder.close();
    }
  }

  public void repair(File path, Options options) throws IOException {
    OptionsResourceHolder holder = new OptionsResourceHolder();
    try {
      holder.init(options);
      NativeDB.repair(path, holder.options);
    } finally {
      holder.close();
    }
  }

  @Override
  public String toString() {
    return String.format("com.linkedin version %s", VERSION);
  }


  public static void pushMemoryPool(int size) {
    NativeBuffer.pushMemoryPool(size);
  }

  public static void popMemoryPool() {
    NativeBuffer.popMemoryPool();
  }
}
