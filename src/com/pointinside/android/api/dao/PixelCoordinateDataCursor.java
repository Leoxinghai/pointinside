package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.maps.PixelCoordinate;

public class PixelCoordinateDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PixelCoordinateDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PixelCoordinateDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PixelCoordinateDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnAltitude;
  private int mColumnLatitude;
  private int mColumnLongitude;
  private int mColumnPixelX;
  private int mColumnPixelY;
  private int mColumnSpecialAreaId;

  private PixelCoordinateDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnSpecialAreaId = paramCursor.getColumnIndex("special_area_id");
    this.mColumnAltitude = paramCursor.getColumnIndex("altitude");
    this.mColumnPixelX = paramCursor.getColumnIndex("pixel_x");
    this.mColumnPixelY = paramCursor.getColumnIndex("pixel_y");
    this.mColumnLatitude = paramCursor.getColumnIndex("latitude");
    this.mColumnLongitude = paramCursor.getColumnIndex("longitude");
  }

  public static PixelCoordinateDataCursor getInstance(Cursor paramCursor)
  {
    return (PixelCoordinateDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PixelCoordinateDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PixelCoordinateDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public float getAltitude()
  {
    return this.mCursor.getFloat(this.mColumnAltitude);
  }

  public double getLatitude()
  {
    return this.mCursor.getDouble(this.mColumnLatitude);
  }

  public double getLongitude()
  {
    return this.mCursor.getDouble(this.mColumnLongitude);
  }

  public PixelCoordinate getPixelCoordinate()
  {
    return new PixelCoordinate(getSpecialAreaId(), getAltitude(), getLatitude(), getLongitude(), getX(), getY());
  }

  public long getSpecialAreaId()
  {
    return this.mCursor.getLong(this.mColumnSpecialAreaId);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPixelCoordinateUri(getId());
  }

  public int getX()
  {
    return this.mCursor.getInt(this.mColumnPixelX);
  }

  public int getY()
  {
    return this.mCursor.getInt(this.mColumnPixelY);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PixelCoordinateDataCursor
 * JD-Core Version:    0.7.0.1
 */