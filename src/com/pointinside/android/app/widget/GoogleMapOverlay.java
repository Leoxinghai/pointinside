package com.pointinside.android.app.widget;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
//import com.google.android.maps.ItemizedOverlay;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class GoogleMapOverlay
{
  private static final String TAG = GoogleMapOverlay.class.getSimpleName();
  private final HashMap<String, Drawable> mMarkers;
  private final ArrayList<GoogleOverlayItem> mOverlays = new ArrayList();
  private final HashMap<String, GoogleOverlayItem> mOverlaysCodeMap = new HashMap();
  
  public GoogleMapOverlay(Drawable paramDrawable)
  {
    this(makeMarkers(paramDrawable));
  }
  
  public GoogleMapOverlay(HashMap<String, Drawable> paramHashMap)
  {
   // super(boundCenterBottom((Drawable)paramHashMap.get("Default")));
    this.mMarkers = paramHashMap;
    populate();
  }
  
  public void populate() {

  }
  private boolean add(GoogleOverlayItem paramGoogleOverlayItem)
  {
    if (!containsOverlay(paramGoogleOverlayItem))
    {
      String str = paramGoogleOverlayItem.getType();
      Drawable localDrawable = (Drawable)this.mMarkers.get(str);
      if (localDrawable != null) {
//        paramGoogleOverlayItem.setMarker(boundCenterBottom(localDrawable));
      }
      this.mOverlays.add(paramGoogleOverlayItem);
      this.mOverlaysCodeMap.put(paramGoogleOverlayItem.getGeoCode(), paramGoogleOverlayItem);
      return true;
    }
    return false;
  }
  
  private static HashMap<String, Drawable> makeMarkers(Drawable paramDrawable)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("Default", paramDrawable);
    return localHashMap;
  }
  
  public int addAllOverlays(List<GoogleOverlayItem> paramList)
  {
    int i = 0;
    Iterator localIterator = paramList.iterator();
    for (;localIterator.hasNext();)
    {
      add((GoogleOverlayItem)localIterator.next());
      i++;
    }
    populate();
    return i;
  }
  
  public void addOverlay(GoogleOverlayItem paramGoogleOverlayItem)
  {
    if (!containsOverlay(paramGoogleOverlayItem))
    {
      add(paramGoogleOverlayItem);
      populate();
    }
  }
  
  public void clear()
  {
    this.mOverlaysCodeMap.clear();
    this.mOverlays.clear();
    populate();
  }
  
  public boolean containsOverlay(GoogleOverlayItem paramGoogleOverlayItem)
  {
    return this.mOverlaysCodeMap.containsKey(paramGoogleOverlayItem.getGeoCode());
  }
  
  public boolean containsOverlay(String paramString)
  {
    return this.mOverlaysCodeMap.containsKey(paramString);
  }
  
  protected GoogleOverlayItem createItem(int paramInt)
  {
    return (GoogleOverlayItem)this.mOverlays.get(paramInt);
  }
  
  public GoogleOverlayItem getItem(String paramString)
  {
    return (GoogleOverlayItem)this.mOverlaysCodeMap.get(paramString);
  }
  
  public boolean onTap(LatLng paramGeoPoint, MapView paramMapView)
  {
    return true;
	  //return super.onTap(paramGeoPoint, paramMapView);
  }
  
  public GoogleOverlayItem removeOverlay(GoogleOverlayItem paramGoogleOverlayItem)
  {
    if (paramGoogleOverlayItem == null) {
      return null;
    }
    GoogleOverlayItem localGoogleOverlayItem = (GoogleOverlayItem)this.mOverlaysCodeMap.remove(paramGoogleOverlayItem.getGeoCode());
    this.mOverlays.remove(localGoogleOverlayItem);
    //setLastFocusedIndex(-1);
    populate();
    return localGoogleOverlayItem;
  }
  
  public GoogleOverlayItem removeOverlay(String paramString)
  {
    //setLastFocusedIndex(-1);
    return removeOverlay((GoogleOverlayItem)this.mOverlaysCodeMap.get(paramString));
  }
  
  public void setFocus(GoogleOverlayItem paramGoogleOverlayItem)
  {
    //super.setFocus(paramGoogleOverlayItem);
  }
  
  public int size()
  {
    return this.mOverlays.size();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.GoogleMapOverlay
 * JD-Core Version:    0.7.0.1
 */