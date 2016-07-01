package com.pointinside.android.api.maps;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class PIMapItemizedOverlay
  extends PIMapOverlay
  implements PIMapOverlay.Snappable
{
  private static final BlurMaskFilter BLUR_FILTER;
  private static final int SHADOW_COLOR = -301989888;
  private static final Paint SHADOW_PAINT = new Paint(3);
  private static final float SHADOW_RADIUS = 2.0F;
  private static final String TAG = "PIMapItemizedOverlay";
  private static volatile Matrix sScaleMatrix;
  private final Rect mDefaultMarkerBounds;
  private PIMapOverlayItem mFocusedItem;
  private int mLastFocusedIndex = -1;

  static
  {
    BLUR_FILTER = new BlurMaskFilter(2.0F, BlurMaskFilter.Blur.SOLID);
    SHADOW_PAINT.setColor(-16777216);
    SHADOW_PAINT.setMaskFilter(BLUR_FILTER);
  }

  public PIMapItemizedOverlay(Drawable paramDrawable)
  {
    this.mDefaultMarker = paramDrawable;
    this.mDefaultMarkerBounds = paramDrawable.copyBounds();
  }

  private void drawShadow(Canvas paramCanvas, PIMapView paramPIMapView, Bitmap paramBitmap, Rect paramRect)
  {
    int[] arrayOfInt = new int[2];
    Bitmap localBitmap = paramBitmap.extractAlpha(SHADOW_PAINT, arrayOfInt);
    RectF localRectF = new RectF(paramRect);
    paramCanvas.save();
    Matrix localMatrix = paramCanvas.getMatrix();
    localMatrix.setSkew(-0.8F, 0.0F, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
    localMatrix.mapRect(localRectF);
    paramCanvas.drawBitmap(localBitmap, null, localRectF, null);
    paramCanvas.restore();
  }

  public Rect copyDefaultMarkerDrawableBounds(Rect paramRect)
  {
    if (paramRect == null) {
      paramRect = new Rect();
    }
    paramRect.set(this.mDefaultMarkerBounds);
    return paramRect;
  }

  protected abstract PIMapOverlayItem createItem(int paramInt);

  public boolean dispatchTap(PIMapOverlayItem paramItem)
  {
    int i = size();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return false;
      }
      if (getItem(j).equals(paramItem)) {
        return onTap(j);
      }
    }
  }

  public PIMapLocation getCenter()
  {
    return null;
  }

  public Drawable getDefaultMarkerDrawable()
  {
    return this.mDefaultMarker;
  }

  public PIMapOverlayItem getFocus()
  {
    return this.mFocusedItem;
  }

  protected int getIndexToDraw(int paramInt)
  {
    return paramInt;
  }

  public final PIMapOverlayItem getItem(int paramInt)
  {
    return createItem(paramInt);
  }

  public final int getLastFocusedIndex()
  {
    return this.mLastFocusedIndex;
  }

  protected boolean hitTest(PIMapOverlayItem paramItem, Drawable paramDrawable, int paramInt1, int paramInt2, PIMapView paramPIMapView)
  {
    Rect localRect = paramItem.getHitBounds();
    return (localRect != null) && (localRect.contains(paramInt1, paramInt2));
  }

  public PIMapOverlayItem nextFocus(boolean paramBoolean)
  {
    if (size() == 0) {
        return null;
    }
    do
    {
      do
      {
        if (this.mLastFocusedIndex == -1)
        {
          setLastFocusedIndex(0);
          PIMapOverlayItem localPIMapOverlayItem3 = getItem(this.mLastFocusedIndex);
          setFocus(localPIMapOverlayItem3);
          return localPIMapOverlayItem3;
        }
        if (!paramBoolean) {
            int i = -1 + this.mLastFocusedIndex;
            this.mLastFocusedIndex = i;
            PIMapOverlayItem localPIMapOverlayItem1 = getItem(i);
            setFocus(localPIMapOverlayItem1);
            return localPIMapOverlayItem1;
        }
      } while (this.mLastFocusedIndex >= -1 + size());
      int j = 1 + this.mLastFocusedIndex;
      this.mLastFocusedIndex = j;
      PIMapOverlayItem localPIMapOverlayItem2 = getItem(j);
      setFocus(localPIMapOverlayItem2);
      return localPIMapOverlayItem2;
    } while (this.mLastFocusedIndex <= 0);
  }

  public void onDraw(Canvas paramCanvas, PIMapView paramPIMapView, boolean paramBoolean)
  {
    for (int i = -1 + size();; i--)
    {
      if (i < 0) {
        return;
      }
      PIMapOverlayItem localPIMapOverlayItem = getItem(i);
      if (localPIMapOverlayItem != null)
      {
        PIMapLocation localPIMapLocation = localPIMapOverlayItem.getLocation();
        localPIMapLocation.translate(paramPIMapView, localPIMapOverlayItem.getZoneIndex());
        onDrawItem(paramCanvas, paramPIMapView, i, localPIMapLocation, paramBoolean);
      }
    }
  }

  protected void onDrawFinished(Canvas paramCanvas, PIMapView paramPIMapView) {}

  protected void onDrawItem(Canvas paramCanvas, PIMapView paramPIMapView, int paramInt, PIMapLocation paramPIMapLocation, boolean paramBoolean)
  {
    PIMapOverlayItem localPIMapOverlayItem = getItem(paramInt);
    Drawable localDrawable = localPIMapOverlayItem.getMarker(0);
    if (localDrawable == null) {
      localDrawable = this.mDefaultMarker;
    }
    for (Rect localRect = this.mDefaultMarkerBounds;; localRect = localPIMapOverlayItem.getMarkerBounds())
    {
      int i = paramPIMapLocation.getTranslatedPixelX() + localRect.left;
      int j = i + localRect.width();
      int k = paramPIMapLocation.getTranslatedPixelY() + localRect.top;
      int m = k + localRect.height();
      localDrawable.setBounds(i, k, j, m);
      localPIMapOverlayItem.setHitBounds(i, k, j, m);
      localDrawable.draw(paramCanvas);
      localDrawable.setBounds(localRect);
      return;
    }
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onSingleTapUp(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    int i = size();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return false;
      }
      PIMapOverlayItem localPIMapOverlayItem = getItem(j);
      if (hitTest(localPIMapOverlayItem, localPIMapOverlayItem.getMarker(0), (int)paramMotionEvent.getX(), (int)paramMotionEvent.getY(), paramPIMapView))
      {
        setFocus(localPIMapOverlayItem);
        return (onTap(localPIMapOverlayItem.getLocation(), paramPIMapView)) || (onTap(j));
      }
    }
  }

  public boolean onSnapToItem(int paramInt1, int paramInt2, Point paramPoint, PIMapView paramPIMapView)
  {
    return false;
  }

  protected boolean onTap(int paramInt)
  {
    return false;
  }

  public boolean onTap(PIMapLocation paramPIMapLocation, PIMapView paramPIMapView)
  {
    return false;
  }

  public boolean onTrackballEvent(MotionEvent paramMotionEvent, PIMapView paramPIMapView)
  {
    return false;
  }

  protected final void populate()
  {
	  this.mIsPopulated = true;
  }

  public void setDrawFocusedItem(boolean paramBoolean) {}

  public void setFocus(PIMapOverlayItem paramItem)
  {
    this.mFocusedItem = paramItem;
  }

  protected void setLastFocusedIndex(int paramInt)
  {
    this.mLastFocusedIndex = paramInt;
  }

  public void setOnFocusChangeListener(OnFocusChangeListener paramOnFocusChangeListener) {}

  public abstract int size();

  public abstract interface OnFocusChangeListener
  {
    public abstract void onFocusChanged(PIMapItemizedOverlay paramPIMapItemizedOverlay, PIMapOverlayItem paramPIMapOverlayItem);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapItemizedOverlay
 * JD-Core Version:    0.7.0.1
 */