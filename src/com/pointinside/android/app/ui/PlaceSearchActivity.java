package com.pointinside.android.app.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapPlaceDataCursor;
import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;
import com.pointinside.android.api.widget.PIVenuePlaceAdapter;
import com.pointinside.android.api.widget.PIVenueSummaryAdapter;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.R;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.PIDataUtils;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;

public class PlaceSearchActivity
  extends ListActivity
{
  private static final String EXTRA_EXISTING_SEARCH_TERM = "existing-search-term";
  private static final int REQUEST_CODE_SHOW_VENUE_MAP = 1;
  private static final int RUN_SEARCH_MILLISECONDS_DELAY = 500;
  private static final int SEARCH_MODE_IN_VENUE = 1;
  private static final int SEARCH_MODE_OUTDOORS = 2;
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private final Runnable mDoSearch = new Runnable()
  {
    public void run()
    {
      Editable localEditable = PlaceSearchActivity.this.mSearchBox.getText();
      if ((localEditable != null) && (!localEditable.toString().trim().equals(PlaceSearchActivity.this.mLastSearchText))) {
        PlaceSearchActivity.this.runLocalSearch(localEditable);
      }
    }
  };
  private View mGoButton;
  private final View.OnClickListener mGoClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      PlaceSearchResultsActivity.sendSearch(PlaceSearchActivity.this, PlaceSearchActivity.this.mSearchBox.getText().toString());
    }
  };
  private final Handler mHandler = new Handler();
  private String mLastSearchText = "";
  private final TextView.OnEditorActionListener mOnSearchAction = new TextView.OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
    {
      if (PlaceSearchActivity.this.mSearchBox.getText().length() > 0)
      {
        PlaceSearchResultsActivity.sendSearch(PlaceSearchActivity.this, PlaceSearchActivity.this.mSearchBox.getText().toString());
        return true;
      }
      return false;
    }
  };
  private final View.OnKeyListener mOnSearchKeyEvent = new View.OnKeyListener()
  {
      public boolean onKey(View view, int i, KeyEvent keyevent)
      {
          mHandler.removeCallbacks(mDoSearch);
          if(i == 66)
              mHandler.post(mDoSearch);
          else
              mHandler.postDelayed(mDoSearch, 500L);
          return false;
      }
  };
  private final TextWatcher mOnSearchTextChanged = new TextWatcher()
  {
    public void afterTextChanged(Editable paramAnonymousEditable)
    {
      View localView = PlaceSearchActivity.this.mGoButton;
      if (paramAnonymousEditable.length() > 0) {}
      for (boolean bool = true;; bool = false)
      {
        localView.setEnabled(bool);
        PlaceSearchActivity.this.mHandler.removeCallbacks(PlaceSearchActivity.this.mDoSearch);
        PlaceSearchActivity.this.mHandler.postDelayed(PlaceSearchActivity.this.mDoSearch, 500L);
        return;
      }
    }

    public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}

    public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
  };
  private EditText mSearchBox;
  private SearchModeImpl mSearchDelegate;
  private int mSearchMode;
  private TextView mVenueName;

  private void handleIntent(Intent intent)
  {
      String s = PointInside.getInstance().getCurrentVenueUUID();
      if(intent.hasExtra("existing-search-term"))
      {
          mSearchBox.setText(intent.getStringExtra("existing-search-term"));
          mSearchBox.setSelection(0, mSearchBox.getText().length());
          intent.removeExtra("existing-search-term");
      } else
      {
          mSearchBox.setText("");
      }
      mSearchBox.requestFocus();
      if(s != null)
          setupSearch(1, new VenueImpl(s));
      else
          setupSearch(2, new OutdoorImpl());
      PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", PointInside.getInstance().getCurrentVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SHOW_SEARCH);
  }

  private void runLocalSearch(CharSequence paramCharSequence)
  {
    CursorAdapter localCursorAdapter = this.mSearchDelegate.getAdapter();
    if ((paramCharSequence != null) && (localCursorAdapter != null))
    {
      String str = paramCharSequence.toString().trim();
      localCursorAdapter.getFilter().filter(str, new Filter.FilterListener()
      {
        public void onFilterComplete(int paramAnonymousInt)
        {
          PlaceSearchActivity localPlaceSearchActivity = PlaceSearchActivity.this;
          if (paramAnonymousInt == 0) {}
          for (boolean bool = true;; bool = false)
          {
            localPlaceSearchActivity.showEmptyResults(bool);
            return;
          }
        }
      });
      this.mLastSearchText = str;
      return;
    }
    showEmptyResults(true);
    this.mLastSearchText = "";
  }

  private void setupSearch(int paramInt, SearchModeImpl paramSearchModeImpl)
  {
    this.mSearchMode = paramInt;
    this.mSearchDelegate = paramSearchModeImpl;
    setListAdapter(paramSearchModeImpl.getAdapter());
  }

  private void showEmptyResults(boolean paramBoolean) {}

  public static boolean showSearch(Context paramContext)
  {
    return showSearch(paramContext, null);
  }

  public static boolean showSearch(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent(paramContext, PlaceSearchActivity.class);
    if (paramString != null) {
      localIntent.putExtra("existing-search-term", paramString);
    }
    paramContext.startActivity(localIntent);
    return true;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (paramInt1 == 1))
    {
      startActivity(new Intent(this, VenueMapActivity.class));
      finish();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903075);
    this.mActionBarHelper.setActionBarTitle(getTitle());
    this.mSearchBox = ((EditText)findViewById(R.id.search_box));
    this.mSearchBox.setOnEditorActionListener(this.mOnSearchAction);
    this.mSearchBox.addTextChangedListener(this.mOnSearchTextChanged);
    this.mSearchBox.setOnKeyListener(this.mOnSearchKeyEvent);
    this.mVenueName = ((TextView)findViewById(R.id.venue_name));
    this.mGoButton = findViewById(R.id.go);
    this.mGoButton.setOnClickListener(this.mGoClicked);
    this.mGoButton.setEnabled(false);
    getListView().setFastScrollEnabled(true);
    handleIntent(getIntent());
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mHandler.removeCallbacks(this.mDoSearch);
    this.mSearchDelegate.getAdapter().changeCursor(null);
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    this.mSearchDelegate.onItemClick(paramInt, paramLong);
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

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.mActionBarHelper.onPostCreate(paramBundle);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return this.mActionBarHelper.onPrepareOptionsMenu(paramMenu);
  }

  public boolean onSearchRequested()
  {
    return false;
  }

  private class OutdoorImpl
    implements PlaceSearchActivity.SearchModeImpl
  {
    private final PlaceSearchActivity.VenueSummaryAdapter mAdapter ;

    public OutdoorImpl() {
        mAdapter = new VenueSummaryAdapter(PlaceSearchActivity.this, PointInside.getPIMapReference().getVenues(PointInside.getInstance().getUserLocation()).getCursor());

    }

    public CursorAdapter getAdapter()
    {
      return this.mAdapter;
    }

    public void onItemClick(int paramInt, long paramLong)
    {
      PIMapVenueSummaryDataCursor localPIMapVenueSummaryDataCursor = (PIMapVenueSummaryDataCursor)this.mAdapter.getDataCursor(paramInt);
      VenueDownloadActivity.loadVenueForResult(PlaceSearchActivity.this, 1, localPIMapVenueSummaryDataCursor.getVenueUUID(), false, false);
    }
  }

  private static abstract interface SearchModeImpl
  {
    public abstract CursorAdapter getAdapter();

    public abstract void onItemClick(int paramInt, long paramLong);
  }

  private class VenueImpl
    implements PlaceSearchActivity.SearchModeImpl
  {
    private final PlaceSearchActivity.VenuePlaceAdapter mAdapter;

    public VenueImpl(String s)
    {
        super();
        PIMapVenue pimapvenue = VenueMapActivity.establishCurrentVenue(getIntent());
        if(!pimapvenue.getVenueUUID().equals(s))
        {
            throw new IllegalStateException("Global current venue state doesn't match with search venue state");
        } else
        {
            mVenueName.setVisibility(0);
            mVenueName.setText(pimapvenue.getVenueName());
            mAdapter = new VenuePlaceAdapter(PlaceSearchActivity.this, pimapvenue, pimapvenue.getMapPlaces(true));
            return;
        }
    }
    public CursorAdapter getAdapter()
    {
      return this.mAdapter;
    }

    public void onItemClick(int paramInt, long paramLong)
    {
      PIMapPlaceDataCursor localPIMapPlaceDataCursor = (PIMapPlaceDataCursor)this.mAdapter.getDataCursor(paramInt);
      PlaceDetailActivity.show(PlaceSearchActivity.this, localPIMapPlaceDataCursor.getUUID(), PointInside.getInstance().getCurrentVenueUUID());
      PlaceSearchActivity.this.finish();
    }
  }

  private class VenuePlaceAdapter
    extends PIVenuePlaceAdapter
    implements Filterable
  {
    private final PIMapVenue mVenue;

    public VenuePlaceAdapter(Context paramContext, PIMapVenue paramPIMapVenue, PIMapPlaceDataCursor paramPIMapPlaceDataCursor)
    {
      super(paramContext, paramPIMapVenue, paramPIMapPlaceDataCursor.getCursor());
      this.mVenue = paramPIMapVenue;
    }

    public void bindView(View paramView, Context paramContext, Cursor paramCursor)
    {
      PlaceSearchActivity.ViewHolder localViewHolder = (PlaceSearchActivity.ViewHolder)paramView.getTag();
      PIMapPlaceDataCursor localPIMapPlaceDataCursor = (PIMapPlaceDataCursor)getDataCursor(paramCursor.getPosition());
      localViewHolder.name.setText(localPIMapPlaceDataCursor.getName());
      localViewHolder.summary.setText(PIDataUtils.getAreaName(this.mVenue, localPIMapPlaceDataCursor.getId(), localPIMapPlaceDataCursor.getZoneId()));
    }

    public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
    {
      View localView = LayoutInflater.from(paramContext).inflate(2130903089, paramViewGroup, false);
      localView.setTag(new PlaceSearchActivity.ViewHolder(localView));
      return localView;
    }

    public Cursor runQueryOnBackgroundThread(CharSequence charsequence)
    {
        PIMapPlaceDataCursor pimapplacedatacursor;
        if(charsequence == null || charsequence.length() == 0)
        {
            pimapplacedatacursor = mVenue.getMapPlaces();
        } else
        {
            pimapplacedatacursor = mVenue.getMapPlaceSearchForName(charsequence.toString());
            PITouchstreamContract.addEvent(PlaceSearchActivity.this, PointInside.getInstance().getUserLocation(), charsequence.toString(), mVenue.getVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SEARCH_FOR_PLACE);
        }
        if(pimapplacedatacursor != null)
            return pimapplacedatacursor.getCursor();
        else
            return null;
    }
  }

  private class VenueSummaryAdapter extends PIVenueSummaryAdapter
  implements Filterable
{

  private Drawable getImageByVenueType(int i)
  {
      Resources resources = getResources();
      switch(i)
      {
      default:
          return resources.getDrawable(0x7f0200b4);

      case 2: // '\002'
          return resources.getDrawable(0x7f020008);
      }
  }

  public void bindView(View view, Context context, Cursor cursor)
  {
      ViewHolder viewholder = (ViewHolder)view.getTag();
      PIMapVenueSummaryDataCursor pimapvenuesummarydatacursor = (PIMapVenueSummaryDataCursor)getDataCursor(cursor.getPosition());
      String s = pimapvenuesummarydatacursor.getFormattedCityState();
      String s1 = pimapvenuesummarydatacursor.getVenueName();
      viewholder.name.setText(s1);
      viewholder.summary.setText(s);
      viewholder.image.setImageDrawable(getImageByVenueType(pimapvenuesummarydatacursor.getVenueTypeId()));
  }

  public View newView(Context context, Cursor cursor, ViewGroup viewgroup)
  {
      View view = LayoutInflater.from(context).inflate(0x7f030031, viewgroup, false);
      view.setTag(new ViewHolder(view));
      return view;
  }

  public Cursor runQueryOnBackgroundThread(CharSequence charsequence)
  {
      android.location.Location location = PointInside.getInstance().getUserLocation();
      PIMapVenueSummaryDataCursor pimapvenuesummarydatacursor;
      if(TextUtils.isEmpty(charsequence))
      {
          pimapvenuesummarydatacursor = PointInside.getPIMapReference().getVenues(location);
      } else
      {
          PITouchstreamContract.addEvent(PlaceSearchActivity.this, PointInside.getInstance().getUserLocation(), charsequence.toString(), 0L, com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SEARCH_FOR_VENUE);
          pimapvenuesummarydatacursor = PointInside.getPIMapReference().getVenueSearchForText(charsequence.toString(), location);
      }
      if(pimapvenuesummarydatacursor != null)
          return pimapvenuesummarydatacursor.getCursor();
      else
          return null;
  }


  public VenueSummaryAdapter(Context context, Cursor cursor)
  {
      super(context, PointInside.getPIMapReference(), cursor);
  }
}

  private static class ViewHolder
  {
    ImageView image;
    TextView name;
    TextView summary;

    public ViewHolder(View paramView)
    {
      this.name = ((TextView)paramView.findViewById(16908310));
      this.summary = ((TextView)paramView.findViewById(16908304));
      this.image = ((ImageView)paramView.findViewById(2131624047));
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.PlaceSearchActivity
 * JD-Core Version:    0.7.0.1
 */