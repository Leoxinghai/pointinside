package com.pointinside.android.api.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils
{
  public static void closeQuietly(Closeable paramCloseable)
  {
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException localIOException) {}
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.IOUtils
 * JD-Core Version:    0.7.0.1
 */