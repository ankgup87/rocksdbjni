package com.linkedin.rocksdbjni;

import java.io.Closeable;
import java.util.Map;

import com.linkedin.rocksdbjni.internal.ReadOptions;
import com.linkedin.rocksdbjni.internal.WriteOptions;

public interface DB extends Iterable<Map.Entry<byte[], byte[]>>, Closeable
{

  public byte[] get(byte[] key) throws DBException;

  public byte[] get(byte[] key, ReadOptions options) throws DBException;

  public DBIterator iterator();

  public DBIterator iterator(ReadOptions options);

  public void merge(byte[] key, byte[] value) throws DBException;

  public void put(byte[] key, byte[] value) throws DBException;

  public void delete(byte[] key) throws DBException;

  public void write(DBWriteBatch updates) throws DBException;

  public DBWriteBatch createWriteBatch();

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot of the DB after this
   *         operation.
   */
  public DBSnapshot put(byte[] key, byte[] value, WriteOptions options) throws DBException;

  public DBSnapshot merge(byte[] key, byte[] value, WriteOptions options) throws DBException;

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot of the DB after this
   *         operation.
   */
  public DBSnapshot delete(byte[] key, WriteOptions options) throws DBException;

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot of the DB after this
   *         operation.
   */
  public DBSnapshot write(DBWriteBatch updates, WriteOptions options) throws DBException;

  public DBSnapshot getSnapshot();

  public long[] getApproximateSizes(DBRange... ranges);

  public String getProperty(String name);

  /**
   * Suspends any background compaction threads. This methods returns once the background
   * compactions are suspended.
   */
  public void suspendCompactions() throws InterruptedException;

  /**
   * Resumes the background compaction threads.
   */
  public void resumeCompactions();

  /**
   * Force a compaction of the specified key range.
   * 
   * @param begin
   *          if null then compaction start from the first key
   * @param end
   *          if null then compaction ends at the last key
   * @throws DBException
   */
  public void compactRange(byte[] begin, byte[] end) throws DBException;

  public long statisticsPtr();

  public long envPtr();
}
