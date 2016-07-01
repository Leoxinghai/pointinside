package com.pointinside.android.api.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.ArrayList;

public class GoogleMyLocationEngine
  extends AbstractLocationEngine
  implements LocationListener
{
  public static final String STATUS_EXTRAS_PROVIDER_KEY = GoogleMyLocationEngine.class.getName() + ".PROVIDER";
  private static final String TAG = GoogleMyLocationEngine.class.getSimpleName();
  private final LocationManager mLocationManager;
  private final ArrayList<String> mProviders = new ArrayList();

  public GoogleMyLocationEngine(Context paramContext)
  {
    this.mLocationManager = ((LocationManager)paramContext.getSystemService("location"));
  }

  private void disableProviders()
  {
    this.mLocationManager.removeUpdates(this);
    this.mProviders.clear();
  }

  private boolean enableLocation(int paramInt)
  {
    boolean bool = false;
    switch (paramInt)
    {
    default:
      return false;
    case 1:
      bool = false | enableProvider("gps");
    }
    return bool | enableProvider("network");
  }

  private boolean enableProvider(String paramString)
  {
    if (!this.mProviders.contains(paramString))
    {
      if (this.mLocationManager.isProviderEnabled(paramString))
      {
        this.mLocationManager.requestLocationUpdates(paramString, 0L, 0.0F, this);
        this.mProviders.add(paramString);
        return true;
      }
      return false;
    }
    return true;
  }

  public void onAccuracyChanged(int paramInt1, int paramInt2)
  {
    enableLocation(paramInt2);
  }

  public void onLocationChanged(Location paramLocation)
  {
    notifyLocationChanged(paramLocation);
  }

  protected void onLocationDisabled()
  {
    disableProviders();
  }

  protected boolean onLocationEnabled()
  {
    return enableLocation(getAccuracy());
  }

  public void onProviderDisabled(String paramString) {}

  public void onProviderEnabled(String paramString) {}

  public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
  {
    paramBundle.putString(STATUS_EXTRAS_PROVIDER_KEY, paramString);
    notifyStatusChanged(paramInt, paramBundle);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.location.GoogleMyLocationEngine
 * JD-Core Version:    0.7.0.1
 */