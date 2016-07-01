package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapEventDataCursor;

public abstract class PIEventAdapter
  extends AbstractDataCursorAdapter<PIMapEventDataCursor>
{
  public PIEventAdapter(Context paramContext, PIMapVenue paramPIMapVenue, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapEventDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIEventAdapter
 * JD-Core Version:    0.7.0.1
 */