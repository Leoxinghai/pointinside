package com.pointinside.android.api.maps;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.location.AbstractLocationEngine;
import com.pointinside.android.api.location.CompassEngine;
import com.pointinside.android.api.location.GoogleMyLocationEngine;

public class MyLocationOverlay
  extends PIMapOverlay
  implements PIMapOverlay.Snappable, PIMapAnnotation
{
  private static final int INDICATOR_COLOR = -10053121;
  private static final int MILE_IN_METERS = 1609;
  private static final String TAG = MyLocationOverlay.class.getSimpleName();
  private final IndoorLocationOverlayDelegate mDelegate;
  private Display mDisplay;
  private final PIMapView mPIMapView;
  private final MapPixelAdapter mPixelAdapter;
  private String mSubTitle;
  private String mTitle;

  public MyLocationOverlay(Context paramContext, PIMapView paramPIMapView, int paramInt1, int paramInt2)
  {
    this(paramContext, paramPIMapView, new GoogleMyLocationEngine(paramContext), paramInt1, paramInt2);
  }

  public MyLocationOverlay(Context paramContext, PIMapView paramPIMapView, AbstractLocationEngine paramAbstractLocationEngine, int paramInt1, int paramInt2)
  {
    this.mPIMapView = paramPIMapView;
    this.mDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    this.mPixelAdapter = new MapPixelAdapter();
    this.mDelegate = new IndoorLocationOverlayDelegate(paramAbstractLocationEngine, new CompassEngine(paramContext), this.mPixelAdapter);
    this.mDelegate.setIndicatorColor(-10053121);
    Drawable localDrawable1 = paramContext.getResources().getDrawable(paramInt1);
    this.mDelegate.setIndicatorDrawable(localDrawable1, localDrawable1.getIntrinsicWidth(), localDrawable1.getIntrinsicHeight());
    Drawable localDrawable2 = paramContext.getResources().getDrawable(paramInt2);
    this.mDelegate.setCompassDrawable(localDrawable2, localDrawable2.getIntrinsicWidth(), localDrawable2.getIntrinsicHeight());
  }

  private int getRotation()
  {
    switch (this.mDisplay.getOrientation())
    {
    case 0:
        return 270;
    default:
      return 0;
    case 1:
      return 90;
    case 2:
      return 180;
    }
  }

  public void disableCompass()
  {
    this.mDelegate.disableCompass();
  }

  public void disableMyLocation()
  {
    this.mDelegate.disableLocation();
  }

  public void disableNavMode() {}

  public boolean dispatchTap()
  {
    return onTap(this.mPixelAdapter.currentLocationToPixelLocation(), this.mPIMapView);
  }

  protected void drawCompass(Canvas paramCanvas, PIMapView paramPIMapView)
  {
    if (!isCompassEnabled()) {
      throw new IllegalStateException("Can't draw compass drawable when compass is not enabled");
    }
    this.mDelegate.draw(paramCanvas, false, SystemClock.uptimeMillis());
  }

  protected void drawMyLocation(Canvas paramCanvas, PIMapView paramPIMapView)
  {
    if (isCompassEnabled()) {
      throw new IllegalStateException("Can't draw location drawable when compass is enabled");
    }
    this.mDelegate.draw(paramCanvas, false, SystemClock.uptimeMillis());
  }

  public boolean enableCompass()
  {
    return this.mDelegate.enableCompass();
  }

  public boolean enableMyLocation()
  {
    return enableMyLocation(true);
  }

  public boolean enableMyLocation(boolean paramBoolean)
  {
    return this.mDelegate.enableLocation(paramBoolean);
  }

  public void enableNavMode()
  {
    throw new UnsupportedOperationException("Functionality temporarily removed");
  }

  public PIMapLocation getLocation()
  {
    if (this.mDelegate.getLastFix() != null) {
      return this.mPixelAdapter.currentLocationToPixelLocation();
    }
    return null;
  }

  public float getOrientation()
  {
    return this.mDelegate.getOrientation();
  }

  public String getSubTitle()
  {
    return this.mSubTitle;
  }

  public String getTitle()
  {
    return this.mTitle;
  }

  protected boolean hitTest(int paramInt1, int paramInt2, PIMapView paramPIMapView)
  {
    if (this.mDelegate.isCompassEnabled()) {}
    for (Rect localRect = this.mDelegate.getCompassDrawable().getBounds(); (localRect != null) && (localRect.contains(paramInt1, paramInt2)); localRect = this.mDelegate.getIndicatorDrawable().getBounds()) {
      return true;
    }
    return false;
  }

  public boolean isCompassEnabled()
  {
    return this.mDelegate.isCompassEnabled();
  }

  public boolean isMyLocationEnabled()
  {
    return this.mDelegate.isLocationEnabled();
  }

  protected void onDraw(Canvas paramCanvas, PIMapView paramPIMapView, boolean paramBoolean)
  {
    this.mDelegate.draw(paramCanvas, paramBoolean, SystemClock.uptimeMillis());
  }

  protected void onDrawFinished(Canvas paramCanvas, PIMapView paramPIMapView) {}

  protected void onLocationChanged(Location paramLocation) {}

  public boolean onSingleTapUp(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    if (hitTest((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY(), paramPIMapView)) {
      return onTap(this.mPixelAdapter.currentLocationToPixelLocation(), paramPIMapView);
    }
    return false;
  }

  public boolean onSnapToItem(int paramInt1, int paramInt2, Point paramPoint, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onTap(PIMapLocation paramPIMapLocation, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean runOnFirstFix(Runnable paramRunnable)
  {
    return this.mDelegate.runOnFirstFix(paramRunnable);
  }

  public void setLocation(double paramDouble1, double paramDouble2)
  {
    throw new UnsupportedOperationException("Functionality temporarily removed");
  }

  public void setSubTitle(String paramString)
  {
    this.mSubTitle = paramString;
  }

  public void setTitle(String paramString)
  {
    this.mTitle = paramString;
  }

  private class IndoorLocationOverlayDelegate
    extends LocationOverlayDelegate
  {
    private final AbstractLocationEngine mLocationEngine;

    public IndoorLocationOverlayDelegate(AbstractLocationEngine paramAbstractLocationEngine, CompassEngine paramCompassEngine, LocationOverlayDelegate.MapViewAdapter paramMapViewAdapter)
    {
      super(paramAbstractLocationEngine, paramCompassEngine, paramMapViewAdapter);
      this.mLocationEngine = paramAbstractLocationEngine;
      paramAbstractLocationEngine.setAccuracy(1);
    }

    private void adjustAccuracyByProximity(Location paramLocation)
    {
      if (MyLocationOverlay.this.mPIMapView.mPIMapVenue != null)
      {
        Location localLocation = MyLocationOverlay.this.mPIMapView.mPIMapVenue.getLocation();
        if (localLocation != null)
        {
          if (paramLocation.distanceTo(localLocation) <= 1609.0F) {
              if (this.mLocationEngine.getAccuracy() != 2) {
                  return;
                }
                fineRegisterForLocation();
          }
          if (this.mLocationEngine.getAccuracy() == 1) {
            courseRegisterForLocation();
          }
        }
      }
    }

    private void courseRegisterForLocation()
    {
      enableLocation(false);
    }

    private void fineRegisterForLocation()
    {
      enableLocation(true);
    }

    public boolean enableLocation(boolean flag)
    {
    	try {
    	AbstractLocationEngine abstractlocationengine = mLocationEngine;
        int i;
        boolean flag1;
        if(flag)
            i = 1;
        else
            i = 2;
        abstractlocationengine.setAccuracy(i);
        flag1 = enableLocation();
        return flag1;

    	} catch(Exception exception) {
    	}
    	return false;
    }

    public void onLocationChanged(Location paramLocation)
    {
      try
      {
        super.onLocationChanged(paramLocation);
        MyLocationOverlay.this.onLocationChanged(paramLocation);
        adjustAccuracyByProximity(paramLocation);
        return;
      } catch(Exception ex)
      {
      }
    }
  }

  private class MapPixelAdapter
    implements LocationOverlayDelegate.MapViewAdapter
  {
    private final Point mPoint = new Point();

    private MapPixelAdapter() {}

    public float currentAccuracyRadius()
    {
      return MyLocationOverlay.this.mPIMapView.getProjection().translatedPixelCountForMeters(MyLocationOverlay.this.mDelegate.getLastFix().getAccuracy());
    }

    public PIMapLocation currentLocationToPixelLocation()
    {
      Location localLocation = MyLocationOverlay.this.mDelegate.getLastFix();
      PIMapLocation localPIMapLocation = MyLocationOverlay.this.mPIMapView.getProjection().fromCoordinates(localLocation.getLatitude(), localLocation.getLongitude());
      localPIMapLocation.setExtras(localLocation.getExtras());
      localPIMapLocation.translate(MyLocationOverlay.this.mPIMapView);
      return localPIMapLocation;
    }

    public Point currentLocationToPoint()
    {
      PIMapLocation localPIMapLocation = currentLocationToPixelLocation();
      this.mPoint.set(localPIMapLocation.getTranslatedPixelX(), localPIMapLocation.getTranslatedPixelY());
      return this.mPoint;
    }

    public float currentOrientation(float paramFloat)
    {
      return paramFloat - MyLocationOverlay.this.mPIMapView.getCurrentZone().getBearingPoint2Point1();
    }

    public void invalidate()
    {
      MyLocationOverlay.this.mPIMapView.invalidate();
    }

    public boolean isLocationHorizontallyOffscreen()
    {
      throw new UnsupportedOperationException();
    }

    public boolean isLocationVerticallyOffscreen()
    {
      Bundle localBundle = MyLocationOverlay.this.mDelegate.getLastFix().getExtras();
      boolean bool = false;
      if (localBundle != null)
      {
        int i = MyLocationOverlay.this.mPIMapView.getCurrentZoneIndex();
        int j = localBundle.getInt(AbstractLocationEngine.LOCATION_EXTRAS_ZONE_INDEX_KEY, -1);
        bool = false;
        if (j != -1)
        {
          bool = false;
          if (j != i) {
            bool = true;
          }
        }
      }
      return bool;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.MyLocationOverlay
 * JD-Core Version:    0.7.0.1
 */