package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapGeoCityDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapGeoCityDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapGeoCityDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapGeoCityDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnCountryId;
  private int mColumnCreationDate;
  private int mColumnModificationDate;
  private int mColumnName;
  private int mColumnSubdivisionId;

  private PIMapGeoCityDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnName = paramCursor.getColumnIndex("name");
    this.mColumnCountryId = paramCursor.getColumnIndex("country_id");
    this.mColumnSubdivisionId = paramCursor.getColumnIndex("subdivision_id");
    this.mColumnCreationDate = paramCursor.getColumnIndex("creation_date");
    this.mColumnModificationDate = paramCursor.getColumnIndex("modification_date");
  }

  public static PIMapGeoCityDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapGeoCityDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapGeoCityDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapGeoCityDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
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

  public int getSubdivisionId()
  {
    return this.mCursor.getInt(this.mColumnSubdivisionId);
  }

  public Uri getUri()
  {
    return PIReference.getGeoCityUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapGeoCityDataCursor
 * JD-Core Version:    0.7.0.1
 */