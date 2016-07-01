package com.pointinside.android.api.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FrobInputStream
  extends FilterInputStream
{
  private static final byte[] XOR_KEY = "yKlahNnjW7JzAkVZdeFPlJ9WbM7NDGhqdxjSMCi6O61Yx1NZ".getBytes();
  private int keyOffset = 0;
  private int markedKeyOffset = -1;

  public FrobInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }

  private byte frob(byte paramByte)
  {
    byte[] arrayOfByte = XOR_KEY;
    int i = this.keyOffset;
    this.keyOffset = (i + 1);
    byte b = (byte)(paramByte ^ arrayOfByte[i]);
    if (this.keyOffset >= XOR_KEY.length) {
      this.keyOffset = 0;
    }
    return b;
  }

  public void mark(int paramInt)
  {
      super.mark(paramInt);
      this.markedKeyOffset = this.keyOffset;
      return;
  }

  public int read()
    throws IOException
  {
    int i = this.in.read();
    if (i >= 0) {
      i = frob((byte)i);
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
    int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i >= 0) {}
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return i;
      }
      paramArrayOfByte[j] = frob(paramArrayOfByte[j]);
    }
  }

  public void reset()
    throws IOException
  {
      super.reset();
      this.keyOffset = this.markedKeyOffset;
      this.markedKeyOffset = -1;
      return;
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l = super.skip(paramLong);
    this.keyOffset = ((int)((l + this.keyOffset) % XOR_KEY.length));
    return l;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.FrobInputStream
 * JD-Core Version:    0.7.0.1
 */