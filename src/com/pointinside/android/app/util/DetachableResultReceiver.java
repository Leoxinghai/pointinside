package com.pointinside.android.app.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class DetachableResultReceiver
  extends ResultReceiver
{
  private static final String TAG = "DetachableResultReceiver";
  private Receiver mReceiver;

  public DetachableResultReceiver(Handler paramHandler)
  {
    super(paramHandler);
  }

  public void clearReceiver()
  {
    this.mReceiver = null;
  }

  protected void onReceiveResult(int paramInt, Bundle paramBundle)
  {
    if (this.mReceiver != null)
    {
      this.mReceiver.onReceiveResult(paramInt, paramBundle);
      return;
    }
    Log.w("DetachableResultReceiver", "Dropping result on floor for code " + paramInt + ": " + paramBundle.toString());
  }

  public void setReceiver(Receiver paramReceiver)
  {
    this.mReceiver = paramReceiver;
  }

  public static abstract interface Receiver
  {
    public abstract void onReceiveResult(int paramInt, Bundle paramBundle);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.DetachableResultReceiver
 * JD-Core Version:    0.7.0.1
 */