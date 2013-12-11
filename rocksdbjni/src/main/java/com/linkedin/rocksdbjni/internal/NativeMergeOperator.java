package com.linkedin.rocksdbjni.internal;

import java.util.ArrayList;
import java.util.List;
import org.fusesource.hawtjni.runtime.JniArg;
import org.fusesource.hawtjni.runtime.JniClass;
import org.fusesource.hawtjni.runtime.JniField;
import org.fusesource.hawtjni.runtime.JniMethod;

import static org.fusesource.hawtjni.runtime.ArgFlag.CRITICAL;
import static org.fusesource.hawtjni.runtime.ArgFlag.NO_OUT;
import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;
import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;
import static org.fusesource.hawtjni.runtime.FieldFlag.POINTER_FIELD;
import static org.fusesource.hawtjni.runtime.MethodFlag.CONSTANT_INITIALIZER;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_DELETE;
import static org.fusesource.hawtjni.runtime.MethodFlag.CPP_NEW;


/**
 * @author Ankit Gupta
 */
public abstract class NativeMergeOperator extends NativeObject
{
  @JniClass(name="JNIMergeOperator", flags={STRUCT, CPP})
  static public class MergeOperatorJNI {

    static {
      NativeDB.LIBRARY.load();
      init();
    }

    @JniMethod(flags={CPP_NEW})
    public static final native long create();

    @JniMethod(flags={CPP_DELETE})
    public static final native void delete(
        long self
    );

    public static final native void memmove (
        @JniArg(cast="void *") long dest,
        @JniArg(cast="const void *", flags={NO_OUT, CRITICAL}) MergeOperatorJNI src,
        @JniArg(cast="size_t") long size);

    @JniField(cast="jobject", flags={POINTER_FIELD})
    long target;

    @JniField(cast="jmethodID", flags={POINTER_FIELD})
    long partial_merge_method;

    @JniField(cast="jmethodID", flags={POINTER_FIELD})
    long full_merge_method;

    @JniField(cast="const char *")
    long name;

    @JniMethod(flags={CONSTANT_INITIALIZER})
    private static final native void init();

    @JniField(flags={CONSTANT}, accessor="sizeof(struct JNIMergeOperator)")
    static int SIZEOF;
  }

  private NativeBuffer name_buffer;
  private long globalRef;

  public NativeMergeOperator() {
    super(MergeOperatorJNI.create());
    try {
      name_buffer = NativeBuffer.create(name());
      globalRef = NativeDB.DBJNI.NewGlobalRef(this);
      if( globalRef==0 ) {
        throw new RuntimeException("jni call failed: NewGlobalRef");
      }
      MergeOperatorJNI struct = new MergeOperatorJNI();

      struct.partial_merge_method = NativeDB.DBJNI.GetMethodID(this.getClass(), "partial_merge", "(JJJ)[B");
      if( struct.partial_merge_method==0 ) {
        throw new RuntimeException("jni call failed: GetMethodID");
      }

        struct.full_merge_method = NativeDB.DBJNI.GetMethodID(this.getClass(), "full_merge", "(JJ[J)[B");
      if( struct.full_merge_method==0 ) {
        throw new RuntimeException("jni call failed: GetMethodID");
      }

      struct.target = globalRef;
      struct.name = name_buffer.pointer();
      MergeOperatorJNI.memmove(self, struct, MergeOperatorJNI.SIZEOF);

    } catch (RuntimeException e) {
      delete();
      throw e;
    }
  }

  NativeMergeOperator(long ptr) {
    super(ptr);
  }

  public void delete() {
    if( name_buffer!=null ) {
      name_buffer.delete();
      name_buffer = null;
    }
    if( globalRef!=0 ) {
      NativeDB.DBJNI.DeleteGlobalRef(globalRef);
      globalRef = 0;
    }
  }

  private byte[] partial_merge(long ptr1, long ptr2, long ptr3) {
    NativeSlice s1 = new NativeSlice();
    s1.read(ptr1, 0);

    NativeSlice s2 = new NativeSlice();
    s2.read(ptr2, 0);

    NativeSlice s3 = new NativeSlice();
    s3.read(ptr3, 0);

    return partialMerge(s1.toByteArray(), s2.toByteArray(), s3.toByteArray());
  }

  private byte[] full_merge(long ptr1, long ptr2, long[] ptr3) {
    NativeSlice s1 = new NativeSlice();
    s1.read(ptr1, 0);

    NativeSlice s2 = null;

    if(ptr2 != 0)
    {
      s2 = new NativeSlice();
      s2.read(ptr2, 0);
    }

    List<byte[]> list = new ArrayList<byte[]>();
    for(long ptr : ptr3)
    {
      NativeSlice s3 = new NativeSlice();
      s3.read(ptr, 0);
      list.add(s3.toByteArray());
    }

    return fullMerge(s1.toByteArray(), s2 == null ? null : s2.toByteArray(), list);
  }

  public abstract String name();

  public abstract byte[] fullMerge(byte[] key,
                                   byte[] existing_value,
                                   List<byte[]> operandList);

  public abstract byte[] partialMerge(byte[] key,
                                      byte[] left_operand,
                                      byte[] right_operand);
}

