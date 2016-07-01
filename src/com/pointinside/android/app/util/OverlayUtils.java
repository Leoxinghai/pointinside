package com.pointinside.android.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.pointinside.android.app.ui.GoogleMapActivity;
import com.pointinside.android.app.widget.GoogleMapOverlay;
import com.pointinside.android.app.widget.GoogleOverlayItem;
import com.pointinside.android.app.widget.VenueGeoPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class OverlayUtils
{
  public static final String DEFAULT_MARKER = "Default";
  
  public static int addGoogleOverlaysForVisibleArea(CopyOnWriteArrayList<GoogleOverlayItem> paramCopyOnWriteArrayList, GoogleMapOverlay paramGoogleMapOverlay)
  {
    ArrayList localArrayList = new ArrayList();
    LatLongRect localLatLongRect = LocationHelper.getMapArea();
    if ((localLatLongRect == null) || (paramCopyOnWriteArrayList == null)) {
      return 0;
    }
    int i = LocationHelper.toInt(localLatLongRect.getLatitudeSpan()) / 2;
    int j = LocationHelper.toInt(localLatLongRect.getLongitudeSpan()) / 2;
    if (i > 90000000.0D) {
      i = 90000000;
    }
    if (j > 180000000.0D) {
      j = 180000000;
    }
    int k = i + LocationHelper.toInt(localLatLongRect.getLatitudeCenter());
    int m = LocationHelper.toInt(localLatLongRect.getLongitudeCenter()) - j;
    int n = LocationHelper.toInt(localLatLongRect.getLatitudeCenter()) - i;
    int i1 = j + LocationHelper.toInt(localLatLongRect.getLongitudeCenter());
    if (k > 90000000.0D) {
      k = 89999999;
    }
    if (n < -90000000.0D) {
      n = -89999999;
    }
    if (m < -180000000.0D) {
      m = -179999999;
    }
    if (i1 > 180000000.0D) {
      i1 = 179999999;
    }
    VenueGeoPoint localVenueGeoPoint1 = new VenueGeoPoint(k, m);
    VenueGeoPoint localVenueGeoPoint2 = new VenueGeoPoint(n, i1);
    int i2 = Collections.binarySearch(paramCopyOnWriteArrayList, localVenueGeoPoint1, localVenueGeoPoint1);
    if (i2 < 0) {
      i2 = -1 + -i2;
    }
    int i3 = paramCopyOnWriteArrayList.size();
    int i4 = 0;
    if (i2 >= 0) {}
    for (;;)
    {
      if (i2 >= i3) {
          paramGoogleMapOverlay.addAllOverlays(localArrayList);
          return i4;
      }
      GoogleOverlayItem localGoogleOverlayItem;
      localGoogleOverlayItem = (GoogleOverlayItem)paramCopyOnWriteArrayList.get(i2);
      if(localGoogleOverlayItem.getLongitudeE6() > localVenueGeoPoint2.getLongitudeE6()) {
          if (localGoogleOverlayItem.isActive()) {
            break;
          }
      }
      if ((localGoogleOverlayItem.getLatitudeE6() <= localVenueGeoPoint1.getLatitudeE6()) && (localGoogleOverlayItem.getLatitudeE6() >= localVenueGeoPoint2.getLatitudeE6()))
      {
        localGoogleOverlayItem.setActive(true);
        localArrayList.add(localGoogleOverlayItem);
        i4++;
      }
      i2++;
    }
    return i4;
  }
  
  public static void deactivateOverlays(CopyOnWriteArrayList<GoogleOverlayItem> paramCopyOnWriteArrayList)
  {
    Iterator localIterator = paramCopyOnWriteArrayList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      ((GoogleOverlayItem)localIterator.next()).setActive(false);
    }
  }
  
  public static HashMap<String, Drawable> getDealMarkerDrawableMap(Context paramContext)
  {
    HashMap localHashMap = new HashMap();
    Resources localResources = paramContext.getResources();
    localHashMap.put("Default", localResources.getDrawable(2130837690));
    localHashMap.putAll(DealsUtils.getPinDrawables(localResources));
    return localHashMap;
  }
  
  public static GoogleOverlayItem getOverlayItem(CopyOnWriteArrayList<GoogleOverlayItem> paramCopyOnWriteArrayList, String paramString)
  {
    if (paramString == null) {
      return null;
    }
    Iterator localIterator = paramCopyOnWriteArrayList.iterator();
    GoogleOverlayItem localGoogleOverlayItem;
    do
    {
      if (!localIterator.hasNext()) {
        return null;
      }
      localGoogleOverlayItem = (GoogleOverlayItem)localIterator.next();
    } while (!paramString.equals(localGoogleOverlayItem.getVenueUUID()));
    return localGoogleOverlayItem;
  }
  
  public static HashMap<String, Drawable> getVenueMarkerDrawableMap(Context paramContext)
  {
    HashMap localHashMap = new HashMap();
    Resources localResources = paramContext.getResources();
    localHashMap.put("Default", localResources.getDrawable(2130837686));
    localHashMap.put(String.valueOf(4), localResources.getDrawable(2130837686));
    localHashMap.put(String.valueOf(2), localResources.getDrawable(2130837687));
    localHashMap.put(String.valueOf(1), localResources.getDrawable(2130837692));
    return localHashMap;
  }
  
  public static int hideVenueOverlaysForDeals(Collection<GoogleMapActivity.MapDeal> paramCollection, GoogleMapOverlay paramGoogleMapOverlay)
  {
    int i = 0;
    Iterator localIterator = paramCollection.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return i;
      }
      GoogleMapActivity.MapDeal localMapDeal = (GoogleMapActivity.MapDeal)localIterator.next();
      if (localMapDeal.getVenueUUID() != null)
      {
        GoogleOverlayItem localGoogleOverlayItem = paramGoogleMapOverlay.removeOverlay(localMapDeal.getGeoCode());
        if (localGoogleOverlayItem != null)
        {
          i++;
          localGoogleOverlayItem.setActive(false);
        }
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.OverlayUtils
 * JD-Core Version:    0.7.0.1
 */