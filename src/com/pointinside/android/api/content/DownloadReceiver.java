package com.pointinside.android.api.content;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;

public class DownloadReceiver
  extends BroadcastReceiver
{
  private static volatile boolean sIsNetworkAvailable = false;

  public static void initNetworkStatus(Context paramContext)
  {
    sIsNetworkAvailable = ContentManagerUtils.isNetworkAvailable(paramContext);
  }

  public boolean isNetworkAvailable()
  {
    return sIsNetworkAvailable;
  }

  public void onReceive(Context context, Intent intent)
  {
      System.out.println("onReceive "+intent.getAction());
	  if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
      NetworkInfo networkinfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");

      if(networkinfo != null && networkinfo.isConnected()) {

    	  if(ContentManagerUtils.isNetworkAvailable(context)) {
		      DownloadService.getInstance().start();
		      context.getContentResolver().notifyChange(Downloads.CONTENT_URI, null);
		      sIsNetworkAvailable = true;
		      return;
    	  } else {
		      sIsNetworkAvailable = false;
		      return;
    	  }
      } else {
	      if(ContentManagerUtils.isNetworkAvailable(context))
	      {
	          DownloadService.getInstance().start();
	          context.getContentResolver().notifyChange(Downloads.CONTENT_URI, null);
	          sIsNetworkAvailable = true;
	          return;
	      } else
	      {
	          sIsNetworkAvailable = false;
	          return;
	      }
      }
      } else {

    	  if(intent.getAction().equals("pointinside.intent.action.DOWNLOAD_WAKEUP"))
      {
          DownloadService.getInstance().start();
          context.getContentResolver().notifyChange(Downloads.CONTENT_URI, null);
          return;
      }
      if(intent.getAction().equals("pointinside.intent.action.DOWNLOAD_CANCEL"))
      {
          DownloadService.getInstance().stop();
          return;
      }
      if(intent.getAction().equals("pointinside.intent.action.DOWNLOAD_NEW"))
      {
          DownloadService.getInstance().start();
          return;
      }
      if(intent.getAction().equals("pointinside.intent.action.DOWNLOAD_COMPLETE"))
    	  //add by xinghai
          return;
      }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.DownloadReceiver
 * JD-Core Version:    0.7.0.1
 */