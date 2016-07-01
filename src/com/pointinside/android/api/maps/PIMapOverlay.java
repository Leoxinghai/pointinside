package com.pointinside.android.api.maps;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class PIMapOverlay
{
  protected static final float SHADOW_X_SKEW = -0.9F;
  protected static final float SHADOW_Y_SCALE = 0.5F;
  protected Drawable mDefaultMarker;
  protected volatile boolean mIsPopulated = false;

  public static Drawable boundCenter(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    int k = -i / 2;
    int m = -j / 2;
    paramDrawable.setBounds(k, m, k + i, m + j);
    return paramDrawable;
  }

  public static Drawable boundCenterBottom(Drawable paramDrawable)
  {
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    int k = -i / 2;
    int m = k + i;
    paramDrawable.setBounds(k, -j, m, 0);
    return paramDrawable;
  }

  boolean isPopulated()
  {
    return this.mIsPopulated;
  }

  protected abstract void onDraw(Canvas paramCanvas, PIMapView paramPIMapView, boolean paramBoolean);

  protected abstract void onDrawFinished(Canvas paramCanvas, PIMapView paramPIMapView);

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onLongPress(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public void onManagedDraw(Canvas paramCanvas, PIMapView paramPIMapView, boolean paramBoolean)
  {
    onDraw(paramCanvas, paramPIMapView, paramBoolean);
    onDrawFinished(paramCanvas, paramPIMapView);
  }

  public boolean onSingleTapUp(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onTrackballEvent(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public static abstract interface Snappable
  {
    public abstract boolean onSnapToItem(int paramInt1, int paramInt2, Point paramPoint, PIMapView paramPIMapView);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapOverlay
 * JD-Core Version:    0.7.0.1
 */