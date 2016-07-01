package com.pointinside.android.app.ui;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.PIMapVenue.PIVenueDownloadObserver;
import com.pointinside.android.api.dao.PIMapPlaceDataCursor;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.net.HolidayGameClient;
import com.pointinside.android.app.net.HolidayGameClient.GameState;
import com.pointinside.android.app.net.HolidayGameClient.InfoResponse;
import com.pointinside.android.app.net.HolidayGameClient.PlaceInfo;
import com.pointinside.android.app.net.HolidayGameClient.PlaceState;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DateUtils;
import com.pointinside.android.app.util.DealsUtils;
import com.pointinside.android.app.util.DetachableAsyncTask;
import com.pointinside.android.app.util.DetachableAsyncTask.TaskCallbacks;
import com.pointinside.android.app.util.DetachableResultReceiver;
import com.pointinside.android.app.util.DetachableResultReceiver.Receiver;
import com.pointinside.android.app.util.HolidayGameCache;
import com.pointinside.android.piwebservices.net.PlaceSearchClient;
import com.pointinside.android.piwebservices.net.PlaceSearchClient.Place;
import com.pointinside.android.piwebservices.net.PlaceSearchClient.PlaceFeedbackRequest;
import com.pointinside.android.piwebservices.net.PlaceSearchClient.PlaceRequest;
import com.pointinside.android.piwebservices.net.PlaceSearchClient.PlaceResult;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.service.DealsService;
import com.pointinside.android.piwebservices.util.DevIdUtils;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PlaceDetailActivity
  extends Activity
{
  private static final String EXTRA_FEATURED_TAB_FIRST = "featured-tab-first";
  private static final String EXTRA_IN_CURRENT_VENUE = "in-current-venue";
  private static final String EXTRA_PLACE_SEARCH_URI = "place-search-uri";
  private static final String EXTRA_URBANQ_ID = "urbanq-id";
  private static final int GAME_AREA_BANNER = 1;
  private static final int GAME_AREA_LOADING = 0;
  private static final int REQUEST_CODE_SHOW_VENUE_MAP = 1;
  private static final String TAG = PlaceDetailActivity.class.getSimpleName();
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private TextView mAddress;
  private TextView mAddressTitle;
  private Button mCallButton;
  private RadioButton mContactButton;
  private View mContactContainer;
  private final DetachableResultReceiver.Receiver mDealLoadHandler = new DetachableResultReceiver.Receiver()
  {
    public void onReceiveResult(int i, Bundle bundle)
    {
        Log.d(PlaceDetailActivity.TAG, (new StringBuilder("onReceiveResult: resultCode=")).append(i).toString());
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
            Log.d(PlaceDetailActivity.TAG, (new StringBuilder("Error getting destination deals: ")).append(s).toString());
            return;
        } else
        {
            QueryHandler queryhandler = mQueryHandler;
            String as[] = new String[1];
            as[0] = mPlaceUUID;
            queryhandler.startQuery(1, uri, uri, null, "place_uuid=?", as, null);
            return;
        }
    }
  };
  private DetachableResultReceiver mDealLoadReceiver;
  private ViewGroup mDescContainer;
  private final View.OnClickListener mDescMoreOrLessClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      boolean bool = true;
      switch (paramAnonymousView.getId())
      {
      default:
        return;
      case 2131624019:
        showMoreDescription(bool);
        break;
      case 2131624020:
        showMoreDescription(false);
        break;
      }
      PlaceDetailActivity localPlaceDetailActivity = PlaceDetailActivity.this;
      if (PlaceDetailActivity.this.mMoreDescButton.getVisibility() == 0) {}
    localPlaceDetailActivity.showMoreDescription(bool);
    return;
    }
  };
  private TextView mDescTitle;
  private TextView mDescription;
  private RadioButton mDetailsButton;
  private PIMapVenue.PIVenueDownloadObserver mDownloadObserver = new PIMapVenue.PIVenueDownloadObserver()
  {
    public void venueImagesReady()
    {
      super.venueImagesReady();
      if ((PlaceDetailActivity.this.mPIMapPlaceDataCursor != null) && (!PlaceDetailActivity.this.isFinishing()))
      {
        PlaceDetailActivity.this.mPlaceLogo = PlaceDetailActivity.this.mPIMapPlaceDataCursor.getLogo(PlaceDetailActivity.this.mPIMapVenue);
        if (PlaceDetailActivity.this.mPlaceLogo != null) {
          PlaceDetailActivity.this.mLogo.setImageBitmap(PlaceDetailActivity.this.mPlaceLogo);
        }
      }
    }
  };
  private FeaturedAdapter mFeaturedAdapter;
  private ListView mFeaturedList;
  private ViewSwitcher mGameArea;
  private final View.OnClickListener mGameAreaClicked = new View.OnClickListener()
  {
    public void onClick(View view)
    {
        InfoResponse inforesponse;
        String s = "";
        if(mGameArea.getDisplayedChild() != 1) {
            return;
        } else {
            if((inforesponse = (InfoResponse)view.getTag()) == null) {
                return;
            }
            if(inforesponse.gameState != GameState.ON || inforesponse.placeInfo.state != PlaceState.Available)
                return;
            if(mPIMapVenue != null) {
            	s = mPIMapVenue.getVenueName();
            } else {
                    Place place = mPlace;
                    s = null;
                    if(place != null)
                        s = mPlace.city;

                    GamePlaceClaimActivity.show(PlaceDetailActivity.this, mGameSpaceUUID, mPlaceTitle, s);
                    return;
            }

        }
        if(inforesponse.linkUrl != null) {
            Intent intent = new Intent("android.intent.action.VIEW", inforesponse.linkUrl.buildUpon().appendQueryParameter("devid", DevIdUtils.getHashedUUID(PlaceDetailActivity.this)).build());
            startActivity(intent);
            return;
        }
    }
  };
  private ImageView mGameBanner;
  private TextView mGameNumViews;
  private String mGameSpaceUUID;
  private HolidayGameTask mGameTask;
  private final DetachableAsyncTask.TaskCallbacks<HolidayGameClient.InfoResponse, Void> mGameTaskCallback = new DetachableAsyncTask.TaskCallbacks<HolidayGameClient.InfoResponse, Void>()
  {
      protected void onPostExecute(com.pointinside.android.app.net.HolidayGameClient.InfoResponse inforesponse)
      {
          int i;
          HolidayGameCache.getInstance(PlaceDetailActivity.this).updateGlobalState(inforesponse);
          TextView textview;
          Resources resources;
          int j;
          Object aobj[];
          HolidayGameClient.PlaceInfo placeinfo;
          if(inforesponse == null || inforesponse.gameState == com.pointinside.android.app.net.HolidayGameClient.GameState.OFF)
              i = 0;
          else
          if(inforesponse.gameState == com.pointinside.android.app.net.HolidayGameClient.GameState.FINAL)
          {
              i = 0x7f020015;
          } else
          {
                  placeinfo = inforesponse.placeInfo;
                  if(placeinfo != null){
                      switch(placeinfo.state)
                      {
                      default:
                          throw new IllegalStateException();

                      case Available: // '\001'
                          i = 0x7f020016;
                          break;

                      case Owned: // '\003'
                          i = 0x7f020017;
                          break;

                      case Claimed: // '\002'
                          i = 0x7f020012;
                          break;
                      }

                  } else
                	  i =0;
          }
        while(true){
          mGameArea.setTag(inforesponse);
          if(i != 0)
          {
              mGameArea.setDisplayedChild(1);
              mGameBanner.setImageResource(i);
              if(i == 0x7f020016 || i == 0x7f020017)
              {
                  textview = mGameNumViews;
                  resources = getResources();
                  j = inforesponse.placeInfo.pageViews;
                  aobj = new Object[1];
                  aobj[0] = Integer.valueOf(inforesponse.placeInfo.pageViews);
                  textview.setText(resources.getQuantityString(0x7f0a0001, j, aobj));
                  mGameNumViews.setVisibility(0);
              } else
              {
                  mGameNumViews.setVisibility(8);
              }
              if(mGameArea.getVisibility() != 0)
              {
                  mGameArea.setVisibility(0);
                  mGameArea.startAnimation(fadeInAnimation());
              }
              return;
          } else
          {
              mGameArea.setDisplayedChild(0);
              mGameArea.setVisibility(8);
              return;
          }
        }

      }
  };
  private final Handler mHandler = new Handler();
  private TextView mHours;
  private final View.OnClickListener mHoursMoreOrLessClicked = new View.OnClickListener()
  {
      public void onClick(View view)
      {
          boolean flag = true;
          PlaceDetailActivity placedetailactivity;
          switch(view.getId())
          {
          default:
              return;
          case 2131624023:
              showMoreHours(flag);
              return;

          case 2131624024:
              showMoreHours(false);
              return;

          case 2131624044:
              placedetailactivity = PlaceDetailActivity.this;
              break;
          }
          if(mMoreHoursButton.getVisibility() != 0)
              flag = false;
          placedetailactivity.showMoreHours(flag);
      }
  };
  private TextView mHoursTitle;
  private boolean mIsPlaceInCurrentVenue;
  private Button mLessDescButton;
  private Button mLessHoursButton;
  private RadioButton mLikeButton;
  private RadioGroup mLikeGroup;
  private int mLikes = -1;
  private ImageView mLogo;
  private ImageButton mMapButton;
  private final View.OnClickListener mMapClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (PlaceDetailActivity.this.mIsPlaceInCurrentVenue)
      {
        VenueMapActivity.showWithResult(PlaceDetailActivity.this, PlaceDetailActivity.this.mPIMapVenue.getVenueUUID(), PlaceDetailActivity.this.mPlaceUUID);
        return;
      }
      if ((PlaceDetailActivity.this.mPlaceUUID != null) && (PlaceDetailActivity.this.mVenueUUID != null))
      {
        VenueDownloadActivity.loadVenueForResult(PlaceDetailActivity.this, 1, PlaceDetailActivity.this.mVenueUUID, false, false);
        return;
      }
//      GoogleMapActivity.showPlace(PlaceDetailActivity.this, PlaceDetailActivity.this.mPlace.title, PlaceDetailActivity.this.mPlace.description, PlaceDetailActivity.this.mPlace.urbanQId, PlaceDetailActivity.this.mPlace.searchUri, PlaceDetailActivity.this.mPlace.latitude, PlaceDetailActivity.this.mPlace.longitude);
    }
  };
  private Button mMoreDescButton;
  private Button mMoreHoursButton;
  private TextView mNumLikes;
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
      DealDetailActivity.show(PlaceDetailActivity.this, localUri);
    }
  };
  private RadioGroup.OnCheckedChangeListener mOnLikeChangeListener = new RadioGroup.OnCheckedChangeListener()
  {
      public void onCheckedChanged(RadioGroup radiogroup, int i)
      {
          switch(i)
          {
          default:
              return;

          case 2131624037:
              likeThisPlace(true);
              return;

          case 2131624038:
              likeThisPlace(false);
              break;
          }
      }

  };
  private PIMapPlaceDataCursor mPIMapPlaceDataCursor;
  private PIMapVenue mPIMapVenue;
  private TextView mPhoneTitle;
  private final Place mPlace = new Place();
  private Bitmap mPlaceLogo;
  private PlaceTask mPlaceTask;
  private final DetachableAsyncTask.TaskCallbacks<PlaceSearchClient.PlaceResult, Void> mPlaceTaskCallback = new DetachableAsyncTask.TaskCallbacks<PlaceSearchClient.PlaceResult, Void>()
  {
    protected void onPostExecute(PlaceSearchClient.PlaceResult paramAnonymousPlaceResult)
    {
      Iterator localIterator;
      if (paramAnonymousPlaceResult != null)
      {
        PlaceSearchClient.Place localPlace = paramAnonymousPlaceResult.place;
        if (localPlace != null)
        {
          if (localPlace.getId("urbanQ") != null) {
            PlaceDetailActivity.this.mUrbanQid = localPlace.getId("urbanQ");
          }
          if (localPlace.hasLikeCount())
          {
            PlaceDetailActivity.this.mLikes = localPlace.getLikeCount();
            if ((PlaceDetailActivity.this.mLikeButton.isChecked()) && (PlaceDetailActivity.this.mLikes > 0))
            {
              PlaceDetailActivity localPlaceDetailActivity = PlaceDetailActivity.this;
              localPlaceDetailActivity.mLikes = (-1 + localPlaceDetailActivity.mLikes);
            }
            PlaceDetailActivity.this.showLikes();
          }
          if (localPlace.hasUserRating()) {
            PlaceDetailActivity.this.showRating(localPlace.getUserRating());
          }
          if (localPlace.hasPrice()) {
            PlaceDetailActivity.this.addAttribute(PlaceDetailActivity.this.getString(2131099795), localPlace.getPrice());
          }
          if (localPlace.hasCategory()) {
            PlaceDetailActivity.this.addAttribute(PlaceDetailActivity.this.getString(2131099794), localPlace.getCategory());
          }
          if (localPlace.hasAttributes()) {
            localIterator = localPlace.getAttributes().entrySet().iterator();
            for (;localIterator.hasNext();)
            {
              Map.Entry localEntry = (Map.Entry)localIterator.next();
              PlaceDetailActivity.this.addAttribute((String)localEntry.getKey(), (String)localEntry.getValue());
            }
          }
        }
      }
    }
  };
  public String mPlaceTitle;
  private String mPlaceUUID;
  private long mPlaceid;
  private View mPromoView;
  private QueryHandler mQueryHandler;
  private float mRating;
  private RatingBar mRatingBar;
  private TextView mRatingTitle;
  private SharedPreferences mSharedPreferences;
  private RadioGroup mTabGroup;
  private RadioButton mUnlikeButton;
  private String mUrbanQid;
  private String mVenueUUID;
  private TextView mWebAddress;
  private TextView mWebsiteTitle;

  private View addAttribute(String paramString1, String paramString2, View.OnClickListener paramOnClickListener)
  {
    ViewGroup localViewGroup = (ViewGroup)this.mDescContainer.getChildAt(0);
    View localView = LayoutInflater.from(this).inflate(2130903088, localViewGroup, false);
    TextView localTextView1 = (TextView)localView.findViewById(2131624045);
    TextView localTextView2 = (TextView)localView.findViewById(2131624046);
    localTextView1.setText(paramString1);
    localTextView2.setText(paramString2);
    localViewGroup.addView(localView);
    localView.startAnimation(fadeInAnimation());
    if (paramOnClickListener != null) {
      localView.setOnClickListener(paramOnClickListener);
    }
    return localView;
  }

  private void addAttribute(String paramString1, String paramString2)
  {
    addAttribute(paramString1, paramString2, null);
  }

  private void executeGameInfoRequest(HolidayGameTask holidaygametask)
  {
      mGameTask = holidaygametask;
      if(mGameTask == null)
      {
          mGameSpaceUUID = HolidayGameClient.getSpaceUUID(mPlaceUUID, mUrbanQid);
          mGameTask = new HolidayGameTask(this, mGameSpaceUUID);
      }
      mGameTask.setCallback(mGameTaskCallback);
      if(mGameTask.getStatus() == android.os.AsyncTask.Status.PENDING)
          mGameTask.execute(new Void[0]);
  }

  private void executeOnResumeRemoteRequests()
  {
    executeGameInfoRequest(null);
  }

  private void executeOneTimeRemoteRequests()
  {
      Pair pair = (Pair)getLastNonConfigurationInstance();
      PlaceTask placetask;
      if(pair != null)
          placetask = (PlaceTask)pair.first;
      else
          placetask = null;
      executePlaceInfoRequest(placetask);
  }

  private void executePlaceInfoRequest(PlaceTask placetask)
  {
      mPlaceTask = placetask;
      if(mPlaceTask == null)
          mPlaceTask = new PlaceTask(this, mPlaceUUID, mUrbanQid);
      mPlaceTask.setCallback(mPlaceTaskCallback);
      if(mPlaceTask.getStatus() == android.os.AsyncTask.Status.PENDING)
          mPlaceTask.execute(new Void[0]);
  }

  private Animation fadeInAnimation()
  {
    return AnimationUtils.loadAnimation(this, 2130968581);
  }

  public static CharSequence getWebLink(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1)) {
      paramString1 = paramString2;
    }
    SpannableString localSpannableString = new SpannableString(paramString1);
    URLSpan localURLSpan = makeURLSpan(paramString2);
    if (localURLSpan != null) {
      localSpannableString.setSpan(localURLSpan, 0, localSpannableString.length(), 0);
    }
    return localSpannableString;
  }

  private void handleGameStateOnLoad()
  {
    if (HolidayGameCache.getInstance(this).shouldShowGame())
    {
      this.mGameArea.setVisibility(0);
      return;
    }
    this.mGameArea.setVisibility(8);
  }

  private void handleIntent(Intent intent)
  {
      mPlaceUUID = intent.getStringExtra("place_uuid");
      mVenueUUID = intent.getStringExtra("venue_uuid");
      mIsPlaceInCurrentVenue = intent.getBooleanExtra("in-current-venue", false);
      mUrbanQid = intent.getStringExtra("urbanq-id");
      Uri uri = (Uri)intent.getParcelableExtra("place-search-uri");
      mPlace.searchUri = uri;
      if(mIsPlaceInCurrentVenue)
          mPIMapVenue = VenueMapActivity.establishCurrentVenue(intent);
      else
          mQueryHandler.startQuery(2, null, uri, null, null, null, null);
      if(intent.getBooleanExtra("featured-tab-first", false))
          mTabGroup.check(2131624013);
  }


  private void hideAddressSection(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mAddressTitle.setVisibility(8);
      this.mAddress.setVisibility(8);
      return;
    }
    this.mAddressTitle.setVisibility(0);
    this.mAddress.setVisibility(0);
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

  private void likeThisPlace(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 1;; i = 0)
    {
      this.mSharedPreferences.edit().putInt(this.mPlaceUUID, i).commit();
      submitLike(paramBoolean);
      showLikes();
      return;
    }
  }

  private void loadDeals()
  {
    DealsService.loadDestinationDeals(this, this.mDealLoadReceiver, null, this.mPIMapVenue.getVenueUUID());
  }

  private void loadIfILikeThisPlace()
  {
    int i = this.mSharedPreferences.getInt(this.mPlaceUUID, -1);
    if (i > 0) {
      this.mLikeButton.setChecked(true);
    }
    while (i != 0) {
      return;
    }
    this.mUnlikeButton.setChecked(true);
  }

  private static URLSpan makeURLSpan(String paramString)
  {
    SpannableString localSpannableString = new SpannableString(paramString);
    URLSpan[] arrayOfURLSpan;
    if (Linkify.addLinks(localSpannableString, 15))
    {
      arrayOfURLSpan = (URLSpan[])localSpannableString.getSpans(0, localSpannableString.length(), URLSpan.class);
      if ((arrayOfURLSpan != null) && (arrayOfURLSpan.length != 0)) {}
    }
    else
    {
      return null;
    }
    return arrayOfURLSpan[0];
  }

  private void populateHoursFromCursor()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str = getString(2131099712);
    localStringBuilder.append(getString(2131099714));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getMondayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getMondayCloseMinute()));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099715));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getTuesdayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getTuesdayCloseMinute()));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099716));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getWednesdayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getWednesdayCloseMinute()));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099717));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getThursdayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getThursdayCloseMinute()));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099718));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getFridayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getFridayCloseMinute()));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099719));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getSaturdayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getSaturdayCloseMinute()));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099720));
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getSundayOpenMinute()));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getTimeFromMinutes(this.mPIMapPlaceDataCursor.getSundayCloseMinute()));
    this.mHours.setText(localStringBuilder.toString());
  }

  private void populateHoursFromSearch()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str = getString(2131099712);
    localStringBuilder.append(getString(2131099714));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenMonday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseMonday));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099715));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenTuesday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseTuesday));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099716));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenWednesday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseWednesday));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099717));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenThursday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseThursday));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099718));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenFriday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseFriday));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099719));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenSaturday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseSaturday));
    localStringBuilder.append("\n");
    localStringBuilder.append(getString(2131099720));
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursOpenSunday));
    localStringBuilder.append(str);
    localStringBuilder.append(DateUtils.getFormattedTime(this.mPlace.hoursCloseSunday));
    this.mHours.setText(localStringBuilder.toString());
  }

  private void populateTodaysHoursFromCursor()
  {
      int i;
      StringBuilder stringbuilder;
      String s;
      i = Calendar.getInstance().get(7);
      stringbuilder = new StringBuilder();
      s = getString(0x7f060040);
      stringbuilder.append(getString(2131099713));
      switch(i){
//      JVM INSTR tableswitch 1 7: default 80
  //                   1 92
  //                   2 131
  //                   3 170
  //                   4 209
  //                   5 248
  //                   6 287
  //                   7 326;
//         goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8
default:
      return;
case 1:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getSundayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getSundayCloseMinute()));
      break;
case 2:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getMondayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getMondayCloseMinute()));
      break;
case 3:

      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getTuesdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getTuesdayCloseMinute()));
      break;
case 4:

      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getWednesdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getWednesdayCloseMinute()));
      break;
case 5:

      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getThursdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getThursdayCloseMinute()));
      break;
case 6:

      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getFridayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getFridayCloseMinute()));
      break;
case 7:
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getSaturdayOpenMinute()));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getTimeFromMinutes(mPIMapPlaceDataCursor.getSaturdayCloseMinute()));
      break;
      }
      mHours.setText(stringbuilder.toString());

  }


  private void populateTodaysHoursFromSearch()
  {
      int i;
      StringBuilder stringbuilder;
      String s;
      i = Calendar.getInstance().get(7);
      stringbuilder = new StringBuilder();
      s = getString(0x7f060040);
      stringbuilder.append(getString(0x7f060041));
      switch(i) {
      //JVM INSTR tableswitch 1 7: default 80
  //                   1 92
  //                   2 131
  //                   3 170
  //                   4 209
  //                   5 248
  //                   6 287
  //                   7 326;
//         goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8
default:
      return;
case 1:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenSunday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseSunday));
      break;
case 2:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenMonday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseMonday));
      break;
case 3:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenTuesday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseTuesday));
      break;
case 4:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenWednesday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseWednesday));
      break;
case 5:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenThursday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseThursday));
      break;
case 6:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenFriday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseFriday));
      break;
case 7:
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursOpenSunday));
      stringbuilder.append(s);
      stringbuilder.append(DateUtils.getFormattedTime(mPlace.hoursCloseSunday));
      break;
     }
      mHours.setText(stringbuilder.toString());
  }

  private void populateViews()
  {
      mMapButton.setOnClickListener(mMapClicked);
      if(mIsPlaceInCurrentVenue)
      {
          mPIMapPlaceDataCursor = mPIMapVenue.getMapPlaceForUUID(mPlaceUUID);
          if(mPIMapPlaceDataCursor == null)
              throw new IllegalStateException("place is not loaded");
          mPlaceid = mPIMapPlaceDataCursor.getId();
          PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), String.valueOf(mPlaceid), mPIMapVenue.getVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SHOW_PLACE_DETAIL);
          mPlaceTitle = mPIMapPlaceDataCursor.getName();
          mActionBarHelper.setActionBarTitle(mPlaceTitle);
          hideAddressSection(true);
          String s3 = mPIMapPlaceDataCursor.getWebsite();
          String s4;
          final String phoneNumber;
          if(!TextUtils.isEmpty(s3))
          {
              mWebAddress.setText(getWebLink(s3, s3));
          } else
          {
              mWebsiteTitle.setVisibility(8);
              mWebAddress.setVisibility(8);
          }
          s4 = mPIMapPlaceDataCursor.getDescription();
          if(!TextUtils.isEmpty(s4))
          {
              mDescription.setText(s4);
              hideDescriptionSection(false);
          } else
          {
              hideDescriptionSection(true);
          }
          mPlaceLogo = mPIMapPlaceDataCursor.getLogo(mPIMapVenue);
          if(mPlaceLogo != null)
              mLogo.setImageBitmap(mPlaceLogo);
          mHandler.postDelayed(new Runnable() {

              public void run()
              {
                  if(!isFinishing())
                      mPIMapVenue.loadOrUpdateVenuePlaceImages(mDownloadObserver);
              }

          }
, 500L);
          phoneNumber = mPIMapPlaceDataCursor.getPhoneNumber();
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
          return;
      }
      mPlaceTitle = mPlace.title;
      mActionBarHelper.setActionBarTitle(mPlaceTitle);
      String s = mPlace.getFormattedAddress();
      String s1;
      String s2;
      final String phoneNumber;
      if(!TextUtils.isEmpty(s))
      {
          mAddress.setText(s);
          hideAddressSection(false);
      } else
      {
          hideAddressSection(true);
      }
      s1 = mPlace.websiteUrl;
      if(!TextUtils.isEmpty(s1))
      {
          mWebsiteTitle.setVisibility(0);
          mWebAddress.setVisibility(0);
          mWebAddress.setText(getWebLink(mPlace.websiteLabel, s1));
      } else
      {
          mWebsiteTitle.setVisibility(8);
          mWebAddress.setVisibility(8);
      }
      s2 = mPlace.description;
      if(!TextUtils.isEmpty(s2))
      {
          mDescription.setText(s2);
          hideDescriptionSection(false);
      } else
      {
          hideDescriptionSection(true);
      }
      if(!TextUtils.isEmpty(mPlace.hoursOpenMonday))
          hideHoursSection(false);
      else
          hideHoursSection(true);
      phoneNumber = mPlace.phoneNumber;
      if(!TextUtils.isEmpty(phoneNumber))
      {
          mPhoneTitle.setVisibility(0);
          mCallButton.setVisibility(0);
          mCallButton.setText(phoneNumber);
          mCallButton.setOnClickListener(new android.view.View.OnClickListener() {

              public void onClick(View view)
              {
                  Intent intent = new Intent("android.intent.action.DIAL", Uri.parse((new StringBuilder("tel:")).append(phoneNumber).toString()));
                  startActivity(intent);
              }

          }
);
          return;
      } else
      {
          mPhoneTitle.setVisibility(8);
          mCallButton.setVisibility(8);
          return;
      }
  }

  public static void show(Context paramContext, String paramString1, String paramString2)
  {
    show(paramContext, paramString1, paramString2, false);
  }

  private static void show(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    Intent localIntent = new Intent(paramContext, PlaceDetailActivity.class);
    localIntent.putExtra("place_uuid", paramString1);
    localIntent.putExtra("in-current-venue", true);
    if (paramBoolean) {
      localIntent.putExtra("featured-tab-first", true);
    }
    paramContext.startActivity(localIntent);
  }

  private void showLikes()
  {
      if(mLikes != -1)
      {
          int i = mLikes;
          boolean flag = mLikeButton.isChecked();
          int j;
          TextView textview;
          Object aobj[];
          if(i == 0)
          {
              if(flag)
                  j = 2131099774;
              else
                  j = 2131099775;
          } else
          if(i == 1)
          {
              if(flag)
                  j = 2131099773;
              else
                  j = 2131099770;
          } else
          if(flag)
              j = 2131099772;
          else
              j = 2131099771;
          textview = mNumLikes;
          aobj = new Object[1];
          aobj[0] = Integer.valueOf(i);
          textview.setText(getString(j, aobj));
          if(mNumLikes.getVisibility() != 0)
          {
              mNumLikes.setVisibility(0);
              mNumLikes.startAnimation(fadeInAnimation());
              return;
          }
      }
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
      if (this.mIsPlaceInCurrentVenue)
      {
        populateHoursFromCursor();
        return;
      }
      populateHoursFromSearch();
      return;
    }
    this.mMoreHoursButton.setVisibility(0);
    this.mLessHoursButton.setVisibility(8);
    if (this.mIsPlaceInCurrentVenue)
    {
      populateTodaysHoursFromCursor();
      return;
    }
    populateTodaysHoursFromSearch();
  }

  public static void showOnFeaturedTab(Context paramContext, String paramString1, String paramString2)
  {
    show(paramContext, paramString1, paramString2, true);
  }

  public static void showPlaceFromSearch(Context paramContext, Uri paramUri, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    Intent localIntent = new Intent(paramContext, PlaceDetailActivity.class);
    if (paramString3 != null)
    {
      localIntent.putExtra("place_uuid", paramString2);
      localIntent.putExtra("venue_uuid", paramString3);
      localIntent.putExtra("in-current-venue", paramBoolean);
    }
    localIntent.putExtra("place-search-uri", paramUri);
    localIntent.putExtra("urbanq-id", paramString1);
    paramContext.startActivity(localIntent);
  }

  private void showRating(float paramFloat)
  {
    this.mRatingBar.setRating(paramFloat);
    this.mRatingTitle.setVisibility(0);
    this.mRatingTitle.startAnimation(fadeInAnimation());
    this.mRatingBar.setVisibility(0);
    this.mRatingBar.startAnimation(fadeInAnimation());
  }

  private void submitLike(boolean paramBoolean)
  {
    final PlaceSearchClient localPlaceSearchClient = PlaceSearchClient.getInstance(this);
    final PlaceSearchClient.PlaceFeedbackRequest localPlaceFeedbackRequest = new PlaceSearchClient.PlaceFeedbackRequest();
    localPlaceFeedbackRequest.placeUUID = this.mPlaceUUID;
    localPlaceFeedbackRequest.urbanQId = this.mUrbanQid;
    if (paramBoolean) {}
    for (String str = "like";; str = "dislike")
    {
      localPlaceFeedbackRequest.actionType = str;
      new Thread()
      {
        public void run()
        {
          if (localPlaceFeedbackRequest.urbanQId != null) {}
          try
          {
            localPlaceSearchClient.sendFeedback(localPlaceFeedbackRequest);
            return;
          }
          catch (JSONWebRequester.RestResponseException localRestResponseException)
          {
            localRestResponseException.printStackTrace();
          }
        }
      }.start();
      return;
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (paramInt1 == 1)) {
      VenueMapActivity.showWithResult(this, this.mVenueUUID, this.mPlaceUUID);
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903087);
    this.mQueryHandler = new QueryHandler();
    this.mSharedPreferences = getSharedPreferences("PointInside", 0);
    this.mDealLoadReceiver = new DetachableResultReceiver(new Handler());
    this.mFeaturedAdapter = new FeaturedAdapter(this);
    this.mFeaturedList = ((ListView)findViewById(2131624033));
    this.mFeaturedList.setAdapter(this.mFeaturedAdapter);
    this.mFeaturedList.setEmptyView(findViewById(2131624034));
    this.mFeaturedList.setOnItemClickListener(this.mOnItemClickListener);
    this.mPromoView = findViewById(2131624032);
    this.mTabGroup = ((RadioGroup)findViewById(2131624010));
    this.mTabGroup.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
    this.mLikeGroup = ((RadioGroup)findViewById(2131624036));
    this.mLikeGroup.setOnCheckedChangeListener(this.mOnLikeChangeListener);
    this.mDetailsButton = ((RadioButton)findViewById(2131624011));
    this.mContactButton = ((RadioButton)findViewById(2131624012));
    this.mLikeButton = ((RadioButton)findViewById(2131624037));
    this.mUnlikeButton = ((RadioButton)findViewById(2131624038));
    this.mWebAddress = ((TextView)findViewById(2131624031));
    this.mDescription = ((TextView)findViewById(2131624018));
    this.mDescContainer = ((ViewGroup)findViewById(2131624014));
    this.mContactContainer = findViewById(2131624025);
    this.mHoursTitle = ((TextView)findViewById(2131624021));
    this.mHours = ((TextView)findViewById(2131624022));
    this.mNumLikes = ((TextView)findViewById(2131624039));
    this.mCallButton = ((Button)findViewById(2131624027));
    this.mMapButton = ((ImageButton)findViewById(2131624001));
    this.mMoreHoursButton = ((Button)findViewById(2131624023));
    this.mLessHoursButton = ((Button)findViewById(2131624024));
    this.mMoreDescButton = ((Button)findViewById(2131624019));
    this.mLessDescButton = ((Button)findViewById(2131624020));
    this.mLogo = ((ImageView)findViewById(2131624035));
    this.mRatingBar = ((RatingBar)findViewById(2131624016));
    this.mRatingTitle = ((TextView)findViewById(2131624015));
    this.mPhoneTitle = ((TextView)findViewById(2131624026));
    this.mWebsiteTitle = ((TextView)findViewById(2131624030));
    this.mDescTitle = ((TextView)findViewById(2131624017));
    this.mAddress = ((TextView)findViewById(2131624029));
    this.mAddressTitle = ((TextView)findViewById(2131624028));
    this.mGameArea = ((ViewSwitcher)findViewById(2131624040));
    this.mGameArea.setOnClickListener(this.mGameAreaClicked);
    this.mGameBanner = ((ImageView)findViewById(2131624041));
    this.mGameNumViews = ((TextView)findViewById(2131624042));
    handleGameStateOnLoad();
    handleIntent(getIntent());
    executeOneTimeRemoteRequests();
    findViewById(2131624044).setOnClickListener(this.mHoursMoreOrLessClicked);
    this.mMoreHoursButton.setOnClickListener(this.mHoursMoreOrLessClicked);
    this.mLessHoursButton.setOnClickListener(this.mHoursMoreOrLessClicked);
    findViewById(2131624043).setOnClickListener(this.mDescMoreOrLessClicked);
    this.mMoreDescButton.setOnClickListener(this.mDescMoreOrLessClicked);
    this.mLessDescButton.setOnClickListener(this.mDescMoreOrLessClicked);
    this.mWebAddress.setMovementMethod(LinkMovementMethod.getInstance());
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558407, paramMenu);
    getMenuInflater().inflate(2131558400, paramMenu);
    if (!HolidayGameCache.getInstance(this).isGameOn()) {
      paramMenu.removeItem(2131624058);
    }
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    this.mPlaceTask.clearCallback();
    if (this.mPIMapVenue != null) {
      this.mPIMapVenue.unRegisterPIVenueDownloadObserver();
    }
    if (this.mPIMapPlaceDataCursor != null) {
      this.mPIMapPlaceDataCursor.close();
    }
    super.onDestroy();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    case 2131624053:
    case 2131624054:
    case 2131624055:
    case 2131624056:
    case 2131624057:
        GameClaimCongratsActivity.sharePlace(this, this.mGameSpaceUUID);
        return true;
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
    loadIfILikeThisPlace();
    executeOnResumeRemoteRequests();
  }

  public Object onRetainNonConfigurationInstance()
  {
    return Pair.create(this.mPlaceTask, this.mGameTask);
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

  private static class HolidayGameTask
    extends DetachableAsyncTask<Void, Void, HolidayGameClient.InfoResponse>
  {
    private Context mContext;
    private String mThisSpaceUUID;

    private HolidayGameTask(Context paramContext, String paramString)
    {
      this.mContext = paramContext;
      this.mThisSpaceUUID = paramString;
    }

    protected HolidayGameClient.InfoResponse doInBackground(Void... paramVarArgs)
    {
      try
      {
        HolidayGameClient.InfoResponse localInfoResponse = HolidayGameClient.getInstance(this.mContext).placeInfo(this.mThisSpaceUUID);
        return localInfoResponse;
      }
      catch (JSONWebRequester.RestResponseException localRestResponseException)
      {
        Log.e(PlaceDetailActivity.TAG, "Response error getting game info", localRestResponseException);
      }
      return null;
    }
  }

  private static class Place
  {
    public String city;
    public String description;
    public String hoursCloseFriday;
    public String hoursCloseMonday;
    public String hoursCloseSaturday;
    public String hoursCloseSunday;
    public String hoursCloseThursday;
    public String hoursCloseTuesday;
    public String hoursCloseWednesday;
    public String hoursOpenFriday;
    public String hoursOpenMonday;
    public String hoursOpenSaturday;
    public String hoursOpenSunday;
    public String hoursOpenThursday;
    public String hoursOpenTuesday;
    public String hoursOpenWednesday;
    public double latitude;
    public double longitude;
    public String phoneNumber;
    public String placeUUID;
    public String rawHours;
    public Uri searchUri;
    public String state;
    public String street;
    public String title;
    public String urbanQId;
    public String venueUUID;
    public String websiteLabel;
    public String websiteUrl;
    public String zip;

    private String getFormattedAddress()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      if (!TextUtils.isEmpty(this.street))
      {
        localStringBuilder.append(this.street);
        localStringBuilder.append("\n");
      }
      if (!TextUtils.isEmpty(this.city)) {
        localStringBuilder.append(this.city);
      }
      if (!TextUtils.isEmpty(this.state))
      {
        localStringBuilder.append(", ");
        localStringBuilder.append(this.state);
      }
      if (!TextUtils.isEmpty(this.zip))
      {
        localStringBuilder.append(" ");
        localStringBuilder.append(this.zip);
      }
      if (localStringBuilder.length() > 0) {
        return localStringBuilder.toString();
      }
      return null;
    }
  }

  private static class PlaceTask
    extends DetachableAsyncTask<Void, Void, PlaceSearchClient.PlaceResult>
  {
    private Context mContext;
    private String mPlaceUUID;
    private String mUrbanQId;

    private PlaceTask(Context paramContext, String paramString1, String paramString2)
    {
      this.mContext = paramContext;
      this.mPlaceUUID = paramString1;
      this.mUrbanQId = paramString2;
    }

    protected PlaceSearchClient.PlaceResult doInBackground(Void... paramVarArgs)
    {
      PlaceSearchClient.PlaceRequest localPlaceRequest = new PlaceSearchClient.PlaceRequest();
      localPlaceRequest.setOutputFields(new String[] { "attributes", "category", "likeCount", "userRating", "price" });
      localPlaceRequest.placeUUID = this.mPlaceUUID;
      localPlaceRequest.urbanQId = this.mUrbanQId;
      try
      {
        PlaceSearchClient.PlaceResult localPlaceResult = PlaceSearchClient.getInstance(this.mContext).getPlace(localPlaceRequest);
        return localPlaceResult;
      }
      catch (JSONWebRequester.RestResponseException localRestResponseException)
      {
        Log.e(PlaceDetailActivity.TAG, "Response error accessing place detail", localRestResponseException);
      }
      return null;
    }
  }

  private class QueryHandler
    extends AsyncQueryHandler
  {
    public static final int TOKEN_GET_DEALS = 1;
    public static final int TOKEN_LOAD_PLACE_SEARCH_RESULT = 2;

    public QueryHandler()
    {
      super(getContentResolver());
    }

    protected void onQueryComplete(int paramInt, Object paramObject, Cursor paramCursor)
    {
      Log.d(PlaceDetailActivity.TAG, "onQueryComplete: token=" + paramInt);
      switch (paramInt)
      {
      default:
        throw new IllegalArgumentException("Unknown token: " + paramInt);
      case 1:
        if (paramCursor != null) {
          PlaceDetailActivity.this.mFeaturedAdapter.loadResults(paramCursor);
        }
        return;
      case 2:
    	  break;
      }
      if ((paramCursor != null) && (paramCursor.moveToFirst()))
      {
        PlaceDetailActivity.this.mPlace.title = paramCursor.getString(paramCursor.getColumnIndexOrThrow("title"));
        PlaceDetailActivity.this.mPlace.description = paramCursor.getString(paramCursor.getColumnIndexOrThrow("description"));
        PlaceDetailActivity.this.mPlace.phoneNumber = paramCursor.getString(paramCursor.getColumnIndexOrThrow("phone"));
        PlaceDetailActivity.this.mPlace.placeUUID = paramCursor.getString(paramCursor.getColumnIndexOrThrow("place_uuid"));
        PlaceDetailActivity.this.mPlace.urbanQId = paramCursor.getString(paramCursor.getColumnIndexOrThrow("urbanq_id"));
        PlaceDetailActivity.this.mPlace.latitude = paramCursor.getDouble(paramCursor.getColumnIndexOrThrow("latitude"));
        PlaceDetailActivity.this.mPlace.longitude = paramCursor.getDouble(paramCursor.getColumnIndexOrThrow("longitude"));
        PlaceDetailActivity.this.mPlace.websiteUrl = paramCursor.getString(paramCursor.getColumnIndexOrThrow("web_url"));
        PlaceDetailActivity.this.mPlace.websiteLabel = paramCursor.getString(paramCursor.getColumnIndexOrThrow("web_label"));
        PlaceDetailActivity.this.mPlace.street = paramCursor.getString(paramCursor.getColumnIndexOrThrow("STREET"));
        PlaceDetailActivity.this.mPlace.city = paramCursor.getString(paramCursor.getColumnIndexOrThrow("city"));
        PlaceDetailActivity.this.mPlace.state = paramCursor.getString(paramCursor.getColumnIndexOrThrow("state"));
        PlaceDetailActivity.this.mPlace.zip = paramCursor.getString(paramCursor.getColumnIndexOrThrow("zip"));
        PlaceDetailActivity.this.mPlace.rawHours = paramCursor.getString(paramCursor.getColumnIndexOrThrow("raw_hours"));
        PlaceDetailActivity.this.mPlace.hoursOpenMonday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_monday"));
        PlaceDetailActivity.this.mPlace.hoursCloseMonday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_monday"));
        PlaceDetailActivity.this.mPlace.hoursOpenTuesday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_tuesday"));
        PlaceDetailActivity.this.mPlace.hoursCloseTuesday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_tuesday"));
        PlaceDetailActivity.this.mPlace.hoursOpenWednesday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_wednesday"));
        PlaceDetailActivity.this.mPlace.hoursCloseWednesday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_wednesday"));
        PlaceDetailActivity.this.mPlace.hoursOpenThursday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_thursday"));
        PlaceDetailActivity.this.mPlace.hoursCloseThursday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_thursday"));
        PlaceDetailActivity.this.mPlace.hoursOpenFriday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_friday"));
        PlaceDetailActivity.this.mPlace.hoursCloseFriday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_friday"));
        PlaceDetailActivity.this.mPlace.hoursOpenSaturday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_saturday"));
        PlaceDetailActivity.this.mPlace.hoursCloseSaturday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_saturday"));
        PlaceDetailActivity.this.mPlace.hoursOpenSunday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_open_sunday"));
        PlaceDetailActivity.this.mPlace.hoursCloseSunday = paramCursor.getString(paramCursor.getColumnIndexOrThrow("hours_close_sunday"));
      }
      PlaceDetailActivity.this.populateViews();
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.PlaceDetailActivity
 * JD-Core Version:    0.7.0.1
 */