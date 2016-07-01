package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;

public abstract class AbstractDataCursor
{
  protected int mColumnId;
  protected Cursor mCursor;

  AbstractDataCursor(Cursor paramCursor)
  {
    if ((paramCursor == null) || (paramCursor.getCount() == 0)) {
      throw new IllegalArgumentException("Cursor cannot be null or empty");
    }
    this.mCursor = paramCursor;
    this.mColumnId = this.mCursor.getColumnIndex("_id");
  }

  protected static Uri parseUriNullSafe(String paramString)
  {
    if (paramString != null) {
      return Uri.parse(paramString);
    }
    return null;
  }

  public void close()
  {
    this.mCursor.close();
  }

  public int getCount()
  {
    return this.mCursor.getCount();
  }

  public Cursor getCursor()
  {
    return this.mCursor;
  }

  public long getId()
  {
    return this.mCursor.getLong(this.mColumnId);
  }

  public int getPosition()
  {
    return this.mCursor.getPosition();
  }

  public abstract Uri getUri();

  public boolean isAfterLast()
  {
    return this.mCursor.isAfterLast();
  }

  public boolean isBeforeFirst()
  {
    return this.mCursor.isBeforeFirst();
  }

  public boolean isClosed()
  {
    return this.mCursor.isClosed();
  }

  public boolean isFirst()
  {
    return this.mCursor.isFirst();
  }

  public boolean isLast()
  {
    return this.mCursor.isLast();
  }

  public boolean isNull(int paramInt)
  {
    return this.mCursor.isNull(paramInt);
  }

  public boolean moveToFirst()
  {
    return this.mCursor.moveToFirst();
  }

  public boolean moveToLast()
  {
    return this.mCursor.moveToLast();
  }

  public boolean moveToNext()
  {
    return this.mCursor.moveToNext();
  }

  public boolean moveToPosition(int paramInt)
  {
    return this.mCursor.moveToPosition(paramInt);
  }

  public boolean moveToPrevious()
  {
    return this.mCursor.moveToPrevious();
  }

  public void setPosition(int paramInt)
  {
    this.mCursor.moveToPosition(paramInt);
  }

  protected static abstract class Creator<T extends AbstractDataCursor>
  {
    public abstract T init(Cursor paramCursor);

    public T newInstance(Cursor paramCursor)
    {
      if (paramCursor != null)
      {
        if (paramCursor.moveToFirst()) {
          return init(paramCursor);
        }
        paramCursor.close();
      }
      return null;
    }

    public AbstractDataCursor newInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
    {
      AbstractDataCursor localAbstractDataCursor = null;
      if (paramUri != null) {
        localAbstractDataCursor = newInstance(paramPIMapDataset.query(paramUri, null, null, null, null));
      }
      return localAbstractDataCursor;
    }
  }
}

