package com.pointinside.android.api.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import com.pointinside.android.api.dao.AbstractDataCursor;

public abstract class AbstractDataCursorAdapter<T extends AbstractDataCursor>
  extends CursorAdapter
{
  private final Context mContext;
  protected T mDataCursor;
  private final LayoutInflater mInflater;

  public AbstractDataCursorAdapter(Context paramContext, Cursor paramCursor, boolean paramBoolean)
  {
    super(paramContext, paramCursor, paramBoolean);
    this.mContext = paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    allocInternal(paramCursor);
  }

  private void allocInternal(Cursor paramCursor)
  {
    if ((paramCursor != null) && (paramCursor.getCount() > 0))
    {
      onAllocInternal(paramCursor);
      return;
    }
    this.mDataCursor = null;
  }

  public int findItem(Context paramContext, Uri paramUri)
  {
    int j;
    if (paramUri == null)
    {
      j = -1;
      return j;
    }
    for (int i = getCount();; i = j)
    {
      j = i - 1;
      if (i <= 0) {
        return -1;
      }
      if (paramUri.equals(getDataCursor(j).getUri())) {
        break;
      }
    }
    return -1;
  }

  protected Context getContext()
  {
    return this.mContext;
  }

  public T getDataCursor(int paramInt)
  {
    if ((paramInt >= 0) && (getCount() >= 0))
    {
      this.mDataCursor.setPosition(paramInt);
      return this.mDataCursor;
    }
    return null;
  }

  protected LayoutInflater getInflater()
  {
    return this.mInflater;
  }

  public void notifyDataSetChanged()
  {
    allocInternal(getCursor());
    super.notifyDataSetChanged();
  }

  public void notifyDataSetInvalidated()
  {
    this.mDataCursor = null;
    super.notifyDataSetInvalidated();
  }

  protected abstract void onAllocInternal(Cursor paramCursor);
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.widget.AbstractDataCursorAdapter
 * JD-Core Version:    0.7.0.1
 */