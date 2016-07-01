package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapServiceTypeDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapServiceTypeDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapServiceTypeDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapServiceTypeDataCursor(paramAnonymousCursor);
    }
  };

  private PIMapServiceTypeDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapServiceTypeDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapServiceTypeDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapServiceTypeDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapServiceTypeDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public long getId()
  {
    return 0L;
  }

  public Uri getUri()
  {
    return null;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapServiceTypeDataCursor
 * JD-Core Version:    0.7.0.1
 */