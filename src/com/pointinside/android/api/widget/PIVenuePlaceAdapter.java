package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapPlaceDataCursor;

public abstract class PIVenuePlaceAdapter
  extends AbstractDataCursorAdapter<PIMapPlaceDataCursor>
{
  public PIVenuePlaceAdapter(Context paramContext, PIMapVenue paramPIMapVenue, Cursor paramCursor)
  {
    super(paramContext, paramCursor, true);
  }

  public PIVenuePlaceAdapter(Context paramContext, PIMapVenue paramPIMapVenue, String paramString)
  {
    super(paramContext, loadVenuePlacesForName(paramPIMapVenue, paramString), true);
  }

  private static Cursor loadVenuePlacesForName(PIMapVenue paramPIMapVenue, String paramString)
  {
    if ((paramString != null) && (paramString.length() > 0)) {
      return paramPIMapVenue.getMapPlaceSearchForName(paramString).getCursor();
    }
    return paramPIMapVenue.getMapPlaces().getCursor();
  }

  protected void onAllocInternal(Cursor paramCursor)
  {
    this.mDataCursor = PIMapPlaceDataCursor.getInstance(paramCursor);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.PIVenuePlaceAdapter
 * JD-Core Version:    0.7.0.1
 */