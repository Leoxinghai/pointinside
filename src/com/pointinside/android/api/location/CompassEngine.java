package com.pointinside.android.api.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassEngine
  implements SensorEventListener
{
  private boolean mCompassEnabled;
  private CompassEngineListener mListener;
  private SensorManager mSensorManager;

  public CompassEngine(Context paramContext)
  {
    this.mSensorManager = ((SensorManager)paramContext.getSystemService("sensor"));
  }

  public void disableCompass()
  {
    this.mSensorManager.unregisterListener(this);
    this.mCompassEnabled = false;
  }

  public boolean enableCompass()
  {
    if (!this.mCompassEnabled)
    {
      Sensor localSensor = this.mSensorManager.getDefaultSensor(3);
      if (localSensor != null)
      {
        this.mSensorManager.registerListener(this, localSensor, 2);
        this.mCompassEnabled = true;
      }
    }
    return this.mCompassEnabled;
  }

  public boolean isCompassEnabled()
  {
    return this.mCompassEnabled;
  }

  public final void onAccuracyChanged(Sensor paramSensor, int paramInt) {}

  public final void onSensorChanged(SensorEvent paramSensorEvent)
  {
    if (this.mListener != null) {
      this.mListener.onCompassChanged(paramSensorEvent.values[0]);
    }
  }

  public void setCompassListener(CompassEngineListener paramCompassEngineListener)
  {
    this.mListener = paramCompassEngineListener;
  }

  public static abstract interface CompassEngineListener
  {
    public abstract void onCompassChanged(float paramFloat);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.location.CompassEngine
 * JD-Core Version:    0.7.0.1
 */