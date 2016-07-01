package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.maps.PIMapPolygonZone;

public class PIMapPolygonZoneDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapPolygonZoneDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapPolygonZoneDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapPolygonZoneDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnHostZoneIndex;
  private int mColumnName;
  private int mColumnPixelX;
  private int mColumnPixelY;
  private int mColumnPlaceCount;
  private int mColumnSpecialAreaId;
  private int mColumnZoneDisplayOrder;
  private int mColumnZoneId;
  private int mColumnZoneIndex;

  private PIMapPolygonZoneDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnSpecialAreaId = paramCursor.getColumnIndex("special_area_id");
    this.mColumnName = paramCursor.getColumnIndex("name");
    this.mColumnPixelX = paramCursor.getColumnIndex("pixel_x");
    this.mColumnPixelY = paramCursor.getColumnIndex("pixel_y");
    this.mColumnZoneId = paramCursor.getColumnIndex("zone_id");
    this.mColumnZoneIndex = paramCursor.getColumnIndex("zone_index");
    this.mColumnZoneDisplayOrder = paramCursor.getColumnIndex("zone_display_order");
    this.mColumnPlaceCount = paramCursor.getColumnIndex("place_count");
    this.mColumnHostZoneIndex = paramCursor.getColumnIndex("zonesrc_index");
  }

  public static PIMapPolygonZoneDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapPolygonZoneDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapPolygonZoneDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapPolygonZoneDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public int getHostZoneIndex()
  {
    return this.mCursor.getInt(this.mColumnHostZoneIndex);
  }

  public String getName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public PIMapPolygonZone getPIMapPolygonZone()
  {
    return new PIMapPolygonZone(getId(), getSpecialAreaId(), getName(), getPixelX(), getPixelY(), getZoneId(), getZoneIndex(), getPlaceCount(), getHostZoneIndex());
  }

  public int getPixelX()
  {
    return this.mCursor.getInt(this.mColumnPixelX);
  }

  public int getPixelY()
  {
    return this.mCursor.getInt(this.mColumnPixelY);
  }

  public int getPlaceCount()
  {
    return this.mCursor.getInt(this.mColumnPlaceCount);
  }

  public long getSpecialAreaId()
  {
    return this.mCursor.getLong(this.mColumnSpecialAreaId);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPolygonZoneUri(getId());
  }

  public int getZoneDisplayOrder()
  {
    return this.mCursor.getInt(this.mColumnZoneDisplayOrder);
  }

  public long getZoneId()
  {
    return this.mCursor.getLong(this.mColumnZoneId);
  }

  public int getZoneIndex()
  {
    return this.mCursor.getInt(this.mColumnZoneIndex);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapPolygonZoneDataCursor
 * JD-Core Version:    0.7.0.1
 */