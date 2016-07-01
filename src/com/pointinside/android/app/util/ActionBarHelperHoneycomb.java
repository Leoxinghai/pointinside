package com.pointinside.android.app.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ActionBarHelperHoneycomb
  extends ActionBarHelper
{
  protected ActionBarHelperHoneycomb(Activity paramActivity)
  {
    super(paramActivity);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    MenuItem localMenuItem1 = paramMenu.findItem(2131624054);
    if (localMenuItem1 != null) {
      addActionListener(localMenuItem1, localMenuItem1.getActionView());
    }
    MenuItem localMenuItem2 = paramMenu.findItem(2131624056);
    if (localMenuItem2 != null)
    {
      this.mDealsButtonToggle = localMenuItem2.getActionView();
      addActionListener(localMenuItem2, this.mDealsButtonToggle);
    }
    return true;
  }

  public void onPostCreate(Bundle paramBundle) {}

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return true;
  }

  public void setActionBarTitle(CharSequence paramCharSequence)
  {
    this.mActivity.setTitle(paramCharSequence);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.ActionBarHelperHoneycomb
 * JD-Core Version:    0.7.0.1
 */