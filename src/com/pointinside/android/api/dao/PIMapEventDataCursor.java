package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapEventDataCursor
  extends PIMapPromotionDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapEventDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapEventDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapEventDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnVenuePlaceId;

  private PIMapEventDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnVenuePlaceId = paramCursor.getColumnIndex("venue_place_id");
  }

  public static PIMapEventDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapEventDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapEventDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapEventDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public Uri getUri()
  {
    return PIVenue.getMapEventUri(getId());
  }

  public int getVenuePlaceId()
  {
    return this.mCursor.getInt(this.mColumnVenuePlaceId);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapEventDataCursor
 * JD-Core Version:    0.7.0.1
 */