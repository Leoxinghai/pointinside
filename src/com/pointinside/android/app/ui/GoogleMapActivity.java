package com.pointinside.android.app.ui;

import com.pointinside.android.app.R;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;
import android.widget.ZoomControls;

import com.google.android.gms.maps.model.LatLng;
/*
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
*/

import android.app.*;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DealsUtils;
import com.pointinside.android.app.util.DetachableAsyncTask;
import com.pointinside.android.app.util.DetachableAsyncTask.TaskCallbacks;
import com.pointinside.android.app.util.DetachableResultReceiver;
import com.pointinside.android.app.util.DetachableResultReceiver.Receiver;
import com.pointinside.android.app.util.DistanceUtils;
import com.pointinside.android.app.util.LatLongRect;
import com.pointinside.android.app.util.LocationHelper;
import com.pointinside.android.app.util.OverlayUtils;
import com.pointinside.android.app.util.WhatsNewHelper;
import com.pointinside.android.app.widget.DealBar;
import com.pointinside.android.app.widget.DealBar.ToggleViewListener;
import com.pointinside.android.app.widget.DealsAdapter;
import com.pointinside.android.app.widget.GoogleMapOverlay;
import com.pointinside.android.app.widget.GoogleOverlayItem;
import com.pointinside.android.app.widget.MyGoogleLocation;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.service.DealsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.location.*;

import android.support.v4.app.FragmentActivity;

public class GoogleMapActivity
//  extends Activity
extends FragmentActivity
//  implements DetachableResultReceiver.Receiver
{
  private static final int DIALOG_WHATS_NEW = 1;
  private static final String EXTRA_DESCRIPTION = "place-desc";
  private static final String EXTRA_LATITUDE = "place-lat";
  private static final String EXTRA_LONGITUDE = "place-lon";
  private static final String EXTRA_SEARCH_URI = "search-uri";
  private static final String EXTRA_TITLE = "place-title";
  private static final String EXTRA_URBANQ_ID = "urbanq-id";
  private static final int REQUEST_VENUE_DOWNLOAD_FOR_DETAIL_CODE = 100;
  private static final int REQUEST_VENUE_DOWNLOAD_FOR_MAP_CODE = 101;
  private static final String TAG = GoogleMapActivity.class.getSimpleName();
  private GoogleMap mMap;
//  private ArrayList mMapOverlays;

  /*
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    System.out.println("GoogleMapActivity.create");
	  setContentView(R.layout.splash2);
    
	mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))        	
            .getMap();
	mMap.setMyLocationEnabled(true);
	MarkerOptions options = new MarkerOptions();
	LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
	Criteria criteria = new Criteria();
	String provider = locationManager.getBestProvider(criteria, true);
	Location location = locationManager.getLastKnownLocation(provider);
	try {
		PIMapVenueSummaryDataCursor cursor = PIMapReference.getInstance().getVenues();//loadVenue("1");
		cursor.moveToFirst();
		int i=0;
		for(;i<20 && cursor.moveToNext();) {
			double latitude = cursor.getLatitude();
			double longitude = cursor.getLongitude();
			LatLng latLng = new LatLng(latitude, longitude);
			options.position(latLng).title(cursor.getVenueName());
			mMap.addMarker(options);
			i++;
		}
	} catch(Exception ex) {
		ex.printStackTrace();
	}
	if(location != null) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		
		options.position(latLng).title("xinghai");
//		mMap.addMarker(options);
	} else {
		LatLng latLng = new LatLng(30, 130);
		

		options.position(latLng).title("xinghai");
//		mMap.addMarker(options);
		
	}
	
  }
*/
  
  private Runnable firstLocationFix = new Runnable()
  {
    public void run()
    {
//      mMapController.setZoom(11);
      centerOnCurrentLocation();
    }
  };
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private DealBar mDealBar;
  private ViewStub mDealBarStub;
  private HashMap<String, MapDeal> mDeals = new HashMap();
  private volatile boolean mDealsLoaderRunning = false;
  private GoogleMapOverlay mDealsOverlay;
  private final Runnable mDismissOnScreenControlRunner = new Runnable()
  {
    public void run()
    {
      GoogleMapActivity.this.hideOnScreenControls();
    }
  };
  private final Handler mHandler = new Handler();
  private boolean mIsDealMode = false;
  private long mLastDealsRequestId = -1L;
  private Bundle mLastRequestBundle;
  private LayoutInflater mLayoutInflater;
  private Runnable mLoadDealsRunner = new Runnable()
  {
    public void run()
    {
      if (!GoogleMapActivity.this.mDealsLoaderRunning)
      {
        GoogleMapActivity.this.setDealsLoading(true);
        LocationHelper.setViewPort(GoogleMapActivity.this.mMapView);
        LatLongRect localLatLongRect = LocationHelper.getMapArea();
        if (localLatLongRect == null)
        {
          Log.w(GoogleMapActivity.TAG, "Unknown search area, selecting an arbitrary location in the US");
          localLatLongRect = new LatLongRect(47.544091000000002D - 1.0D, -122.200928D - 1.0D, 47.544091000000002D + 1.0D, -122.200928D + 1.0D);
        }
        float[] arrayOfFloat = new float[1];
        Location.distanceBetween(localLatLongRect.getLat1(), localLatLongRect.getLong1(), localLatLongRect.getLat2(), localLatLongRect.getLong2(), arrayOfFloat);
        int i = Math.round(DistanceUtils.kilometersToMiles(arrayOfFloat[0] / 2.0F / 1000.0F));
        Log.d(GoogleMapActivity.TAG, "Loading mDeals around ll=" + localLatLongRect.getLatitudeCenter() + "," + localLatLongRect.getLongitudeCenter() + " with radius=" + i);
        double d1 = localLatLongRect.getLatitudeCenter();
        double d2 = localLatLongRect.getLongitudeCenter();
        Bundle localBundle = new Bundle();
        localBundle.putDouble("req_lat", d1);
        localBundle.putDouble("req_long", d2);
        localBundle.putInt("req_radius", i);
        GoogleMapActivity.this.mLastRequestBundle = localBundle;
        DealsService.loadNearbyDeals(GoogleMapActivity.this, GoogleMapActivity.this.mReceiver, localBundle, d1, d2, i);
      }
    }
  };
  private LoadVenueSummariesTask mLoadVenueSummariesTask;
  private Runnable mLoadVisibleOverlaysRunner = new Runnable()
  {
	    public void run()
	    {
	    	List items = PointInside.getInstance().getOverlayItems();
	    	Iterator iterator = items.iterator();
	    	int i=0;
	    	for(;iterator.hasNext();) {
	    		GoogleOverlayItem goi = (GoogleOverlayItem)iterator.next();
	    		MarkerOptions options = new MarkerOptions();
	    		LatLng position = new LatLng(goi.getLatitudeE6(),goi.getLongitudeE6());
	    		options.position(position);
	    		options.title(goi.getVenueUUID());
	    		mMap.addMarker(options);
	    		i++;
//	    		if(i>20)
//	    			break;
	    	}
/*	    	
//	      if (GoogleMapActivity.this.mMapView == null) {
//	        return;
//	      }
	      int i = OverlayUtils.addGoogleOverlaysForVisibleArea(PointInside.getInstance().getOverlayItems(), GoogleMapActivity.this.mVenuesOverlay);
	      ArrayList localArrayList = null;
	      Iterator localIterator=null;
//	      if (PointInside.getInstance().showDealMode())
//	      {
	        localIterator = GoogleMapActivity.this.mDeals.values().iterator();
		    localArrayList = new ArrayList();
//	      }
	      for (;;)
	      {
	        if (!localIterator.hasNext())
	        {
	          int j = GoogleMapActivity.this.mDealsOverlay.addAllOverlays(localArrayList);
	          i += j;
	          if (j > 0) {}
	          OverlayUtils.hideVenueOverlaysForDeals(GoogleMapActivity.this.mDeals.values(), GoogleMapActivity.this.mVenuesOverlay);
	          if (i <= 0) {
	            break;
	          }
//	          GoogleMapActivity.this.mMapView.invalidate();
	          return;
	        }
	        localArrayList.add((GoogleOverlayItem)localIterator.next());
	      }
*/	      
	    }
  };
//  private MapController mMapController;
  private List mMapOverlays;
  private MapView mMapView;
  private MyGoogleLocation mMyLocationOverlay;
  private LinearLayout mPopupLayout;
  private QueryHandler mQueryHandler;
  private DetachableResultReceiver mReceiver;
  private GoogleMapOverlay mSearchOverlay;
  private DealBar.ToggleViewListener mToggleViewListener = new DealBar.ToggleViewListener()
  {
      public void onToggle(int i)
      {
          switch(i)
          {
          default:
              return;

          case 2131623951:
              DealListActivity.show(GoogleMapActivity.this, mLastDealsRequestId, mLastRequestBundle);
              break;
          }
      }
  };
  private GoogleMapOverlay mVenuesOverlay;
  private final DetachableAsyncTask.TaskCallbacks<Integer, Void> mVenuesTaskCallback = new DetachableAsyncTask.TaskCallbacks<Integer, Void>()
  {
    protected void onPostExecute(Integer paramAnonymousInteger)
    {
      if (paramAnonymousInteger.intValue() > 0) {
        GoogleMapActivity.this.loadVenueOverlaysForVisibleArea();
      }
    }
  };
  private ZoomControls mZoomControls;
  private View.OnClickListener mZoomInClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
  //    GoogleMapActivity.this.mMapController.zoomIn();
      GoogleMapActivity.this.updateZoomControls();
    }
  };
  private int mZoomLevel = 0;
  private View.OnClickListener mZoomOutClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
//      GoogleMapActivity.this.mMapController.zoomOut();
      GoogleMapActivity.this.updateZoomControls();
      GoogleMapActivity.this.reloadForMode();
    }
  };

  private void centerOnCurrentLocation()
  {
//    this.mMapController.animateTo(this.mMyLocationOverlay.getMyLocation());
    loadVenueOverlaysForVisibleArea();
  }

  private void centerOnOverlay(GoogleOverlayItem paramGoogleOverlayItem)
  {
    Point localPoint1 = new Point();
    Projection localProjection = this.mMapView.getMap().getProjection();
//    Point localPoint2 = localProjection.toPixels(paramGoogleOverlayItem.getPoint(), localPoint1);
    int i = getResources().getDimensionPixelSize(2131296256);
//    localPoint2.set(localPoint2.x, i + localPoint2.y);
//    LatLng localGeoPoint = localProjection.fromPixels(localPoint2.x, localPoint2.y);
//    this.mMapController.animateTo(localGeoPoint);
    loadVenueOverlaysForVisibleArea();
  }

  private void dismissPopup()
  {
    if (this.mPopupLayout != null)
    {
      this.mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 2130968590));
      this.mMapView.removeView(this.mPopupLayout);
    }
  }

  private void goToMyLocation()
  {
	  /*
    this.mMyLocationOverlay.runOnFirstFix(new Runnable()
    {
      public void run()
      {
        GoogleMapActivity.this.centerOnCurrentLocation();
      }
    });
    */
  }

  private void handleIntent(Intent paramIntent)
  {
    String str1 = paramIntent.getAction();
    if ((str1 != null) && (str1.equals("com.pointinside.android.app.action.SHOW_PIN")))
    {
      String str2 = paramIntent.getStringExtra("place-title");
      String str3 = paramIntent.getStringExtra("place-desc");
      String str4 = paramIntent.getStringExtra("urbanq-id");
      Uri localUri = (Uri)paramIntent.getParcelableExtra("search-uri");
      double d1 = paramIntent.getDoubleExtra("place-lat", 0.0D);
      double d2 = paramIntent.getDoubleExtra("place-lon", 0.0D);
      UrbanQPlace localUrbanQPlace = new UrbanQPlace(LocationHelper.toInt(d1), LocationHelper.toInt(d2), str2, str3, localUri, str4);
      this.mSearchOverlay.addOverlay(localUrbanQPlace);
      if (!this.mMapOverlays.contains(this.mSearchOverlay)) {
        this.mMapOverlays.add(this.mSearchOverlay);
      }
//      this.mMapController.setZoom(15);
      showSearchPopup(localUrbanQPlace, true);
      this.mPopupLayout.forceLayout();
//      this.mSearchOverlay.onTap(localUrbanQPlace.getPoint(), this.mMapView);
    }
  }

  private void loadDeals()
  {
    if (!this.mDealsLoaderRunning)
    {
      this.mHandler.post(this.mLoadDealsRunner);
      return;
    }
    this.mHandler.removeCallbacks(this.mLoadDealsRunner);
    this.mHandler.postDelayed(this.mLoadDealsRunner, 1000L);
  }

  private void loadVenueOverlaysForVisibleArea()
  {
//    if (this.mMapView != null)
//    {
//      LocationHelper.setViewPort(this.mMapView);
      this.mHandler.removeCallbacks(this.mLoadVisibleOverlaysRunner);
      this.mHandler.post(this.mLoadVisibleOverlaysRunner);
//    }
  }

  private void reloadForMode()
  {
      if(shouldReload())
      {
          if(PointInside.getInstance().showDealMode())
              loadDeals();
          else
              loadVenueOverlaysForVisibleArea();
//          mZoomLevel = mMapView.getZoomLevel();
          LocationHelper.setViewPort(mMapView);
      }
  }

  static void reportDealsToggled(Context context, long l)
  {
      boolean flag;
      Location location;
      com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType touchstreamtype;
      if(PointInside.getInstance().showDealMode())
          flag = false;
      else
          flag = true;
      location = PointInside.getInstance().getUserLocation();
      if(flag)
          touchstreamtype = com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.TOGGLE_DEALS_ON;
      else
          touchstreamtype = com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.TOGGLE_DEALS_OFF;
      PITouchstreamContract.addEvent(context, location, "", l, touchstreamtype);
  }

  private void scheduleDismissOnScreenControls()
  {
    this.mHandler.removeCallbacks(this.mDismissOnScreenControlRunner);
    this.mHandler.postDelayed(this.mDismissOnScreenControlRunner, ViewConfiguration.getZoomControlsTimeout());
  }

  private void setDealMode(boolean flag)
  {
      showDealBar(flag);
      if(mIsDealMode != flag)
      {
          mIsDealMode = flag;
          PointInside.getInstance().setShowDealMode(flag);
          dismissPopup();
          if(flag)
          {
              if(!mMapOverlays.contains(mDealsOverlay))
                  mMapOverlays.add(mDealsOverlay);
              loadDeals();
          } else
          {
              mMapOverlays.remove(mDealsOverlay);
          }
  //        OverlayUtils.deactivateOverlays(PointInside.getInstance().getOverlayItems());
          loadVenueOverlaysForVisibleArea();
      }
  }

  private void setDealsLoading(boolean paramBoolean)
  {
    this.mDealBar.showProgress(paramBoolean);
    this.mDealsLoaderRunning = paramBoolean;
  }

  private boolean shouldReload()
  {
    //if (this.mMapView.getZoomLevel() != this.mZoomLevel) {
//      return true;
//    }
    LatLongRect localLatLongRect1 = PointInside.getInstance().getViewport();
    LatLongRect localLatLongRect2 = LocationHelper.createViewPort(this.mMapView);
    if (localLatLongRect1 != null)
    {
      float[] arrayOfFloat = new float[1];
      Location.distanceBetween(localLatLongRect1.getLatitudeCenter(), localLatLongRect1.getLongitudeCenter(), localLatLongRect2.getLatitudeCenter(), localLatLongRect2.getLongitudeCenter(), arrayOfFloat);
      return DistanceUtils.kilometersToMiles(arrayOfFloat[0] / 1000.0F) > 10.0F;
    }
    return true;
  }

  private void showDealBar(boolean paramBoolean)
  {
    this.mActionBarHelper.setDealsButtonOn(paramBoolean);
    if (this.mDealBar == null)
    {
      this.mDealBar = ((DealBar)this.mDealBarStub.inflate());
      this.mDealBar.setBackgroundDrawable(getResources().getDrawable(2130837588));
      this.mDealBar.setVisibility(8);
      this.mDealBar.setToggleViewListener(this.mToggleViewListener);
    }
    this.mDealBar.setChecked(2131623950);
    if (paramBoolean)
    {
      this.mDealBar.show();
      return;
    }
    this.mDealBar.hide();
  }

  private void showDealPopup(final GoogleOverlayItem overlay, boolean flag)
  {
      if(flag)
          centerOnOverlay(overlay);
      if(mPopupLayout != null)
          mMapView.removeView(mPopupLayout);
      mPopupLayout = (LinearLayout)mLayoutInflater.inflate(0x7f030009, null);
      final MapDeal dealPin = (MapDeal)overlay;
      final int dealCount = dealPin.mDealCount;
      ((ImageView)mPopupLayout.findViewById(0x7f0e0006)).setImageResource(DealsUtils.getDealListIconId(dealPin.getType()));
//      ((TextView)mPopupLayout.findViewById(0x1020016)).setText(overlay.getTitle());
//      ((TextView)mPopupLayout.findViewById(0x1020010)).setText(overlay.getSnippet());
      TextView textview = (TextView)mPopupLayout.findViewById(0x7f0e0007);
      int i;
      if(dealCount > 1)
          textview.setVisibility(0);
      else
          textview.setVisibility(8);
      textview.setText(String.valueOf(dealCount));
      i = getResources().getDimensionPixelSize(0x7f090000);
//      mPopupLayout.setLayoutParams(new com.google.android.maps.MapView.LayoutParams(-2, -2, overlay.getPoint(), 0, i, 81));
      mMapView.addView(mPopupLayout);
      mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040008));
      mPopupLayout.setOnClickListener(new android.view.View.OnClickListener() {

          public void onClick(View view)
          {
              if(TextUtils.isEmpty(dealPin.getVenueUUID()))
              {
                  if(dealCount > 1)
                  {
                      DealSubActivity.show(GoogleMapActivity.this, dealPin.getVenueUUID(), dealPin.getRequestId(), dealPin.getLatitude(), dealPin.getLongitude());
                      return;
                  } else
                  {
                      DealDetailActivity.show(GoogleMapActivity.this, dealPin.getDealUri());
                      return;
                  }
              } else
              {
                  VenueDownloadActivity.loadVenueForResult(GoogleMapActivity.this, 101, overlay.getVenueUUID(), false, false);
                  return;
              }
          }

      }
);
  }


  public static void showPlace(Context paramContext, String paramString1, String paramString2, String paramString3, Uri paramUri, double paramDouble1, double paramDouble2)
  {
    Intent localIntent = new Intent("com.pointinside.android.app.action.SHOW_PIN", null, paramContext, GoogleMapActivity.class);
    localIntent.putExtra("place-title", paramString1);
    localIntent.putExtra("place-desc", paramString2);
    localIntent.putExtra("urbanq-id", paramString3);
    localIntent.putExtra("place-lat", paramDouble1);
    localIntent.putExtra("place-lon", paramDouble2);
    localIntent.putExtra("search-uri", paramUri);
    localIntent.addFlags(4194304);
    localIntent.addFlags(67108864);
    paramContext.startActivity(localIntent);
  }

  private void showPopup(GoogleOverlayItem paramGoogleOverlayItem)
  {
    showPopup(paramGoogleOverlayItem, false);
  }

  private void showPopup(final GoogleOverlayItem paramGoogleOverlayItem, boolean paramBoolean)
  {
    if (paramBoolean) {
      centerOnOverlay(paramGoogleOverlayItem);
    }
    if (this.mPopupLayout != null) {
      this.mMapView.removeView(this.mPopupLayout);
    }
    this.mPopupLayout = ((LinearLayout)this.mLayoutInflater.inflate(2130903060, null));
    int i = getResources().getDimensionPixelSize(2131296256);
//    this.mPopupLayout.setLayoutParams(new MapView.LayoutParams(-2, -2, paramGoogleOverlayItem.getPoint(), 0, i, 81));
    this.mMapView.addView(this.mPopupLayout);
    this.mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 2130968584));
//    ((TextView)this.mPopupLayout.findViewById(16908310)).setText(paramGoogleOverlayItem.getTitle());
//    ((TextView)this.mPopupLayout.findViewById(16908304)).setText(paramGoogleOverlayItem.getSnippet());
    this.mPopupLayout.findViewById(2131623961).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        GoogleMapActivity.this.showVenueMap(paramGoogleOverlayItem.getVenueUUID());
      }
    });
    ((ImageView)this.mPopupLayout.findViewById(2131623960)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        try
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:q=" + paramGoogleOverlayItem.getVenueUUID()));
          GoogleMapActivity.this.startActivity(localIntent);
          return;
        }
        catch (Exception localException)
        {
          Toast.makeText(GoogleMapActivity.this, "Google Navigation not installed", 0).show();
        }
      }
    });
    ((ImageView)this.mPopupLayout.findViewById(2131623962)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        GoogleMapActivity.this.showVenueMap(paramGoogleOverlayItem.getVenueUUID());
      }
    });
  }

  private void showSearchPopup(final GoogleOverlayItem overlay, boolean flag)
  {
      if(flag)
          centerOnOverlay(overlay);
      if(mPopupLayout != null)
          mMapView.removeView(mPopupLayout);
      final UrbanQPlace place = (UrbanQPlace)overlay;
      mPopupLayout = (LinearLayout)mLayoutInflater.inflate(R.layout.g_pointer_view, null);
//      ((TextView)mPopupLayout.findViewById(0x1020016)).setText(overlay.getTitle());
      TextView textview = (TextView)mPopupLayout.findViewById(0x1020010);
      int i;
      if(!TextUtils.isEmpty(overlay.getVenueUUID()))
      {
          textview.setVisibility(0);
          textview.setText(overlay.getVenueUUID());
      } else
      {
          textview.setVisibility(8);
      }
      mPopupLayout.findViewById(0x7f0e0019).setOnClickListener(new android.view.View.OnClickListener() {

          public void onClick(View view)
          {
              PlaceDetailActivity.showPlaceFromSearch(GoogleMapActivity.this, place.mSearchUri, place.getUrbanQid(), null, null, false);
          }


      }
);
      ((ImageView)mPopupLayout.findViewById(0x7f0e0018)).setOnClickListener(new android.view.View.OnClickListener() {

          public void onClick(View view)
          {
              try
              {
                  Intent intent = new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder("google.navigation:q=")).append(overlay.getVenueUUID()).toString()));
                  startActivity(intent);
                  return;
              }
              catch(Exception exception)
              {
                  Toast.makeText(GoogleMapActivity.this, "Google Navigation not installed", 0).show();
              }
          }

      }
);
      ((ImageView)mPopupLayout.findViewById(0x7f0e001a)).setVisibility(8);
      i = getResources().getDimensionPixelSize(0x7f090000);
//      mPopupLayout.setLayoutParams(new com.google.android.maps.MapView.LayoutParams(-2, -2, overlay.getPoint(), 0, i, 81));
      mMapView.addView(mPopupLayout);
      mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040008));
  }

  public static void showVenue(Context paramContext, String paramString) {}

  private void showVenueDetails(String paramString)
  {
    VenueDownloadActivity.loadVenueForResult(this, 100, paramString, false, false);
  }

  private void showVenueMap(String paramString)
  {
    VenueDownloadActivity.loadVenueForResult(this, 101, paramString, false, false);
  }

  private void updateZoomControls()
  {
	  /*
      if(mMapView.getZoomLevel() == mMapView.getMaxZoomLevel())
          mZoomControls.setIsZoomInEnabled(false);
      else
          mZoomControls.setIsZoomInEnabled(true);
      if(mMapView.getZoomLevel() == 1)
          mZoomControls.setIsZoomOutEnabled(false);
      else
          mZoomControls.setIsZoomOutEnabled(true);
          */
      scheduleDismissOnScreenControls();
  }
  public void hideOnScreenControls()
  {
    this.mHandler.removeCallbacks(this.mDismissOnScreenControlRunner);
    this.mZoomControls.hide();
  }

  protected boolean isLocationDisplayed()
  {
    return true;
  }

  protected boolean isRouteDisplayed()
  {
    return false;
  }


  protected void onActivityResult(int i, int j, Intent intent)
  {
      if(j == -1) {

      switch(i) {
//      JVM INSTR tableswitch 100 100: default 24
  //                   100 47;
//         goto _L3 _L4
		default:
		      startActivity(new Intent(this, VenueMapActivity.class));
		      break;
		case 100:
		      startActivity(new Intent(this, VenueDetailActivity.class));
		      break;
      }
      }
      super.onActivityResult(i, j, intent);
      return;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    System.out.println("GoogleMapActivity.create");
	setContentView(R.layout.google_map);
    
	mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))        	
            .getMap();
//	this.mMapView = (MapView)((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getView();
	
    this.mLayoutInflater = ((LayoutInflater)getSystemService("layout_inflater"));
    this.mReceiver = new DetachableResultReceiver(new Handler());
    this.mQueryHandler = new QueryHandler();
    this.mDealBarStub = ((ViewStub)findViewById(R.id.stub_deals));
   this.mMapView = ((MapView)findViewById(R.id.google_map_view));
   //add by xinghai
//   loadDeals();
   
    this.mZoomControls = ((ZoomControls)findViewById(R.id.zoom_controls));
    this.mZoomControls.hide();
    this.mZoomControls.setOnZoomInClickListener(this.mZoomInClickListener);
    this.mZoomControls.setOnZoomOutClickListener(this.mZoomOutClickListener);
    //this.mMap.setOnMarkerClickListener(listener);
    /*
    setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        if (paramAnonymousMotionEvent.getAction() == 1)
        {
          GoogleMapActivity.this.showOnScreenControls();
          GoogleMapActivity.this.dismissPopup();
        }
        if (paramAnonymousMotionEvent.getAction() == 2) {
          GoogleMapActivity.this.reloadForMode();
        }
        return false;
      }
    });
    */

//    this.mMapOverlays = this.mMapView.getOverlays();
    mMapOverlays = new ArrayList();
    
    this.mMyLocationOverlay = new MyGoogleLocation(this, this.mMapView)
    {
      public void onLocationChanged(Location paramAnonymousLocation)
      {
//        super.onLocationChanged(paramAnonymousLocation);
        PointInside.getInstance().setUserLocation(paramAnonymousLocation);
      }
    };
    this.mMapOverlays.add(this.mMyLocationOverlay);
    this.mVenuesOverlay = new GoogleMapOverlay(OverlayUtils.getVenueMarkerDrawableMap(this))
    {
      public boolean onTap(int paramAnonymousInt)
      {
//        GoogleMapActivity.this.showPopup((GoogleOverlayItem)getItem(paramAnonymousInt), true);
//        return super.onTap(paramAnonymousInt);
    	  return true;
      }
    };
    this.mMapOverlays.add(this.mVenuesOverlay);
    this.mDealsOverlay = new GoogleMapOverlay(OverlayUtils.getDealMarkerDrawableMap(this))
    {
      public boolean onTap(int paramAnonymousInt)
      {
//        GoogleMapActivity.this.showDealPopup((GoogleOverlayItem)getItem(paramAnonymousInt), true);
//        return super.onTap(paramAnonymousInt);
    	  return true;
      }
    };
    this.mSearchOverlay = new GoogleMapOverlay(OverlayUtils.getVenueMarkerDrawableMap(this))
    {
      public boolean onTap(int paramAnonymousInt)
      {
//        GoogleMapActivity.this.showSearchPopup((GoogleOverlayItem)getItem(paramAnonymousInt), true);
//        return super.onTap(paramAnonymousInt);
    	  return true;
      }
    };
    this.mLoadVenueSummariesTask = ((LoadVenueSummariesTask)getLastNonConfigurationInstance());
    if (this.mLoadVenueSummariesTask == null) {
      this.mLoadVenueSummariesTask = new LoadVenueSummariesTask();
    }
    this.mLoadVenueSummariesTask.setCallback(this.mVenuesTaskCallback);
    if (this.mLoadVenueSummariesTask.getStatus() == AsyncTask.Status.PENDING)
    {
      LoadVenueSummariesTask localLoadVenueSummariesTask = this.mLoadVenueSummariesTask;
      CopyOnWriteArrayList[] arrayOfCopyOnWriteArrayList = new CopyOnWriteArrayList[1];
      arrayOfCopyOnWriteArrayList[0] = PointInside.getInstance().getOverlayItems();
      localLoadVenueSummariesTask.execute(arrayOfCopyOnWriteArrayList);
    }
/*
    this.mMapView.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getAction() == 1) {
          GoogleMapActivity.this.loadVenueOverlaysForVisibleArea();
        }
        return false;
      }
    });
    */
    handleIntent(getIntent());
    if (WhatsNewHelper.getAndSetShouldShow(this)) {
      showDialog(1);
    }
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    if (paramInt == 1) {
      return WhatsNewHelper.createDialog(this);
    }
    throw new IllegalStateException("Unknown dialog=" + paramInt);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558403, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mLoadVenueSummariesTask.clearCallback();
  }

  public void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    handleIntent(paramIntent);
  }

  public boolean onOptionsItemSelected(MenuItem menuitem)
  {
      switch(menuitem.getItemId())
      {
      case 2131624053:
      default:
          return false;

      case 2131624052:
      case 2131624054:
          onSearchRequested();
          return true;

      case 2131624055:
          PlaceBrowseActivity.show(this);
          return true;

      case 2131624056:
          reportDealsToggled(this, 0L);
          boolean flag = PointInside.getInstance().showDealMode();
          boolean flag1 = false;
          if(!flag)
              flag1 = true;
          setDealMode(flag1);
          return true;

      case 2131624057:
          goToMyLocation();
          return true;

      case 2131624050:
          startActivity(new Intent(this, AboutActivity.class));
          return true;

      case 2131624051:
          startActivity(new Intent(this, FeedbackActivity.class));
          return true;
      }
  }

  protected void onPause()
  {
    super.onPause();
    this.mReceiver.clearReceiver();
//    this.mMyLocationOverlay.disableMyLocation();
    setDealsLoading(false);
  }

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.mActionBarHelper.onPostCreate(paramBundle);
  }

  protected void onPostResume()
  {
    super.onPostResume();
  //  this.mZoomLevel = this.mMapView.getZoomLevel();
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return this.mActionBarHelper.onPrepareOptionsMenu(paramMenu);
  }

  public void onReceiveResult(int i, Bundle bundle)
  {
      Log.d(TAG, (new StringBuilder("onReceiveResult: resultCode=")).append(i).toString());
      String s = bundle.getString("error-text");
      Uri uri = (Uri)bundle.getParcelable("result-uri");
      switch(i)
      {
      default:
          throw new IllegalArgumentException((new StringBuilder("Unknown resultCode: ")).append(i).toString());

      case 1: // '\001'
          setDealsLoading(false);
          break;
      }
      if(s != null)
      {
          Toast.makeText(this, (new StringBuilder("Unable to fetch mDeals: ")).append(s).toString(), 1).show();
          setDealMode(false);
          return;
      } else
      {
          Bundle bundle1 = (Bundle)bundle.getParcelable("extras");
          com.pointinside.android.app.widget.DealsAdapter.ResultContainer resultcontainer = new com.pointinside.android.app.widget.DealsAdapter.ResultContainer();
          resultcontainer.resultUri = uri;
          resultcontainer.requestLatitude = bundle1.getDouble("req_lat");
          resultcontainer.requestLongitude = bundle1.getDouble("req_long");
          mLastDealsRequestId = ContentUris.parseId(uri);
          Uri uri1 = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriGroupByLocation(uri);
          mQueryHandler.startQuery(1, resultcontainer, uri1, null, null, null, "distance");
          Uri uri2 = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriByVenueUUID(uri);
          mQueryHandler.startQuery(2, resultcontainer, uri2, null, null, null, "distance");
          return;
      }
  }

  protected void onResume()
  {
    super.onResume();
    PointInside.getInstance().setCurrentVenueUUID(null);
//    this.mReceiver.setReceiver(this);
//    if (!this.mMyLocationOverlay.isMyLocationEnabled()) {
//      this.mMyLocationOverlay.enableMyLocation();
//    }
    if (PointInside.getInstance().getUserLocation() == null) {
  //    this.mMyLocationOverlay.runOnFirstFix(this.firstLocationFix);
    }
    setDealMode(PointInside.getInstance().showDealMode());
  }
/*
  public Object onRetainNonConfigurationInstance()
  {
    return this.mLoadVenueSummariesTask;
  }
*/
  
  public boolean onSearchRequested()
  {
    return PlaceSearchActivity.showSearch(this);
  }

  public void showOnScreenControls()
  {
    if (!this.mZoomControls.isShown()) {
      this.mZoomControls.show();
    }
    scheduleDismissOnScreenControls();
  }

  private static class LoadVenueSummariesTask
    extends DetachableAsyncTask<CopyOnWriteArrayList<GoogleOverlayItem>, Void, Integer>
  {
    protected Integer doInBackground(CopyOnWriteArrayList<GoogleOverlayItem>... acopyonwritearraylist)
//    protected transient Integer doInBackground(CopyOnWriteArrayList acopyonwritearraylist[])
    {
        int j;
        try {
        CopyOnWriteArrayList copyonwritearraylist;
        PIMapVenueSummaryDataCursor pimapvenuesummarydatacursor;
        int i = acopyonwritearraylist.length;
        j = 0;
        if(i <= 0)
            return -1;
        copyonwritearraylist = acopyonwritearraylist[0];
        pimapvenuesummarydatacursor = PointInside.getPIMapReference().getVenues();
        j = 0;
        if(pimapvenuesummarydatacursor == null)
            return -1;
        pimapvenuesummarydatacursor.moveToFirst();
        copyonwritearraylist.clear();
        boolean flag = true;
        while(flag) {
	        GoogleOverlayItem googleoverlayitem;
	        int l;
	        int k = pimapvenuesummarydatacursor.getVenueTypeId();
//	        googleoverlayitem = new GoogleOverlayItem(LocationHelper.toInt(pimapvenuesummarydatacursor.getLatitude()), LocationHelper.toInt(pimapvenuesummarydatacursor.getLongitude()), pimapvenuesummarydatacursor.getVenueName(), pimapvenuesummarydatacursor.getFormattedCityState(), pimapvenuesummarydatacursor.getVenueUUID(), String.valueOf(k));
	        googleoverlayitem = new GoogleOverlayItem(pimapvenuesummarydatacursor.getLatitude(), pimapvenuesummarydatacursor.getLongitude(), pimapvenuesummarydatacursor.getVenueName(), pimapvenuesummarydatacursor.getFormattedCityState(), pimapvenuesummarydatacursor.getVenueUUID(), String.valueOf(k));	        
	        l = Collections.binarySearch(copyonwritearraylist, googleoverlayitem, googleoverlayitem);
	        if(l < 0)
	            l = -1 + -l;
	        copyonwritearraylist.add(l, googleoverlayitem);
	        j++;
	        flag = pimapvenuesummarydatacursor.moveToNext();
		}

        pimapvenuesummarydatacursor.close();
        return Integer.valueOf(j);
    }catch( Exception exception) {
        exception.printStackTrace();
    }
//        pimapvenuesummarydatacursor.close();
//        throw exception;
        return -1;
    }
  }

  public static class MapDeal
    extends GoogleOverlayItem
  {
    private final double latitude;
    private final double longitude;
    private int mDealCount;
    private final Uri mDealUri;
    private final String mPlaceUUID;
    private long mRequestId;

    public MapDeal(double paramDouble1, double paramDouble2, String paramString1, String paramString2, String paramString3, String paramString4, Uri paramUri, int paramInt, long paramLong, String paramString5)
    {
      super(LocationHelper.toInt(paramDouble1),LocationHelper.toInt(paramDouble2), paramString1, paramString2, paramString4, paramString5);
      this.mPlaceUUID = paramString3;
      this.mDealUri = paramUri;
      this.mDealCount = paramInt;
      this.mRequestId = paramLong;
      this.latitude = paramDouble1;
      this.longitude = paramDouble2;
    }

    public int getDealCount()
    {
      return this.mDealCount;
    }

    public Uri getDealUri()
    {
      return this.mDealUri;
    }

    public double getLatitude()
    {
      return this.latitude;
    }

    public double getLongitude()
    {
      return this.longitude;
    }

    public String getPlaceUUID()
    {
      return this.mPlaceUUID;
    }

    public long getRequestId()
    {
      return this.mRequestId;
    }
  }

  private class QueryHandler
    extends AsyncQueryHandler
  {
    public static final int TOKEN_GET_ALL_DEALS_FOR_GEOCODE = 1;
    public static final int TOKEN_GET_DEALS_BY_VENUE = 2;
    private boolean mAllDealsQueryRunning = false;
    private Runnable mUpdateOverlaysRunner = new Runnable()
    {
      public void run()
      {
        if (PointInside.getInstance().showDealMode()) {
          GoogleMapActivity.this.loadVenueOverlaysForVisibleArea();
        }
        GoogleMapActivity.this.setDealsLoading(false);
      }
    };

    private boolean mVenueDealsQueryRunning = false;
    private Runnable updateDealOverlays = new Runnable()
    {
      public void run()
      {
        if ((!GoogleMapActivity.QueryHandler.this.mAllDealsQueryRunning) && (!GoogleMapActivity.QueryHandler.this.mVenueDealsQueryRunning))
        {
          GoogleMapActivity.QueryHandler.this.removeCallbacks(GoogleMapActivity.QueryHandler.this.updateDealOverlays);
          GoogleMapActivity.QueryHandler.this.post(GoogleMapActivity.QueryHandler.this.mUpdateOverlaysRunner);
          return;
        }
        GoogleMapActivity.QueryHandler.this.removeCallbacks(GoogleMapActivity.QueryHandler.this.updateDealOverlays);
        GoogleMapActivity.QueryHandler.this.postDelayed(GoogleMapActivity.QueryHandler.this.updateDealOverlays, 1000L);
      }
    };

    public QueryHandler()
    {
      super(getContentResolver());
    }

    private void clear()
    {
      GoogleMapActivity.this.mDeals.clear();
    }

    protected void onQueryComplete(int i, Object obj, Cursor cursor)
    {
        Log.d(GoogleMapActivity.TAG, (new StringBuilder("onQueryComplete: token=")).append(i).toString());
        if(!PointInside.getInstance().showDealMode()) {
	        setDealsLoading(false);
	        return;
        }

        int j;
        j = 0;
        switch(i)
        {
        default:
            throw new IllegalArgumentException((new StringBuilder("Unknown token: ")).append(i).toString());

        case 1: // '\001'
            while(cursor.moveToNext())
            {
                String s5 = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String s6 = cursor.getString(cursor.getColumnIndexOrThrow("organization"));
                double d = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double d1 = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                String s7 = cursor.getString(cursor.getColumnIndexOrThrow("place_uuid"));
                String s8 = cursor.getString(cursor.getColumnIndexOrThrow("venue_uuid"));
                int j1 = cursor.getInt(cursor.getColumnIndexOrThrow("deal_count"));
                long l3 = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                long l4 = cursor.getLong(cursor.getColumnIndexOrThrow("request_id"));
                String s9 = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String s10 = s5;
                String s11 = s6;
                if(j1 > 1)
                {
                    GoogleMapActivity googlemapactivity1 = GoogleMapActivity.this;
                    Object aobj1[] = new Object[1];
                    aobj1[0] = Integer.valueOf(j1);
                    s11 = googlemapactivity1.getString(0x7f06008b, aobj1);
                    s10 = s6;
                }
                MapDeal mapdeal1 = new MapDeal(d, d1, s10, s11, s7, s8, com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultUri(l3), j1, l4, s9);
                if(TextUtils.isEmpty(s8) && !mDeals.containsKey(mapdeal1.getGeoCode()))
                {
                    mDeals.put(mapdeal1.getGeoCode(), mapdeal1);
                    j++;
                }
            }
            mAllDealsQueryRunning = false;
            if(j > 0)
            {
                post(updateDealOverlays);
                return;
            }
            break;

        case 2: // '\002'
            while(cursor.moveToNext())
            {
                String s = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String s1 = cursor.getString(cursor.getColumnIndexOrThrow("organization"));
                int k = LocationHelper.toInt(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
                int l = LocationHelper.toInt(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
                String s2 = cursor.getString(cursor.getColumnIndexOrThrow("place_uuid"));
                String s3 = cursor.getString(cursor.getColumnIndexOrThrow("venue_uuid"));
                long l1 = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                long l2 = cursor.getLong(cursor.getColumnIndexOrThrow("request_id"));
                int i1 = cursor.getInt(cursor.getColumnIndexOrThrow("deal_count"));
                String s4 = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                if(i1 > 1)
                {
                    GoogleMapActivity googlemapactivity = GoogleMapActivity.this;
                    Object aobj[] = new Object[1];
                    aobj[0] = Integer.valueOf(i1);
                    s1 = googlemapactivity.getString(0x7f06008b, aobj);
                    s4 = "General";
                }
                GoogleOverlayItem googleoverlayitem = OverlayUtils.getOverlayItem(PointInside.getInstance().getOverlayItems(), s3);
                if(googleoverlayitem != null)
                {
                    s = googleoverlayitem.getVenueUUID();
                    k = googleoverlayitem.getLatitudeE6();
                    l = googleoverlayitem.getLongitudeE6();
                }
                MapDeal mapdeal = new MapDeal(LocationHelper.toDouble(k), LocationHelper.toDouble(l), s, s1, s2, s3, com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultUri(l1), i1, l2, s4);
                if(!mDeals.containsKey(mapdeal.getGeoCode()))
                {
                    mDeals.put(mapdeal.getGeoCode(), mapdeal);
                    j++;
                }
            }
            mVenueDealsQueryRunning = false;
            break;//continue;
        }
        if(true)
        	return;

        if(j <= 0)
        	return;

        post(updateDealOverlays);
        dismissPopup();
        return;
    }

    public void startQuery(int i, Object obj, Uri uri, String as[], String s, String as1[], String s1)
    {
        switch(i) {
			default:
			        super.startQuery(i, obj, uri, as, s, as1, s1);
			        return;
			case 1:
			        mAllDealsQueryRunning = true;
			        break;
			case 2:
			        mVenueDealsQueryRunning = true;
			        break;
        }
        super.startQuery(i, obj, uri, as, s, as1, s1);
    }

  }

  public static class UrbanQPlace
    extends GoogleOverlayItem
  {
    private final Uri mSearchUri;
    private final String mUrbanQid;

    public UrbanQPlace(int paramInt1, int paramInt2, String paramString1, String paramString2, Uri paramUri, String paramString3)
    {
      super(paramInt1, paramInt2, paramString1, paramString2, null);
      this.mUrbanQid = paramString3;
      this.mSearchUri = paramUri;
    }

    public Uri getSearchUri()
    {
      return this.mSearchUri;
    }

    public String getUrbanQid()
    {
      return this.mUrbanQid;
    }
  }

}

