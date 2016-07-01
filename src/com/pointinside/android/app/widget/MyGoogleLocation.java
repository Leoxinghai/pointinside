package com.pointinside.android.app.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.MapView;
//import com.google.android.maps.MyLocationOverlay;
import com.google.android.gms.maps.Projection;


public class MyGoogleLocation
//  extends MyLocationOverlay
{
  private static final long ACCURACY_LIMIT = 12L;
  private final Paint mErrorCirclePaint;
  private final Rect mLocationBounds;
  private AnimationDrawable mLocationImage;
  private int mLocationImageCenterX;
  private int mLocationImageCenterY;
  private int mLocationImageHeight;
  private int mLocationImageWidth;
  
  public MyGoogleLocation(Context paramContext, MapView paramMapView)
  {
//    super(paramContext, paramMapView);
    this.mLocationImage = ((AnimationDrawable)paramContext.getResources().getDrawable(2130837681));
    this.mErrorCirclePaint = new Paint();
    this.mErrorCirclePaint.setARGB(0, 102, 153, 255);
    this.mErrorCirclePaint.setStrokeWidth(3.0F);
    this.mErrorCirclePaint.setDither(true);
    this.mErrorCirclePaint.setAntiAlias(true);
    int i = paramContext.getResources().getDimensionPixelSize(2131296264);
    this.mLocationImageWidth = i;
    this.mLocationImageHeight = i;
    this.mLocationImageCenterX = (this.mLocationImageWidth / 2);
    this.mLocationImageCenterY = (this.mLocationImageHeight / 2);
    int j = -this.mLocationImageCenterX;
    int k = j + this.mLocationImageWidth;
    int m = -this.mLocationImageCenterY;
    int n = m + this.mLocationImageHeight;
    this.mLocationImage.setBounds(j, m, k, n);
    this.mLocationBounds = this.mLocationImage.copyBounds();
    this.mLocationImage.start();
  }
  
  private void drawErrorRing(Canvas paramCanvas, Projection paramProjection, float paramFloat, int paramInt1, int paramInt2)
  {
    if (paramFloat > 12.0F)
    {
      paramCanvas.save();
      //float f = paramProjection.metersToEquatorPixels(paramFloat);
      //float f = paramProjection.metersToEquatorPixels(paramFloat);
      float f = 12.0F;
      this.mErrorCirclePaint.setAlpha(50);
      this.mErrorCirclePaint.setStyle(Paint.Style.FILL);
      paramCanvas.drawCircle(paramInt1, paramInt2, f, this.mErrorCirclePaint);
      this.mErrorCirclePaint.setAlpha(150);
      this.mErrorCirclePaint.setStyle(Paint.Style.STROKE);
      paramCanvas.drawCircle(paramInt1, paramInt2, f, this.mErrorCirclePaint);
      paramCanvas.restore();
    }
  }
  
  protected void drawMyLocation(Canvas paramCanvas, MapView paramMapView, Location paramLocation, LatLng paramGeoPoint, long paramLong)
  {
    //Projection localProjection = paramMapView.getProjection();
	Projection localProjection =  paramMapView.getMap().getProjection();
    Point localPoint = localProjection.toScreenLocation(paramGeoPoint);
    if ((paramLocation != null) && (paramLocation.hasAccuracy())) {
      drawErrorRing(paramCanvas, localProjection, paramLocation.getAccuracy(), localPoint.x, localPoint.y);
    }
    paramCanvas.save();
    AnimationDrawable localAnimationDrawable = this.mLocationImage;
    int i = localPoint.x + this.mLocationBounds.left;
    int j = i + this.mLocationImageWidth;
    int k = localPoint.y + this.mLocationBounds.top;
    localAnimationDrawable.setBounds(i, k, j, k + this.mLocationImageHeight);
    localAnimationDrawable.draw(paramCanvas);
    paramCanvas.restore();
  }
}

