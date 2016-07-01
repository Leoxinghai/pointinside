package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapDisplayServiceDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapDisplayServiceDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapDisplayServiceDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapDisplayServiceDataCursor(paramAnonymousCursor);
    }
  };

  private PIMapDisplayServiceDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapDisplayServiceDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapDisplayServiceDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapDisplayServiceDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapDisplayServiceDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public long getId()
  {
    return 0L;
  }

  public String getName()
  {
    return null;
  }

  public int getServiceTypeId()
  {
    return 0;
  }

  public Uri getUri()
  {
    return null;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapDisplayServiceDataCursor
 * JD-Core Version:    0.7.0.1
 */