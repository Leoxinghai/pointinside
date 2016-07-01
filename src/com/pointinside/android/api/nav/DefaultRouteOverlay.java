package com.pointinside.android.api.nav;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.TypedValue;
import com.pointinside.android.api.maps.PIMapOverlay;
import com.pointinside.android.api.maps.PIMapVenueZone;
import com.pointinside.android.api.maps.PIMapView;
import com.pointinside.android.api.maps.PIMapView.PIMapViewProjection;
import java.util.ArrayList;

public class DefaultRouteOverlay
  extends PIMapOverlay
{
  private static final String TAG = DefaultRouteOverlay.class.getSimpleName();
  private final Context mContext;
  private String mCurrentZoneUUID;
  private ArrayList<Route.RoutePoint> mPointsInZone;
  private final Route mRoute;
  private final Paint mRoutePaint;
  private final Path mRoutePath;
  private final PointF mTempPoint;

  public DefaultRouteOverlay(Context paramContext, Route paramRoute)
  {
    this.mContext = paramContext;
    this.mRoute = paramRoute;
    this.mRoutePaint = createRoutePaint();
    this.mRoutePath = new Path();
    this.mTempPoint = new PointF();
  }

  private static ArrayList<Route.RoutePoint> buildPointsInZone(Route paramRoute, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramRoute.getPointsCount();
    int j = 0;
    int k = 0;
    if (k >= i) {
      return localArrayList;
    }
    Route.RoutePoint localRoutePoint = paramRoute.getPoint(k);
    if (localRoutePoint.getZoneUUID().equals(paramString))
    {
      j = 1;
      localArrayList.add(localRoutePoint);
    }
    while (j == 0)
    {
      k++;
      break;
    }
    return localArrayList;
  }

  protected Paint createRoutePaint()
  {
    Paint localPaint = new Paint();
    localPaint.setDither(true);
    localPaint.setColor(-7829368);
    localPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    localPaint.setStrokeJoin(Paint.Join.ROUND);
    localPaint.setStrokeCap(Paint.Cap.ROUND);
    localPaint.setStrokeWidth(TypedValue.applyDimension(1, 4.0F, getContext().getResources().getDisplayMetrics()));
    return localPaint;
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public Route getRoute()
  {
    return this.mRoute;
  }

  protected void onDraw(Canvas paramCanvas, PIMapView paramPIMapView, boolean paramBoolean)
  {
    String str = paramPIMapView.getCurrentZone().getZoneUUID();
    if ((this.mCurrentZoneUUID == null) || (!this.mCurrentZoneUUID.equals(str)))
    {
      this.mPointsInZone = buildPointsInZone(this.mRoute, str);
      this.mCurrentZoneUUID = str;
    }
    this.mRoutePath.reset();
    PIMapView.PIMapViewProjection localPIMapViewProjection = paramPIMapView.getProjection();
    int i = this.mPointsInZone.size();
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        paramCanvas.drawPath(this.mRoutePath, this.mRoutePaint);
        return;
      }
      Route.RoutePoint localRoutePoint = (Route.RoutePoint)this.mPointsInZone.get(j);
      PointF localPointF = localPIMapViewProjection.getViewPoint(localRoutePoint.getPixelX(), localRoutePoint.getPixelY(), this.mTempPoint);
      if (j > 0) {
        this.mRoutePath.lineTo(localPointF.x, localPointF.y);
      }
      this.mRoutePath.moveTo(localPointF.x, localPointF.y);
    }
  }

  protected void onDrawFinished(Canvas paramCanvas, PIMapView paramPIMapView) {}
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.nav.DefaultRouteOverlay
 * JD-Core Version:    0.7.0.1
 */