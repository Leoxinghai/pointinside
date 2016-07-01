package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapGeoCountryDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapGeoCountryDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapGeoCountryDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapGeoCountryDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnCountryCode;
  private int mColumnCreationDate;
  private int mColumnModificationDate;
  private int mColumnName;

  private PIMapGeoCountryDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnName = paramCursor.getColumnIndex("name");
    this.mColumnCountryCode = paramCursor.getColumnIndex("country_code");
    this.mColumnCreationDate = paramCursor.getColumnIndex("creation_date");
    this.mColumnModificationDate = paramCursor.getColumnIndex("modification_date");
  }

  public static PIMapGeoCountryDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapGeoCountryDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapGeoCountryDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapGeoCountryDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getCode()
  {
    return this.mCursor.getString(this.mColumnCountryCode);
  }

  public String getCreationDate()
  {
    return this.mCursor.getString(this.mColumnCreationDate);
  }

  public String getModificationDate()
  {
    return this.mCursor.getString(this.mColumnModificationDate);
  }

  public String getName()
  {
    return this.mCursor.getString(this.mColumnName);
  }

  public Uri getUri()
  {
    return PIReference.getGeoCountryUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapGeoCountryDataCursor
 * JD-Core Version:    0.7.0.1
 */