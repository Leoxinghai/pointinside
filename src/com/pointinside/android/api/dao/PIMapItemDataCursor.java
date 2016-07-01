package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.maps.PIMapLocation;
import com.pointinside.android.api.maps.PIMapOverlayItem;

public class PIMapItemDataCursor
  extends PIMapAbstractVenueDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapItemDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapItemDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapItemDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnIlcCode = this.mCursor.getColumnIndex("ilc_code");
  private int mColumnIsPortaled = this.mCursor.getColumnIndex("is_portaled");
  private int mColumnUUID = this.mCursor.getColumnIndex("uuid");

  private PIMapItemDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapItemDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapItemDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapItemDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapItemDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getILCCode()
  {
    return this.mCursor.getString(this.mColumnIlcCode);
  }

  public PIMapItem getPIMapItem()
  {
    return new PIMapItem(new PIMapLocation(getLocationPixelX(), getLocationPixelY()), getName(), getDescription(), getILCCode(), getLocationPixelX(), getLocationPixelY(), getUUID(), getVenueId(), getZoneId(), getZoneIndex(), isPortaled());
  }

  public String getUUID()
  {
    return this.mCursor.getString(this.mColumnUUID);
  }

  public Uri getUri()
  {
    return PIVenue.getMapItemUri(getId());
  }

  public boolean isPortaled()
  {
    return this.mCursor.getInt(this.mColumnIsPortaled) == 1;
  }

  public static class PIMapItem
    extends PIMapOverlayItem
  {
    private String mILCCode;
    private boolean mIsPortaled;
    private int mLocationPixelX;
    private int mLocationPixelY;
    private String mUUID;
    private int mVenueId;
    private int mZoneId;

    public PIMapItem(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
    {
      super(paramPIMapLocation, paramString1, paramString2, paramInt5);
      this.mLocationPixelX = paramInt1;
      this.mLocationPixelY = paramInt2;
      this.mUUID = paramString3;
      this.mVenueId = paramInt3;
      this.mZoneId = paramInt4;
      this.mIsPortaled = paramBoolean;
    }

    public PIMapItem(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, String paramString4, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
    {
      super(paramPIMapLocation, paramString1, paramString2, paramInt5);
      this.mILCCode = paramString3;
      this.mLocationPixelX = paramInt1;
      this.mLocationPixelY = paramInt2;
      this.mUUID = paramString4;
      this.mVenueId = paramInt3;
      this.mZoneId = paramInt4;
      this.mIsPortaled = paramBoolean;
    }

    public String getILCCode()
    {
      return this.mILCCode;
    }

    public int getLocationPixelX()
    {
      return this.mLocationPixelX;
    }

    public int getLocationPixelY()
    {
      return this.mLocationPixelY;
    }

    public String getUUID()
    {
      return this.mUUID;
    }

    public int getVenueId()
    {
      return this.mVenueId;
    }

    public int getZoneId()
    {
      return this.mZoneId;
    }

    public boolean isPortaled()
    {
      return this.mIsPortaled;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapItemDataCursor
 * JD-Core Version:    0.7.0.1
 */