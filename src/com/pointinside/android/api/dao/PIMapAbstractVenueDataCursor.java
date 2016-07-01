package com.pointinside.android.api.dao;

import android.database.Cursor;

public abstract class PIMapAbstractVenueDataCursor
  extends AbstractDataCursor
{
  private int mColumnDescription = this.mCursor.getColumnIndex("description");
  private int mColumnLocationPixelX = this.mCursor.getColumnIndex("location_pixel_x");
  private int mColumnLocationPixelY = this.mCursor.getColumnIndex("location_pixel_y");
  private int mColumnName = this.mCursor.getColumnIndex("name");
  private int mColumnVenueId = this.mCursor.getColumnIndex("venue_id");
  private int mColumnZoneId = this.mCursor.getColumnIndex("zone_id");
  private int mColumnZoneIndex = this.mCursor.getColumnIndex("zone_index");

  protected PIMapAbstractVenueDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public String getDescription()
  {
    return this.mCursor.getString(this.mColumnDescription);
  }

  public int getLocationPixelX()
  {
    return this.mCursor.getInt(this.mColumnLocationPixelX);
  }

  public int getLocationPixelY()
  {
    return this.mCursor.getInt(this.mColumnLocationPixelY);
  }

  public String getName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public int getVenueId()
  {
    return this.mCursor.getInt(this.mColumnVenueId);
  }

  public int getZoneId()
  {
    return this.mCursor.getInt(this.mColumnZoneId);
  }

  public int getZoneIndex()
  {
    return this.mCursor.getInt(this.mColumnZoneIndex);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapAbstractVenueDataCursor
 * JD-Core Version:    0.7.0.1
 */