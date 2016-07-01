package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapVenueSummaryDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapVenueSummaryDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapVenueSummaryDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapVenueSummaryDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnAddress1;
  private int mColumnAddress2;
  private int mColumnCity;
  private int mColumnCityId;
  private int mColumnCountry;
  private int mColumnDescription;
  private int mColumnLatitude;
  private int mColumnLongitude;
  private int mColumnPhoneNumber;
  private int mColumnPostalCode;
  private int mColumnStateCode;
  private int mColumnVenueDatasetDate;
  private int mColumnVenueDatasetFile;
  private int mColumnVenueDatasetMD5;
  private int mColumnVenueId;
  private int mColumnVenueImagesDate;
  private int mColumnVenueImagesFile;
  private int mColumnVenueImagesMD5;
  private int mColumnVenueName;
  private int mColumnVenuePDEMapDate;
  private int mColumnVenuePDEMapFile;
  private int mColumnVenuePDEMapMD5;
  private int mColumnVenuePlaceImagesDate;
  private int mColumnVenuePlaceImagesFile;
  private int mColumnVenuePlaceImagesMD5;
  private int mColumnVenuePromotionsImagesDate;
  private int mColumnVenuePromotionsImagesFile;
  private int mColumnVenuePromotionsImagesMD5;
  private int mColumnVenueTypeId;
  private int mColumnVenueUUID;
  private int mColumnVenueZoneImagesDate;
  private int mColumnVenueZoneImagesFile;
  private int mColumnVenueZoneImagesMD5;
  private int mColumnWebsite;

  private PIMapVenueSummaryDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnAddress1 = paramCursor.getColumnIndex("address_line1");
    this.mColumnAddress2 = paramCursor.getColumnIndex("address_line2");
    this.mColumnCity = paramCursor.getColumnIndex("city");
    this.mColumnCountry = paramCursor.getColumnIndex("country");
    this.mColumnDescription = paramCursor.getColumnIndex("description");
    this.mColumnLatitude = paramCursor.getColumnIndex("latitude");
    this.mColumnLongitude = paramCursor.getColumnIndex("longitude");
    this.mColumnVenueName = paramCursor.getColumnIndex("venue_name");
    this.mColumnPhoneNumber = paramCursor.getColumnIndex("phone_number");
    this.mColumnPostalCode = paramCursor.getColumnIndex("postal_code");
    this.mColumnStateCode = paramCursor.getColumnIndex("state_code");
    this.mColumnVenueDatasetDate = paramCursor.getColumnIndex("venue_dataset_date");
    this.mColumnVenueDatasetFile = paramCursor.getColumnIndex("venue_dataset_file");
    this.mColumnVenueDatasetMD5 = paramCursor.getColumnIndex("venue_dataset_md5");
    this.mColumnVenueImagesDate = paramCursor.getColumnIndex("venue_images_date");
    this.mColumnVenueImagesFile = paramCursor.getColumnIndex("venue_images_file");
    this.mColumnVenueImagesMD5 = paramCursor.getColumnIndex("venue_images_md5");
    this.mColumnVenueZoneImagesDate = paramCursor.getColumnIndex("venue_zone_images_date");
    this.mColumnVenueZoneImagesFile = paramCursor.getColumnIndex("venue_zone_images_file");
    this.mColumnVenueZoneImagesMD5 = paramCursor.getColumnIndex("venue_zone_images_md5");
    this.mColumnVenuePlaceImagesDate = paramCursor.getColumnIndex("venue_place_images_date");
    this.mColumnVenuePlaceImagesFile = paramCursor.getColumnIndex("venue_place_images_file");
    this.mColumnVenuePlaceImagesMD5 = paramCursor.getColumnIndex("venue_place_images_md5");
    this.mColumnVenuePromotionsImagesDate = paramCursor.getColumnIndex("venue_promotion_images_date");
    this.mColumnVenuePromotionsImagesFile = paramCursor.getColumnIndex("venue_promotion_images_file");
    this.mColumnVenuePromotionsImagesMD5 = paramCursor.getColumnIndex("venue_promotion_images_md5");
    this.mColumnVenuePDEMapDate = paramCursor.getColumnIndex("venue_pdemap_date");
    this.mColumnVenuePDEMapFile = paramCursor.getColumnIndex("venue_pdemap_file");
    this.mColumnVenuePDEMapMD5 = paramCursor.getColumnIndex("venue_pdemap_md5");
    this.mColumnWebsite = paramCursor.getColumnIndex("website");
    this.mColumnCityId = paramCursor.getColumnIndex("city_id");
    this.mColumnVenueId = paramCursor.getColumnIndex("venue_id");
    this.mColumnVenueUUID = paramCursor.getColumnIndex("venue_uuid");
    this.mColumnVenueTypeId = paramCursor.getColumnIndex("venue_type_id");
  }

  public static PIMapVenueSummaryDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapVenueSummaryDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapVenueSummaryDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapVenueSummaryDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getAddress1()
  {
    return this.mCursor.getString(this.mColumnAddress1);
  }

  public String getAddress2()
  {
    return this.mCursor.getString(this.mColumnAddress2);
  }

  public String getCity()
  {
    return this.mCursor.getString(this.mColumnCity);
  }

  public long getCityId()
  {
    return this.mCursor.getLong(this.mColumnCityId);
  }

  public String getCountry()
  {
    return this.mCursor.getString(this.mColumnCountry);
  }

  public String getDescription()
  {
    return this.mCursor.getString(this.mColumnDescription);
  }

  public String getFormattedCityState()
  {
    return getCity() + ", " + getState();
  }

  public String getFormattedCityStateZip()
  {
    return getCity() + ", " + getState() + " " + getPostalCode();
  }

  public double getLatitude()
  {
    return this.mCursor.getDouble(this.mColumnLatitude);
  }

  public double getLongitude()
  {
    return this.mCursor.getDouble(this.mColumnLongitude);
  }

  public PIMapVenueSummary getPIMapVenueSummary()
  {
    return new PIMapVenueSummary(getId(), getUri(), getAddress1(), getAddress2(), getCity(), getCountry(), getDescription(), getLatitude(), getLongitude(), getVenueName(), getPhoneNumber(), getPostalCode(), getState(), getVenueDatasetDate(), getVenueDatasetFile(), getVenueDatasetMD5(), getVenueImagesDate(), getVenueImagesFile(), getVenueImagesMD5(), getVenueZoneImagesDate(), getVenueZoneImagesFile(), getVenueZoneImagesMD5(), getVenuePlaceImagesDate(), getVenuePlaceImagesFile(), getVenuePlaceImagesMD5(), getVenuePromotionsImagesDate(), getVenuePromotionsImagesFile(), getVenuePromotionsImagesMD5(), getVenuePDEMapDate(), getVenuePDEMapFile(), getVenuePDEMapMD5(), getWebsite(), getCityId(), getVenueId(), getVenueUUID(), getVenueTypeId());
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
    return PIReference.getVenueSummaryUri(getId());
  }

  public String getVenueDatasetDate()
  {
    return this.mCursor.getString(this.mColumnVenueDatasetDate);
  }

  public String getVenueDatasetFile()
  {
    return this.mCursor.getString(this.mColumnVenueDatasetFile);
  }

  public String getVenueDatasetMD5()
  {
    return this.mCursor.getString(this.mColumnVenueDatasetMD5);
  }

  public long getVenueId()
  {
    return this.mCursor.getLong(this.mColumnVenueId);
  }

  public String getVenueImagesDate()
  {
    return this.mCursor.getString(this.mColumnVenueImagesDate);
  }

  public String getVenueImagesFile()
  {
    return this.mCursor.getString(this.mColumnVenueImagesFile);
  }

  public String getVenueImagesMD5()
  {
    return this.mCursor.getString(this.mColumnVenueImagesMD5);
  }

  public String getVenueName()
  {
    return this.mCursor.getString(this.mColumnVenueName);
  }

  public String getVenuePDEMapDate()
  {
    return this.mCursor.getString(this.mColumnVenuePDEMapDate);
  }

  public String getVenuePDEMapFile()
  {
    return this.mCursor.getString(this.mColumnVenuePDEMapFile);
  }

  public String getVenuePDEMapMD5()
  {
    return this.mCursor.getString(this.mColumnVenuePDEMapMD5);
  }

  public String getVenuePlaceImagesDate()
  {
    return this.mCursor.getString(this.mColumnVenuePlaceImagesDate);
  }

  public String getVenuePlaceImagesFile()
  {
    return this.mCursor.getString(this.mColumnVenuePlaceImagesFile);
  }

  public String getVenuePlaceImagesMD5()
  {
    return this.mCursor.getString(this.mColumnVenuePlaceImagesMD5);
  }

  public String getVenuePromotionsImagesDate()
  {
    return this.mCursor.getString(this.mColumnVenuePromotionsImagesDate);
  }

  public String getVenuePromotionsImagesFile()
  {
    return this.mCursor.getString(this.mColumnVenuePromotionsImagesFile);
  }

  public String getVenuePromotionsImagesMD5()
  {
    return this.mCursor.getString(this.mColumnVenuePromotionsImagesMD5);
  }

  public int getVenueTypeId()
  {
    return this.mCursor.getInt(this.mColumnVenueTypeId);
  }

  public String getVenueUUID()
  {
    return this.mCursor.getString(this.mColumnVenueUUID);
  }

  public String getVenueZoneImagesDate()
  {
    return this.mCursor.getString(this.mColumnVenueZoneImagesDate);
  }

  public String getVenueZoneImagesFile()
  {
    return this.mCursor.getString(this.mColumnVenueZoneImagesFile);
  }

  public String getVenueZoneImagesMD5()
  {
    return this.mCursor.getString(this.mColumnVenueZoneImagesMD5);
  }

  public String getWebsite()
  {
    return this.mCursor.getString(this.mColumnWebsite);
  }

  public static class PIMapVenueSummary
  {
    private String mAddress1;
    private String mAddress2;
    private String mCity;
    private long mCityId;
    private String mCountry;
    private String mDescription;
    private long mId;
    private double mLatitude;
    private double mLongitude;
    private String mPhoneNumber;
    private String mPostalCode;
    private String mStateCode;
    private Uri mUri;
    private String mVenueDatasetDate;
    private String mVenueDatasetFile;
    private String mVenueDatasetMD5;
    private long mVenueId;
    private String mVenueImagesDate;
    private String mVenueImagesFile;
    private String mVenueImagesMD5;
    private String mVenueName;
    private String mVenuePDEMapDate;
    private String mVenuePDEMapFile;
    private String mVenuePDEMapMD5;
    private String mVenuePlaceImagesDate;
    private String mVenuePlaceImagesFile;
    private String mVenuePlaceImagesMD5;
    private String mVenuePromotionsImagesDate;
    private String mVenuePromotionsImagesFile;
    private String mVenuePromotionsImagesMD5;
    private int mVenueTypeId;
    private String mVenueUUID;
    private String mVenueZoneImagesDate;
    private String mVenueZoneImagesFile;
    private String mVenueZoneImagesMD5;
    private String mWebsite;

    public PIMapVenueSummary(long paramLong1, Uri paramUri, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, double paramDouble1, double paramDouble2, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13, String paramString14, String paramString15, String paramString16, String paramString17, String paramString18, String paramString19, String paramString20, String paramString21, String paramString22, String paramString23, String paramString24, String paramString25, String paramString26, String paramString27, String paramString28, long paramLong2, long paramLong3, String paramString29, int paramInt)
    {
      this.mId = paramLong1;
      this.mUri = paramUri;
      this.mAddress1 = paramString1;
      this.mAddress2 = paramString2;
      this.mCity = paramString3;
      this.mCountry = paramString4;
      this.mDescription = paramString5;
      this.mLatitude = paramDouble1;
      this.mLongitude = paramDouble2;
      this.mVenueName = paramString6;
      this.mPhoneNumber = paramString7;
      this.mPostalCode = paramString8;
      this.mStateCode = paramString9;
      this.mVenueDatasetDate = paramString10;
      this.mVenueDatasetFile = paramString11;
      this.mVenueDatasetMD5 = paramString12;
      this.mVenueImagesDate = paramString13;
      this.mVenueImagesFile = paramString14;
      this.mVenueImagesMD5 = paramString15;
      this.mVenueZoneImagesDate = paramString16;
      this.mVenueZoneImagesFile = paramString17;
      this.mVenueZoneImagesMD5 = paramString18;
      this.mVenuePlaceImagesDate = paramString19;
      this.mVenuePlaceImagesFile = paramString20;
      this.mVenuePlaceImagesMD5 = paramString21;
      this.mVenuePromotionsImagesDate = paramString22;
      this.mVenuePromotionsImagesFile = paramString23;
      this.mVenuePromotionsImagesMD5 = paramString24;
      this.mVenuePDEMapDate = paramString25;
      this.mVenuePDEMapFile = paramString26;
      this.mVenuePDEMapMD5 = paramString27;
      this.mWebsite = paramString28;
      this.mCityId = paramLong2;
      this.mVenueId = paramLong3;
      this.mVenueUUID = paramString29;
      this.mVenueTypeId = paramInt;
    }

    public String getAddress1()
    {
      return this.mAddress1;
    }

    public String getAddress2()
    {
      return this.mAddress2;
    }

    public String getCity()
    {
      return this.mCity;
    }

    public long getCityId()
    {
      return this.mCityId;
    }

    public String getCountry()
    {
      return this.mCountry;
    }

    public String getDescription()
    {
      return this.mDescription;
    }

    public long getId()
    {
      return this.mId;
    }

    public double getLatitude()
    {
      return this.mLatitude;
    }

    public double getLongitude()
    {
      return this.mLongitude;
    }

    public String getPhoneNumber()
    {
      return this.mPhoneNumber;
    }

    public String getPostalCode()
    {
      return this.mPostalCode;
    }

    public String getStateCode()
    {
      return this.mStateCode;
    }

    public Uri getUri()
    {
      return this.mUri;
    }

    public String getVenueDatasetDate()
    {
      return this.mVenueDatasetDate;
    }

    public String getVenueDatasetFile()
    {
      return this.mVenueDatasetFile;
    }

    public String getVenueDatasetMD5()
    {
      return this.mVenueDatasetMD5;
    }

    public long getVenueId()
    {
      return this.mVenueId;
    }

    public String getVenueImagesDate()
    {
      return this.mVenueImagesDate;
    }

    public String getVenueImagesFile()
    {
      return this.mVenueImagesFile;
    }

    public String getVenueImagesMD5()
    {
      return this.mVenueImagesMD5;
    }

    public String getVenueName()
    {
      return this.mVenueName;
    }

    public String getVenuePDEMapDate()
    {
      return this.mVenuePDEMapDate;
    }

    public String getVenuePDEMapFile()
    {
      return this.mVenuePDEMapFile;
    }

    public String getVenuePDEMapMD5()
    {
      return this.mVenuePDEMapMD5;
    }

    public String getVenuePlaceImagesDate()
    {
      return this.mVenuePlaceImagesDate;
    }

    public String getVenuePlaceImagesFile()
    {
      return this.mVenuePlaceImagesFile;
    }

    public String getVenuePlaceImagesMD5()
    {
      return this.mVenuePlaceImagesMD5;
    }

    public String getVenuePromotionsImagesDate()
    {
      return this.mVenuePromotionsImagesDate;
    }

    public String getVenuePromotionsImagesFile()
    {
      return this.mVenuePromotionsImagesFile;
    }

    public String getVenuePromotionsImagesMD5()
    {
      return this.mVenuePromotionsImagesMD5;
    }

    public int getVenueTypeId()
    {
      return this.mVenueTypeId;
    }

    public String getVenueUUID()
    {
      return this.mVenueUUID;
    }

    public String getVenueZoneImagesDate()
    {
      return this.mVenueZoneImagesDate;
    }

    public String getVenueZoneImagesFile()
    {
      return this.mVenueZoneImagesFile;
    }

    public String getVenueZoneImagesMD5()
    {
      return this.mVenueZoneImagesMD5;
    }

    public String getWebsite()
    {
      return this.mWebsite;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor
 * JD-Core Version:    0.7.0.1
 */