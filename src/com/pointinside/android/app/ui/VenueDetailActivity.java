package com.pointinside.android.app.ui;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.PIMapVenue.PIVenueDownloadObserver;
import com.pointinside.android.api.dao.PIMapOperationMinutesDataCursor;
import com.pointinside.android.api.dao.PIMapVenueDataCursor;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DateUtils;
import com.pointinside.android.app.util.DealsUtils;
import com.pointinside.android.app.util.DetachableResultReceiver;
import com.pointinside.android.app.util.DetachableResultReceiver.Receiver;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.service.DealsService;
import java.util.Calendar;

public class VenueDetailActivity
  extends Activity
{
  private static final String EXTRA_FEATURED_TAB_FIRST = "featured-tab-first";
  private static final String TAG = VenueDetailActivity.class.getSimpleName();
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private TextView mAddress;
  private Button mCallButton;
  private View mContactContainer;
  private final DetachableResultReceiver.Receiver mDealLoadHandler = new DetachableResultReceiver.Receiver()
  {
      public void onReceiveResult(int i, Bundle bundle)
      {
          Log.d(VenueDetailActivity.TAG, (new StringBuilder("onReceiveResult: resultCode=")).append(i).toString());
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
              Log.d(VenueDetailActivity.TAG, (new StringBuilder("Error getting destination deals: ")).append(s).toString());
              return;
          } else
          {
              mQueryHandler.startQuery(1, uri, uri, null, null, null, null);
              return;
          }
      }
  };
  private DetachableResultReceiver mDealLoadReceiver;
  private View mDescContainer;
  private TextView mDescTitle;
  private TextView mDescription;
  private RadioButton mDetailsButton;
  private PIMapVenue.PIVenueDownloadObserver mDownloadObserver = new PIMapVenue.PIVenueDownloadObserver()
  {
    public void venueImagesReady()
    {
      super.venueImagesReady();
      if (!VenueDetailActivity.this.isFinishing())
      {
        VenueDetailActivity.this.mVenueLogo = VenueDetailActivity.this.mPIMapVenueDataCursor.getLogo(VenueDetailActivity.this.mPIMapVenue);
        if (VenueDetailActivity.this.mVenueLogo != null) {
          VenueDetailActivity.this.mLogo.setImageBitmap(VenueDetailActivity.this.mVenueLogo);
        }
      }
    }
  };
  private FeaturedAdapter mFeaturedAdapter;
  private ListView mFeaturedList;
  private final Handler mHandler = new Handler();
  private TextView mHours;
  private TextView mHoursTitle;
  private Button mLessDescButton;
  private Button mLessHoursButton;
  private ImageView mLogo;
  private Button mMoreDescButton;
  private Button mMoreHoursButton;
  private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
  {
      private void showTab(int i)
      {
          switch(i)
          {
          default:
              throw new IllegalArgumentException((new StringBuilder("Unknown tab=")).append(i).toString());

          case 2131624011:
              mDescContainer.setVisibility(0);
              mPromoView.setVisibility(8);
              mContactContainer.setVisibility(8);
              return;

          case 2131624013:
              mDescContainer.setVisibility(8);
              mPromoView.setVisibility(0);
              mContactContainer.setVisibility(8);
              return;

          case 2131624012:
              mDescContainer.setVisibility(8);
              mPromoView.setVisibility(8);
              mContactContainer.setVisibility(0);
              return;
          }
      }

    public void onCheckedChanged(RadioGroup paramAnonymousRadioGroup, int paramAnonymousInt)
    {
      showTab(paramAnonymousInt);
    }
  };
  private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      Uri localUri = PIWebServicesContract.DealsResults.makeResultUri(paramAnonymousLong);
      DealDetailActivity.show(VenueDetailActivity.this, localUri);
    }
  };
  private PIMapOperationMinutesDataCursor mPIMapOperationMinutesDataCursor;
  private PIMapVenue mPIMapVenue;
  private PIMapVenueDataCursor mPIMapVenueDataCursor;
  private TextView mPhoneTitle;
  private View mPromoView;
  private QueryHandler mQueryHandler;
  private TextView mRatingTitle;
  private RadioGroup mTabGroup;
  private Bitmap mVenueLogo;
  private String mVenueUUID;
  private TextView mWebAddress;
  private TextView mWebsiteTitle;

  private void handleIntent(Intent paramIntent)
  {
    if (paramIntent.getBooleanExtra("featured-tab-first", false)) {
      this.mTabGroup.check(2131624013);
    }
  }

  private void hideDescriptionSection(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mDescTitle.setVisibility(8);
      this.mDescription.setVisibility(8);
      this.mMoreDescButton.setVisibility(8);
      this.mLessDescButton.setVisibility(8);
      return;
    }
    this.mDescTitle.setVisibility(0);
    this.mDescription.setVisibility(0);
    showMoreDescription(false);
  }

  private void hideHoursSection(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mMoreHoursButton.setVisibility(8);
      this.mLessHoursButton.setVisibility(8);
      this.mHoursTitle.setVisibility(8);
      this.mHours.setVisibility(8);
      return;
    }
    this.mHoursTitle.setVisibility(0);
    this.mHours.setVisibility(0);
    showMoreHours(false);
  }

  private void loadDeals()
  {
    DealsService.loadDestinationDeals(this, this.mDealLoadReceiver, null, this.mPIMapVenue.getVenueUUID());
  }

  private void populateHours()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.mPIMapOperationMinutesDataCursor != null)
    {
      String str = getString(2131099712);
      localStringBuilder.append(getString(2131099714));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getMondayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getMondayCloseMinute()));
      localStringBuilder.append("\n");
      localStringBuilder.append(getString(2131099715));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getTuesdayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getTuesdayCloseMinute()));
      localStringBuilder.append("\n");
      localStringBuilder.append(getString(2131099716));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getWednesdayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getWednesdayCloseMinute()));
      localStringBuilder.append("\n");
      localStringBuilder.append(getString(2131099717));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getThursdayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getThursdayCloseMinute()));
      localStringBuilder.append("\n");
      localStringBuilder.append(getString(2131099718));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getFridayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getFridayCloseMinute()));
      localStringBuilder.append("\n");
      localStringBuilder.append(getString(2131099719));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getSaturdayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getSaturdayCloseMinute()));
      localStringBuilder.append("\n");
      localStringBuilder.append(getString(2131099720));
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getSundayOpenMinute()));
      localStringBuilder.append(str);
      localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapOperationMinutesDataCursor.getSundayCloseMinute()));
      this.mHours.setText(localStringBuilder.toString());
    }
  }

  private void populateTodaysHours()
  {
      int i;
      StringBuilder stringbuilder;
      String s;
      i = Calendar.getInstance().get(7);
      stringbuilder = new StringBuilder();
      s = getString(0x7f060040);
      stringbuilder.append(getString(2131099713));
      switch(i) {
//      JVM INSTR tableswitch 1 7: default 80
  //                   1 92
  //                   2 131
  //                   3 170
  //                   4 209
  //                   5 248
  //                   6 287
  //                   7 326;
  //       goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8
default:
      return;
case 1:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getSundayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getSundayCloseMinute()));
      break;
case 2:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getMondayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getMondayCloseMinute()));
      break;
case 3:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getTuesdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getTuesdayCloseMinute()));
      break;
case 4:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getWednesdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getWednesdayCloseMinute()));
      break;
case 5:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getThursdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getThursdayCloseMinute()));
      break;
case 6:

      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getFridayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getFridayCloseMinute()));
      break;
case 7:

      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getSaturdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapOperationMinutesDataCursor.getSaturdayCloseMinute()));
      break;
     }
      mHours.setText(stringbuilder.toString());

  }

  private void populateViews()
  {
      mActionBarHelper.setActionBarTitle(mPIMapVenueDataCursor.getName());
      String s = mPIMapVenueDataCursor.getWebsite();
      String s1;
      String s2;
      final String phoneNumber;
      if(!TextUtils.isEmpty(s))
      {
          mWebAddress.setText(s);
      } else
      {
          mWebsiteTitle.setVisibility(8);
          mWebAddress.setVisibility(8);
      }
      s1 = (new StringBuilder(String.valueOf(mPIMapVenueDataCursor.getAddress1()))).append("\n").append(mPIMapVenueDataCursor.getFormattedCityStateZip()).toString();
      mAddress.setText(s1);
      s2 = mPIMapVenueDataCursor.getDescription();
      if(!TextUtils.isEmpty(s2))
      {
          mDescription.setText(s2);
          hideDescriptionSection(false);
      } else
      {
          hideDescriptionSection(true);
      }
      mVenueLogo = mPIMapVenueDataCursor.getLogo(mPIMapVenue);
      if(mVenueLogo != null)
          mLogo.setImageBitmap(mVenueLogo);
      else
      if(mPIMapVenueDataCursor.getVenueTypeId() == 2)
          mLogo.setImageResource(0x7f020009);
      mHandler.postDelayed(new Runnable() {

          public void run()
          {
              if(!isFinishing())
                  mPIMapVenue.loadOrUpdateVenueImages(mDownloadObserver);
          }
      }
, 500L);
      phoneNumber = mPIMapVenueDataCursor.getPhoneNumber();
      if(!TextUtils.isEmpty(phoneNumber))
      {
          mCallButton.setText(phoneNumber);
          mCallButton.setOnClickListener(new android.view.View.OnClickListener() {

              public void onClick(View view)
              {
                  Intent intent = new Intent("android.intent.action.DIAL", Uri.parse((new StringBuilder("tel:")).append(phoneNumber).toString()));
                  startActivity(intent);
              }

          }
);
      } else
      {
          mPhoneTitle.setVisibility(8);
          mCallButton.setVisibility(8);
      }
      showMoreHours(false);
      loadDeals();
  }

  public static void show(Context paramContext, String paramString)
  {
    show(paramContext, paramString, false);
  }

  private static void show(Context paramContext, String paramString, boolean paramBoolean)
  {
    Intent localIntent = new Intent(paramContext, VenueDetailActivity.class);
    localIntent.putExtra("venue_uuid", paramString);
    if (paramBoolean) {
      localIntent.putExtra("featured-tab-first", true);
    }
    paramContext.startActivity(localIntent);
  }

  private void showMap(String paramString)
  {
    Intent localIntent = new Intent(this, VenueMapActivity.class);
    localIntent.putExtra("venue_uuid", paramString);
    startActivity(localIntent);
  }

  private void showMoreDescription(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mMoreDescButton.setVisibility(8);
      this.mLessDescButton.setVisibility(0);
      this.mDescription.setMaxLines(200);
      return;
    }
    this.mMoreDescButton.setVisibility(0);
    this.mLessDescButton.setVisibility(8);
    this.mDescription.setMaxLines(3);
  }

  private void showMoreHours(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mMoreHoursButton.setVisibility(8);
      this.mLessHoursButton.setVisibility(0);
      populateHours();
      return;
    }
    this.mMoreHoursButton.setVisibility(0);
    this.mLessHoursButton.setVisibility(8);
    populateTodaysHours();
  }

  public static void showOnFeaturedTab(Context paramContext, String paramString)
  {
    show(paramContext, paramString, true);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903086);
    this.mVenueUUID = getIntent().getStringExtra("venue_uuid");
    if (this.mVenueUUID == null) {}
    for (this.mPIMapVenue = PointInside.getInstance().getCurrentVenue();; this.mPIMapVenue = PointInside.getInstance().loadVenue(this.mVenueUUID))
    {
      if (this.mPIMapVenue == null) {
        this.mPIMapVenue = PointInside.getInstance().loadVenue(PointInside.getInstance().getCurrentPIMapVenueUUID());
      }
      if (this.mPIMapVenue != null) {
        break;
      }
      throw new IllegalStateException("venue is not loaded");
    }
    if (!this.mPIMapVenue.isLoaded()) {
      this.mPIMapVenue = PointInside.getInstance().loadVenue(PointInside.getInstance().getCurrentPIMapVenueUUID());
    }
    if (!this.mPIMapVenue.isLoaded()) {
      throw new IllegalStateException("unable to load venue");
    }
    this.mPIMapVenueDataCursor = this.mPIMapVenue.getVenue();
    if (this.mPIMapVenueDataCursor == null) {
      throw new IllegalStateException("venue is not loaded");
    }
    PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", this.mPIMapVenue.getVenueId(), PITouchstreamContract.TouchstreamType.VISIT_VENUE_ID);
    this.mPIMapOperationMinutesDataCursor = this.mPIMapVenue.getOperationMinutes(this.mPIMapVenueDataCursor.getOperationMinutesId());
    this.mDealLoadReceiver = new DetachableResultReceiver(this.mHandler);
    this.mQueryHandler = new QueryHandler();
    this.mAddress = ((TextView)findViewById(2131624029));
    this.mWebAddress = ((TextView)findViewById(2131624031));
    this.mDescription = ((TextView)findViewById(2131624018));
    this.mHours = ((TextView)findViewById(2131624022));
    this.mHoursTitle = ((TextView)findViewById(2131624021));
    this.mDescContainer = findViewById(2131624014);
    this.mContactContainer = findViewById(2131624025);
    this.mCallButton = ((Button)findViewById(2131624027));
    this.mFeaturedAdapter = new FeaturedAdapter(this);
    this.mFeaturedList = ((ListView)findViewById(2131624033));
    this.mFeaturedList.setAdapter(this.mFeaturedAdapter);
    this.mFeaturedList.setEmptyView(findViewById(2131624034));
    this.mFeaturedList.setOnItemClickListener(this.mOnItemClickListener);
    this.mPromoView = findViewById(2131624032);
    this.mTabGroup = ((RadioGroup)findViewById(2131624010));
    this.mTabGroup.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
    this.mDetailsButton = ((RadioButton)findViewById(2131624011));
    this.mMoreHoursButton = ((Button)findViewById(2131624023));
    this.mLessHoursButton = ((Button)findViewById(2131624024));
    this.mMoreDescButton = ((Button)findViewById(2131624019));
    this.mLessDescButton = ((Button)findViewById(2131624020));
    this.mLogo = ((ImageView)findViewById(2131624009));
    this.mRatingTitle = ((TextView)findViewById(2131624015));
    this.mPhoneTitle = ((TextView)findViewById(2131624026));
    this.mWebsiteTitle = ((TextView)findViewById(2131624030));
    this.mDescTitle = ((TextView)findViewById(2131624017));
    this.mMoreHoursButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        VenueDetailActivity.this.showMoreHours(true);
      }
    });
    this.mLessHoursButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        VenueDetailActivity.this.showMoreHours(false);
      }
    });
    this.mMoreDescButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        VenueDetailActivity.this.showMoreDescription(true);
      }
    });
    this.mLessDescButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        VenueDetailActivity.this.showMoreDescription(false);
      }
    });
    handleIntent(getIntent());
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    if (this.mPIMapVenue != null) {
      this.mPIMapVenue.unRegisterPIVenueDownloadObserver();
    }
    if (this.mPIMapVenueDataCursor != null) {
      this.mPIMapVenueDataCursor.close();
    }
    if (this.mPIMapOperationMinutesDataCursor != null) {
      this.mPIMapOperationMinutesDataCursor.close();
    }
    super.onDestroy();
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
    this.mDealLoadReceiver.clearReceiver();
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

  protected void onResume()
  {
    super.onResume();
    this.mDealLoadReceiver.setReceiver(this.mDealLoadHandler);
    populateViews();
  }

  public boolean onSearchRequested()
  {
    return PlaceSearchActivity.showSearch(this);
  }

  private static class FeaturedAdapter
    extends CursorAdapter
  {
    private final LayoutInflater mInflater;

    public FeaturedAdapter(Context paramContext)
    {
      super(paramContext,null);
      this.mInflater = LayoutInflater.from(paramContext);
    }

    public void bindView(View paramView, Context paramContext, Cursor paramCursor)
    {
      ((ImageView)paramView.findViewById(2131623942)).setImageResource(DealsUtils.getDealListIconId(paramCursor.getString(paramCursor.getColumnIndexOrThrow("category"))));
      ((TextView)paramView.findViewById(16908310)).setText(paramCursor.getString(paramCursor.getColumnIndexOrThrow("title")));
      TextView localTextView = (TextView)paramView.findViewById(16908304);
      String str = DealDetailActivity.getDisplayEndDate(paramContext, paramCursor);
      if (str != null)
      {
        localTextView.setVisibility(0);
        localTextView.setText(paramContext.getString(2131099753, new Object[] { str }));
        return;
      }
      localTextView.setVisibility(8);
      localTextView.setText("");
    }

    public void loadResults(Cursor paramCursor)
    {
      changeCursor(paramCursor);
    }

    public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
    {
      return this.mInflater.inflate(2130903048, paramViewGroup, false);
    }
  }

  private class QueryHandler
    extends AsyncQueryHandler
  {
    public static final int TOKEN_GET_DEALS = 1;

    public QueryHandler()
    {
      super(getContentResolver());
    }

    protected void onQueryComplete(int i, Object obj, Cursor cursor)
    {
        Log.d(VenueDetailActivity.TAG, (new StringBuilder("onQueryComplete: token=")).append(i).toString());
        switch(i)
        {
        default:
            throw new IllegalArgumentException((new StringBuilder("Unknown token: ")).append(i).toString());

        case 1: // '\001'
            break;
        }
        if(cursor != null)
            mFeaturedAdapter.loadResults(cursor);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.VenueDetailActivity
 * JD-Core Version:    0.7.0.1
 */