package com.pointinside.android.api.maps;

import java.util.ArrayList;

public class PIMapPolygon
{
  private final ArrayList<PixelCoordinate> mCoords = new ArrayList();
  private final PIMapVenueZone mZone;

  public PIMapPolygon(PIMapVenueZone paramPIMapVenueZone)
  {
    if (paramPIMapVenueZone == null) {
      throw new IllegalArgumentException("zone must not be null");
    }
    this.mZone = paramPIMapVenueZone;
  }

  public void addCoordinate(PixelCoordinate paramPixelCoordinate)
  {
    this.mCoords.add(paramPixelCoordinate);
  }

  public void close()
  {
    if (!this.mCoords.isEmpty())
    {
      PixelCoordinate localPixelCoordinate = getCoordinateAt(0);
      if (!localPixelCoordinate.pixelsMatch(getCoordinateAt(-1 + getCoordinateCount()))) {
        addCoordinate(new PixelCoordinate(localPixelCoordinate));
      }
    }
  }

  public PixelCoordinate getCoordinateAt(int paramInt)
  {
    return (PixelCoordinate)this.mCoords.get(paramInt);
  }

  public int getCoordinateCount()
  {
    return this.mCoords.size();
  }

  public PIMapVenueZone getZone()
  {
    return this.mZone;
  }

  public boolean hitTest(int paramInt1, int paramInt2)
  {
    return PIMapUtil.isPointInPolygon(paramInt1, paramInt2, (IPoint[])this.mCoords.toArray(new PixelCoordinate[this.mCoords.size()]));
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('{');
    int i = getCoordinateCount();
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        localStringBuilder.append('}');
        return localStringBuilder.toString();
      }
      PixelCoordinate localPixelCoordinate = getCoordinateAt(j);
      localStringBuilder.append('(');
      localStringBuilder.append(localPixelCoordinate.getX());
      localStringBuilder.append(',');
      localStringBuilder.append(localPixelCoordinate.getY());
      localStringBuilder.append(')');
    }
  }

  PIMapPolygon translate(PIMapVenueZone paramPIMapVenueZone)
  {
    PIMapPolygon localPIMapPolygon = new PIMapPolygon(paramPIMapVenueZone);
    int i = getCoordinateCount();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return localPIMapPolygon;
      }
      PixelCoordinate localPixelCoordinate = getCoordinateAt(j);
      PIMapLocation localPIMapLocation = PIMapLocation.getXYOfLatLon(paramPIMapVenueZone, localPixelCoordinate.getLatitude(), localPixelCoordinate.getLongitude());
      localPIMapPolygon.addCoordinate(new PixelCoordinate(localPixelCoordinate.specialAreaId, localPixelCoordinate.altitude, localPixelCoordinate.getLatitude(), localPixelCoordinate.getLongitude(), localPIMapLocation.getPixelX(), localPIMapLocation.getPixelY()));
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapPolygon
 * JD-Core Version:    0.7.0.1
 */