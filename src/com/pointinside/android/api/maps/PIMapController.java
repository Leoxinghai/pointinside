package com.pointinside.android.api.maps;

import android.graphics.Point;
import android.graphics.PointF;
import android.widget.ZoomButtonsController;
import com.pointinside.android.api.utils.MyScroller;

public class PIMapController
{
  private static final String TAG = PIMapController.class.getSimpleName();
  private final PIMapView mMapView;

  PIMapController(PIMapView paramPIMapView)
  {
    this.mMapView = paramPIMapView;
  }

  private float nextZoomFactor(int paramInt)
  {
    float f = this.mMapView.getCurrentOrTargetScale();
    if (paramInt == 1) {
      return f * 1.5F;
    }
    if (paramInt == -1) {
      return f / 1.5F;
    }
    throw new IllegalArgumentException();
  }

  public void animateTo(PIMapLocation paramPIMapLocation)
  {
    paramPIMapLocation.translate(this.mMapView);
    centerToXY(paramPIMapLocation.getTranslatedPixelX(), paramPIMapLocation.getTranslatedPixelY());
  }

  public boolean canZoomIn()
  {
    return this.mMapView.canZoom(nextZoomFactor(1));
  }

  public boolean canZoomOut()
  {
    return this.mMapView.canZoom(nextZoomFactor(-1));
  }

  public void centerToXY(int paramInt1, int paramInt2)
  {
    stopAnimation(false);
    MyScroller localMyScroller = this.mMapView.getScroller();
    int i = paramInt1 - this.mMapView.getWidth() / 2;
    int j = paramInt2 - this.mMapView.getHeight() / 2;
    localMyScroller.startScroll(this.mMapView.getPanX(), this.mMapView.getPanY(), i, j);
    this.mMapView.invalidate();
  }

  public ZoomButtonsController getZoomButtonsController()
  {
    return this.mMapView.getZoomButtonsController();
  }

  public boolean hasNextZone()
  {
    return this.mMapView.hasNextZone();
  }

  public boolean hasPreviousZone()
  {
    return this.mMapView.hasPreviousZone();
  }

  public boolean loadOverview()
  {
    return this.mMapView.goToOverview();
  }

  public boolean loadSpecialArea(long paramLong)
  {
    return this.mMapView.goToSpecialArea(paramLong);
  }

  public boolean loadZone(int paramInt)
  {
    return this.mMapView.goToZone(paramInt);
  }

  public boolean nextZone()
  {
    return this.mMapView.nextZone();
  }

  public boolean previousZone()
  {
    return this.mMapView.previousZone();
  }

  public void scrollBy(int paramInt1, int paramInt2)
  {
    this.mMapView.panBy(paramInt1, paramInt2);
  }

  public void setCenter(int paramInt1, int paramInt2)
  {
    PointF localPointF = this.mMapView.mapPointToViewPoint(paramInt1, paramInt2, new PointF());
    int i = Math.round(localPointF.x) - this.mMapView.getWidth() / 2;
    int j = Math.round(localPointF.y) - this.mMapView.getHeight() / 2;
    this.mMapView.panBy(i, j);
  }

  public void setCenter(Point paramPoint)
  {
    setCenter(paramPoint.x, paramPoint.y);
  }

  public void setCenter(PIMapLocation paramPIMapLocation)
  {
    setCenter(paramPIMapLocation.getPixelX(), paramPIMapLocation.getPixelY());
  }

  public int setZoom(int paramInt)
  {
    if (paramInt == 1)
    {
      this.mMapView.doZoom(this.mMapView.getScaleMin());
      return paramInt;
    }
    throw new UnsupportedOperationException();
  }

  public void stopAnimation(boolean flag)
  {
      MyScroller myscroller = mMapView.getScroller();
      if(flag)
          myscroller.abortAnimation();
      else
          myscroller.forceFinished(true);
      mMapView.cancelZoom();
  }

  public boolean zoomIn()
  {
    return this.mMapView.doZoom(nextZoomFactor(1));
  }

  public boolean zoomInFixing(int paramInt1, int paramInt2)
  {
    return this.mMapView.doZoom(nextZoomFactor(1), paramInt1, paramInt2);
  }

  public boolean zoomOut()
  {
    return this.mMapView.doZoom(nextZoomFactor(-1));
  }

  public boolean zoomOutFixing(int paramInt1, int paramInt2)
  {
    return this.mMapView.doZoom(nextZoomFactor(-1), paramInt1, paramInt2);
  }

  public void zoomToSpan(int paramInt1, int paramInt2)
  {
    throw new UnsupportedOperationException();
  }

  public void zoomToSpan(PIMapLocation paramPIMapLocation)
  {
    throw new UnsupportedOperationException();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapController
 * JD-Core Version:    0.7.0.1
 */