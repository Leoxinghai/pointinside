package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapDealDataCursor
  extends PIMapPromotionDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapDealDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapDealDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapDealDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnVenuePlaceId;

  private PIMapDealDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnVenuePlaceId = paramCursor.getColumnIndex("venue_place_id");
  }

  public static PIMapDealDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapDealDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapDealDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapDealDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPromotionPlaceUri(getId());
  }

  public int getVenuePlaceId()
  {
    return this.mCursor.getInt(this.mColumnVenuePlaceId);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapDealDataCursor
 * JD-Core Version:    0.7.0.1
 */