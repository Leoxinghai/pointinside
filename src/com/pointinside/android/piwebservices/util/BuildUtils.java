package com.pointinside.android.piwebservices.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import com.pointinside.android.piwebservices.net.PIWebServices;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildUtils
{
  private static String sAppName;
  private static String sAppVersionLabel;
  private static String sBaseVersionName;
  private static boolean sBuildInfoCollected = false;
  private static boolean sIsDebuggable;

  private static void ensureBuildInfo(Context context)
  {
      boolean flag;
      flag = true;
      if(sBuildInfoCollected)
          return;
      try
      {
      
      PackageInfo packageinfo;
      packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      if(packageinfo.applicationInfo.labelRes == 0) {
              sAppName = packageinfo.packageName;
      } else 
    	  sAppName = context.getString(packageinfo.applicationInfo.labelRes);

      
      sBaseVersionName = packageinfo.versionName;
      if((2 & packageinfo.applicationInfo.flags) == 0)
          flag = false;
      sIsDebuggable = flag;
      sBuildInfoCollected = true;
      
      } catch(android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
      {
          throw new RuntimeException(namenotfoundexception);
      }
      
      return;
  }

  public static String getAppName(Context paramContext)
  {
    ensureBuildInfo(paramContext);
    return sAppName;
  }

  public static String getAppVersionLabel(Context paramContext)
  {
    ensureBuildInfo(paramContext);
    if (sAppVersionLabel == null) {
      sAppVersionLabel = makeAppVersionLabel(paramContext);
    }
    return sAppVersionLabel;
  }

  public static String getUserAgent(Context paramContext)
  {
    return PIWebServices.getUserAgent("PointInside", getAppVersionLabel(paramContext));
  }

  public static boolean isDebuggable(Context paramContext)
  {
	  return false;
//    ensureBuildInfo(paramContext);
//    return sIsDebuggable;
  }

  private static String makeAppVersionLabel(Context context)
  {
      StringBuilder stringbuilder;
      stringbuilder = new StringBuilder();
      Resources resources = context.getResources();
      int i = resources.getIdentifier("build_number", "integer", context.getPackageName());
      int j;
      int k;
      if(i != 0)
          j = resources.getInteger(i);
      else
          j = 0;
      
      if(!isDebuggable(context) || j <= 0) {
    	      stringbuilder.append(sBaseVersionName);
    	      if(isDebuggable(context))
    	          stringbuilder.append('*');
      } else {

	      k = resources.getIdentifier("build_timestamp", "string", context.getPackageName());
	      long l;
	      if(k != 0)
	          l = 1000L * Long.parseLong(resources.getString(k));
	      else
	          l = 0L;
	      stringbuilder.append((new SimpleDateFormat("yyyyMMdd")).format(new Date(l)));
	      stringbuilder.append(" build ");
	      stringbuilder.append(String.valueOf(j));
      }
      return stringbuilder.toString();

  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.util.BuildUtils
 * JD-Core Version:    0.7.0.1
 */