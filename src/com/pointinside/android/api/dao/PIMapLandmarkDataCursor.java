package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.graphics.Bitmap;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.maps.PIMapLocation;
import com.pointinside.android.api.maps.PIMapOverlayItem;

public abstract class PIMapLandmarkDataCursor
  extends PIMapAbstractVenueDataCursor
{
  private int mColumnIlcCode = this.mCursor.getColumnIndex("ilc_code");
  private int mColumnImageId = this.mCursor.getColumnIndex("image_id");
  private int mColumnIsDisplayable = this.mCursor.getColumnIndex("is_displayable");
  private int mColumnIsPortaled = this.mCursor.getColumnIndex("is_portaled");
  private int mColumnPhoneNumber = this.mCursor.getColumnIndex("phone_number");
  private int mColumnPlaceTypeId = this.mCursor.getColumnIndex("place_type_id");
  private int mColumnPlaceTypeName = this.mCursor.getColumnIndex("place_type_name");
  private int mColumnServiceTypeId = this.mCursor.getColumnIndex("service_type_id");
  private int mColumnServiceTypeName = this.mCursor.getColumnIndex("service_type_name");
  private int mColumnServiceTypeUUID = this.mCursor.getColumnIndex("service_type_uuid");
  private int mColumnUUID = this.mCursor.getColumnIndex("uuid");

  protected PIMapLandmarkDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public String getILCCode()
  {
    return this.mCursor.getString(this.mColumnIlcCode);
  }

  public long getImageId()
  {
    return this.mCursor.getLong(this.mColumnImageId);
  }

  public Bitmap getLogo(PIMapVenue paramPIMapVenue)
  {
    if (paramPIMapVenue != null) {
      return paramPIMapVenue.getPlaceImage(getImageId());
    }
    return null;
  }

  public PIMapPlace getPIMapPlace()
  {
    return new PIMapPlace(new PIMapLocation(getLocationPixelX(), getLocationPixelY()), getName(), getDescription(), getZoneIndex(), getPlaceTypeId(), getPlaceTypeName(), getServiceTypeId(), getServiceTypeName(), getServiceTypeUUID(), getUUID(), getLocationPixelX(), getLocationPixelY(), getILCCode());
  }

  public String getPhoneNumber()
  {
    return this.mCursor.getString(this.mColumnPhoneNumber);
  }

  public int getPlaceTypeId()
  {
    return this.mCursor.getInt(this.mColumnPlaceTypeId);
  }

  public String getPlaceTypeName()
  {
    return this.mCursor.getString(this.mColumnPlaceTypeName);
  }

  public int getServiceTypeId()
  {
    return this.mCursor.getInt(this.mColumnServiceTypeId);
  }

  public String getServiceTypeName()
  {
    return this.mCursor.getString(this.mColumnServiceTypeName);
  }

  public String getServiceTypeUUID()
  {
    return this.mCursor.getString(this.mColumnServiceTypeUUID);
  }

  public String getUUID()
  {
    return this.mCursor.getString(this.mColumnUUID);
  }

  public boolean isDisplayable()
  {
    return this.mCursor.getInt(this.mColumnIsDisplayable) == 1;
  }

  public boolean isPortaled()
  {
    return this.mCursor.getInt(this.mColumnIsPortaled) == 1;
  }

  public static class PIMapPlace
    extends PIMapOverlayItem
  {
    private String mILCCode;
    private int mLocationPixelX;
    private int mLocationPixelY;
    private int mPlaceTypeId;
    private String mPlaceTypeName;
    private int mServiceTypeId;
    private String mServiceTypeName;
    private String mServiceTypeUUID;
    private String mUUID;

    public PIMapPlace(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3, int paramInt3, String paramString4, String paramString5, String paramString6, int paramInt4, int paramInt5)
    {
      super(paramPIMapLocation,paramString1, paramString2, paramInt1);
      this.mPlaceTypeId = paramInt2;
      this.mPlaceTypeName = paramString3;
      this.mServiceTypeId = paramInt3;
      this.mServiceTypeName = paramString4;
      this.mServiceTypeUUID = paramString5;
      this.mUUID = paramString6;
      this.mLocationPixelX = paramInt4;
      this.mLocationPixelY = paramInt5;
    }

    public PIMapPlace(PIMapLocation paramPIMapLocation, String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3, int paramInt3, String paramString4, String paramString5, String paramString6, int paramInt4, int paramInt5, String paramString7)
    {
      super(paramPIMapLocation, paramString1, paramString2, paramInt1);
      this.mPlaceTypeId = paramInt2;
      this.mPlaceTypeName = paramString3;
      this.mServiceTypeId = paramInt3;
      this.mServiceTypeName = paramString4;
      this.mServiceTypeUUID = paramString5;
      this.mUUID = paramString6;
      this.mILCCode = paramString7;
      this.mLocationPixelX = paramInt4;
      this.mLocationPixelY = paramInt5;
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

    public int getPlaceTypeId()
    {
      return this.mPlaceTypeId;
    }

    public String getPlaceTypeName()
    {
      return this.mPlaceTypeName;
    }

    public int getServiceTypeId()
    {
      return this.mServiceTypeId;
    }

    public String getServiceTypeName()
    {
      return this.mServiceTypeName;
    }

    public String getServiceTypeUUID()
    {
      return this.mServiceTypeUUID;
    }

    public String getUUID()
    {
      return this.mUUID;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapLandmarkDataCursor
 * JD-Core Version:    0.7.0.1
 */