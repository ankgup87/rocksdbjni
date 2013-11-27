package org.fusesource.rocksdbjni.internal;


import java.util.List;


/**
 * @author: aigupta
 */
public interface MergeOperator
{
  public byte[] fullMerge(byte[] key,
                          byte[] existing_value,
                          List<byte[]> operandList);

  public byte[] partialMerge(byte[] key,
                             byte[] left_operand,
                             byte[] right_operand);

  public String name();
}
