package com.pointinside.android.api.location;

import android.location.Location;
import android.os.Bundle;

public abstract interface LocationEngineListener
{
  public abstract void onLocationChanged(Location paramLocation);

  public abstract void onStatusChanged(int paramInt, Bundle paramBundle);
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.location.LocationEngineListener
 * JD-Core Version:    0.7.0.1
 */