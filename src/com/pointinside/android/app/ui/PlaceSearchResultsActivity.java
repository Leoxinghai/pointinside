package com.pointinside.android.app.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DetachableResultReceiver;
import com.pointinside.android.app.util.DetachableResultReceiver.Receiver;
import com.pointinside.android.app.util.DistanceUtils;
import com.pointinside.android.app.util.LatLongRect;
//import com.pointinside.android.app.util.LocationHelper;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.service.PlaceSearchService;
import java.util.ArrayList;

public class PlaceSearchResultsActivity
  extends ListActivity
  implements DetachableResultReceiver.Receiver, View.OnClickListener
{
  private static final String EXTRA_CATEGORY_ID = "category_id";
  private static final String EXTRA_REQUEST_LAT = "search_lat";
  private static final String EXTRA_REQUEST_LONG = "search_long";
  private static final String EXTRA_REQUEST_RADIUS = "radius";
  private static final String EXTRA_REQUEST_VENUE = "venue";
  private static final int REQUEST_CODE_SHOW_VENUE_MAP = 1;
  private static final String TAG = PlaceSearchResultsActivity.class.getSimpleName();
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private PlacesAdapter mAdapter;
  private TextView mNoResultsView;
  private QueryHandler mQueryHandler;
  private DetachableResultReceiver mReceiver;
  private EditText mSearchBox;
  private ProgressDialog mSearchingDialog;
  private boolean mShouldQuery;

  private void cancelSearch()
  {
    this.mQueryHandler.cancelOperation(1);
    dismissSearchingDialog();
  }

  private void dismissSearchingDialog()
  {
    if (this.mSearchingDialog != null)
    {
      this.mSearchingDialog.dismiss();
      this.mSearchingDialog = null;
    }
  }

  private Bundle getRequestLocation(Bundle bundle)
  {
	  return null;
	  /*
      LatLongRect latlongrect = LocationHelper.getMapArea();
      if(latlongrect == null)
      {
          Log.w(TAG, "Unknown search area, selecting an arbitrary location in the US");
          latlongrect = new LatLongRect(47.544091000000002D - 1.0D, -122.200928D - 1.0D, 1.0D + 47.544091000000002D, 1.0D - 122.200928D);
      }
      float af[] = new float[1];
      Location.distanceBetween(latlongrect.getLat1(), latlongrect.getLong1(), latlongrect.getLat2(), latlongrect.getLong2(), af);
      float f = DistanceUtils.kilometersToMiles(af[0] / 2.0F / 1000F);
      Log.d(TAG, (new StringBuilder("Running search around ll=")).append(latlongrect.getLatitudeCenter()).append(",").append(latlongrect.getLongitudeCenter()).append(" with radius=").append(f).toString());
      String s = PointInside.getInstance().getCurrentVenueUUID();
      double d;
      double d1;
      if(s != null)
          Log.d(TAG, (new StringBuilder(" (in venue ")).append(s).append(")").toString());
      else
          Log.d(TAG, " (in outdoor view)");
      d = latlongrect.getLatitudeCenter();
      d1 = latlongrect.getLongitudeCenter();
      bundle.putString("venue", s);
      bundle.putDouble("radius", f);
      bundle.putDouble("search_lat", d);
      bundle.putDouble("search_long", d1);
      return bundle;
      */
  }


  private void handleIntent(Intent paramIntent)
  {
    if ("android.intent.action.SEARCH".equals(paramIntent.getAction()))
    {
      if (this.mSearchingDialog == null) {
        handleSearch();
      }
    }
    else {
      return;
    }
    Log.w(TAG, "Ignoring search intent because another search is running.");
  }

  private void handleSearch()
  {
      Intent intent = getIntent();
      int i = -1;
      String s;
      com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType touchstreamtype;
      String s1;
      Bundle bundle;
      if(intent.hasExtra("query"))
      {
          s1 = intent.getStringExtra("query");
          s = s1;
          touchstreamtype = com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.REMOTE_SEARCH_BY_KEYWORD;
      } else
      {
          i = getIntent().getIntExtra("category_id", -1);
          if(i == -1)
              throw new IllegalStateException();
          s = PlaceBrowseActivity.getCategoryString(getResources(), i);
          touchstreamtype = com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.REMOTE_SEARCH_BY_CATEGORY;
          s1 = null;
      }
      PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), s, PointInside.getInstance().getCurrentVenueId(), touchstreamtype);
      mSearchBox.setText(s);
      showProgressDialog(s);
      bundle = getRequestLocation(new Bundle());
      PlaceSearchService.startSearch(this, mReceiver, bundle, s1, i, bundle.getString("venue"), bundle.getDouble("search_lat"), bundle.getDouble("search_long"), bundle.getDouble("radius"));
  }

  public static void sendSearch(Context paramContext, int paramInt)
  {
    Intent localIntent = new Intent("android.intent.action.SEARCH", null, paramContext, PlaceSearchResultsActivity.class);
    localIntent.addFlags(268435456);
    localIntent.putExtra("category_id", paramInt);
    paramContext.startActivity(localIntent);
  }

  public static void sendSearch(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEARCH", null, paramContext, PlaceSearchResultsActivity.class);
    localIntent.addFlags(268435456);
    localIntent.putExtra("query", paramString);
    paramContext.startActivity(localIntent);
  }

  private void showProgressDialog(final String paramString)
  {
    this.mSearchingDialog = ProgressDialog.show(this, null, TextUtils.replace(getText(2131099738), new String[] { "%s" }, new CharSequence[] { paramString }), true, true);
    this.mSearchingDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramAnonymousDialogInterface)
      {
        PlaceSearchResultsActivity.this.finish();
      }
    });
    this.mSearchingDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
    {
      public void onDismiss(DialogInterface paramAnonymousDialogInterface)
      {
        if (!PlaceSearchResultsActivity.this.mAdapter.resultsLoaded()) {
          return;
        }
        if (PlaceSearchResultsActivity.this.mAdapter.getCount() == 0)
        {
          PlaceSearchResultsActivity.this.getListView().setVisibility(8);
          TextView localTextView = PlaceSearchResultsActivity.this.mNoResultsView;
          CharSequence localCharSequence = PlaceSearchResultsActivity.this.getText(2131099739);
          String[] arrayOfString = { "%s" };
          CharSequence[] arrayOfCharSequence = new CharSequence[1];
          arrayOfCharSequence[0] = paramString;
          localTextView.setText(TextUtils.replace(localCharSequence, arrayOfString, arrayOfCharSequence));
          PlaceSearchResultsActivity.this.mNoResultsView.setVisibility(0);
          return;
        }
        PlaceSearchResultsActivity.this.getListView().setVisibility(0);
        PlaceSearchResultsActivity.this.mNoResultsView.setText("");
        PlaceSearchResultsActivity.this.mNoResultsView.setVisibility(8);
      }
    });
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (paramInt1 == 1))
    {
      startActivity(new Intent(this, VenueMapActivity.class));
      finish();
    }
  }

  public void onClick(View view)
  {
      switch(view.getId())
      {
      default:
          return;

      case 2131624003:
          String s = PointInside.getInstance().getCurrentVenueUUID();
          if(s != null)
          {
              VenueMapActivity.showWithResults(this, s, mAdapter.getResultsUri());
              return;
          } else
          {
              Toast.makeText(this, "Google Map display not yet implemented", 1).show();
              return;
          }

      case 2131623993:
          onSearchRequested();
          return;
      }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903082);
    this.mActionBarHelper.setActionBarTitle(getTitle());
    this.mSearchBox = ((EditText)findViewById(2131623993));
    this.mSearchBox.setOnClickListener(this);
    findViewById(2131624003).setOnClickListener(this);
    this.mNoResultsView = ((TextView)findViewById(2131623954));
    if (PointInside.getInstance().getCurrentVenue() != null) {}
    for (String str1 = PointInside.getInstance().getCurrentVenue().getVenueName();; str1 = null)
    {
      String str2 = null;
      if (str1 != null) {
        str2 = PointInside.getInstance().getCurrentVenueUUID();
      }
      this.mAdapter = new PlacesAdapter(this, str2, str1);
      setListAdapter(this.mAdapter);
      this.mReceiver = new DetachableResultReceiver(new Handler());
      this.mQueryHandler = new QueryHandler();
      handleIntent(getIntent());
      return;
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    dismissSearchingDialog();
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    Place localPlace = (Place)paramListView.getItemAtPosition(paramInt);
    String str1;
    String str2;
    if (localPlace != null)
    {
      str1 = localPlace.venueUUID;
      str2 = localPlace.placeUUID;
      if (localPlace.venueRelationship == 3) {
        VenueDownloadActivity.loadVenueForResult(this, 1, str1, false, false);
      }
    }
    else
    {
      return;
    }
    boolean bool1 = TextUtils.isEmpty(str2);
    boolean bool2 = false;
    if (!bool1)
    {
      boolean bool3 = TextUtils.isEmpty(str1);
      bool2 = false;
      if (!bool3)
      {
        boolean bool4 = str1.equals(PointInside.getInstance().getCurrentVenueUUID());
        bool2 = false;
        if (bool4) {
          bool2 = true;
        }
      }
    }
    PlaceDetailActivity.showPlaceFromSearch(this, PIWebServicesContract.PlacesResults.makeResultsUriById(Long.valueOf(localPlace.resultId).longValue()), localPlace.urbanQId, str2, str1, bool2);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
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
    super.onPause();
    this.mReceiver.clearReceiver();
    if (!this.mAdapter.resultsLoaded())
    {
      dismissSearchingDialog();
      this.mShouldQuery = true;
    }
  }

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.mActionBarHelper.onPostCreate(paramBundle);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return this.mActionBarHelper.onPrepareOptionsMenu(paramMenu);
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
          break;
      }
      if(s != null)
      {
          dismissSearchingDialog();
          Toast.makeText(this, (new StringBuilder("Error: ")).append(s).toString(), 1).show();
          finish();
          return;
      } else
      {
          Bundle bundle1 = (Bundle)bundle.getParcelable("extras");
          ResultContainer resultcontainer = new ResultContainer();
          resultcontainer.resultUri = uri;
          resultcontainer.requestLatitude = bundle1.getDouble("search_lat");
          resultcontainer.requestLongitude = bundle1.getDouble("search_long");
          mQueryHandler.startQuery(1, resultcontainer, uri, null, null, null, null);
          return;
      }
  }

  protected void onResume()
  {
    super.onResume();
    this.mReceiver.setReceiver(this);
    if (this.mShouldQuery)
    {
      handleSearch();
      this.mShouldQuery = false;
    }
  }

  public boolean onSearchRequested()
  {
    finish();
    return PlaceSearchActivity.showSearch(this, this.mSearchBox.getText().toString());
  }

  private static class Place
  {
    public String description;
    public String distanceLabel;
    public String placeUUID;
    public long resultId;
    public String title;
    public String urbanQId;
    public int venueRelationship;
    public String venueUUID;

    public boolean isVenue()
    {
      return (this.placeUUID == null) && (this.venueUUID != null);
    }
  }

  private static class PlaceHolder
  {
    public TextView distance;
    public TextView text1;
    public TextView text2;

    public PlaceHolder(View paramView)
    {
      this.text1 = ((TextView)paramView.findViewById(16908308));
      this.text2 = ((TextView)paramView.findViewById(16908309));
      this.distance = ((TextView)paramView.findViewById(2131623980));
    }
  }

  private static class PlacesAdapter
    extends BaseAdapter
  {
    private static final int VIEW_TYPE_IN_VENUE_EMPTY = 2;
    private static final int VIEW_TYPE_PLACE = 1;
    private static final int VIEW_TYPE_SEPARATOR =0;
    private final Context mContext;
    private final ArrayList<PlaceSearchResultsActivity.Place> mInVenue = new ArrayList();
    private int mInVenueEmptyPos = -1;
    public String mInVenueName;
    private int mInVenueSeparatorPos = -1;
    private String mInVenueUUID;
    private final LayoutInflater mInflater;
    private boolean mLoaded;
    private final ArrayList<PlaceSearchResultsActivity.Place> mNearby = new ArrayList();
    private int mNearbySeparatorPos = -1;
    private Uri mResultUri;

    public PlacesAdapter(Context paramContext, String paramString1, String paramString2)
    {
      this.mContext = paramContext;
      this.mInflater = LayoutInflater.from(paramContext);
      this.mInVenueUUID = paramString1;
      this.mInVenueName = paramString2;
    }

    private void bindInVenueEmpty(int paramInt, View paramView)
    {
      ((TextView)paramView).setText(2131099769);
    }

    private void bindPlace(Place place, boolean flag, View view)
    {
        byte byte0 = 8;
        PlaceHolder placeholder = (PlaceHolder)view.getTag();
        placeholder.text1.setText(place.title);
        TextView textview;
        if(!TextUtils.isEmpty(place.description))
        {
            placeholder.text2.setText(place.description);
            placeholder.text2.setVisibility(0);
        } else
        {
            placeholder.text2.setText("");
            placeholder.text2.setVisibility(byte0);
        }
        textview = placeholder.distance;
        if(!flag)
            byte0 = 0;
        textview.setVisibility(byte0);
        if(!flag)
            placeholder.distance.setText(place.distanceLabel);
    }

    private void bindSeparator(int i, View view)
    {
        String s;
        if(i == mInVenueSeparatorPos)
        {
            Context context = getContext();
            Object aobj[] = new Object[1];
            aobj[0] = mInVenueName;
            s = context.getString(0x7f060078, aobj);
        } else
        {
            s = getContext().getString(0x7f060077);
        }
        ((TextView)view).setText(s);
    }


    private void bindView(int i, View view)
    {
        getItem(i);
        int j;
        boolean flag;
        switch(getItemViewType(i))
        {
        default:
            return;

        case 0: // '\0'
            bindSeparator(i, view);
            return;

        case 2: // '\002'
            bindInVenueEmpty(i, view);
            return;

        case 1: // '\001'
            j = mInVenueSeparatorPos;
            flag = false;
            break;
        }
        if(j >= 0)
            if(mNearbySeparatorPos != -1 && i >= mNearbySeparatorPos)
                flag = false;
            else
                flag = true;
        bindPlace(getItem(i), flag, view);
    }

    private Context getContext()
    {
      return this.mContext;
    }

    private View newView(int i, ViewGroup viewgroup)
    {
        View view;
        switch(getItemViewType(i))
        {
        default:
            throw new IllegalStateException();

        case 0: // '\0'
            return mInflater.inflate(0x7f030029, viewgroup, false);

        case 2: // '\002'
            return mInflater.inflate(0x7f030028, viewgroup, false);

        case 1: // '\001'
            view = mInflater.inflate(0x7f030027, viewgroup, false);
            break;
        }
        view.setTag(new PlaceHolder(view));
        return view;
    }

    public boolean areAllItemsEnabled()
    {
      return false;
    }

    public int getCount()
    {
      int i = this.mInVenue.size() + this.mNearby.size();
      if (this.mInVenueSeparatorPos >= 0) {
        i++;
      }
      if (this.mInVenueEmptyPos >= 0) {
        i++;
      }
      if (this.mNearbySeparatorPos >= 0) {
        i++;
      }
      return i;
    }

    public PlaceSearchResultsActivity.Place getItem(int paramInt)
    {
      if ((paramInt == this.mInVenueSeparatorPos) || (paramInt == this.mInVenueEmptyPos) || (paramInt == this.mNearbySeparatorPos)) {
        return null;
      }
      if ((this.mInVenueSeparatorPos >= 0) && (paramInt > this.mInVenueSeparatorPos) && (paramInt <= this.mInVenueSeparatorPos + this.mInVenue.size())) {
        return (PlaceSearchResultsActivity.Place)this.mInVenue.get(-1 + (paramInt - this.mInVenueSeparatorPos));
      }
      if ((this.mNearbySeparatorPos >= 0) && (paramInt > this.mNearbySeparatorPos)) {
        return (PlaceSearchResultsActivity.Place)this.mNearby.get(-1 + (paramInt - this.mNearbySeparatorPos));
      }
      return (PlaceSearchResultsActivity.Place)this.mNearby.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if ((paramInt == this.mInVenueSeparatorPos) || (paramInt == this.mNearbySeparatorPos)) {
        return 0;
      }
      if (paramInt == this.mInVenueEmptyPos) {
        return 2;
      }
      return 1;
    }

    public Uri getResultsUri()
    {
      return this.mResultUri;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView != null) {}
      for (View localView = paramView;; localView = newView(paramInt, paramViewGroup))
      {
        bindView(paramInt, localView);
        return localView;
      }
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public boolean isEnabled(int paramInt)
    {
      return getItemViewType(paramInt) == 1;
    }

    public void loadResults(ResultContainer resultcontainer, Cursor cursor)
    {
        mResultUri = resultcontainer.resultUri;
        mLoaded = true;
        int i;
        if(mInVenueUUID != null)
            i = 0;
        else
            i = -1;
        mInVenueSeparatorPos = i;
        mInVenue.clear();
        mNearbySeparatorPos = -1;
        mNearby.clear();
        do
        {
            if(!cursor.moveToNext())
            {
                cursor.close();
                if(mInVenueUUID != null)
                {
                    boolean flag3 = mInVenue.isEmpty();
                    boolean flag4 = false;
                    if(flag3)
                    {
                        flag4 = true;
                        mInVenueEmptyPos = 1;
                    }
                    if(!mNearby.isEmpty())
                    {
                        mNearbySeparatorPos = 1 + mInVenue.size();
                        if(flag4)
                            mNearbySeparatorPos = 1 + mNearbySeparatorPos;
                    }
                }
                notifyDataSetChanged();
                return;
            }
            Place place = new Place();
            place.resultId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            place.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            place.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            place.placeUUID = cursor.getString(cursor.getColumnIndexOrThrow("place_uuid"));
            place.urbanQId = cursor.getString(cursor.getColumnIndexOrThrow("urbanq_id"));
            int j = cursor.getColumnIndexOrThrow("venue_proximity");
            boolean flag = cursor.isNull(j);
            boolean flag1 = false;
            if(!flag)
            {
                String s = cursor.getString(cursor.getColumnIndexOrThrow("venue_uuid"));
                int k = cursor.getInt(j);
                flag1 = false;
                if(k == 1)
                {
                    String s1 = mInVenueUUID;
                    flag1 = false;
                    if(s1 != null)
                    {
                        boolean flag2 = s.equals(mInVenueUUID);
                        flag1 = false;
                        if(flag2)
                            flag1 = true;
                    }
                }
                place.venueUUID = s;
                place.venueRelationship = k;
            }
//            if(!flag1)
//                place.distanceLabel = LocationHelper.getUserDistanceFromLabel(getContext(), cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")), cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
            if(flag1)
                mInVenue.add(place);
            else
                mNearby.add(place);
        } while(true);
    }

    public boolean resultsLoaded()
    {
      return this.mLoaded;
    }
  }

  private class QueryHandler
    extends AsyncQueryHandler
  {
    public static final int TOKEN_GET_PLACES = 1;

    public QueryHandler()
    {
      super(getContentResolver());
    }

    protected void onQueryComplete(int i, Object obj, Cursor cursor)
    {
        Log.d(PlaceSearchResultsActivity.TAG, (new StringBuilder("onQueryComplete: token=")).append(i).toString());
        switch(i)
        {
        default:
            throw new IllegalArgumentException((new StringBuilder("Unknown token: ")).append(i).toString());

        case 1: // '\001'
            mAdapter.loadResults((ResultContainer)obj, cursor);
            break;
        }
        dismissSearchingDialog();
    }

  }

  private static class ResultContainer
  {
    public double requestLatitude;
    public double requestLongitude;
    public Uri resultUri;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.PlaceSearchResultsActivity
 * JD-Core Version:    0.7.0.1
 */