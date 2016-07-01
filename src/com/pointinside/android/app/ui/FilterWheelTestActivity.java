package com.pointinside.android.app.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import com.pointinside.android.app.widget.FilterWheel;
import com.pointinside.android.app.widget.FilterWheel.FilterWheelItem;
import com.pointinside.android.app.widget.FilterWheel.OnFilterEventListener;
import java.io.PrintStream;
import java.util.ArrayList;

public class FilterWheelTestActivity
  extends Activity
  implements FilterWheel.OnFilterEventListener
{
  private FilterWheel mFilterWheel;

  private static Bitmap getBitmapFor(Resources paramResources, int paramInt)
  {
    return ((BitmapDrawable)paramResources.getDrawable(paramInt)).getBitmap();
  }

  public void onCollapse()
  {
    System.out.println("onCollapse");
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903059);
    this.mFilterWheel = ((FilterWheel)findViewById(2131623959));
    this.mFilterWheel.setOnFilterEventListener(this);
    this.mFilterWheel.setLeftText(2131099757);
    this.mFilterWheel.setRightText(2131099759);
    ArrayList localArrayList1 = new ArrayList();
    localArrayList1.add(FilterWheel.FilterWheelItem.makeTextItem("Airport Overview"));
    localArrayList1.add(FilterWheel.FilterWheelItem.makeTextItem("Main Terminal"));
    localArrayList1.add(FilterWheel.FilterWheelItem.makeTextItem("North Satellite"));
    localArrayList1.add(FilterWheel.FilterWheelItem.makeTextItem("South Satellite"));
    this.mFilterWheel.setLeftItems(localArrayList1, false);
    ArrayList localArrayList2 = new ArrayList();
    Resources localResources = getResources();
    localArrayList2.add(FilterWheel.FilterWheelItem.makeBitmapItem(getBitmapFor(localResources, 2130837616), getBitmapFor(localResources, 2130837617)));
    localArrayList2.add(FilterWheel.FilterWheelItem.makeBitmapItem(getBitmapFor(localResources, 2130837628), getBitmapFor(localResources, 2130837629)));
    localArrayList2.add(FilterWheel.FilterWheelItem.makeBitmapItem(getBitmapFor(localResources, 2130837618), getBitmapFor(localResources, 2130837619)));
    localArrayList2.add(FilterWheel.FilterWheelItem.makeBitmapItem(getBitmapFor(localResources, 2130837624), getBitmapFor(localResources, 2130837625)));
    this.mFilterWheel.setRightItems(localArrayList2, false);
    this.mFilterWheel.setLeftSelected(false);
  }

  public void onExpand()
  {
    boolean bool = this.mFilterWheel.isLeftSelected();
    if (bool) {}
    for (int i = this.mFilterWheel.getLeftSelectedPosition();; i = this.mFilterWheel.getRightSelectedPosition())
    {
      System.out.println("onExpand: left=" + bool + "; selection=" + i);
      return;
    }
  }

  public void onLeftItemSelectionChange(int paramInt)
  {
    System.out.println("onLeftItemSelected: position=" + paramInt);
  }

  public void onLeftSelected()
  {
    System.out.println("onLeftSelected: position=" + this.mFilterWheel.getLeftSelectedPosition());
  }

  public void onRightItemSelectionChange(int paramInt)
  {
    System.out.println("onRightItemSelected: position=" + paramInt);
  }

  public void onRightSelected()
  {
    System.out.println("onRightSelected: position=" + this.mFilterWheel.getRightSelectedPosition());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.FilterWheelTestActivity
 * JD-Core Version:    0.7.0.1
 */