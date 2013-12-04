package org.fusesource.rocksdbjni.internal;


/**
 * @author: aigupta
 */

import org.iq80.leveldb.DBException;
import java.io.Closeable;
import java.util.Map;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;


/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public interface DB extends Iterable<Map.Entry<byte[], byte[]>>, Closeable {

  public byte[] get(byte[] key) throws DBException;
  public byte[] get(byte[] key, NativeReadOptions options) throws DBException;

  public DBIterator iterator();
  public DBIterator iterator(NativeReadOptions options);

  public void merge(byte[] key, byte[] value) throws DBException;
  public void put(byte[] key, byte[] value) throws DBException;
  public void delete(byte[] key) throws DBException;
  public void write(WriteBatch updates) throws DBException;

  public WriteBatch createWriteBatch();

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot
   * of the DB after this operation.
   */
  public JniSnapshot put(byte[] key, byte[] value, NativeWriteOptions options) throws DBException;

  public JniSnapshot merge(byte[] key, byte[] value, NativeWriteOptions options) throws DBException;

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot
   * of the DB after this operation.
   */
  public JniSnapshot delete(byte[] key, NativeWriteOptions options) throws DBException;

  /**
   * @return null if options.isSnapshot()==false otherwise returns a snapshot
   * of the DB after this operation.
   */
  public JniSnapshot write(WriteBatch updates, NativeWriteOptions options) throws DBException;

  public JniSnapshot getSnapshot();

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

  public NativeCache cache();
}
