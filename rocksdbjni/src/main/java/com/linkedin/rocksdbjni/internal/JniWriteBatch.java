
package com.linkedin.rocksdbjni.internal;

import org.iq80.leveldb.WriteBatch;

/**
 * @author Ankit Gupta
 */
public class JniWriteBatch implements WriteBatch {

    private final NativeWriteBatch writeBatch;

    JniWriteBatch(NativeWriteBatch writeBatch) {
        this.writeBatch = writeBatch;
    }

    public void close() {
        writeBatch.delete();
    }

    public WriteBatch put(byte[] key, byte[] value) {
        writeBatch.put(key, value);
        return this;
    }

    public WriteBatch delete(byte[] key) {
        writeBatch.delete(key);
        return this;
    }

    public NativeWriteBatch writeBatch() {
        return writeBatch;
    }
}
