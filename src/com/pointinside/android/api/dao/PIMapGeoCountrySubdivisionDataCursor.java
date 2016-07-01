package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapGeoCountrySubdivisionDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapGeoCountrySubdivisionDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapGeoCountrySubdivisionDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapGeoCountrySubdivisionDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnCountryId;
  private int mColumnCreationDate;
  private int mColumnModificationDate;
  private int mColumnName;
  private int mColumnSubdivisionCode;

  private PIMapGeoCountrySubdivisionDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnName = paramCursor.getColumnIndex("name");
    this.mColumnCountryId = paramCursor.getColumnIndex("country_id");
    this.mColumnSubdivisionCode = paramCursor.getColumnIndex("subdivision_code");
    this.mColumnCreationDate = paramCursor.getColumnIndex("creation_date");
    this.mColumnModificationDate = paramCursor.getColumnIndex("modification_date");
  }

  public static PIMapGeoCountrySubdivisionDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapGeoCountrySubdivisionDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapGeoCountrySubdivisionDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapGeoCountrySubdivisionDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getCode()
  {
    return this.mCursor.getString(this.mColumnSubdivisionCode);
  }

  public int getCountryId()
  {
    return this.mCursor.getInt(this.mColumnCountryId);
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
    return PIReference.getGeoCountrySubdivisionUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapGeoCountrySubdivisionDataCursor
 * JD-Core Version:    0.7.0.1
 */