// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.piwebservices.service;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import com.pointinside.android.piwebservices.net.DealsClient;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.util.BatchProcessorHelper;
import java.util.*;

// Referenced classes of package com.pointinside.android.piwebservices.service:
//            AbstractRESTServiceHelper

public class DealsService extends AbstractRESTServiceHelper
{

    public DealsService()
    {
        super(TAG);
        registerMethodHandler("com.pointinside.android.app.service.DealsService.GET_NEARBY_DEALS", 1, mNearbyDealsHandler);
        registerMethodHandler("com.pointinside.android.app.service.DealsService.GET_DESTINATION_DEALS", 2, mDestinationDealsHandler);
    }

    private static void addInsertDealOps(ArrayList arraylist, Uri uri, ArrayList arraylist1, String s)
    {
        Iterator iterator = arraylist1.iterator();
        do
        {
            if(!iterator.hasNext())
                return;
            com.pointinside.android.piwebservices.net.DealsClient.Deal deal = (com.pointinside.android.piwebservices.net.DealsClient.Deal)iterator.next();
            android.content.ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.CONTENT_URI).withValue("request_type", s).withValue("title", deal.getTitle()).withValue("organization", deal.getPlaceName()).withValue("display_image", deal.getDisplayImage());
            String s1;
            if(uri != null)
                builder.withValue("request_id", Long.valueOf(ContentUris.parseId(uri)));
            else
                builder.withValueBackReference("request_id", 0);
            s1 = getDataSource(deal.getDataSources());
            builder.withValue("datasource", s1);
            builder.withValue("datasource_id", deal.getId(s1));
            if(deal.hasType())
                builder.withValue("type", deal.getType());
            if(deal.hasDescription())
                builder.withValue("description", deal.getDescription());
            if(deal.hasDisplayStartDate())
                builder.withValue("display_start_date", deal.getDisplayStartDate());
            if(deal.hasDisplayEndDate())
                builder.withValue("display_end_date", deal.getDisplayEndDate());
            if(deal.hasStartDate())
                builder.withValue("start_date", deal.getStartDate());
            if(deal.hasEndDate())
                builder.withValue("end_date", deal.getEndDate());
            if(deal.hasThumbnailImage())
                builder.withValue("thumbnail_image", deal.getThumbnailImage());
            if(deal.hasBrand())
                builder.withValue("brand", deal.getBrand());
            if(deal.hasDescription())
                builder.withValue("description", deal.getDescription());
            if(deal.hasLatLong())
            {
                builder.withValue("latitude", Double.valueOf(deal.getLatitude()));
                builder.withValue("longitude", Double.valueOf(deal.getLongitude()));
            }
            if(deal.hasPlaceUUID())
                builder.withValue("place_uuid", deal.getPlaceUUID());
            if(deal.hasVenueUUID())
                builder.withValue("venue_uuid", deal.getVenueUUID());
            if(deal.hasDistance())
                builder.withValue("distance", deal.getDistance());
            if(deal.hasCategory())
                builder.withValue("category", deal.getCategory());
            arraylist.add(builder.build());
        } while(true);
    }

    private static void deleteDealsResults(ContentResolver contentresolver, BatchProcessorHelper batchprocessorhelper, Uri uri, String s, long l)
        throws OperationApplicationException, RemoteException
    {
        try {
	        Cursor cursor = contentresolver.query(uri, null, (new StringBuilder("timestamp < ")).append(l).toString(), null, null);
	        boolean flag = cursor.moveToNext();
	        while(flag) {
		        long l1 = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
		        batchprocessorhelper.add(ContentProviderOperation.newDelete(uri).withSelection((new StringBuilder("_id = ")).append(l1).toString(), null).build());
		        batchprocessorhelper.add(ContentProviderOperation.newDelete(com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriByRequest(l1, s)).build());
		        batchprocessorhelper.runWhenThresholdReached();
		        flag = cursor.moveToNext();
	        }
	        cursor.close();
        } catch(Exception exception) {
	        //cursor.close();
	        //throw exception;
        }
    }

    private static String getDataSource(Set set)
    {
        if(set.size() != 1)
            throw new IllegalStateException("Deals should not have multiple data sources today");
        Iterator iterator = set.iterator();
        if(iterator.hasNext())
            return (String)iterator.next();
        else
            throw new AssertionError();
    }

    public static void loadDestinationDeals(Context context, ResultReceiver resultreceiver, Bundle bundle, String s)
    {
        Intent intent = new Intent("com.pointinside.android.app.service.DealsService.GET_DESTINATION_DEALS", null, context, DealsService.class);
        intent.putExtra("result-receiver", resultreceiver);
        intent.putExtra("extras", bundle);
        intent.putExtra("venue", s);
        context.startService(intent);
    }

    public static void loadNearbyDeals(Context context, ResultReceiver resultreceiver, Bundle bundle, double d, double d1, int i)
    {
        Intent intent = new Intent("com.pointinside.android.app.service.DealsService.GET_NEARBY_DEALS", null, context, DealsService.class);
        intent.putExtra("result-receiver", resultreceiver);
        intent.putExtra("extras", bundle);
        intent.putExtra("latitude", d);
        intent.putExtra("longitude", d1);
        intent.putExtra("radius", i);
        context.startService(intent);
    }

    protected void onResultsCleanup()
        throws RemoteException, OperationApplicationException
    {
        long l = System.currentTimeMillis() - 0xa4cb800L;
        BatchProcessorHelper batchprocessorhelper = new BatchProcessorHelper(getContentResolver(), PIWebServicesContract.getAuthority());
        deleteDealsResults(getContentResolver(), batchprocessorhelper, com.pointinside.android.piwebservices.provider.PIWebServicesContract.DestinationDealsRequests.CONTENT_URI, "destination", l);
        deleteDealsResults(getContentResolver(), batchprocessorhelper, com.pointinside.android.piwebservices.provider.PIWebServicesContract.NearbyDealsRequests.CONTENT_URI, "nearby", l);
        batchprocessorhelper.runBatch();
    }

    public static final String ACTION_GET_DESTINATION_DEALS = "com.pointinside.android.app.service.DealsService.GET_DESTINATION_DEALS";
    public static final String ACTION_GET_NEARBY_DEALS = "com.pointinside.android.app.service.DealsService.GET_NEARBY_DEALS";
    private static final long DESTINATION_DEALS_CACHE_LENGTH = 0x36ee80L;
    public static final String EXTRA_CACHED_RESULT = "cached-result";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    public static final String EXTRA_RADIUS = "radius";
    public static final String EXTRA_VENUE = "venue";
    public static final int RESULT_GET_DESTINATION_DEALS = 2;
    public static final int RESULT_GET_NEARBY_DEALS = 1;
    private static final String TAG = DealsService.class.getSimpleName();

    private final AbstractRESTServiceHelper.MethodHandler mDestinationDealsHandler = new AbstractRESTServiceHelper.MethodHandler() {

        private Uri queryRemoteService(Uri uri, String s)
            throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException
        {
            com.pointinside.android.piwebservices.net.DealsClient.DestinationDealsRequest destinationdealsrequest = new com.pointinside.android.piwebservices.net.DealsClient.DestinationDealsRequest();
            destinationdealsrequest.venue = s;
            com.pointinside.android.piwebservices.net.DealsClient.DealsResult dealsresult = DealsClient.getInstance(DealsService.this).getDestinationDeals(destinationdealsrequest);
            ArrayList arraylist = new ArrayList();
            android.content.ContentProviderOperation.Builder builder;
            ContentProviderResult acontentproviderresult[];
            if(uri != null)
                builder = ContentProviderOperation.newUpdate(uri);
            else
                builder = ContentProviderOperation.newInsert(com.pointinside.android.piwebservices.provider.PIWebServicesContract.DestinationDealsRequests.CONTENT_URI).withValue("venue", destinationdealsrequest.venue);
            builder.withValue("timestamp", Long.valueOf(System.currentTimeMillis()));
            arraylist.add(builder.build());
            if(uri != null)
                arraylist.add(ContentProviderOperation.newDelete(com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriByRequest(uri, "destination")).build());
            DealsService.addInsertDealOps(arraylist, uri, dealsresult.deals, "destination");
            acontentproviderresult = getContentResolver().applyBatch(PIWebServicesContract.getAuthority(), arraylist);
            if(uri == null)
                uri = acontentproviderresult[0].uri;
            return com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriByRequest(ContentUris.parseId(uri), "destination");
        }

        public void executeMethod(int i, Intent intent, Bundle bundle)
            throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException
        {
            ResultReceiver resultreceiver;
            String s;
            Cursor cursor;
            long l;
            try {
            resultreceiver = (ResultReceiver)intent.getParcelableExtra("result-receiver");
            s = intent.getStringExtra("venue");
            cursor = getContentResolver().query(com.pointinside.android.piwebservices.provider.PIWebServicesContract.DestinationDealsRequests.CONTENT_URI, null, "venue = ?", new String[] {
                s
            }, null);
            l = 0L;
            boolean flag = cursor.moveToFirst();
            Uri uri;
            Uri uri1;
            uri = null;
            uri1 = null;
            if(flag) {
            long l1;
            uri1 = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DestinationDealsRequests.makeRequestUri(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            uri = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriByRequest(uri1, "destination");
            l1 = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));
            l = l1;
            }
            cursor.close();
            if(resultreceiver != null && uri != null)
            {
                Bundle bundle1 = createResultData(intent);
                bundle1.putBoolean("cached-result", true);
                bundle1.putParcelable("result-uri", uri);
                resultreceiver.send(i, bundle1);
            }
            long l2 = System.currentTimeMillis();
            if(l == 0L || l2 > 0x36ee80L + l)
            {
                Log.d(DealsService.TAG, (new StringBuilder("Refreshing deals from remote service for venue=")).append(s).toString());
                Uri uri2 = queryRemoteService(uri1, s);
                if(resultreceiver != null)
                {
                    bundle.putParcelable("result-uri", uri2);
                    resultreceiver.send(i, bundle);
                }
            }
            return;
            } catch(Exception exception) {
            //exception;
            //cursor.close();
            //throw exception;
            }
        }

        final DealsService this$0;


            {
                this$0 = DealsService.this;
//                super();
            }
    }
;
    private final AbstractRESTServiceHelper.MethodHandler mNearbyDealsHandler = new AbstractRESTServiceHelper.SimpleMethodHandler() {

        protected Uri onExecuteMethod(Intent intent)
            throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException
        {
            DealsClient.NearbyDealsRequest nearbydealsrequest = new DealsClient.NearbyDealsRequest();
            nearbydealsrequest.latitude = intent.getDoubleExtra("latitude", 0.0D);
            nearbydealsrequest.longitude = intent.getDoubleExtra("longitude", 0.0D);
            nearbydealsrequest.radius = intent.getIntExtra("radius", 0);
            nearbydealsrequest.maxResults = 100;
            DealsClient.DealsResult dealsresult = DealsClient.getInstance(DealsService.this).getNearbyDeals(nearbydealsrequest);
            ArrayList arraylist = new ArrayList();
            arraylist.add(ContentProviderOperation.newInsert(PIWebServicesContract.NearbyDealsRequests.CONTENT_URI).withValue("latitude", Double.valueOf(nearbydealsrequest.latitude)).withValue("longitude", Double.valueOf(nearbydealsrequest.longitude)).withValue("radius", Integer.valueOf(nearbydealsrequest.radius)).withValue("timestamp", Long.valueOf(System.currentTimeMillis())).build());
            DealsService.addInsertDealOps(arraylist, null, dealsresult.deals, "nearby");
            return PIWebServicesContract.DealsResults.makeResultsUriByRequest(getContentResolver().applyBatch(PIWebServicesContract.getAuthority(), arraylist)[0].uri, "nearby");
        }

        final DealsService this$0;


            {
                this$0 = DealsService.this;
//                super();
            }
    }
;



}
