package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public abstract interface PIMapDataset
{
  public abstract Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2);
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapDataset
 * JD-Core Version:    0.7.0.1
 */