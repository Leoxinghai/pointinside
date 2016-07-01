package com.pointinside.android.api.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.HashMap;
import android.os.Build;

public abstract class SystemFeatureTester
{
  private static final String TAG = SystemFeatureTester.class.getSimpleName();

  public static SystemFeatureTester getInstance()
  {
    if (Build.VERSION.SDK_INT >= 5) {
      return SystemFeatureTester.EclairAndBeyond.Holder.sInstance;
    }
    return SystemFeatureTester.PreEclair.Holder.sInstance;
  }

  public abstract boolean hasSystemFeature(Context paramContext, String paramString);

  private static class EclairAndBeyond
    extends SystemFeatureTester
  {
    public boolean hasSystemFeature(Context paramContext, String paramString)
    {
      return paramContext.getPackageManager().hasSystemFeature(paramString);
    }

    private static class Holder
    {
      private static final SystemFeatureTester.EclairAndBeyond sInstance = new SystemFeatureTester.EclairAndBeyond();
    }
  }

  private static class PreEclair
    extends SystemFeatureTester
  {
    private final HashMap<String, Boolean> mFeatures = new HashMap();

    private PreEclair()
    {
      this.mFeatures.put("android.hardware.touchscreen", Boolean.valueOf(true));
      this.mFeatures.put("android.hardware.touchscreen.multitouch", Boolean.valueOf(false));
    }

    public boolean hasSystemFeature(Context paramContext, String paramString)
    {
      Boolean localBoolean = (Boolean)this.mFeatures.get(paramString);
      if (localBoolean == null)
      {
        Log.w(SystemFeatureTester.TAG, "Unknown feature for pre-eclair platform: " + paramString);
        return false;
      }
      return localBoolean.booleanValue();
    }

    private static class Holder
    {
      private static final SystemFeatureTester.PreEclair sInstance = new SystemFeatureTester.PreEclair();
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.SystemFeatureTester
 * JD-Core Version:    0.7.0.1
 */