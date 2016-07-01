package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapDealDataCursor;

public abstract class PIDealAdapter
  extends AbstractDataCursorAdapter<PIMapDealDataCursor>
{
  public PIDealAdapter(Context paramContext, PIMapVenue paramPIMapVenue, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapDealDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIDealAdapter
 * JD-Core Version:    0.7.0.1
 */