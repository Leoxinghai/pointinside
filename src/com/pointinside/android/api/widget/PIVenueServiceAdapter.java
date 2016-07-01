package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapServiceDataCursor;

public abstract class PIVenueServiceAdapter
  extends AbstractDataCursorAdapter<PIMapServiceDataCursor>
{
  public PIVenueServiceAdapter(Context paramContext, PIMapVenue paramPIMapVenue, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  public PIVenueServiceAdapter(Context paramContext, PIMapVenue paramPIMapVenue, String paramString)
  {
    super(paramContext, loadVenueServicesForName(paramPIMapVenue, paramString), true);
  }

  private static Cursor loadVenueServicesForName(PIMapVenue paramPIMapVenue, String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0)) {
      return paramPIMapVenue.getMapServiceSearchForName(paramString).getCursor();
    }
    return paramPIMapVenue.getMapServices().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapServiceDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIVenueServiceAdapter
 * JD-Core Version:    0.7.0.1
 */