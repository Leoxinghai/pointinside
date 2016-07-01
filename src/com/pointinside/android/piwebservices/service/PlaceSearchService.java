// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.piwebservices.service;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import com.pointinside.android.piwebservices.net.PlaceSearchClient;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.util.BatchProcessorHelper;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.pointinside.android.piwebservices.service:
//            AbstractRESTServiceHelper

public class PlaceSearchService extends AbstractRESTServiceHelper
{

    public PlaceSearchService()
    {
        super(TAG);
        registerMethodHandler("com.pointinside.android.app.service.PlaceSearchService.GET_PLACES", 1, mGetPlacesHandler);
    }

    private static int clampAndConvertRadius(double d)
    {
        return (int)Math.min(500D, Math.max(1.0D, d));
    }

    private static void deletePlaceSearchResults(ContentResolver contentresolver, BatchProcessorHelper batchprocessorhelper, Uri uri, long l)
        throws OperationApplicationException, RemoteException
    {
        try {
	    	Cursor cursor = contentresolver.query(uri, null, (new StringBuilder("timestamp < ")).append(l).toString(), null, null);
	        boolean flag = cursor.moveToNext();
	        while(flag)
	        {
	            long l1 = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
	            batchprocessorhelper.add(ContentProviderOperation.newDelete(uri).withSelection((new StringBuilder("_id = ")).append(l1).toString(), null).build());
	            batchprocessorhelper.add(ContentProviderOperation.newDelete(com.pointinside.android.piwebservices.provider.PIWebServicesContract.PlacesResults.makeResultsUriById(l1)).build());
	            batchprocessorhelper.runWhenThresholdReached();
	            flag = cursor.moveToNext();
	        }

          cursor.close();
          return;
	    } catch(Exception exception) {
	        exception.printStackTrace();
	    }
    }

    public static void startSearch(Context context, ResultReceiver resultreceiver, Bundle bundle, String s, int i, String s1, double d,
            double d1, double d2)
    {
        Intent intent = new Intent("com.pointinside.android.app.service.PlaceSearchService.GET_PLACES", null, context, PlaceSearchService.class);
        intent.putExtra("result-receiver", resultreceiver);
        intent.putExtra("extras", bundle);
        if(s != null)
            intent.putExtra("query", s);
        else
            intent.putExtra("category_id", i);
        intent.putExtra("inVenue", s1);
        intent.putExtra("latitude", d);
        intent.putExtra("longitude", d1);
        intent.putExtra("radius", d2);
        context.startService(intent);
    }

    protected void onResultsCleanup()
        throws RemoteException, OperationApplicationException
    {
        long l = System.currentTimeMillis() - 0xa4cb800L;
        BatchProcessorHelper batchprocessorhelper = new BatchProcessorHelper(getContentResolver(), PIWebServicesContract.getAuthority());
        deletePlaceSearchResults(getContentResolver(), batchprocessorhelper, com.pointinside.android.piwebservices.provider.PIWebServicesContract.PlacesRequests.CONTENT_URI, l);
        batchprocessorhelper.runBatch();
    }

    public static final String ACTION_GET_PLACES = "com.pointinside.android.app.service.PlaceSearchService.GET_PLACES";
    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_IN_VENUE = "inVenue";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_QUERY = "query";
    public static final String EXTRA_RADIUS = "radius";
    public static final int RESULT_GET_PLACES = 1;
    private static final String TAG = PlaceSearchService.class.getSimpleName();
    private final AbstractRESTServiceHelper.MethodHandler mGetPlacesHandler = new AbstractRESTServiceHelper.SimpleMethodHandler() {

        protected Uri onExecuteMethod(Intent intent)
            throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException
        {
            com.pointinside.android.piwebservices.net.PlaceSearchClient.PlacesRequest placesrequest = new com.pointinside.android.piwebservices.net.PlaceSearchClient.PlacesRequest();
            com.pointinside.android.piwebservices.net.PlaceSearchClient.PlacesResult placesresult;
            ArrayList arraylist;
            Iterator iterator;
            if(intent.hasExtra("query"))
                placesrequest.query = intent.getStringExtra("query");
            else
                placesrequest.categoryServerId = intent.getIntExtra("category_id", -1);
            placesrequest.inVenue = intent.getStringExtra("inVenue");
            placesrequest.latitude = intent.getDoubleExtra("latitude", 0.0D);
            placesrequest.longitude = intent.getDoubleExtra("longitude", 0.0D);
            placesrequest.radius = PlaceSearchService.clampAndConvertRadius(intent.getDoubleExtra("radius", 0.0D));
            placesrequest.setOutputFields(new String[] {
                "title", "lat", "long", "venueProximity", "rating", "description", "phone", "websiteUrl", "websiteLabel", "hours",
                "address"
            });
            placesresult = PlaceSearchClient.getInstance(PlaceSearchService.this).getPlaces(placesrequest);
            arraylist = new ArrayList();
            arraylist.add(ContentProviderOperation.newInsert(com.pointinside.android.piwebservices.provider.PIWebServicesContract.PlacesRequests.CONTENT_URI).withValue("query", placesrequest.query).withValue("category_id", Integer.valueOf(placesrequest.categoryServerId)).withValue("latitude", Double.valueOf(placesrequest.latitude)).withValue("longitude", Double.valueOf(placesrequest.longitude)).withValue("radius", Integer.valueOf(placesrequest.radius)).withValue("timestamp", Long.valueOf(System.currentTimeMillis())).build());
            iterator = placesresult.places.iterator();
            do
            {
                if(!iterator.hasNext())
                    return com.pointinside.android.piwebservices.provider.PIWebServicesContract.PlacesResults.makeResultsUriByRequest(getContentResolver().applyBatch(PIWebServicesContract.getAuthority(), arraylist)[0].uri);
                com.pointinside.android.piwebservices.net.PlaceSearchClient.Place place = (com.pointinside.android.piwebservices.net.PlaceSearchClient.Place)iterator.next();
                android.content.ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(com.pointinside.android.piwebservices.provider.PIWebServicesContract.PlacesResults.CONTENT_URI).withValueBackReference("request_id", 0).withValue("title", place.getTitle()).withValue("latitude", Double.valueOf(place.getLatitude())).withValue("longitude", Double.valueOf(place.getLongitude())).withValue("place_uuid", place.getId("pi")).withValue("urbanq_id", place.getId("urbanQ"));
                if(place.hasWebUrl())
                    builder.withValue("web_url", place.getWebUrl());
                if(place.hasWebLabel())
                    builder.withValue("web_label", place.getWebLabel());
                if(place.hasRawHours())
                    builder.withValue("raw_hours", place.getRawHours());
                if(place.hasStreet())
                    builder.withValue("STREET", place.getStreet());
                if(place.hasCity())
                    builder.withValue("city", place.getCity());
                if(place.hasState())
                    builder.withValue("state", place.getState());
                if(place.hasZip())
                    builder.withValue("zip", place.getZip());
                if(place.hasDescription())
                    builder.withValue("description", place.getDescription());
                if(place.hasPhoneNumber())
                    builder.withValue("phone", place.getPhoneNumber());
                if(place.hasProximityVenueRelationship())
                    builder.withValue("venue_proximity", Integer.valueOf(place.getProximityVenueRelationship())).withValue("venue_uuid", place.getProximityVenueUUID());
                if(place.hasHoursOpenMonday())
                    builder.withValue("hours_open_monday", place.getHoursOpenMonday());
                if(place.hasHoursCloseMonday())
                    builder.withValue("hours_close_monday", place.getHoursCloseMonday());
                if(place.hasHoursOpenTuesday())
                    builder.withValue("hours_open_tuesday", place.getHoursOpenTuesday());
                if(place.hasHoursCloseTuesday())
                    builder.withValue("hours_close_tuesday", place.getHoursCloseTuesday());
                if(place.hasHoursOpenWednesday())
                    builder.withValue("hours_open_wednesday", place.getHoursOpenWednesday());
                if(place.hasHoursCloseWednesday())
                    builder.withValue("hours_close_wednesday", place.getHoursCloseWednesday());
                if(place.hasHoursOpenThursday())
                    builder.withValue("hours_open_thursday", place.getHoursOpenThursday());
                if(place.hasHoursCloseThursday())
                    builder.withValue("hours_close_thursday", place.getHoursCloseThursday());
                if(place.hasHoursOpenFriday())
                    builder.withValue("hours_open_friday", place.getHoursOpenFriday());
                if(place.hasHoursCloseFriday())
                    builder.withValue("hours_close_friday", place.getHoursCloseFriday());
                if(place.hasHoursOpenSaturday())
                    builder.withValue("hours_open_saturday", place.getHoursOpenSaturday());
                if(place.hasHoursCloseSaturday())
                    builder.withValue("hours_close_saturday", place.getHoursCloseSaturday());
                if(place.hasHoursOpenSunday())
                    builder.withValue("hours_open_sunday", place.getHoursOpenSunday());
                if(place.hasHoursCloseSunday())
                    builder.withValue("hours_close_sunday", place.getHoursCloseSunday());
                arraylist.add(builder.build());
            } while(true);
        }

        final PlaceSearchService this$0;


            {
                this$0 = PlaceSearchService.this;
//                super();
            }
    }
;


}
