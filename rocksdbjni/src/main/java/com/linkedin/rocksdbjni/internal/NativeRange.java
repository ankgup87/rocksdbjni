
package com.linkedin.rocksdbjni.internal;

import org.fusesource.hawtjni.runtime.*;

import static org.fusesource.hawtjni.runtime.ArgFlag.*;
import static org.fusesource.hawtjni.runtime.ClassFlag.CPP;
import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;
import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;
import static org.fusesource.hawtjni.runtime.FieldFlag.FIELD_SKIP;
import static org.fusesource.hawtjni.runtime.MethodFlag.CONSTANT_INITIALIZER;

/**
 * Provides a java interface to the C++ leveldb::ReadOptions class.
 *
 * @author Ankit Gupta
 */
public class NativeRange {

    @JniClass(name="rocksdb::Range", flags={STRUCT, CPP})
    static public class RangeJNI {

        static {
            NativeDB.LIBRARY.load();
            init();
        }

        public static final native void memmove (
                @JniArg(cast="void *") long dest,
                @JniArg(cast="const void *", flags={NO_OUT, CRITICAL}) RangeJNI src,
                @JniArg(cast="size_t") long size);

        public static final native void memmove (
                @JniArg(cast="void *", flags={NO_IN, CRITICAL}) RangeJNI dest,
                @JniArg(cast="const void *") long src,
                @JniArg(cast="size_t") long size);


        @JniMethod(flags={CONSTANT_INITIALIZER})
        private static final native void init();

        @JniField(flags={CONSTANT}, accessor="sizeof(struct rocksdb::Range)")
        static int SIZEOF;

        @JniField
        NativeSlice start = new NativeSlice();
        @JniField(flags={FIELD_SKIP})
        NativeBuffer start_buffer;

        @JniField
        NativeSlice limit = new NativeSlice();
        @JniField(flags={FIELD_SKIP})
        NativeBuffer limit_buffer;

        public RangeJNI(NativeRange range) {
            start_buffer = NativeBuffer.create(range.start());
            start.set(start_buffer);
            try {
                limit_buffer = NativeBuffer.create(range.limit());
            } catch (OutOfMemoryError e) {
                start_buffer.delete();
                throw e;
            }
            limit.set(limit_buffer);
        }

        public void delete() {
            start_buffer.delete();
            limit_buffer.delete();
        }

        static NativeBuffer arrayCreate(int dimension) {
            return NativeBuffer.create(dimension*SIZEOF);
        }

        void arrayWrite(long buffer, int index) {
            RangeJNI.memmove(PointerMath.add(buffer, SIZEOF * index), this, SIZEOF);
        }

        void arrayRead(long buffer, int index) {
            RangeJNI.memmove(this, PointerMath.add(buffer, SIZEOF * index), SIZEOF);
        }

    }

    final private byte[] start;
    final private byte[] limit;

    public byte[] limit() {
        return limit;
    }

    public byte[] start() {
        return start;
    }

    public NativeRange(byte[] start, byte[] limit) {
        NativeDB.checkArgNotNull(start, "start");
        NativeDB.checkArgNotNull(limit, "limit");
        this.limit = limit;
        this.start = start;
    }
}
