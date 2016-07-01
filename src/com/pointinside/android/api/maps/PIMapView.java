package com.pointinside.android.api.maps;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;


import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapDealDataCursor;
import com.pointinside.android.api.dao.PIMapEventDataCursor;
import com.pointinside.android.api.dao.PIMapItemDataCursor;
import com.pointinside.android.api.dao.PIMapItemDataCursor.PIMapItem;
//import com.pointinside.android.api.dao.PIMapLandmarkDataCursor.PIMapPlace;;
import com.pointinside.android.api.dao.PIMapLandmarkDataCursor;
import com.pointinside.android.api.dao.PIMapPlaceDataCursor;
import com.pointinside.android.api.dao.PIMapPolygonZoneDataCursor;
import com.pointinside.android.api.dao.PIMapServiceDataCursor;
import com.pointinside.android.api.dao.PIMapWormholeDataCursor;
import com.pointinside.android.api.dao.PIMapZoneDataCursor;
import com.pointinside.android.api.dao.PixelCoordinateDataCursor;
import com.pointinside.android.api.utils.MyScroller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public final class PIMapView
extends ViewGroup
{
static final float ONE_ZOOM_LEVEL = 1.5F;
static final String TAG = "PIMapView";
static final boolean USE_BUILT_IN_ZOOM = false;
private static final float ZOOM_DECELERATE_FACTOR = 2.0F;
private static final int ZOOM_DURATION = 450;
private static final Object sLock = new Object();
private final PIMapController mController;
private Uri mCurrentImageUri;
PIMapVenueZone mCurrentZone;
private int mCurrentZoneOrderIndex = 0;
private boolean mDirtyBounds;


private float azimut = 0.0f;

public void setAzimut(float f1) {
	  azimut = f1;
}

private final Runnable mDismissOnScreenControlRunner = new Runnable()
{
  public void run()
  {
    PIMapView.this.hideOnScreenControls();
  }
};
private Display mDisplay;
private final RectF mDstRect = new RectF();
final Handler mHandler = new Handler();
private boolean mHasOverview = false;
private boolean mHotlinkZones = false;
private final InputHandler mInputHandler;
private Context mLocalContext;
private LogoView mLogoView;
private int mMapPaddingBottom;
private int mMapPaddingLeft;
private int mMapPaddingRight;
private int mMapPaddingTop;
private final Matrix mMatrix = new Matrix();
private MapCenterAndZoom mNextMapCenterAndZoom;
private OnLongClickMapListener mOnLongClickMapListener;
private final ArrayList<Runnable> mOnMapReadyRunnables = new ArrayList();
private OnPlaceClickListener mOnPlaceClickListener;
private OnZoneChangeListener mOnZoneChangeListener;
private final Runnable mOnZoneChangeRunnable = new Runnable()
{
  public void run()
  {
    PIMapView.this.onZoneChanged();
  }
};
private OnZoomChangedListener mOnZoomChangedListener;
private OverlayView mOverlayView;
private final OverlayList<PIMapOverlay> mOverlays = new OverlayList();
private final LinkedHashMap<Long, PIMapPolygonZone> mOverviewPolygons = new LinkedHashMap();
PIMapVenueZone mOverviewZone;
PIMapVenue mPIMapVenue;
private final Paint mPaint;
private int mPanX;
private int mPanY;
private final PIMapViewProjection mProjection;
private float mScaleFactor;
private float mScaleFactorMax;
private float mScaleFactorMin;
private final MyScroller mScroller;
private long mSpecialAreaId = -1L;
private final Rect mSrcRect = new Rect();
private final PointF mTempPoint1 = new PointF();
private final PointF mTempPoint2 = new PointF();
private final AsyncTileLoader mTileLoader = new AsyncTileLoader(this);
private boolean mUseBuiltInZoom = false;
final LinkedHashMap<Integer, PIMapVenueZone> mVenueZones = new LinkedHashMap();
private WindowManager mWindowManager;
protected Bitmap mZoneBitmap;
private int[] mZoneIndices;
private AnimationSet mZoomAnim;
private float mZoomAnimTargetScale;
private ZoomButtonsController mZoomButtonsController;
private final float[] mZoomMatrixValues = new float[9];
private Point mZoomPoint;
private final Transformation mZoomTransformation = new Transformation();

public PIMapView(Context paramContext)
{
  this(paramContext, null);
}

public PIMapView(Context paramContext, AttributeSet paramAttributeSet)
{
  this(paramContext, paramAttributeSet, 0);
}

public PIMapView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
{
  super(paramContext, paramAttributeSet, paramInt);
  this.mLocalContext = paramContext;

  if (PIMapActivity.sPIMapActivity != null)
  {
    PIMapActivity.sPIMapActivity.setMapView(this);
    setWillNotDraw(false);
    this.mPaint = new Paint();
    this.mPaint.setFilterBitmap(true);
    this.mPaint.setDither(true);
    this.mController = new PIMapController(this);
    this.mProjection = new PIMapViewProjection();
    this.mScroller = new MyScroller(paramContext);
    this.mInputHandler = new InputHandler(this, this.mScroller);
    attachLayout();
    this.mWindowManager = ((WindowManager)paramContext.getSystemService("window"));
    this.mDisplay = this.mWindowManager.getDefaultDisplay();
    return;
  }
  throw new IllegalStateException("PIMapView can only be used in a PIMapActivity!");

}

private void attachLayout()
{
  this.mLogoView = new LogoView(this.mLocalContext);
  addView(this.mLogoView, new LayoutParams(-2, -2, 85));
  this.mOverlayView = new OverlayView(this.mLocalContext);
  addView(this.mOverlayView, new LayoutParams(-1, -1));
}

private static float clamp(float paramFloat1, float paramFloat2, float paramFloat3)
{
  return Math.max(paramFloat2, Math.min(paramFloat3, paramFloat1));
}

private static int clamp(int paramInt1, int paramInt2, int paramInt3)
{
  return Math.max(paramInt2, Math.min(paramInt3, paramInt1));
}

private void doLayout(boolean flag, int i, int j, int k, int l)
{
    int i1;
    int j1;
    int k1;
    int l1;
    i1 = getChildCount();
    j1 = k - i;
    k1 = l - j;
    l1 = 0;

    View view;
    while(true) {
	      if(l1 >= i1)
	          return;
    view = getChildAt(l1);

    if(view.getVisibility() != 8) {

  	  LayoutParams layoutparams;
    int i2;
    int j2;
    int k2;
    int l2;
    layoutparams = (LayoutParams)view.getLayoutParams();
    i2 = view.getMeasuredWidth();
    j2 = view.getMeasuredHeight();
    k2 = layoutparams.gravity;
    l2 = layoutparams.alignment;
    if(k2 == -1) {
        if(l2 != -1)
            onScrollChild(view, layoutparams, 0, 0, i2, j2, l2);
	      l1++;
	      continue;
    }

    int i3;
    int j3;
    i3 = k2 & 7;
    j3 = k2 & 0x70;
    int k3 =0;
    switch(i3) {
	      case 2:
	      case 4:
        default:
      	      k3 = 0 + layoutparams.leftMargin;
      	      break;
        case 1:
      	      k3 = 0 + ((j1 + 0 + layoutparams.leftMargin + layoutparams.rightMargin) - i2) / 2;
        break;
        case 3:
      	      k3 = 0 + layoutparams.leftMargin;
      	  break;
        case 5:
      	      k3 = j1 - i2 - layoutparams.rightMargin;
      	  break;


    }

    int l3 =0;
    switch(j3) {
    	default:
  	      l3 = 0 + layoutparams.topMargin;
  	      break;
    	case 16:
  	      l3 = 0 + ((k1 + 0 + layoutparams.topMargin + layoutparams.bottomMargin) - j2) / 2;
  	      break;
    	case 48:
  	      l3 = 0 + layoutparams.topMargin;
  	      break;
    	case 80:
  	      l3 = k1 - j2 - layoutparams.bottomMargin;
  	      break;
    }

    view.layout(k3, l3, k3 + i2, l3 + j2);

    } else {
    }
    l1++;

  }
}

private MapCenterAndZoom getCurrentMapCenterAndZoom()
{
  float f = (float)(this.mCurrentZone.getFeetPerPixelX() * getScale() * getResources().getDisplayMetrics().xdpi);
  PIMapLocation localPIMapLocation = getMapCenter();
  MapCenterAndZoom localMapCenterAndZoom = new MapCenterAndZoom();
  localMapCenterAndZoom.feetPerInchX = f;
  localMapCenterAndZoom.centerLatitude = localPIMapLocation.getLatitude();
  localMapCenterAndZoom.centerLongitude = localPIMapLocation.getLongitude();
  return localMapCenterAndZoom;
}


private static Collection getSpecialAreas(PIMapVenue pimapvenue, Map map)
{
    HashMap hashmap;
    PIMapPolygonZoneDataCursor pimappolygonzonedatacursor;
    hashmap = new HashMap();
    try {
    pimappolygonzonedatacursor = pimapvenue.getPolygonZones();
    if(pimappolygonzonedatacursor != null) {
  	  boolean flag1 = pimappolygonzonedatacursor.moveToFirst();
  	  while(flag1) {
		      PIMapPolygonZone pimappolygonzone1 = (PIMapPolygonZone)hashmap.get(Long.valueOf(pimappolygonzonedatacursor.getSpecialAreaId()));
		      if(pimappolygonzone1 == null) {
		    	      PIMapPolygonZone pimappolygonzone2 = pimappolygonzonedatacursor.getPIMapPolygonZone();
		    	      hashmap.put(Long.valueOf(pimappolygonzone2.getSpecialAreaId()), pimappolygonzone2);
		    	      pimappolygonzone2.setPolygon(new PIMapPolygon((PIMapVenueZone)map.get(Integer.valueOf(pimappolygonzone2.getHostZoneIndex()))));
		      } else {
		    	  pimappolygonzone1.addHotLink(pimappolygonzonedatacursor.getId(), pimappolygonzonedatacursor.getPixelX(), pimappolygonzonedatacursor.getPixelY(), pimappolygonzonedatacursor.getZoneId(), pimappolygonzonedatacursor.getZoneIndex(), pimappolygonzonedatacursor.getPlaceCount());
		      }
		      flag1 = pimappolygonzonedatacursor.moveToNext();
    	  }
    	pimappolygonzonedatacursor.close();
    } else {

  	  PixelCoordinateDataCursor pixelcoordinatedatacursor = pimapvenue.getPixelCoordinates();
    if(pixelcoordinatedatacursor != null) {
  	  boolean flag = pixelcoordinatedatacursor.moveToFirst();

  	  while(flag) {
		      PixelCoordinate pixelcoordinate;
		      PIMapPolygonZone pimappolygonzone;
		      pixelcoordinate = pixelcoordinatedatacursor.getPixelCoordinate();
		      pimappolygonzone = (PIMapPolygonZone)hashmap.get(Long.valueOf(pixelcoordinate.specialAreaId));
		      if(pimappolygonzone == null){
	    	      PIMapPolygonZone pimappolygonzone2 = pimappolygonzonedatacursor.getPIMapPolygonZone();
	    	      hashmap.put(Long.valueOf(pimappolygonzone2.getSpecialAreaId()), pimappolygonzone2);
	    	      pimappolygonzone2.setPolygon(new PIMapPolygon((PIMapVenueZone)map.get(Integer.valueOf(pimappolygonzone2.getHostZoneIndex()))));
		      }
		      pimappolygonzone.getPolygon().addCoordinate(pixelcoordinate);
		      flag = pixelcoordinatedatacursor.moveToNext();
  	  }
	      pixelcoordinatedatacursor.close();
    }

    Iterator iterator = hashmap.values().iterator();

    if(!iterator.hasNext())
        ((PIMapPolygonZone)iterator.next()).getPolygon().close();
    }
    return hashmap.values();

} catch(Exception exception1) {
	  exception1.printStackTrace();
}
    return null;
//    exception1;
//    pimappolygonzonedatacursor.close();
//    throw exception1;
//    Exception exception;
//    exception;
//    pixelcoordinatedatacursor.close();
//    throw exception;
}


private int getZoneOrderIndex()
{
    int i =0;
    if(mZoneIndices != null) {
	      int k;
	      int j = mZoneIndices.length;
	      if(mCurrentZone == null)
	          k = 0;
	      else
	          k = mCurrentZone.getZoneIndex();
	      i = 0;
		  for(;i < j;) {
  	      if(mZoneIndices[i] == k)
  	    	  return i;
  	      i++;
	      }
    }
    i = 0;
    return i;
}

private void onMapReady()
{
  int i = this.mOnMapReadyRunnables.size();
  for (int j = 0;; j++)
  {
    if (j >= i)
    {
      this.mOnMapReadyRunnables.clear();
      return;
    }
    ((Runnable)this.mOnMapReadyRunnables.get(j)).run();
  }
}

private void onScrollChild(View view, LayoutParams layoutparams, int i, int j, int k, int l, int i1)
{
    layoutparams.translate(this);
    int j1;
    int k1;
    switch(i1) {
//    JVM INSTR lookupswitch 9: default 88
//                   1: 253
//                   3: 137
//                   5: 167
//                   16: 283
//                   17: 313
//                   48: 200
//                   51: 348
//                   80: 225
//                   81: 373;
//       goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10
    default:
    j1 = (layoutparams.x + layoutparams.offsetX) - k / 2;
    k1 = (layoutparams.y + layoutparams.offsetY) - l;
    break;
//_L3:
    case 3:
    j1 = layoutparams.x + layoutparams.offsetX;
    k1 = (layoutparams.y + layoutparams.offsetY) - l / 2;
    break; /* Loop/switch isn't completed */
//_L4:
    case 5:
    j1 = (layoutparams.x + layoutparams.offsetX) - k;
    k1 = (layoutparams.y + layoutparams.offsetY) - l / 2;
    break; /* Loop/switch isn't completed */
//_L7:
    case 48:
    j1 = layoutparams.x + layoutparams.offsetX;
    k1 = layoutparams.y + layoutparams.offsetY;
    break; /* Loop/switch isn't completed */
//_L9:
    case 80:
    j1 = layoutparams.x + layoutparams.offsetX;
    k1 = (layoutparams.y + layoutparams.offsetY) - l;
    break; /* Loop/switch isn't completed */
//_L2:
    case 1:
    j1 = (layoutparams.x + layoutparams.offsetX) - k / 2;
    k1 = layoutparams.y + layoutparams.offsetY;
    break; /* Loop/switch isn't completed */
//_L5:
    case 16:
    j1 = layoutparams.x + layoutparams.offsetX;
    k1 = (layoutparams.y + layoutparams.offsetY) - l / 2;
    break; /* Loop/switch isn't completed */
//_L6:
    case 17:
    j1 = (layoutparams.x + layoutparams.offsetX) - k / 2;
    k1 = (layoutparams.y + layoutparams.offsetY) - l / 2;
    break; /* Loop/switch isn't completed */
//_L8:
    case 51:
    j1 = layoutparams.x + layoutparams.offsetX;
    k1 = layoutparams.y + layoutparams.offsetY;
    break; /* Loop/switch isn't completed */
//_L10:
    case 81:
    j1 = (layoutparams.x + layoutparams.offsetX) - k / 2;
    k1 = (layoutparams.y + layoutparams.offsetY) - l;
    break;
    }

//    _L12:
    view.layout(j1, k1, j1 + k, k1 + l);
    return;

}

private void onZoneChanged()
{
  this.mNextMapCenterAndZoom = getCurrentMapCenterAndZoom();
  zoomToZoomPoint();
  if (this.mOnZoneChangeListener != null) {
    this.mOnZoneChangeListener.onZoneChange(this);
  }
}

private static int pinPan(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float paramFloat)
{
  float f = paramFloat * paramInt2;
  if (paramInt5 - paramInt3 - paramInt4 > f) {
    return -(int)((paramInt5 - f) / 2.0F);
  }
  return clamp(paramInt1, -paramInt3, paramInt4 + (int)(f - paramInt5));
}

private boolean recomputeInitialScale()
{
  Bitmap localBitmap = this.mZoneBitmap;
  if (localBitmap == null) {
    return false;
  }
  float f1 = localBitmap.getWidth();
  float f2 = localBitmap.getHeight();
  float f3 = getWidth();
  float f4 = getHeight();
  Log.d("PIMapView", "d=" + f1 + "x" + f2 + "; v=" + f3 + "x" + f4);
  float f5 = Math.min(Math.min(f3 / f1, 3.0F), Math.min(f4 / f2, 3.0F));
  this.mScaleFactorMin = f5;
  this.mScaleFactorMax = (f5 * (float)Math.pow(1.5D, 4.0D));
  MapCenterAndZoom localMapCenterAndZoom = this.mNextMapCenterAndZoom;
  this.mNextMapCenterAndZoom = null;
  boolean bool1 = false;
  if (localMapCenterAndZoom != null)
  {
    boolean bool2 = this.mCurrentZone.isOverview();
    bool1 = false;
    if (!bool2) {
      bool1 = setMapCenterAndZoomIfVisible(localMapCenterAndZoom);
    }
  }
  if (!bool1)
  {
    setScale(f5);
    this.mController.setCenter((int)(f1 / 2.0F), (int)(f2 / 2.0F));
  }
  notifyZoomChange(getScale());
  onMapReady();
  return true;
}

private void scheduleDismissOnScreenControls()
{
  this.mHandler.removeCallbacks(this.mDismissOnScreenControlRunner);
  this.mHandler.postDelayed(this.mDismissOnScreenControlRunner, ViewConfiguration.getZoomControlsTimeout());
}

private boolean setMapCenterAndZoomIfVisible(MapCenterAndZoom paramMapCenterAndZoom)
{
  if (!PIMapLocation.isLocationInZone(this.mCurrentZone, paramMapCenterAndZoom.centerLatitude, paramMapCenterAndZoom.centerLongitude)) {
    return false;
  }
  float f = getResources().getDisplayMetrics().xdpi;
  setScale(pinScale((float)(paramMapCenterAndZoom.feetPerInchX / (this.mCurrentZone.getFeetPerPixelX() * f))));
  PIMapLocation localPIMapLocation = PIMapLocation.getXYOfLatLon(this.mCurrentZone, paramMapCenterAndZoom.centerLatitude, paramMapCenterAndZoom.centerLongitude);
  this.mController.setCenter(localPIMapLocation);
  return true;
}

private void setupZoomButtonController(View paramView)
{
  this.mZoomButtonsController = new ZoomButtonsController(paramView);
  this.mZoomButtonsController.setAutoDismissed(false);
  this.mZoomButtonsController.setZoomSpeed(100L);
  this.mZoomButtonsController.setOnZoomListener(new ZoomButtonsController.OnZoomListener()
  {
    public void onVisibilityChanged(boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        PIMapView.this.updateZoomButtonsEnabled();
      }
    }

    public void onZoom(boolean paramAnonymousBoolean)
    {
      if (paramAnonymousBoolean) {
        PIMapView.this.getController().zoomIn();
      } else {
          PIMapView.this.getController().zoomOut();
      }
      PIMapView.this.mZoomButtonsController.setVisible(true);
      PIMapView.this.updateZoomButtonsEnabled();
      return;
    }
  });
  setZoomControlsAlignment(85);
}

private void stepZoomAnimation()
{
  if (this.mZoomAnim.getTransformation(AnimationUtils.currentAnimationTimeMillis(), this.mZoomTransformation))
  {
    this.mZoomTransformation.getMatrix().getValues(this.mZoomMatrixValues);
    setScale(this.mZoomMatrixValues[0]);
    panTo((int)this.mZoomMatrixValues[2], (int)this.mZoomMatrixValues[5]);
    invalidate();
    return;
  }
  this.mZoomAnim = null;
}

private void updateMatrix()
{
  if (this.mZoneBitmap != null)
  {
    float f = getScale();
    int i = this.mZoneBitmap.getWidth();
    int j = this.mZoneBitmap.getHeight();
    int k = getWidth();
    int m = getHeight();
    PointF localPointF1 = viewPointToMapPoint(0.0F, 0.0F, this.mTempPoint1);
    PointF localPointF2 = viewPointToMapPoint(k, m, this.mTempPoint2);
    localPointF1.x = clamp(localPointF1.x, 0.0F, i);
    localPointF1.y = clamp(localPointF1.y, 0.0F, j);
    localPointF2.x = clamp(localPointF2.x, 0.0F, i);
    localPointF2.y = clamp(localPointF2.y, 0.0F, j);
    this.mSrcRect.set((int)localPointF1.x, (int)localPointF1.y, (int)localPointF2.x, (int)localPointF2.y);
    int n = Math.max(0, -this.mPanX);
    int i1 = Math.max(0, -this.mPanY);
    this.mDstRect.set(n, i1, n + f * this.mSrcRect.width(), i1 + f * this.mSrcRect.height());
    invalidate();
  }
}

private void updateZoomButtonsEnabled()
{
  if (this.mUseBuiltInZoom)
  {
    this.mZoomButtonsController.setZoomInEnabled(getController().canZoomIn());
    this.mZoomButtonsController.setZoomOutEnabled(getController().canZoomOut());
  }
}

private void zoomToZoomPoint()
{
  if (this.mZoomPoint != null)
  {
    PointF localPointF = computePointforScale(this.mZoomPoint.x, this.mZoomPoint.y);
    int i = (int)localPointF.x;
    int j = (int)localPointF.y;
    getController().centerToXY(i, j);
  }
}

boolean canZoom(float paramFloat)
{
  return Float.compare(pinScale(paramFloat), getCurrentOrTargetScale()) != 0;
}

void cancelZoom()
{
  if (isZooming())
  {
    this.mZoomAnim = null;
    notifyZoomChange(getScale());
  }
}

boolean checkHotlinkHit(int i, int j)
{
    if(!mHotlinkZones || !isOverview())
        return false;
    Iterator iterator = mOverviewPolygons.values().iterator();

    for(;iterator.hasNext();) {
	      PIMapPolygonZone pimappolygonzone = (PIMapPolygonZone)iterator.next();
	      if(pimappolygonzone.getPolygon().hitTest(i, j))
	      {
	          goToSpecialArea(pimappolygonzone.getSpecialAreaId());
	          return true;
	      }
		}
    return false;
}


protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
{
  return paramLayoutParams instanceof LayoutParams;
}

void cleanUp()
{
  if (this.mPIMapVenue != null)
  {
    this.mHandler.removeCallbacks(this.mDismissOnScreenControlRunner);
    this.mPIMapVenue.close();
  }
}

PIMapLandmarkDataCursor.PIMapPlace closestPlaceToPoint(int paramInt1, int paramInt2)
{
  ArrayList localArrayList = new ArrayList();
  PIMapPlaceDataCursor localPIMapPlaceDataCursor;
  PIMapVenueZone localPIMapVenueZone;
  if (this.mPIMapVenue != null)
  {
    localPIMapPlaceDataCursor = this.mPIMapVenue.getMapPlaces(true);
    localPIMapVenueZone = this.mCurrentZone;
    if ((localPIMapPlaceDataCursor == null) || (localPIMapVenueZone == null)) {
  	  return null;
    }
    try
    {
      boolean bool;
      do
      {
        PIMapLandmarkDataCursor.PIMapPlace localPIMapPlace = localPIMapPlaceDataCursor.getPIMapPlace();
        if ((localPIMapPlace.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) && (!localPIMapVenueZone.isOverview())) {
          localArrayList.add(localPIMapPlace);
        }
        bool = localPIMapPlaceDataCursor.moveToNext();
      } while (bool);
      localPIMapPlaceDataCursor.close();
      return closestPlaceToPoint(paramInt1, paramInt2, localArrayList);
    } catch(Exception ex) {
  	  ex.printStackTrace();
    }
  }
  return null;
}

PIMapLandmarkDataCursor.PIMapPlace closestPlaceToPoint(int paramInt1, int paramInt2, List<PIMapLandmarkDataCursor.PIMapPlace> paramList)
{
  int i = 2147483647;
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  PIMapLandmarkDataCursor.PIMapPlace localObject = null;
  PIMapVenueZone localPIMapVenueZone;
  Iterator localIterator;
  if (localPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localObject = null;
    if (paramList != null)
    {
      localObject = null;
      if (localPIMapVenueZone != null) {
        localIterator = paramList.iterator();
        for (;localIterator.hasNext();)
        {
          PIMapLandmarkDataCursor.PIMapPlace localPIMapPlace = (PIMapLandmarkDataCursor.PIMapPlace)localIterator.next();
          if ((localPIMapPlace.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) && (!localPIMapVenueZone.isOverview()))
          {
            int j = localPIMapPlace.getLocationPixelX() - paramInt1;
            int k = localPIMapPlace.getLocationPixelY() - paramInt2;
            int m = j * j + k * k;
            if (m < i)
            {
              localObject = localPIMapPlace;
              i = m;
            }
          }
        }
      }
    }
  }
  return localObject;
}

PointF computePointforScale(float paramFloat1, float paramFloat2)
{
  return mapPointToViewPoint(paramFloat1, paramFloat2, new PointF());
}

PointF computePointfromScale(float paramFloat1, float paramFloat2)
{
  return viewPointToMapPoint(paramFloat1, paramFloat2, new PointF());
}

protected void dispatchDraw(Canvas paramCanvas)
{
  super.dispatchDraw(paramCanvas);
  PIMapVenueZone localPIMapVenueZone = this.mCurrentZone;
  Uri localUri1 = this.mCurrentImageUri;
  if (localPIMapVenueZone != null)
  {
    Uri localUri2 = Uri.parse(localPIMapVenueZone.getDataPath());
    if ((localUri1 == null) || (!localUri1.equals(localUri2)) || (this.mZoneBitmap == null)) {
      this.mTileLoader.loadTile(localUri2);
    }
  }
  int i = getChildCount();
  for (int j = 0;; j++)
  {
    if (j >= i) {
      return;
    }
    View localView = getChildAt(j);
    if (localView.getVisibility() != 8)
    {
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (localLayoutParams.piMapLocation != null)
      {
        int k = localView.getMeasuredWidth();
        int m = localView.getMeasuredHeight();
        int n = localLayoutParams.alignment;
        if (n != -1) {
          onScrollChild(localView, localLayoutParams, 0, 0, k, m, n);
        }
      }
    }
  }
}

boolean doZoom(float paramFloat)
{
  return doZoom(paramFloat, getWidth() / 2, getHeight() / 2);
}

boolean doZoom(float paramFloat, int paramInt1, int paramInt2)
{
  cancelZoom();
  if (!canZoom(paramFloat)) {
    return false;
  }
  float f1 = getScale();
  float f2 = pinScale(paramFloat);
  float f3 = f2 / f1;
  int i = pinPanX((int)(f3 * (paramInt1 + getPanX()) - getWidth() / 2.0F), f2);
  int j = pinPanY((int)(f3 * (paramInt2 + getPanY()) - getHeight() / 2.0F), f2);
  this.mZoomAnim = new AnimationSet(true);
  this.mZoomAnim.setInterpolator(new DecelerateInterpolator(2.0F));
  this.mZoomAnim.addAnimation(new ScaleAnimation(f1, f2, f1, f2));
  this.mZoomAnim.addAnimation(new TranslateAnimation(getPanX(), i, getPanY(), j));
  this.mZoomAnim.setDuration(450L);
  this.mZoomAnim.initialize(getWidth(), getHeight(), getWidth(), getHeight());
  this.mZoomAnim.start();
  invalidate();
  this.mZoomAnimTargetScale = f2;
  notifyZoomChange(f2);
  return true;
}

protected ViewGroup.LayoutParams generateDefaultLayoutParams()
{
  return new LayoutParams(-2, -2, 0, 0);
}

public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
{
  return new LayoutParams(getContext(), paramAttributeSet);
}

public List<PIMapOverlayItem> getAllPlacesForZone()
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapPlaceDataCursor localPIMapPlaceDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapPlaceDataCursor = this.mPIMapVenue.getAllMapPlaces();
    if ((localPIMapPlaceDataCursor != null) && (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        if ((localPIMapPlaceDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
  	          localArrayList.add(localPIMapPlaceDataCursor.getPIMapPlace());
  	        }
  	        bool = localPIMapPlaceDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapPlaceDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    	ex.printStackTrace();
  	    }

    }
  }
  return localArrayList;
}

public PIMapController getController()
{
  return this.mController;
}

public String getCurrentAreaName()
{
  PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(this.mSpecialAreaId));
  if (localPIMapPolygonZone != null) {
    return localPIMapPolygonZone.getName();
  }
  return null;
}

float getCurrentOrTargetScale()
{
  if (isZooming()) {
    return this.mZoomAnimTargetScale;
  }
  return getScale();
}

public PIMapVenueZone getCurrentZone()
{
  return this.mCurrentZone;
}

public int getCurrentZoneIndex()
{
  if ((this.mPIMapVenue != null) && (this.mCurrentZone != null)) {
    return this.mCurrentZone.getZoneIndex();
  }
  return -1;
}

public String getCurrentZoneName()
{
  if ((this.mPIMapVenue != null) && (this.mCurrentZone != null)) {
    return this.mCurrentZone.getName();
  }
  return null;
}

public List<PIMapOverlayItem> getHotlinksForOverview()
{
  ArrayList localArrayList = new ArrayList();
  Iterator localIterator =null;
  if (this.mPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone = this.mOverviewZone;
    if ((localPIMapVenueZone != null) && (localPIMapVenueZone.isOverview())) {
      localIterator = this.mOverviewPolygons.values().iterator();
    }
  }
  for (;localIterator.hasNext();)
  {
    PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)localIterator.next();
    localArrayList.add(new PIMapOverlayItem(new PIMapLocation(localPIMapPolygonZone.getPixelX(), localPIMapPolygonZone.getPixelY()), localPIMapPolygonZone.getName(), "", localPIMapPolygonZone.getZoneIndex(), localPIMapPolygonZone.getSpecialAreaId()));
  }
  return localArrayList;
}

public int getLatitudeSpan()
{
  PointF localPointF1 = viewPointToMapPoint(0.0F, 0.0F, new PointF());
  PIMapLocation localPIMapLocation = PIMapLocation.getLatLonOfXY(this.mCurrentZone, (int)localPointF1.x, (int)localPointF1.y);
  PointF localPointF2 = viewPointToMapPoint(getWidth(), getHeight(), new PointF());
  return (int)(1000000.0D * Math.abs(PIMapLocation.getLatLonOfXY(this.mCurrentZone, (int)localPointF2.x, (int)localPointF2.y).getLatitude() - localPIMapLocation.getLatitude()));
}

public int getLongitudeSpan()
{
  PointF localPointF1 = viewPointToMapPoint(0.0F, 0.0F, new PointF());
  PIMapLocation localPIMapLocation = PIMapLocation.getLatLonOfXY(this.mCurrentZone, (int)localPointF1.x, (int)localPointF1.y);
  PointF localPointF2 = viewPointToMapPoint(getWidth(), getHeight(), new PointF());
  return (int)(1000000.0D * Math.abs(PIMapLocation.getLatLonOfXY(this.mCurrentZone, (int)localPointF2.x, (int)localPointF2.y).getLongitude() - localPIMapLocation.getLongitude()));
}

public PIMapLocation getMapCenter()
{
  PointF localPointF = viewPointToMapPoint(getWidth() / 2, getHeight() / 2, new PointF());
  int i = Math.round(localPointF.x);
  int j = Math.round(localPointF.y);
  PIMapLocation localPIMapLocation1 = PIMapLocation.getLatLonOfXY(this.mCurrentZone, i, j);
  PIMapLocation localPIMapLocation2 = new PIMapLocation(localPIMapLocation1.getLatitude(), localPIMapLocation1.getLongitude(), i, j);
  localPIMapLocation2.translate(this);
  return localPIMapLocation2;
}

public List<PIMapOverlayItem> getMapEventsForZone()
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapEventDataCursor localPIMapEventDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapEventDataCursor = this.mPIMapVenue.getPlaceEvents();
    if ((localPIMapEventDataCursor != null) && (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        PIMapItemDataCursor.PIMapItem localPIMapItem = this.mPIMapVenue.getMapItemForPromotion(localPIMapEventDataCursor.getId());
  	        if ((localPIMapItem != null) && (!localPIMapItem.isPortaled()) && ((localPIMapItem.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())))
  	        {
  	          localPIMapItem.setPromoType(localPIMapEventDataCursor.getPromotionTypeId());
  	          localPIMapItem.setPromoId(localPIMapEventDataCursor.getId());
  	          localPIMapItem.setSubTitle(localPIMapItem.getTitle());
  	          localPIMapItem.setTitle(localPIMapEventDataCursor.getTitle());
  	          localArrayList.add(localPIMapItem);
  	        }
  	        bool = localPIMapEventDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapEventDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    	ex.printStackTrace();
  	    }

    }
  }
  return localArrayList;
}

public List<PIMapOverlayItem> getMapItemsForZone()
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapItemDataCursor localPIMapItemDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapItemDataCursor = this.mPIMapVenue.getMapItems();
    if ((localPIMapItemDataCursor != null) && (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        if ((localPIMapItemDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
  	          localArrayList.add(localPIMapItemDataCursor.getPIMapItem());
  	        }
  	        bool = localPIMapItemDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapItemDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    	ex.printStackTrace();
  	    }

    }
  }
  return localArrayList;
}

public List<PIMapLandmarkDataCursor.PIMapPlace> getMapPlaceForServiceTypeUUID(String... paramVarArgs)
{
  ArrayList<PIMapLandmarkDataCursor.PIMapPlace> localArrayList = new ArrayList<PIMapLandmarkDataCursor.PIMapPlace>();
  PIMapVenueZone localPIMapVenueZone;
  int i;
  int j;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    i = paramVarArgs.length;
    j = 0;
	    for (;j<i;)
	    {
	      String str = paramVarArgs[j];
	      PIMapServiceDataCursor localPIMapServiceDataCursor = this.mPIMapVenue.getMapPlaceForServiceTypeUUID(str);
	      if ((localPIMapServiceDataCursor != null) && (localPIMapVenueZone != null)) {
		      try
		      {
		        boolean bool;
		        do
		        {
		          if ((localPIMapServiceDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
		            localArrayList.add(localPIMapServiceDataCursor.getPIMapPlace());
		          }
		          bool = localPIMapServiceDataCursor.moveToNext();
		        } while (bool);
		        localPIMapServiceDataCursor.close();
		      }
		      catch(Exception ex)
		      {
		    	  ex.printStackTrace();
		      }
	      }
	      j++;
	    }
  }
  return localArrayList;
}

public List<PIMapOverlayItem> getMapPromotionsForZone()
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapDealDataCursor localPIMapDealDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapDealDataCursor = this.mPIMapVenue.getDeals();
    if ((localPIMapDealDataCursor != null) && (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        PIMapItemDataCursor.PIMapItem localPIMapItem = this.mPIMapVenue.getMapItemForPromotion(localPIMapDealDataCursor.getId());
  	        if ((localPIMapItem != null) && (!localPIMapItem.isPortaled()) && ((localPIMapItem.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())))
  	        {
  	          localPIMapItem.setPromoType(localPIMapDealDataCursor.getPromotionTypeId());
  	          localPIMapItem.setPromoId(localPIMapDealDataCursor.getId());
  	          localPIMapItem.setSubTitle(localPIMapItem.getTitle());
  	          localPIMapItem.setTitle(localPIMapDealDataCursor.getTitle());
  	          localArrayList.add(localPIMapItem);
  	        }
  	        bool = localPIMapDealDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapDealDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    }

    }
  }
  return localArrayList;
}

public List<PIMapOverlayItem> getMapWormholeForTypes(int... paramVarArgs)
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  int i;
  int j;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    i = paramVarArgs.length;
    j = 0;
    for (;j < i;)
    {
      int k = paramVarArgs[j];
      PIMapWormholeDataCursor localPIMapWormholeDataCursor = this.mPIMapVenue.getMapWormholeForType(k);
      if ((localPIMapWormholeDataCursor != null) && (localPIMapVenueZone != null)) {
	        try
	        {
	          boolean bool = true;
	          do
	          {
	            if ((localPIMapWormholeDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
	              localArrayList.add(localPIMapWormholeDataCursor.getPIMapWormhole());
	            }
	            bool = localPIMapWormholeDataCursor.moveToNext();
	          } while (bool);
	          localPIMapWormholeDataCursor.close();
	          j++;
	        }
	        catch(Exception ex)
	        {
	        	ex.printStackTrace();
	        }
      }
    }
  }
  return localArrayList;
}

public PIMapVenueZone getNextZone()
{
  if (this.mPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone1 = this.mCurrentZone;
    if (localPIMapVenueZone1 != null)
    {
      PIMapVenueZone localPIMapVenueZone2 = null;
      if (this.mSpecialAreaId > 0L)
      {
        PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(this.mSpecialAreaId));
        if ((localPIMapPolygonZone != null) && (localPIMapPolygonZone.moveNext()))
        {
          localPIMapVenueZone2 = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(localPIMapPolygonZone.getZoneIndex()));
          if (localPIMapVenueZone2.getZoneIndex() == localPIMapVenueZone1.getZoneIndex()) {
              return localPIMapVenueZone2;
          }
        }
      }
      else
      {
        do
        {
          int i = this.mZoneIndices.length;
          if (i <= 0) {
            break;
          }
          int j = this.mCurrentZoneOrderIndex;
          if (j + 1 < i) {
            j++;
          }
          localPIMapVenueZone2 = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(this.mZoneIndices[j]));
        } while (localPIMapVenueZone2.getZoneIndex() != localPIMapVenueZone1.getZoneIndex());
        return localPIMapVenueZone2;
      }
    }
  }
  return null;
}

public PIMapVenueZone getNextZone2()
{
    if(mPIMapVenue == null)
  	  return null;
    PIMapVenueZone pimapvenuezone = mCurrentZone;
    PIMapVenueZone pimapvenuezone1;
    if(pimapvenuezone == null)
  	  return null;

    if(mSpecialAreaId <= 0L) {
        int i = mZoneIndices.length;
        if(i <= 0)
            return null;
        int j = mCurrentZoneOrderIndex;
        if(j + 1 < i)
            j++;
        pimapvenuezone1 = (PIMapVenueZone)mVenueZones.get(Integer.valueOf(mZoneIndices[j]));
        if(pimapvenuezone1.getZoneIndex() != pimapvenuezone.getZoneIndex())
            return pimapvenuezone1;
        else
      	  return null;
    } else {
	      PIMapPolygonZone pimappolygonzone = (PIMapPolygonZone)mOverviewPolygons.get(Long.valueOf(mSpecialAreaId));
	      if(pimappolygonzone == null || !pimappolygonzone.moveNext())
	    	  return null;
	      pimapvenuezone1 = (PIMapVenueZone)mVenueZones.get(Integer.valueOf(pimappolygonzone.getZoneIndex()));
	      if(pimapvenuezone1.getZoneIndex() == pimapvenuezone.getZoneIndex())
	    	  return null;
	      return pimapvenuezone1;
    }
//    return null;
}


public OnLongClickMapListener getOnLongClickMapListener()
{
  return this.mOnLongClickMapListener;
}

public OnPlaceClickListener getOnPlaceClickListener()
{
  return this.mOnPlaceClickListener;
}

public List<PIMapOverlay> getOverlays()
{
  return this.mOverlays;
}

public PIMapVenueZone getOverviewZone()
{
  return this.mOverviewZone;
}

public int getPanX()
{
  return this.mPanX;
}

public int getPanY()
{
  return this.mPanY;
}

public List<PIMapOverlayItem> getPlace(String paramString)
{
  return getPlaces(new String[] { paramString });
}

public List<PIMapOverlayItem> getPlaces(String as[])
{
	  PIMapPlaceDataCursor pimapplacedatacursor;
    ArrayList arraylist = new ArrayList();
    try {
	      if(mPIMapVenue == null)
	          return arraylist;
	      boolean flag;
	      if(as.length == 1)
	          pimapplacedatacursor = mPIMapVenue.getMapPlaceForUUID(as[0]);
	      else
	          pimapplacedatacursor = mPIMapVenue.getMapPlacesForUUIDs(as);
	      if(pimapplacedatacursor == null)
	          return arraylist;
	      do
	      {
	          arraylist.add(pimapplacedatacursor.getPIMapPlace());
	          flag = pimapplacedatacursor.moveToNext();
	      } while(flag);
	      pimapplacedatacursor.close();
	      return arraylist;
	  } catch(Exception ex) {
		  ex.printStackTrace();
	  }
    return arraylist;
}

public List<PIMapOverlayItem> getPlacesForZone()
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapPlaceDataCursor localPIMapPlaceDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapPlaceDataCursor = this.mPIMapVenue.getMapPlaces();
    if ((localPIMapPlaceDataCursor != null) && (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        if ((localPIMapPlaceDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
  	          localArrayList.add(localPIMapPlaceDataCursor.getPIMapPlace());
  	        }
  	        bool = localPIMapPlaceDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapPlaceDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    	ex.printStackTrace();
  	    }
    }
  }
  return localArrayList;
}

public PIMapVenueZone getPreviousZone()
{
    if(mPIMapVenue == null)
  	  return null;

    PIMapVenueZone pimapvenuezone = mCurrentZone;
    if(pimapvenuezone == null)
  	  return null;

    PIMapVenueZone pimapvenuezone1;
    if(mSpecialAreaId <= 0L) {
        if(mZoneIndices.length <= 0)
            return null;
        int i = mCurrentZoneOrderIndex;
        if(i > 0)
            i--;
        pimapvenuezone1 = (PIMapVenueZone)mVenueZones.get(Integer.valueOf(mZoneIndices[i]));
        if(pimapvenuezone1.getZoneIndex() != pimapvenuezone.getZoneIndex())
            return pimapvenuezone1;
        else
            return null;

    } else {

	      PIMapPolygonZone pimappolygonzone = (PIMapPolygonZone)mOverviewPolygons.get(Long.valueOf(mSpecialAreaId));
	      if(pimappolygonzone == null || !pimappolygonzone.movePrevious())
	          return null;

	      pimapvenuezone1 = (PIMapVenueZone)mVenueZones.get(Integer.valueOf(pimappolygonzone.getZoneIndex()));
	      if(pimapvenuezone1.getZoneIndex() == pimapvenuezone.getZoneIndex())
	    	  return null;
	      else
	    	  return pimapvenuezone1;
	  }
}



public PIMapViewProjection getProjection()
{
  return this.mProjection;
}

public float getScale()
{
  return this.mScaleFactor;
}

float getScaleMin()
{
  return this.mScaleFactorMin;
}

MyScroller getScroller()
{
  return this.mScroller;
}

public List<PIMapOverlayItem> getServicesForZone()
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapServiceDataCursor localPIMapServiceDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapServiceDataCursor = this.mPIMapVenue.getMapServices();
    if ((localPIMapServiceDataCursor != null) && (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        if ((localPIMapServiceDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
  	          localArrayList.add(localPIMapServiceDataCursor.getPIMapPlace());
  	        }
  	        bool = localPIMapServiceDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapServiceDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    	ex.printStackTrace();
  	    }
    }
  }
  return localArrayList;
}

public List<PIMapOverlayItem> getServicesForZone(String paramString)
{
  ArrayList localArrayList = new ArrayList();
  PIMapVenueZone localPIMapVenueZone;
  PIMapServiceDataCursor localPIMapServiceDataCursor;
  if (this.mPIMapVenue != null)
  {
    localPIMapVenueZone = this.mCurrentZone;
    localPIMapServiceDataCursor = this.mPIMapVenue.getMapService(paramString);
    if ((localPIMapServiceDataCursor == null) || (localPIMapVenueZone == null)) {
  	    try
  	    {
  	      boolean bool;
  	      do
  	      {
  	        if ((localPIMapServiceDataCursor.getZoneIndex() == localPIMapVenueZone.getZoneIndex()) || (localPIMapVenueZone.isOverview())) {
  	          localArrayList.add(localPIMapServiceDataCursor.getPIMapPlace());
  	        }
  	        bool = localPIMapServiceDataCursor.moveToNext();
  	      } while (bool);
  	      localPIMapServiceDataCursor.close();
  	      return localArrayList;
  	    }
  	    catch(Exception ex)
  	    {
  	    	ex.printStackTrace();
  	    }

    }
  }
  return localArrayList;
}

ZoomButtonsController getZoomButtonsController()
{
  return this.mZoomButtonsController;
}

boolean goToOverview()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool = false;
  if (localPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone = this.mOverviewZone;
    bool = false;
    if (localPIMapVenueZone != null)
    {
      this.mCurrentZone = localPIMapVenueZone;
      this.mCurrentZoneOrderIndex = 0;
      this.mSpecialAreaId = -1L;
      this.mZoomPoint = null;
      bool = true;
      postInvalidate();
    }
  }
  return bool;
}

boolean goToSpecialArea(long paramLong)
{
  PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(paramLong));
  if ((localPIMapPolygonZone != null) && (localPIMapPolygonZone.moveFirst()))
  {
    int i = localPIMapPolygonZone.getPlaceCount();
    int j = localPIMapPolygonZone.getZoneIndex();
    for (;;)
    {
      if (!localPIMapPolygonZone.hasNextZone())
      {
        localPIMapPolygonZone.moveToZone(j);
        this.mSpecialAreaId = paramLong;
        this.mCurrentZone = ((PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(localPIMapPolygonZone.getZoneIndex())));
        this.mCurrentZoneOrderIndex = getZoneOrderIndex();
        this.mZoomPoint = new Point(localPIMapPolygonZone.getPixelX(), localPIMapPolygonZone.getPixelY());
        postInvalidate();
        return true;
      }
      localPIMapPolygonZone.moveNext();
      if (localPIMapPolygonZone.getPlaceCount() > i)
      {
        i = localPIMapPolygonZone.getPlaceCount();
        j = localPIMapPolygonZone.getZoneIndex();
      }
    }
  }
  return false;
}

boolean goToZone(int paramInt)
{
  return goToZone(paramInt, null);
}

boolean goToZone(int paramInt, Point paramPoint)
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool = false;
  if (localPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(paramInt));
    bool = false;
    if (localPIMapVenueZone != null)
    {
      if (localPIMapVenueZone.isOverview()) {
        return goToOverview();
      }
      this.mSpecialAreaId = -1L;
      this.mCurrentZone = localPIMapVenueZone;
      this.mCurrentZoneOrderIndex = getZoneOrderIndex();
      this.mZoomPoint = paramPoint;
      bool = true;
      postInvalidate();
    }
  }
  return bool;
}

boolean hasNextZone()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool = false;
  if (localPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone = this.mCurrentZone;
    bool = false;
    if (localPIMapVenueZone != null)
    {
      if (this.mSpecialAreaId <= 0L) {
          return this.mCurrentZoneOrderIndex < -1 + this.mZoneIndices.length;
      }
      PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(this.mSpecialAreaId));
      if ((localPIMapPolygonZone == null) || (!localPIMapPolygonZone.hasNextZone())) {
          return false;
      }
      bool = true;
    }
  }
  return bool;
}

public boolean hasOverview()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool = false;
  if (localPIMapVenue != null) {
    bool = this.mPIMapVenue.hasOverview();
  }
  return bool;
}

boolean hasPreviousZone()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool = false;
  if (localPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone = this.mCurrentZone;
    bool = false;
    if (localPIMapVenueZone != null)
    {
      if (this.mSpecialAreaId <= 0L) {
          return (this.mCurrentZoneOrderIndex > 0) && (this.mZoneIndices.length > 0);
      }
      PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(this.mSpecialAreaId));
      if ((localPIMapPolygonZone == null) || (!localPIMapPolygonZone.hasPreviousZone())) {
          return false;
      }
      bool = true;
    }
  }
  return bool;
}

public void hideOnScreenControls()
{
  if (this.mZoomButtonsController != null) {
    this.mZoomButtonsController.setVisible(false);
  }
}

public void invalidate()
{
  super.invalidate();
  if (this.mOverlayView != null) {
    this.mOverlayView.invalidate();
  }
}

public boolean isMapReady()
{
  if ((getWidth() == 0) || (getHeight() == 0)) {}
  while ((Float.compare(getScale(), 0.0F) == 0) || (this.mZoneBitmap == null)) {
    return false;
  }
  return true;
}

public boolean isOverview()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool1 = false;
  if (localPIMapVenue != null)
  {
    PIMapVenueZone localPIMapVenueZone = this.mCurrentZone;
    bool1 = false;
    if (localPIMapVenueZone != null)
    {
      boolean bool2 = localPIMapVenueZone.isOverview();
      bool1 = false;
      if (bool2) {
        bool1 = true;
      }
    }
  }
  return bool1;
}

boolean isZooming()
{
  return this.mZoomAnim != null;
}

PointF mapPointToViewPoint(float paramFloat1, float paramFloat2, PointF paramPointF)
{
  paramPointF.x = (paramFloat1 * this.mScaleFactor - this.mPanX);
  paramPointF.y = (paramFloat2 * this.mScaleFactor - this.mPanY);
  return paramPointF;
}

boolean nextZone()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool1 = false;
  PIMapVenueZone localPIMapVenueZone1;
  if (localPIMapVenue != null)
  {
    localPIMapVenueZone1 = this.mCurrentZone;
    bool1 = false;
    if (localPIMapVenueZone1 != null)
    {
      if (this.mSpecialAreaId <= 0L) {
          int i = this.mZoneIndices.length;
          bool1 = false;
          if (i > 0)
          {
            if (1 + this.mCurrentZoneOrderIndex < i) {
              this.mCurrentZoneOrderIndex = (1 + this.mCurrentZoneOrderIndex);
            }
            PIMapVenueZone localPIMapVenueZone2 = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(this.mZoneIndices[this.mCurrentZoneOrderIndex]));
            int j = localPIMapVenueZone2.getZoneIndex();
            int k = localPIMapVenueZone1.getZoneIndex();
            bool1 = false;
            if (j != k)
            {
              this.mCurrentZone = localPIMapVenueZone2;
              this.mZoomPoint = null;
              bool1 = true;
            }
          }
      }
      PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(this.mSpecialAreaId));
      bool1 = false;
      if (localPIMapPolygonZone != null)
      {
        boolean bool2 = localPIMapPolygonZone.moveNext();
        bool1 = false;
        if (bool2)
        {
          PIMapVenueZone localPIMapVenueZone3 = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(localPIMapPolygonZone.getZoneIndex()));
          if (localPIMapVenueZone3.getZoneIndex() != localPIMapVenueZone1.getZoneIndex()) {
            this.mCurrentZone = localPIMapVenueZone3;
          }
          this.mZoomPoint = new Point(localPIMapPolygonZone.getPixelX(), localPIMapPolygonZone.getPixelY());
          bool1 = true;
        }
      }
    }
  }
    postInvalidate();
    return bool1;
}

void notifyZoomChange(float paramFloat)
{
  if (this.mOnZoomChangedListener != null) {
    this.mOnZoomChangedListener.onZoomChanged(paramFloat);
  }
  updateZoomButtonsEnabled();
}

protected void onDetachedFromWindow()
{
  super.onDetachedFromWindow();
  if (this.mUseBuiltInZoom) {
    this.mZoomButtonsController.setVisible(false);
  }
}

protected void onDraw(Canvas paramCanvas)
{
  paramCanvas.drawColor(-1);
  this.mInputHandler.handleScrollOffset();
  if (this.mZoomAnim != null) {
    stepZoomAnimation();
  }

  //paramCanvas.rotate(-azimut*360/(2*3.14159f), this.getWidth()/2, this.getHeight()/2);

  if (this.mZoneBitmap != null) {
    paramCanvas.drawBitmap(this.mZoneBitmap, this.mSrcRect, this.mDstRect, this.mPaint);
  }

  super.onDraw(paramCanvas);
}

public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
{
  if (this.mInputHandler.onKeyDown(paramInt, paramKeyEvent)) {
    return true;
  }
  return super.onKeyDown(paramInt, paramKeyEvent);
}

public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
{
  if (this.mInputHandler.onKeyUp(paramInt, paramKeyEvent)) {
    return true;
  }
  return super.onKeyUp(paramInt, paramKeyEvent);
}

protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
{
  doLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  if ((this.mDirtyBounds) && (recomputeInitialScale()))
  {
    post(this.mOnZoneChangeRunnable);
    this.mDirtyBounds = false;
  }
}

protected void onMeasure(int paramInt1, int paramInt2)
{
  int i = getChildCount();
  int j = 0;
  int k = 0;
  measureChildren(paramInt1, paramInt2);
  for (int m = 0;; m++)
  {
    if (m >= i)
    {
      int i2 = Math.max(j, getSuggestedMinimumHeight());
      setMeasuredDimension(resolveSize(Math.max(k, getSuggestedMinimumWidth()), paramInt1), resolveSize(i2, paramInt2));
      return;
    }
    View localView = getChildAt(m);
    if (localView.getVisibility() != 8)
    {
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      int n = localLayoutParams.x + localView.getMeasuredWidth();
      int i1 = localLayoutParams.y + localView.getMeasuredHeight();
      k = Math.max(k, n);
      j = Math.max(j, i1);
    }
  }
}

protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
{
  super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
  if ((paramInt3 != 0) || (paramInt4 != 0))
  {
    this.mNextMapCenterAndZoom = getCurrentMapCenterAndZoom();
    recomputeInitialScale();
  }
}

public boolean onTouchEvent(MotionEvent paramMotionEvent)
{
  if (this.mZoneBitmap != null) {
    return this.mInputHandler.onTouchEvent(paramMotionEvent);
  }
  return false;
}

public boolean onTrackballEvent(MotionEvent paramMotionEvent)
{
  if (this.mInputHandler.onTrackballEvent(paramMotionEvent)) {
    return true;
  }
  return super.onTrackballEvent(paramMotionEvent);
}

protected void onVisibilityChanged(View paramView, int paramInt)
{
  super.onVisibilityChanged(paramView, paramInt);
  if ((this.mUseBuiltInZoom) && (paramInt != 0)) {
    this.mZoomButtonsController.setVisible(false);
  }
}

void panBy(int paramInt1, int paramInt2)
{
  panTo(pinPanX(paramInt1 + getPanX()), pinPanY(paramInt2 + getPanY()));
}

void panTo(int paramInt1, int paramInt2)
{
  if ((this.mPanX != paramInt1) || (this.mPanY != paramInt2))
  {
    this.mPanX = paramInt1;
    this.mPanY = paramInt2;
    updateMatrix();
  }
}

int pinPanX(int paramInt)
{
  return pinPanX(paramInt, this.mScaleFactor);
}

int pinPanX(int paramInt, float paramFloat)
{
  return pinPan(paramInt, this.mZoneBitmap.getWidth(), this.mMapPaddingLeft, this.mMapPaddingRight, getWidth(), paramFloat);
}

int pinPanY(int paramInt)
{
  return pinPanY(paramInt, this.mScaleFactor);
}

int pinPanY(int paramInt, float paramFloat)
{
  return pinPan(paramInt, this.mZoneBitmap.getHeight(), this.mMapPaddingTop, this.mMapPaddingBottom, getHeight(), paramFloat);
}

float pinScale(float paramFloat)
{
  return clamp(paramFloat, this.mScaleFactorMin, this.mScaleFactorMax);
}

boolean pinchZoom(float paramFloat1, float paramFloat2, float paramFloat3)
{
  if (!canZoom(paramFloat1)) {
    return false;
  }
  float f1 = 1.0F / getScale();
  float f2 = pinScale(paramFloat1);
  float f3 = f2 * f1;
  setScale(f2);
  panTo(pinPanX((int)(f3 * getPanX() + paramFloat2 * (f3 - 1.0F))), pinPanY((int)(f3 * getPanY() + paramFloat3 * (f3 - 1.0F))));
  return true;
}

boolean previousZone()
{
  PIMapVenue localPIMapVenue = this.mPIMapVenue;
  boolean bool1 = false;
  PIMapVenueZone localPIMapVenueZone1;
  if (localPIMapVenue != null)
  {
    localPIMapVenueZone1 = this.mCurrentZone;
    bool1 = false;
    if (localPIMapVenueZone1 != null)
    {
      if (this.mSpecialAreaId <= 0L) {
          int i = this.mZoneIndices.length;
          bool1 = false;
          if (i > 0)
          {
            if (this.mCurrentZoneOrderIndex > 0) {
              this.mCurrentZoneOrderIndex = (-1 + this.mCurrentZoneOrderIndex);
            }
            PIMapVenueZone localPIMapVenueZone2 = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(this.mZoneIndices[this.mCurrentZoneOrderIndex]));
            int j = localPIMapVenueZone2.getZoneIndex();
            int k = localPIMapVenueZone1.getZoneIndex();
            bool1 = false;
            if (j != k)
            {
              this.mCurrentZone = localPIMapVenueZone2;
              this.mZoomPoint = null;
              bool1 = true;
            }
          }
      }
      PIMapPolygonZone localPIMapPolygonZone = (PIMapPolygonZone)this.mOverviewPolygons.get(Long.valueOf(this.mSpecialAreaId));
      bool1 = false;
      if (localPIMapPolygonZone != null)
      {
        boolean bool2 = localPIMapPolygonZone.movePrevious();
        bool1 = false;
        if (bool2)
        {
          PIMapVenueZone localPIMapVenueZone3 = (PIMapVenueZone)this.mVenueZones.get(Integer.valueOf(localPIMapPolygonZone.getZoneIndex()));
          if (localPIMapVenueZone3.getZoneIndex() != localPIMapVenueZone1.getZoneIndex()) {
            this.mCurrentZone = localPIMapVenueZone3;
          }
          this.mZoomPoint = new Point(localPIMapPolygonZone.getPixelX(), localPIMapPolygonZone.getPixelY());
          bool1 = true;
        }
      }
    }
  }
    postInvalidate();
    return bool1;
}

public void runOnMapReady(Runnable paramRunnable)
{
  if (!isMapReady())
  {
    this.mOnMapReadyRunnables.add(paramRunnable);
    return;
  }
  paramRunnable.run();
}

public void setHotlinkMode(boolean paramBoolean)
{
  this.mHotlinkZones = paramBoolean;
}

public void setLogoGravity(int paramInt)
{
  if (this.mLogoView != null) {
    ((LayoutParams)this.mLogoView.getLayoutParams()).gravity = paramInt;
  }
}

public void setLogoMargins(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
{
  if (this.mLogoView != null)
  {
    LayoutParams localLayoutParams = (LayoutParams)this.mLogoView.getLayoutParams();
    localLayoutParams.leftMargin = paramInt1;
    localLayoutParams.rightMargin = paramInt3;
    localLayoutParams.topMargin = paramInt2;
    localLayoutParams.bottomMargin = paramInt4;
  }
}

public void setMapPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
{
  this.mMapPaddingLeft = paramInt1;
  this.mMapPaddingTop = paramInt2;
  this.mMapPaddingRight = paramInt3;
  this.mMapPaddingBottom = paramInt4;
}

public void setOnLongClickMapListener(OnLongClickMapListener paramOnLongClickMapListener)
{
  this.mOnLongClickMapListener = paramOnLongClickMapListener;
}

public void setOnPlaceClickListener(OnPlaceClickListener paramOnPlaceClickListener)
{
  this.mOnPlaceClickListener = paramOnPlaceClickListener;
}

public void setOnZoneChangeListener(OnZoneChangeListener paramOnZoneChangeListener)
{
  this.mOnZoneChangeListener = paramOnZoneChangeListener;
}

public void setOnZoomChangedListener(OnZoomChangedListener paramOnZoomChangedListener)
{
  this.mOnZoomChangedListener = paramOnZoomChangedListener;
}

void setScale(float paramFloat)
{
  if (this.mScaleFactor != paramFloat)
  {
    this.mScaleFactor = paramFloat;
    updateMatrix();
  }
}

public void setScrollPadding(int paramInt) {}

public void setUseBuiltInZoom(boolean flag)
{
    mUseBuiltInZoom = flag;
    if(mUseBuiltInZoom)
    {
        setupZoomButtonController(this);
    } else
    {
        mHandler.removeCallbacks(mDismissOnScreenControlRunner);
        hideOnScreenControls();
        if(mZoomButtonsController != null)
        {
            mZoomButtonsController.getContainer().removeView(this);
            mZoomButtonsController = null;
            return;
        }
    }
}

public void setVenue(PIMapVenue pimapvenue)
{
    if(pimapvenue == null)
  	  return;

    PIMapZoneDataCursor pimapzonedatacursor;
    mPIMapVenue = pimapvenue;
    mOnMapReadyRunnables.clear();
    mOverlays.clear();
    mVenueZones.clear();
    mOverviewPolygons.clear();
    mCurrentZone = null;
    mZoomPoint = null;
    mOverviewZone = null;
    mCurrentImageUri = null;
    mZoneIndices = null;
    mCurrentZoneOrderIndex = 0;
    try {
	      pimapzonedatacursor = pimapvenue.getVenueZones();
	      if(pimapzonedatacursor != null) {

	      mZoneIndices = new int[pimapzonedatacursor.getCount()];
	      int j = 0;
	      boolean flag = pimapzonedatacursor.moveToFirst();
	      while(flag) {
	      PIMapVenueZone pimapvenuezone2;
	      pimapvenuezone2 = pimapzonedatacursor.getPIMapVenueZone(mLocalContext);

	      //add by xinghai
	      if(pimapvenuezone2== null)
	    	  break;

	      mVenueZones.put(Integer.valueOf(pimapvenuezone2.getZoneIndex()), pimapvenuezone2);
	      mZoneIndices[j] = pimapvenuezone2.getZoneIndex();
	      if(!pimapvenuezone2.isOverview()) {
	    	      if(mCurrentZone != null)
	    	    	  return;
	    	      else {
	    	    	  mCurrentZone = pimapvenuezone2;
	    	      }

	      } else {
		      mOverviewZone = pimapvenuezone2;
		      mCurrentZone = mOverviewZone;
		      mHasOverview = true;
	      }
	      j++;
	      flag = pimapzonedatacursor.moveToNext();
	      }
	      pimapzonedatacursor.close();

	  } else {
	      Iterator iterator = getSpecialAreas(pimapvenue, mVenueZones).iterator();
	      for(;iterator.hasNext();) {
  	      PIMapPolygonZone pimappolygonzone = (PIMapPolygonZone)iterator.next();
  	      PIMapVenueZone pimapvenuezone = (PIMapVenueZone)mVenueZones.get(Integer.valueOf(pimappolygonzone.getHostZoneIndex()));
  	      if(pimapvenuezone == mOverviewZone)
  	      {
  	          mOverviewPolygons.put(Long.valueOf(pimappolygonzone.getSpecialAreaId()), pimappolygonzone);
  	      } else
  	      {
  	          if(pimapvenuezone.getPolygon() != null)
  	              Log.w("PIMapView", (new StringBuilder("Overriding other zone polygon on zoneIndex=")).append(pimapvenuezone.getZoneIndex()).toString());
  	          pimapvenuezone.setPolygon(pimappolygonzone.getPolygon());
  	      }
	      }
	      Iterator iterator1;
	      if(mOverviewZone != null)
	          mOverviewZone.setHotlinks(new HashMap(mOverviewPolygons));

	      iterator1 = mOverviewPolygons.values().iterator();
	      for(;iterator1.hasNext();){
	          PIMapPolygonZone pimappolygonzone1 = (PIMapPolygonZone)iterator1.next();
	          int i = pimappolygonzone1.getZoneIndex();
	          PIMapVenueZone pimapvenuezone1 = (PIMapVenueZone)mVenueZones.get(Integer.valueOf(i));
	          if(pimapvenuezone1 != null && pimapvenuezone1.getPolygon() == null)
	              pimapvenuezone1.setPolygon(pimappolygonzone1.getPolygon().translate(pimapvenuezone1));
	      }
	      postInvalidate();
	  }
	  } catch(Exception exception) {
	      exception.printStackTrace();
	  }
    return;

}


public void setZoomControlsAlignment(int paramInt)
{
  if (!this.mUseBuiltInZoom) {
    throw new IllegalStateException("Zoom controls must be enabled first");
  }
  ViewGroup.LayoutParams localLayoutParams = this.mZoomButtonsController.getZoomControls().getLayoutParams();
  if ((localLayoutParams instanceof FrameLayout.LayoutParams))
  {
    ((FrameLayout.LayoutParams)localLayoutParams).gravity = paramInt;
    return;
  }
  if ((localLayoutParams instanceof LinearLayout.LayoutParams))
  {
    ((LinearLayout.LayoutParams)localLayoutParams).gravity = paramInt;
    return;
  }
  Log.w("PIMapView", "Zoom button layout params are of unexpected type " + localLayoutParams.getClass().getName() + ", ignoring gravity change...");
}

public void setZoomControlsPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
{
  if (!this.mUseBuiltInZoom) {
    throw new IllegalStateException("Zoom controls must be enabled first");
  }
  this.mZoomButtonsController.getContainer().setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
}

public void showOnScreenControls()
{
  if (this.mUseBuiltInZoom)
  {
    if (!this.mZoomButtonsController.isVisible())
    {
      updateZoomButtonsEnabled();
      this.mZoomButtonsController.setVisible(true);
    }
    scheduleDismissOnScreenControls();
  }
}

void toggleZoom(int paramInt1, int paramInt2)
{
  if (Float.compare(this.mScaleFactor, this.mScaleFactorMin) > 0)
  {
    doZoom(this.mScaleFactorMin, paramInt1, paramInt2);
    return;
  }
  doZoom(this.mScaleFactorMin + (this.mScaleFactorMax - this.mScaleFactorMin) / 2.0F, paramInt1, paramInt2);
}

public void useBuiltInZoomIfNoMultiTouch()
{
  if (this.mInputHandler.isMultitouchSupported()) {}
  for (boolean bool = false;; bool = true)
  {
    setUseBuiltInZoom(bool);
    return;
  }
}

PointF viewPointToMapPoint(float paramFloat1, float paramFloat2, PointF paramPointF)
{
  paramPointF.x = ((paramFloat1 + this.mPanX) / this.mScaleFactor);
  paramPointF.y = ((paramFloat2 + this.mPanY) / this.mScaleFactor);
  return paramPointF;
}

private class AsyncTileLoader
  extends PIMapAsyncTileHandler
{
  private static final int MSG_LOAD_TILE = -1;
  PIMapView mPIMapView;

  private AsyncTileLoader(PIMapView param1) {
  	mPIMapView = param1;
  }

  public void loadTile(Uri paramUri)
  {
    startDecode(-1, paramUri);
  }

  public void onDecodeComplete(int paramInt, Uri paramUri, Bitmap paramBitmap)
  {
  	mPIMapView.mCurrentImageUri = paramUri;
  	mPIMapView.mDirtyBounds = true;
  	mPIMapView.mZoneBitmap = paramBitmap;
  	mPIMapView.requestLayout();
  	mPIMapView.invalidate();
  }

  protected void onOOM()
  {
  	mPIMapView.mDirtyBounds = true;
  	mPIMapView.mZoneBitmap = null;
  }
}

public static class LayoutParams
  extends ViewGroup.LayoutParams
{
  public static final int BOTTOM = 80;
  public static final int BOTTOM_CENTER = 81;
  public static final int CENTER = 17;
  public static final int CENTER_HORIZONTAL = 1;
  public static final int CENTER_VERTICAL = 16;
  public static final int LEFT = 3;
  public static final int RIGHT = 5;
  public static final int TOP = 48;
  public static final int TOP_LEFT = 51;
  int alignment = -1;
  public int bottomMargin = 0;
  int gravity = -1;
  public int leftMargin = 0;
  int offsetX;
  int offsetY;
  PIMapLocation piMapLocation;
  public int rightMargin = 0;
  public int topMargin = 0;
  int x;
  int y;

  public LayoutParams(int paramInt1, int paramInt2)
  {
    super(paramInt1,paramInt2);
    this.x = 0;
    this.y = 0;
    this.offsetX = 0;
    this.offsetY = 0;
    this.gravity = 3;
  }

  public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramInt1,paramInt2);
    this.x = 0;
    this.y = 0;
    this.offsetX = 0;
    this.offsetY = 0;
    this.gravity = paramInt3;
  }

  public LayoutParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super(paramInt1,paramInt2);
    this.x = paramInt3;
    this.y = paramInt4;
    this.offsetX = 0;
    this.offsetY = 0;
    this.alignment = 17;
  }

  public LayoutParams(int paramInt1, int paramInt2, PIMapLocation paramPIMapLocation, int paramInt3)
  {
    super(paramInt1,paramInt2);
    this.piMapLocation = paramPIMapLocation;
    this.x = paramPIMapLocation.getTranslatedPixelX();
    this.y = paramPIMapLocation.getTranslatedPixelY();
    this.offsetX = 0;
    this.offsetY = 0;
    this.alignment = paramInt3;
  }

  public LayoutParams(int paramInt1, int paramInt2, PIMapLocation paramPIMapLocation, int paramInt3, int paramInt4, int paramInt5)
  {
    super(paramInt1,paramInt2);
    this.piMapLocation = paramPIMapLocation;
    this.x = paramPIMapLocation.getTranslatedPixelX();
    this.y = paramPIMapLocation.getTranslatedPixelY();
    this.offsetX = paramInt3;
    this.offsetY = paramInt4;
    this.alignment = paramInt5;
  }

  public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext,paramAttributeSet);
    this.x = 0;
    this.y = 0;
    this.offsetX = 0;
    this.offsetY = 0;
    this.gravity = 3;
  }

  public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    super(paramLayoutParams);
    this.x = 0;
    this.y = 0;
    this.offsetX = 0;
    this.offsetY = 0;
    this.gravity = 3;
  }

  void translate(PIMapView paramPIMapView)
  {
    if ((paramPIMapView != null) && (this.piMapLocation != null))
    {
      this.x = this.piMapLocation.getTranslatedPixelX();
      this.y = this.piMapLocation.getTranslatedPixelY();
    }
  }
}

private class LogoView
  extends ImageView
{
  private static final String NEW_PI_LOGO_RES_NAME = "pi_logo";
  private static final String PI_LOGO = "pointinside.png";

  private LogoView(Context paramContext)
  {
    super(paramContext);
    loadLogo(paramContext);
  }

  private void loadLogo(Context context)
  {
      Resources resources;
      DisplayMetrics displaymetrics;
      int i;
      int j;
      java.io.InputStream inputstream;
      android.graphics.BitmapFactory.Options options;
      try
      {
          resources = context.getResources();
          displaymetrics = resources.getDisplayMetrics();
          i = resources.getIdentifier("pi_logo", "drawable", context.getPackageName());
          if(i == 0) {
              inputstream = context.getAssets().open("pointinside.png");
              options = new android.graphics.BitmapFactory.Options();
              options.inDensity = 240;
              setImageBitmap(BitmapFactory.decodeStream(inputstream, null, options));
              if(true)
                  setScaleType(android.widget.ImageView.ScaleType.FIT_START);
              else
              	return;
          }
          setImageDrawable(resources.getDrawable(i));
          j = (int)TypedValue.applyDimension(1, 4F, displaymetrics);
          setPadding(0, 0, j, j);
      }
      catch(Exception exception)
      {
          Log.e("PIMapView", "Unable to load Point Inside logo!", exception);
          return;
      }
      return;
  }

}

private static class MapCenterAndZoom
{
  public double centerLatitude;
  public double centerLongitude;
  public float feetPerInchX;
}

public static class OnLongClickMapListener
{
  String[] mTestPointUUIDs;
  boolean mUseTestPoints = false;

  public OnLongClickMapListener()
  {
    this.mUseTestPoints = false;
  }

  public OnLongClickMapListener(boolean paramBoolean, String[] paramArrayOfString)
  {
    this.mTestPointUUIDs = paramArrayOfString;
    if (this.mTestPointUUIDs != null) {
      this.mUseTestPoints = paramBoolean;
    }
  }

  public void onLongClick(double paramDouble1, double paramDouble2) {}

  public void onLongClick(PIMapLandmarkDataCursor.PIMapPlace paramPIMapPlace) {}

  public void onLongClick(PIMapLandmarkDataCursor.PIMapPlace paramPIMapPlace, double paramDouble1, double paramDouble2) {}
}

public static abstract interface OnPlaceClickListener
{
  public abstract void onPlaceClicked(PIMapLandmarkDataCursor.PIMapPlace paramPIMapPlace);
}

public static abstract interface OnZoneChangeListener
{
  public abstract void onZoneChange(PIMapView paramPIMapView);
}

public static abstract interface OnZoomChangedListener
{
  public abstract void onZoomChanged(float paramFloat);
}

private class OverlayList<PIMapOverlay>
  extends ArrayList<PIMapOverlay>
{
  private PIMapOverlay[] mOverlays;

  private OverlayList()
  {
    populate();
  }

  private void populate()
  {
    this.mOverlays = ((PIMapOverlay[])this.toArray(new Object[size()]));
    if (PIMapView.this.mOverlayView != null) {
      PIMapView.this.mHandler.post(new Runnable()
      {
        public void run()
        {
          PIMapView.this.mOverlayView.invalidate();
        }
      });
    }
  }

  public void add(int paramInt, PIMapOverlay paramE)
  {
    super.add(paramInt, paramE);
    populate();
  }

  public boolean add(PIMapOverlay paramE)
  {
    boolean bool = super.add(paramE);
    populate();
    return bool;
  }

  public boolean addAll(int paramInt, Collection paramCollection)
  {
    boolean bool = super.addAll(paramInt, paramCollection);
    populate();
    return bool;
  }

  public boolean addAll(Collection paramCollection)
  {
    boolean bool = super.addAll(paramCollection);
    populate();
    return bool;
  }

  public void clear()
  {
    super.clear();
    populate();
  }

  public PIMapOverlay[] getOverlays()
  {
    synchronized (PIMapView.sLock)
    {
      PIMapOverlay[] arrayOfPIMapOverlay = this.mOverlays;
      return arrayOfPIMapOverlay;
    }
  }

  public PIMapOverlay remove(int paramInt)
  {
    PIMapOverlay localPIMapOverlay = (PIMapOverlay)super.remove(paramInt);
    populate();
    return localPIMapOverlay;
  }

  public boolean remove(Object paramObject)
  {
    boolean bool = super.remove(paramObject);
    populate();
    return bool;
  }

  protected void removeRange(int paramInt1, int paramInt2)
  {
    super.removeRange(paramInt1, paramInt2);
    populate();
  }

  public PIMapOverlay set(int paramInt, PIMapOverlay paramE)
  {
    PIMapOverlay localPIMapOverlay = (PIMapOverlay)super.set(paramInt, paramE);
    populate();
    return localPIMapOverlay;
  }

  public Object[] toArray()
  {
    synchronized (PIMapView.sLock)
    {
      Object[] arrayOfObject = super.toArray();
      return arrayOfObject;
    }
  }

  public PIMapOverlay[] toArray(Object[] paramArrayOfE)
  {
    synchronized (PIMapView.sLock)
    {
  	  PIMapOverlay[] arrayOfObject = (PIMapOverlay[])super.toArray(paramArrayOfE);
      return arrayOfObject;
    }
  }
}

private class OverlayView
  extends View
{
  private OverlayView(Context paramContext)
  {
    super(paramContext);
    setBackgroundDrawable(null);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    Object[] arrayOfPIMapOverlay = PIMapView.this.mOverlays.getOverlays();

    int i = arrayOfPIMapOverlay.length;
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        super.onDraw(paramCanvas);
        return;
      }
      ((PIMapOverlay)arrayOfPIMapOverlay[j]).onManagedDraw(paramCanvas, PIMapView.this, false);
    }
  }
}

public class PIMapViewProjection
  implements Projection
{
  private PIMapViewProjection() {}

  public PIMapLocation fromCoordinates(double paramDouble1, double paramDouble2)
  {
    PIMapVenueZone localPIMapVenueZone = PIMapView.this.mCurrentZone;
    if (localPIMapVenueZone != null)
    {
      PIMapLocation localPIMapLocation = PIMapLocation.getXYOfLatLon(localPIMapVenueZone, paramDouble1, paramDouble2);
      localPIMapLocation.translate(PIMapView.this);
      return localPIMapLocation;
    }
    return null;
  }

  public PIMapLocation fromPixels(int paramInt1, int paramInt2)
  {
    PIMapVenueZone localPIMapVenueZone = PIMapView.this.mCurrentZone;
    if (localPIMapVenueZone != null)
    {
      PIMapLocation localPIMapLocation = PIMapLocation.getLatLonOfXY(localPIMapVenueZone, paramInt1, paramInt2);
      localPIMapLocation.translate(PIMapView.this);
      return localPIMapLocation;
    }
    return null;
  }

  public PointF getMapPoint(float paramFloat1, float paramFloat2, PointF paramPointF)
  {
    return PIMapView.this.viewPointToMapPoint(paramFloat1, paramFloat2, paramPointF);
  }

  public Point getProjectedPoint(int paramInt1, int paramInt2)
  {
    PointF localPointF = PIMapView.this.computePointfromScale(paramInt1, paramInt2);
    return new Point((int)localPointF.x, (int)localPointF.y);
  }

  public PointF getViewPoint(float paramFloat1, float paramFloat2, PointF paramPointF)
  {
    return PIMapView.this.mapPointToViewPoint(paramFloat1, paramFloat2, paramPointF);
  }

  public float translatedPixelCountForMeters(float paramFloat)
  {
    float f = paramFloat * 3.28084F;
    PIMapVenueZone localPIMapVenueZone = PIMapView.this.mCurrentZone;
    return f / ((float)Math.min(localPIMapVenueZone.getFeetPerPixelX(), localPIMapVenueZone.getFeetPerPixelY()) / PIMapView.this.getScale());
  }
}
}
