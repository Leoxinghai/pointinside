package com.pointinside.android.api.maps;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class PIMapOverlayItem
  implements PIMapAnnotation
{
  private Rect mHitBounds;
  protected PIMapLocation mLocation;
  protected Drawable mMarker;
  private long mPromoId = -1L;
  private int mPromoType = -1;
  private Rect mRelativeBounds;
  private final long mSpecialAreaId;
  protected String mSubTitle;
  protected String mTitle;
  private final int mZoneIndex;

  public PIMapOverlayItem(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, int paramInt)
  {
    this.mLocation = paramPIMapLocation;
    this.mTitle = paramString1;
    this.mSubTitle = paramString2;
    this.mZoneIndex = paramInt;
    this.mSpecialAreaId = 0L;
  }

  public PIMapOverlayItem(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, int paramInt, long paramLong)
  {
    this.mLocation = paramPIMapLocation;
    this.mTitle = paramString1;
    this.mSubTitle = paramString2;
    this.mZoneIndex = paramInt;
    this.mSpecialAreaId = paramLong;
  }

  public static void setState(Drawable paramDrawable, int paramInt) {}

  Rect getHitBounds()
  {
    return this.mHitBounds;
  }

  public PIMapLocation getLocation()
  {
    return this.mLocation;
  }

  public Drawable getMarker(int paramInt)
  {
    return this.mMarker;
  }

  public Rect getMarkerBounds()
  {
    return this.mRelativeBounds;
  }

  public int getPlaceTypeId()
  {
    return -1;
  }

  public String getPlaceTypeName()
  {
    return null;
  }

  public long getPromoId()
  {
    return this.mPromoId;
  }

  public int getPromoType()
  {
    return this.mPromoType;
  }

  public int getServiceTypeId()
  {
    return -1;
  }

  public String getServiceTypeName()
  {
    return null;
  }

  public String getServiceTypeUUID()
  {
    return null;
  }

  public long getSpecialAreaId()
  {
    return this.mSpecialAreaId;
  }

  public String getSubTitle()
  {
    return this.mSubTitle;
  }

  public String getTitle()
  {
    return this.mTitle;
  }

  public String getUUID()
  {
    return null;
  }

  public int getWormholeTypeId()
  {
    return -1;
  }

  public int getZoneIndex()
  {
    return this.mZoneIndex;
  }

  public boolean isWormhole()
  {
    return false;
  }

  public String routableAddress()
  {
    return null;
  }

  void setHitBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.mHitBounds == null) {
      this.mHitBounds = new Rect();
    }
    this.mHitBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setLocation(PIMapLocation paramPIMapLocation)
  {
    this.mLocation = paramPIMapLocation;
  }

  public void setMarker(Drawable paramDrawable)
  {
    this.mMarker = paramDrawable;
    if (paramDrawable != null)
    {
      this.mMarker.setDither(true);
      this.mRelativeBounds = paramDrawable.copyBounds();
      return;
    }
    this.mRelativeBounds = null;
  }

  public void setProjection(PIMapView paramPIMapView)
  {
    if (this.mLocation != null) {
      this.mLocation.translate(paramPIMapView, this.mZoneIndex);
    }
  }

  public void setPromoId(long paramLong)
  {
    this.mPromoId = paramLong;
  }

  public void setPromoType(int paramInt)
  {
    this.mPromoType = paramInt;
  }

  public void setSubTitle(String paramString)
  {
    this.mSubTitle = paramString;
  }

  public void setTitle(String paramString)
  {
    this.mTitle = paramString;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapOverlayItem
 * JD-Core Version:    0.7.0.1
 */