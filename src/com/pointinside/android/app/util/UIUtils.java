package com.pointinside.android.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;

public class UIUtils
{
  public static String getDevice()
  {
    return Build.MODEL;
  }

  public static String getVersion()
  {
    return Build.VERSION.RELEASE;
  }

  public static boolean isEclairOrGreater()
  {
    return Build.VERSION.SDK_INT >= 5;
  }

  public static boolean isHoneycomb()
  {
    return Build.VERSION.SDK_INT >= 11;
  }

  public static boolean isHoneycombTablet(Context paramContext)
  {
    return (isHoneycomb()) && ((0xF & paramContext.getResources().getConfiguration().screenLayout) == 4);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.UIUtils
 * JD-Core Version:    0.7.0.1
 */