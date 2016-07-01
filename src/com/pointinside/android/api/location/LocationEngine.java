package com.pointinside.android.api.location;

import android.location.Location;

public abstract interface LocationEngine
{
  public abstract void disableLocation();

  public abstract boolean enableLocation();

  public abstract Location getLastFix();

  public abstract boolean isLocationEnabled();

  public abstract void setLocationListener(LocationEngineListener paramLocationEngineListener);
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.location.LocationEngine
 * JD-Core Version:    0.7.0.1
 */