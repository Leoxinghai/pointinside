package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapServiceGroupedDataCursor;

public abstract class PIVenueServiceGroupedAdapter
  extends AbstractDataCursorAdapter<PIMapServiceGroupedDataCursor>
{
  public PIVenueServiceGroupedAdapter(Context paramContext, PIMapVenue paramPIMapVenue, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  public PIVenueServiceGroupedAdapter(Context paramContext, PIMapVenue paramPIMapVenue, String paramString)
  {
    super(paramContext, loadVenueServicesForName(paramPIMapVenue, paramString), true);
  }

  private static Cursor loadVenueServicesForName(PIMapVenue paramPIMapVenue, String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0)) {
      return paramPIMapVenue.getMapServiceSearchForNameGrouped(paramString).getCursor();
    }
    return paramPIMapVenue.getMapServicesGrouped().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapServiceGroupedDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIVenueServiceGroupedAdapter
 * JD-Core Version:    0.7.0.1
 */