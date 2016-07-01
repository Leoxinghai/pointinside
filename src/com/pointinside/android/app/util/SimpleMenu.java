package com.pointinside.android.app.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.ArrayList;

public class SimpleMenu
  implements Menu
{
  private Context mContext;
  private ArrayList<SimpleMenuItem> mItems;
  private Resources mResources;

  public SimpleMenu(Context paramContext)
  {
    this.mContext = paramContext;
    this.mResources = paramContext.getResources();
    this.mItems = new ArrayList();
  }

  private MenuItem addInternal(int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    SimpleMenuItem localSimpleMenuItem = new SimpleMenuItem(this, paramInt1, paramInt2, paramCharSequence);
    this.mItems.add(findInsertIndex(this.mItems, paramInt2), localSimpleMenuItem);
    return localSimpleMenuItem;
  }

  private static int findInsertIndex(ArrayList<? extends MenuItem> paramArrayList, int paramInt)
  {
    for (int i = -1 + paramArrayList.size();; i--)
    {
      if (i < 0) {
        return 0;
      }
      if (((MenuItem)paramArrayList.get(i)).getOrder() <= paramInt) {
        return i + 1;
      }
    }
  }

  private void removeItemAtInt(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.mItems.size())) {
      return;
    }
    this.mItems.remove(paramInt);
  }

  public MenuItem add(int paramInt)
  {
    return addInternal(0, 0, this.mResources.getString(paramInt));
  }

  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return addInternal(paramInt2, paramInt3, this.mResources.getString(paramInt4));
  }

  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    return addInternal(paramInt2, paramInt3, paramCharSequence);
  }

  public MenuItem add(CharSequence paramCharSequence)
  {
    return addInternal(0, 0, paramCharSequence);
  }

  public int addIntentOptions(int paramInt1, int paramInt2, int paramInt3, ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent, int paramInt4, MenuItem[] paramArrayOfMenuItem)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public SubMenu addSubMenu(int paramInt)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public SubMenu addSubMenu(CharSequence paramCharSequence)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public void clear()
  {
    this.mItems.clear();
  }

  public void close()
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public MenuItem findItem(int paramInt)
  {
    int i = size();
    MenuItem localObject = null;
    for (int j = 0;j<i; j++) {
    	localObject = (SimpleMenuItem)this.mItems.get(j);
        if(((SimpleMenuItem)localObject).getItemId() == paramInt)
        	break;
    }
    return localObject;
  }

  public int findItemIndex(int paramInt)
  {
    int i = size();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        j = -1;
      }
      while (((SimpleMenuItem)this.mItems.get(j)).getItemId() == paramInt) {
        return j;
      }
    }
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public MenuItem getItem(int paramInt)
  {
    return (MenuItem)this.mItems.get(paramInt);
  }

  public Resources getResources()
  {
    return this.mResources;
  }

  public boolean hasVisibleItems()
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public boolean performIdentifierAction(int paramInt1, int paramInt2)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public void removeGroup(int paramInt)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public void removeItem(int paramInt)
  {
    removeItemAtInt(findItemIndex(paramInt));
  }

  public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public void setGroupEnabled(int paramInt, boolean paramBoolean)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public void setGroupVisible(int paramInt, boolean paramBoolean)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public void setQwertyMode(boolean paramBoolean)
  {
    throw new UnsupportedOperationException("This operation is not supported for SimpleMenu");
  }

  public int size()
  {
    return this.mItems.size();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.SimpleMenu
 * JD-Core Version:    0.7.0.1
 */