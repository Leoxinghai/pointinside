package com.pointinside.android.api.maps;

import java.util.HashMap;
import java.util.Map;

public class PIMapVenueZone
{
  private float mBearingPoint2Point1;
  private float mBearingPoint4Point3;
  private int mCountPixelPoint1Point2Y;
  private int mCountPixelPoint3Point4X;
  private String mDataPath;
  private int mDisplayOrder;
  private double mFeetPerPixelX;
  private double mFeetPerPixelY;
  private Map<Long, PIMapPolygonZone> mHotlinks;
  private String mImageName;
  private int mImageSizeX;
  private int mImageSizeY;
  private boolean mIsOverview;
  private String mName;
  private double mPoint1Latitude;
  private double mPoint1Longitude;
  private int mPoint1PixelX;
  private int mPoint1PixelY;
  private double mPoint2Latitude;
  private double mPoint2Longitude;
  private int mPoint2PixelX;
  private int mPoint2PixelY;
  private double mPoint3Latitude;
  private double mPoint3Longitude;
  private int mPoint3PixelX;
  private int mPoint3PixelY;
  private double mPoint4Latitude;
  private double mPoint4Longitude;
  private int mPoint4PixelX;
  private int mPoint4PixelY;
  private PIMapPolygon mPolygon;
  private String mTagText;
  private String mVenueUUID;
  private int mZoneIndex;
  private String mZoneUUID;

  public PIMapVenueZone(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, String paramString5, int paramInt2, int paramInt3, int paramInt4, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, float paramFloat1, float paramFloat2, String paramString6, boolean paramBoolean)
  {
    this.mName = paramString1;
    this.mVenueUUID = paramString2;
    this.mZoneUUID = paramString3;
    this.mTagText = paramString4;
    this.mDisplayOrder = paramInt1;
    this.mImageName = paramString5;
    this.mZoneIndex = paramInt2;
    this.mImageSizeX = paramInt3;
    this.mImageSizeY = paramInt4;
    this.mFeetPerPixelX = paramDouble1;
    this.mFeetPerPixelY = paramDouble2;
    this.mPoint1Latitude = paramDouble3;
    this.mPoint1Longitude = paramDouble4;
    this.mPoint2Latitude = paramDouble5;
    this.mPoint2Longitude = paramDouble6;
    this.mPoint3Latitude = paramDouble7;
    this.mPoint3Longitude = paramDouble8;
    this.mPoint4Latitude = paramDouble9;
    this.mPoint4Longitude = paramDouble10;
    this.mPoint1PixelX = paramInt5;
    this.mPoint1PixelY = paramInt6;
    this.mPoint2PixelX = paramInt7;
    this.mPoint2PixelY = paramInt8;
    this.mPoint3PixelX = paramInt9;
    this.mPoint3PixelY = paramInt10;
    this.mPoint4PixelX = paramInt11;
    this.mPoint4PixelY = paramInt12;
    this.mCountPixelPoint1Point2Y = paramInt13;
    this.mCountPixelPoint3Point4X = paramInt14;
    this.mBearingPoint2Point1 = paramFloat1;
    this.mBearingPoint4Point3 = paramFloat2;
    this.mDataPath = paramString6;
    this.mIsOverview = paramBoolean;
  }

  public float getBearingPoint2Point1()
  {
    return this.mBearingPoint2Point1;
  }

  public float getBearingPoint4Point3()
  {
    return this.mBearingPoint4Point3;
  }

  public int getCountPixelPoint1Point2Y()
  {
    return this.mCountPixelPoint1Point2Y;
  }

  public int getCountPixelPoint3Point4X()
  {
    return this.mCountPixelPoint3Point4X;
  }

  public String getDataPath()
  {
    return this.mDataPath;
  }

  public int getDisplayOrder()
  {
    return this.mDisplayOrder;
  }

  public double getFeetPerPixelX()
  {
    return this.mFeetPerPixelX;
  }

  public double getFeetPerPixelY()
  {
    return this.mFeetPerPixelY;
  }

  public Map<Long, PIMapPolygonZone> getHotlinks()
  {
    return this.mHotlinks;
  }

  public String getImageName()
  {
    return this.mImageName;
  }

  public int getImageSizeX()
  {
    return this.mImageSizeX;
  }

  public int getImageSizeY()
  {
    return this.mImageSizeY;
  }

  public String getName()
  {
    return this.mName;
  }

  public double getPoint1Latitude()
  {
    return this.mPoint1Latitude;
  }

  public double getPoint1Longitude()
  {
    return this.mPoint1Longitude;
  }

  public int getPoint1PixelX()
  {
    return this.mPoint1PixelX;
  }

  public int getPoint1PixelY()
  {
    return this.mPoint1PixelY;
  }

  public double getPoint2Latitude()
  {
    return this.mPoint2Latitude;
  }

  public double getPoint2Longitude()
  {
    return this.mPoint2Longitude;
  }

  public int getPoint2PixelX()
  {
    return this.mPoint2PixelX;
  }

  public int getPoint2PixelY()
  {
    return this.mPoint2PixelY;
  }

  public double getPoint3Latitude()
  {
    return this.mPoint3Latitude;
  }

  public double getPoint3Longitude()
  {
    return this.mPoint3Longitude;
  }

  public int getPoint3PixelX()
  {
    return this.mPoint3PixelX;
  }

  public int getPoint3PixelY()
  {
    return this.mPoint3PixelY;
  }

  public double getPoint4Latitude()
  {
    return this.mPoint4Latitude;
  }

  public double getPoint4Longitude()
  {
    return this.mPoint4Longitude;
  }

  public int getPoint4PixelX()
  {
    return this.mPoint4PixelX;
  }

  public int getPoint4PixelY()
  {
    return this.mPoint4PixelY;
  }

  public PIMapPolygon getPolygon()
  {
    return this.mPolygon;
  }

  public String getTagText()
  {
    return this.mTagText;
  }

  public String getUUID()
  {
    return getVenueUUID();
  }

  public String getVenueUUID()
  {
    return this.mVenueUUID;
  }

  public int getZoneIndex()
  {
    return this.mZoneIndex;
  }

  public String getZoneUUID()
  {
    return this.mZoneUUID;
  }

  public boolean isOverview()
  {
    return this.mIsOverview;
  }

  void setHotlinks(HashMap<Long, PIMapPolygonZone> paramHashMap)
  {
    this.mHotlinks = paramHashMap;
  }

  void setPolygon(PIMapPolygon paramPIMapPolygon)
  {
    this.mPolygon = paramPIMapPolygon;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapVenueZone
 * JD-Core Version:    0.7.0.1
 */