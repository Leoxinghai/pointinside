package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.dao.PIMapGeoCountryDataCursor;

public abstract class PICountryAdapter
  extends AbstractDataCursorAdapter<PIMapGeoCountryDataCursor>
{
  public PICountryAdapter(Context paramContext, PIMapReference paramPIMapReference)
  {
    super(paramContext, loadCountries(paramPIMapReference), true);
  }

  private static Cursor loadCountries(PIMapReference paramPIMapReference)
  {
    return paramPIMapReference.getCountries().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapGeoCountryDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PICountryAdapter
 * JD-Core Version:    0.7.0.1
 */