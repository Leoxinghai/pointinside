package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapCategoryDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapCategoryDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapCategoryDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapCategoryDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnDescription = this.mCursor.getColumnIndex("description");
  private int mColumnName = this.mCursor.getColumnIndex("name");

  private PIMapCategoryDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapCategoryDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapCategoryDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapCategoryDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapCategoryDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
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
    return PIVenue.getMapCategoryUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapCategoryDataCursor
 * JD-Core Version:    0.7.0.1
 */