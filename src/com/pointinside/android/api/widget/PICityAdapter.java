package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.dao.PIMapGeoCityDataCursor;

public abstract class PICityAdapter
  extends AbstractDataCursorAdapter<PIMapGeoCityDataCursor>
{
  public PICityAdapter(Context paramContext, PIMapReference paramPIMapReference, long paramLong)
  {
    super(paramContext, loadCities(paramPIMapReference, paramLong), true);
  }

  private static Cursor loadCities(PIMapReference paramPIMapReference, long paramLong)
  {
    if (paramLong > 0L) {
      return paramPIMapReference.getCitiesForSubdivision(paramLong).getCursor();
    }
    return paramPIMapReference.getCities().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapGeoCityDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PICityAdapter
 * JD-Core Version:    0.7.0.1
 */