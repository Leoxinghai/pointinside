package com.pointinside.android.api.maps;

public class PixelCoordinate
  implements IPoint
{
  final float altitude;
  final double latitude;
  final double longitude;
  final long specialAreaId;
  final int x;
  final int y;

  public PixelCoordinate(long paramLong, float paramFloat, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2)
  {
    this.specialAreaId = paramLong;
    this.altitude = paramFloat;
    this.latitude = paramDouble1;
    this.longitude = paramDouble2;
    this.x = paramInt1;
    this.y = paramInt2;
  }

  public PixelCoordinate(PixelCoordinate paramPixelCoordinate)
  {
    this.specialAreaId = paramPixelCoordinate.specialAreaId;
    this.altitude = paramPixelCoordinate.altitude;
    this.latitude = paramPixelCoordinate.latitude;
    this.longitude = paramPixelCoordinate.longitude;
    this.x = paramPixelCoordinate.x;
    this.y = paramPixelCoordinate.y;
  }

  public double getLatitude()
  {
    return this.latitude;
  }

  public double getLongitude()
  {
    return this.longitude;
  }

  public int getX()
  {
    return this.x;
  }

  public int getY()
  {
    return this.y;
  }

  public boolean pixelsMatch(PixelCoordinate paramPixelCoordinate)
  {
    return (this.x == paramPixelCoordinate.x) && (this.y == paramPixelCoordinate.y);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PixelCoordinate
 * JD-Core Version:    0.7.0.1
 */