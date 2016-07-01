// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.ui;

import com.pointinside.android.app.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapPlaceDataCursor;
import com.pointinside.android.api.dao.PIMapZoneDataCursor;
import com.pointinside.android.api.location.AbstractLocationEngine;
import com.pointinside.android.api.maps.*;
import com.pointinside.android.api.nav.DefaultRouteOverlay;
import com.pointinside.android.api.nav.Route;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.*;
import com.pointinside.android.app.widget.*;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.service.DealsService;
import com.pointinside.android.api.dao.PIMapLandmarkDataCursor;

import java.util.*;

// Referenced classes of package com.pointinside.android.app.ui:
//            PlaceDetailActivity, PlaceBrowseActivity, GoogleMapActivity, FeedbackActivity,
//            AboutActivity, PlaceSearchActivity

public class VenueMapActivity extends PIMapActivity
{
		
			
			
	private class BaseMapPlaceOverlay extends PointInsideMapOverlay
    {

        public void addOverlay(PIMapOverlayItem pimapoverlayitem)
        {
            if(pimapoverlayitem.getMarker(0) == null)
            {
                Drawable drawable = getDefaultMarkerDrawable();
                drawable.setBounds(copyDefaultMarkerDrawableBounds(null));
                pimapoverlayitem.setMarker(drawable);
            }
            super.addOverlay(pimapoverlayitem);
        }

        final VenueMapActivity this$0;

        public BaseMapPlaceOverlay(Drawable drawable)
        {
            super(drawable);
            this$0 = VenueMapActivity.this;
        }
    }

    private static class CalculateRouteTask extends DetachableAsyncTask
    {

        protected  Route doInBackground(Void avoid[])
        {
            Route route;
            try
            {
                route = Route.calculate(mVenueUUID, mStart, mEnd);
            }
            catch(com.pointinside.android.api.net.JSONWebRequester.RestResponseException restresponseexception)
            {
                Log.e(VenueMapActivity.TAG, (new StringBuilder("Unable to calculate route from ")).append(mStart).append(" to ").append(mEnd).append(" for venue ").append(mVenueUUID).toString(), restresponseexception);
                return null;
            }
            return route;
        }

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        private final com.pointinside.android.api.nav.Route.RouteEndpoint mEnd;
        private final com.pointinside.android.api.nav.Route.RouteEndpoint mStart;
        private final String mVenueUUID;

        public CalculateRouteTask(String s, com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint, com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint1)
        {
            mVenueUUID = s;
            mStart = routeendpoint;
            mEnd = routeendpoint1;
        }
    }

    private class DialogHelperListener
        implements com.pointinside.android.app.util.DialogCompatHelper.OnCreateDialogListener, com.pointinside.android.app.util.DialogCompatHelper.OnPrepareDialogListener
    {

        private Dialog createNavPointSelectionDialog(Bundle bundle)
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VenueMapActivity.this);
            builder.setTitle(0x7f060065);
            View view = LayoutInflater.from(VenueMapActivity.this).inflate(0x7f03001c, null);
            final View start = view.findViewById(R.id.start);
            final View end = view.findViewById(R.id.end);

            NavPointHolder navpointholder = new NavPointHolder(start, 1);
            start.setTag(navpointholder);
            navpointholder.pickerButton.setOnClickListener(new NavDialogPickerButtonListener(navpointholder));
            View view1 = view.findViewById(0x7f0e002f);
            NavPointHolder navpointholder1 = new NavPointHolder(view1, 2);
            view1.setTag(navpointholder1);
            navpointholder1.pickerButton.setOnClickListener(new NavDialogPickerButtonListener(navpointholder1));
            builder.setPositiveButton(0x104000a, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    NavPointHolder navpointholder = (NavPointHolder)start.getTag();
                    NavPointHolder navpointholder1 = (NavPointHolder)end.getTag();
                    if(!navpointholder.isSet() || !navpointholder1.isSet())
                    {
                        Toast.makeText(VenueMapActivity.this, 0x7f060098, 0).show();
                        return;
                    }
                    com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint = convertNavPointHolderToEndpoint(navpointholder);
                    if(routeendpoint == null)
                    {
                        Toast.makeText(VenueMapActivity.this, 0x7f060099, 1).show();
                        return;
                    } else
                    {
                        com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint1 = convertNavPointHolderToEndpoint(navpointholder1);
                        requestShowRoute(routeendpoint, routeendpoint1, navpointholder1.placeName);
                        return;
                    }
                }
/*
                final DialogHelperListener this$1;
                private final View val$end;
                private final View val$start;


	            {
	                this$1 = final_dialoghelperlistener;
	                start = view;
	                end = View;
	            }
*/
            }
            );
            builder.setNegativeButton(0x1040000, null);
            builder.setView(view);
            return builder.create();
        }

        private void prepareNavPointSelectionDialog(Dialog dialog, Bundle bundle)
        {
            boolean flag = bundle.getBoolean("resuming");
            boolean flag1 = bundle.getBoolean("startPointUseMyLocation", false);
            String s = bundle.getString("startPointUUID");
            String s1 = bundle.getString("startPointName");
            String s2 = bundle.getString("endPointUUID");
            String s3 = bundle.getString("endPointName");
            View view = dialog.findViewById(0x7f0e002e);
            View view1;
            if(!flag || s != null)
                if(flag1)
                    setNavPointToUserLocation(view);
                else
                    setNavPoint(view, s, s1);
            view1 = dialog.findViewById(0x7f0e002f);
            if(!flag || s2 != null)
                setNavPoint(view1, s2, s3);
        }

        private void setNavPoint(View view, String s, String s1)
        {
            NavPointHolder navpointholder = (NavPointHolder)view.getTag();
            navpointholder.useMyLocation = false;
            navpointholder.placeUUID = s;
            navpointholder.placeName = s1;
            navpointholder.label.setText(s1);
        }

        private void setNavPointToUserLocation(View view)
        {
            NavPointHolder navpointholder = (NavPointHolder)view.getTag();
            navpointholder.useMyLocation = true;
            navpointholder.placeUUID = null;
            navpointholder.placeName = null;
            navpointholder.label.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(0x7f0200b2), null, null, null);
            navpointholder.label.setText(0x7f060097);
        }

        public Dialog onCreateDialog(int i, Bundle bundle)
        {
            switch(i)
            {
            default:
                throw new IllegalArgumentException((new StringBuilder("Unknown dialog with id=")).append(i).toString());

            case 1: // '\001'
                return createNavPointSelectionDialog(bundle);
            }
        }

        public void onPrepareDialog(int i, Dialog dialog, Bundle bundle)
        {
            switch(i)
            {
            default:
                throw new IllegalArgumentException((new StringBuilder("Unknown dialog with id=")).append(i).toString());

            case 1: // '\001'
                prepareNavPointSelectionDialog(dialog, bundle);
                break;
            }
        }

        public void resumeNavPointSelectionDialog(int i, String s, String s1)
        {
            Bundle bundle = new Bundle();
            bundle.putBoolean("resuming", true);
            String s2;
            String s3;
            if(i == 1)
            {
                s2 = "startPointUUID";
                s3 = "startPointName";
            } else
            {
                s2 = "endPointUUID";
                s3 = "endPointName";
            }
            bundle.putString(s2, s);
            bundle.putString(s3, s1);
            mDialogHelper.showDialog(1, bundle);
        }

        public void showNavPointSelectionDialog(String s, String s1)
        {
            Bundle bundle = new Bundle();
            bundle.putBoolean("resuming", false);
            PIMapLocation pimaplocation = mLocationOverlay.getLocation();
            if(pimaplocation != null && locateUserZoneByLocation(pimaplocation) != null)
                bundle.putBoolean("startPointUseMyLocation", true);
            bundle.putString("endPointUUID", s);
            bundle.putString("endPointName", s1);
            mDialogHelper.showDialog(1, bundle);
        }

        private static final String ARG_END_POINT_NAME = "endPointName";
        private static final String ARG_END_POINT_UUID = "endPointUUID";
        private static final String ARG_RESUMING = "resuming";
        private static final String ARG_START_POINT_NAME = "startPointName";
        private static final String ARG_START_POINT_USE_MY_LOCATION = "startPointUseMyLocation";
        private static final String ARG_START_POINT_UUID = "startPointUUID";
        private static final int DIALOG_NAV_POINT_SELECTION = 1;
        final VenueMapActivity this$0;


        private DialogHelperListener()
        {
            super();
            this$0 = VenueMapActivity.this;
        }

        DialogHelperListener(DialogHelperListener dialoghelperlistener)
        {
            this();
        }

        private class NavDialogPickerButtonListener
        implements android.view.View.OnClickListener
    {

        public void onClick(View view)
        {
            dismissDialog(1);
            VenueMapActivity venuemapactivity = VenueMapActivity.this;//_fld0;
            int i;
            if(mReceiver.which == 1)
                i = R.string.pick_start_point;
            else
                i = R.string.pick_end_point;
            Toast.makeText(venuemapactivity, i, 0).show();
            clearPlacePins();
            mNavPickerMode = true;
            mNavPickerWhichEndpoint = mReceiver.which;
        }

        private final NavPointHolder mReceiver;
        final DialogHelperListener this$1;

        public NavDialogPickerButtonListener(NavPointHolder navpointholder)
        {
            super();
            this$1 = DialogHelperListener.this;
            mReceiver = navpointholder;
        }
    }
    }


    private class FilterEventHandler
        implements com.pointinside.android.app.widget.FilterWheel.OnFilterEventListener
    {

        private void showServiceTypePins(final int i, final String serviceUUIDs[], final int wormholeTypes[])
        {
            clearPlacePins();
            (new Thread() {

                public void run()
                {
                	android.os.Process.setThreadPriority(10);
                    final List services;
                    
                    if(serviceUUIDs != null)
                        services = mPIMapView.getMapPlaceForServiceTypeUUID(serviceUUIDs);
                    else
                        services = mPIMapView.getMapWormholeForTypes(wormholeTypes);
                    runOnUiThread( new Runnable() {

                        public void run()
                        {

                        	clearServicePins();
                            Iterator iterator = services.iterator();
                            for(;iterator.hasNext();) {
                                PIMapOverlayItem pimapoverlayitem = (PIMapOverlayItem)iterator.next();
                                
                                pimapoverlayitem.setMarker(PIMapOverlay.boundCenter(InVenueFilterHelper.getServiceMarker(getResources(), type, pimapoverlayitem)));
                                mServicePins.addOverlay(pimapoverlayitem);
                            }
                            mServicePins.populateOverlays();
                            mMapOverlays.add(mServicePins);
//                            type = ((PIMapLandmarkDataCursor.PIMapPlace)iterator).getPlaceTypeId();
                            if(services.isEmpty())
                                Toast.makeText(VenueMapActivity.this, InVenueFilterHelper.getNoServicesMessageForType(type), 0).show();
                            return;
                        }

                        private int type =i;

                    }
);
                }
            }
          ).start();
        }

        public void onCollapse()
        {
            updateMapPadding();
            clearServicePins();
            mPIMapView.hideOnScreenControls();
        }

        public void onExpand()
        {
            updateMapPadding();
            if(!mFilterWheel.isLeftSelected())
                onServiceSelected(mFilterWheel.getRightSelectedPosition());
            mPIMapView.hideOnScreenControls();
        }

        public void onLeftItemSelectionChange(int i)
        {
            if(mFilterWheel.isExpanded())
                onZoneSelected(i);
        }

        public void onLeftSelected()
        {
            clearServicePins();
        }

        public void onRightItemSelectionChange(int i)
        {
            if(mFilterWheel.isExpanded())
                onServiceSelected(i);
        }

        public void onRightSelected()
        {
            if(mFilterWheel.isExpanded())
                onServiceSelected(mFilterWheel.getRightSelectedPosition());
        }

        public void onServiceSelected(int i)
        {
            InVenueFilterHelper.Service service = mFilterHelper.getService(i);
            showServiceTypePins(service.type, service.mapUUIDs, service.wormholeTypes);
        }

        public void onZoneSelected(int i)
        {
            InVenueFilterHelper.Zone zone = mFilterHelper.getZone(i);
            if(zone.zoneIndex != mPIMapView.getCurrentZoneIndex())
            {
                prepareForZoneChange();
                mController.loadZone(zone.zoneIndex);
            }
        }

        final VenueMapActivity this$0;


        private FilterEventHandler()
        {
            super();
            this$0 = VenueMapActivity.this;
        }

        FilterEventHandler(FilterEventHandler filtereventhandler)
        {
            this();
        }
    }

    private class NavPinsFakeOverlay
    {

        private ZoneChangePinItem createAndAddZoneChangePin(Route.RoutePoint routepoint, Route.RoutePoint routepoint1, int i, String s, String s1)
        {
            PIMapLocation pimaplocation = new PIMapLocation(routepoint1.getPixelX(), routepoint1.getPixelY());
            int j = whichDirection(routepoint.getZoneUUID(), s, s1);
            ZoneChangePinItem zonechangepinitem = new ZoneChangePinItem(pimaplocation, null, null, i, j, mPIMapVenue.getIndexOfZoneWithUUID(routepoint.getZoneUUID()));
            if(j == 1)
            {
                mZoneChangePinsUp.addOverlay(zonechangepinitem);
            } else
            {
                if(j == 2)
                {
                    mZoneChangePinsDown.addOverlay(zonechangepinitem);
                    return zonechangepinitem;
                }
                if(j == 0)
                {
                    mZoneChangePinsJump.addOverlay(zonechangepinitem);
                    return zonechangepinitem;
                }
            }
            return zonechangepinitem;
        }

        private PIMapOverlayItem getOverlayItemForRouteEndpoint(Route.RouteEndpoint routeendpoint)
        {
            if(routeendpoint.placeUUID != null)
            {
                return getOverlayItemForPlace(routeendpoint.placeUUID);
            } else
            {
                Route.RoutePoint routepoint = routeendpoint.point;
                return new PIMapOverlayItem(new PIMapLocation(routepoint.getLatitude(), routepoint.getLongitude(), routepoint.getPixelX(), routepoint.getPixelY()), "foo", "bar", mPIMapVenue.getIndexOfZoneWithUUID(routepoint.getZoneUUID()));
            }
        }

        private PIMapOverlayItem getPin(NavPinOverlay navpinoverlay)
        {
            if(navpinoverlay.size() == 0)
                return null;
            else
                return navpinoverlay.getItem(0);
        }

        private void updateZoneChangePins(int i)
        {
            String s;
            String s1;
            String s2;
            Route route;
            Route.RoutePoint routepoint;
            boolean flag;
            int k;
            mZoneChangePinsUp.clear();
            mZoneChangePinsDown.clear();
            mZoneChangePinsJump.clear();
            s = mPIMapView.getCurrentZone().getZoneUUID();
            if(mPIMapView.getCurrentZone().getZoneIndex() != i)
                throw new IllegalStateException();
            PIMapVenueZone pimapvenuezone = mPIMapView.getNextZone();
            PIMapVenueZone pimapvenuezone1 = mPIMapView.getPreviousZone();
            int j;
            if(pimapvenuezone != null)
                s1 = pimapvenuezone.getZoneUUID();
            else
                s1 = null;
            if(pimapvenuezone1 != null)
                s2 = pimapvenuezone1.getZoneUUID();
            else
                s2 = null;
            route = mRouteOverlay.getRoute();
            j = route.getPointsCount();
            routepoint = null;
            flag = false;
            k = 0;
    	  for(;k < j;k++) {
            Route.RoutePoint routepoint1;
            routepoint1 = route.getPoint(k);
            if(!routepoint1.getZoneUUID().equals(s)) {
                flag = true;
                return;
            }
            if(k > 0 && !flag)
                createAndAddZoneChangePin(routepoint, routepoint1, i, s1, s2);

            if(!flag) {
            	routepoint = routepoint1;
                //createAndAddZoneChangePin(routepoint1, routepoint, i, s1, s2);
                //return;
            }
    	  }
        }

        private int whichDirection(String s, String s1, String s2)
        {
            if(s.equals(s1))
                return 1;
            return !s.equals(s2) ? 0 : 2;
        }

        public void addOverlays(Route.RouteEndpoint routeendpoint, Route.RouteEndpoint routeendpoint1)
        {
            mStartPin.addOverlay(getOverlayItemForRouteEndpoint(routeendpoint));
            mEndPin.addOverlay(getOverlayItemForRouteEndpoint(routeendpoint1));
            updateCurrentZone(mPIMapView.getCurrentZoneIndex());
            mMapOverlays.add(mStartPin);
            mMapOverlays.add(mEndPin);
            mMapOverlays.add(mZoneChangePinsUp);
            mMapOverlays.add(mZoneChangePinsDown);
            mMapOverlays.add(mZoneChangePinsJump);
        }

        public void clear()
        {
            mStartPin.clear();
            mEndPin.clear();
            mZoneChangePinsUp.clear();
            mZoneChangePinsDown.clear();
            mZoneChangePinsJump.clear();
            mMapOverlays.remove(mStartPin);
            mMapOverlays.remove(mEndPin);
            mMapOverlays.remove(mZoneChangePinsUp);
            mMapOverlays.remove(mZoneChangePinsDown);
            mMapOverlays.remove(mZoneChangePinsJump);
        }

        public PIMapOverlayItem getEndPin()
        {
            return getPin(mEndPin);
        }

        public PIMapOverlayItem getStartPin()
        {
            return getPin(mStartPin);
        }

        public void updateCurrentZone(int i)
        {
            boolean flag = true;
            NavPinOverlay navpinoverlay = mStartPin;
            boolean flag1;
            NavPinOverlay navpinoverlay1;
            if(getStartPin().getZoneIndex() == i)
                flag1 = flag;
            else
                flag1 = false;
            navpinoverlay.setNavPinInCurrentZone(flag1);
            navpinoverlay1 = mEndPin;
            if(getEndPin().getZoneIndex() != i)
                flag = false;
            navpinoverlay1.setNavPinInCurrentZone(flag);
            updateZoneChangePins(i);
        }

        private final NavPinOverlay mEndPin;
        private final NavPinOverlay mStartPin;
        private final ZoneChangePinsOverlay mZoneChangePinsDown;
        private final ZoneChangePinsOverlay mZoneChangePinsJump;
        private final ZoneChangePinsOverlay mZoneChangePinsUp;
        final VenueMapActivity this$0;




        public NavPinsFakeOverlay()
        {
            super();
            this$0 = VenueMapActivity.this;
            Resources resources = getResources();
            mStartPin = new NavPinOverlay(resources.getDrawable(0x7f0200be));
            mEndPin = new NavPinOverlay(resources.getDrawable(0x7f0200bd));
            mZoneChangePinsUp = new ZoneChangePinsOverlay(resources.getDrawable(0x7f02006b));
            mZoneChangePinsDown = new ZoneChangePinsOverlay(resources.getDrawable(0x7f02005e));
            mZoneChangePinsJump = new ZoneChangePinsOverlay(resources.getDrawable(0x7f02005a));
        }
    }

    private class NavPinOverlay extends PopupShowingPlaceOverlay
    {

        private PIMapVenueZone getZoneByIndex(int i)
        {
            try {
        	PIMapZoneDataCursor pimapzonedatacursor;
            pimapzonedatacursor = mPIMapVenue.getVenueZones();
            if(pimapzonedatacursor == null)
                return null;
            boolean flag = true;
            while(flag) {
	            PIMapVenueZone pimapvenuezone;
	            if(pimapzonedatacursor.getZoneIndex() == i) {
		            pimapvenuezone = pimapzonedatacursor.getPIMapVenueZone(VenueMapActivity.this);
		            pimapzonedatacursor.close();
		            return pimapvenuezone;
	            }
	            flag = pimapzonedatacursor.moveToNext();

            }

            pimapzonedatacursor.close();
            } catch(Exception exception) {
            	exception.printStackTrace();
            }
            return null;
        }

        public void setNavPinInCurrentZone(boolean flag)
        {
            Drawable drawable = mMarker;
            char c;
            if(flag)
                c = '\377';
            else
                c = '\231';
            drawable.setAlpha(c);
            if(size() > 0)
            {
                PIMapOverlayItem pimapoverlayitem = getItem(0);
                PIMapLocation pimaplocation = pimapoverlayitem.getLocation();
                if(!mInZonePixelCopied)
                {
                    mInZonePixelX = pimaplocation.getPixelX();
                    mInZonePixelY = pimaplocation.getPixelY();
                    mInZonePixelCopied = true;
                }
                PIMapLocation pimaplocation1;
                if(!flag)
                {
                    PIMapLocation pimaplocation2 = PIMapLocation.getLatLonOfXY(getZoneByIndex(pimapoverlayitem.getZoneIndex()), mInZonePixelX, mInZonePixelY);
                    pimaplocation1 = mPIMapView.getProjection().fromCoordinates(pimaplocation2.getLatitude(), pimaplocation2.getLongitude());
                } else
                {
                    pimaplocation1 = new PIMapLocation(mInZonePixelX, mInZonePixelY);
                }
                pimapoverlayitem.setLocation(pimaplocation1);
                mPIMapView.invalidate();
            }
        }

        private boolean mInZonePixelCopied;
        private int mInZonePixelX;
        private int mInZonePixelY;
        private final Drawable mMarker;

        public NavPinOverlay(Drawable drawable)
        {
            super(drawable.mutate());
            mMarker = drawable;
        }
    }

    private class ZoneChangePinsOverlay extends PointInsideMapOverlay
    {

        protected boolean onTap(int i)
        {
            ZoneChangePinItem zonechangepinitem = (ZoneChangePinItem)getItem(i);
            Log.d(VenueMapActivity.TAG, (new StringBuilder("Changing zone to ")).append(zonechangepinitem.getZoneChangeIndex()).append(" (currently ").append(mPIMapView.getCurrentZoneIndex()).append(")").toString());
            prepareForZoneChange();
            mController.loadZone(zonechangepinitem.getZoneChangeIndex());
            return true;
        }


        public ZoneChangePinsOverlay(Drawable drawable)
        {
            super(drawable, true);
        }
    }

    private static class NavPointHolder
    {

        public boolean equals(Object obj)
        {
            if(obj instanceof NavPointHolder)
            {
                for(NavPointHolder navpointholder = (NavPointHolder)obj; useMyLocation && navpointholder.useMyLocation || placeUUID != null && placeUUID.equals(navpointholder.placeUUID);)
                    return true;

            }
            return false;
        }

        public boolean isSet()
        {
            return useMyLocation || placeUUID != null;
        }

        public static final int WHICH_END = 2;
        public static final int WHICH_START = 1;
        public final TextView label;
        public final View pickerButton;
        public String placeName;
        public String placeUUID;
        public boolean useMyLocation;
        public final int which;

        public NavPointHolder(View view, int i)
        {
            which = i;
            label = (TextView)view.findViewById(0x7f0e0030);
            pickerButton = view.findViewById(0x7f0e0031);
        }
    }

    private static class PIMapDeal extends PIMapOverlayItem
    {

        public String getUUID()
        {
            return mPlaceUUID;
        }

        private final String mPlaceUUID;

        public PIMapDeal(PIMapLocation pimaplocation, String s, String s1, String s2, int i)
        {
            super(pimaplocation, s, s1, i);
            mPlaceUUID = s2;
        }
    }

    private class PopupShowingPlaceOverlay extends BaseMapPlaceOverlay
    {

        protected boolean onTap(int i)
        {
            PIMapOverlayItem pimapoverlayitem = getItem(i);
            if(showingPopupForPlace(pimapoverlayitem.getUUID()))
                dismissPopup();
            else
                showPopup(pimapoverlayitem);
            return true;
        }


        public PopupShowingPlaceOverlay(Drawable drawable)
        {
            super(drawable);
        }
    }

    private class QueryHandler extends AsyncQueryHandler
    {

        private String getCategory(ArrayList arraylist)
        {
            String s;
        	if(arraylist.size() == 1) {
	            s = ((DealData)arraylist.get(0)).category;
	            return s;
            } else {
            int i = arraylist.size();
            s = null;
            int j = 0;
            do
            {
                DealData dealdata;
                if(j < i)
                {
                        dealdata = (DealData)arraylist.get(j);
                        if(s == null || s.equals(dealdata.category))
                            continue;
                        s = null;
                }
                if(s == null)
                    return "General";
                if(true)
                    continue;
                s = dealdata.category;
                j++;
            } while(true);
            }
        }

        private void showDeals(Cursor cursor)
        {
            HashMap hashmap;
            mDealPins.clear();
            mMapOverlays.remove(mDealPins);
            hashmap = new HashMap();
            String s4;
            ArrayList arraylist1 = null;
	        PIMapPlaceDataCursor pimapplacedatacursor =null;
            while(true) {
	            boolean flag = cursor.moveToNext();

	            if(!flag) {
		            cursor.close();
		            if(hashmap.isEmpty()) continue;

	            Set set = hashmap.keySet();

	            pimapplacedatacursor = mPIMapVenue.getMapPlacesForUUIDs((String[])set.toArray(new String[set.size()]));
	            PIMapLocation pimaplocation;
	            String s1;
	            pimaplocation = new PIMapLocation(pimapplacedatacursor.getLocationPixelX(), pimapplacedatacursor.getLocationPixelY());
	            s1 = pimapplacedatacursor.getUUID();
	            arraylist1 = (ArrayList)hashmap.get(s1);
	            if(arraylist1 != null) {

	            if(pimapplacedatacursor.getZoneIndex() == mPIMapView.getCurrentZoneIndex()) {
		            String s2 = getCategory(arraylist1);
		            if(arraylist1.size() == 1) {
			            s4 = ((DealData)arraylist1.get(0)).title;
			            PIMapDeal pimapdeal = new PIMapDeal(pimaplocation, pimapplacedatacursor.getName(), s4, s1, pimapplacedatacursor.getZoneIndex());
			            pimapdeal.setMarker(PIMapOverlay.boundCenterBottom(getResources().getDrawable(DealsUtils.getPinIconId(s2))));
			            mDealPins.addOverlay(pimapdeal);
		            }
	            } else {
		            boolean flag1 = pimapplacedatacursor.moveToNext();
		            if(!flag1) {
			            pimapplacedatacursor.close();
			            mMapOverlays.add(mDealPins);
			            return;
		            }
	            }

	        } else {
	            String s;
	            DealData dealdata;
	            ArrayList arraylist;
	            s = cursor.getString(cursor.getColumnIndexOrThrow("place_uuid"));
	            if(TextUtils.isEmpty(s))
	                continue; /* Loop/switch isn't completed */
	            dealdata = new DealData(null);
	            dealdata.id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
	            dealdata.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
	            dealdata.desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
	            dealdata.category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
	            arraylist = (ArrayList)hashmap.get(s);
	            if(arraylist == null)
		            arraylist = new ArrayList();
	            hashmap.put(s, arraylist);
	            arraylist.add(dealdata);
	            cursor.close();
	            continue; /* Loop/switch isn't completed */
	       		}
	         }
	            String s3;
	            VenueMapActivity venuemapactivity = VenueMapActivity.this;
	            Object aobj[] = new Object[1];
	            aobj[0] = Integer.valueOf(arraylist1.size());
	            s3 = venuemapactivity.getString(0x7f06008b, aobj);
	            s4 = s3;
	            pimapplacedatacursor.close();
            }

        }

        protected void onQueryComplete(int i, Object obj, Cursor cursor)
        {
            Log.d(VenueMapActivity.TAG, (new StringBuilder("onQueryComplete: token=")).append(i).toString());
            switch(i)
            {
            default:
                throw new IllegalArgumentException((new StringBuilder("Unknown token: ")).append(i).toString());

            case 1: // '\001'
                break;
            }
            if(cursor != null)
                showDeals(cursor);
        }

        public static final int TOKEN_GET_DEALS = 1;

        public QueryHandler()
        {
            super(getContentResolver());
        }
    }

    private class DealData
    {

        public String category;
        public String desc;
        public long id;
        public String title;

        private DealData()
        {
            super();
        }

        DealData(DealData dealdata)
        {
            this();
        }
    }

    private static class ZoneChangePinItem extends PIMapOverlayItem
    {

        public int getZoneChangeDirection()
        {
            return mWhichDirection;
        }

        public int getZoneChangeIndex()
        {
            return mZoneChangeIndex;
        }

        private static final int DIRECTION_DOWN = 2;
        private static final int DIRECTION_JUMP = 0;
        private static final int DIRECTION_UP = 1;
        private final int mWhichDirection;
        private final int mZoneChangeIndex;

        public ZoneChangePinItem(PIMapLocation pimaplocation, String s, String s1, int i, int j, int k)
        {
            super(pimaplocation, s, s1, i);
            mWhichDirection = j;
            mZoneChangeIndex = k;
        }
    }


    public VenueMapActivity()
    {
        mDownloadObserver = new com.pointinside.android.api.PIMapVenue.PIVenueDownloadObserver() {

            public void promoImagesReady()
            {
                super.promoImagesReady();
                isFinishing();
            }

        }
;
    }

    private static void assertCurrentVenue(String s)
    {
        PIMapVenue pimapvenue = PointInside.getInstance().getCurrentVenue();
        if(pimapvenue == null || !pimapvenue.getVenueUUID().equals(s))
            throw new IllegalStateException("Results should be shown from the current venue");
        else
            return;
    }

    private void centerMapOnItem(final PIMapOverlayItem item, final int offsetX, final int offsetY)
    {
        mPIMapView.runOnMapReady(new Runnable() {

            public void run()
            {
                PIMapLocation pimaplocation = item.getLocation();
                item.setProjection(mPIMapView);
                int i = pimaplocation.getTranslatedPixelX();
                int j = pimaplocation.getTranslatedPixelY();
                mController.centerToXY(i + offsetX, j + offsetY);
            }

        }
);
    }

    private void clearDealPins()
    {
        mDealPins.clear();
        mMapOverlays.remove(mDealPins);
        clearPopup();
    }

    private void clearOverviewPins()
    {
        mOverviewPins.clear();
        mMapOverlays.remove(mOverviewPins);
        clearPopup();
    }

    private void clearPlacePins()
    {
        mPlacePins.clear();
        mMapOverlays.remove(mPlacePins);
        clearPopup();
    }

    private void clearPopup()
    {
        if(mPopupLayout != null)
        {
            mPIMapView.removeView(mPopupLayout);
            mPopupLayout = null;
        }
    }

    private void clearServicePins()
    {
        mServicePins.clear();
        mMapOverlays.remove(mServicePins);
        clearPopup();
    }

    private com.pointinside.android.api.nav.Route.RouteEndpoint convertNavPointHolderToEndpoint(NavPointHolder navpointholder)
    {
        if(navpointholder.useMyLocation)
        {
            PIMapLocation pimaplocation = mLocationOverlay.getLocation();
            com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint = null;
            if(pimaplocation != null)
            {
                PIMapVenueZone pimapvenuezone = locateUserZoneByLocation(pimaplocation);
                String s;
                if(pimapvenuezone != null)
                    s = pimapvenuezone.getZoneUUID();
                else
                    s = null;
                routeendpoint = null;
                if(s != null)
                    routeendpoint = com.pointinside.android.api.nav.Route.RouteEndpoint.fromArbitraryLocation(s, pimaplocation.getLatitude(), pimaplocation.getLongitude());
            }
            return routeendpoint;
        } else
        {
            return com.pointinside.android.api.nav.Route.RouteEndpoint.fromPlaceUUID(navpointholder.placeUUID);
        }
    }

    private void dismissPopup()
    {
        if(mPopupLayout != null)
        {
            mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 0x7f04000e));
            mPIMapView.removeView(mPopupLayout);
            mPopupLayout = null;
        }
    }

    private void enterNavModeWithRoute(Route route, String s)
    {
        int i = route.getPointsCount();
        int j = 0;
        com.pointinside.android.api.nav.Route.RoutePoint routepoint;
        do
        {
            if(j >= i)
            {
                mNavMode = true;
                mNavBar.setVisibility(0);
                Spannable spannable = (Spannable)TextUtils.replace(getText(0x7f060063), new String[] {
                    "%s"
                }, new CharSequence[] {
                    s
                });
                int k = spannable.toString().indexOf(s);
                spannable.setSpan(new ForegroundColorSpan(-1), k, k + s.length(), 0);
                spannable.setSpan(new RelativeSizeSpan(1.2F), k, k + s.length(), 0);
                mNavBar.setTitle(spannable);
                int l = route.getWalkingTimeInMinutes();
                float f = DistanceUtils.kilometersToMiles(route.getDistance() / 1000F);
                CharSequence charsequence = getText(0x7f060064);
                String as[] = {
                    "{0}", "{1}", "{2}", "{3}"
                };
                CharSequence acharsequence[] = new CharSequence[4];
                acharsequence[0] = String.valueOf(l);
                acharsequence[1] = getResources().getQuantityString(0x7f0a0000, l);
                Object aobj[] = new Object[1];
                aobj[0] = Float.valueOf(f);
                acharsequence[2] = String.format("%.02f", aobj);
                acharsequence[3] = "mi";
                Spannable spannable1 = (Spannable)TextUtils.replace(charsequence, as, acharsequence);
                int i1 = spannable1.toString().indexOf('-');
                spannable1.setSpan(new ForegroundColorSpan(0xff999999), i1 - 1, i1 + 1, 0);
                mNavBar.setDistance(spannable1);
                mRouteOverlay = new DefaultRouteOverlay(this, route);
                mMapOverlays.add(0, mRouteOverlay);
                clearPlacePins();
                mNavPinsFake.addOverlays(route.getStart(), route.getEnd());
                final PIMapOverlayItem startPin = mNavPinsFake.getStartPin();
                Runnable runnable = new Runnable() {

                    public void run()
                    {
                        mController.animateTo(startPin.getLocation());
                    }

                }
;
                if(mPIMapView.getCurrentZoneIndex() != startPin.getZoneIndex())
                {
                    mRunnableQueue.offer(runnable);
                    mController.loadZone(startPin.getZoneIndex());
                    return;
                } else
                {
                    runnable.run();
                    return;
                }
            }
            routepoint = route.getPoint(j);
            Log.d(TAG, (new StringBuilder("point #")).append(j).append(": ").append(routepoint.getName()).append(" (").append(routepoint.getPixelX()).append(", ").append(routepoint.getPixelY()).append(") ").append("in zone ").append(routepoint.getZoneUUID()).toString());
            j++;
        } while(true);
    }

    public static PIMapVenue establishCurrentVenue(Intent intent)
    {
        String s = intent.getStringExtra("venue_uuid");
        PIMapVenue pimapvenue;
        if(s == null)
            pimapvenue = PointInside.getInstance().getCurrentVenue();
        else
            pimapvenue = PointInside.getInstance().loadVenue(s);
        if(pimapvenue == null)
            pimapvenue = PointInside.getInstance().loadVenue(PointInside.getInstance().getCurrentPIMapVenueUUID());
        if(pimapvenue == null)
            throw new IllegalStateException("venue is not loaded");
        else
            return pimapvenue;
    }

    private void exitNavMode()
    {
        clearPopup();
        mNavMode = false;
        mNavBar.setVisibility(8);
        mMapOverlays.remove(mRouteOverlay);
        mNavPinsFake.clear();
        mRouteOverlay = null;
    }

    private PIMapOverlayItem findPinForPlace(String s)
    {
        List list = mMapOverlays;
        PointInsideMapOverlay apointinsidemapoverlay[] = new PointInsideMapOverlay[6];
        apointinsidemapoverlay[0] = mDealPins;
        apointinsidemapoverlay[1] = mServicePins;
        apointinsidemapoverlay[2] = mDealPins;
        apointinsidemapoverlay[3] = mNavPinsFake.mStartPin;
        apointinsidemapoverlay[4] = mNavPinsFake.mEndPin;
        apointinsidemapoverlay[5] = mPlacePins;
        return findPinForPlaceInOverlays(s, list, apointinsidemapoverlay);
    }

    private static PIMapOverlayItem findPinForPlaceInOverlays(String s, List list, PointInsideMapOverlay apointinsidemapoverlay[])
    {
        int i;
        int j;
        i = apointinsidemapoverlay.length;
        j = 0;
        PIMapOverlayItem pimapoverlayitem = null;
        for(;j < i;) {

        PointInsideMapOverlay pointinsidemapoverlay;
        int k;
        int l;
        pointinsidemapoverlay = apointinsidemapoverlay[j];
        k = pointinsidemapoverlay.size();
        l = 0;
        for(;l<k;)
        {
            pimapoverlayitem = pointinsidemapoverlay.getItem(l);
            if(s.equals(pimapoverlayitem.getUUID())) {
                    return pimapoverlayitem;
            }
            l++;

        }
        j++;
       }
       return null;
    }

    private PIMapOverlayItem getOverlayItemForPlace(String s)
    {
        List list = mPIMapView.getPlace(s);
        if(list.size() > 0)
            return (PIMapOverlayItem)list.get(0);
        else
            return null;
    }

    private void goToMe()
    {
        final PIMapLocation userLoc = mLocationOverlay.getLocation();
        PIMapVenueZone pimapvenuezone = locateUserZoneByLocation(userLoc);
        if(pimapvenuezone != null)
        {
            if(pimapvenuezone.getZoneIndex() == mPIMapView.getCurrentZoneIndex())
            {
                mController.animateTo(userLoc);
                return;
            } else
            {
                mRunnableQueue.offer(new Runnable() {

                    public void run()
                    {
                        mPIMapView.runOnMapReady( new Runnable() {

                            public void run()
                            {
                                mController.animateTo(userLoc);
                            }

                        }
);
                    }
                }
);
                mController.loadZone(pimapvenuezone.getZoneIndex());
                return;
            }
        } else
        {
            Toast.makeText(this, 0x7f060096, 0).show();
            return;
        }
    }

    private void handleSomeOfTheIntent(Intent intent)
    {
        mFilterHelper.setVenue(mPIMapVenue);
        mFilterHelper.setZone(mPIMapView.getCurrentZoneIndex(), mPIMapView.isOverview());
        if("com.pointinside.android.app.action.SHOW_RESULTS".equals(intent.getAction()))
        {
            mFilterWheel.collapse();
            showResults((Uri)intent.getParcelableExtra("results_uri"));
        } else
        if("com.pointinside.android.app.action.SHOW_PIN".equals(intent.getAction()))
        {
            mFilterWheel.collapse();
            showUUID(intent.getStringExtra("place_uuid"));
            return;
        }
    }

    private void hideDeals()
    {
        clearDealPins();
    }

    private void loadOverviewZoneOverlays()
    {
        (new Thread() {

            public void run()
            {
            	android.os.Process.setThreadPriority(10);
                mOverviewPins.clear();
                mMapOverlays.remove(mOverviewPins);
                if(!mPIMapView.isOverview())
                	return;
                List list = mPIMapView.getHotlinksForOverview();
                if(list == null || list.size() <= 0)
                	return;

                Iterator iterator = list.iterator();
                for(;iterator.hasNext();) {
                        PIMapOverlayItem pimapoverlayitem = (PIMapOverlayItem)iterator.next();
                        mOverviewPins.addOverlay(pimapoverlayitem);
                }

                runOnUiThread(new Runnable() {

                    public void run()
                    {
                        clearPlacePins();
                        mOverviewPins.populateOverlays();
                        mMapOverlays.add(mOverviewPins);
                    }
                }
               );
            }

        }
).start();
    }

    private PIMapVenueZone locateUserZoneByLocation(PIMapLocation pimaplocation)
    {
        int i = pimaplocation.getExtras().getInt(AbstractLocationEngine.LOCATION_EXTRAS_ZONE_INDEX_KEY, -1);
        if(i != -1)
            return whichZoneByIndex(i);
        double d = pimaplocation.getLatitude();
        double d1 = pimaplocation.getLongitude();
        if(PIMapLocation.isLocationInZone(mPIMapView.getCurrentZone(), d, d1))
            return mPIMapView.getCurrentZone();
        else
            return whichZoneIsLocationIn(d, d1);
    }

    private void prepareForZoneChange()
    {
        clearOverviewPins();
        clearPlacePins();
        clearDealPins();
        clearServicePins();
    }

    private void requestShowRoute(com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint, com.pointinside.android.api.nav.Route.RouteEndpoint routeendpoint1, final String endName)
    {
        if(mNavMode)
            exitNavMode();
        mCalculateRouteTask = new CalculateRouteTask(mPIMapVenue.getVenueUUID(), routeendpoint, routeendpoint1);
        
    	DetachableAsyncTask.TaskCallbacks<Route, Void> mRouteTaskCallback = 
    			new DetachableAsyncTask.TaskCallbacks<Route,Void>() {

            protected void onPostExecute(Route route)
            {
                mCalculatingRouteDialog.dismiss();
                mCalculateRouteTask = null;
                if(route == null)
                {
                    Toast.makeText(VenueMapActivity.this, 0x7f060062, 0).show();
                    return;
                } else
                {
                    enterNavModeWithRoute(route, endName);
                    return;
                }
            }
    /*
            protected void onPostExecute(Object obj)
            {
                onPostExecute((Route)obj);
            }
    */            
        };
        
        mCalculateRouteTask.setCallback(mRouteTaskCallback);
        
        mCalculatingRouteDialog = ProgressDialog.show(this, null, getString(0x7f060061), true, true, new android.content.DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialoginterface)
            {
                if(mCalculateRouteTask != null)
                {
                    mCalculateRouteTask.clearCallback();
                    mCalculateRouteTask = null;
                }
            }

        }
);
        mCalculateRouteTask.execute(new Void[0]);
    }

    private void runRunnables()
    {
        do
        {
            if(mRunnableQueue.isEmpty())
                return;
            ((Runnable)mRunnableQueue.remove()).run();
        } while(true);
    }

    private void showDeals()
    {
        DealsService.loadDestinationDeals(this, mDealLoadReceiver, null, mPIMapVenue.getVenueUUID());
    }

    private void showOverviewPopup(final PIMapOverlayItem overlay)
    {
        if(mPopupLayout != null)
            mPIMapView.removeView(mPopupLayout);
        mPopupLayout = (LinearLayout)mLayoutInflater.inflate(0x7f03001e, null);
        int i = getResources().getDimensionPixelSize(0x7f090002);
        int j = mPIMapView.getChildCount();
        com.pointinside.android.api.maps.PIMapView.LayoutParams layoutparams = new com.pointinside.android.api.maps.PIMapView.LayoutParams(-2, -2, overlay.getLocation(), 0, i, 81);
        mPIMapView.addView(mPopupLayout, j, layoutparams);
        mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040008));
        TextView textview = (TextView)mPopupLayout.findViewById(0x1020016);
        textview.setText(overlay.getTitle());
        textview.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                prepareForZoneChange();
                mController.loadSpecialArea(overlay.getSpecialAreaId());
            }

        }
);
        centerMapOnItem(overlay, 0, i);
    }

    private void showPopup(final PIMapOverlayItem overlay)
    {
        if(mPopupLayout != null)
            mPIMapView.removeView(mPopupLayout);
        mPopupLayout = (LinearLayout)mLayoutInflater.inflate(R.layout.pi_pointer_view, null);
        mPopupLayout.setTag(overlay);
        mPopupLayout.findViewById(0x7f0e0018).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view1)
            {
                mFilterWheel.collapse();
                mDialogHelperListener.showNavPointSelectionDialog(overlay.getUUID(), overlay.getTitle());
            }
        }
);
        View view = mPopupLayout.findViewById(0x7f0e0036);
        int i;
        int j;
        com.pointinside.android.api.maps.PIMapView.LayoutParams layoutparams;
        TextView textview;
        String s;
        if(overlay.getPlaceTypeId() != 101 && !overlay.isWormhole())
        {
            view.setVisibility(0);
            mPopupLayout.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    if(overlay instanceof PIMapDeal)
                    {
                        PlaceDetailActivity.showOnFeaturedTab(VenueMapActivity.this, overlay.getUUID(), mPIMapVenue.getVenueUUID());
                        return;
                    } else
                    {
                        startPlaceDetailActivity(overlay.getUUID());
                        return;
                    }
                }
            }
);
        } else
        {
            view.setVisibility(8);
            mPopupLayout.setOnClickListener(null);
        }
        i = getResources().getDimensionPixelSize(0x7f090001) + overlay.getMarkerBounds().top;
        j = mPIMapView.getChildCount();
        layoutparams = new com.pointinside.android.api.maps.PIMapView.LayoutParams(-2, -2, overlay.getLocation(), 0, i, 81);
        mPIMapView.addView(mPopupLayout, j, layoutparams);
        mPopupLayout.startAnimation(AnimationUtils.loadAnimation(this, 0x7f040008));
        ((TextView)mPopupLayout.findViewById(0x1020016)).setText(overlay.getTitle());
        textview = (TextView)mPopupLayout.findViewById(0x1020010);
        s = overlay.getSubTitle();
        if(s != null && s.trim().length() > 0)
        {
            textview.setVisibility(0);
            textview.setText(s);
            textview.requestFocus();
        } else
        {
            textview.setVisibility(8);
        }
        centerMapOnItem(overlay, 0, i);
    }

    private void showResults(Uri uri)
    {
        Cursor cursor;
        try {
        String s = mPIMapVenue.getVenueUUID();
        ContentResolver contentresolver = getContentResolver();
        String as[] = {
            "place_uuid"
        };
        String as1[] = new String[2];
        as1[0] = String.valueOf(1);
        as1[1] = s;
        cursor = contentresolver.query(uri, as, "venue_proximity = ? AND venue_uuid = ?", as1, null);
        ArrayList arraylist = new ArrayList();
        PIMapOverlayItem pimapoverlayitem;
        for(;cursor.moveToNext();) {
            String s1;
            List list;
            s1 = cursor.getString(0);
            list = mPIMapView.getPlace(s1);
            if(list != null)
            {
                pimapoverlayitem = (PIMapOverlayItem)list.get(0);
                if(pimapoverlayitem.getZoneIndex() == mPIMapView.getCurrentZoneIndex()) {
                    arraylist.add(pimapoverlayitem);
                    continue;
                }
                Log.d(TAG, (new StringBuilder("Filtering out placeUUID=")).append(s1).append(" (on zone ").append(pimapoverlayitem.getZoneIndex()).append(")").toString());
                continue;
            }
            if(!list.isEmpty()) {
            }
            Log.w(TAG, (new StringBuilder("No place matching placeUUID=")).append(s1).append(" from search resultsUri=").append(uri).toString());

        }

        if(arraylist.size() == 0) {
        	Toast.makeText(this, "No results from this venue", 1).show();
        } else {
            Iterator iterator;
            clearPlacePins();
            iterator = arraylist.iterator();
            for(;iterator.hasNext();) {
                PIMapOverlayItem pimapoverlayitem1 = (PIMapOverlayItem)iterator.next();
                mPlacePins.addOverlay(pimapoverlayitem1);
                mPlacePins.populateOverlays();
                pimapoverlayitem1.setProjection(mPIMapView);
            }
            mMapOverlays.add(mPlacePins);

        }

        cursor.close();
        return;
    } catch(Exception exception) {
        exception.printStackTrace();
    }
//        cursor.close();
//        throw exception;
    }

    private void showUUID(String s)
    {
        int i = mPIMapView.getCurrentZoneIndex();
        final PIMapOverlayItem item = getOverlayItemForPlace(s);
        if(item != null)
        {
            dismissPopup();
            int j = item.getZoneIndex();
            boolean flag = false;
            if(j != i)
                flag = true;
            Runnable runnable = new Runnable() {

                public void run()
                {
                    clearPlacePins();
                    mPlacePins.addOverlay(item);
                    mPlacePins.populateOverlays();
                    mMapOverlays.add(mPlacePins);
                    item.setProjection(mPIMapView);
                    mPlacePins.dispatchTap(item);
                }

            }
;
            if(flag)
            {
                mRunnableQueue.offer(runnable);
                mController.loadZone(item.getZoneIndex());
                return;
            } else
            {
                runnable.run();
                return;
            }
        } else
        {
            Toast.makeText(this, "Place not found", 0).show();
            return;
        }
    }

    public static void showWithResult(Context context, String s, String s1)
    {
        assertCurrentVenue(s);
        Intent intent = new Intent("com.pointinside.android.app.action.SHOW_PIN", null, context, VenueMapActivity.class);
        intent.putExtra("place_uuid", s1);
        intent.addFlags(0x4000000);
        context.startActivity(intent);
    }

    public static void showWithResults(Context context, String s, Uri uri)
    {
        assertCurrentVenue(s);
        Intent intent = new Intent("com.pointinside.android.app.action.SHOW_RESULTS", null, context, VenueMapActivity.class);
        intent.putExtra("results_uri", uri);
        intent.addFlags(0x4000000);
        context.startActivity(intent);
    }

    private boolean showingPopupForPlace(String s)
    {
        if(mPopupLayout != null && s != null)
        {
            PIMapOverlayItem pimapoverlayitem = (PIMapOverlayItem)mPopupLayout.getTag();
            if(pimapoverlayitem != null && s.equals(pimapoverlayitem.getUUID()))
                return true;
        }
        return false;
    }

    private void startPlaceDetailActivity(String s)
    {
        PlaceDetailActivity.show(this, s, mPIMapVenue.getVenueUUID());
    }

    private void toggleDeals()
    {
        boolean flag = PointInside.getInstance().showDealMode();
        PointInside pointinside;
        boolean flag1;
        ActionBarHelper actionbarhelper;
        boolean flag2;
        if(flag)
            hideDeals();
        else
            showDeals();
        pointinside = PointInside.getInstance();
        if(flag)
            flag1 = false;
        else
            flag1 = true;
        pointinside.setShowDealMode(flag1);
        actionbarhelper = mActionBarHelper;
        flag2 = false;
        if(!flag)
            flag2 = true;
        actionbarhelper.setDealsButtonOn(flag2);
    }

    private void updateMapPadding()
    {
        int i = getResources().getDimensionPixelSize(R.dimen.zoom_controls_padding_right);
        int j = getResources().getDimensionPixelSize(R.dimen.zoom_controls_padding_bottom);
        int k = getResources().getDimensionPixelSize(R.dimen.base_map_padding);
        int l = k;
        if(mFilterWheel.isExpanded())
        {
            l += getResources().getDimensionPixelSize(R.dimen.extra_map_padding_for_filter_wheel);
            j += getResources().getDimensionPixelSize(R.dimen.zoom_controls_extra_padding_bottom_for_filter_wheel);
        }
        mPIMapView.setMapPadding(k, k, k, l);
        mPIMapView.setZoomControlsPadding(0, 0, i, j);
    }

    private void updateZone()
    {
        String s = mPIMapView.getCurrentZoneName();
        String s1 = mPIMapView.getCurrentAreaName();
        if(s1 == null)
            setTitle(s);
        else
            setTitle(s1);
        mFilterHelper.setZone(mPIMapView.getCurrentZoneIndex(), mPIMapView.isOverview());
    }

    private PIMapVenueZone whichZoneByIndex(int i)
    {
        try {
    	PIMapZoneDataCursor pimapzonedatacursor;
        pimapzonedatacursor = mPIMapVenue.getVenueZones();
        if(pimapzonedatacursor == null)
            return null;
        boolean flag = true;
        while(true) {
	        PIMapVenueZone pimapvenuezone;
	        int j;
	        pimapvenuezone = pimapzonedatacursor.getPIMapVenueZone(this);
	        j = pimapvenuezone.getZoneIndex();
	        if(j == i)
	        {
	            pimapzonedatacursor.close();
	            return pimapvenuezone;
	        }
	        flag = pimapzonedatacursor.moveToNext();
        }

        } catch(Exception exception) {
        	exception.printStackTrace();
        }
        return null;
//        pimapzonedatacursor.close();
//        throw exception;
    }

    private PIMapVenueZone whichZoneIsLocationIn(double d, double d1)
    {
    	try {
	        PIMapZoneDataCursor pimapzonedatacursor;
	        pimapzonedatacursor = mPIMapVenue.getVenueZones();
	        if(pimapzonedatacursor == null)
	            return null;
	        boolean flag1 = true;
	        while(true) {
		        PIMapVenueZone pimapvenuezone;
		        boolean flag;
		        pimapvenuezone = pimapzonedatacursor.getPIMapVenueZone(this);
		        flag = PIMapLocation.isLocationInZone(pimapvenuezone, d, d1);
		        if(flag)
		        {
		            pimapzonedatacursor.close();
		            return pimapvenuezone;
		        }
		        flag1 = pimapzonedatacursor.moveToNext();
	        }

	    } catch(Exception exception) {
	        exception.printStackTrace();
	    }
    	return null;
    }

    public void onBackPressed()
    {
        if(mPIMapView.hasOverview() && !mPIMapView.isOverview())
        {
            mController.loadOverview();
            return;
        } else
        {
            finish();
            return;
        }
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
        if(configuration.orientation == 2)
            mFilterWheel.collapse();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Intent intent = getIntent();
        mPIMapVenue = establishCurrentVenue(intent);
        setContentView(R.layout.pi_map);
        
        mDealLoadReceiver = new DetachableResultReceiver(new Handler());
        mQueryHandler = new QueryHandler();
        mDialogHelper = DialogCompatHelper.newInstance(this);
        mDialogHelperListener = new DialogHelperListener(null);
        mDialogHelper.setOnCreateDialogListener(mDialogHelperListener);
        mDialogHelper.setOnPrepareDialogListener(mDialogHelperListener);
        mLayoutInflater = LayoutInflater.from(this);
        PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", mPIMapVenue.getVenueId(), PITouchstreamContract.TouchstreamType.VISIT_VENUE_ID);
        mPIMapView = (PIMapView)findViewById(R.id.pimap_view);
        mPIMapView.setVenue(mPIMapVenue);
        mPIMapView.setUseBuiltInZoom(true);
        mTitle = (TextView)findViewById(R.id.txt_title);
        mTitle.setText(mPIMapVenue.getVenueName());
        mNavBar = (NavigationBar)findViewById(R.id.nav_bar);
        mNavBar.setOnExitClickListener(mExitNavClicked);
        mZone = (TextView)findViewById(R.id.txt_zone);
        mFilterWheel = (FilterWheel)findViewById(R.id.filter_wheel);
        mFilterWheel.setOnFilterEventListener(mFilterEventHandler);
        mFilterHelper = new InVenueFilterHelper(mFilterWheel);
        mController = mPIMapView.getController();
        updateMapPadding();
        mActionBarHelper.setDealsButtonOn(PointInside.getInstance().showDealMode());
        mPIMapView.setOnZoneChangeListener(new PIMapView.OnZoneChangeListener() {

            public void onZoneChange(PIMapView pimapview)
            {
                runRunnables();
                loadOverviewZoneOverlays();
                updateZone();
                if(mNavMode)
                    mNavPinsFake.updateCurrentZone(mPIMapView.getCurrentZoneIndex());
                if(PointInside.getInstance().showDealMode())
                    showDeals();
                PIMapVenueZone pimapvenuezone = mPIMapView.getCurrentZone();
                PointInside.getInstance().setViewport(pimapvenuezone.getPoint1Latitude(), pimapvenuezone.getPoint1Longitude(), pimapvenuezone.getPoint4Latitude(), pimapvenuezone.getPoint4Longitude());
            }

        }
);
        mPIMapView.setOnPlaceClickListener(new PIMapView.OnPlaceClickListener() {

            public void onPlaceClicked(PIMapLandmarkDataCursor.PIMapPlace pimapplace)
            {
                if(mNavPickerMode)
                {
                    mDialogHelperListener.resumeNavPointSelectionDialog(mNavPickerWhichEndpoint, pimapplace.getUUID(), pimapplace.getTitle());
                    mNavPickerMode = false;
                    return;
                }
                if(showingPopupForPlace(pimapplace.getUUID()))
                {
                    dismissPopup();
                    return;
                }
                PITouchstreamContract.addEvent(VenueMapActivity.this, PointInside.getInstance().getUserLocation(), pimapplace.getUUID(), mPIMapVenue.getVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.MAP_TAP_PLACE);
                PIMapOverlayItem pimapoverlayitem = findPinForPlace(pimapplace.getUUID());
                if(pimapoverlayitem != null)
                {
                    showPopup(pimapoverlayitem);
                    return;
                } else
                {
                    showUUID(pimapplace.getUUID());
                    return;
                }
            }
        }
);
        mPIMapView.setOnZoomChangedListener(new PIMapView.OnZoomChangedListener() {

            public void onZoomChanged(float f)
            {
            }

        }
);
        mMapOverlays = mPIMapView.getOverlays();
        
        mLocationOverlay = new MyLocationOverlay(this, mPIMapView, R.drawable.location, R.drawable.compass) {
            public void onLocationChanged(Location location)
            {
                super.onLocationChanged(location);
                PointInside.getInstance().setUserLocation(location);
            }
        };
        
        mMapOverlays.add(mLocationOverlay);
        mPlacePins = new PopupShowingPlaceOverlay(getResources().getDrawable(R.drawable.marker));
        mServicePins = new PopupShowingPlaceOverlay(getResources().getDrawable(R.drawable.marker));
        mDealPins = new PopupShowingPlaceOverlay(getResources().getDrawable(R.drawable.marker_general_deals));
        mNavPinsFake = new NavPinsFakeOverlay();
        mOverviewPins = new BaseMapPlaceOverlay(getResources().getDrawable(R.drawable.zone_annotation)) {

            public boolean onSingleTapUp(MotionEvent motionevent, PIMapView pimapview)
            {
                boolean flag = super.onSingleTapUp(motionevent, pimapview);
                if(!flag)
                    dismissPopup();
                return flag;
            }

            protected boolean onTap(int i)
            {
                PIMapOverlayItem pimapoverlayitem = getItem(i);
                showOverviewPopup(pimapoverlayitem);
                return true;
            }

        }
;
        handleSomeOfTheIntent(intent);
        mHandler.postDelayed(new Runnable() {

            public void run()
            {
                if(!isFinishing())
                    mPIMapVenue.loadOrUpdatePromotionImages(mDownloadObserver);
            }

        }
, 500L);
        if(PointInside.getInstance().showDealMode())
            showDeals();

    }

    protected Dialog onCreateDialog(int i)
    {
        return mDialogHelper.onCreateDialog(i, null);
    }

    protected Dialog onCreateDialog(int i, Bundle bundle)
    {
        return mDialogHelper.onCreateDialog(i, bundle);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(0x7f0d0006, menu);
        return mActionBarHelper.onCreateOptionsMenu(menu);
    }

    protected void onDestroy()
    {
        if(mPIMapVenue != null)
            mPIMapVenue.unRegisterPIVenueDownloadObserver();
        if(mCalculateRouteTask != null)
            mCalculateRouteTask.clearCallback();
        mLocationOverlay.disableCompass();
        mLocationOverlay.disableMyLocation();
        super.onDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(!PIDataUtils.isEclairOrGreater() && i == 4 && keyevent.getRepeatCount() == 0)
            onBackPressed();
        return super.onKeyDown(i, keyevent);
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        PIMapVenue pimapvenue = establishCurrentVenue(intent);
        if(pimapvenue != mPIMapVenue)
        {
            mPIMapVenue = pimapvenue;
            mPIMapView.setVenue(pimapvenue);
        }
        handleSomeOfTheIntent(intent);
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
//            GoogleMapActivity.reportDealsToggled(this, mPIMapVenue.getVenueId());
            toggleDeals();
            return true;

        case 2131624057:
            mLocationOverlay.runOnFirstFix(mGoToMe);
            return true;

        case 2131624051:
            startActivity(new Intent(this, FeedbackActivity.class));
            return true;

        case 2131624050:
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
    }

    protected void onPause()
    {
        mDealLoadReceiver.clearReceiver();
        mLocationOverlay.disableCompass();
        mLocationOverlay.disableMyLocation();
        super.onPause();
    }

    protected void onPostCreate(Bundle bundle)
    {
        super.onPostCreate(bundle);
        mActionBarHelper.onPostCreate(bundle);
    }

    protected void onPrepareDialog(int i, Dialog dialog)
    {
        mDialogHelper.onPrepareDialog(i, dialog, null);
    }

    protected void onPrepareDialog(int i, Dialog dialog, Bundle bundle)
    {
        mDialogHelper.onPrepareDialog(i, dialog, bundle);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return mActionBarHelper.onPrepareOptionsMenu(menu);
    }

    protected void onRestoreInstanceState(Bundle bundle)
    {
        super.onRestoreInstanceState(bundle);
        mDialogHelper.onRestoreInstanceState(bundle);
    }

    protected void onResume()
    {
        super.onResume();
        PointInside.getInstance().setCurrentVenueUUID(mPIMapVenue.getVenueUUID());
        mDealLoadReceiver.setReceiver(mDealLoadHandler);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass();
        mActionBarHelper.setDealsButtonOn(PointInside.getInstance().showDealMode());
    }

    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        mDialogHelper.onSaveInstanceState(bundle);
    }

    public boolean onSearchRequested()
    {
        return PlaceSearchActivity.showSearch(this);
    }

    public void setTitle(CharSequence charsequence)
    {
        super.setTitle(charsequence);
        mZone.setText(charsequence);
    }

    private static final String TAG = VenueMapActivity.class.getSimpleName();
    private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
    private CalculateRouteTask mCalculateRouteTask;
    private ProgressDialog mCalculatingRouteDialog;
    private PIMapController mController;
    private final com.pointinside.android.app.util.DetachableResultReceiver.Receiver mDealLoadHandler = new com.pointinside.android.app.util.DetachableResultReceiver.Receiver() {

        public void onReceiveResult(int i, Bundle bundle)
        {
            Log.d(VenueMapActivity.TAG, (new StringBuilder("onReceiveResult: resultCode=")).append(i).toString());
            bundle.getBoolean("cached-result");
            String s = bundle.getString("error-text");
            Uri uri = (Uri)bundle.getParcelable("result-uri");
            switch(i)
            {
            default:
                throw new IllegalArgumentException((new StringBuilder("Unknown resultCode: ")).append(i).toString());

            case 2: // '\002'
                break;
            }
            if(s != null)
            {
                Log.d(VenueMapActivity.TAG, (new StringBuilder("Error getting destination deals: ")).append(s).toString());
                return;
            } else
            {
                mQueryHandler.startQuery(1, uri, uri, null, null, null, null);
                return;
            }
        }

    }
;
    private DetachableResultReceiver mDealLoadReceiver;
    private PopupShowingPlaceOverlay mDealPins;
    private DialogCompatHelper mDialogHelper;
    private DialogHelperListener mDialogHelperListener;
    private com.pointinside.android.api.PIMapVenue.PIVenueDownloadObserver mDownloadObserver;
    private final android.view.View.OnClickListener mExitNavClicked = new android.view.View.OnClickListener() {

        public void onClick(View view)
        {
            exitNavMode();
        }

    }
;
    private final FilterEventHandler mFilterEventHandler = new FilterEventHandler(null);
    private InVenueFilterHelper mFilterHelper;
    private FilterWheel mFilterWheel;
    private final Runnable mGoToMe = new Runnable() {

        public void run()
        {
            goToMe();
        }

    }
;
    private final Handler mHandler = new Handler();
    private LayoutInflater mLayoutInflater;
    private MyLocationOverlay mLocationOverlay;
    private List<PIMapOverlay> mMapOverlays;
    private NavigationBar mNavBar;
    private boolean mNavMode;
    private boolean mNavPickerMode;
    private int mNavPickerWhichEndpoint;
    private NavPinsFakeOverlay mNavPinsFake;
    private BaseMapPlaceOverlay mOverviewPins;
    private PIMapVenue mPIMapVenue;
    private PIMapView mPIMapView;
    private PopupShowingPlaceOverlay mPlacePins;
    private LinearLayout mPopupLayout;
    private QueryHandler mQueryHandler;
    private DefaultRouteOverlay mRouteOverlay;
    private final LinkedList<Runnable> mRunnableQueue = new LinkedList<Runnable>();
    private PopupShowingPlaceOverlay mServicePins;
    private TextView mTitle;
    private TextView mZone;
}
