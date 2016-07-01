package com.pointinside.android.app.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public abstract class DialogCompatHelper
{
  protected OnCreateDialogListener mOnCreateDialogListener;
  protected OnPrepareDialogListener mOnPrepareDialogListener;

  public static DialogCompatHelper newInstance(Activity paramActivity)
  {
    if (Build.VERSION.SDK_INT >= 8) {
      return new FroyoAndBeyond(paramActivity);
    }
    return new Eclair(paramActivity);
  }

  public abstract Dialog onCreateDialog(int paramInt, Bundle paramBundle);

  public abstract void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle);

  public abstract void onRestoreInstanceState(Bundle paramBundle);

  public abstract void onSaveInstanceState(Bundle paramBundle);

  public void setOnCreateDialogListener(OnCreateDialogListener paramOnCreateDialogListener)
  {
    this.mOnCreateDialogListener = paramOnCreateDialogListener;
  }

  public void setOnPrepareDialogListener(OnPrepareDialogListener paramOnPrepareDialogListener)
  {
    this.mOnPrepareDialogListener = paramOnPrepareDialogListener;
  }

  public abstract void showDialog(int paramInt, Bundle paramBundle);

  private static class Eclair
    extends DialogCompatHelper
  {
    private static final String SAVED_DIALOG_ARGS = Eclair.class.getName() + ":dialogArgs";
    public static final HashMap<Integer, Bundle> mArgs = new HashMap();
    private final Activity mContext;

    public Eclair(Activity paramActivity)
    {
      this.mContext = paramActivity;
    }

    public Dialog onCreateDialog(int paramInt, Bundle paramBundle)
    {
      if (paramBundle != null) {
        throw new AssertionError();
      }
      return this.mOnCreateDialogListener.onCreateDialog(paramInt, (Bundle)mArgs.get(Integer.valueOf(paramInt)));
    }

    public void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle)
    {
      if (paramBundle != null) {
        throw new AssertionError();
      }
      this.mOnPrepareDialogListener.onPrepareDialog(paramInt, paramDialog, (Bundle)mArgs.get(Integer.valueOf(paramInt)));
    }

    public void onRestoreInstanceState(Bundle paramBundle)
    {
      Bundle localBundle = paramBundle.getBundle(SAVED_DIALOG_ARGS);
      if (!mArgs.isEmpty()) {
        throw new IllegalStateException("Twice restored instance state?");
      }
      Iterator localIterator = localBundle.keySet().iterator();
      for (;;)
      {
        if (!localIterator.hasNext()) {
          return;
        }
        String str = (String)localIterator.next();
        mArgs.put(new Integer(str), localBundle.getBundle(str));
      }
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      Bundle localBundle = new Bundle();
      Iterator localIterator = mArgs.entrySet().iterator();
      for (;;)
      {
        if (!localIterator.hasNext())
        {
          paramBundle.putBundle(SAVED_DIALOG_ARGS, localBundle);
          return;
        }
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localBundle.putBundle(((Integer)localEntry.getKey()).toString(), (Bundle)localEntry.getValue());
      }
    }

    public void showDialog(int paramInt, Bundle paramBundle)
    {
      if (paramBundle != null) {
        mArgs.put(Integer.valueOf(paramInt), paramBundle);
      }
      this.mContext.showDialog(paramInt);
    }
  }

  private static class FroyoAndBeyond
    extends DialogCompatHelper
  {
    private final Activity mContext;

    public FroyoAndBeyond(Activity paramActivity)
    {
      this.mContext = paramActivity;
    }

    public Dialog onCreateDialog(int paramInt, Bundle paramBundle)
    {
      return this.mOnCreateDialogListener.onCreateDialog(paramInt, paramBundle);
    }

    public void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle)
    {
      this.mOnPrepareDialogListener.onPrepareDialog(paramInt, paramDialog, paramBundle);
    }

    public void onRestoreInstanceState(Bundle paramBundle) {}

    public void onSaveInstanceState(Bundle paramBundle) {}

    public void showDialog(int paramInt, Bundle paramBundle)
    {
      this.mContext.showDialog(paramInt, paramBundle);
    }
  }

  public static abstract interface OnCreateDialogListener
  {
    public abstract Dialog onCreateDialog(int paramInt, Bundle paramBundle);
  }

  public static abstract interface OnPrepareDialogListener
  {
    public abstract void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.DialogCompatHelper
 * JD-Core Version:    0.7.0.1
 */