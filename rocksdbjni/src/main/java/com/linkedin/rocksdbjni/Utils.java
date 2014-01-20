package com.linkedin.rocksdbjni;

public class Utils
{

  public static void checkArgNotNull(Object value, String name)
  {
    if (value == null)
    {
      throw new IllegalArgumentException("The " + name + " argument cannot be null");
    }
  }

}
