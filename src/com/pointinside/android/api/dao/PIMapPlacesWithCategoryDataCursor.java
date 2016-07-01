package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapPlacesWithCategoryDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapPlacesWithCategoryDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapPlacesWithCategoryDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapPlacesWithCategoryDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnCategoryId = this.mCursor.getColumnIndex("category_type_id");
  private int mColumnCategoryName = this.mCursor.getColumnIndex("category_name");
  private int mColumnPlaceId = this.mCursor.getColumnIndex("_id");
  private int mColumnPlaceName = this.mCursor.getColumnIndex("place_name");
  private int mColumnPlaceUUID = this.mCursor.getColumnIndex("place_uuid");

  private PIMapPlacesWithCategoryDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapPlacesWithCategoryDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapPlacesWithCategoryDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapPlacesWithCategoryDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapPlacesWithCategoryDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public long getCategoryId()
  {
    return this.mCursor.getLong(this.mColumnCategoryId);
  }

  public String getCategoryName()
  {
    return this.mCursor.getString(this.mColumnCategoryName);
  }

  public long getPlaceId()
  {
    return this.mCursor.getLong(this.mColumnPlaceId);
  }

  public String getPlaceName()
  {
    return this.mCursor.getString(this.mColumnPlaceName);
  }

  public String getPlaceUUID()
  {
    return this.mCursor.getString(this.mColumnPlaceUUID);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPlaceCategoriesUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapPlacesWithCategoryDataCursor
 * JD-Core Version:    0.7.0.1
 */