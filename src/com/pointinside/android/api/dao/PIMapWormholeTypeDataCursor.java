package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public class PIMapWormholeTypeDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapWormholeTypeDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapWormholeTypeDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapWormholeTypeDataCursor(paramAnonymousCursor);
    }
  };

  private PIMapWormholeTypeDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
  }

  public static PIMapWormholeTypeDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapWormholeTypeDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapWormholeTypeDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapWormholeTypeDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public String getDescription()
  {
    return null;
  }

  public String getName()
  {
    return null;
  }

  public Uri getUri()
  {
    return null;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapWormholeTypeDataCursor
 * JD-Core Version:    0.7.0.1
 */