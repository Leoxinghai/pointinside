package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapAddressDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapAddressDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapAddressDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapAddressDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnAddress1;
  private int mColumnAddress2;
  private int mColumnCity;
  private int mColumnCountry;
  private int mColumnPostalCode;
  private int mColumnStateCode;

  private PIMapAddressDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnAddress1 = paramCursor.getColumnIndex("address_line1");
    this.mColumnAddress2 = paramCursor.getColumnIndex("address_line2");
    this.mColumnCity = paramCursor.getColumnIndex("city");
    this.mColumnCountry = paramCursor.getColumnIndex("country");
    this.mColumnPostalCode = paramCursor.getColumnIndex("postal_code");
    this.mColumnStateCode = paramCursor.getColumnIndex("state_code");
  }

  public static PIMapAddressDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapAddressDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapAddressDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapAddressDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
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

  public String getCountry()
  {
    return this.mCursor.getString(this.mColumnCountry);
  }

  public String getFormattedCityStateZip()
  {
    return getCity() + ", " + getState() + " " + getPostalCode();
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
    return PIVenue.getAddressUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapAddressDataCursor
 * JD-Core Version:    0.7.0.1
 */