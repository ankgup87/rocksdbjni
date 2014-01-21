package com.linkedin.rocksdbjni.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.linkedin.rocksdbjni.DBFactory;
import com.linkedin.rocksdbjni.internal.Options;

public class TestUtils
{

  public static String asString(byte value[])
  {
    if (value == null)
    {
      return null;
    }
    try
    {
      return new String(value, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static byte[] bytes(String value)
  {
    if (value == null)
    {
      return null;
    }
    try
    {
      return value.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static File getTestDirectory(DBFactory factory, String name) throws IOException
  {
    File rc = new File(new File("test-data"), name);
    factory.destroy(rc, new Options().createIfMissing(true));
    rc.mkdirs();
    return rc;
  }
}
