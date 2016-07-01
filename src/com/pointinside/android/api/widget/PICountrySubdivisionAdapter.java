package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.dao.PIMapGeoCountrySubdivisionDataCursor;

public abstract class PICountrySubdivisionAdapter
  extends AbstractDataCursorAdapter<PIMapGeoCountrySubdivisionDataCursor>
{
  public PICountrySubdivisionAdapter(Context paramContext, PIMapReference paramPIMapReference, long paramLong)
  {
    super(paramContext, loadCountrySubdivisions(paramPIMapReference, paramLong), true);
  }

  private static Cursor loadCountrySubdivisions(PIMapReference paramPIMapReference, long paramLong)
  {
    if (paramLong > 0L) {
      return paramPIMapReference.getGeoCountrySubdivisionsForCountry(paramLong).getCursor();
    }
    return paramPIMapReference.getGeoCountrySubdivisions().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapGeoCountrySubdivisionDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PICountrySubdivisionAdapter
 * JD-Core Version:    0.7.0.1
 */