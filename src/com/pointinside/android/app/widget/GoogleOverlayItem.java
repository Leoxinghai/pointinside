package com.pointinside.android.app.widget;

import android.graphics.drawable.Drawable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.OverlayItem;
import com.pointinside.android.app.util.ComparablePoint;
import java.util.Comparator;

public class GoogleOverlayItem
//  extends OverlayItem
  implements ComparablePoint, Comparator<ComparablePoint>
{
  private boolean mActive;
  private String mType;
  private String mVenueUUID;
  private LatLng mLatLng;
  private Marker mMarker;
  
  public GoogleOverlayItem(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
  {
    this(new LatLng(paramInt1, paramInt2), paramString1, paramString2, paramString3);
  }
  
  public GoogleOverlayItem(double paramInt1, double paramInt2, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this(new LatLng(paramInt1, paramInt2), paramString1, paramString2, paramString3, paramString4);
  }
  
  public GoogleOverlayItem(LatLng paramGeoPoint, String paramString1, String paramString2, String paramString3)
  {
    this(paramGeoPoint, paramString1, paramString2, paramString3, String.valueOf(4));
  }
  
  public GoogleOverlayItem(LatLng paramGeoPoint, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    //super(paramGeoPoint, paramString1, paramString2);
//	  System.out.println("GoogleOverlayItem." + paramGeoPoint.latitude+":"+paramGeoPoint.longitude+":"+paramString2);
	  mLatLng = paramGeoPoint;
    this.mVenueUUID = paramString3;
    this.mType = paramString4;
  }
  
  protected Drawable boundCenterBottom(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    int k = i / 2;
    int m = i - k;
    paramDrawable.setBounds(-k, -j, m, 0);
    return paramDrawable;
  }
  
  public int compare(ComparablePoint paramComparablePoint1, ComparablePoint paramComparablePoint2)
  {
    int i = Integer.valueOf(paramComparablePoint1.getLongitudeE6()).compareTo(Integer.valueOf(paramComparablePoint2.getLongitudeE6()));
    if (i == 0) {
      i = -1 * Integer.valueOf(paramComparablePoint1.getLatitudeE6()).compareTo(Integer.valueOf(paramComparablePoint2.getLatitudeE6()));
    }
    return i;
  }
  
  public String getGeoCode()
  {
    return this.mLatLng.latitude + "," + this.mLatLng.longitude;
  }
  
  public int getLatitudeE6()
  {
    return (int)this.mLatLng.latitude;
  }
  
  public int getLongitudeE6()
  {
    return (int)this.mLatLng.longitude;
  }
  
  public String getType()
  {
    return this.mType;
  }
  
  public String getVenueUUID()
  {
    return this.mVenueUUID;
  }
  
  public boolean isActive()
  {
    return this.mActive;
  }
  
  public void setActive(boolean paramBoolean)
  {
    this.mActive = paramBoolean;
  }
  
  public void setMarker(Drawable paramDrawable)
  {
    //super.setMarker(boundCenterBottom(paramDrawable));
	  MarkerOptions options = new MarkerOptions();
	  //options.
	  //mMarker = new Marker(paramDrawable);
  }
  
  public void setType(String paramString)
  {
    this.mType = paramString;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.GoogleOverlayItem
 * JD-Core Version:    0.7.0.1
 */