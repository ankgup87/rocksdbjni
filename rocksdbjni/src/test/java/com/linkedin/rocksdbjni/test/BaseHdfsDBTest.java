package com.linkedin.rocksdbjni.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.log4j.Logger;

public class BaseHdfsDBTest extends TestCase
{
  private Logger logger = Logger.getLogger(BaseHdfsDBTest.class);

  private MiniDFSCluster dfsCluster;
  private MiniMRCluster mrCluster;
  private File logPaths;
  private FileSystem fs;

  public BaseHdfsDBTest() throws IOException
  {
    this(1, 1, true);
  }

  public BaseHdfsDBTest(int numTaskTrackers, int numDataNodes, boolean testLocal) throws IOException
  {

    if (numTaskTrackers < 1)
    {
      throw new IllegalArgumentException("[TEST] Invalid task trackers value, must be greater than 0");
    }
    if (numDataNodes < 1)
    {
      throw new IllegalArgumentException("[TEST] Invalid data nodes value, must be greater than 0");
    }

    this.logPaths = new File("test-logs");
    System.setProperty("hadoop.log.dir", this.logPaths.getPath());
    logger.info("[TEST] Test logs at path " + this.logPaths);

    logger.info("[TEST] Starting Mini DFS Cluster");
    dfsCluster = new MiniDFSCluster(new JobConf(), numDataNodes, true, null);
    fs = dfsCluster.getFileSystem();

    logger.info("[TEST] Starting Mini MR Cluster");
    mrCluster = new MiniMRCluster(numTaskTrackers, fs.getName(), 1);
  }

  public FileSystem getFileSystem()
  {
    return fs;
  }

  public MiniMRCluster getMRCluster()
  {
    return mrCluster;
  }

  public JobConf getJobConf() throws Exception
  {
    return getMRCluster().createJobConf();
  }

  public File getLogPath()
  {
    return this.logPaths;
  }

  public void afterClass() throws Exception
  {
    if (dfsCluster != null)
    {
      logger.info("[TEST] Shutting down Mini DFS Cluster");
      dfsCluster.shutdown();
      dfsCluster = null;
    }

    if (mrCluster != null)
    {
      logger.info("[TEST] Shutting down Mini MR Cluster");
      mrCluster.shutdown();
      mrCluster = null;
    }
  }

}
