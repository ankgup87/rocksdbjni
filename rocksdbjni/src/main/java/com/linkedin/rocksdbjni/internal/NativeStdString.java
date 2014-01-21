package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;

/**
 * Provides a java interface to the C++ std::string class.
 */
class NativeStdString extends NativeObject
{

  @JniClass(name = "std::string", flags = { CPP })
  private static class StdStringJNI
  {
    static
    {
      NativeDB.LIBRARY.load();
    }

    @JniMethod(flags = { CPP_NEW })
    public static final native long create();

    @JniMethod(flags = { CPP_NEW })
    public static final native long create(String value);

    @JniMethod(flags = { CPP_DELETE })
    static final native void delete(long self);

    @JniMethod(flags = { CPP_METHOD }, accessor = "c_str", cast = "const char*")
    public static final native long c_str_ptr(long self);

    @JniMethod(flags = { CPP_METHOD }, cast = "size_t")
    public static final native long length(long self);

  }

  public NativeStdString(long self)
  {
    super(self);
  }

  public NativeStdString()
  {
    super(StdStringJNI.create());
  }

  public void delete()
  {
    assertAllocated();
    StdStringJNI.delete(self);
    self = 0;
  }

  public String toString()
  {
    return new String(toByteArray());
  }

  public long length()
  {
    assertAllocated();
    return StdStringJNI.length(self);
  }

  public byte[] toByteArray()
  {
    long l = length();
    if (l > Integer.MAX_VALUE)
    {
      throw new ArrayIndexOutOfBoundsException("Native string is larger than the maximum Java array");
    }
    byte[] rc = new byte[(int) l];
    NativeBuffer.NativeBufferJNI.buffer_copy(StdStringJNI.c_str_ptr(self), 0, rc, 0, rc.length);
    return rc;
  }
}
