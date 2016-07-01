// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.api.maps;

import android.graphics.PointF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.pointinside.android.api.utils.MasterGestureDetector;
import com.pointinside.android.api.utils.MyScroller;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.pointinside.android.api.maps:
//            PIMapView, PIMapOverlay, PIMapLocation, PIMapController

class InputHandler
{

    public InputHandler(PIMapView pimapview, MyScroller myscroller)
    {
        mMapView = pimapview;
        mGestureDetector = new MasterGestureDetector(pimapview.getContext(), mGestureListener);
        mScroller = myscroller;
    }

    public boolean handleScrollOffset()
    {
        boolean flag = mScroller.computeScrollOffset();
        if(flag)
        {
            mMapView.panTo(mScroller.getCurrX(), mScroller.getCurrY());
            mMapView.invalidate();
        }
        return flag;
    }

    public boolean isMultitouchSupported()
    {
        return mGestureDetector.isMultitouchSupported();
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        Iterator iterator = mMapView.getOverlays().iterator();
        do
            if(!iterator.hasNext())
                return false;
        while(!((PIMapOverlay)iterator.next()).onKeyDown(i, keyevent, mMapView));
        return true;
    }

    public boolean onKeyUp(int i, KeyEvent keyevent)
    {
        Iterator iterator = mMapView.getOverlays().iterator();
        do
            if(!iterator.hasNext())
                return false;
        while(!((PIMapOverlay)iterator.next()).onKeyUp(i, keyevent, mMapView));
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        mGestureDetector.onTouchEvent(motionevent);
        return true;
    }

    public boolean onTrackballEvent(MotionEvent motionevent)
    {
        Iterator iterator = mMapView.getOverlays().iterator();
        do
            if(!iterator.hasNext())
                return false;
        while(!((PIMapOverlay)iterator.next()).onTrackballEvent(motionevent, mMapView));
        return true;
    }

    private final MasterGestureDetector mGestureDetector;
    private final com.pointinside.android.api.utils.MasterGestureDetector.SimpleOnMasterGestureListener mGestureListener = new com.pointinside.android.api.utils.MasterGestureDetector.SimpleOnMasterGestureListener() {

        public boolean onDoubleTap(MotionEvent motionevent)
        {
            mMapView.toggleZoom((int)motionevent.getX(), (int)motionevent.getY());
            mMapView.hideOnScreenControls();
            return true;
        }

        public boolean onDown(MotionEvent motionevent)
        {
            if(!mScroller.isFinished())
                mScroller.forceFinished(true);
            return false;
        }

        public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
        {
            mScroller.fling(mMapView.getPanX(), mMapView.getPanY(), -(int)f, -(int)f1, mMapView.pinPanX(0x80000000), mMapView.pinPanX(0x7fffffff), mMapView.pinPanY(0x80000000), mMapView.pinPanY(0x7fffffff));
            mMapView.showOnScreenControls();
            mMapView.invalidate();
            return true;
        }

        public void onLongPress(MotionEvent motionevent)
        {
            Iterator iterator = mMapView.getOverlays().iterator();
            do
            {
                if(iterator.hasNext())
                    continue;
                PIMapView.OnLongClickMapListener onlongclickmaplistener = mMapView.getOnLongClickMapListener();
                if(onlongclickmaplistener != null)
                {
                    PointF pointf = mMapView.computePointfromScale(motionevent.getX(), motionevent.getY());
                    int i = Math.round(pointf.x);
                    int j = Math.round(pointf.y);
                    com.pointinside.android.api.dao.PIMapLandmarkDataCursor.PIMapPlace pimapplace;
                    if(onlongclickmaplistener.mUseTestPoints)
                    {
                        List list = mMapView.getMapPlaceForServiceTypeUUID(onlongclickmaplistener.mTestPointUUIDs);
                        pimapplace = mMapView.closestPlaceToPoint(i, j, list);
                    } else
                    {
                        pimapplace = mMapView.closestPlaceToPoint(i, j);
                    }
                    if(pimapplace != null)
                    {
                        onlongclickmaplistener.onLongClick(pimapplace);
                        PIMapVenueZone pimapvenuezone = mMapView.getCurrentZone();
                        if(pimapvenuezone != null)
                        {
                            PIMapLocation pimaplocation = PIMapLocation.getLatLonOfXY(pimapvenuezone, i, j);
                            double d = pimaplocation.getLatitude();
                            double d1 = pimaplocation.getLongitude();
                            onlongclickmaplistener.onLongClick(d, d1);
                            onlongclickmaplistener.onLongClick(pimapplace, d, d1);
                            return;
                        }
                    }
                }
                return;
            } while(!((PIMapOverlay)iterator.next()).onLongPress(motionevent, mMapView));
        }

        public boolean onScale(com.pointinside.android.api.utils.MasterGestureDetector.ScaleGestureDetectorCompat scalegesturedetectorcompat)
        {
            float f = mMapView.getScale();
            boolean flag = mMapView.pinchZoom(mMapView.getScale() * scalegesturedetectorcompat.getScaleFactor(), scalegesturedetectorcompat.getFocusX(), scalegesturedetectorcompat.getFocusY());
            float f1 = mMapView.getScale();
            if(f != f1)
                if(!flag)
                {
                    mCanZoomBothWays = false;
                    mMapView.notifyZoomChange(f1);
                } else
                if(!mCanZoomBothWays)
                {
                    boolean flag1 = mMapView.getController().canZoomIn();
                    boolean flag2 = false;
                    if(flag1)
                    {
                        boolean flag3 = mMapView.getController().canZoomOut();
                        flag2 = false;
                        if(flag3)
                            flag2 = true;
                    }
                    if(flag2)
                    {
                        mCanZoomBothWays = true;
                        mMapView.notifyZoomChange(f1);
                        return true;
                    }
                }
            return true;
        }

        public boolean onScaleBegin(com.pointinside.android.api.utils.MasterGestureDetector.ScaleGestureDetectorCompat scalegesturedetectorcompat)
        {
            mMapView.cancelZoom();
            mMapView.hideOnScreenControls();
            boolean flag;
            if(mMapView.getController().canZoomIn() && mMapView.getController().canZoomOut())
                flag = true;
            else
                flag = false;
            mCanZoomBothWays = flag;
            return true;
        }

        public void onScaleEnd(com.pointinside.android.api.utils.MasterGestureDetector.ScaleGestureDetectorCompat scalegesturedetectorcompat)
        {
            mMapView.notifyZoomChange(mMapView.getScale());
        }

        public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
        {
            mMapView.panTo((int)(f + (float)mMapView.getPanX()), (int)(f1 + (float)mMapView.getPanY()));
            mMapView.showOnScreenControls();
            return true;
        }

        public boolean onScrollFinished(MotionEvent motionevent)
        {
            mScroller.springBack(mMapView.getPanX(), mMapView.getPanY(), mMapView.pinPanX(0x80000000), mMapView.pinPanX(0x7fffffff), mMapView.pinPanY(0x80000000), mMapView.pinPanY(0x7fffffff));
            mMapView.invalidate();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent motionevent)
        {
            Iterator iterator = mMapView.getOverlays().iterator();
            do
                if(!iterator.hasNext())
                {
                    PointF pointf = mMapView.computePointfromScale(motionevent.getX(), motionevent.getY());
                    int i = Math.round(pointf.x);
                    int j = Math.round(pointf.y);
                    if(!mMapView.checkHotlinkHit(i, j))
                    {
                        PIMapView.OnPlaceClickListener onplaceclicklistener = mMapView.getOnPlaceClickListener();
                        if(onplaceclicklistener != null)
                        {
                            com.pointinside.android.api.dao.PIMapLandmarkDataCursor.PIMapPlace pimapplace = mMapView.closestPlaceToPoint(i, j);
                            if(pimapplace != null)
                                onplaceclicklistener.onPlaceClicked(pimapplace);
                        }
                        mMapView.showOnScreenControls();
                    }
                    return true;
                }
            while(!((PIMapOverlay)iterator.next()).onSingleTapUp(motionevent, mMapView));
            return true;
        }

        private boolean mCanZoomBothWays;
        final InputHandler this$0;


            {
//                super();
                this$0 = InputHandler.this;
            }
    }
;
    private final PIMapView mMapView;
    private final MyScroller mScroller;


}
