package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;

public abstract class PIVenueSummaryAdapter
  extends AbstractDataCursorAdapter<PIMapVenueSummaryDataCursor>
{
  public PIVenueSummaryAdapter(Context paramContext, PIMapReference paramPIMapReference, long paramLong)
  {
    super(paramContext, loadVenueSummaries(paramPIMapReference, paramLong), true);
  }

  public PIVenueSummaryAdapter(Context paramContext, PIMapReference paramPIMapReference, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  private static Cursor loadVenueSummaries(PIMapReference paramPIMapReference, long paramLong)
  {
    if (paramLong > 0L) {
      return paramPIMapReference.getVenuesForCity(paramLong).getCursor();
    }
    return paramPIMapReference.getVenues().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapVenueSummaryDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIVenueSummaryAdapter
 * JD-Core Version:    0.7.0.1
 */