package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapPlaceDataCursor
  extends PIMapLandmarkDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapPlaceDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapPlaceDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapPlaceDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnFridayCloseMinute = this.mCursor.getColumnIndex("friday_close_minute");
  private int mColumnFridayOpenMinute = this.mCursor.getColumnIndex("friday_open_minute");
  private int mColumnMondayCloseMinute = this.mCursor.getColumnIndex("monday_close_minute");
  private int mColumnMondayOpenMinute = this.mCursor.getColumnIndex("monday_open_minute");
  private int mColumnOperationTimesId = this.mCursor.getColumnIndex("operation_times_id");
  private int mColumnSaturdayCloseMinute = this.mCursor.getColumnIndex("saturday_close_minute");
  private int mColumnSaturdayOpenMinute = this.mCursor.getColumnIndex("saturday_open_minute");
  private int mColumnSundayCloseMinute = this.mCursor.getColumnIndex("sunday_close_minute");
  private int mColumnSundayOpenMinute = this.mCursor.getColumnIndex("sunday_open_minute");
  private int mColumnThursdayCloseMinute = this.mCursor.getColumnIndex("thursday_close_minute");
  private int mColumnThursdayOpenMinute = this.mCursor.getColumnIndex("thursday_open_minute");
  private int mColumnTuesdayCloseMinute = this.mCursor.getColumnIndex("tuesday_close_minute");
  private int mColumnTuesdayOpenMinute = this.mCursor.getColumnIndex("tuesday_open_minute");
  private int mColumnWebsite = this.mCursor.getColumnIndex("website");
  private int mColumnWednesdayCloseMinute = this.mCursor.getColumnIndex("wednesday_close_minute");
  private int mColumnWednesdayOpenMinute = this.mCursor.getColumnIndex("wednesday_open_minute");

  private PIMapPlaceDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapPlaceDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapPlaceDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapPlaceDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapPlaceDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public int getFridayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnFridayCloseMinute);
  }

  public int getFridayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnFridayOpenMinute);
  }

  public int getMondayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnMondayCloseMinute);
  }

  public int getMondayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnMondayOpenMinute);
  }

  public int getOperationTimesId()
  {
    return this.mCursor.getInt(this.mColumnOperationTimesId);
  }

  public int getSaturdayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnSaturdayCloseMinute);
  }

  public int getSaturdayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnSaturdayOpenMinute);
  }

  public int getSundayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnSundayCloseMinute);
  }

  public int getSundayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnSundayOpenMinute);
  }

  public int getThursdayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnThursdayCloseMinute);
  }

  public int getThursdayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnThursdayOpenMinute);
  }

  public int getTuesdayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnTuesdayCloseMinute);
  }

  public int getTuesdayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnTuesdayOpenMinute);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPlaceUri(getId());
  }

  public String getWebsite()
  {
    return this.mCursor.getString(this.mColumnWebsite);
  }

  public int getWednesdayCloseMinute()
  {
    return this.mCursor.getInt(this.mColumnWednesdayCloseMinute);
  }

  public int getWednesdayOpenMinute()
  {
    return this.mCursor.getInt(this.mColumnWednesdayOpenMinute);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapPlaceDataCursor
 * JD-Core Version:    0.7.0.1
 */