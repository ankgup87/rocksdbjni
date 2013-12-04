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


import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.WriteBatch;


/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class JniDB implements DB {

  private NativeDB db;
  private NativeCache cache;
  private NativeComparator comparator;
  private NativeLogger logger;

  public JniDB(NativeDB db, NativeCache cache, NativeComparator comparator, NativeLogger logger) {
    this.db = db;
    this.cache = cache;
    this.comparator = comparator;
    this.logger = logger;
  }

  public void close() {
    if( db!=null ) {
      db.delete();
      db = null;
      if(cache!=null) {
        cache.delete();
        cache = null;
      }
      if(comparator!=null){
        comparator.delete();
        comparator = null;
      }
      if(logger!=null) {
        logger.delete();
        logger = null;
      }
    }
  }


  public byte[] get(byte[] key) throws DBException
  {
    if( db==null ) {
      throw new DBException("Closed");
    }
    return get(key, new NativeReadOptions());
  }

  public byte[] get(byte[] key, NativeReadOptions options) throws DBException {
    if( db==null ) {
      throw new DBException("Closed");
    }
    try {
      return db.get(options, key);
    } catch (NativeDB.DBException e) {
      if(e.isNotFound()) {
        return null;
      }
      throw new DBException(e.getMessage(), e);
    }
  }

  public DBIterator iterator() {
    return iterator(new NativeReadOptions());
  }

  public DBIterator iterator(NativeReadOptions options) {
    if( db==null ) {
      throw new DBException("Closed");
    }
    return new JniDBIterator(db.iterator(options));
  }

  public void put(byte[] key, byte[] value) throws DBException {
    put(key, value, new NativeWriteOptions());
  }

  public void merge(byte[] key, byte[] value) throws DBException {
    merge(key, value, new NativeWriteOptions());
  }

  public void delete(byte[] key) throws DBException {
    delete(key, new NativeWriteOptions());
  }

  public void write(WriteBatch updates) throws DBException {
    write(updates, new NativeWriteOptions());
  }

  public WriteBatch createWriteBatch() {
    return new JniWriteBatch(new NativeWriteBatch());
  }

  public JniSnapshot put(byte[] key, byte[] value, NativeWriteOptions options) throws DBException {
    if( db==null ) {
      throw new DBException("Closed");
    }
    try {
      db.put(options, key, value);
      return null;
    } catch (NativeDB.DBException e) {
      throw new DBException(e.getMessage(), e);
    }
  }

  public JniSnapshot merge(byte[] key, byte[] value, NativeWriteOptions options) throws DBException {
    if( db==null ) {
      throw new DBException("Closed");
    }
    try {
      db.merge(options, key, value);
      return null;
    } catch (NativeDB.DBException e) {
      throw new DBException(e.getMessage(), e);
    }
  }

  public JniSnapshot delete(byte[] key, NativeWriteOptions options) throws DBException {
    if( db==null ) {
      throw new DBException("Closed");
    }
    try {
      db.delete(options, key);
      return null;
    } catch (NativeDB.DBException e) {
      throw new DBException(e.getMessage(), e);
    }
  }

  public JniSnapshot write(WriteBatch updates, NativeWriteOptions options) throws DBException {
    if( db==null ) {
      throw new DBException("Closed");
    }
    try {
      db.write(options, ((JniWriteBatch) updates).writeBatch());
      return null;
    } catch (NativeDB.DBException e) {
      throw new DBException(e.getMessage(), e);
    }
  }

  public JniSnapshot getSnapshot() {
    if( db==null ) {
      throw new DBException("Closed");
    }
    return new JniSnapshot(db, db.getSnapshot());
  }

  public long[] getApproximateSizes(Range... ranges) {
    if( db==null ) {
      throw new DBException("Closed");
    }
    NativeRange args[] = new NativeRange[ranges.length];
    for (int i = 0; i < args.length; i++) {
      args[i] = new NativeRange(ranges[i].start(), ranges[i].limit());
    }
    return db.getApproximateSizes(args);
  }

  public String getProperty(String name) {
    if( db==null ) {
      throw new DBException("Closed");
    }
    return db.getProperty(name);
  }

  public void suspendCompactions()
      throws InterruptedException
  {
  }

  public void resumeCompactions()
  {
  }

  public void compactRange(byte[] begin, byte[] end) throws DBException {
    if( db==null ) {
      throw new DBException("Closed");
    }
    db.compactRange(begin, end);
  }

  public NativeCache cache()
  {
    return cache;
  }

//    private static class Suspension {
//        static long env = Util.EnvJNI.Default();
//
//        CountDownLatch suspended = new CountDownLatch(1);
//        CountDownLatch resumed = new CountDownLatch(1);
//        Callback callback = new Callback(this, "suspended", 1);
//
//        public Suspension() {
//            Util.EnvJNI.Schedule(env, callback.getAddress(), 0);
//        }
//
//        private long suspended(long arg) {
//            suspended.countDown();
//            try {
//                resumed.await();
//            } catch (InterruptedException e) {
//            } finally {
//                callback.dispose();
//            }
//            return 0;
//        }
//    }
//
//    int suspendCounter = 0;
//    Suspension suspension = null;
//
//    public void suspendCompactions() throws InterruptedException {
//        Suspension s = null;
//        synchronized (this) {
//            suspendCounter++;
//            if( suspendCounter==1 ) {
//                suspension = new Suspension();
//            }
//            s = suspension;
//        }
//        // Don't return until the compactions have suspended.
//        s.suspended.await();
//    }
//
//    synchronized public void resumeCompactions() {
//        suspendCounter--;
//        if( suspendCounter==0 ) {
//            suspension.resumed.countDown();
//            suspension = null;
//        }
//    }
}
