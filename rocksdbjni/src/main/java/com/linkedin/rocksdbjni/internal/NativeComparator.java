package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.*;

import static org.fusesource.hawtjni.runtime.FieldFlag.*;
import static org.fusesource.hawtjni.runtime.MethodFlag.*;
import static org.fusesource.hawtjni.runtime.ArgFlag.*;
import static org.fusesource.hawtjni.runtime.ClassFlag.*;

/**
 * Provides a java interface to the C++ leveldb::Comparator class.
 */
public abstract class NativeComparator extends NativeObject
{

  @JniClass(name = "JNIComparator", flags = { STRUCT, CPP })
  static public class ComparatorJNI
  {

    static
    {
      NativeDB.LIBRARY.load();
      init();
    }

    @JniMethod(flags = { CPP_NEW })
    public static final native long create();

    @JniMethod(flags = { CPP_DELETE })
    public static final native void delete(long ptr);

    public static final native void memmove(@JniArg(cast = "void *") long dest,
                                            @JniArg(cast = "const void *", flags = { NO_OUT, CRITICAL }) ComparatorJNI src,
                                            @JniArg(cast = "size_t") long size);

    public static final native void memmove(@JniArg(cast = "void *", flags = { NO_IN, CRITICAL }) ComparatorJNI dest,
                                            @JniArg(cast = "const void *") long src,
                                            @JniArg(cast = "size_t") long size);

    @JniField(cast = "jobject", flags = { POINTER_FIELD })
    long target;

    @JniField(cast = "jmethodID", flags = { POINTER_FIELD })
    long compare_method;

    @JniField(cast = "const char *")
    long name;

    @JniMethod(flags = { CONSTANT_INITIALIZER })
    private static final native void init();

    @JniField(flags = { CONSTANT }, accessor = "sizeof(struct JNIComparator)")
    static int SIZEOF;

    @JniField(flags = { CONSTANT }, cast = "const Comparator*", accessor = "rocksdb::BytewiseComparator()")
    private static long BYTEWISE_COMPARATOR;

  }

  private NativeBuffer name_buffer;
  private long globalRef;

  public NativeComparator()
  {
    super(ComparatorJNI.create());
    try
    {
      name_buffer = NativeBuffer.create(name());
      globalRef = NativeDB.DBJNI.NewGlobalRef(this);
      if (globalRef == 0)
      {
        throw new RuntimeException("jni call failed: NewGlobalRef");
      }
      ComparatorJNI struct = new ComparatorJNI();
      struct.compare_method = NativeDB.DBJNI.GetMethodID(this.getClass(), "compare", "(JJ)I");
      if (struct.compare_method == 0)
      {
        throw new RuntimeException("jni call failed: GetMethodID");
      }
      struct.target = globalRef;
      struct.name = name_buffer.pointer();
      ComparatorJNI.memmove(self, struct, ComparatorJNI.SIZEOF);

    }
    catch (RuntimeException e)
    {
      delete();
      throw e;
    }
  }

  public static final NativeComparator BYTEWISE_COMPARATOR = new NativeComparator(ComparatorJNI.BYTEWISE_COMPARATOR)
  {
    @Override
    public void delete()
    {
      // we won't really delete this one since it's static.
    }

    @Override
    public int compare(byte[] key1, byte[] key2)
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public String name()
    {
      throw new UnsupportedOperationException();
    }
  };

  NativeComparator(long ptr)
  {
    super(ptr);
  }

  public void delete()
  {
    if (name_buffer != null)
    {
      name_buffer.delete();
      name_buffer = null;
    }
    if (globalRef != 0)
    {
      NativeDB.DBJNI.DeleteGlobalRef(globalRef);
      globalRef = 0;
    }
  }

  private int compare(long ptr1, long ptr2)
  {
    NativeSlice s1 = new NativeSlice();
    s1.read(ptr1, 0);
    NativeSlice s2 = new NativeSlice();
    s2.read(ptr2, 0);
    return compare(s1.toByteArray(), s2.toByteArray());
  }

  public abstract int compare(byte[] key1, byte[] key2);

  public abstract String name();

}
