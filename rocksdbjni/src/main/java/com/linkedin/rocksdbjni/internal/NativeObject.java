
package com.linkedin.rocksdbjni.internal;

/**
 * A helper base class which is used to track a pointer to a native
 * structure or class.
 *
 * @author Ankit Gupta
 */
class NativeObject {

    protected long self;

    protected NativeObject(long self) {
        this.self = self;
        if( self ==0 ) {
            throw new OutOfMemoryError("Failure allocating native heap memory");
        }
    }

    long pointer() {
        return self;
    }

    public boolean isAllocated() {
        return self !=0;
    }

    protected void assertAllocated() {
        assert isAllocated() : "This object has been deleted";
    }

}
