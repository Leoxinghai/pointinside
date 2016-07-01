package com.pointinside.android.api.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream
  extends FilterInputStream
{
  private long count = 0L;

  public CountingInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }

  public long bytesRead()
  {
    return this.count;
  }

  public void mark(int paramInt) {}

  public boolean markSupported()
  {
    return false;
  }

  public int read()
    throws IOException
  {
    int i = super.read();
    if (i != -1) {
      this.count = (1L + this.count);
    }
    return i;
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i >= 0) {
      this.count += i;
    }
    return i;
  }

  public void reset()
    throws IOException
  {
    try
    {
      throw new IOException();
    }
    finally {}
  }

  public long skip(long paramLong)
    throws IOException
  {
    throw new IOException();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.CountingInputStream
 * JD-Core Version:    0.7.0.1
 */