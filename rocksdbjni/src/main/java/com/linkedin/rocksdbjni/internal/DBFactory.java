package com.linkedin.rocksdbjni.internal;


import java.io.File;
import java.io.IOException;


/**
 * @author Ankit Gupta
 */
public interface DBFactory {

  public DB open(File path, Options options) throws IOException;

  public void destroy(File path, Options options) throws IOException;

  public void repair(File path, Options options) throws IOException;

}

