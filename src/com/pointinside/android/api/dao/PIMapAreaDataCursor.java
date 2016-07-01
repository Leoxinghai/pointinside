package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapAreaDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapAreaDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapAreaDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapAreaDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnName;
  private int mColumnVenuePlaceId;

  private PIMapAreaDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnName = paramCursor.getColumnIndex("name");
    this.mColumnVenuePlaceId = paramCursor.getColumnIndex("venue_place_id");
  }

  public static PIMapAreaDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapAreaDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapAreaDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapAreaDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPlaceAreaUri(getId());
  }

  public long getVenuePlaceId()
  {
    return this.mCursor.getLong(this.mColumnVenuePlaceId);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapAreaDataCursor
 * JD-Core Version:    0.7.0.1
 */