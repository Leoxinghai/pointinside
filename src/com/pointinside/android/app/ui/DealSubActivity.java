package com.pointinside.android.app.ui;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DealsUtils;
import com.pointinside.android.app.util.PIDataUtils;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;

public class DealSubActivity
  extends ListActivity
{
  public static final String EXTRA_EXTRAS = "extras";
  public static final String EXTRA_LATITUDE = "latitude";
  public static final String EXTRA_LONGITUDE = "longitude";
  public static final String EXTRA_REQUEST_ID = "request_id";
  public static final String EXTRA_TITLE = "title";
  public static final String EXTRA_VENUE_UUID = "venue_uuid";
  private static final String STRING_LIMITED_ESCAPE = "For Limited Time";
  private static final String STRING_LIMITED_TODAY = "Today";
  private static final String STRING_LIMITED_TOMORROW = "Tomorrow";
  private static final String TAG = DealSubActivity.class.getSimpleName();
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private DealsSubAdapter mAdapter;
  private QueryHandler mQueryHandler;

  public static void show(Context paramContext, long paramLong, String paramString)
  {
    Intent localIntent = new Intent(paramContext, DealSubActivity.class);
    Bundle localBundle = new Bundle();
    localBundle.putString("title", PIDataUtils.getVenueName(PointInside.getPIMapReference(), paramString));
    localBundle.putLong("request_id", paramLong);
    localBundle.putString("venue_uuid", paramString);
    localIntent.putExtra("extras", localBundle);
    paramContext.startActivity(localIntent);
  }

  public static void show(Context paramContext, String paramString, long paramLong, double paramDouble1, double paramDouble2)
  {
    Intent localIntent = new Intent(paramContext, DealSubActivity.class);
    Bundle localBundle = new Bundle();
    localBundle.putString("title", paramString);
    localBundle.putLong("request_id", paramLong);
    localBundle.putDouble("latitude", paramDouble1);
    localBundle.putDouble("longitude", paramDouble2);
    localIntent.putExtra("extras", localBundle);
    paramContext.startActivity(localIntent);
  }

  protected void onCreate(Bundle bundle)
  {
      String s;
      long l;
      double d;
      double d1;
      super.onCreate(bundle);
      setContentView(0x7f030010);
      mAdapter = new DealsSubAdapter(this);
      setListAdapter(mAdapter);
      mQueryHandler = new QueryHandler();
      Bundle bundle1 = getIntent().getBundleExtra("extras");
      s = bundle1.getString("venue_uuid");
      l = bundle1.getLong("request_id", -1L);
      d = bundle1.getDouble("latitude", 0.0D);
      d1 = bundle1.getDouble("longitude", 0.0D);
      String s1 = bundle1.getString("title");
      if(l == -1L)
          finish();
      mActionBarHelper.setActionBarTitle(s1);
      Uri uri = null;
      if(s != null) {
		uri = com.pointinside.android.piwebservices.provider.PIWebServicesContract.DealsResults.makeResultsUriByRequestAndVenue(l, s);
      } else {
          boolean bool1 = d < 0.0D;
          uri = null;
          if (bool1)
          {
            boolean bool2 = d1 < 0.0D;
            uri = null;
            if (bool2) {
            	uri = PIWebServicesContract.DealsResults.makeResultsUriByRequestAndLocation(l, Uri.encode(d + "," + d1));
            }
          }

      }
          if(uri != null)
              mQueryHandler.startQuery(1, null, uri, null, null, null, null);
          return;

  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    DealDetailActivity.show(this, PIWebServicesContract.DealsResults.makeResultUri(paramLong));
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
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
  }

  public boolean onSearchRequested()
  {
    return PlaceSearchActivity.showSearch(this);
  }

  private static class DealsSubAdapter
    extends CursorAdapter
  {
    private final LayoutInflater mInflater;

    public DealsSubAdapter(Context paramContext)
    {
      super(paramContext,null);
      this.mInflater = LayoutInflater.from(paramContext);
    }

    public void bindView(View view, Context context, Cursor cursor)
    {
        ((ImageView)view.findViewById(0x7f0e0006)).setImageResource(DealsUtils.getDealListIconId(cursor.getString(cursor.getColumnIndexOrThrow("category"))));
        ((TextView)view.findViewById(0x1020016)).setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        TextView textview = (TextView)view.findViewById(0x7f0e0009);
        String s = DealDetailActivity.getDisplayEndDate(context, cursor);
        TextView textview1;
        String s1 =null;
        if(s != null)
        {
            textview.setVisibility(0);
            if(s.equalsIgnoreCase("For Limited Time"))
                textview.setText(s);
            else
            if(s.equalsIgnoreCase("Today") || s.equalsIgnoreCase("Tomorrow"))
                textview.setText(context.getString(0x7f060069, new Object[] {
                    s
                }));
            else
                textview.setText(context.getString(0x7f060068, new Object[] {
                    s
                }));
        } else
        {
            textview.setVisibility(8);
            textview.setText("");
        }
        s1 = cursor.getString(cursor.getColumnIndexOrThrow("upc"));
        textview1 = (TextView)view.findViewById(0x7f0e000a);
        if(!TextUtils.isEmpty(s1))
        {
            textview1.setVisibility(0);
            textview1.setText(context.getString(0x7f06006a, new Object[] {
                s1
            }));
            return;
        } else
        {
            textview1.setVisibility(8);
            return;
        }
    }

    public void loadResults(Cursor paramCursor)
    {
      changeCursor(paramCursor);
    }

    public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
    {
      return this.mInflater.inflate(2130903051, paramViewGroup, false);
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

    protected void onQueryComplete(int paramInt, Object paramObject, Cursor paramCursor)
    {
      Log.d(DealSubActivity.TAG, "onQueryComplete: token=" + paramInt);
      switch (paramInt)
      {
      default:
        throw new IllegalArgumentException("Unknown token: " + paramInt);
      case 1:
          DealSubActivity.this.mAdapter.loadResults(paramCursor);
          break;
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.DealSubActivity
 * JD-Core Version:    0.7.0.1
 */