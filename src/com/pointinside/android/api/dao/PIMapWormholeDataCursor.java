package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.maps.PIMapLocation;
import com.pointinside.android.api.maps.PIMapOverlayItem;

public class PIMapWormholeDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapWormholeDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapWormholeDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapWormholeDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnDescription = this.mCursor.getColumnIndex("description");
  private int mColumnIsHandicapAccessible = this.mCursor.getColumnIndex("is_handicap_accessible");
  private int mColumnLocationPixelX = this.mCursor.getColumnIndex("location_pixel_x");
  private int mColumnLocationPixelY = this.mCursor.getColumnIndex("location_pixel_y");
  private int mColumnName = this.mCursor.getColumnIndex("name");
  private int mColumnWormholeTypeId = this.mCursor.getColumnIndex("wormhole_type_id");
  private int mColumnZoneId = this.mCursor.getColumnIndex("zone_id");
  private int mColumnZoneIndex = this.mCursor.getColumnIndex("zone_index");

  private PIMapWormholeDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapWormholeDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapWormholeDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapWormholeDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapWormholeDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
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

  public PIMapWormhole getPIMapWormhole()
  {
    return new PIMapWormhole(new PIMapLocation(getLocationPixelX(), getLocationPixelY()), getName(), getDescription(), getLocationPixelX(), getLocationPixelY(), getWormholeTypeId(), getZoneId(), getZoneIndex(), isHandicapAccessible());
  }

  public Uri getUri()
  {
    return PIVenue.getMapWormholeUri(getId());
  }

  public int getWormholeTypeId()
  {
    return this.mCursor.getInt(this.mColumnWormholeTypeId);
  }

  public int getZoneId()
  {
    return this.mCursor.getInt(this.mColumnZoneId);
  }

  public int getZoneIndex()
  {
    return this.mCursor.getInt(this.mColumnZoneIndex);
  }

  public boolean isHandicapAccessible()
  {
    return this.mCursor.getInt(this.mColumnIsHandicapAccessible) == 1;
  }

  public static class PIMapWormhole
    extends PIMapOverlayItem
  {
    private boolean mIsHandicapAccessible;
    private int mLocationPixelX;
    private int mLocationPixelY;
    private int mWormholeTypeId;
    private int mZoneId;

    public PIMapWormhole(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
    {
      super(paramPIMapLocation, paramString1, paramString2, paramInt5);
      this.mLocationPixelX = paramInt1;
      this.mLocationPixelY = paramInt2;
      this.mWormholeTypeId = paramInt3;
      this.mZoneId = paramInt4;
      this.mIsHandicapAccessible = paramBoolean;
    }

    public int getLocationPixelX()
    {
      return this.mLocationPixelX;
    }

    public int getLocationPixelY()
    {
      return this.mLocationPixelY;
    }

    public int getWormholeTypeId()
    {
      return this.mWormholeTypeId;
    }

    public int getZoneId()
    {
      return this.mZoneId;
    }

    public boolean isHandicapAccessible()
    {
      return this.mIsHandicapAccessible;
    }

    public boolean isWormhole()
    {
      return true;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapWormholeDataCursor
 * JD-Core Version:    0.7.0.1
 */