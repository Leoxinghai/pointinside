package com.pointinside.android.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.MapView;

import com.pointinside.android.api.maps.PIMapLocation;
import com.pointinside.android.api.maps.PIMapView;
import com.pointinside.android.app.PointInside;
import android.graphics.Rect;

public class LocationHelper
{
  private static final String TAG = LocationHelper.class.getSimpleName();
  
  public static LatLongRect createViewPort(MapView paramMapView)
  {
	  /*
    LatLng localGeoPoint = paramMapView.getClipBounds();//getMapCenter();
    double d1 = toDouble(localGeoPoint.getLatitudeE6());
    double d2 = toDouble(localGeoPoint.getLongitudeE6());
    double d3 = toDouble(paramMapView.getLatitudeSpan() / 2);
    double d4 = toDouble(paramMapView.getLongitudeSpan() / 2);
    return new LatLongRect(d1 - d3, d2 - d4, d1 + d3, d2 + d4);
    */
	  Rect rect = paramMapView.getClipBounds();
	  return new LatLongRect(rect.left,rect.top,rect.right,rect.bottom);
	  
  }
  

  public static LatLongRect getMapArea()
  {
      LatLongRect latlongrect = PointInside.getInstance().getViewport();
      Location location = PointInside.getInstance().getUserLocation();
      if(location != null)
      {
          double d = location.getLatitude();
          double d1 = location.getLongitude();
          if(latlongrect == null)
          {
              LatLongRect latlongrect1 = new LatLongRect(d - 1.0D, d1 - 1.0D, d + 1.0D, 1.0D + d1);
              Log.w(TAG, (new StringBuilder("Bogus search area guess of ")).append(latlongrect1).toString());
              return latlongrect1;
          }
      }
      return latlongrect;
  }
  public static String getUserDistanceFromLabel(Context paramContext, double paramDouble1, double paramDouble2)
  {
    Location localLocation = PointInside.getInstance().getUserLocation();
    if (localLocation != null)
    {
      float[] arrayOfFloat = new float[1];
      Location.distanceBetween(localLocation.getLatitude(), localLocation.getLongitude(), paramDouble1, paramDouble2, arrayOfFloat);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Float.valueOf(DistanceUtils.kilometersToMiles(arrayOfFloat[0] / 1000.0F));
      String str = String.format("%.02f", arrayOfObject);
      return paramContext.getResources().getString(2131099756, new Object[] { str });
    }
    return null;
  }
  
  public static void setViewPort(MapView paramMapView)
  {

//    LatLng localGeoPoint = paramMapView.get .getgetMapCenter();
	  int location[] = new int[6];
	  paramMapView.getLocationInWindow(location);
	  Rect r = new Rect();
	  paramMapView.getLocalVisibleRect(r);
//    double d1 = toDouble(localGeoPoint.getLatitudeE6());
//    double d2 = toDouble(localGeoPoint.getLongitudeE6());
//    double d3 = toDouble(paramMapView.getLatitudeSpan() / 2);
//    double d4 = toDouble(paramMapView.getLongitudeSpan() / 2);
//    PointInside.getInstance().setViewport(d1 - d3, d2 - d4, d1 + d3, d2 + d4);

	    double d1 = 30.0;
	    double d2 = 131;
	    double d3 = 31.2;
	    double d4 = 134;
    PointInside.getInstance().setViewport(d1 - d3, d2 - d4, d1 + d3, d2 + d4);
  }
  
  public static void setViewPort(PIMapView paramPIMapView)
  {
    PIMapLocation localPIMapLocation = paramPIMapView.getMapCenter();
    double d1 = localPIMapLocation.getLatitude();
    double d2 = localPIMapLocation.getLongitude();
    double d3 = toDouble(paramPIMapView.getLatitudeSpan() / 2);
    double d4 = toDouble(paramPIMapView.getLongitudeSpan() / 2);
    PointInside.getInstance().setViewport(d1 - d3, d2 - d4, d1 + d3, d2 + d4);
  }
  
  public static double toDouble(int paramInt)
  {
    return paramInt / 1000000.0D;
  }
  
  public static int toInt(double paramDouble)
  {
    return (int)(1000000.0D * paramDouble);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.LocationHelper
 * JD-Core Version:    0.7.0.1
 */