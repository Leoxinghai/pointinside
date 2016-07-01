package com.pointinside.android.api.maps;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PIMapPolygonZone
{
  private int mCurrentZoneIndex;
  private final int mDefaultZoneIndex;
  private int mHostZoneIndex;
  private final LinkedHashMap<Integer, HotLink> mHotLinks = new LinkedHashMap();
  private String mName;
  private PIMapPolygon mPolygon;
  private long mSpecialAreaId;
  private final LinkedList<Integer> mVenueZones = new LinkedList();

  public PIMapPolygonZone(long paramLong1, long paramLong2, String paramString, int paramInt1, int paramInt2, long paramLong3, int paramInt3, int paramInt4, int paramInt5)
  {
    this.mSpecialAreaId = paramLong2;
    this.mName = paramString;
    this.mCurrentZoneIndex = paramInt3;
    this.mDefaultZoneIndex = paramInt3;
    this.mHostZoneIndex = paramInt5;
    HotLink localHotLink = new HotLink(paramLong1, paramInt1, paramInt2, paramLong3, paramInt3, paramInt4);
    this.mHotLinks.put(Integer.valueOf(paramInt3), localHotLink);
    this.mVenueZones.add(Integer.valueOf(paramInt3));
  }

  void addHotLink(long paramLong1, int paramInt1, int paramInt2, long paramLong2, int paramInt3, int paramInt4)
  {
    HotLink localHotLink = new HotLink(paramLong1, paramInt1, paramInt2, paramLong2, paramInt3, paramInt4);
    this.mHotLinks.put(Integer.valueOf(paramInt3), localHotLink);
    this.mVenueZones.add(Integer.valueOf(paramInt3));
  }

  void addVenueZone(PIMapVenueZone paramPIMapVenueZone)
  {
    if (!this.mVenueZones.contains(Integer.valueOf(paramPIMapVenueZone.getZoneIndex()))) {
      this.mVenueZones.add(Integer.valueOf(paramPIMapVenueZone.getZoneIndex()));
    }
  }

  public int getDefaultZoneIndex()
  {
    return this.mDefaultZoneIndex;
  }

  public int getHostZoneIndex()
  {
    return this.mHostZoneIndex;
  }

  public long getHotlinkId()
  {
    return ((HotLink)this.mHotLinks.get(Integer.valueOf(this.mCurrentZoneIndex))).hotlinkId;
  }

  public String getName()
  {
    return this.mName;
  }

  public int getPixelX()
  {
    return ((HotLink)this.mHotLinks.get(Integer.valueOf(this.mCurrentZoneIndex))).pixelX;
  }

  public int getPixelY()
  {
    return ((HotLink)this.mHotLinks.get(Integer.valueOf(this.mCurrentZoneIndex))).pixelY;
  }

  public int getPlaceCount()
  {
    return ((HotLink)this.mHotLinks.get(Integer.valueOf(this.mCurrentZoneIndex))).placeCount;
  }

  public PIMapPolygon getPolygon()
  {
    return this.mPolygon;
  }

  public long getSpecialAreaId()
  {
    return this.mSpecialAreaId;
  }

  public long getZoneId()
  {
    return ((HotLink)this.mHotLinks.get(Integer.valueOf(this.mCurrentZoneIndex))).zoneId;
  }

  public int getZoneIndex()
  {
    return this.mCurrentZoneIndex;
  }

  boolean hasNextZone()
  {
    int i = this.mVenueZones.size();
    int j = this.mVenueZones.indexOf(Integer.valueOf(this.mCurrentZoneIndex));
    return (j != -1) && (j < i - 1);
  }

  boolean hasPreviousZone()
  {
    int i = this.mVenueZones.size();
    int j = this.mVenueZones.indexOf(Integer.valueOf(this.mCurrentZoneIndex));
    return (i > 0) && (j > 0);
  }

  boolean moveFirst()
  {
    if (this.mVenueZones.size() == 0) {
      return false;
    }
    this.mCurrentZoneIndex = this.mDefaultZoneIndex;
    return true;
  }

  boolean moveNext()
  {
    if (!hasNextZone()) {
      return false;
    }
    int i = this.mVenueZones.indexOf(Integer.valueOf(this.mCurrentZoneIndex));
    this.mCurrentZoneIndex = ((Integer)this.mVenueZones.get(i + 1)).intValue();
    return true;
  }

  boolean movePrevious()
  {
    if (!hasPreviousZone()) {
      return false;
    }
    int i = this.mVenueZones.indexOf(Integer.valueOf(this.mCurrentZoneIndex));
    this.mCurrentZoneIndex = ((Integer)this.mVenueZones.get(i - 1)).intValue();
    return true;
  }

  void moveToZone(int paramInt)
  {
    this.mCurrentZoneIndex = paramInt;
  }

  void setPolygon(PIMapPolygon paramPIMapPolygon)
  {
    this.mPolygon = paramPIMapPolygon;
  }

  private static class HotLink
  {
    private final long hotlinkId;
    private final int pixelX;
    private final int pixelY;
    private final int placeCount;
    private final long zoneId;
    private final int zoneIndex;

    HotLink(long paramLong1, int paramInt1, int paramInt2, long paramLong2, int paramInt3, int paramInt4)
    {
      this.hotlinkId = paramLong1;
      this.pixelX = paramInt1;
      this.pixelY = paramInt2;
      this.zoneId = paramLong2;
      this.zoneIndex = paramInt3;
      this.placeCount = paramInt4;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapPolygonZone
 * JD-Core Version:    0.7.0.1
 */