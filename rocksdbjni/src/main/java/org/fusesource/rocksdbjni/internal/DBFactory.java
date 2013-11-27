package org.fusesource.rocksdbjni.internal;


import java.io.File;
import java.io.IOException;


/**
 * @author: aigupta
 */
public interface DBFactory {

  public DB open(File path, Options options) throws IOException;

  public void destroy(File path, Options options) throws IOException;

  public void repair(File path, Options options) throws IOException;

}


