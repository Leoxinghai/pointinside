package com.pointinside.android.api.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.content.PIContentManager;
import com.pointinside.android.api.maps.PIMapVenueZone;

public class PIMapZoneDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapZoneDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapZoneDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapZoneDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnBearingPoint2Point1 = this.mCursor.getColumnIndex("bearing_point2_point1");
  private int mColumnBearingPoint4Point3 = this.mCursor.getColumnIndex("bearing_point4_point3");
  private int mColumnCountPixelPoint1Point2Y = this.mCursor.getColumnIndex("count_pixel_point1_point2_y");
  private int mColumnCountPixelPoint3Point4X = this.mCursor.getColumnIndex("count_pixel_point3_point4_x");
  private int mColumnFeetPerPixelX = this.mCursor.getColumnIndex("feet_per_pixel_x");
  private int mColumnFeetPerPixelY = this.mCursor.getColumnIndex("feet_per_pixel_y");
  private int mColumnImageId = this.mCursor.getColumnIndex("image_id");
  private int mColumnImageName = this.mCursor.getColumnIndex("image_name");
  private int mColumnImageSizeX = this.mCursor.getColumnIndex("image_size_pixel_x");
  private int mColumnImageSizeY = this.mCursor.getColumnIndex("image_size_pixel_y");
  private int mColumnIsAboveGroundLevel = this.mCursor.getColumnIndex("is_above_ground_zone");
  private int mColumnIsOverView = this.mCursor.getColumnIndex("is_overview_zone");
  private int mColumnPlaceCount = this.mCursor.getColumnIndex("place_count");
  private int mColumnPoint1Latitude = this.mCursor.getColumnIndex("point1_latitude");
  private int mColumnPoint1Longitude = this.mCursor.getColumnIndex("point1_longitude");
  private int mColumnPoint1PixelX = this.mCursor.getColumnIndex("point1_pixel_x");
  private int mColumnPoint1PixelY = this.mCursor.getColumnIndex("point1_pixel_y");
  private int mColumnPoint2Latitude = this.mCursor.getColumnIndex("point2_latitude");
  private int mColumnPoint2Longitude = this.mCursor.getColumnIndex("point2_longitude");
  private int mColumnPoint2PixelX = this.mCursor.getColumnIndex("point2_pixel_x");
  private int mColumnPoint2PixelY = this.mCursor.getColumnIndex("point2_pixel_y");
  private int mColumnPoint3Latitude = this.mCursor.getColumnIndex("point3_latitude");
  private int mColumnPoint3Longitude = this.mCursor.getColumnIndex("point3_longitude");
  private int mColumnPoint3PixelX = this.mCursor.getColumnIndex("point3_pixel_x");
  private int mColumnPoint3PixelY = this.mCursor.getColumnIndex("point3_pixel_y");
  private int mColumnPoint4Latitude = this.mCursor.getColumnIndex("point4_latitude");
  private int mColumnPoint4Longitude = this.mCursor.getColumnIndex("point4_longitude");
  private int mColumnPoint4PixelX = this.mCursor.getColumnIndex("point4_pixel_x");
  private int mColumnPoint4PixelY = this.mCursor.getColumnIndex("point4_pixel_y");
  private int mColumnVenueId = this.mCursor.getColumnIndex("venue_id");
  private int mColumnVenueUUID = this.mCursor.getColumnIndex("uuid");
  private int mColumnZoneDisplayOrder = this.mCursor.getColumnIndex("zone_display_order");
  private int mColumnZoneIndex = this.mCursor.getColumnIndex("zone_index");
  private int mColumnZoneName = this.mCursor.getColumnIndex("zone_name");
  private int mColumnZoneTagText = this.mCursor.getColumnIndex("zone_tag_text");
  private int mColumnZoneUUID = this.mCursor.getColumnIndex("zone_uuid");

  private PIMapZoneDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapZoneDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapZoneDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapZoneDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapZoneDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public float getBearingPoint2Point1()
  {
    return this.mCursor.getFloat(this.mColumnBearingPoint2Point1);
  }

  public float getBearingPoint4Point3()
  {
    return this.mCursor.getFloat(this.mColumnBearingPoint4Point3);
  }

  public int getCountPixelPoint1Point2Y()
  {
    return this.mCursor.getInt(this.mColumnCountPixelPoint1Point2Y);
  }

  public int getCountPixelPoint3Point4X()
  {
    return this.mCursor.getInt(this.mColumnCountPixelPoint3Point4X);
  }

  public double getFeetPerPixelX()
  {
    return this.mCursor.getDouble(this.mColumnFeetPerPixelX);
  }

  public double getFeetPerPixelY()
  {
    return this.mCursor.getDouble(this.mColumnFeetPerPixelY);
  }

  public String getFileUri(Context paramContext)
  {
    PIFileDataCursor localPIFileDataCursor = PIContentManager.getVenueZoneImageFile(paramContext, getVenueUUID(), getImageName());
    if (localPIFileDataCursor != null) {
      try
      {
        String str = localPIFileDataCursor.getFileUri();
        return str;
      }
      finally
      {
        localPIFileDataCursor.close();
      }
    }
    return null;
  }

  public long getImageId()
  {
    return this.mCursor.getLong(this.mColumnImageId);
  }

  public String getImageName()
  {
    return this.mCursor.getString(this.mColumnImageName);
  }

  public int getImageSizeX()
  {
    return this.mCursor.getInt(this.mColumnImageSizeX);
  }

  public int getImageSizeY()
  {
    return this.mCursor.getInt(this.mColumnImageSizeY);
  }

  public PIMapVenueZone getPIMapVenueZone(Context paramContext)
  {
    PIFileDataCursor localPIFileDataCursor = PIContentManager.getVenueZoneImageFile(paramContext, getVenueUUID(), getImageName());
    if (localPIFileDataCursor != null) {
      try
      {
        PIMapVenueZone localPIMapVenueZone = new PIMapVenueZone(getZoneName(), getVenueUUID(), getZoneUUID(), getZoneTagText(), getZoneDisplayOrder(), getImageName(), getZoneIndex(), getImageSizeX(), getImageSizeY(), getFeetPerPixelX(), getFeetPerPixelY(), getPoint1Latitude(), getPoint1Longitude(), getPoint2Latitude(), getPoint2Longitude(), getPoint3Latitude(), getPoint3Longitude(), getPoint4Latitude(), getPoint4Longitude(), getPoint1PixelX(), getPoint1PixelY(), getPoint2PixelX(), getPoint2PixelY(), getPoint3PixelX(), getPoint3PixelY(), getPoint4PixelX(), getPoint4PixelY(), getCountPixelPoint1Point2Y(), getCountPixelPoint3Point4X(), getBearingPoint2Point1(), getBearingPoint4Point3(), localPIFileDataCursor.getFileUri(), isOverview());
        localPIFileDataCursor.close();
        return localPIMapVenueZone;
      } catch(Exception ex ){
    	  ex.printStackTrace();
      }
    }
    return null;
  }

  public int getPlaceCount()
  {
    return this.mCursor.getInt(this.mColumnPlaceCount);
  }

  public double getPoint1Latitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint1Latitude);
  }

  public double getPoint1Longitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint1Longitude);
  }

  public int getPoint1PixelX()
  {
    return this.mCursor.getInt(this.mColumnPoint1PixelX);
  }

  public int getPoint1PixelY()
  {
    return this.mCursor.getInt(this.mColumnPoint1PixelY);
  }

  public double getPoint2Latitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint2Latitude);
  }

  public double getPoint2Longitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint2Longitude);
  }

  public int getPoint2PixelX()
  {
    return this.mCursor.getInt(this.mColumnPoint2PixelX);
  }

  public int getPoint2PixelY()
  {
    return this.mCursor.getInt(this.mColumnPoint2PixelY);
  }

  public double getPoint3Latitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint3Latitude);
  }

  public double getPoint3Longitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint3Longitude);
  }

  public int getPoint3PixelX()
  {
    return this.mCursor.getInt(this.mColumnPoint3PixelX);
  }

  public int getPoint3PixelY()
  {
    return this.mCursor.getInt(this.mColumnPoint3PixelY);
  }

  public double getPoint4Latitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint4Latitude);
  }

  public double getPoint4Longitude()
  {
    return this.mCursor.getDouble(this.mColumnPoint4Longitude);
  }

  public int getPoint4PixelX()
  {
    return this.mCursor.getInt(this.mColumnPoint4PixelX);
  }

  public int getPoint4PixelY()
  {
    return this.mCursor.getInt(this.mColumnPoint4PixelY);
  }

  public String getUUID()
  {
    return getVenueUUID();
  }

  public Uri getUri()
  {
    return PIVenue.getVenueZoneUri(getId());
  }

  public long getVenueId()
  {
    return this.mCursor.getLong(this.mColumnVenueId);
  }

  public String getVenueUUID()
  {
    return this.mCursor.getString(this.mColumnVenueUUID);
  }

  public int getZoneDisplayOrder()
  {
    return this.mCursor.getInt(this.mColumnZoneDisplayOrder);
  }

  public int getZoneIndex()
  {
    return this.mCursor.getInt(this.mColumnZoneIndex);
  }

  public String getZoneName()
  {
    return this.mCursor.getString(this.mColumnZoneName);
  }

  public String getZoneTagText()
  {
    return this.mCursor.getString(this.mColumnZoneTagText);
  }

  public String getZoneUUID()
  {
    return this.mCursor.getString(this.mColumnZoneUUID);
  }

  public boolean isAboveGroundLevel()
  {
    return this.mCursor.getInt(this.mColumnIsAboveGroundLevel) == 1;
  }

  public boolean isOverview()
  {
    return this.mCursor.getInt(this.mColumnIsOverView) == 1;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapZoneDataCursor
 * JD-Core Version:    0.7.0.1
 */