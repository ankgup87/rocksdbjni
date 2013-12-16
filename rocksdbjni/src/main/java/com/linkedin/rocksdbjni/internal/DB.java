package com.linkedin.rocksdbjni.internal;


/**
 * @author Ankit Gupta
 */

import org.iq80.leveldb.DBException;
import java.io.Closeable;
import java.util.Map;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.WriteBatch;

public interface DB extends Iterable<Map.Entry<byte[], byte[]>>, Closeable {

  public byte[] get(byte[] key) throws DBException;
  public byte[] get(byte[] key, ReadOptions options) throws DBException;

  public DBIterator iterator();
  public DBIterator iterator(ReadOptions options);

  public void merge(byte[] key, byte[] value) throws DBException;
  public void put(byte[] key, byte[] value) throws DBException;
  public void delete(byte[] key) throws DBException;
  public void write(WriteBatch updates) throws DBException;

  public WriteBatch createWriteBatch();

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot
   * of the DB after this operation.
   */
  public Snapshot put(byte[] key, byte[] value, WriteOptions options) throws DBException;

  public Snapshot merge(byte[] key, byte[] value, WriteOptions options) throws DBException;

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot
   * of the DB after this operation.
   */
  public Snapshot delete(byte[] key, WriteOptions options) throws DBException;

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot
   * of the DB after this operation.
   */
  public Snapshot write(WriteBatch updates, WriteOptions options) throws DBException;

  public Snapshot getSnapshot();

  public long[] getApproximateSizes(Range... ranges);
  public String getProperty(String name);

  /**
   * Suspends any background compaction threads. This methods
   * returns once the background compactions are suspended.
   */
  public void suspendCompactions() throws InterruptedException;

  /**
   * Resumes the background compaction threads.
   */
  public void resumeCompactions();

  /**
   * Force a compaction of the specified key range.
   *
   * @param begin if null then compaction start from the first key
   * @param end if null then compaction ends at the last key
   * @throws DBException
   */
  public void compactRange(byte[] begin, byte[] end) throws DBException;

  public long statisticsPtr();

  public long envPtr();
}
