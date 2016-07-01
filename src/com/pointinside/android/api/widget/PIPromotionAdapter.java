package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapPromotionDataCursor;

public abstract class PIPromotionAdapter
  extends AbstractDataCursorAdapter<PIMapPromotionDataCursor>
{
  public PIPromotionAdapter(Context paramContext, PIMapVenue paramPIMapVenue, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapPromotionDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIPromotionAdapter
 * JD-Core Version:    0.7.0.1
 */