package com.pointinside.android.api.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import java.util.Random;

public class DriftMyLocationEngine
  extends AbstractLocationEngine
{
  private static final int DRIFT_PERIOD = 60000;
  private static final int MAX_DRIFT_1E6 = 10000;
  private static final int MIN_DRIFT_1E6 = 3000;
  private static final Random sRandom = new Random();
  private final LocationEngineListener mBaseListener = new LocationEngineListener()
  {
    public void onLocationChanged(Location paramAnonymousLocation)
    {
      DriftMyLocationEngine.this.mHandler.sendApplyDrift(0L);
    }

    public void onStatusChanged(int paramAnonymousInt, Bundle paramAnonymousBundle) {}
  };
  private double mDriftLat;
  private double mDriftLong;
  private long mEndTime;
  private final LocationEngine mEngine;
  private final DriftHandler mHandler = new DriftHandler();
  private final Interpolator mInterpolator;
  private long mStartTime;

  public DriftMyLocationEngine(Context paramContext)
  {
    this.mEngine = new GoogleMyLocationEngine(paramContext);
    this.mInterpolator = new AccelerateDecelerateInterpolator();
  }

  private Location applyDrift(Location paramLocation)
  {
    Location localLocation = new Location(paramLocation);
    localLocation.setLatitude(0.001D + paramLocation.getLatitude());
    localLocation.setLongitude(0.0005D + paramLocation.getLongitude());
    return localLocation;
  }

  protected void onLocationDisabled()
  {
    this.mEngine.setLocationListener(null);
    this.mHandler.stopTracking();
    this.mEngine.disableLocation();
  }

  protected boolean onLocationEnabled()
  {
    this.mHandler.startTracking();
    this.mEngine.setLocationListener(this.mBaseListener);
    return this.mEngine.enableLocation();
  }

  private class DriftHandler
    extends Handler
  {
    private static final int MSG_APPLY_DRIFT =0;
    private volatile boolean mTracking;

    private DriftHandler() {}

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 1:
          if(mTracking)
          {
              notifyLocationChanged(applyDrift(mEngine.getLastFix()));
              sendApplyDrift(1000L);
              return;
          }
    	  break;
  	  default:

      }
    }

    public void sendApplyDrift(long paramLong)
    {
      removeMessages(0);
      sendEmptyMessageDelayed(0, paramLong);
    }

    public void startTracking()
    {
      this.mTracking = true;
      sendApplyDrift(0L);
    }

    public void stopTracking()
    {
      this.mTracking = false;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.location.DriftMyLocationEngine
 * JD-Core Version:    0.7.0.1
 */