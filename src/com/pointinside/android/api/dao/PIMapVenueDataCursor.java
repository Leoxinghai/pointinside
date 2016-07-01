package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import com.pointinside.android.api.PIMapVenue;

public class PIMapVenueDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapVenueDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapVenueDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapVenueDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnAboveGroundZoneCount = this.mCursor.getColumnIndex("above_ground_zone_count");
  private int mColumnAddress1;
  private int mColumnAddress2;
  private int mColumnBelowGroundZoneCount = this.mCursor.getColumnIndex("below_ground_zone_count");
  private int mColumnCity;
  private int mColumnCountry;
  private int mColumnDescription = this.mCursor.getColumnIndex("description");
  private int mColumnEmail = this.mCursor.getColumnIndex("email");
  private int mColumnImageId;
  private int mColumnIsValidShopNumbers;
  private int mColumnName;
  private int mColumnOperationMinutesId;
  private int mColumnOrganizationId;
  private int mColumnPhoneNumber;
  private int mColumnPostalCode;
  private int mColumnStateCode;
  private int mColumnUserDefinedId;
  private int mColumnVenueClassification;
  private int mColumnVenueType;
  private int mColumnVenueTypeId;
  private int mColumnVenueUUID;
  private int mColumnWebsite;
  private int mColumnWormholeCount;

  private PIMapVenueDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnAddress1 = paramCursor.getColumnIndex("address_line1");
    this.mColumnAddress2 = paramCursor.getColumnIndex("address_line2");
    this.mColumnIsValidShopNumbers = this.mCursor.getColumnIndex("is_valid_shop_numbers");
    this.mColumnCity = paramCursor.getColumnIndex("city");
    this.mColumnCountry = paramCursor.getColumnIndex("country");
    this.mColumnPostalCode = paramCursor.getColumnIndex("postal_code");
    this.mColumnStateCode = paramCursor.getColumnIndex("state_code");
    this.mColumnName = this.mCursor.getColumnIndex("name");
    this.mColumnPhoneNumber = this.mCursor.getColumnIndex("phone_number");
    this.mColumnUserDefinedId = this.mCursor.getColumnIndex("user_defined_id");
    this.mColumnVenueClassification = this.mCursor.getColumnIndex("venue_classification");
    this.mColumnWebsite = this.mCursor.getColumnIndex("website");
    this.mColumnWormholeCount = this.mCursor.getColumnIndex("wormhole_count");
    this.mColumnImageId = this.mCursor.getColumnIndex("image_id");
    this.mColumnOperationMinutesId = this.mCursor.getColumnIndex("operation_times_id");
    this.mColumnOrganizationId = this.mCursor.getColumnIndex("organization_id");
    this.mColumnVenueTypeId = this.mCursor.getColumnIndex("venue_type_id");
    this.mColumnVenueUUID = this.mCursor.getColumnIndex("uuid");
  }

  public static PIMapVenueDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapVenueDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapVenueDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapVenueDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public int getAboveGroundZoneCount()
  {
    return this.mCursor.getInt(this.mColumnAboveGroundZoneCount);
  }

  public String getAddress1()
  {
    return this.mCursor.getString(this.mColumnAddress1);
  }

  public String getAddress2()
  {
    return this.mCursor.getString(this.mColumnAddress2);
  }

  public int getBelowGroundZoneCount()
  {
    return this.mCursor.getInt(this.mColumnBelowGroundZoneCount);
  }

  public String getCity()
  {
    return this.mCursor.getString(this.mColumnCity);
  }

  public String getCountry()
  {
    return this.mCursor.getString(this.mColumnCountry);
  }

  public String getDescription()
  {
    return this.mCursor.getString(this.mColumnDescription);
  }

  public String getEmail()
  {
    return this.mCursor.getString(this.mColumnEmail);
  }

  public String getFormattedCityStateZip()
  {
    return getCity() + ", " + getState() + " " + getPostalCode();
  }

  public int getImageId()
  {
    return this.mCursor.getInt(this.mColumnImageId);
  }

  public Bitmap getLogo(PIMapVenue paramPIMapVenue)
  {
    if (paramPIMapVenue != null) {
      return paramPIMapVenue.getVenueImage(getImageId());
    }
    return null;
  }

  public String getName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public int getOperationMinutesId()
  {
    return this.mCursor.getInt(this.mColumnOperationMinutesId);
  }

  public int getOrganizationId()
  {
    return this.mCursor.getInt(this.mColumnOrganizationId);
  }

  public String getPhoneNumber()
  {
    return this.mCursor.getString(this.mColumnPhoneNumber);
  }

  public String getPostalCode()
  {
    return this.mCursor.getString(this.mColumnPostalCode);
  }

  public String getState()
  {
    return this.mCursor.getString(this.mColumnStateCode);
  }

  public Uri getUri()
  {
    return PIVenue.getVenueUri(getId());
  }

  public String getUserDefinedId()
  {
    return this.mCursor.getString(this.mColumnUserDefinedId);
  }

  public String getVenueClassification()
  {
    return this.mCursor.getString(this.mColumnVenueClassification);
  }

  public String getVenueType()
  {
    return this.mCursor.getString(this.mColumnVenueType);
  }

  public int getVenueTypeId()
  {
    return this.mCursor.getInt(this.mColumnVenueTypeId);
  }

  public String getVenueUUID()
  {
    return this.mCursor.getString(this.mColumnVenueUUID);
  }

  public String getWebsite()
  {
    return this.mCursor.getString(this.mColumnWebsite);
  }

  public int getWormholeCount()
  {
    return this.mCursor.getInt(this.mColumnWormholeCount);
  }

  public boolean isValidShopNumbers()
  {
    return this.mCursor.getInt(this.mColumnIsValidShopNumbers) == 1;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapVenueDataCursor
 * JD-Core Version:    0.7.0.1
 */