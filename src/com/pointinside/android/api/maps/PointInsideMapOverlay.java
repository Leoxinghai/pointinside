package com.pointinside.android.api.maps;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class PointInsideMapOverlay
//  extends PIMapItemizedOverlay<PIMapOverlayItem>
extends PIMapItemizedOverlay
{
  private final ArrayList<PIMapOverlayItem> mOverlays = new ArrayList();

  public PointInsideMapOverlay(Drawable paramDrawable)
  {
    super(boundCenterBottom(paramDrawable));
  }

  public PointInsideMapOverlay(Drawable paramDrawable, boolean paramBoolean) {
	  super(boundCenterBottom(paramDrawable));
  }

  public void addOverlay(PIMapOverlayItem paramPIMapOverlayItem)
  {
    this.mOverlays.add(paramPIMapOverlayItem);
  }

  public void clear()
  {
    this.mOverlays.clear();
  }

  protected PIMapOverlayItem createItem(int paramInt)
  {
    if (paramInt > -1 + this.mOverlays.size()) {
      return null;
    }
    return (PIMapOverlayItem)this.mOverlays.get(paramInt);
  }

  public void populateOverlays()
  {
	    System.out.println(""+this+".populate");
    populate();
  }

  public int size()
  {
    return this.mOverlays.size();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PointInsideMapOverlay
 * JD-Core Version:    0.7.0.1
 */