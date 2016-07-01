package com.pointinside.android.app.util;

import android.os.Build;
import android.os.Build.VERSION;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapAreaDataCursor;
import com.pointinside.android.api.dao.PIMapItemDataCursor;
import com.pointinside.android.api.dao.PIMapItemDataCursor.PIMapItem;
import com.pointinside.android.api.dao.PIMapPromotionDataCursor;
import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;
import com.pointinside.android.api.dao.PIMapZoneDataCursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PIDataUtils
{
  public static final String[] ATM_UUIDS;
  public static final String[] BUS_STOP_UUIDS;
  public static final String[] CAR_RENTAL_UUIDS;
  public static final String[] CURRENCY_EXCHANGE_UUIDS;
  public static final String[] ENTRANCE_UUIDS;
  public static final String[] FOOD_UUIDS;
  public static final String[] PARKING_UUIDS;
  public static final int PI_PROMOTION_TYPE_APPLICATION = 3;
  public static final int PI_PROMOTION_TYPE_EVENT = 1;
  public static final int PI_PROMOTION_TYPE_INFORMATION = 4;
  public static final int PI_PROMOTION_TYPE_PROMOTION = 2;
  public static final int PLACE_TYPE_PLACE = 100;
  public static final int PLACE_TYPE_PRODUCT = 102;
  public static final int PLACE_TYPE_SERVICE = 101;
  public static final String[] RESTROOM_UUIDS = { "a8dd988346d011df83e6f8477a8f5f2c", "a8ddd5dc46d011df83e6f8477a8f5f2c", "a8de11fb46d011df83e6f8477a8f5f2c", "a8de4f2e46d011df83e6f8477a8f5f2c" };
  public static final String[] TAXI_UUIDS;
  public static final String[] TRAIN_UUIDS;
  public static final String[] TRAM_UUIDS;
  public static final int VENUE_TYPE_AIRPORT = 2;
  public static final int VENUE_TYPE_BIGBOX = 3;
  public static final int VENUE_TYPE_MALL = 1;
  public static final int WORMHOLE_TYPE_ELEVATOR = 100;
  public static final int WORMHOLE_TYPE_ESCALATOR = 101;
  public static final int WORMHOLE_TYPE_STAIRS = 102;
  private static String sAppVersion = null;
  private static int sSDKVersion;

  static
  {
    FOOD_UUIDS = new String[] { "a8e5750046d011df83e6f8477a8f5f2c" };
    ATM_UUIDS = new String[] { "a8c0420f46d011df83e6f8477a8f5f2c" };
    CURRENCY_EXCHANGE_UUIDS = new String[] { "a8cbe17946d011df83e6f8477a8f5f2c" };
    TAXI_UUIDS = new String[] { "a8e2428a46d011df83e6f8477a8f5f2c" };
    TRAM_UUIDS = new String[] { "a8e3074946d011df83e6f8477a8f5f2c" };
    TRAIN_UUIDS = new String[] { "a8e2c1c846d011df83e6f8477a8f5f2c" };
    BUS_STOP_UUIDS = new String[] { "a8c4ec2c46d011df83e6f8477a8f5f2c" };
    CAR_RENTAL_UUIDS = new String[] { "a8e0ef0e46d011df83e6f8477a8f5f2c" };
    PARKING_UUIDS = new String[] { "a8d7dd9046d011df83e6f8477a8f5f2c", "a8d8407b46d011df83e6f8477a8f5f2c", "a8e05ab846d011df83e6f8477a8f5f2c", "a8d89f4246d011df83e6f8477a8f5f2c" };
    ENTRANCE_UUIDS = new String[] { "e20aae595efd11dfb9214c86bd2374de" };
    sSDKVersion = 0;
  }

  public static String getAreaName(PIMapVenue paramPIMapVenue, long paramLong1, long paramLong2)
  {
    String str1 = getSpecialAreaName(paramPIMapVenue, paramLong1);
    String str2 = getZoneName(paramPIMapVenue, paramLong2);
    if (str1 == null) {}
    while (str2.equals(str1)) {
      return str2;
    }
    return str2 + " - " + str1;
  }

  public static String getDevice()
  {
    return Build.MODEL;
  }

  public static PIMapItemDataCursor.PIMapItem getMapItem(PIMapVenue paramPIMapVenue, long paramLong)
  {
    PIMapItemDataCursor localPIMapItemDataCursor = paramPIMapVenue.getMapPlaceForPromotion(paramLong);
    if (localPIMapItemDataCursor != null) {
      try
      {
        PIMapItemDataCursor.PIMapItem localPIMapItem = localPIMapItemDataCursor.getPIMapItem();
        return localPIMapItem;
      }
      finally
      {
        localPIMapItemDataCursor.close();
      }
    }
    return null;
  }

  public static PIMapItemDataCursor.PIMapItem getPlace(PIMapVenue paramPIMapVenue, long paramLong)
  {
    PIMapItemDataCursor localPIMapItemDataCursor = paramPIMapVenue.getMapItem(paramLong);
    if (localPIMapItemDataCursor != null) {
      try
      {
        PIMapItemDataCursor.PIMapItem localPIMapItem = localPIMapItemDataCursor.getPIMapItem();
        return localPIMapItem;
      }
      finally
      {
        localPIMapItemDataCursor.close();
      }
    }
    return null;
  }

  public static long getPlaceId(PIMapVenue paramPIMapVenue, String paramString)
  {
    PIMapItemDataCursor localPIMapItemDataCursor = paramPIMapVenue.getMapItemForUUID(paramString);
    if (localPIMapItemDataCursor != null) {
      try
      {
        long l = localPIMapItemDataCursor.getId();
        return l;
      }
      finally
      {
        localPIMapItemDataCursor.close();
      }
    }
    return 0L;
  }

  public static String getPlaceName(PIMapVenue paramPIMapVenue, String paramString)
  {
    PIMapItemDataCursor localPIMapItemDataCursor = paramPIMapVenue.getMapItemForUUID(paramString);
    if (localPIMapItemDataCursor != null) {
      try
      {
        String str = localPIMapItemDataCursor.getName();
        return str;
      }
      finally
      {
        localPIMapItemDataCursor.close();
      }
    }
    return null;
  }

  public static String getPromoName(PIMapVenue paramPIMapVenue, long paramLong)
  {
    PIMapPromotionDataCursor localPIMapPromotionDataCursor = paramPIMapVenue.getPromotion(paramLong);
    if (localPIMapPromotionDataCursor != null) {
      try
      {
        String str = localPIMapPromotionDataCursor.getTitle();
        return str;
      }
      finally
      {
        localPIMapPromotionDataCursor.close();
      }
    }
    return null;
  }

  public static String getSpecialAreaName(PIMapVenue paramPIMapVenue, long paramLong)
  {
    PIMapAreaDataCursor localPIMapAreaDataCursor = paramPIMapVenue.getMapPlaceAreaForPlace(paramLong);
    if (localPIMapAreaDataCursor != null) {
      try
      {
        String str = localPIMapAreaDataCursor.getName();
        return str;
      }
      finally
      {
        localPIMapAreaDataCursor.close();
      }
    }
    return null;
  }

  public static String getVenueName(PIMapReference paramPIMapReference, String paramString)
  {
    PIMapVenueSummaryDataCursor localPIMapVenueSummaryDataCursor = paramPIMapReference.getVenueForUUID(paramString);
    if (localPIMapVenueSummaryDataCursor != null) {
      try
      {
        String str = localPIMapVenueSummaryDataCursor.getVenueName();
        return str;
      }
      finally
      {
        localPIMapVenueSummaryDataCursor.close();
      }
    }
    return null;
  }

  public static String getVersion()
  {
    return Build.VERSION.RELEASE;
  }

  public static String getZoneName(PIMapVenue paramPIMapVenue, long paramLong)
  {
    PIMapZoneDataCursor localPIMapZoneDataCursor = paramPIMapVenue.getVenueZone(paramLong);
    if (localPIMapZoneDataCursor != null) {
      try
      {
        String str = localPIMapZoneDataCursor.getZoneName();
        return str;
      }
      finally
      {
        localPIMapZoneDataCursor.close();
      }
    }
    return null;
  }

  public static <T> boolean inSet(T[] paramArrayOfT, T paramT)
  {
    return Arrays.asList(paramArrayOfT).contains(paramT);
  }

  public static boolean isEclairOrGreater()
  {
    if (sSDKVersion == 0) {
      sSDKVersion = Integer.parseInt(Build.VERSION.SDK);
    }
    return Integer.parseInt(Build.VERSION.SDK) >= 5;
  }

  private static <T> T[] mergeArray(NewArray<T> paramNewArray, T[]... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return null;
    }
    ArrayList<T> localArrayList = new ArrayList<T>();
    int i = paramVarArgs.length;
    int j = 0;
    if (j >= i) {
      return localArrayList.toArray(paramNewArray.newArray(localArrayList.size()));
    }
    T[] arrayOfT = paramVarArgs[j];
    int k = arrayOfT.length;
    for (int m = 0;; m++)
    {
      if (m >= k)
      {
        j++;
        break;
      }
      localArrayList.add(arrayOfT[m]);
    }
    return localArrayList.toArray(paramNewArray.newArray(localArrayList.size()));
  }


  public static String[] mergeStringArray(String as[][])
  {
      NewArray<String> newArray = new NewArray<String>() {

          public String[] newArray(int i)
          {
              return new String[i];
          }

      };
	  return (String[])mergeArray(newArray, as);
  }

  public static String stringifyStream(InputStream paramInputStream)
    throws IOException
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
    StringBuilder localStringBuilder = new StringBuilder();
    for (;;)
    {
      String str = localBufferedReader.readLine();
      if (str == null) {
        return localStringBuilder.toString();
      }
      localStringBuilder.append(str + "\n");
    }
  }

  private static abstract interface NewArray<T>
  {
    public abstract T[] newArray(int paramInt);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.PIDataUtils
 * JD-Core Version:    0.7.0.1
 */