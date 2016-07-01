package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapPlaceCategoriesDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapPlaceCategoriesDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapPlaceCategoriesDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapPlaceCategoriesDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnDescription = this.mCursor.getColumnIndex("description");
  private int mColumnName = this.mCursor.getColumnIndex("name");

  private PIMapPlaceCategoriesDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapPlaceCategoriesDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapPlaceCategoriesDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapPlaceCategoriesDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapPlaceCategoriesDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getDescription()
  {
    return this.mCursor.getString(this.mColumnDescription);
  }

  public String getName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPlaceCategoriesUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapPlaceCategoriesDataCursor
 * JD-Core Version:    0.7.0.1
 */