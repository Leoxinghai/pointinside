package com.pointinside.android.api.dao;

import android.net.Uri;
import android.net.Uri.Builder;
//import com.pointinside.android.api.PIMapVenue.PIVenueAccess;
import com.pointinside.android.api.PIMapVenue;
import android.database.Cursor;

public class PIVenue
{
  static final String AUTHORITY = "com.pointinside.android.api.venue";
  public static final Uri MAP_ADDRESS_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_address");
  public static final Uri MAP_CATEGORY_CONTENT_URI;
  public static final Uri MAP_EVENT_CONTENT_URI;
  public static final Uri MAP_EVENT_PLACE_CONTENT_URI;
  public static final Uri MAP_IMAGE_CONTENT_URI;
  public static final Uri MAP_OPERATION_MINUTES_CONTENT_URI;
  public static final Uri MAP_PIXEL_COORDINATE_CONTENT_URI;
  public static final Uri MAP_PLACES_CATEGORIES_CONTENT_URI;
  public static final Uri MAP_PLACE_AREA_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_place_area");
  public static final Uri MAP_PLACE_CATEGORIES_CONTENT_URI;
  public static final Uri MAP_PLACE_CONTENT_URI;
  public static final Uri MAP_PLACE_ITEM_CONTENT_URI;
  public static final Uri MAP_PLACE_PROMOTION_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_place_promotion");
  public static final Uri MAP_PLACE_RAW_CONTENT_URI;
  public static final Uri MAP_POLYGON_ZONE_CONTENT_URI;
  public static final Uri MAP_PROMOTION_CONTENT_URI;
  public static final Uri MAP_PROMOTION_PLACE_CONTENT_URI;
  public static final Uri MAP_SERVICE_CONTENT_URI;
  public static final Uri MAP_VENUE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_venue");
  public static final Uri MAP_VENUE_PROMOTION_CONTENT_URI;
  public static final Uri MAP_VENUE_ZONE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_zone");
  public static final Uri MAP_WORMHOLE_CONTENT_URI;

  static
  {
    MAP_SERVICE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_service");
    MAP_PLACE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_place");
    MAP_PLACE_RAW_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_place_raw");
    MAP_PLACE_ITEM_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_place_item");
    MAP_WORMHOLE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_wormhole");
    MAP_CATEGORY_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_category");
    MAP_PLACE_CATEGORIES_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_place_category");
    MAP_PLACES_CATEGORIES_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_places_with_category");
    MAP_POLYGON_ZONE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_polygon_zone");
    MAP_PIXEL_COORDINATE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_pixel_coordinate");
    MAP_PROMOTION_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_promotion");
    MAP_PROMOTION_PLACE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_promotion_place");
    MAP_EVENT_PLACE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_event_place");
    MAP_VENUE_PROMOTION_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_venue_promotion");
    MAP_EVENT_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_event");
    MAP_IMAGE_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_image");
    MAP_OPERATION_MINUTES_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.venue/map_operation_minutes");
  }

  public PIVenue(PIMapVenue.PIVenueAccess paramPIVenueAccess) {}

  public static Uri getAddressUri(long paramLong)
  {
    return MAP_ADDRESS_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  private String getCommaString(long... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramVarArgs.length;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return localStringBuilder.toString();
      }
      Long localLong = Long.valueOf(paramVarArgs[j]);
      if (localLong != null)
      {
        localStringBuilder.append(localLong);
        if (j + 1 < i) {
          localStringBuilder.append(",");
        }
      }
    }
  }

  public static Uri getMapCategoryUri(long paramLong)
  {
    return MAP_CATEGORY_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapEventPlaceUri(long paramLong)
  {
    return MAP_EVENT_PLACE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapEventUri(long paramLong)
  {
    return MAP_EVENT_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapImageUri(long paramLong)
  {
    return MAP_IMAGE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapItemUri(long paramLong)
  {
    return MAP_PLACE_ITEM_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPixelCoordinateUri(long paramLong)
  {
    return MAP_PIXEL_COORDINATE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPlaceAreaUri(long paramLong)
  {
    return MAP_PLACE_AREA_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPlaceCategoriesUri(long paramLong)
  {
    return MAP_PLACE_CATEGORIES_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPlaceUri(long paramLong)
  {
    return MAP_PLACE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPolygonZoneUri(long paramLong)
  {
    return MAP_POLYGON_ZONE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPromotionPlaceUri(long paramLong)
  {
    return MAP_PROMOTION_PLACE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapPromotionUri(long paramLong)
  {
    return MAP_PROMOTION_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapServiceUri(long paramLong)
  {
    return MAP_SERVICE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapVenuePromotionUri(long paramLong)
  {
    return MAP_VENUE_PROMOTION_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getMapWormholeUri(long paramLong)
  {
    return MAP_WORMHOLE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getOperationMinutesUri(long paramLong)
  {
    return MAP_OPERATION_MINUTES_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getVenueUri(long paramLong)
  {
    return MAP_VENUE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getVenueUri(String paramString)
  {
    return MAP_VENUE_CONTENT_URI.buildUpon().appendPath(paramString).build();
  }

  public static Uri getVenueZoneUri(long paramLong)
  {
    return MAP_VENUE_ZONE_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public PIMapAddressDataCursor getAddress(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapAddressDataCursor.getInstance(paramPIVenueDataset.query(getAddressUri(paramLong), null, null, null, null));
  }

  public PIMapAddressDataCursor getAddresses(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapAddressDataCursor.getInstance(paramPIVenueDataset.query(MAP_ADDRESS_CONTENT_URI, null, null, null, null));
  }

  public PIMapPlaceDataCursor getAllMapPlaces(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapPlaceDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_RAW_CONTENT_URI, null, null, null, "lower(venue_place.name) ASC"));
  }

  public PIMapPromotionDataCursor getAllPromotions(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_CONTENT_URI, null, "display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public int getAllPromotionsCount(PIVenueDataset paramPIVenueDataset)
  {
    PIMapPromotionDataCursor localPIMapPromotionDataCursor = getPromotions(paramPIVenueDataset, new long[0]);
    int i = 0;
    if (localPIMapPromotionDataCursor == null) {
    	return 0;
    }
        i = localPIMapPromotionDataCursor.getCount();
      if (localPIMapPromotionDataCursor != null) {
        localPIMapPromotionDataCursor.close();
      }
      return i;
  }

  public PIMapPromotionDataCursor getAllPromotionsSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_CONTENT_URI, null, "(title LIKE '%" + paramString + "%' OR promotion.description LIKE '%" + paramString + "%') AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapPlaceCategoriesDataCursor getCategoriesForPlace(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    Uri localUri = MAP_PLACE_CATEGORIES_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    return PIMapPlaceCategoriesDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, "venue_place_category_type.venue_place_id = ?", arrayOfString, "lower(category_type.name) ASC"));
  }

  public int getDealCount(PIVenueDataset paramPIVenueDataset)
  {
    PIMapDealDataCursor localPIMapDealDataCursor = getDeals(paramPIVenueDataset);
    if (localPIMapDealDataCursor == null) {}
    for (int i = 0;; i = localPIMapDealDataCursor.getCount())
    {
      if (localPIMapDealDataCursor != null) {
        localPIMapDealDataCursor.close();
      }
      return i;
    }
  }

  public PIMapDealDataCursor getDealSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapDealDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_PLACE_CONTENT_URI, null, "(title LIKE '%" + paramString + "%' OR promotion.description LIKE '%" + paramString + "%') AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapDealDataCursor getDeals(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapDealDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_PLACE_CONTENT_URI, null, "display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapDealDataCursor getDealsForPlace(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return getDealsForPlace(paramPIVenueDataset, true, paramLong);
  }

  public PIMapDealDataCursor getDealsForPlace(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapDealDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_PLACE_CONTENT_URI, null, "venue_place.uuid = ? AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", new String[] { paramString }, "lower(promotion.title) ASC"));
  }

  public PIMapDealDataCursor getDealsForPlace(PIVenueDataset paramPIVenueDataset, boolean paramBoolean, long paramLong)
  {
    if (paramBoolean) {}
    for (String str = "promotion_venue_place.venue_place_id = ? AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')";; str = "promotion_venue_place.venue_place_id = ?")
    {
      Uri localUri = MAP_PROMOTION_PLACE_CONTENT_URI;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = String.valueOf(paramLong);
      return PIMapDealDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, str, arrayOfString, "lower(promotion.title) ASC"));
    }
  }

  public int getEventCount(PIVenueDataset paramPIVenueDataset)
  {
    PIMapPromotionDataCursor localPIMapPromotionDataCursor = getEvents(paramPIVenueDataset);
    if (localPIMapPromotionDataCursor == null) {}
    for (int i = 0;; i = localPIMapPromotionDataCursor.getCount())
    {
      if (localPIMapPromotionDataCursor != null) {
        localPIMapPromotionDataCursor.close();
      }
      return i;
    }
  }

  public PIMapPromotionDataCursor getEventSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_EVENT_CONTENT_URI, null, "(title LIKE '%" + paramString + "%' OR promotion.description LIKE '%" + paramString + "%') AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapPromotionDataCursor getEvents(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_EVENT_CONTENT_URI, null, "display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapEventDataCursor getEventsForPlace(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return getEventsForPlace(paramPIVenueDataset, true, paramLong);
  }

  public PIMapEventDataCursor getEventsForPlace(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapEventDataCursor.getInstance(paramPIVenueDataset.query(MAP_EVENT_PLACE_CONTENT_URI, null, "promotion_venue_place.uuid = ? AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", new String[] { paramString }, "lower(promotion.title) ASC"));
  }

  public PIMapEventDataCursor getEventsForPlace(PIVenueDataset paramPIVenueDataset, boolean paramBoolean, long paramLong)
  {
    if (paramBoolean) {}
    for (String str = "promotion_venue_place.venue_place_id = ? AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')";; str = "promotion_venue_place.venue_place_id = ?")
    {
      Uri localUri = MAP_EVENT_PLACE_CONTENT_URI;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = String.valueOf(paramLong);
      return PIMapEventDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, str, arrayOfString, "lower(promotion.title) ASC"));
    }
  }

  public PIMapImageDataCursor getImage(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapImageDataCursor.getInstance(paramPIVenueDataset.query(getMapImageUri(paramLong), null, null, null, null));
  }

  public PIMapImageDataCursor getImages(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapImageDataCursor.getInstance(paramPIVenueDataset.query(MAP_IMAGE_CONTENT_URI, null, null, null, null));
  }

  public PIMapCategoryDataCursor getMapCategories(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapCategoryDataCursor.getInstance(paramPIVenueDataset.query(MAP_CATEGORY_CONTENT_URI, null, null, null, "name ASC"));
  }

  public PIMapCategoryDataCursor getMapCategory(PIVenueDataset paramPIVenueDataset, int paramInt)
  {
    return PIMapCategoryDataCursor.getInstance(paramPIVenueDataset.query(getMapCategoryUri(paramInt), null, null, null, "name ASC"));
  }

  public PIMapItemDataCursor getMapItem(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(getMapItemUri(paramLong), null, null, null, "name ASC"));
  }

  public PIMapItemDataCursor getMapItemForIlcCode(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_ITEM_CONTENT_URI, null, "ilc_code=?", new String[] { paramString }, "lower(name) ASC"));
  }

  public PIMapItemDataCursor getMapItemForUUID(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_ITEM_CONTENT_URI, null, "uuid=?", new String[] { paramString }, "name ASC"));
  }

  public PIMapItemDataCursor getMapItemNameStartsWith(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_ITEM_CONTENT_URI, null, "name LIKE '" + paramString + "%'", null, "lower(name) ASC"));
  }

  public PIMapItemDataCursor getMapItemSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_ITEM_CONTENT_URI, null, "name LIKE '%" + paramString + "%'", null, "lower(name) ASC"));
  }

  public PIMapItemDataCursor getMapItems(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_ITEM_CONTENT_URI, null, null, null, "lower(name) ASC"));
  }

  public PIMapPlaceDataCursor getMapPlace(PIVenueDataset paramPIVenueDataset, int paramInt)
  {
    return PIMapPlaceDataCursor.getInstance(paramPIVenueDataset.query(getMapPlaceUri(paramInt), null, null, null, "lower(venue_place.name) ASC"));
  }

  public PIMapAreaDataCursor getMapPlaceAreaForPlace(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    Uri localUri = MAP_PLACE_AREA_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    return PIMapAreaDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, "special_area_venue_place.venue_place_id=?", arrayOfString, null));
  }

  public PIMapAreaDataCursor getMapPlaceAreas(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapAreaDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_AREA_CONTENT_URI, null, null, null, null));
  }

  public PIMapAreaDataCursor getMapPlaceAreas(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapAreaDataCursor.getInstance(paramPIVenueDataset.query(getMapPlaceAreaUri(paramLong), null, null, null, null));
  }

  public PIMapItemDataCursor getMapPlaceForPromotion(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    Uri localUri = MAP_PLACE_PROMOTION_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = Long.toString(paramLong);
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, "promotion_venue_place.promotion_id=?", arrayOfString, null));
  }

  public PIMapServiceDataCursor getMapPlaceForServiceTypeUUID(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapServiceDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_RAW_CONTENT_URI, null, "service_type_uuid=?", new String[] { paramString }, "lower(venue_place.name) ASC"));
  }

  public PIMapPlaceDataCursor getMapPlaceForUUID(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapPlaceDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_RAW_CONTENT_URI, null, "venue_place.uuid=?", new String[] { paramString }, "venue_place.name ASC"));
  }

  public PIMapPlaceDataCursor getMapPlaceSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapPlaceDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_CONTENT_URI, null, "(venue_place.name LIKE '%" + paramString + "%' OR venue_place.description LIKE '%" + paramString + "%') " + "OR venue_place.venue_place_id IN (SELECT venue_place_category_type.venue_place_id FROM venue_place_category_type " + "JOIN category_type ON venue_place_category_type.category_type_id=category_type.category_type_id " + "WHERE category_type.name LIKE '%" + paramString + "%')", null, "lower(venue_place.name) ASC"));
  }

  public PIMapPlaceDataCursor getMapPlaces(PIVenueDataset paramPIVenueDataset)
  {
    return getMapPlaces(paramPIVenueDataset, false);
  }

  public PIMapPlaceDataCursor getMapPlaces(PIVenueDataset paramPIVenueDataset, boolean paramBoolean)
  {
    Uri localUri = MAP_PLACE_CONTENT_URI;
    if (!paramBoolean) {}
    for (String str = null;; str = "venue_place.name <> 'Open'") {
      return PIMapPlaceDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, str, null, "lower(venue_place.name) ASC"));
    }
  }

  public PIMapPlaceDataCursor getMapPlacesForUUIDs(PIVenueDataset paramPIVenueDataset, String[] paramArrayOfString)
  {
    if (paramArrayOfString.length == 0) {
      throw new IllegalArgumentException("uuids array must not be empty");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfString.length;
    localStringBuilder.append("?");
    for (int j = 0;; j++)
    {
      if (j >= i - 1) {
        return PIMapPlaceDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_RAW_CONTENT_URI, null, "venue_place.uuid IN (" + localStringBuilder.toString() + ")", paramArrayOfString, "venue_place.name ASC"));
      }
      localStringBuilder.append(",?");
    }
  }

  public PIMapItemDataCursor getMapPlacesWithPromotion(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapItemDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_PROMOTION_CONTENT_URI, null, null, null, "lower(name) ASC"));
  }

  public PIMapServiceDataCursor getMapService(PIVenueDataset paramPIVenueDataset, int paramInt)
  {
    return PIMapServiceDataCursor.getInstance(paramPIVenueDataset.query(getMapServiceUri(paramInt), null, null, null, "lower(venue_place.name) ASC"));
  }

  public PIMapServiceDataCursor getMapService(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapServiceDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACE_RAW_CONTENT_URI, null, "service_type_name=?", new String[] { paramString }, "lower(venue_place.name) ASC"));
  }

  public PIMapServiceDataCursor getMapServiceSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapServiceDataCursor.getInstance(paramPIVenueDataset.query(MAP_SERVICE_CONTENT_URI, null, "(venue_place.name LIKE '%" + paramString + "%' OR venue_place.description LIKE '%" + paramString + "%') " + "OR venue_place.venue_place_id IN (SELECT venue_place_category_type.venue_place_id FROM venue_place_category_type " + "JOIN category_type ON venue_place_category_type.category_type_id=category_type.category_type_id " + "WHERE category_type.name LIKE '%" + paramString + "%')", null, "lower(venue_place.name) ASC"));
  }

  public PIMapServiceGroupedDataCursor getMapServiceSearchForNameGrouped(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapServiceGroupedDataCursor.getInstance(paramPIVenueDataset.query(MAP_SERVICE_CONTENT_URI, new String[] { "name", "zone_id", "zone_index", "min(venue_place.venue_place_id) AS '_id'", "count(venue_place.venue_place_id) AS 'count'" }, "(venue_place.name LIKE '%" + paramString + "%' OR venue_place.description LIKE '%" + paramString + "%') " + "OR venue_place.venue_place_id IN (SELECT venue_place_category_type.venue_place_id FROM venue_place_category_type " + "JOIN category_type ON venue_place_category_type.category_type_id=category_type.category_type_id " + "WHERE category_type.name LIKE '%" + paramString + "%')", null, "lower(venue_place.name) ASC", String.format("%s, %s, %s", new Object[] { "name", "zone_id", "zone_index" })));
  }

  public PIMapServiceDataCursor getMapServices(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapServiceDataCursor.getInstance(paramPIVenueDataset.query(MAP_SERVICE_CONTENT_URI, null, null, null, "lower(venue_place.name) ASC"));
  }

  public PIMapServiceGroupedDataCursor getMapServicesGrouped(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapServiceGroupedDataCursor.getInstance(paramPIVenueDataset.query(MAP_SERVICE_CONTENT_URI, new String[] { "name", "zone_id", "zone_index", "min(venue_place.venue_place_id) AS '_id'", "count(venue_place.venue_place_id) AS 'count'" }, null, null, "lower(venue_place.name) ASC", String.format("%s, %s, %s", new Object[] { "name", "zone_id", "zone_index" })));
  }

  public PIMapWormholeDataCursor getMapWormhole(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapWormholeDataCursor.getInstance(paramPIVenueDataset.query(getMapWormholeUri(paramLong), null, null, null, "lower(wormhole_type.name) ASC"));
  }

  public PIMapWormholeDataCursor getMapWormholeForType(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    Uri localUri = MAP_WORMHOLE_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    return PIMapWormholeDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, "wormhole.wormhole_type_id=?", arrayOfString, "lower(wormhole_type.name) ASC"));
  }

  public PIMapWormholeDataCursor getMapWormholes(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapWormholeDataCursor.getInstance(paramPIVenueDataset.query(MAP_WORMHOLE_CONTENT_URI, null, null, null, "lower(wormhole_type.name) ASC"));
  }

  public PIMapOperationMinutesDataCursor getOperationMinutes(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapOperationMinutesDataCursor.getInstance(paramPIVenueDataset.query(MAP_OPERATION_MINUTES_CONTENT_URI, null, null, null, null));
  }

  public PIMapOperationMinutesDataCursor getOperationMinutes(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapOperationMinutesDataCursor.getInstance(paramPIVenueDataset.query(getOperationMinutesUri(paramLong), null, null, null, null));
  }

  public PixelCoordinateDataCursor getPixelCoordinates(PIVenueDataset paramPIVenueDataset)
  {
    return PixelCoordinateDataCursor.getInstance(paramPIVenueDataset.query(MAP_PIXEL_COORDINATE_CONTENT_URI, null, null, null, null));
  }

  public PixelCoordinateDataCursor getPixelCoordinatesForArea(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    Uri localUri = MAP_PIXEL_COORDINATE_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    return PixelCoordinateDataCursor.getInstance(paramPIVenueDataset.query(localUri, null, "special_area_id=?", arrayOfString, null));
  }

  public int getPlaceEventCount(PIVenueDataset paramPIVenueDataset)
  {
    PIMapEventDataCursor localPIMapEventDataCursor = getPlaceEvents(paramPIVenueDataset);
    if (localPIMapEventDataCursor == null) {}
    for (int i = 0;; i = localPIMapEventDataCursor.getCount())
    {
      if (localPIMapEventDataCursor != null) {
        localPIMapEventDataCursor.close();
      }
      return i;
    }
  }

  public PIMapEventDataCursor getPlaceEvents(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapEventDataCursor.getInstance(paramPIVenueDataset.query(MAP_EVENT_PLACE_CONTENT_URI, null, "display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapPlacesWithCategoryDataCursor getPlacesByCategory(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapPlacesWithCategoryDataCursor.getInstance(paramPIVenueDataset.query(MAP_PLACES_CATEGORIES_CONTENT_URI, null, null, null, "lower(category_type.name) ASC, lower(venue_place.name) ASC"));
  }

  public PIMapPolygonZoneDataCursor getPolygonZones(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapPolygonZoneDataCursor.getInstance(paramPIVenueDataset.query(MAP_POLYGON_ZONE_CONTENT_URI, null, null, null, "special_area_id, zone_index"));
  }

  public PIMapPromotionDataCursor getPromotion(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(getMapPromotionUri(paramLong), null, null, null, "lower(promotion.title) ASC"));
  }

  public int getPromotionCount(PIVenueDataset paramPIVenueDataset)
  {
    PIMapPromotionDataCursor localPIMapPromotionDataCursor = getPromotions(paramPIVenueDataset, new long[] { 2L });
    int i = 0;
    if (localPIMapPromotionDataCursor == null) {
    	return 0;
    }
    i = localPIMapPromotionDataCursor.getCount();
      if (localPIMapPromotionDataCursor != null) {
        localPIMapPromotionDataCursor.close();
      }
      return i;
  }

  public PIMapPromotionDataCursor getPromotionSearchForName(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_CONTENT_URI, null, "(title LIKE '%" + paramString + "%' OR promotion.description LIKE '%" + paramString + "%') AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime')", null, "lower(promotion.title) ASC"));
  }

  public PIMapPromotionDataCursor getPromotions(PIVenueDataset paramPIVenueDataset, boolean paramBoolean, long... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length == 0)) {
      return getAllPromotions(paramPIVenueDataset);
    }
    if (paramBoolean) {}
    for (String str = "display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime') AND promotion.promotion_type_id IN (" + getCommaString(paramVarArgs) + ")";; str = "promotion.promotion_type_id IN (" + getCommaString(paramVarArgs) + ")") {
      return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_CONTENT_URI, null, str, null, "lower(promotion.title) ASC"));
    }
  }

  public PIMapPromotionDataCursor getPromotions(PIVenueDataset paramPIVenueDataset, long... paramVarArgs)
  {
    return getPromotions(paramPIVenueDataset, true, paramVarArgs);
  }

  public PIMapPromotionDataCursor getPromotionsSearchForName(PIVenueDataset paramPIVenueDataset, String paramString, long... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length == 0)) {
      return getAllPromotionsSearchForName(paramPIVenueDataset, paramString);
    }
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_PROMOTION_CONTENT_URI, null, "(title LIKE '%" + paramString + "%' OR promotion.description LIKE '%" + paramString + "%') AND display_end_datetime >= datetime('now', 'localtime') AND display_start_datetime <= datetime('now', 'localtime') " + "AND promotion.promotion_type_id IN (" + getCommaString(paramVarArgs) + ")", null, "lower(promotion.title) ASC"));
  }

  public PIMapVenueDataCursor getVenue(PIVenueDataset paramPIVenueDataset, int paramInt)
  {
    return PIMapVenueDataCursor.getInstance(paramPIVenueDataset.query(getVenueUri(paramInt), null, null, null, null));
  }

  public PIMapVenueDataCursor getVenue(PIVenueDataset paramPIVenueDataset, String paramString)
  {
    return PIMapVenueDataCursor.getInstance(paramPIVenueDataset.query(getVenueUri(paramString), null, null, null, null));
  }

  public PIMapPromotionDataCursor getVenuePromotion(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(getMapVenuePromotionUri(paramLong), null, null, null, "lower(promotion.title) ASC"));
  }

  public PIMapPromotionDataCursor getVenuePromotions(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapPromotionDataCursor.getInstance(paramPIVenueDataset.query(MAP_VENUE_PROMOTION_CONTENT_URI, null, null, null, "lower(promotion.title) ASC"));
  }

  public PIMapZoneDataCursor getVenueZone(PIVenueDataset paramPIVenueDataset, long paramLong)
  {
    return PIMapZoneDataCursor.getInstance(paramPIVenueDataset.query(getVenueZoneUri(paramLong), null, null, null, null));
  }

  public PIMapZoneDataCursor getVenueZones(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapZoneDataCursor.getInstance(paramPIVenueDataset.query(MAP_VENUE_ZONE_CONTENT_URI, null, null, null, "is_overview_zone DESC, zone_display_order"));
  }

  public PIMapVenueDataCursor getVenues(PIVenueDataset paramPIVenueDataset)
  {
    return PIMapVenueDataCursor.getInstance(paramPIVenueDataset.query(MAP_VENUE_CONTENT_URI, null, null, null, "name ASC"));
  }

  public boolean hasOverview(PIVenueDataset pivenuedataset)
  {
      Cursor cursor = null;
      cursor = pivenuedataset.query(MAP_VENUE_ZONE_CONTENT_URI, null, "is_overview_zone=1", null, null);
      boolean flag = false;
      if(cursor != null) {

	      int i = cursor.getCount();
	      if(i > 0) {
	    	  flag = true;
	      }

	      if(cursor != null)
	          cursor.close();
	      return flag;
      }
      flag = false;
      if(cursor != null)
          cursor.close();
      return flag;
  }


  static abstract interface AddressColumns
  {
    public static final String ADDRESS1 = "address_line1";
    public static final String ADDRESS2 = "address_line2";
    public static final String ADDRESS_ID = "address_id";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String POSTAL_CODE = "postal_code";
    public static final String STATE_CODE = "state_code";
  }

  static abstract interface CategoryTypeColumns
  {
    public static final String CATEGORY_TYPE_ID = "category_type_id";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
  }

  static abstract interface ImageColumns
  {
    public static final String IMAGE_ID = "_id";
    public static final String IMAGE_NAME = "name";
  }

  static abstract interface OperationMinutesColumns
  {
    public static final String FRIDAY_CLOSE_MINUTE = "friday_close_minute";
    public static final String FRIDAY_OPEN_MINUTE = "friday_open_minute";
    public static final String MONDAY_CLOSE_MINUTE = "monday_close_minute";
    public static final String MONDAY_OPEN_MINUTE = "monday_open_minute";
    public static final String OPERATION_MINUTES_ID = "operation_minutes_id";
    public static final String SATURDAY_CLOSE_MINUTE = "saturday_close_minute";
    public static final String SATURDAY_OPEN_MINUTE = "saturday_open_minute";
    public static final String SUNDAY_CLOSE_MINUTE = "sunday_close_minute";
    public static final String SUNDAY_OPEN_MINUTE = "sunday_open_minute";
    public static final String THURSDAY_CLOSE_MINUTE = "thursday_close_minute";
    public static final String THURSDAY_OPEN_MINUTE = "thursday_open_minute";
    public static final String TUESDAY_CLOSE_MINUTE = "tuesday_close_minute";
    public static final String TUESDAY_OPEN_MINUTE = "tuesday_open_minute";
    public static final String WEDNESDAY_CLOSE_MINUTE = "wednesday_close_minute";
    public static final String WEDNESDAY_OPEN_MINUTE = "wednesday_open_minute";
  }

  static abstract interface PixelCoordinateColumns
  {
    public static final String ALTITUDE = "altitude";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String PIXEL_COORDINATE_ID = "_id";
    public static final String PIXEL_X = "pixel_x";
    public static final String PIXEL_Y = "pixel_y";
    public static final String SPECIAL_AREA_ID = "special_area_id";
  }

  static abstract interface PlaceAreaColumns
  {
    public static final String NAME = "name";
    public static final String SPECIAL_AREA_ID = "special_area_id";
    public static final String VENUE_PLACE_ID = "venue_place_id";
  }

  static abstract interface PlaceColumns
  {
    public static final String DESCRIPTION = "description";
    public static final String FRIDAY_CLOSE_MINUTE = "friday_close_minute";
    public static final String FRIDAY_OPEN_MINUTE = "friday_open_minute";
    public static final String ILC_CODE = "ilc_code";
    public static final String IMAGE_ID = "image_id";
    public static final String IS_DISPLAYABLE = "is_displayable";
    public static final String IS_PORTALED = "is_portaled";
    public static final String LOCATION_PIXEL_X = "location_pixel_x";
    public static final String LOCATION_PIXEL_Y = "location_pixel_y";
    public static final String MONDAY_CLOSE_MINUTE = "monday_close_minute";
    public static final String MONDAY_OPEN_MINUTE = "monday_open_minute";
    public static final String NAME = "name";
    public static final String OPERATION_TIMES_ID = "operation_times_id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PLACE_TYPE_ID = "place_type_id";
    public static final String PLACE_TYPE_NAME = "place_type_name";
    public static final String SATURDAY_CLOSE_MINUTE = "saturday_close_minute";
    public static final String SATURDAY_OPEN_MINUTE = "saturday_open_minute";
    public static final String SERVICE_TYPE_ID = "service_type_id";
    public static final String SERVICE_TYPE_NAME = "service_type_name";
    public static final String SERVICE_TYPE_UUID = "service_type_uuid";
    public static final String SUNDAY_CLOSE_MINUTE = "sunday_close_minute";
    public static final String SUNDAY_OPEN_MINUTE = "sunday_open_minute";
    public static final String THURSDAY_CLOSE_MINUTE = "thursday_close_minute";
    public static final String THURSDAY_OPEN_MINUTE = "thursday_open_minute";
    public static final String TUESDAY_CLOSE_MINUTE = "tuesday_close_minute";
    public static final String TUESDAY_OPEN_MINUTE = "tuesday_open_minute";
    public static final String UUID = "uuid";
    public static final String VENUE_ID = "venue_id";
    public static final String VENUE_PLACE_ID = "venue_place_id";
    public static final String WEBSITE = "website";
    public static final String WEDNESDAY_CLOSE_MINUTE = "wednesday_close_minute";
    public static final String WEDNESDAY_OPEN_MINUTE = "wednesday_open_minute";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_INDEX = "zone_index";
  }

  static abstract interface PlaceTypeColumns
  {
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String PLACE_TYPE_ID = "place_type_id";
  }

  static abstract interface PolygonZoneColumns
  {
    public static final String HOST_ZONE_INDEX = "zonesrc_index";
    public static final String HOTLINK_ID = "_id";
    public static final String NAME = "name";
    public static final String PIXEL_X = "pixel_x";
    public static final String PIXEL_Y = "pixel_y";
    public static final String PLACE_COUNT = "place_count";
    public static final String SPECIAL_AREA_ID = "special_area_id";
    public static final String ZONE_DISPLAY_ORDER = "zone_display_order";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_INDEX = "zone_index";
  }

  static abstract interface PromotionColumns
  {
    public static final String ACTIVE_FRIDAY = "active_friday";
    public static final String ACTIVE_MONDAY = "active_monday";
    public static final String ACTIVE_SATURDAY = "active_saturday";
    public static final String ACTIVE_SUNDAY = "active_sunday";
    public static final String ACTIVE_THURSDAY = "active_thursday";
    public static final String ACTIVE_TUESDAY = "active_tuesday";
    public static final String ACTIVE_WEDNESDAY = "active_wednesday";
    public static final String CODE_ID = "code_id";
    public static final String CONTACT_INFORMATION = "contact_information";
    public static final String DISPLAY_END_DATE = "display_end_datetime";
    public static final String DISPLAY_START_DATE = "display_start_datetime";
    public static final String END_DATE = "end_datetime";
    public static final String FREQUENCY = "frequency";
    public static final String IMAGE_ID = "image_id";
    public static final String LOCATION = "location";
    public static final String OTHER_INFORMATION = "other_information";
    public static final String PROMOTION_DESCRIPTION = "description";
    public static final String PROMOTION_ID = "promotion_id";
    public static final String PROMOTION_TYPE_ID = "promotion_type_id";
    public static final String START_DATE = "start_datetime";
    public static final String TITLE = "title";
    public static final String VENUE_PLACE_ID = "venue_place_id";
  }

  static abstract interface ServiceTypeColumns
  {
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String SERVICE_TYPE_ID = "service_type_id";
  }

  static abstract interface VenueColumns
  {
    public static final String ABOVE_GROUND_ZONE_COUNT = "above_ground_zone_count";
    public static final String ADDRESS1 = "address_line1";
    public static final String ADDRESS2 = "address_line2";
    public static final String BELOW_GROUND_ZONE_COUNT = "below_ground_zone_count";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String CREATION_DATE = "creation_date";
    public static final String DESCRIPTION = "description";
    public static final String EMAIL = "email";
    public static final String IMAGE_ID = "image_id";
    public static final String IS_VALID_SHOP_NUMBERS = "is_valid_shop_numbers";
    public static final String NAME = "name";
    public static final String OPERATION_TIMES_ID = "operation_times_id";
    public static final String ORGANIZATION_ID = "organization_id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String POSTAL_CODE = "postal_code";
    public static final String STATE_CODE = "state_code";
    public static final String USER_DEFINED_ID = "user_defined_id";
    public static final String VENUE_CLASSIFICATION = "venue_classification";
    public static final String VENUE_ID = "venue_id";
    public static final String VENUE_TYPE_ID = "venue_type_id";
    public static final String VENUE_UUID = "uuid";
    public static final String WEBSITE = "website";
    public static final String WORMHOLE_COUNT = "wormhole_count";
  }

  static abstract interface VenuePlaceCategoryTypeColumns
  {
    public static final String CATEGORY_TYPE_ID = "category_type_id";
    public static final String VENUE_PLACE_ID = "venue_place_id";
  }

  static abstract interface VenuePlacesWithCategoryTypeColumns
    extends PIVenue.VenuePlaceCategoryTypeColumns
  {
    public static final String CATEGORY_NAME = "category_name";
    public static final String PLACE_NAME = "place_name";
    public static final String PLACE_UUID = "place_uuid";
  }

  static abstract interface WormholeColumns
  {
    public static final String DESCRIPTION = "description";
    public static final String IS_HANDICAP_ACCESSIBLE = "is_handicap_accessible";
    public static final String LOCATION_PIXEL_X = "location_pixel_x";
    public static final String LOCATION_PIXEL_Y = "location_pixel_y";
    public static final String NAME = "name";
    public static final String WORMHOLE_ID = "wormhole_id";
    public static final String WORMHOLE_TYPE_ID = "wormhole_type_id";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_INDEX = "zone_index";
  }

  static abstract interface ZoneColumns
  {
    public static final String BEARING_POINT2_POINT1 = "bearing_point2_point1";
    public static final String BEARING_POINT4_POINT3 = "bearing_point4_point3";
    public static final String COUNT_PIXEL_POINT1_POINT2_Y = "count_pixel_point1_point2_y";
    public static final String COUNT_PIXEL_POINT3_POINT4_X = "count_pixel_point3_point4_x";
    public static final String FEET_PER_PIXEL_X = "feet_per_pixel_x";
    public static final String FEET_PER_PIXEL_Y = "feet_per_pixel_y";
    public static final String IMAGE_ID = "image_id";
    public static final String IMAGE_NAME = "image_name";
    public static final String IMAGE_SIZE_PIXEL_X = "image_size_pixel_x";
    public static final String IMAGE_SIZE_PIXEL_Y = "image_size_pixel_y";
    public static final String IS_ABOVE_GROUND_ZONE = "is_above_ground_zone";
    public static final String IS_OVERVIEW_ZONE = "is_overview_zone";
    public static final String PLACE_COUNT = "place_count";
    public static final String POINT1_LATITUDE = "point1_latitude";
    public static final String POINT1_LONGITUDE = "point1_longitude";
    public static final String POINT1_PIXEL_X = "point1_pixel_x";
    public static final String POINT1_PIXEL_Y = "point1_pixel_y";
    public static final String POINT2_LATITUDE = "point2_latitude";
    public static final String POINT2_LONGITUDE = "point2_longitude";
    public static final String POINT2_PIXEL_X = "point2_pixel_x";
    public static final String POINT2_PIXEL_Y = "point2_pixel_y";
    public static final String POINT3_LATITUDE = "point3_latitude";
    public static final String POINT3_LONGITUDE = "point3_longitude";
    public static final String POINT3_PIXEL_X = "point3_pixel_x";
    public static final String POINT3_PIXEL_Y = "point3_pixel_y";
    public static final String POINT4_LATITUDE = "point4_latitude";
    public static final String POINT4_LONGITUDE = "point4_longitude";
    public static final String POINT4_PIXEL_X = "point4_pixel_x";
    public static final String POINT4_PIXEL_Y = "point4_pixel_y";
    public static final String VENUE_ID = "venue_id";
    public static final String VENUE_UUID = "uuid";
    public static final String ZONE_DISPLAY_ORDER = "zone_display_order";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_INDEX = "zone_index";
    public static final String ZONE_NAME = "zone_name";
    public static final String ZONE_TAG_TEXT = "zone_tag_text";
    public static final String ZONE_UUID = "zone_uuid";
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIVenue
 * JD-Core Version:    0.7.0.1
 */