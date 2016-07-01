package com.pointinside.android.api.maps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import com.pointinside.android.api.location.CompassEngine;
import com.pointinside.android.api.location.CompassEngine.CompassEngineListener;
import com.pointinside.android.api.location.LocationEngine;
import com.pointinside.android.api.location.LocationEngineListener;
import java.util.ArrayList;

public class LocationOverlayDelegate
  implements LocationEngineListener, CompassEngine.CompassEngineListener
{
  private final MapViewAdapter mAdapter;
  private final CompassEngine mCompassEngine;
  private Drawable mCompassImage;
  private int mCompassImageHeight;
  private int mCompassImageWidth;
  private final Paint mErrorCircleEdgePaint;
  private final Paint mErrorCircleShadePaint;
  private final Handler mHandler = new Handler();
  private final LocationEngine mLocationEngine;
  private Drawable mLocationImage;
  private int mLocationImageHeight;
  private int mLocationImageWidth;
  private float mOrientation;
  private final ArrayList<Runnable> mRunOnFirstFix = new ArrayList();

  public LocationOverlayDelegate(LocationEngine paramLocationEngine, CompassEngine paramCompassEngine, MapViewAdapter paramMapViewAdapter)
  {
    this.mLocationEngine = paramLocationEngine;
    this.mCompassEngine = paramCompassEngine;
    this.mAdapter = paramMapViewAdapter;
    this.mErrorCircleShadePaint = createErrorCirclePaint(50, Paint.Style.FILL);
    this.mErrorCircleEdgePaint = createErrorCirclePaint(150, Paint.Style.STROKE);
  }

  private static Paint createErrorCirclePaint(int paramInt, Paint.Style paramStyle)
  {
    Paint localPaint = new Paint();
    localPaint.setStrokeWidth(3.0F);
    localPaint.setAlpha(paramInt);
    localPaint.setDither(true);
    localPaint.setAntiAlias(true);
    localPaint.setStyle(paramStyle);
    return localPaint;
  }

  private void drawAccuracyShade(Canvas paramCanvas, int paramInt1, int paramInt2, float paramFloat)
  {
    paramCanvas.drawCircle(paramInt1, paramInt2, paramFloat, this.mErrorCircleShadePaint);
    paramCanvas.drawCircle(paramInt1, paramInt2, paramFloat, this.mErrorCircleEdgePaint);
  }

  private void handleFirstFixRunnables()
  {
    int i = this.mRunOnFirstFix.size();
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        this.mRunOnFirstFix.clear();
        return;
      }
      Runnable localRunnable = (Runnable)this.mRunOnFirstFix.get(j);
      this.mHandler.post(localRunnable);
    }
  }

  public void disableCompass()
  {
    try
    {
      this.mCompassEngine.setCompassListener(null);
      this.mCompassEngine.disableCompass();
      return;
     } catch(Exception ex){
    }
  }

  public void disableLocation()
  {
    try
    {
      this.mLocationEngine.setLocationListener(null);
      this.mLocationEngine.disableLocation();
      return;
    } catch(Exception ex){

    }

  }

  public boolean draw(Canvas paramCanvas, boolean paramBoolean, long paramLong)
  {
    if (!paramBoolean) {}
    try
    {
      if (this.mLocationEngine.getLastFix() != null) {
        drawMyLocation(paramCanvas, this.mLocationEngine.getLastFix(), paramLong);
        return true;
      }
      return false;
    } catch(Exception ex){
    }
    return false;
  }

  protected void drawMyLocation(Canvas paramCanvas, Location paramLocation, long paramLong)
  {
    boolean bool;
    bool = isCompassEnabled();
    try
    {
      if ((bool) && (this.mCompassImage == null)) {
        throw new IllegalStateException("Must call setCompassDrawable before drawing occurs");
      }
    } catch(Exception ex){

    }

    if (this.mLocationImage == null) {
      throw new IllegalStateException("Must call setIndicatorDrawable before drawing occurs");
    }

    int i;
    int j;
    int k;
    int m;
    Point localPoint;
    Drawable localDrawable;
    if (bool)
    {
      i = this.mCompassImageWidth;
      j = this.mCompassImageHeight;
      localDrawable = this.mCompassImage;
    } else {
        i = this.mLocationImageWidth;
        j = this.mLocationImageHeight;
        localDrawable = this.mLocationImage;
    }
      k = i / 2;
      m = j / 2;
      localPoint = this.mAdapter.currentLocationToPoint();
      if (paramLocation.hasAccuracy())
      {
        float f = this.mAdapter.currentAccuracyRadius();
        if ((f > k) || (f > m)) {
          drawAccuracyShade(paramCanvas, localPoint.x, localPoint.y, f);
        }
      }

      if (!this.mAdapter.isLocationVerticallyOffscreen()) {
          localDrawable.setColorFilter(null);
      }
      ColorMatrix localColorMatrix = new ColorMatrix();
      localColorMatrix.setSaturation(0.0F);
      ColorMatrixColorFilter localColorMatrixColorFilter = new ColorMatrixColorFilter(localColorMatrix);
      localDrawable.setColorFilter(localColorMatrixColorFilter);

      int n = localPoint.x - k;
      int i1 = n + i;
      int i2 = localPoint.y - m;
      localDrawable.setBounds(n, i2, i1, i2 + j);
      if (bool)
      {
        paramCanvas.save();
        paramCanvas.rotate(this.mAdapter.currentOrientation(this.mOrientation), localPoint.x, localPoint.y);
      }
      localDrawable.draw(paramCanvas);
      if (bool) {
        paramCanvas.restore();
      }
      return;
  }

  public boolean enableCompass()
  {
    try
    {
      this.mCompassEngine.setCompassListener(this);
      boolean bool = this.mCompassEngine.enableCompass();
      return bool;
    } catch(Exception ex)
    {
    }
    return false;
  }

  public boolean enableLocation()
  {
    try
    {
      this.mLocationEngine.setLocationListener(this);
      boolean bool = this.mLocationEngine.enableLocation();
      return bool;
    }
    catch(Exception ex)
    {
    }
    return false;
  }

  public Drawable getCompassDrawable()
  {
    try
    {
      Drawable localDrawable = this.mCompassImage;
      return localDrawable;
    }
    catch(Exception ex)
    {
    }
    return null;
  }

  public Drawable getIndicatorDrawable()
  {
    try
    {
      Drawable localDrawable = this.mLocationImage;
      return localDrawable;
    }
    catch(Exception ex)
    {
    }
    return null;
  }

  public Location getLastFix()
  {
    try
    {
      Location localLocation = this.mLocationEngine.getLastFix();
      return localLocation;
    }
    catch(Exception ex)
    {
    }
    return null;
  }

  public float getOrientation()
  {
      float f = this.mOrientation;
      return f;
  }

  public boolean isCompassEnabled()
  {
      boolean bool = this.mCompassEngine.isCompassEnabled();
      return bool;
  }

  public boolean isLocationEnabled()
  {
      boolean bool = this.mLocationEngine.isLocationEnabled();
      return bool;
  }

  public void onCompassChanged(float paramFloat)
  {
    try
    {
      this.mOrientation = paramFloat;
      this.mAdapter.invalidate();
      return;
    }
    catch(Exception ex)
    {
    }
  }

  public void onLocationChanged(Location paramLocation)
  {
    try
    {
      handleFirstFixRunnables();
      this.mAdapter.invalidate();
      return;
    }
    catch(Exception ex)
    {
    }
  }

  public final void onStatusChanged(int paramInt, Bundle paramBundle) {}

  public boolean runOnFirstFix(Runnable runnable)
  {

      boolean flag = true;
	  if(getLastFix() != null) {
	      mHandler.post(runnable);
	      return flag;
	  } else {
	      mRunOnFirstFix.add(runnable);
	      flag = false;
	      return flag;
	  }
  }

  public void setCompassDrawable(Drawable paramDrawable, int paramInt1, int paramInt2)
  {
    try
    {
      this.mCompassImage = paramDrawable;
      this.mCompassImageWidth = paramInt1;
      this.mCompassImageHeight = paramInt2;
      this.mAdapter.invalidate();
      return;
    }
	catch(Exception ex)
	{
    }
  }

  public void setIndicatorColor(int paramInt)
  {
    try
    {
      this.mErrorCircleShadePaint.setColor(Color.argb(this.mErrorCircleShadePaint.getAlpha(), Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt)));
      this.mErrorCircleEdgePaint.setColor(Color.argb(this.mErrorCircleEdgePaint.getAlpha(), Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt)));
      this.mAdapter.invalidate();
      return;
    }
	catch(Exception ex)
    {
    }
  }

  public void setIndicatorDrawable(Drawable paramDrawable, int paramInt1, int paramInt2)
  {
    try
    {
      this.mLocationImage = paramDrawable;
      this.mLocationImageWidth = paramInt1;
      this.mLocationImageHeight = paramInt2;
      this.mAdapter.invalidate();
      return;
    }
	catch(Exception ex)
    {
    }
  }

  public static abstract interface MapViewAdapter
  {
    public abstract float currentAccuracyRadius();

    public abstract Point currentLocationToPoint();

    public abstract float currentOrientation(float paramFloat);

    public abstract void invalidate();

    public abstract boolean isLocationHorizontallyOffscreen();

    public abstract boolean isLocationVerticallyOffscreen();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.LocationOverlayDelegate
 * JD-Core Version:    0.7.0.1
 */