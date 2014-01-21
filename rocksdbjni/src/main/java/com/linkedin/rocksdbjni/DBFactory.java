package com.linkedin.rocksdbjni;

import java.io.File;
import java.io.IOException;

import com.linkedin.rocksdbjni.internal.Options;

public interface DBFactory
{

  public DB open(File path, Options options) throws IOException;

  public void destroy(File path, Options options) throws IOException;

  public void repair(File path, Options options) throws IOException;

}
