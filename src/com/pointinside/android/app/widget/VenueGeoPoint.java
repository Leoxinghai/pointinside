package com.pointinside.android.app.widget;

import com.google.android.gms.maps.model.LatLng;
import com.pointinside.android.app.util.ComparablePoint;
import java.util.Comparator;

public class VenueGeoPoint
  implements ComparablePoint, Comparator<ComparablePoint>
{
  private boolean mActive;
  private String mCityState;
  private int mType;
  private String mVenueName;
  private String mVenueUUID;
  private LatLng mLatLng;
  
  public VenueGeoPoint(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, 0, null, null, null);
  }
  
  public VenueGeoPoint(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2, String paramString3)
  {
    //super(paramInt1, paramInt2);
	this.mLatLng = new LatLng(paramInt1, paramInt2);
    this.mType = paramInt3;
    this.mVenueName = paramString1;
    this.mCityState = paramString2;
    this.mVenueUUID = paramString3;
    this.mActive = false;
  }
  
  public int compare(ComparablePoint paramComparablePoint1, ComparablePoint paramComparablePoint2)
  {
    int i = Integer.valueOf(paramComparablePoint1.getLongitudeE6()).compareTo(Integer.valueOf(paramComparablePoint2.getLongitudeE6()));
    if (i == 0) {
      i = -1 * Integer.valueOf(paramComparablePoint1.getLatitudeE6()).compareTo(Integer.valueOf(paramComparablePoint2.getLatitudeE6()));
    }
    return i;
  }
  
  public boolean getActive()
  {
    return this.mActive;
  }
  
  public String getCityState()
  {
    return this.mCityState;
  }
  
  public int getType()
  {
    return this.mType;
  }
  
  public String getVenueName()
  {
    return this.mVenueName;
  }
  
  public String getVenueUUID()
  {
    return this.mVenueUUID;
  }
  
  public void setActive(boolean paramBoolean)
  {
    this.mActive = paramBoolean;
  }

  public int getLatitudeE6() {
	  return (int)mLatLng.latitude;
  }
  public boolean equals(Comparator<ComparablePoint> object ) {
	  if( object == this)  return true;
	  return false;
  }

  public int getLongitudeE6() {
	  return (int)mLatLng.longitude;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.VenueGeoPoint
 * JD-Core Version:    0.7.0.1
 */