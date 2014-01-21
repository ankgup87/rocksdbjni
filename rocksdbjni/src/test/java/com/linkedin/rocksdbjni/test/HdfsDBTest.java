package com.linkedin.rocksdbjni.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.linkedin.rocksdbjni.DB;
import com.linkedin.rocksdbjni.DBFactory;
import com.linkedin.rocksdbjni.internal.JniDBFactory;
import com.linkedin.rocksdbjni.internal.NativeHdfsEnv;
import com.linkedin.rocksdbjni.internal.Options;
import com.linkedin.rocksdbjni.internal.ReadOptions;
import com.linkedin.rocksdbjni.internal.WriteOptions;
import static com.linkedin.rocksdbjni.test.TestUtils.bytes;

public class HdfsDBTest extends BaseHdfsDBTest
{

  private DBFactory factory = JniDBFactory.factory;

  public HdfsDBTest() throws IOException
  {
    super();
  }

  @Test
  public void testDBCreation() throws IOException
  {

    long env = NativeHdfsEnv.create(getFileSystem().getCanonicalServiceName());
    Options options = new Options().envPtr(env).createIfMissing(true);

    DB db = null;
    try
    {
      db = factory.open(new File("/tmp/bb"), options);

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

      // Rocks DB does not consider deleting something that does not exist an error.
      db.delete(bytes("New York"), wo);

    }
    finally
    {
      if (db != null)
      {
        db.close();
      }
    }

  }
}
