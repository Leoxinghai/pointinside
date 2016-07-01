package com.pointinside.android.app.util;

public class LatLongRect
{
  private double lat1;
  private double lat2;
  private double long1;
  private double long2;

  public LatLongRect()
  {
    reset();
  }

  public LatLongRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    set(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
  }

  public String flatten()
  {
    return this.lat1 + "," + this.long1 + "-" + this.lat2 + "," + this.long2;
  }

  public double getLat1()
  {
    return this.lat1;
  }

  public double getLat2()
  {
    return this.lat2;
  }

  public double getLatitudeCenter()
  {
    return this.lat1 + getLatitudeSpan() / 2.0D;
  }

  public double getLatitudeSpan()
  {
    return this.lat2 - this.lat1;
  }

  public double getLong1()
  {
    return this.long1;
  }

  public double getLong2()
  {
    return this.long2;
  }

  public double getLongitudeCenter()
  {
    return this.long1 + getLongitudeSpan() / 2.0D;
  }

  public double getLongitudeSpan()
  {
    return this.long2 - this.long1;
  }

  public void reset()
  {
    this.long2 = 0.0D;
    this.long1 = 0.0D;
    this.lat2 = 0.0D;
    this.lat1 = 0.0D;
  }

  public void set(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    this.lat1 = paramDouble1;
    this.long1 = paramDouble2;
    this.lat2 = paramDouble3;
    this.long2 = paramDouble4;
  }

  public void setLat1(double paramDouble)
  {
    this.lat1 = paramDouble;
  }

  public void setLat2(double paramDouble)
  {
    this.lat2 = paramDouble;
  }

  public void setLong1(double paramDouble)
  {
    this.long1 = paramDouble;
  }

  public void setLong2(double paramDouble)
  {
    this.long2 = paramDouble;
  }

  public String toString()
  {
    return flatten();
  }

  public void unflatten(String s)
  {
      String as[] = s.split("\\-", 2);
      int i = 0;
      do
      {
          if(i >= 2)
              return;
          String as1[] = as[i].split(",", 2);
          double d = Double.parseDouble(as1[0]);
          double d1 = Double.parseDouble(as1[1]);
          if(i == 0)
          {
              lat1 = d;
              long1 = d1;
          } else
          {
              lat2 = d;
              long2 = d1;
          }
          i++;
      } while(true);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.LatLongRect
 * JD-Core Version:    0.7.0.1
 */