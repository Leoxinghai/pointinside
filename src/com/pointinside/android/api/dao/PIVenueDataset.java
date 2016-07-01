package com.pointinside.android.api.dao;

import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;
import java.util.List;

public class PIVenueDataset
  implements PIMapDataset
{
  private static final int ADDRESS = 31;
  private static final int ADDRESS_ID = 32;
  private static final int CATEGORIES = 12;
  private static final int CATEGORY_ID = 13;
  private static final int EVENTS = 25;
  private static final int EVENT_ID = 26;
  private static final int IMAGE = 29;
  private static final int IMAGE_ID = 30;
  static final String MAP_ADDRESS = "map_address";
  static final String MAP_CATEGORY = "map_category";
  static final String MAP_EVENT = "map_event";
  static final String MAP_EVENT_PLACE = "map_event_place";
  static final String MAP_IMAGE = "map_image";
  static final String MAP_OPERATION_MINUTES = "map_operation_minutes";
  static final String MAP_PIXEL_COORDINATE = "map_pixel_coordinate";
  static final String MAP_PLACE = "map_place";
  static final String MAP_PLACES_WITH_CATEGORIES = "map_places_with_category";
  static final String MAP_PLACE_AREA = "map_place_area";
  static final String MAP_PLACE_CATEGORIES = "map_place_category";
  static final String MAP_PLACE_ITEM = "map_place_item";
  static final String MAP_PLACE_PROMOTION = "map_place_promotion";
  static final String MAP_PLACE_RAW = "map_place_raw";
  static final String MAP_POLYGON_ZONE = "map_polygon_zone";
  static final String MAP_PROMOTION = "map_promotion";
  static final String MAP_PROMOTION_PLACE = "map_promotion_place";
  static final String MAP_SERVICE = "map_service";
  static final String MAP_VENUE = "map_venue";
  static final String MAP_VENUE_PROMOTION = "map_venue_promotion";
  static final String MAP_WORMHOLE = "map_wormhole";
  static final String MAP_ZONE = "map_zone";
  private static final int OPERATION_MINUTES = 33;
  private static final int OPERATION_MINUTES_ID = 34;
  private static final int PIXEL_COORDINATE = 16;
  private static final int PIXEL_COORDINATE_ID = 17;
  private static final int PLACES = 6;
  private static final int PLACE_AREA = 35;
  private static final int PLACE_AREA_ID = 36;
  private static final int PLACE_ID = 7;
  private static final int PLACE_ITEM = 27;
  private static final int PLACE_ITEM_ID = 28;
  private static final int PLACE_PROMOTION_ITEM = 37;
  private static final int PROMOTIONS = 19;
  private static final int PROMOTION_ID = 20;
  private static final int RAW_PLACE = 18;
  private static final int SERVICES = 4;
  private static final int SERVICE_ID = 5;
  private static final int SPECIAL_AREAS = 14;
  private static final int SPECIAL_AREA_ID = 15;
  private static final String TABLE_ADDRESS = "address";
  private static final String TABLE_CATEGORY_TYPE = "category_type";
  private static final String TABLE_IMAGE = "image";
  private static final String TABLE_OPERATION_MINUTES = "operation_minutes";
  private static final String TABLE_PIXEL_COORDINATE_JOIN = "pixel_coordinate JOIN special_area_pixel_coordinate ON special_area_pixel_coordinate.pixel_coordinate_id=pixel_coordinate.pixel_coordinate_id";
  private static final String TABLE_PLACE_AREA = "special_area JOIN special_area_venue_place ON special_area.special_area_id = special_area_venue_place.special_area_id";
  private static final String TABLE_PLACE_TYPE = "place_type";
  private static final String TABLE_PROMOTION_JOIN = "promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id";
  private static final String TABLE_PROMOTION_PLACE_JOIN = "promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue_place ON promotion.promotion_id=promotion_venue_place.promotion_id JOIN venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id";
  private static final String TABLE_PROMOTION_VENUE_JOIN = "promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue ON promotion.promotion_id=promotion_venue.promotion_id";
  private static final String TABLE_SERVICE_TYPE = "service_type";
  private static final String TABLE_SPECIAL_AREA_JOIN = "special_area_hotlink JOIN special_area ON special_area_hotlink.special_area_id=special_area.special_area_id JOIN hotlink ON special_area_hotlink.hotlink_id=hotlink.hotlink_id JOIN zone AS zonedest ON hotlink.zone_id = zonedest.zone_id JOIN zone AS zonesrc ON special_area.zone_id = zonesrc.zone_id";
  private static final String TABLE_VENUE = "venue JOIN venue_address ON venue.venue_id = venue_address.venue_id JOIN address ON venue_address.address_id = address.address_id";
  private static final String TABLE_VENUE_CATEGORY = "venue_place_category_type JOIN category_type ON venue_place_category_type.category_type_id = category_type.category_type_id";
  private static final String TABLE_VENUE_PLACE = "venue_place";
  private static final String TABLE_VENUE_PLACES_WITH_CATEGORY = "venue_place_category_type JOIN category_type ON venue_place_category_type.category_type_id = category_type.category_type_id JOIN venue_place ON venue_place.venue_place_id=venue_place_category_type.venue_place_id";
  private static final String TABLE_VENUE_PLACE_JOIN = "venue_place JOIN service_type ON venue_place.service_type_id=service_type.service_type_id JOIN operation_minutes ON venue_place.operation_times_id=operation_minutes.operation_minutes_id JOIN place_type ON venue_place.place_type_id=place_type.place_type_id";
  private static final String TABLE_VENUE_PLACE_PROMOTION_JOIN = "venue_place JOIN promotion_venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id";
  private static final String TABLE_WORMHOLE_JOIN = "wormhole JOIN wormhole_type ON wormhole.wormhole_type_id=wormhole_type.wormhole_type_id JOIN zone_wormhole ON wormhole.wormhole_id=zone_wormhole.wormhole_id JOIN zone ON zone_wormhole.zone_id=zone.zone_id";
  private static final String TABLE_ZONE = "zone JOIN image ON zone.image_id = image.image_id JOIN venue ON zone.venue_id = venue.venue_id";
  private static final UriMatcher URI_MATCHER = new UriMatcher(-1);
  private static final int VENUES = 1;
  private static final int VENUE_ID = 2;
  private static final int VENUE_PLACES_WITH_CATEGORIES = 41;
  private static final int VENUE_PLACE_CATEGORIES = 40;
  private static final int VENUE_PLACE_EVENTS = 38;
  private static final int VENUE_PLACE_EVENT_ID = 39;
  private static final int VENUE_PLACE_PROMOTIONS = 23;
  private static final int VENUE_PLACE_PROMOTION_ID = 24;
  private static final int VENUE_PROMOTIONS = 21;
  private static final int VENUE_PROMOTION_ID = 22;
  private static final int VENUE_UUID = 3;
  private static final int WORMHOLES = 8;
  private static final int WORMHOLE_ID = 9;
  private static final int ZONES = 10;
  private static final int ZONE_ID = 11;
  private static HashMap<String, String> sAddressProjectionMap;
  private static HashMap<String, String> sCategoryTypeProjectionMap;
  private static HashMap<String, String> sCategoryVenuePlaceProjectionMap;
  private static HashMap<String, String> sImageProjectionMap;
  private static HashMap<String, String> sItemProjectionMap;
  private static HashMap<String, String> sOperationMinutesProjectionMap;
  private static HashMap<String, String> sPixelCoordinateProjectionMap;
  private static HashMap<String, String> sPlaceAreaProjectionMap;
  private static HashMap<String, String> sPlaceProjectionMap;
  private static HashMap<String, String> sPlacePromotionProjectionMap;
  private static HashMap<String, String> sPlaceTypeProjectionMap;
  private static HashMap<String, String> sPlacesWithCategoryVenueProjectionMap;
  private static HashMap<String, String> sPolygonZoneProjectionMap;
  private static HashMap<String, String> sPromotionProjectionMap;
  private static HashMap<String, String> sPromotionVenuePlaceProjectionMap;
  private static HashMap<String, String> sServiceTypeProjectionMap;
  private static HashMap<String, String> sVenueProjectionMap;
  private static HashMap<String, String> sWormholeProjectionMap;
  private static HashMap<String, String> sZoneProjectionMap;
  private PISQLiteHelper mPISQLiteHelper;

  static
  {
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_venue", 1);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_venue/#", 2);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_venue/*", 3);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_raw", 18);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_item", 27);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_item/#", 28);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place", 6);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place/#", 7);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_zone", 10);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_zone/#", 11);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_service", 4);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_service/#", 5);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_category", 12);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_category/#", 13);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_category", 40);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_places_with_category", 41);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_polygon_zone", 14);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_polygon_zone/#", 15);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_pixel_coordinate", 16);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_pixel_coordinate/#", 17);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_promotion", 19);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_promotion/#", 20);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_wormhole", 8);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_wormhole/#", 9);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_venue_promotion", 21);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_venue_promotion/#", 22);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_promotion_place", 23);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_promotion_place/#", 24);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_promotion", 37);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_event", 25);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_event/#", 26);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_event_place", 38);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_event_place/#", 39);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_area", 35);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_area/#", 36);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_place_area", 35);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_image", 29);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_image/#", 30);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_address", 31);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_address/#", 32);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_operation_minutes", 33);
    URI_MATCHER.addURI("com.pointinside.android.api.venue", "map_operation_minutes/#", 34);
    sVenueProjectionMap = new HashMap();
    sVenueProjectionMap.put("venue_id", "venue.venue_id AS _id");
    sVenueProjectionMap.put("above_ground_zone_count", "above_ground_zone_count");
    sVenueProjectionMap.put("below_ground_zone_count", "below_ground_zone_count");
    sVenueProjectionMap.put("description", "description");
    sVenueProjectionMap.put("email", "email");
    sVenueProjectionMap.put("image_id", "image_id");
    sVenueProjectionMap.put("address_line1", "address_line1");
    sVenueProjectionMap.put("address_line2", "address_line2");
    sVenueProjectionMap.put("city", "city");
    sVenueProjectionMap.put("country", "country");
    sVenueProjectionMap.put("postal_code", "postal_code");
    sVenueProjectionMap.put("state_code", "state_code");
    sVenueProjectionMap.put("is_valid_shop_numbers", "is_valid_shop_numbers");
    sVenueProjectionMap.put("name", "name");
    sVenueProjectionMap.put("operation_times_id", "operation_times_id");
    sVenueProjectionMap.put("phone_number", "phone_number");
    sVenueProjectionMap.put("user_defined_id", "user_defined_id");
    sVenueProjectionMap.put("venue_classification", "venue_classification");
    sVenueProjectionMap.put("website", "website");
    sVenueProjectionMap.put("wormhole_count", "wormhole_count");
    sVenueProjectionMap.put("organization_id", "organization_id");
    sVenueProjectionMap.put("venue_type_id", "venue_type_id");
    sVenueProjectionMap.put("uuid", "uuid");
    sPlaceProjectionMap = new HashMap();
    sPlaceProjectionMap.put("venue_place_id", "venue_place.venue_place_id AS _id");
    sPlaceProjectionMap.put("name", "venue_place.name AS name");
    sPlaceProjectionMap.put("is_portaled", "is_portaled");
    sPlaceProjectionMap.put("is_displayable", "is_displayable");
    sPlaceProjectionMap.put("zone_id", "zone_id");
    sPlaceProjectionMap.put("zone_index", "zone_index");
    sPlaceProjectionMap.put("description", "venue_place.description AS description");
    sPlaceProjectionMap.put("location_pixel_x", "location_pixel_x");
    sPlaceProjectionMap.put("location_pixel_y", "location_pixel_y");
    sPlaceProjectionMap.put("phone_number", "phone_number");
    sPlaceProjectionMap.put("service_type_id", "service_type.service_type_id AS service_type_id");
    sPlaceProjectionMap.put("service_type_name", "service_type.name AS service_type_name");
    sPlaceProjectionMap.put("service_type_uuid", "service_type.uuid AS service_type_uuid");
    sPlaceProjectionMap.put("website", "website");
    sPlaceProjectionMap.put("operation_times_id", "operation_minutes.operation_minutes_id AS operation_times_id");
    sPlaceProjectionMap.put("monday_open_minute", "monday_open_minute");
    sPlaceProjectionMap.put("monday_close_minute", "monday_close_minute");
    sPlaceProjectionMap.put("tuesday_open_minute", "tuesday_open_minute");
    sPlaceProjectionMap.put("tuesday_close_minute", "tuesday_close_minute");
    sPlaceProjectionMap.put("wednesday_open_minute", "wednesday_open_minute");
    sPlaceProjectionMap.put("wednesday_close_minute", "wednesday_close_minute");
    sPlaceProjectionMap.put("thursday_open_minute", "thursday_open_minute");
    sPlaceProjectionMap.put("thursday_close_minute", "thursday_close_minute");
    sPlaceProjectionMap.put("friday_open_minute", "friday_open_minute");
    sPlaceProjectionMap.put("friday_close_minute", "friday_close_minute");
    sPlaceProjectionMap.put("saturday_open_minute", "saturday_open_minute");
    sPlaceProjectionMap.put("saturday_close_minute", "saturday_close_minute");
    sPlaceProjectionMap.put("sunday_open_minute", "sunday_open_minute");
    sPlaceProjectionMap.put("sunday_close_minute", "sunday_close_minute");
    sPlaceProjectionMap.put("image_id", "venue_place.image_id AS image_id");
    sPlaceProjectionMap.put("uuid", "venue_place.uuid AS uuid");
    sPlaceProjectionMap.put("ilc_code", "venue_place.ilc_code AS ilc_code");
    sPlaceProjectionMap.put("place_type_name", "place_type.name AS place_type_name");
    sPlaceProjectionMap.put("place_type_id", "venue_place.place_type_id AS place_type_id");
    sPlaceProjectionMap.put("venue_id", "venue_place.venue_id AS venue_id");
    sItemProjectionMap = new HashMap();
    sItemProjectionMap.put("venue_place_id", "venue_place_id AS _id");
    sItemProjectionMap.put("name", "venue_place.name AS name");
    sItemProjectionMap.put("description", "description");
    sItemProjectionMap.put("location_pixel_x", "location_pixel_x");
    sItemProjectionMap.put("location_pixel_y", "location_pixel_y");
    sItemProjectionMap.put("zone_id", "zone_id");
    sItemProjectionMap.put("zone_index", "zone_index");
    sItemProjectionMap.put("uuid", "uuid");
    sItemProjectionMap.put("venue_id", "venue_id");
    sItemProjectionMap.put("ilc_code", "ilc_code");
    sItemProjectionMap.put("is_portaled", "is_portaled");
    sPlacePromotionProjectionMap = new HashMap();
    sPlacePromotionProjectionMap.put("venue_place_id", "venue_place.venue_place_id AS _id");
    sPlacePromotionProjectionMap.put("name", "venue_place.name AS name");
    sPlacePromotionProjectionMap.put("description", "description");
    sPlacePromotionProjectionMap.put("location_pixel_x", "location_pixel_x");
    sPlacePromotionProjectionMap.put("location_pixel_y", "location_pixel_y");
    sPlacePromotionProjectionMap.put("zone_id", "zone_id");
    sPlacePromotionProjectionMap.put("zone_index", "zone_index");
    sPlacePromotionProjectionMap.put("uuid", "uuid");
    sPlacePromotionProjectionMap.put("venue_id", "venue_id");
    sPlacePromotionProjectionMap.put("ilc_code", "ilc_code");
    sPlacePromotionProjectionMap.put("is_portaled", "is_portaled");
    sZoneProjectionMap = new HashMap();
    sZoneProjectionMap.put("zone_id", "zone_id AS _id");
    sZoneProjectionMap.put("bearing_point2_point1", "bearing_point2_point1");
    sZoneProjectionMap.put("bearing_point4_point3", "bearing_point4_point3");
    sZoneProjectionMap.put("zone_display_order", "zone_display_order");
    sZoneProjectionMap.put("zone_index", "zone_index");
    sZoneProjectionMap.put("zone_name", "zone_name");
    sZoneProjectionMap.put("zone_tag_text", "zone_tag_text");
    sZoneProjectionMap.put("feet_per_pixel_x", "feet_per_pixel_x");
    sZoneProjectionMap.put("feet_per_pixel_y", "feet_per_pixel_y");
    sZoneProjectionMap.put("point1_latitude", "point1_latitude");
    sZoneProjectionMap.put("point1_longitude", "point1_longitude");
    sZoneProjectionMap.put("point1_pixel_x", "point1_pixel_x");
    sZoneProjectionMap.put("point1_pixel_y", "point1_pixel_y");
    sZoneProjectionMap.put("point2_latitude", "point2_latitude");
    sZoneProjectionMap.put("point2_longitude", "point2_longitude");
    sZoneProjectionMap.put("point2_pixel_x", "point2_pixel_x");
    sZoneProjectionMap.put("point2_pixel_y", "point2_pixel_y");
    sZoneProjectionMap.put("point3_latitude", "point3_latitude");
    sZoneProjectionMap.put("point3_longitude", "point3_longitude");
    sZoneProjectionMap.put("point3_pixel_x", "point3_pixel_x");
    sZoneProjectionMap.put("point3_pixel_y", "point3_pixel_y");
    sZoneProjectionMap.put("point4_latitude", "point4_latitude");
    sZoneProjectionMap.put("point4_longitude", "point4_longitude");
    sZoneProjectionMap.put("point4_pixel_x", "point4_pixel_x");
    sZoneProjectionMap.put("point4_pixel_y", "point4_pixel_y");
    sZoneProjectionMap.put("place_count", "place_count");
    sZoneProjectionMap.put("count_pixel_point1_point2_y", "count_pixel_point1_point2_y");
    sZoneProjectionMap.put("count_pixel_point3_point4_x", "count_pixel_point3_point4_x");
    sZoneProjectionMap.put("image_size_pixel_x", "image_size_pixel_x");
    sZoneProjectionMap.put("image_size_pixel_y", "image_size_pixel_y");
    sZoneProjectionMap.put("image_name", "image.name AS image_name");
    sZoneProjectionMap.put("uuid", "venue.uuid AS uuid");
    sZoneProjectionMap.put("zone_uuid", "zone.uuid AS zone_uuid");
    sZoneProjectionMap.put("is_above_ground_zone", "is_above_ground_zone");
    sZoneProjectionMap.put("is_overview_zone", "is_overview_zone");
    sZoneProjectionMap.put("venue_id", "zone.venue_id AS venue_id");
    sServiceTypeProjectionMap = new HashMap();
    sServiceTypeProjectionMap.put("service_type_id", "service_type_id AS _id");
    sServiceTypeProjectionMap.put("name", "name");
    sServiceTypeProjectionMap.put("description", "description");
    sPlaceTypeProjectionMap = new HashMap();
    sPlaceTypeProjectionMap.put("place_type_id", "place_type_id AS _id");
    sPlaceTypeProjectionMap.put("name", "name");
    sPlaceTypeProjectionMap.put("description", "description");
    sCategoryTypeProjectionMap = new HashMap();
    sCategoryTypeProjectionMap.put("category_type_id", "category_type_id AS _id");
    sCategoryTypeProjectionMap.put("name", "name");
    sCategoryTypeProjectionMap.put("description", "description");
    sPromotionProjectionMap = new HashMap();
    sPromotionProjectionMap.put("promotion_id", "promotion.promotion_id AS _id");
    sPromotionProjectionMap.put("active_sunday", "active_sunday");
    sPromotionProjectionMap.put("active_monday", "active_monday");
    sPromotionProjectionMap.put("active_tuesday", "active_tuesday");
    sPromotionProjectionMap.put("active_wednesday", "active_wednesday");
    sPromotionProjectionMap.put("active_thursday", "active_thursday");
    sPromotionProjectionMap.put("active_friday", "active_friday");
    sPromotionProjectionMap.put("active_saturday", "active_saturday");
    sPromotionProjectionMap.put("code_id", "code_id");
    sPromotionProjectionMap.put("display_start_datetime", "display_start_datetime");
    sPromotionProjectionMap.put("display_end_datetime", "display_end_datetime");
    sPromotionProjectionMap.put("start_datetime", "start_datetime");
    sPromotionProjectionMap.put("end_datetime", "end_datetime");
    sPromotionProjectionMap.put("description", "promotion.description AS description");
    sPromotionProjectionMap.put("frequency", "frequency");
    sPromotionProjectionMap.put("title", "title");
    sPromotionProjectionMap.put("image_id", "promotion.image_id AS image_id");
    sPromotionProjectionMap.put("promotion_type_id", "promotion.promotion_type_id AS promotion_type_id");
    sPromotionProjectionMap.put("contact_information", "contact_information");
    sPromotionProjectionMap.put("location", "location");
    sPromotionProjectionMap.put("other_information", "other_information");
    sPromotionVenuePlaceProjectionMap = new HashMap();
    sPromotionVenuePlaceProjectionMap.put("promotion_id", "promotion.promotion_id AS _id");
    sPromotionVenuePlaceProjectionMap.put("active_sunday", "active_sunday");
    sPromotionVenuePlaceProjectionMap.put("active_monday", "active_monday");
    sPromotionVenuePlaceProjectionMap.put("active_tuesday", "active_tuesday");
    sPromotionVenuePlaceProjectionMap.put("active_wednesday", "active_wednesday");
    sPromotionVenuePlaceProjectionMap.put("active_thursday", "active_thursday");
    sPromotionVenuePlaceProjectionMap.put("active_friday", "active_friday");
    sPromotionVenuePlaceProjectionMap.put("active_saturday", "active_saturday");
    sPromotionVenuePlaceProjectionMap.put("code_id", "code_id");
    sPromotionVenuePlaceProjectionMap.put("display_start_datetime", "display_start_datetime");
    sPromotionVenuePlaceProjectionMap.put("display_end_datetime", "display_end_datetime");
    sPromotionVenuePlaceProjectionMap.put("start_datetime", "start_datetime");
    sPromotionVenuePlaceProjectionMap.put("end_datetime", "end_datetime");
    sPromotionVenuePlaceProjectionMap.put("description", "promotion.description AS description");
    sPromotionVenuePlaceProjectionMap.put("frequency", "frequency");
    sPromotionVenuePlaceProjectionMap.put("title", "title");
    sPromotionVenuePlaceProjectionMap.put("image_id", "promotion.image_id AS image_id");
    sPromotionVenuePlaceProjectionMap.put("promotion_type_id", "promotion.promotion_type_id AS promotion_type_id");
    sPromotionVenuePlaceProjectionMap.put("venue_place_id", "promotion_venue_place.venue_place_id AS venue_place_id");
    sPromotionVenuePlaceProjectionMap.put("contact_information", "contact_information");
    sPromotionVenuePlaceProjectionMap.put("location", "location");
    sPromotionVenuePlaceProjectionMap.put("other_information", "other_information");
    sCategoryVenuePlaceProjectionMap = new HashMap();
    sCategoryVenuePlaceProjectionMap.put("venue_place_id", "venue_place_category_type.venue_place_id AS _id");
    sCategoryVenuePlaceProjectionMap.put("category_type_id", "venue_place_category_type.category_type_id");
    sCategoryVenuePlaceProjectionMap.put("name", "category_type.name");
    sCategoryVenuePlaceProjectionMap.put("description", "category_type.description");
    sPlacesWithCategoryVenueProjectionMap = new HashMap();
    sPlacesWithCategoryVenueProjectionMap.put("_id", "venue_place.venue_place_id AS _id");
    sPlacesWithCategoryVenueProjectionMap.put("category_type_id", "venue_place_category_type.category_type_id");
    sPlacesWithCategoryVenueProjectionMap.put("category_name", "category_type.name AS category_name");
    sPlacesWithCategoryVenueProjectionMap.put("place_name", "venue_place.name AS place_name");
    sPlacesWithCategoryVenueProjectionMap.put("place_uuid", "venue_place.uuid AS place_uuid");
    sPolygonZoneProjectionMap = new HashMap();
    sPolygonZoneProjectionMap.put("_id", "special_area_hotlink.hotlink_id AS _id");
    sPolygonZoneProjectionMap.put("special_area_id", "special_area.special_area_id AS special_area_id");
    sPolygonZoneProjectionMap.put("name", "special_area.name as name");
    sPolygonZoneProjectionMap.put("pixel_x", "pixel_x");
    sPolygonZoneProjectionMap.put("pixel_y", "pixel_y");
    sPolygonZoneProjectionMap.put("zone_id", "hotlink.zone_id AS zone_id");
    sPolygonZoneProjectionMap.put("zone_index", "zonedest.zone_index AS zone_index");
    sPolygonZoneProjectionMap.put("zone_display_order", "zonedest.zone_display_order AS zone_display_order");
    sPolygonZoneProjectionMap.put("place_count", "zonedest.place_count AS place_count");
    sPolygonZoneProjectionMap.put("zonesrc_index", "zonesrc.zone_index AS zonesrc_index");
    sPixelCoordinateProjectionMap = new HashMap();
    sPixelCoordinateProjectionMap.put("_id", "pixel_coordinate.pixel_coordinate_id AS _id");
    sPixelCoordinateProjectionMap.put("special_area_id", "special_area_pixel_coordinate.special_area_id AS special_area_id");
    sPixelCoordinateProjectionMap.put("altitude", "altitude");
    sPixelCoordinateProjectionMap.put("latitude", "latitude");
    sPixelCoordinateProjectionMap.put("longitude", "longitude");
    sPixelCoordinateProjectionMap.put("pixel_x", "x AS pixel_x");
    sPixelCoordinateProjectionMap.put("pixel_y", "y AS pixel_y");
    sImageProjectionMap = new HashMap();
    sImageProjectionMap.put("_id", "image_id AS _id");
    sImageProjectionMap.put("name", "name");
    sAddressProjectionMap = new HashMap();
    sAddressProjectionMap.put("address_id", "address_id AS _id");
    sAddressProjectionMap.put("address_line1", "address_line1");
    sAddressProjectionMap.put("address_line2", "address_line2");
    sAddressProjectionMap.put("city", "city");
    sAddressProjectionMap.put("country", "country");
    sAddressProjectionMap.put("postal_code", "postal_code");
    sAddressProjectionMap.put("state_code", "state_code");
    sOperationMinutesProjectionMap = new HashMap();
    sOperationMinutesProjectionMap.put("operation_minutes_id", "operation_minutes_id AS _id");
    sOperationMinutesProjectionMap.put("monday_open_minute", "monday_open_minute");
    sOperationMinutesProjectionMap.put("monday_close_minute", "monday_close_minute");
    sOperationMinutesProjectionMap.put("tuesday_open_minute", "tuesday_open_minute");
    sOperationMinutesProjectionMap.put("tuesday_close_minute", "tuesday_close_minute");
    sOperationMinutesProjectionMap.put("wednesday_open_minute", "wednesday_open_minute");
    sOperationMinutesProjectionMap.put("wednesday_close_minute", "wednesday_close_minute");
    sOperationMinutesProjectionMap.put("thursday_open_minute", "thursday_open_minute");
    sOperationMinutesProjectionMap.put("thursday_close_minute", "thursday_close_minute");
    sOperationMinutesProjectionMap.put("friday_open_minute", "friday_open_minute");
    sOperationMinutesProjectionMap.put("friday_close_minute", "friday_close_minute");
    sOperationMinutesProjectionMap.put("saturday_open_minute", "saturday_open_minute");
    sOperationMinutesProjectionMap.put("saturday_close_minute", "saturday_close_minute");
    sOperationMinutesProjectionMap.put("sunday_open_minute", "sunday_open_minute");
    sOperationMinutesProjectionMap.put("sunday_close_minute", "sunday_close_minute");
    sPlaceAreaProjectionMap = new HashMap();
    sPlaceAreaProjectionMap.put("special_area_id", "special_area.special_area_id AS _id");
    sPlaceAreaProjectionMap.put("venue_place_id", "special_area_venue_place.venue_place_id AS venue_place_id");
    sPlaceAreaProjectionMap.put("name", "name");
    sWormholeProjectionMap = new HashMap();
    sWormholeProjectionMap.put("wormhole_id", "wormhole.wormhole_id AS _id");
    sWormholeProjectionMap.put("name", "wormhole_type.name AS name");
    sWormholeProjectionMap.put("description", "description");
    sWormholeProjectionMap.put("location_pixel_x", "location_pixel_x");
    sWormholeProjectionMap.put("location_pixel_y", "location_pixel_y");
    sWormholeProjectionMap.put("zone_id", "zone.zone_id AS zone_id");
    sWormholeProjectionMap.put("is_handicap_accessible", "is_handicap_accessible");
    sWormholeProjectionMap.put("wormhole_type_id", "wormhole.wormhole_type_id AS wormhole_type_id");
    sWormholeProjectionMap.put("zone_index", "zone_index");
  }

  public PIVenueDataset(PISQLiteHelper paramPISQLiteHelper)
  {
    this.mPISQLiteHelper = paramPISQLiteHelper;
  }

  public void close()
  {
    if (this.mPISQLiteHelper != null) {
      this.mPISQLiteHelper.close();
    }
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    return query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, null);
  }

  public Cursor query(Uri uri, String as[], String s, String as1[], String s1, String s2)
  {
      SQLiteQueryBuilder sqlitequerybuilder = new SQLiteQueryBuilder();
      switch(URI_MATCHER.match(uri)) {
//      JVM INSTR tableswitch 1 41: default 196
  //                   1 221
  //                   2 258
  //                   3 301
  //                   4 533
  //                   5 559
  //                   6 359
  //                   7 482
  //                   8 671
  //                   9 689
  //                   10 610
  //                   11 628
  //                   12 741
  //                   13 759
  //                   14 1167
  //                   15 1185
  //                   16 1228
  //                   17 1246
  //                   18 385
  //                   19 838
  //                   20 856
  //                   21 899
  //                   22 917
  //                   23 960
  //                   24 986
  //                   25 1029
  //                   26 1055
  //                   27 403
  //                   28 421
  //                   29 1289
  //                   30 1307
  //                   31 1350
  //                   32 1368
  //                   33 1411
  //                   34 1429
  //                   35 1472
  //                   36 1490
  //                   37 464
  //                   38 1098
  //                   39 1124
  //                   40 802
  //                   41 820;
  //       goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16 _L17 _L18 _L19 _L20 _L21 _L22 _L23 _L24 _L25 _L26 _L27 _L28 _L29 _L30 _L31 _L32 _L33 _L34 _L35 _L36 _L37 _L38 _L39 _L40 _L41 _L42
default:
      throw new IllegalArgumentException((new StringBuilder("Unknown URI ")).append(uri).toString());
case 1:
      sqlitequerybuilder.setTables("venue JOIN venue_address ON venue.venue_id = venue_address.venue_id JOIN address ON venue_address.address_id = address.address_id");
      sqlitequerybuilder.setProjectionMap(sVenueProjectionMap);
      break;
case 2:
      sqlitequerybuilder.setTables("venue JOIN venue_address ON venue.venue_id = venue_address.venue_id JOIN address ON venue_address.address_id = address.address_id");
      sqlitequerybuilder.setProjectionMap(sVenueProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 3:
      sqlitequerybuilder.setTables("venue JOIN venue_address ON venue.venue_id = venue_address.venue_id JOIN address ON venue_address.address_id = address.address_id");
      sqlitequerybuilder.setProjectionMap(sVenueProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("uuid = \"")).append((String)uri.getPathSegments().get(1)).append("\"").toString());
      break; /* Loop/switch isn't completed */
case 6:
      sqlitequerybuilder.setTables("venue_place JOIN service_type ON venue_place.service_type_id=service_type.service_type_id JOIN operation_minutes ON venue_place.operation_times_id=operation_minutes.operation_minutes_id JOIN place_type ON venue_place.place_type_id=place_type.place_type_id");
      sqlitequerybuilder.setProjectionMap(sPlaceProjectionMap);
      sqlitequerybuilder.appendWhere("place_type.name = \"Place\"");
      break; /* Loop/switch isn't completed */
case 18:
      sqlitequerybuilder.setTables("venue_place JOIN service_type ON venue_place.service_type_id=service_type.service_type_id JOIN operation_minutes ON venue_place.operation_times_id=operation_minutes.operation_minutes_id JOIN place_type ON venue_place.place_type_id=place_type.place_type_id");
      sqlitequerybuilder.setProjectionMap(sPlaceProjectionMap);
      break; /* Loop/switch isn't completed */
case 27:
      sqlitequerybuilder.setTables("venue_place");
      sqlitequerybuilder.setProjectionMap(sItemProjectionMap);
      break; /* Loop/switch isn't completed */
case 28:
      sqlitequerybuilder.setTables("venue_place");
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      sqlitequerybuilder.setProjectionMap(sItemProjectionMap);
      break; /* Loop/switch isn't completed */
case 37:
      sqlitequerybuilder.setTables("venue_place JOIN promotion_venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id");
      sqlitequerybuilder.setProjectionMap(sPlacePromotionProjectionMap);
      break; /* Loop/switch isn't completed */
case 7:
      sqlitequerybuilder.setTables("venue_place JOIN service_type ON venue_place.service_type_id=service_type.service_type_id JOIN operation_minutes ON venue_place.operation_times_id=operation_minutes.operation_minutes_id JOIN place_type ON venue_place.place_type_id=place_type.place_type_id");
      sqlitequerybuilder.setProjectionMap(sPlaceProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      sqlitequerybuilder.appendWhere(" AND place_type.name = \"Place\"");
      break; /* Loop/switch isn't completed */
case 4:
      sqlitequerybuilder.setTables("venue_place JOIN service_type ON venue_place.service_type_id=service_type.service_type_id JOIN operation_minutes ON venue_place.operation_times_id=operation_minutes.operation_minutes_id JOIN place_type ON venue_place.place_type_id=place_type.place_type_id");
      sqlitequerybuilder.setProjectionMap(sPlaceProjectionMap);
      sqlitequerybuilder.appendWhere("place_type.name = \"Service\"");
      break; /* Loop/switch isn't completed */
case 5:
      sqlitequerybuilder.setTables("venue_place JOIN service_type ON venue_place.service_type_id=service_type.service_type_id JOIN operation_minutes ON venue_place.operation_times_id=operation_minutes.operation_minutes_id JOIN place_type ON venue_place.place_type_id=place_type.place_type_id");
      sqlitequerybuilder.setProjectionMap(sPlaceProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      sqlitequerybuilder.appendWhere(" AND place_type.name = \"Service\"");
      break; /* Loop/switch isn't completed */
case 10:
      sqlitequerybuilder.setTables("zone JOIN image ON zone.image_id = image.image_id JOIN venue ON zone.venue_id = venue.venue_id");
      sqlitequerybuilder.setProjectionMap(sZoneProjectionMap);
      break; /* Loop/switch isn't completed */
case 11:
      sqlitequerybuilder.setTables("zone JOIN image ON zone.image_id = image.image_id JOIN venue ON zone.venue_id = venue.venue_id");
      sqlitequerybuilder.setProjectionMap(sZoneProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder(" _id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 8:
      sqlitequerybuilder.setTables("wormhole JOIN wormhole_type ON wormhole.wormhole_type_id=wormhole_type.wormhole_type_id JOIN zone_wormhole ON wormhole.wormhole_id=zone_wormhole.wormhole_id JOIN zone ON zone_wormhole.zone_id=zone.zone_id");
      sqlitequerybuilder.setProjectionMap(sWormholeProjectionMap);
      break; /* Loop/switch isn't completed */
case 9:
      sqlitequerybuilder.setTables("wormhole JOIN wormhole_type ON wormhole.wormhole_type_id=wormhole_type.wormhole_type_id JOIN zone_wormhole ON wormhole.wormhole_id=zone_wormhole.wormhole_id JOIN zone ON zone_wormhole.zone_id=zone.zone_id");
      sqlitequerybuilder.setProjectionMap(sWormholeProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append((String)uri.getPathSegments().get(1)).toString());
      break; /* Loop/switch isn't completed */
case 12:
      sqlitequerybuilder.setTables("category_type");
      sqlitequerybuilder.setProjectionMap(sCategoryTypeProjectionMap);
      break; /* Loop/switch isn't completed */
case 13:
      sqlitequerybuilder.setTables("category_type");
      sqlitequerybuilder.setProjectionMap(sCategoryTypeProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 40:
      sqlitequerybuilder.setTables("venue_place_category_type JOIN category_type ON venue_place_category_type.category_type_id = category_type.category_type_id");
      sqlitequerybuilder.setProjectionMap(sCategoryVenuePlaceProjectionMap);
      break; /* Loop/switch isn't completed */
case 41:
      sqlitequerybuilder.setTables("venue_place_category_type JOIN category_type ON venue_place_category_type.category_type_id = category_type.category_type_id JOIN venue_place ON venue_place.venue_place_id=venue_place_category_type.venue_place_id");
      sqlitequerybuilder.setProjectionMap(sPlacesWithCategoryVenueProjectionMap);
      break; /* Loop/switch isn't completed */
case 19:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id");
      sqlitequerybuilder.setProjectionMap(sPromotionProjectionMap);
      break; /* Loop/switch isn't completed */
case 20:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id");
      sqlitequerybuilder.setProjectionMap(sPromotionProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 21:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue ON promotion.promotion_id=promotion_venue.promotion_id");
      sqlitequerybuilder.setProjectionMap(sPromotionProjectionMap);
      break; /* Loop/switch isn't completed */
case 22:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue ON promotion.promotion_id=promotion_venue.promotion_id");
      sqlitequerybuilder.setProjectionMap(sPromotionProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 23:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue_place ON promotion.promotion_id=promotion_venue_place.promotion_id JOIN venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id");
      sqlitequerybuilder.setProjectionMap(sPromotionVenuePlaceProjectionMap);
      sqlitequerybuilder.appendWhere("promotion_type.promotion_type_id = 2");
      break; /* Loop/switch isn't completed */
case 24:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue_place ON promotion.promotion_id=promotion_venue_place.promotion_id JOIN venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id");
      sqlitequerybuilder.setProjectionMap(sPromotionVenuePlaceProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 25:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id");
      sqlitequerybuilder.setProjectionMap(sPromotionProjectionMap);
      sqlitequerybuilder.appendWhere("promotion_type.promotion_type_id = 1");
      break; /* Loop/switch isn't completed */
case 26:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id");
      sqlitequerybuilder.setProjectionMap(sPromotionProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 38:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue_place ON promotion.promotion_id=promotion_venue_place.promotion_id JOIN venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id");
      sqlitequerybuilder.setProjectionMap(sPromotionVenuePlaceProjectionMap);
      sqlitequerybuilder.appendWhere("promotion_type.promotion_type_id = 1");
      break; /* Loop/switch isn't completed */
case 39:
      sqlitequerybuilder.setTables("promotion JOIN promotion_type ON promotion.promotion_type_id=promotion_type.promotion_type_id JOIN promotion_venue_place ON promotion.promotion_id=promotion_venue_place.promotion_id JOIN venue_place ON promotion_venue_place.venue_place_id = venue_place.venue_place_id");
      sqlitequerybuilder.setProjectionMap(sPromotionVenuePlaceProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 14:
      sqlitequerybuilder.setTables("special_area_hotlink JOIN special_area ON special_area_hotlink.special_area_id=special_area.special_area_id JOIN hotlink ON special_area_hotlink.hotlink_id=hotlink.hotlink_id JOIN zone AS zonedest ON hotlink.zone_id = zonedest.zone_id JOIN zone AS zonesrc ON special_area.zone_id = zonesrc.zone_id");
      sqlitequerybuilder.setProjectionMap(sPolygonZoneProjectionMap);
      break; /* Loop/switch isn't completed */
case 15:
      sqlitequerybuilder.setTables("special_area_hotlink JOIN special_area ON special_area_hotlink.special_area_id=special_area.special_area_id JOIN hotlink ON special_area_hotlink.hotlink_id=hotlink.hotlink_id JOIN zone AS zonedest ON hotlink.zone_id = zonedest.zone_id JOIN zone AS zonesrc ON special_area.zone_id = zonesrc.zone_id");
      sqlitequerybuilder.setProjectionMap(sPolygonZoneProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 16:
      sqlitequerybuilder.setTables("pixel_coordinate JOIN special_area_pixel_coordinate ON special_area_pixel_coordinate.pixel_coordinate_id=pixel_coordinate.pixel_coordinate_id");
      sqlitequerybuilder.setProjectionMap(sPixelCoordinateProjectionMap);
      break; /* Loop/switch isn't completed */
case 17:
      sqlitequerybuilder.setTables("pixel_coordinate JOIN special_area_pixel_coordinate ON special_area_pixel_coordinate.pixel_coordinate_id=pixel_coordinate.pixel_coordinate_id");
      sqlitequerybuilder.setProjectionMap(sPixelCoordinateProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 29:
      sqlitequerybuilder.setTables("image");
      sqlitequerybuilder.setProjectionMap(sImageProjectionMap);
      break; /* Loop/switch isn't completed */
case 30:
      sqlitequerybuilder.setTables("image");
      sqlitequerybuilder.setProjectionMap(sImageProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 31:
      sqlitequerybuilder.setTables("address");
      sqlitequerybuilder.setProjectionMap(sAddressProjectionMap);
      break; /* Loop/switch isn't completed */
case 32:
      sqlitequerybuilder.setTables("address");
      sqlitequerybuilder.setProjectionMap(sAddressProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 33:
      sqlitequerybuilder.setTables("operation_minutes");
      sqlitequerybuilder.setProjectionMap(sOperationMinutesProjectionMap);
      break; /* Loop/switch isn't completed */
case 34:
      sqlitequerybuilder.setTables("operation_minutes");
      sqlitequerybuilder.setProjectionMap(sOperationMinutesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break; /* Loop/switch isn't completed */
case 35:
      sqlitequerybuilder.setTables("special_area JOIN special_area_venue_place ON special_area.special_area_id = special_area_venue_place.special_area_id");
      sqlitequerybuilder.setProjectionMap(sPlaceAreaProjectionMap);
      break; /* Loop/switch isn't completed */
case 36:
      sqlitequerybuilder.setTables("special_area JOIN special_area_venue_place ON special_area.special_area_id = special_area_venue_place.special_area_id");
      sqlitequerybuilder.setProjectionMap(sPlaceAreaProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append(ContentUris.parseId(uri)).toString());
      break;
      }

      return sqlitequerybuilder.query(mPISQLiteHelper.getReadableDatabase(), as, s, as1, s2, null, s1);
      
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIVenueDataset
 * JD-Core Version:    0.7.0.1
 */