package com.pointinside.android.api.maps;

import android.app.Activity;
import android.os.Bundle;

public abstract class PIMapActivity
  extends Activity
{
  static PIMapActivity sPIMapActivity;
  PIMapView mPIMapView;

  public PIMapActivity()
  {
    sPIMapActivity = this;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mPIMapView != null) {
      this.mPIMapView.cleanUp();
    }
    sPIMapActivity = null;
  }

  protected void onPause()
  {
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
  }

  void setMapView(PIMapView paramPIMapView)
  {
    this.mPIMapView = paramPIMapView;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapActivity
 * JD-Core Version:    0.7.0.1
 */