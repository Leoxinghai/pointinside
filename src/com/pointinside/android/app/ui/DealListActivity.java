// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.ui;

import android.app.ListActivity;
import android.content.*;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DetachableResultReceiver;
import com.pointinside.android.app.widget.DealBar;
//import com.pointinside.android.app.widget.DealsAdapter;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.service.DealsService;

// Referenced classes of package com.pointinside.android.app.ui:
//            DealDetailActivity, DealSubActivity, AboutActivity, FeedbackActivity,
//            PlaceSearchActivity

public class DealListActivity extends ListActivity
    implements com.pointinside.android.app.util.DetachableResultReceiver.Receiver
{
    private class QueryHandler extends AsyncQueryHandler
    {

        protected void onQueryComplete(int i, Object obj, Cursor cursor)
        {
            Log.d(DealListActivity.TAG, (new StringBuilder("onQueryComplete: token=")).append(i).toString());
            switch(i)
            {
            default:
                throw new IllegalArgumentException((new StringBuilder("Unknown token: ")).append(i).toString());

            case 1: // '\001'
//                mDealsAdapter.loadResults((com.pointinside.android.app.widget.DealsAdapter.ResultContainer)obj, cursor);
                break;
            }
        }

        public static final int TOKEN_GET_NEARBY_DEALS_BY_LOCATION = 1;
        final DealListActivity this$0;

        public QueryHandler()
        {
            super(getContentResolver());
            this$0 = DealListActivity.this;
        }
    }


    public DealListActivity()
    {
        mIsLoading = false;
        mToggleViewListener = new com.pointinside.android.app.widget.DealBar.ToggleViewListener() {

            public void onToggle(int i)
            {
                switch(i)
                {
                default:
                    return;

                case 2131623950:
                    finish();
                    break;
                }
            }

        }
;
    }

    private void checkLoading()
    {
        if(mIsLoading)
            setDealsLoading(true);
    }

    private void handleIntent(Intent intent)
    {
        Bundle bundle;
        long l;
        intent.getAction();
        bundle = intent.getBundleExtra("extras");
        l = bundle.getLong("request_id");
        if(l != -1L) {
        Uri uri = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriGroupByLocation(l);
        mQueryHandler.startQuery(1, null, uri, null, null, null, "distance");
        } else {
        double d = bundle.getDouble("req_lat", -1D);
        double d1 = bundle.getDouble("req_long", -1D);
        int i = bundle.getInt("req_radius", -1);
        if(d > 0.0D && d1 > 0.0D && i > 0)
        {
            setDealsLoading(true);
            DealsService.loadNearbyDeals(this, mReceiver, bundle, d, d1, i);
        } else
        if(PointInside.getInstance().getUserLocation() != null)
        {
            setDealsLoading(true);
            Location location = PointInside.getInstance().getUserLocation();
            double d2 = location.getLatitude();
            double d3 = location.getLongitude();
            bundle.putDouble("req_lat", d2);
            bundle.putDouble("req_long", d3);
            bundle.putInt("req_radius", 5);
            DealsService.loadNearbyDeals(this, mReceiver, bundle, d2, d3, 5);
        }
       }
        PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", PointInside.getInstance().getCurrentVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SHOW_DEALS_LIST);
        return;
    }

    private void setDealsLoading(boolean flag)
    {
        mIsLoading = flag;
        if(mDealBar != null)
            mDealBar.showProgress(flag);
    }

    public static void show(Context context, long l, Bundle bundle)
    {
        Intent intent = new Intent(context, DealListActivity.class);
        if(bundle == null)
            bundle = new Bundle();
        bundle.putLong("request_id", l);
        intent.putExtra("extras", bundle);
        context.startActivity(intent);
    }

    public static void showVenueDeals(Context context, long l, String s)
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f03000f);
//        mDealsAdapter = new DealsAdapter(this);
//        setListAdapter(mDealsAdapter);
        mReceiver = new DetachableResultReceiver(new Handler());
        mQueryHandler = new QueryHandler();
        handleIntent(getIntent());
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(0x7f0d0001, menu);
        MenuItem menuitem = menu.findItem(0x7f0e0075);
        if(menuitem != null)
            mDealBar = (DealBar)menuitem.getActionView();
        else
            mDealBar = (DealBar)findViewById(0x7f0e000c);
        mDealBar.show();
        mDealBar.setChecked(0x7f0e000f);
        mDealBar.setToggleViewListener(mToggleViewListener);
        checkLoading();
        return mActionBarHelper.onCreateOptionsMenu(menu);
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

    protected void onListItemClick(ListView listview, View view, int i, long l)
    {
        Cursor cursor;
        long l1;
        double d;
        double d1;
        String s;
        int j;
        cursor = (Cursor)listview.getItemAtPosition(i);
        l1 = cursor.getLong(cursor.getColumnIndexOrThrow("request_id"));
        d = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
        d1 = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
        s = cursor.getString(cursor.getColumnIndexOrThrow("organization"));
        j = 1;
        int k = cursor.getInt(cursor.getColumnIndexOrThrow("deal_count"));
        j = k;
        try {
	        if(j == 1)
	        {
	            DealDetailActivity.show(this, com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultUri(l));
	            return;
	        } else
	        {
	            DealSubActivity.show(this, s, l1, d, d1);
	            return;
	        }
        } catch(Exception exception) {
        	exception.printStackTrace();;
        }
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(getIntent());
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId())
        {
        default:
            return false;

        case 2131624052:
            onSearchRequested();
            return true;

        case 2131624050:
            startActivity(new Intent(this, AboutActivity.class));
            return true;

        case 2131624051:
            startActivity(new Intent(this, FeedbackActivity.class));
            return true;
        }
    }

    protected void onPause()
    {
        super.onPause();
        mReceiver.clearReceiver();
    }

    protected void onPostCreate(Bundle bundle)
    {
        super.onPostCreate(bundle);
        mActionBarHelper.onPostCreate(bundle);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return mActionBarHelper.onPrepareOptionsMenu(menu);
    }

    public void onReceiveResult(int i, Bundle bundle)
    {
        Log.d(TAG, (new StringBuilder("onReceiveResult: resultCode=")).append(i).toString());
        String s = bundle.getString("error-text");
        Uri uri = (Uri)bundle.getParcelable("result-uri");
        switch(i)
        {
        default:
            throw new IllegalArgumentException((new StringBuilder("Unknown resultCode: ")).append(i).toString());

        case 1: // '\001'
            setDealsLoading(false);
            break;
        }
        if(s != null)
        {
            Toast.makeText(this, (new StringBuilder("Unable to fetch deals: ")).append(s).toString(), 1).show();
            return;
        } else
        {
            Bundle bundle1 = (Bundle)bundle.getParcelable("extras");
/*
            com.pointinside.android.app.widget.DealsAdapter.ResultContainer resultcontainer = new com.pointinside.android.app.widget.DealsAdapter.ResultContainer();
            resultcontainer.resultUri = uri;
            resultcontainer.requestLatitude = bundle1.getDouble("req_lat");
            resultcontainer.requestLongitude = bundle1.getDouble("req_long");
            */
            Uri uri1 = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriGroupByLocation(uri);
//            mQueryHandler.startQuery(1, resultcontainer, uri1, null, null, null, "distance");
            return;
        }
    }

    protected void onResume()
    {
        super.onResume();
        mReceiver.setReceiver(this);
    }

    public boolean onSearchRequested()
    {
        return PlaceSearchActivity.showSearch(this);
    }

    private static final String EXTRA_EXTRAS = "extras";
    private static final String REQUEST_ID_EXTRA = "request_id";
    private static final String REQUEST_TYPE_EXTRA = "request_type";
    private static final String TAG = DealListActivity.class.getSimpleName();
    private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
    private DealBar mDealBar;
//    private DealsAdapter mDealsAdapter;
    private ListView mDealsList;
    private boolean mIsLoading;
    private QueryHandler mQueryHandler;
    private DetachableResultReceiver mReceiver;
    private com.pointinside.android.app.widget.DealBar.ToggleViewListener mToggleViewListener;



}
