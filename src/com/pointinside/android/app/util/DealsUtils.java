package com.pointinside.android.app.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DealsUtils
{
  private static HashMap<String, Integer> sListIconIdMap = new HashMap();
  private static HashMap<String, Integer> sPinIdMap = new HashMap();

  static
  {
    sListIconIdMap.put("General", Integer.valueOf(2130837656));
    sListIconIdMap.put("Fashion", Integer.valueOf(2130837655));
    sListIconIdMap.put("Home", Integer.valueOf(2130837657));
    sListIconIdMap.put("Dining", Integer.valueOf(2130837654));
    sListIconIdMap.put("Shops", Integer.valueOf(2130837658));
    sListIconIdMap.put("Retail", Integer.valueOf(2130837658));
    sListIconIdMap.put("Auto", Integer.valueOf(2130837652));
    sPinIdMap.put("General", Integer.valueOf(2130837690));
    sPinIdMap.put("Fashion", Integer.valueOf(2130837689));
    sPinIdMap.put("Home", Integer.valueOf(2130837691));
    sPinIdMap.put("Dining", Integer.valueOf(2130837688));
    sPinIdMap.put("Shops", Integer.valueOf(2130837695));
    sPinIdMap.put("Retail", Integer.valueOf(2130837695));
    sPinIdMap.put("Auto", Integer.valueOf(2130837690));
  }

  private static HashMap<String, Drawable> convertToDrawableMap(Resources paramResources, HashMap<String, Integer> paramHashMap)
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = paramHashMap.entrySet().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localHashMap;
      }
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localHashMap.put((String)localEntry.getKey(), paramResources.getDrawable(((Integer)localEntry.getValue()).intValue()));
    }
  }

  public static int getDealListIconId(String paramString)
  {
    return getIconId(sListIconIdMap, paramString);
  }

  private static int getIconId(HashMap<String, Integer> paramHashMap, String paramString)
  {
    Integer localInteger = (Integer)paramHashMap.get(paramString);
    if (localInteger != null) {
      return localInteger.intValue();
    }
    return ((Integer)paramHashMap.get("General")).intValue();
  }

  public static HashMap<String, Drawable> getPinDrawables(Resources paramResources)
  {
    return convertToDrawableMap(paramResources, sPinIdMap);
  }

  public static int getPinIconId(String paramString)
  {
    return getIconId(sPinIdMap, paramString);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.DealsUtils
 * JD-Core Version:    0.7.0.1
 */