// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.*;

// Referenced classes of package com.pointinside.android.app.util:
//            SimpleMenu

public class SimpleMenuItem
    implements MenuItem
{

    public SimpleMenuItem(SimpleMenu simplemenu, int i, int j, CharSequence charsequence)
    {
        mIconResId = 0;
        mEnabled = true;
        mMenu = simplemenu;
        mId = i;
        mOrder = j;
        mTitle = charsequence;
    }

    public View getActionView()
    {
        return null;
    }

    public char getAlphabeticShortcut()
    {
        return '\0';
    }

    public int getGroupId()
    {
        return 0;
    }

    public Drawable getIcon()
    {
        if(mIconDrawable != null)
            return mIconDrawable;
        if(mIconResId != 0)
            return mMenu.getResources().getDrawable(mIconResId);
        else
            return null;
    }

    public Intent getIntent()
    {
        return null;
    }

    public int getItemId()
    {
        return mId;
    }

    public android.view.ContextMenu.ContextMenuInfo getMenuInfo()
    {
        return null;
    }

    public char getNumericShortcut()
    {
        return '\0';
    }

    public int getOrder()
    {
        return mOrder;
    }

    public SubMenu getSubMenu()
    {
        return null;
    }

    public CharSequence getTitle()
    {
        return mTitle;
    }

    public CharSequence getTitleCondensed()
    {
        if(mTitleCondensed != null)
            return mTitleCondensed;
        else
            return mTitle;
    }

    public boolean hasSubMenu()
    {
        return false;
    }

    public boolean isCheckable()
    {
        return false;
    }

    public boolean isChecked()
    {
        return false;
    }

    public boolean isEnabled()
    {
        return mEnabled;
    }

    public boolean isVisible()
    {
        return true;
    }

    public MenuItem setActionView(int i)
    {
        return this;
    }

    public MenuItem setActionView(View view)
    {
        return this;
    }

    public MenuItem setAlphabeticShortcut(char c)
    {
        return this;
    }

    public MenuItem setCheckable(boolean flag)
    {
        return this;
    }

    public MenuItem setChecked(boolean flag)
    {
        return this;
    }

    public MenuItem setEnabled(boolean flag)
    {
        mEnabled = flag;
        return this;
    }

    public MenuItem setIcon(int i)
    {
        mIconDrawable = null;
        mIconResId = i;
        return this;
    }

    public MenuItem setIcon(Drawable drawable)
    {
        mIconResId = 0;
        mIconDrawable = drawable;
        return this;
    }

    public MenuItem setIntent(Intent intent)
    {
        return this;
    }

    public MenuItem setNumericShortcut(char c)
    {
        return this;
    }

    public MenuItem setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener onmenuitemclicklistener)
    {
        return this;
    }

    public MenuItem setShortcut(char c, char c1)
    {
        return this;
    }

    public void setShowAsAction(int i)
    {
    }

    public MenuItem setTitle(int i)
    {
        return setTitle(((CharSequence) (mMenu.getContext().getString(i))));
    }

    public MenuItem setTitle(CharSequence charsequence)
    {
        mTitle = charsequence;
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence charsequence)
    {
        mTitleCondensed = charsequence;
        return this;
    }

    public MenuItem setVisible(boolean flag)
    {
        return this;
    }

	 public boolean collapseActionView(){
		 return true;
	 }

	 public ActionProvider getActionProvider(){
		 return null;
	 }

	 public MenuItem setActionProvider(ActionProvider s){
		 return this;
	 }

	 public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener listener){
		 return this;
	 }

	 public MenuItem setShowAsActionFlags(int flag){
		 return this;
	 }
	 public boolean expandActionView(){
		 return true;
	 }
	 public boolean isActionViewExpanded(){
		 return true;
	 }

    private boolean mEnabled;
    private Drawable mIconDrawable;
    private int mIconResId;
    private final int mId;
    private SimpleMenu mMenu;
    private final int mOrder;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
}
