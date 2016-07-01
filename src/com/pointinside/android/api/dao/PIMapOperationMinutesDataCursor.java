package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapOperationMinutesDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapOperationMinutesDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapOperationMinutesDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapOperationMinutesDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnFridayCloseMinute = this.mCursor.getColumnIndex("friday_close_minute");
  private int mColumnFridayOpenMinute = this.mCursor.getColumnIndex("friday_open_minute");
  private int mColumnMondayCloseMinute = this.mCursor.getColumnIndex("monday_close_minute");
  private int mColumnMondayOpenMinute = this.mCursor.getColumnIndex("monday_open_minute");
  private int mColumnOperationTimesId;
  private int mColumnSaturdayCloseMinute = this.mCursor.getColumnIndex("saturday_close_minute");
  private int mColumnSaturdayOpenMinute = this.mCursor.getColumnIndex("saturday_open_minute");
  private int mColumnSundayCloseMinute = this.mCursor.getColumnIndex("sunday_close_minute");
  private int mColumnSundayOpenMinute = this.mCursor.getColumnIndex("sunday_open_minute");
  private int mColumnThursdayCloseMinute = this.mCursor.getColumnIndex("thursday_close_minute");
  private int mColumnThursdayOpenMinute = this.mCursor.getColumnIndex("thursday_open_minute");
  private int mColumnTuesdayCloseMinute = this.mCursor.getColumnIndex("tuesday_close_minute");
  private int mColumnTuesdayOpenMinute = this.mCursor.getColumnIndex("tuesday_open_minute");
  private int mColumnWednesdayCloseMinute = this.mCursor.getColumnIndex("wednesday_close_minute");
  private int mColumnWednesdayOpenMinute = this.mCursor.getColumnIndex("wednesday_open_minute");

  private PIMapOperationMinutesDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapOperationMinutesDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapOperationMinutesDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapOperationMinutesDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapOperationMinutesDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
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
    return PIVenue.getOperationMinutesUri(getId());
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
 * Qualified Name:     com.pointinside.android.api.dao.PIMapOperationMinutesDataCursor
 * JD-Core Version:    0.7.0.1
 */