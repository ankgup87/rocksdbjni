
package com.linkedin.rocksdbjni.test;

import com.linkedin.rocksdbjni.JniDBFactory;
import junit.framework.Assert;
import junit.framework.TestCase;
import com.linkedin.rocksdbjni.internal.CompactionFilter;
import com.linkedin.rocksdbjni.internal.DB;
import com.linkedin.rocksdbjni.internal.DBFactory;
import com.linkedin.rocksdbjni.internal.FilterPolicy;
import com.linkedin.rocksdbjni.internal.JniDB;
import com.linkedin.rocksdbjni.internal.MergeOperator;
import com.linkedin.rocksdbjni.internal.NativeCache;
import com.linkedin.rocksdbjni.internal.Options;
import com.linkedin.rocksdbjni.internal.ReadOptions;
import com.linkedin.rocksdbjni.internal.WriteOptions;
import org.iq80.leveldb.DBComparator;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Logger;
import org.iq80.leveldb.Range;
import org.iq80.leveldb.WriteBatch;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static com.linkedin.rocksdbjni.JniDBFactory.asString;
import static com.linkedin.rocksdbjni.JniDBFactory.bytes;


/**
 * A Unit test for the DB class implementation.
 *
 * @author Ankit Gupta
 */
public class DBTest extends TestCase {

  DBFactory factory = JniDBFactory.factory;

  File getTestDirectory(String name) throws IOException {
    File rc = new File(new File("test-data"), name);
    factory.destroy(rc, new Options().createIfMissing(true));
    rc.mkdirs();
    return rc;
  }

  @Test
  public void testOpen() throws IOException {

    Options options = new Options().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    db.close();

    // Try again.. this time we expect a failure since it exists.
    options = new Options().errorIfExists(true);
    try {
      factory.open(path, options);
      fail("Expected exception.");
    } catch (IOException e) {
    }

  }

  @Test
  public void testRepair() throws IOException, DBException {
    testCRUD();
    factory.repair(new File(new File("test-data"), getName()), new Options());
  }

  @Test
  public void testCRUD() throws IOException, DBException
  {
    Options options = new Options().createIfMissing(true).nativeCache(new NativeCache(10 * 1024 * 1024, 6)).
        filterPolicy(new FilterPolicy()
        {
          public int bitsPerKey()
          {
            return 10;
          }

          public String name()
          {
            return "test";
          }
        }).
        logger(new Logger()
        {
          public void log(String s)
          {
            System.out.println(s);
          }
        });

    File path = getTestDirectory(getName());
    factory.destroy(path, new Options());

    DB db = factory.open(path, options);

    File path1 = getTestDirectory(getName() + "test");
    DB db1 = factory.open(path1, options);



    WriteOptions wo = new WriteOptions().sync(false);
    ReadOptions ro = new ReadOptions().fillCache(true).verifyChecksums(true);

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
    db1.close();
  }

  @Test
  public void testIterator() throws IOException, DBException {

    Options options = new Options().createIfMissing(true);

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

    Options options = new Options().createIfMissing(true);
    options.mergeOperator(new MergeOperator()
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
    options.logger(new Logger()
    {
      public void log(String message)
      {
        messages.add(message);
      }
    });

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    WriteOptions wo = new WriteOptions().sync(false);
    ReadOptions ro = new ReadOptions().fillCache(true).verifyChecksums(true);

    db.merge(bytes("key"), bytes("abcdef"), wo);
    db.merge(bytes("key"), bytes("ghijkl"), wo);

    Assert.assertEquals("ghijklabcdef", asString(db.get(bytes("key"), ro)));

    db.merge(bytes("key"), bytes("mnopqr"), wo);

    Assert.assertEquals("mnopqrghijklabcdef", asString(db.get(bytes("key"), ro)));

    db.close();
  }

  @Test
  public void testSnapshot() throws IOException, DBException {

    Options options = new Options().createIfMissing(true);

    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);

    db.put(bytes("Tampa"), bytes("green"));
    db.put(bytes("London"), bytes("red"));
    db.delete(bytes("New York"));

    ReadOptions ro = new ReadOptions().snapshot(db.getSnapshot());

    db.put(bytes("New York"), bytes("blue"));

    assertEquals(db.get(bytes("Tampa"), ro), bytes("green"));
    assertEquals(db.get(bytes("London"), ro), bytes("red"));

    // Should not be able to get "New York" since it was added
    // after the snapshot
    assertNull(db.get(bytes("New York"), ro));

    ro.snapshot().close();

    // Now try again without the snapshot..
    ro.snapshot(null);
    assertEquals(db.get(bytes("New York"), ro), bytes("blue"));

    db.close();
  }

  @Test
  public void testCompactRanges() throws IOException, InterruptedException, DBException {
    Options options = new Options().createIfMissing(true);
    options.compactionFilter(new CompactionFilter()
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

    Options options = new Options().createIfMissing(true);

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
    Options options = new Options().createIfMissing(true);

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
    Options options = new Options().createIfMissing(true);
    options.comparator(new DBComparator() {

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
    Options options = new Options().createIfMissing(true);
    options.comparator(new DBComparator() {

      public int compare(byte[] key1, byte[] key2) {
        return new String(key1).compareTo(new String(key2)) * -1;
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
    Options options = new Options().createIfMissing(true);
    File path = getTestDirectory(getName());
    DB db = factory.open(path, options);
    db.suspendCompactions();
    db.resumeCompactions();
    db.close();
  }

  public void assertEquals(byte[] arg1, byte[] arg2) {
    assertTrue(Arrays.equals(arg1, arg2));
  }

  //TODO: Fix this when DB deletion is fixed
  /*@Test
  public void testIssue26() throws IOException {

    JniDBFactory.pushMemoryPool(1024 * 512);
    try {
      Options options = new Options();
      options.createIfMissing(true);

      DB db = factory.open(getTestDirectory(getName()), options);

      for (int i = 0; i < 1024 * 1024; i++) {
        byte[] key = ByteBuffer.allocate(4).putInt(i).array();
        byte[] value = ByteBuffer.allocate(4).putInt(-i).array();
        db.put(key, value);
        assertTrue(Arrays.equals(db.get(key), value));
      }
      //db.close();
    } finally {
      JniDBFactory.popMemoryPool();
    }

  }*/


  /*@Test
  public void testIssue27() throws IOException {

    Options options = new Options();
    options.createIfMissing(true);
    DB db = factory.open(getTestDirectory(getName()), options);
    db.close();

    try {
      db.iterator();
      fail("Expected a DBException");
    } catch(DBException e) {
    }
  }*/

}