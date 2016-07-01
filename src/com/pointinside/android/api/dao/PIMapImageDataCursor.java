package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapImageDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapImageDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapImageDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapImageDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnImageName = this.mCursor.getColumnIndex("name");

  private PIMapImageDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapImageDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapImageDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapImageDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapImageDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getImageName()
  {
    return this.mCursor.getString(this.mColumnImageName);
  }

  public Uri getUri()
  {
    return PIVenue.getMapImageUri(getId());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapImageDataCursor
 * JD-Core Version:    0.7.0.1
 */