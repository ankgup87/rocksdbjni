package org.fusesource.rocksdbjni.internal;


import java.io.File;
import java.io.IOException;


/**
 * @author: aigupta
 */
public interface DBFactory {

  public DB open(File path, NativeOptions options) throws IOException;

  public void destroy(File path, NativeOptions options) throws IOException;

  public void repair(File path, NativeOptions options) throws IOException;

}


