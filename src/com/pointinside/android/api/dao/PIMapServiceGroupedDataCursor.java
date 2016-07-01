package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapServiceGroupedDataCursor
  extends PIMapAbstractVenueDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapServiceGroupedDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapServiceGroupedDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapServiceGroupedDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnCount;
  private int mColumnName;
  private int mColumnZoneId;
  private int mColumnZoneIndex;

  private PIMapServiceGroupedDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnName = paramCursor.getColumnIndex("name");
    this.mColumnZoneId = paramCursor.getColumnIndex("zone_id");
    this.mColumnZoneIndex = paramCursor.getColumnIndex("zone_index");
    this.mColumnCount = paramCursor.getColumnIndex("count");
    if (this.mColumnCount == -1) {
      this.mColumnCount = 4;
    }
  }

  public static PIMapServiceGroupedDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapServiceGroupedDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapServiceGroupedDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapServiceGroupedDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public int getCount()
  {
    return this.mCursor.getInt(this.mColumnCount);
  }

  public String getPlaceName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public int getPlaceZoneId()
  {
    return this.mCursor.getInt(this.mColumnZoneId);
  }

  public int getPlaceZoneIndex()
  {
    return this.mCursor.getInt(this.mColumnZoneIndex);
  }

  public Uri getUri()
  {
    return PIVenue.getMapServiceUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapServiceGroupedDataCursor
 * JD-Core Version:    0.7.0.1
 */