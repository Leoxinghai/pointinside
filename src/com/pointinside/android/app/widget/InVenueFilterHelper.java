package com.pointinside.android.app.widget;

import com.pointinside.android.app.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.dao.PIMapZoneDataCursor;
import com.pointinside.android.api.maps.PIMapOverlayItem;
import com.pointinside.android.app.util.PIDataUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class InVenueFilterHelper
{
  public static final int SERVICE_TYPE_ATM = 2;
  public static final int SERVICE_TYPE_ELEVATOR_AND_ESCALATOR = 4;
  public static final int SERVICE_TYPE_ENTRANCE = 6;
  public static final int SERVICE_TYPE_FOOD = 3;
  public static final int SERVICE_TYPE_PARKING = 5;
  public static final int SERVICE_TYPE_RESTROOM = 1;
  public static final int SERVICE_TYPE_TRANSPORTATION = 7;
  private static final String TAG = InVenueFilterHelper.class.getSimpleName();
  private final ArrayList<Service> mServices = new ArrayList();
  private PIMapVenue mVenue;
  private final FilterWheel mWheel;
  private int mZoneIndex = -1;
  private boolean mZoneIsOverview;
  private final ArrayList<Zone> mZones = new ArrayList();

  public InVenueFilterHelper(FilterWheel paramFilterWheel)
  {
    this.mWheel = paramFilterWheel;
    paramFilterWheel.setRightText(2131099759);
  }

  private void addService(int paramInt1, String[] paramArrayOfString, int[] paramArrayOfInt, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mServices.add(new Service(paramInt1, paramArrayOfString, paramArrayOfInt, this.mWheel.getContext().getResources(), paramInt2, paramInt3, paramInt4));
  }

  private void emitManualServiceSelection()
  {
    int i = this.mWheel.getRightSelectedPosition();
    if (i >= 0) {
      this.mWheel.getOnFilterEventListener().onRightItemSelectionChange(i);
    }
  }

  private int findZoneByZoneIndex(int paramInt)
  {
    int i = this.mZones.size();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        j = -1;
      }
      while (((Zone)this.mZones.get(j)).zoneIndex == paramInt) {
        return j;
      }
    }
  }

  private void forceZoneSelection(int paramInt)
  {
    int i = findZoneByZoneIndex(paramInt);
    this.mWheel.setLeftSelectedPosition(i);
  }

  public static int getNoServicesMessageForType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown type=" + paramInt);
    case 2:
      return 2131099727;
    case 3:
      return 2131099728;
    case 1:
      return 2131099726;
    case 4:
      return 2131099729;
    case 5:
      return 2131099734;
    case 6:
      return 2131099735;
    case 7:
        return 2131099736;
    }
  }



  public static Drawable getServiceMarker(Resources paramResources, int paramInt, PIMapOverlayItem paramPIMapOverlayItem)
  {
    String str = paramPIMapOverlayItem.getServiceTypeUUID();
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown type=" + paramInt);
    case 2:
      if (PIDataUtils.inSet(PIDataUtils.ATM_UUIDS, str)) {
        return paramResources.getDrawable(R.drawable.emblem_atm_normal);
      }
      if (PIDataUtils.inSet(PIDataUtils.CURRENCY_EXCHANGE_UUIDS, str)) {
        return paramResources.getDrawable(R.drawable.emblem_currency_exchange_normal);
      }
      throw new IllegalArgumentException("Unknown serviceUUID=" + str);
    case 3:
      return paramResources.getDrawable(R.drawable.emblem_food_normal);
    case 1:
      return paramResources.getDrawable(R.drawable.emblem_restroom_normal);
    case 6:
      return paramResources.getDrawable(R.drawable.emblem_entrance_normal);
    case 5:
      return paramResources.getDrawable(R.drawable.emblem_parking_normal);
    case 4:
      switch (paramPIMapOverlayItem.getWormholeTypeId())
      {
      default:
        throw new IllegalArgumentException("Unknown wormhole type=" + paramPIMapOverlayItem.getWormholeTypeId());
      case 100:
        return paramResources.getDrawable(R.drawable.emblem_elevator_normal);
      case 101:
        return paramResources.getDrawable(R.drawable.emblem_escalator_normal);
      case 102:
        return paramResources.getDrawable(R.drawable.emblem_stairs_normal);
      }
    case 7: // '\007'
        break;

    }

    if (PIDataUtils.inSet(PIDataUtils.TAXI_UUIDS, str)) {
      return paramResources.getDrawable(R.drawable.emblem_taxi_normal);
    }
    if (PIDataUtils.inSet(PIDataUtils.TRAM_UUIDS, str)) {
      return paramResources.getDrawable(R.drawable.emblem_tram_normal);
    }
    if (PIDataUtils.inSet(PIDataUtils.TRAIN_UUIDS, str)) {
      return paramResources.getDrawable(R.drawable.emblem_train_normal);
    }
    if (PIDataUtils.inSet(PIDataUtils.BUS_STOP_UUIDS, str)) {
      return paramResources.getDrawable(R.drawable.emblem_bus_stop_normal);
    }
    if (PIDataUtils.inSet(PIDataUtils.CAR_RENTAL_UUIDS, str)) {
      return paramResources.getDrawable(R.drawable.emblem_rental_car_normal);
    }
    throw new IllegalArgumentException("Unknown serviceUUID=" + str);
  }

  private void setServiceItems()
  {
      mServices.clear();
      addService(1, PIDataUtils.RESTROOM_UUIDS, null, R.string.service_restrooms, R.drawable.filter_icon_restrooms_normal, R.drawable. filter_icon_restrooms_pressed);
      String as[][] = new String[2][];
      as[0] = PIDataUtils.ATM_UUIDS;
      as[1] = PIDataUtils.CURRENCY_EXCHANGE_UUIDS;
      addService(2, PIDataUtils.mergeStringArray(as), null, R.string.service_atm, R.drawable.filter_icon_atm_normal, R.drawable.filter_icon_atm_pressed);
      addService(4, null, new int[] {
          100, 101, 102
      }, R.string.service_elevator_escalator, R.drawable.filter_icon_escelev_normal, R.drawable.filter_icon_escelev_pressed);
      String as1[][] = new String[5][];
      as1[0] = PIDataUtils.TAXI_UUIDS;
      as1[1] = PIDataUtils.TRAM_UUIDS;
      as1[2] = PIDataUtils.TRAIN_UUIDS;
      as1[3] = PIDataUtils.BUS_STOP_UUIDS;
      as1[4] = PIDataUtils.CAR_RENTAL_UUIDS;
      addService(7, PIDataUtils.mergeStringArray(as1), null, R.string.service_transportation, R.drawable.filter_icon_transportation_normal, R.drawable.filter_icon_transportation_pressed);
      addService(3, PIDataUtils.FOOD_UUIDS, null, R.string.service_food, R.drawable.filter_icon_food_normal, R.drawable.filter_icon_food_pressed);
      addService(5, PIDataUtils.PARKING_UUIDS, null, R.string.service_parking, R.drawable.filter_icon_parking_normal, R.drawable.filter_icon_parking_pressed);
      ArrayList arraylist = new ArrayList();
      Iterator iterator = mServices.iterator();
      for(;iterator.hasNext();) {
          Service service = (Service)iterator.next();
          arraylist.add(FilterWheel.FilterWheelItem.makeBitmapItem(service.iconNormal, service.iconSelected));
      }
      mWheel.setRightItems(arraylist, true);
      return;
      
  }

  private void setZoneItems()
  {
      int flag = 1;
      PIMapZoneDataCursor pimapzonedatacursor;
      mZones.clear();
      pimapzonedatacursor = mVenue.getVenueZones();
      ArrayList arraylist;
      Iterator iterator;
      boolean flag1;
      try {
      do
      {
          mZones.add(new Zone(pimapzonedatacursor.getZoneIndex(), pimapzonedatacursor.getZoneName()));
          flag1 = pimapzonedatacursor.moveToNext();
      } while(flag1);
      pimapzonedatacursor.close();
      arraylist = new ArrayList();
      iterator = mZones.iterator();

      for(;iterator.hasNext();)
      {
          mWheel.setLeftItems(arraylist, false);
          FilterWheel filterwheel = mWheel;
          if(arraylist.size() > flag)
              flag = 0;
          filterwheel.setLeftDisabled(flag==1);
          arraylist.add(FilterWheel.FilterWheelItem.makeTextItem(((Zone)iterator.next()).label));
      }

      } catch(Exception exception) {
    	  pimapzonedatacursor.close();
    	  exception.printStackTrace();
      //throw exception;
      }
  }

  public Service getService(int paramInt)
  {
    return (Service)this.mServices.get(paramInt);
  }

  public Zone getZone(int paramInt)
  {
    return (Zone)this.mZones.get(paramInt);
  }

  public void setVenue(PIMapVenue pimapvenue)
  {
      mVenue = pimapvenue;
      if(pimapvenue.getVenueType() == 2)
          mWheel.setLeftText(0x7f06006e);
      else
          mWheel.setLeftText(0x7f06006d);
      setZoneItems();
  }


  public void setZone(int paramInt, boolean paramBoolean)
  {
    System.out.println("setZone." + paramInt+":"+mZoneIndex+":"+this.mWheel.isLeftSelected());
    if (this.mZoneIndex == -1) {
      this.mWheel.setLeftSelected(false);
    }
    if (this.mZoneIndex != paramInt)
    {
      this.mZoneIndex = paramInt;
      this.mZoneIsOverview = paramBoolean;
      if (!this.mWheel.isLeftSelected())
      {
        setServiceItems();
        if (this.mWheel.isExpanded()) {
          emitManualServiceSelection();
        }
      }
      forceZoneSelection(paramInt);
    }
  }

  public static class Service
  {
    public Bitmap iconNormal;
    public Bitmap iconSelected;
    public String label;
    public String[] mapUUIDs;
    public int type;
    public int[] wormholeTypes;

    public Service(int paramInt1, String[] paramArrayOfString, int[] paramArrayOfInt, Resources paramResources, int paramInt2, int paramInt3, int paramInt4)
    {
      this.type = paramInt1;
      this.mapUUIDs = paramArrayOfString;
      this.wormholeTypes = paramArrayOfInt;
      this.label = paramResources.getString(paramInt2);
      this.iconNormal = ((BitmapDrawable)paramResources.getDrawable(paramInt3)).getBitmap();
      this.iconSelected = ((BitmapDrawable)paramResources.getDrawable(paramInt4)).getBitmap();
    }

    public String toString()
    {
      return this.label;
    }
  }

  public static class Zone
  {
    public String label;
    public int zoneIndex;

    public Zone(int paramInt, String paramString)
    {
      this.zoneIndex = paramInt;
      this.label = paramString;
    }

    public String toString()
    {
      return this.label;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.InVenueFilterHelper
 * JD-Core Version:    0.7.0.1
 */