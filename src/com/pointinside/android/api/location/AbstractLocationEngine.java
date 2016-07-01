package com.pointinside.android.api.location;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public abstract class AbstractLocationEngine
  implements LocationEngine
{
  public static final String LOCATION_EXTRAS_ZONE_INDEX_KEY = AbstractLocationEngine.class.getName() + ".ZONE_INDEX";
  private static final String TAG = AbstractLocationEngine.class.getSimpleName();
  private int mAccuracy = 2;
  private Location mLastFix;
  private LocationEngineListener mListener;
  private boolean mLocationEnabled;

  public void disableLocation()
  {
    if (this.mLocationEnabled)
    {
      this.mLocationEnabled = false;
      onLocationDisabled();
    }
  }

  public boolean enableLocation()
  {
    if (!this.mLocationEnabled)
    {
      this.mLocationEnabled = onLocationEnabled();
      return this.mLocationEnabled;
    }
    return true;
  }

  public int getAccuracy()
  {
    return this.mAccuracy;
  }

  public Location getLastFix()
  {
    return this.mLastFix;
  }

  public boolean isLocationEnabled()
  {
    return this.mLocationEnabled;
  }

  protected final void notifyLocationChanged(Location paramLocation)
  {
    this.mLastFix = paramLocation;
    if (this.mListener != null) {
      this.mListener.onLocationChanged(paramLocation);
    }
  }

  protected final void notifyStatusChanged(int paramInt, Bundle paramBundle)
  {
    if (this.mListener != null) {
      this.mListener.onStatusChanged(paramInt, paramBundle);
    }
  }

  public void onAccuracyChanged(int paramInt1, int paramInt2)
  {
    if (isLocationEnabled())
    {
      Log.w(TAG, "Crude accuracy change from oldAccuracy=" + paramInt1 + " to newAccuracy=" + paramInt2);
      disableLocation();
      enableLocation();
    }
  }

  protected abstract void onLocationDisabled();

  protected abstract boolean onLocationEnabled();

  public void setAccuracy(int paramInt)
  {
    if ((paramInt != 2) && (paramInt != 1)) {
      throw new IllegalArgumentException();
    }
    if (this.mAccuracy != paramInt)
    {
      int i = this.mAccuracy;
      this.mAccuracy = paramInt;
      onAccuracyChanged(i, paramInt);
    }
  }

  public void setLocationListener(LocationEngineListener paramLocationEngineListener)
  {
    this.mListener = paramLocationEngineListener;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.location.AbstractLocationEngine
 * JD-Core Version:    0.7.0.1
 */