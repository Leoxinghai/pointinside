package com.pointinside.android.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import java.io.PrintStream;
import java.util.ArrayList;

public class PlaceBrowseActivity
  extends Activity
  implements AdapterView.OnItemClickListener
{
  private static final CategoryDescriptor[] sCategories;
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private BrowseAdapter mAdapter;
  private GridView mGrid;

  static
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new CategoryDescriptor(1, 2131099777, 2130837570));
    localArrayList.add(new CategoryDescriptor(2, 2131099778, 2130837574));
    localArrayList.add(new CategoryDescriptor(3, 2131099779, 2130837575));
    localArrayList.add(new CategoryDescriptor(4, 2131099780, 2130837576));
    localArrayList.add(new CategoryDescriptor(5, 2131099781, 2130837569));
    localArrayList.add(new CategoryDescriptor(6, 2131099782, 2130837571));
    localArrayList.add(new CategoryDescriptor(7, 2131099783, 2130837573));
    localArrayList.add(new CategoryDescriptor(8, 2131099784, 2130837572));
    localArrayList.add(new CategoryDescriptor(9, 2131099785, 2130837568));
    sCategories = (CategoryDescriptor[])localArrayList.toArray(new CategoryDescriptor[localArrayList.size()]);
  }

  public static String getCategoryString(Resources paramResources, int paramInt)
  {
    int i = sCategories.length;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return null;
      }
      CategoryDescriptor localCategoryDescriptor = sCategories[j];
      if (localCategoryDescriptor.categoryServerId == paramInt) {
        return paramResources.getString(localCategoryDescriptor.textId);
      }
    }
  }

  public static void show(Context paramContext)
  {
    paramContext.startActivity(new Intent(paramContext, PlaceBrowseActivity.class));
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903073);
    System.out.println("getTitle()=" + getTitle());
    this.mActionBarHelper.setActionBarTitle(getTitle());
    this.mGrid = ((GridView)findViewById(2131623991));
    this.mAdapter = new BrowseAdapter(this);
    System.out.println("items=" + this.mAdapter.getCount());
    this.mGrid.setAdapter(this.mAdapter);
    this.mGrid.setOnItemClickListener(this);
    PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", PointInside.getInstance().getCurrentVenueId(), PITouchstreamContract.TouchstreamType.SHOW_CATEGORY_BROWSE);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    PlaceSearchResultsActivity.sendSearch(this, ((CategoryItem)this.mAdapter.getItem(paramInt)).categoryServerId);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return false;
    case 2131624052:
      onSearchRequested();
      return true;
    case 2131624051:
      startActivity(new Intent(this, FeedbackActivity.class));
      return true;
    case 2131624050:
	  startActivity(new Intent(this, AboutActivity.class));
	  return true;

    }
  }

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.mActionBarHelper.onPostCreate(paramBundle);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return this.mActionBarHelper.onPrepareOptionsMenu(paramMenu);
  }

  public boolean onSearchRequested()
  {
    return PlaceSearchActivity.showSearch(this);
  }

  private static class BrowseAdapter
    extends ArrayAdapter<PlaceBrowseActivity.CategoryItem>
  {
    public BrowseAdapter(Context paramContext)
    {
      super(paramContext,0);
      init();
    }

    private void init()
    {
      Resources localResources = getContext().getResources();
      PlaceBrowseActivity.CategoryDescriptor[] arrayOfCategoryDescriptor = PlaceBrowseActivity.sCategories;
      int i = arrayOfCategoryDescriptor.length;
      for (int j = 0;; j++)
      {
        if (j >= i) {
          return;
        }
        PlaceBrowseActivity.CategoryDescriptor localCategoryDescriptor = arrayOfCategoryDescriptor[j];
        add(new PlaceBrowseActivity.CategoryItem(localCategoryDescriptor.categoryServerId, localResources.getDrawable(localCategoryDescriptor.iconId)));
      }
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      PlaceBrowseActivity.CategoryItem localCategoryItem = (PlaceBrowseActivity.CategoryItem)getItem(paramInt);
      if (paramView == null) {}
      for (View localView = LayoutInflater.from(getContext()).inflate(2130903074, paramViewGroup, false);; localView = paramView)
      {
        ((ImageView)localView).setImageDrawable(localCategoryItem.icon);
        return localView;
      }
    }
  }

  private static class CategoryDescriptor
  {
    public final int categoryServerId;
    public final int iconId;
    public final int textId;

    public CategoryDescriptor(int paramInt1, int paramInt2, int paramInt3)
    {
      this.categoryServerId = paramInt1;
      this.textId = paramInt2;
      this.iconId = paramInt3;
    }
  }

  private static class CategoryItem
  {
    public final int categoryServerId;
    public final Drawable icon;

    public CategoryItem(int paramInt, Drawable paramDrawable)
    {
      this.categoryServerId = paramInt;
      this.icon = paramDrawable;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.PlaceBrowseActivity
 * JD-Core Version:    0.7.0.1
 */