package com.linkedin.rocksdbjni;

import java.util.List;

public interface DBMergeOperator
{
  public byte[] fullMerge(byte[] key, byte[] existing_value, List<byte[]> operandList);

  public byte[] partialMerge(byte[] key, byte[] left_operand, byte[] right_operand);

  public String name();
}
