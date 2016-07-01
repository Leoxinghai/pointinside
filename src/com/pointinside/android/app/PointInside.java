package com.pointinside.android.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.nav.RouteAPI;
import com.pointinside.android.app.net.HolidayGameClient;
import com.pointinside.android.app.util.LatLongRect;
import com.pointinside.android.app.widget.GoogleOverlayItem;
import com.pointinside.android.piwebservices.net.DealsClient;
import com.pointinside.android.piwebservices.net.PIWebServices;
import com.pointinside.android.piwebservices.net.PlaceSearchClient;
import com.pointinside.android.piwebservices.util.BuildUtils;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.auth.UsernamePasswordCredentials;
import java.util.*;

public class PointInside
  extends Application
{
  public static final String ACTION_SHOW_PIN = "com.pointinsde.android.app.action.SHOW_PIN";
  public static final String ACTION_SHOW_RESULTS = "com.pointinsde.android.app.action.SHOW_RESULTS";
  public static final UsernamePasswordCredentials BASE_CREDENTIALS = new UsernamePasswordCredentials("triumph", "0g6qZW3BzKuW6V8MLjaLe");
  public static final boolean DEBUG = false;
  public static final String NAV_BASE_URL = "https://smartmaps-nav.pointinside.com/navws/v1/navigations";
  public static final UsernamePasswordCredentials NAV_CREDENTIALS = new UsernamePasswordCredentials("testnavws", "MsVaJUy345LZWltm5DxW");
  public static final String PI_BASE_URL = "https://smartmaps.pointinside.com/android/v2_0";
  public static final String PI_BYPASS_SDCARD_CHECK_EXTRA = "bypass_sd";
  public static final String PI_FORCE_VENUE_REDOWNLOAD_EXTRA = "force_redownload";
  public static final String PI_PLACE_UUID_EXTRA = "place_uuid";
  public static final String PI_PROMOTION_ID_EXTRA = "promotion_id";
  public static final String PI_PROMOTION_TYPE_EXTRA = "promotion_type";
  public static final String PI_REQUEST_LAT_EXTRA = "req_lat";
  public static final String PI_REQUEST_LONG_EXTRA = "req_long";
  public static final String PI_REQUEST_RADIUS_EXTRA = "req_radius";
  public static final String PI_SEARCH_RESULTS_URI_EXTRA = "results_uri";
  public static final String PI_VENUE_ID_EXTRA = "venue_id";
  public static final String PI_VENUE_UUID_EXTRA = "venue_uuid";
  private static final String PI_WEB_SERVICES_API_KEY = "b62d8e42ace411e0b5ce12313d0185f1";
  private static final String PREF_CURRENT_VENUE_UUID = "current_venue_uuid";
  private static final String PREF_VIEWPORT_LATLONG = "viewport_latlong";
  public static final String TAG = "PointInside";
  public static final int VENUE_TYPE_AIRPORT = 2;
  public static final int VENUE_TYPE_AMUSEMENT_PARK = 3;
  public static final int VENUE_TYPE_DEFAULT = 4;
  public static final int VENUE_TYPE_MALL = 1;
  private static PointInside sInstance;
  private String mCurrentVenueUUID;
  private CopyOnWriteArrayList<GoogleOverlayItem> mOverlayItems = new CopyOnWriteArrayList();
  private SharedPreferences mPrefs;
  private boolean mShowDealMode;
  private Location mUserLocation;
  private LatLongRect mViewport;

  public static PointInside getInstance()
  {
    if (sInstance == null) {
      throw new AssertionError("This should not be possible");
    }
    return sInstance;
  }

  public static PIMapReference getPIMapReference()
  {
    return PIMapReference.getInstance(sInstance.getApplicationContext(), "https://smartmaps.pointinside.com/android/v2_0", BASE_CREDENTIALS);
  }

  private void recoverState()
  {
    String str = this.mPrefs.getString("viewport_latlong", null);
    if (str != null)
    {
      this.mViewport = new LatLongRect();
      this.mViewport.unflatten(str);
    }
    this.mCurrentVenueUUID = this.mPrefs.getString("current_venue_uuid", null);
  }

  public void commitState()
  {
    SharedPreferences.Editor localEditor = this.mPrefs.edit();
    if (this.mViewport != null) {}
    for (String str = this.mViewport.flatten();; str = null)
    {
      localEditor.putString("viewport_latlong", str).putString("current_venue_uuid", this.mCurrentVenueUUID).commit();
      return;
    }
  }

  public String getCurrentPIMapVenueUUID()
  {
    return this.mPrefs.getString("venue_uuid", null);
  }

  public PIMapVenue getCurrentVenue()
  {
    return getPIMapReference().getLoadedVenue();
  }

  public long getCurrentVenueId()
  {
    String str = getCurrentVenueUUID();
    if (str != null)
    {
      PIMapVenue localPIMapVenue = getCurrentVenue();
      if ((localPIMapVenue != null) && (localPIMapVenue.getVenueUUID().equals(str))) {
        return localPIMapVenue.getVenueId();
      }
      Log.w("PointInside", "Global state inconsistency: currentVenueUUID=" + str + " but currentVenue=" + localPIMapVenue);
    }
    return 0L;
  }

  public String getCurrentVenueUUID()
  {
    return this.mCurrentVenueUUID;
  }
  
  public CopyOnWriteArrayList<GoogleOverlayItem> getOverlayItems()
  {
    return this.mOverlayItems;
  }
  
  public Location getUserLocation()
  {
    return this.mUserLocation;
  }

  public LatLongRect getViewport()
  {
    return this.mViewport;
  }

  public PIMapVenue loadVenue(String paramString)
  {
    PIMapVenue localPIMapVenue;
    if (paramString == null) {
      localPIMapVenue = null;
      return null;
    }
    do
    {
      localPIMapVenue = getPIMapReference().loadVenue(paramString, false, true, false);
    } while (localPIMapVenue == null);
    this.mPrefs.edit().putString("venue_uuid", paramString).commit();
    return localPIMapVenue;
  }

  public void onCreate()
  {
    sInstance = this;
    super.onCreate();
    ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
    
    getPIMapReference();
    PIWebServices.init(this, "PointInside", BuildUtils.getAppVersionLabel(this), false);
    PlaceSearchClient.init("b62d8e42ace411e0b5ce12313d0185f1");
    DealsClient.init("b62d8e42ace411e0b5ce12313d0185f1");
    HolidayGameClient.init(null);
    RouteAPI.init(this, Uri.parse("https://smartmaps-nav.pointinside.com/navws/v1/navigations"), NAV_CREDENTIALS);
    this.mPrefs = getSharedPreferences("PointInside", 0);
    recoverState();
    this.mShowDealMode = false;
  }

  public void setCurrentVenueUUID(String paramString)
  {
    this.mCurrentVenueUUID = paramString;
  }
  /*
  public void setOverlayItems(CopyOnWriteArrayList<GoogleOverlayItem> paramCopyOnWriteArrayList)
  {
    this.mOverlayItems = paramCopyOnWriteArrayList;
  }
  */
  public void setShowDealMode(boolean paramBoolean)
  {
    this.mShowDealMode = paramBoolean;
  }

  public void setUserLocation(Location paramLocation)
  {
    this.mUserLocation = paramLocation;
  }

  public void setViewport(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if (this.mViewport == null) {
      this.mViewport = new LatLongRect();
    }
    this.mViewport.set(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
  }

  public boolean showDealMode()
  {
    return this.mShowDealMode;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.PointInside
 * JD-Core Version:    0.7.0.1
 */