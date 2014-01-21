package com.linkedin.rocksdbjni.internal;

import com.linkedin.rocksdbjni.DB;
import com.linkedin.rocksdbjni.DBException;
import com.linkedin.rocksdbjni.DBIterator;
import com.linkedin.rocksdbjni.DBRange;
import com.linkedin.rocksdbjni.DBSnapshot;
import com.linkedin.rocksdbjni.DBWriteBatch;

public class JniDB implements DB
{

  private NativeDB db;
  private NativeCache cache;
  private NativeComparator comparator;
  private NativeLogger logger;
  private long statisticsPtr;
  private long envPtr;

  public JniDB(NativeDB db,
               NativeCache cache,
               NativeComparator comparator,
               NativeLogger logger,
               long statisticsPtr,
               long envPtr)
  {
    this.db = db;
    this.cache = cache;
    this.comparator = comparator;
    this.logger = logger;
    this.statisticsPtr = statisticsPtr;
    this.envPtr = envPtr;
  }

  public void close()
  {
    if (db != null)
    {

      // TODO: Fix this. Use case: Cache is shared between two DBs. First DB is closed which deletes
      // DB and cache pointer. Second DB deletion causes segmentation fault
      /*
       * db.delete(); db = null;
       */
      if (cache != null)
      {
        cache.delete();
        cache = null;
      }
      if (comparator != null)
      {
        comparator.delete();
        comparator = null;
      }
      if (logger != null)
      {
        logger.delete();
        logger = null;
      }
    }
  }

  public long statisticsPtr()
  {
    return statisticsPtr;
  }

  public long envPtr()
  {
    return envPtr;
  }

  public byte[] get(byte[] key) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    return get(key, new ReadOptions());
  }

  public byte[] get(byte[] key, ReadOptions options) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    try
    {
      return db.get(convert(options), key);
    }
    catch (NativeDB.DBException e)
    {
      if (e.isNotFound())
      {
        return null;
      }
      throw new DBException(e.getMessage(), e);
    }
  }

  public DBIterator iterator()
  {
    return iterator(new ReadOptions());
  }

  public DBIterator iterator(ReadOptions options)
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    return new JniDBIterator(db.iterator(convert(options)));
  }

  public void put(byte[] key, byte[] value) throws DBException
  {
    put(key, value, new WriteOptions());
  }

  public void merge(byte[] key, byte[] value) throws DBException
  {
    merge(key, value, new WriteOptions());
  }

  public void delete(byte[] key) throws DBException
  {
    delete(key, new WriteOptions());
  }

  public void write(DBWriteBatch updates) throws DBException
  {
    write(updates, new WriteOptions());
  }

  public DBWriteBatch createWriteBatch()
  {
    return new JniWriteBatch(new NativeWriteBatch());
  }

  public DBSnapshot put(byte[] key, byte[] value, WriteOptions options) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    try
    {
      db.put(convert(options), key, value);
      return null;
    }
    catch (NativeDB.DBException e)
    {
      throw new DBException(e.getMessage(), e);
    }
  }

  public DBSnapshot merge(byte[] key, byte[] value, WriteOptions options) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    try
    {
      db.merge(convert(options), key, value);
      return null;
    }
    catch (NativeDB.DBException e)
    {
      throw new DBException(e.getMessage(), e);
    }
  }

  public DBSnapshot delete(byte[] key, WriteOptions options) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    try
    {
      db.delete(convert(options), key);
      return null;
    }
    catch (NativeDB.DBException e)
    {
      throw new DBException(e.getMessage(), e);
    }
  }

  public DBSnapshot write(DBWriteBatch updates, WriteOptions options) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    try
    {
      db.write(convert(options), ((JniWriteBatch) updates).writeBatch());
      return null;
    }
    catch (NativeDB.DBException e)
    {
      throw new DBException(e.getMessage(), e);
    }
  }

  public DBSnapshot getSnapshot()
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    return new JniSnapshot(db, db.getSnapshot());
  }

  public long[] getApproximateSizes(DBRange... ranges)
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    NativeRange args[] = new NativeRange[ranges.length];
    for (int i = 0; i < args.length; i++)
    {
      args[i] = new NativeRange(ranges[i].start(), ranges[i].limit());
    }
    return db.getApproximateSizes(args);
  }

  public String getProperty(String name)
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    return db.getProperty(name);
  }

  public void suspendCompactions() throws InterruptedException
  {
  }

  public void resumeCompactions()
  {
  }

  private NativeReadOptions convert(ReadOptions options)
  {
    if (options == null)
    {
      return null;
    }
    NativeReadOptions rc = new NativeReadOptions();
    rc.fillCache(options.fillCache());
    rc.verifyChecksums(options.verifyChecksums());
    if (options.snapshot() != null)
    {
      rc.snapshot(((JniSnapshot) options.snapshot()).snapshot());
    }
    return rc;
  }

  private NativeWriteOptions convert(WriteOptions options)
  {
    if (options == null)
    {
      return null;
    }
    NativeWriteOptions rc = new NativeWriteOptions();
    rc.sync(options.sync());
    return rc;
  }

  public void compactRange(byte[] begin, byte[] end) throws DBException
  {
    if (db == null)
    {
      throw new DBException("Closed");
    }
    db.compactRange(begin, end);
  }

  // private static class Suspension {
  // static long env = Util.EnvJNI.Default();
  //
  // CountDownLatch suspended = new CountDownLatch(1);
  // CountDownLatch resumed = new CountDownLatch(1);
  // Callback callback = new Callback(this, "suspended", 1);
  //
  // public Suspension() {
  // Util.EnvJNI.Schedule(env, callback.getAddress(), 0);
  // }
  //
  // private long suspended(long arg) {
  // suspended.countDown();
  // try {
  // resumed.await();
  // } catch (InterruptedException e) {
  // } finally {
  // callback.dispose();
  // }
  // return 0;
  // }
  // }
  //
  // int suspendCounter = 0;
  // Suspension suspension = null;
  //
  // public void suspendCompactions() throws InterruptedException {
  // Suspension s = null;
  // synchronized (this) {
  // suspendCounter++;
  // if( suspendCounter==1 ) {
  // suspension = new Suspension();
  // }
  // s = suspension;
  // }
  // // Don't return until the compactions have suspended.
  // s.suspended.await();
  // }
  //
  // synchronized public void resumeCompactions() {
  // suspendCounter--;
  // if( suspendCounter==0 ) {
  // suspension.resumed.countDown();
  // suspension = null;
  // }
  // }
}
