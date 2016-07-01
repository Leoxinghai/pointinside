package com.pointinside.android.app.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ActionBarHelper
{
  private HashMap<Integer, View> mActionViews = new HashMap();
  protected Activity mActivity;
  protected View mDealsButtonToggle;

  protected ActionBarHelper(Activity paramActivity)
  {
    this.mActivity = paramActivity;
  }

  private boolean addActionViewFromMenuItem(MenuItem paramMenuItem)
  {
    ViewGroup localViewGroup = getActionBarLegacy();
    if (localViewGroup == null) {
      return false;
    }
    ImageView localImageView = new ImageView(this.mActivity, null, 2130771976);
    localImageView.setLayoutParams(new ViewGroup.LayoutParams(2, -1));
    switch (paramMenuItem.getItemId())
    {
    default:
      return false;
    case 2131624054:
      if (!this.mActionViews.containsKey(Integer.valueOf(paramMenuItem.getItemId())))
      {
        View localView5 = LayoutInflater.from(this.mActivity).inflate(2130903083, localViewGroup, false);
        localViewGroup.addView(localView5, 0);
        addActionListener(paramMenuItem, localView5);
        this.mActionViews.put(Integer.valueOf(paramMenuItem.getItemId()), localView5);
      }
      return true;
    case 2131624055:
      if (!this.mActionViews.containsKey(Integer.valueOf(paramMenuItem.getItemId())))
      {
        View localView4 = LayoutInflater.from(this.mActivity).inflate(2130903046, localViewGroup, false);
        localViewGroup.addView(localImageView);
        localViewGroup.addView(localView4);
        addActionListener(paramMenuItem, localView4);
        this.mActionViews.put(Integer.valueOf(paramMenuItem.getItemId()), localView4);
      }
      return true;
    case 2131624056:
      if (!this.mActionViews.containsKey(Integer.valueOf(paramMenuItem.getItemId())))
      {
        View localView3 = LayoutInflater.from(this.mActivity).inflate(2130903054, localViewGroup, false);
        this.mDealsButtonToggle = localView3;
        localViewGroup.addView(localImageView);
        localViewGroup.addView(localView3);
        addActionListener(paramMenuItem, localView3);
        this.mActionViews.put(Integer.valueOf(paramMenuItem.getItemId()), localView3);
      }
      return true;
    case 2131624057:
      if (!this.mActionViews.containsKey(Integer.valueOf(paramMenuItem.getItemId())))
      {
        View localView2 = LayoutInflater.from(this.mActivity).inflate(2130903066, localViewGroup, false);
        localViewGroup.addView(localImageView);
        localViewGroup.addView(localView2);
        addActionListener(paramMenuItem, localView2);
        this.mActionViews.put(Integer.valueOf(paramMenuItem.getItemId()), localView2);
      }
      return true;
    case 2131624058:
        if (!this.mActionViews.containsKey(Integer.valueOf(paramMenuItem.getItemId())))
        {
          View localView1 = LayoutInflater.from(this.mActivity).inflate(2130903084, localViewGroup, false);
          localViewGroup.addView(localImageView);
          localViewGroup.addView(localView1);
          addActionListener(paramMenuItem, localView1);
          this.mActionViews.put(Integer.valueOf(paramMenuItem.getItemId()), localView1);
        }
        return true;

    }
  }

  public static ActionBarHelper createInstance(Activity paramActivity)
  {
    if (UIUtils.isHoneycomb()) {
      return new ActionBarHelperHoneycomb(paramActivity);
    }
    return new ActionBarHelper(paramActivity);
  }

  private ViewGroup getActionBarLegacy()
  {
    return (ViewGroup)this.mActivity.findViewById(2131623937);
  }

  public void addActionListener(final MenuItem paramMenuItem, View paramView)
  {
    paramView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ActionBarHelper.this.mActivity.onMenuItemSelected(0, paramMenuItem);
      }
    });
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    return true;
  }

  public void onPostCreate(Bundle paramBundle)
  {
    SimpleMenu localSimpleMenu = new SimpleMenu(this.mActivity);
    this.mActivity.onCreatePanelMenu(0, localSimpleMenu);
    this.mActivity.onPrepareOptionsMenu(localSimpleMenu);
  }

  public boolean onPrepareOptionsMenu(Menu menu)
  {
      ArrayList arraylist;
      int i;
      int j;
      arraylist = new ArrayList();
      i = menu.size();
      j = 0;

      for(;j < i;) {
	      MenuItem menuitem = menu.getItem(j);
	      if(addActionViewFromMenuItem(menuitem))
	          arraylist.add(Integer.valueOf(menuitem.getItemId()));
	      j++;
      }

	Iterator iterator = arraylist.iterator();
	for(;iterator.hasNext();)
		menu.removeItem(((Integer)iterator.next()).intValue());
	return true;


  }

  public void setActionBarTitle(CharSequence charsequence)
  {
      ViewGroup viewgroup = getActionBarLegacy();
      TextView textview;
      if(viewgroup != null)
          if((textview = (TextView)viewgroup.findViewById(0x1020016)) != null)
          {
              textview.setText(charsequence);
              return;
          }
  }

  public void setDealsButtonOn(boolean paramBoolean)
  {
    if (this.mDealsButtonToggle != null) {
      ((Checkable)this.mDealsButtonToggle).setChecked(paramBoolean);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.ActionBarHelper
 * JD-Core Version:    0.7.0.1
 */