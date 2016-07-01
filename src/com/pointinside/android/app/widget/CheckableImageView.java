package com.pointinside.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class CheckableImageView
  extends ImageView
  implements Checkable
{
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private boolean mChecked;

  public CheckableImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isChecked()
  {
    return this.mChecked;
  }

  public int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked()) {
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET);
    }
    return arrayOfInt;
  }

  public void setChecked(boolean paramBoolean)
  {
    if (this.mChecked != paramBoolean)
    {
      this.mChecked = paramBoolean;
      refreshDrawableState();
    }
  }

  public void toggle()
  {
    if (this.mChecked) {}
    for (boolean bool = false;; bool = true)
    {
      setChecked(bool);
      return;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.CheckableImageView
 * JD-Core Version:    0.7.0.1
 */