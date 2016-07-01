package com.pointinside.android.app.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pointinside.android.app.util.DealsUtils;
import com.pointinside.android.app.util.LocationHelper;

public class DealsAdapter
  extends CursorAdapter
{
  private final LayoutInflater mInflater;
  private boolean mLoaded;
  private ResultContainer mResult;
  
  public DealsAdapter(Context paramContext)
  {
    super(paramContext, null);
    this.mInflater = LayoutInflater.from(paramContext);
  }
  
  public void bindView(View view, Context context, Cursor cursor)
  {
      ((ImageView)view.findViewById(0x7f0e0006)).setImageResource(DealsUtils.getDealListIconId(cursor.getString(cursor.getColumnIndexOrThrow("category"))));
      ((TextView)view.findViewById(0x1020016)).setText(cursor.getString(cursor.getColumnIndexOrThrow("organization")));
      ((TextView)view.findViewById(0x1020010)).setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
      int i = cursor.getInt(cursor.getColumnIndexOrThrow("deal_count"));
      TextView textview = (TextView)view.findViewById(0x7f0e0007);
      if(i > 1)
          textview.setVisibility(0);
      else
          textview.setVisibility(8);
      textview.setText(String.valueOf(i));
      ((TextView)view.findViewById(0x7f0e0008)).setText(LocationHelper.getUserDistanceFromLabel(context, cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")), cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))));
  }
  
  public Uri getResultsUri()
  {
    return this.mResult.resultUri;
  }
  
  public void loadResults(ResultContainer paramResultContainer, Cursor paramCursor)
  {
    this.mResult = paramResultContainer;
    this.mLoaded = true;
    changeCursor(paramCursor);
  }
  
  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    return this.mInflater.inflate(2130903048, paramViewGroup, false);
  }
  
  public boolean resultsLoaded()
  {
    return this.mLoaded;
  }
  
  public static class ResultContainer
  {
    public double requestLatitude;
    public double requestLongitude;
    public Uri resultUri;
  }
}

