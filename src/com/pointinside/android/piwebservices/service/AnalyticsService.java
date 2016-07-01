package com.pointinside.android.piwebservices.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import com.pointinside.android.piwebservices.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.*;
import android.util.Log;
import android.text.TextUtils;

import java.io.*;
import com.pointinside.android.piwebservices.net.PIPostTouchstreamClient;
import com.pointinside.android.piwebservices.provider.*;

public class AnalyticsService
  extends IntentService
{
  private static final String ALTITUDE_TAG = "loc_alt=";
  private static final String APP_ID_TAG = "app_id=";
  private static final String COMMA = ",";
  public static final String EXTRA_SESSION_ID = "session_id";
  private static final String FOUND_AT_STORE_TAG = "found_at_store_id=";
  private static final String FOUND_IN_VENUE_TAG = "found_in_venue_id=";
  private static final String LATITUDE_TAG = "loc_lat=";
  private static final String LOC_HERR_TAG = "loc_herr=";
  private static final String LOC_PROVIDER_TAG = "loc_provider=";
  private static final String LOC_TIME_TAG = "loc_time=";
  private static final String LOC_VERR_TAG = "loc_verr=";
  private static final String LONGITUDE_TAG = "loc_lon=";
  private static final String OS_TAG = "os_version=";
  private static final String PROMO_ID_TAG = "promotion_id=";
  private static final String SEARCH_TAG = "search_keyword=";
  private static final String SESSION_END_TAG = "session_end_time=";
  private static final String SESSION_START_TAG = "session_start_time=";
  private static final String SESSION_TAG = "rid=1";
  private static final String SHOW_ABOUT = "About";
  private static final String SHOW_BROWSE = "Browse";
  private static final String SHOW_DEAL = "Deal";
  private static final String SHOW_DEALS_LIST = "Deals";
  private static final String SHOW_DINING = "Dining";
  private static final String SHOW_EVENTS = "Events";
  private static final String SHOW_FEATURED = "Featured";
  private static final String SHOW_FEEDBACK = "Feedback";
  private static final String SHOW_FOR_VENUE_TAG = "show_for_venue_id=";
  private static final String SHOW_PROMOS = "Promos";
  private static final String SHOW_SEARCH = "Search";
  private static final String SHOW_SERVICES = "Services";
  private static final String SHOW_STORES = "Stores";
  private static final String SHOW_TAG = "show=";
  private static final String SOURCE_TAG = "source=";
  private static final String STORE_ID_TAG = "store_id=";
  private static final String TAG = AnalyticsService.class.getSimpleName();
  private static final String TIME_OF_VISIT_TAG = "time_of_visit=";
  private static final String TOUCHSTREAM_FILE = "touchstream.txt";
  private static final SimpleDateFormat TOUCHSTREAM_FORMATTER = new SimpleDateFormat("yyyy-MM-dd H:mm:ss Z");
  private static final String TOUCHSTREAM_TAG = "rid=2";
  private static final String UUID_TAG = "uuid=";
  private static final String VISIT_VENUE_TAG = "visit_venue_id=";

  public AnalyticsService()
  {
    super(TAG);
  }

  private static String getAppId(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(BuildUtils.getAppName(paramContext));
    localStringBuilder.append(";Android;");
    localStringBuilder.append(BuildUtils.getAppVersionLabel(paramContext));
    return localStringBuilder.toString();
  }

  private static String getTouchstreamDate(long paramLong)
  {
    return TOUCHSTREAM_FORMATTER.format(new Date(paramLong));
  }

  public static void submitTouchstream(Context context, long l)
  {
      Cursor cursor = com.pointinside.android.piwebservices.provider.PITouchstreamContract.Touchstream.getTouchStreamData(context, l);
      if(cursor != null) {
      long l1;
      long l2;
      String s;
      String s1;
      String s2;
      String s3;
      int i;
      int j;
      int k;
      int i1;
      int j1;
      int k1;
      int i2;
      int j2;
      int k2;
      int i3;
      File file2;
      OutputStreamWriter outputstreamwriter;
      l1 = System.currentTimeMillis();
      l2 = System.currentTimeMillis();
      s = getAppId(context);
      s1 = DevIdUtils.getUUID(context);
      s2 = android.os.Build.MODEL;
      s3 = android.os.Build.VERSION.RELEASE;
      i = cursor.getColumnIndex("_data");
      j = cursor.getColumnIndex("venue_id");
      k = cursor.getColumnIndex("data_type");
      i1 = cursor.getColumnIndex("visit_time");
      j1 = cursor.getColumnIndex("loc_time");
      k1 = cursor.getColumnIndex("latitude");
      i2 = cursor.getColumnIndex("longitude");
      j2 = cursor.getColumnIndex("altitude");
      k2 = cursor.getColumnIndex("loc_accuracy");
      i3 = cursor.getColumnIndex("loc_provider");
      File file = context.getFilesDir();
      File file1 = new File(file, "touchstream.txt");
      file1.delete();
      file2 = new File(file, "touchstream.txt");

      try {
      FileOutputStream fileoutputstream = new FileOutputStream(file2);
      outputstreamwriter = new OutputStreamWriter(fileoutputstream);
      cursor.moveToFirst();

      while(true) {
	      if( cursor.isAfterLast()) {

	      if(cursor.getCount() > 0) {
		      StringBuilder stringbuilder;
		      stringbuilder = new StringBuilder();
		      stringbuilder.append("[");
		      stringbuilder.append("rid=1");
		      stringbuilder.append(",");
		      stringbuilder.append("app_id=");
		      stringbuilder.append(s);
		      stringbuilder.append(",");
		      stringbuilder.append("uuid=");
		      stringbuilder.append(s1);
		      stringbuilder.append(",");
		      stringbuilder.append("session_end_time=");
		      if(l1 == l2)
			      l1 = System.currentTimeMillis();

		      PIPostTouchstreamClient piposttouchstreamclient;
		      stringbuilder.append(getTouchstreamDate(l1));
		      stringbuilder.append("]");
		      outputstreamwriter.write(stringbuilder.toString());
		      outputstreamwriter.close();
		      Log.d(TAG, (new StringBuilder("touchstreamData path = ")).append(file2.getPath()).toString());
		      piposttouchstreamclient = new PIPostTouchstreamClient(context, file2.getPath());
		      piposttouchstreamclient.run();
		      com.pointinside.android.piwebservices.provider.PITouchstreamContract.Touchstream.deleteTouchStreamData(context, l);
		      if(PITouchstreamContract.getCurrentSessionId() == l)
		          PITouchstreamContract.resetSessionId();
		      }

		      file2.deleteOnExit();
		      cursor.close();
		      } else {

	          StringBuilder stringbuilder1;
	          int j3;
	          String s4;
	          long l3;
	          stringbuilder1 = new StringBuilder();
	          stringbuilder1.append("[");
	          j3 = cursor.getInt(k);
	          s4 = cursor.getString(i);
	          l3 = cursor.getLong(i1);
	          l1 = l3;
	          String s5;
	          long l4;
	          String s6;
	          String s7;
	          String s8;
	          String s9;
	          String s10;
	          com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType touchstreamtype;
	          s5 = cursor.getString(j);
	          l4 = cursor.getLong(j1);
	          s6 = cursor.getString(k1);
	          s7 = cursor.getString(i2);
	          s8 = cursor.getString(j2);
	          s9 = cursor.getString(k2);
	          s10 = cursor.getString(i3);
	          touchstreamtype = com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.values()[j3];

	          switch(j3) {
	          case 1:
	        	  break;
	          case 2:
	              stringbuilder1.append("rid=1");
	              stringbuilder1.append(",");
	              stringbuilder1.append("app_id=");
	              stringbuilder1.append(s);
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("source=");
	              stringbuilder1.append(s2);
	              stringbuilder1.append(",");
	              stringbuilder1.append("os_version=");
	              stringbuilder1.append(s3);
	              stringbuilder1.append(",");
	              stringbuilder1.append("session_start_time=");
	              l2 = l3;
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 3:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("visit_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 4:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Stores");
	              stringbuilder1.append(",");
	              stringbuilder1.append("store_id=");
	              stringbuilder1.append(s4);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 5:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Promos");
	              stringbuilder1.append(",");
	              stringbuilder1.append("promotion_id=");
	              stringbuilder1.append(s4);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 6:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Events");
	              stringbuilder1.append(",");
	              stringbuilder1.append("promotion_id=");
	              stringbuilder1.append(s4);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 7:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Featured");
	              stringbuilder1.append(",");
	              stringbuilder1.append("promotion_id=");
	              stringbuilder1.append(s4);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 8:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Stores");
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 9:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Events");
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 10:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Services");
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 11:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Promos");
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 12:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show=");
	              stringbuilder1.append("Dining");
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 13:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("search_keyword=");
	              stringbuilder1.append(s4);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 14:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("uuid=");
	              stringbuilder1.append(s1);
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("search_keyword=");
	              stringbuilder1.append(s4);
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	        	  break;
	          case 15:
	          case 16:
	          case 17:
	          case 18:
	          case 19:
	          case 20:
	          case 21:
	          case 22:
	          case 23:
	          case 24:
	          default:
	              stringbuilder1.append("rid=2");
	              stringbuilder1.append(",");
	              stringbuilder1.append("show_for_venue_id=");
	              stringbuilder1.append(s5);
	              stringbuilder1.append(",");
	              stringbuilder1.append("actionTag=");
	              stringbuilder1.append(j3);
	              //stringbuilder1.append(touchstreamtype.toString());
	              if(!TextUtils.isEmpty(s4))
	              {
	                  stringbuilder1.append(",");
	                  stringbuilder1.append("actionData=");
	                  stringbuilder1.append(s4);
	              }
	              stringbuilder1.append(",");
	              stringbuilder1.append("time_of_visit=");
	              stringbuilder1.append(getTouchstreamDate(l3));
	              break;
	          }

	          if(l4 > 0L && s6 != null && s7 != null) {
		          stringbuilder1.append(",");
		          stringbuilder1.append("loc_lat=");
		          stringbuilder1.append(s6);
		          stringbuilder1.append(",");
		          stringbuilder1.append("loc_lon=");
		          stringbuilder1.append(s7);
		          if(s8 != null) {
			          stringbuilder1.append(",");
			          stringbuilder1.append("loc_alt=");
			          stringbuilder1.append(s8);
		          } else {
			          stringbuilder1.append(",");
			          stringbuilder1.append("loc_herr=");
			          stringbuilder1.append(s9);
			          stringbuilder1.append(",");
			          stringbuilder1.append("loc_verr=");
			          stringbuilder1.append(s9);
		          }
		          if(s10 != null) {
			          stringbuilder1.append(",");
			          stringbuilder1.append("loc_provider=");
			          stringbuilder1.append(s10);
		          } else {
			          stringbuilder1.append(",");
			          stringbuilder1.append("loc_time=");
			          stringbuilder1.append(l4);
		          }
	          } else {
		          stringbuilder1.append("]");
		          outputstreamwriter.write(stringbuilder1.toString());
		          if(cursor.moveToNext())
		        	  continue;
		          else
		        	  break;
	          }
	      }
	      }// while true
          cursor.close();
     }catch(FileNotFoundException filenotfoundexception) {
	      Log.w(TAG, "Unable to create touchstream file", filenotfoundexception);
	      //cursor.close();
	      return;
	  } catch(IOException ioexception) {
	      Log.w(TAG, "Unable to submit touchstream", ioexception);
	      cursor.close();

	  } catch(Exception exception){
		  cursor.close();
	  }
    }
    return;
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    long l = paramIntent.getLongExtra("session_id", 0L);
    if (l > 0L) {
      submitTouchstream(getApplicationContext(), l);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.service.AnalyticsService
 * JD-Core Version:    0.7.0.1
 */