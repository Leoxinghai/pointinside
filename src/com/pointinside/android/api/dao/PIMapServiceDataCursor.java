package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapServiceDataCursor
  extends PIMapLandmarkDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapServiceDataCursor> CREATOR = new AbstractDataCursor.Creator<PIMapServiceDataCursor>()
  {
    public PIMapServiceDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapServiceDataCursor(paramAnonymousCursor);
    }
  };

  private PIMapServiceDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapServiceDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapServiceDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapServiceDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapServiceDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public Uri getUri()
  {
    return PIVenue.getMapServiceUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapServiceDataCursor
 * JD-Core Version:    0.7.0.1
 */