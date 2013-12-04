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
package org.fusesource.rocksdbjni.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.fusesource.rocksdbjni.JniDBFactory;
import org.fusesource.rocksdbjni.internal.DB;
import org.fusesource.rocksdbjni.internal.DBFactory;
import org.fusesource.rocksdbjni.internal.JniDB;
import org.fusesource.rocksdbjni.internal.JniSnapshot;
import org.fusesource.rocksdbjni.internal.NativeCache;
import org.fusesource.rocksdbjni.internal.NativeCompactionFilter;
import org.fusesource.rocksdbjni.internal.NativeComparator;
import org.fusesource.rocksdbjni.internal.NativeFilterPolicy;
import org.fusesource.rocksdbjni.internal.NativeLogger;
import org.fusesource.rocksdbjni.internal.NativeMergeOperator;
import org.fusesource.rocksdbjni.internal.NativeOptions;
import org.fusesource.rocksdbjni.internal.NativeReadOptions;
import org.fusesource.rocksdbjni.internal.NativeWriteOptions;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.WriteBatch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static org.fusesource.rocksdbjni.JniDBFactory.asString;
import static org.fusesource.rocksdbjni.JniDBFactory.bytes;

/**
 * A Unit test for the DB class implementation.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class DBTest extends TestCase {

  static {

    try
    {
      System.out.println(System.getProperty("java.library.path"));
    }
    catch (Exception e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  DBFactory factory = JniDBFactory.factory;

  File getTestDirectory(String name) throws IOException {
    File rc = new File(new File("test-data"), name);
    factory.destroy(rc, new NativeOptions().createIfMissing(true));
    rc.mkdirs();
    return rc;
  }

  @Test
  public void testOpen() throws IOException {

    NativeOptions options = new NativeOptions().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    db.close();

    // Try again.. this time we expect a failure since it exists.
    options = new NativeOptions().errorIfExists(true);
    try {
      factory.open(path, options);
      fail("Expected exception.");
    } catch (IOException e) {
    }

  }

  @Test
  public void testRepair() throws IOException, DBException {
    testCRUD();
    factory.repair(new File(new File("test-data"), getName()), new NativeOptions());
  }

  @Test
  public void testCRUD() throws IOException, DBException
  {
    NativeCache nativeCache = new NativeCache(10*1024*1024, 6);
    NativeOptions options = new NativeOptions().createIfMissing(true).
        cache(nativeCache).
        filterPolicy(new NativeFilterPolicy(10)
        {
          public String name()
          {
            return "test";
          }
        }).
        infoLog(new NativeLogger()
        {
          public void log(String s)
          {
            System.out.println(s);
          }
        });

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    NativeWriteOptions wo = new NativeWriteOptions().sync(false);
    NativeReadOptions ro = new NativeReadOptions().fillCache(true).verifyChecksums(true);

    db.put(bytes("Tampa"), bytes("green"));
    db.put(bytes("London"), bytes("red"));
    db.put(bytes("New York"), bytes("blue"));

    assertEquals(db.get(bytes("Tampa"), ro), bytes("green"));
    assertEquals(db.get(bytes("London"), ro), bytes("red"));
    assertEquals(db.get(bytes("New York"), ro), bytes("blue"));

    db.delete(bytes("New York"), wo);
    assertNull(db.get(bytes("New York"), ro));

    // leveldb does not consider deleting something that does not exist an error.
    db.delete(bytes("New York"), wo);

    db.close();
  }

  @Test
  public void testIterator() throws IOException, DBException {

    NativeOptions options = new NativeOptions().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    db.put(bytes("Tampa"), bytes("green"));
    db.put(bytes("London"), bytes("red"));
    db.put(bytes("New York"), bytes("blue"));

    ArrayList<String> expecting = new ArrayList<String>();
    expecting.add("London");
    expecting.add("New York");
    expecting.add("Tampa");

    ArrayList<String> actual = new ArrayList<String>();

    DBIterator iterator = db.iterator();
    for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
      actual.add(asString(iterator.peekNext().getKey()));
    }
    iterator.close();
    assertEquals(expecting, actual);

    db.close();
  }



  @Test
  public void testMerge() throws IOException, DBException {

    NativeOptions options = new NativeOptions().createIfMissing(true);
    options.mergeOperator(new NativeMergeOperator()
    {

      public byte[] fullMerge(byte[] key, byte[] existing_value, List<byte[]> operandList)
      {
        StringBuilder stringBuilder = new StringBuilder();
        if(existing_value != null)
        {
          stringBuilder.append(asString(existing_value));
        }

        for(byte[] operand : operandList)
        {
          stringBuilder.append(asString(operand));
        }

        return bytes(stringBuilder.toString());
      }

      public byte[] partialMerge(byte[] key, byte[] left_operand, byte[] right_operand)
      {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(asString(right_operand));
        stringBuilder.append(asString(left_operand));
        return bytes(stringBuilder.toString());
      }

      public String name()
      {
        return "test";
      }
    });

    final List<String> messages = Collections.synchronizedList(new ArrayList<String>());
    options.infoLog(new NativeLogger()
    {
      public void log(String message)
      {
        messages.add(message);
      }
    });

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    NativeWriteOptions wo = new NativeWriteOptions().sync(false);
    NativeReadOptions ro = new NativeReadOptions().fillCache(true).verifyChecksums(true);

    db.merge(bytes("key"), bytes("abcdef"), wo);
    db.merge(bytes("key"), bytes("ghijkl"), wo);

    Assert.assertEquals("ghijklabcdef", asString(db.get(bytes("key"), ro)));

    db.merge(bytes("key"), bytes("mnopqr"), wo);

    Assert.assertEquals("mnopqrghijklabcdef", asString(db.get(bytes("key"), ro)));

    db.close();
  }

  @Test
  public void testSnapshot() throws IOException, DBException
  {
    NativeOptions options = new NativeOptions().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    db.put(bytes("Tampa"), bytes("green"));
    db.put(bytes("London"), bytes("red"));
    db.delete(bytes("New York"));

    JniSnapshot jniSnapshot = db.getSnapshot();
    NativeReadOptions ro = new NativeReadOptions().snapshot(jniSnapshot);

    db.put(bytes("New York"), bytes("blue"));

    assertEquals(db.get(bytes("Tampa"), ro), bytes("green"));
    assertEquals(db.get(bytes("London"), ro), bytes("red"));

    // Should not be able to get "New York" since it was added
    // after the snapshot
    assertNull(db.get(bytes("New York"), ro));

    jniSnapshot.close();

    // Now try again without the snapshot..
    ro.snapshot(null);
    assertEquals(db.get(bytes("New York"), ro), bytes("blue"));

    db.close();
  }

  @Test
  public void testCompactRanges() throws IOException, InterruptedException, DBException {
    NativeOptions options = new NativeOptions().createIfMissing(true);
    options.compactionFilter(new NativeCompactionFilter()
    {
      public boolean filter(byte[] key, byte[] existingValue)
      {
        return true;
      }

      public String name()
      {
        return "test";
      }
    });

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);
    if( db instanceof JniDB) {
      Random r = new Random(0);
      String data="";
      for(int i=0; i < 1024; i++) {
        data+= 'a'+r.nextInt(26);
      }
      for(int i=0; i < 5*1024; i++) {
        db.put(bytes("row"+i), bytes(data));
      }
      for(int i=0; i < 5*1024; i++) {
        db.delete(bytes("row" + i));
      }

      // After the compaction, level 1 and 2 should not have any files in it..
      db.compactRange(null, null);

    }
    db.close();
  }

  @Test
  public void testWriteBatch() throws IOException, DBException {

    NativeOptions options = new NativeOptions().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    db.put(bytes("NA"), bytes("Na"));

    WriteBatch batch = db.createWriteBatch();
    batch.delete(bytes("NA"));
    batch.put(bytes("Tampa"), bytes("green"));
    batch.put(bytes("London"), bytes("red"));
    batch.put(bytes("New York"), bytes("blue"));
    db.write(batch);
    batch.close();

    ArrayList<String> expecting = new ArrayList<String>();
    expecting.add("London");
    expecting.add("New York");
    expecting.add("Tampa");

    ArrayList<String> actual = new ArrayList<String>();

    DBIterator iterator = db.iterator();
    for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
      actual.add(asString(iterator.peekNext().getKey()));
    }
    iterator.close();
    assertEquals(expecting, actual);

    db.close();
  }

  @Test
  public void testApproximateSizes() throws IOException, DBException {
    NativeOptions options = new NativeOptions().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    Random r = new Random(0);
    String data="";
    for(int i=0; i < 1024; i++) {
      data+= 'a'+r.nextInt(26);
    }
    for(int i=0; i < 5*1024; i++) {
      db.put(bytes("row"+i), bytes(data));
    }

    long[] approximateSizes = db.getApproximateSizes(new Range(bytes("row"), bytes("s")));
    assertNotNull(approximateSizes);
    assertEquals(1, approximateSizes.length);
    assertTrue("Wrong size", approximateSizes[0] > 0);

    db.close();
  }


  @Test
  public void testCustomComparator1() throws IOException, DBException {
    NativeOptions options = new NativeOptions().createIfMissing(true);
    options.comparator(new NativeComparator() {

      public int compare(byte[] key1, byte[] key2) {
        return new String(key1).compareTo(new String(key2));
      }

      public String name() {
        return getName();
      }

      public byte[] findShortestSeparator(byte[] start, byte[] limit) {
        return start;
      }

      public byte[] findShortSuccessor(byte[] key) {
        return key;
      }
    });

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    ArrayList<String> expecting = new ArrayList<String>();
    for(int i=0; i < 26; i++) {
      String t = ""+ ((char) ('a' + i));
      expecting.add(t);
      db.put(bytes(t), bytes(t));
    }

    ArrayList<String> actual = new ArrayList<String>();

    DBIterator iterator = db.iterator();
    for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
      actual.add(asString(iterator.peekNext().getKey()));
    }
    iterator.close();
    assertEquals(expecting, actual);


    db.close();
  }

  @Test
  public void testCustomComparator2() throws IOException, DBException {
    NativeOptions options = new NativeOptions().createIfMissing(true);
    options.comparator(new NativeComparator()
    {

      public int compare(byte[] key1, byte[] key2)
      {
        return new String(key1).compareTo(new String(key2)) * -1;
      }

      public String name()
      {
        return getName();
      }

      public byte[] findShortestSeparator(byte[] start, byte[] limit)
      {
        return start;
      }

      public byte[] findShortSuccessor(byte[] key)
      {
        return key;
      }
    });

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    ArrayList<String> expecting = new ArrayList<String>();
    for(int i=0; i < 26; i++) {
      String t = ""+ ((char) ('a' + i));
      expecting.add(t);
      db.put(bytes(t), bytes(t));
    }
    Collections.reverse(expecting);

    ArrayList<String> actual = new ArrayList<String>();
    DBIterator iterator = db.iterator();
    for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
      actual.add(asString(iterator.peekNext().getKey()));
    }
    iterator.close();
    assertEquals(expecting, actual);

    db.close();
  }

  @Test
  public void testSuspendAndResumeCompactions() throws Exception {
    NativeOptions options = new NativeOptions().createIfMissing(true);
    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);
    db.suspendCompactions();
    db.resumeCompactions();
    db.close();
  }

  public void assertEquals(byte[] arg1, byte[] arg2) {
    assertTrue(Arrays.equals(arg1, arg2));
  }

  @Test
  public void testIssue27() throws IOException {

    NativeOptions options = new NativeOptions();
    options.createIfMissing(true);
    DB db = factory.open(getTestDirectory(getName()), options);
    db.close();

    try {
      db.iterator();
      fail("Expected a DBException");
    } catch(DBException e) {
    }
  }

}
