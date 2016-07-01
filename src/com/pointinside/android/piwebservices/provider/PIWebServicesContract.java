package com.pointinside.android.piwebservices.provider;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.BaseColumns;

public class PIWebServicesContract
{
  private static DynamicAuthorityHelper sAuthority;

  public static final String getAuthority()
  {
      String str = sAuthority.getAuthority();
      return str;
  }

  private static final Uri getBaseUri()
  {
      Uri localUri = sAuthority.getBaseUri();
      return localUri;
  }

  public static void init(Context paramContext)
  {
      sAuthority = new DynamicAuthorityHelper(paramContext, PIWebServicesProvider.class, ".provider.PIWebServicesProvider");
      return;
  }

  public static abstract interface CommonDealsRequestsColumns
    extends BaseColumns
  {
    public static final String TIMESTAMP = "timestamp";
  }

  public static class DealsResults
    implements PIWebServicesContract.DealsResultsColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.pointinside.cursor.item/dealResults";
    public static final String CONTENT_TYPE = "vnd.pointinside.cursor.dir/dealResults";
    //public static final Uri CONTENT_URI = PIWebServicesContract.access$0().buildUpon().appendPath("deals").appendPath("results").build();
    public static final Uri CONTENT_URI = PIWebServicesContract.getBaseUri().buildUpon().appendPath("deals").appendPath("results").build();

    public static final String REQUEST_TYPE_DESTINATION = "destination";
    public static final String REQUEST_TYPE_NEARBY = "nearby";

    public static Uri makeResultUri(long paramLong)
    {
      return ContentUris.withAppendedId(CONTENT_URI, paramLong);
    }

    public static Uri makeResultsUriByRequest(long paramLong, String paramString)
    {
      return CONTENT_URI.buildUpon().appendPath("byRequest").appendPath(paramString).appendPath(String.valueOf(paramLong)).build();
    }

    public static Uri makeResultsUriByRequest(Uri paramUri, String paramString)
    {
      return CONTENT_URI.buildUpon().appendPath("byRequest").appendPath(paramString).appendPath(String.valueOf(ContentUris.parseId(paramUri))).build();
    }

    public static Uri makeResultsUriByRequestAndLocation(long paramLong, String paramString)
    {
      return CONTENT_URI.buildUpon().appendPath("byRequestAndLocation").appendPath(paramString).appendPath(String.valueOf(paramLong)).build();
    }

    public static Uri makeResultsUriByRequestAndVenue(long paramLong, String paramString)
    {
      return CONTENT_URI.buildUpon().appendPath("byRequestAndVenue").appendPath(paramString).appendPath(String.valueOf(paramLong)).build();
    }

    public static Uri makeResultsUriByVenueUUID(Uri paramUri)
    {
      return CONTENT_URI.buildUpon().appendPath("groupByVenueUUID").appendPath(String.valueOf(ContentUris.parseId(paramUri))).build();
    }

    public static Uri makeResultsUriGroupByLocation(long paramLong)
    {
      return CONTENT_URI.buildUpon().appendPath("groupByLocation").appendPath(String.valueOf(paramLong)).build();
    }

    public static Uri makeResultsUriGroupByLocation(Uri paramUri)
    {
      return CONTENT_URI.buildUpon().appendPath("groupByLocation").appendPath(String.valueOf(ContentUris.parseId(paramUri))).build();
    }
  }

  public static abstract interface DealsResultsColumns
    extends BaseColumns
  {
    public static final String BRAND = "brand";
    public static final String CATEGORY = "category";
    public static final String DATASOURCE = "datasource";
    public static final String DATASOURCE_ID = "datasource_id";
    public static final String DEAL_CATEGORY_AUTO = "Auto";
    public static final String DEAL_CATEGORY_DINING = "Dining";
    public static final String DEAL_CATEGORY_FASHION = "Fashion";
    public static final String DEAL_CATEGORY_GENERAL = "General";
    public static final String DEAL_CATEGORY_HOME = "Home";
    public static final String DEAL_CATEGORY_RETAIL = "Retail";
    public static final String DEAL_CATEGORY_SHOPS = "Shops";
    public static final String DEAL_COUNT = "deal_count";
    public static final String DESCRIPTION = "description";
    public static final String DISPLAY_END_DATE = "display_end_date";
    public static final String DISPLAY_IMAGE = "display_image";
    public static final String DISPLAY_START_DATE = "display_start_date";
    public static final String DISTANCE = "distance";
    public static final String END_DATE = "end_date";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String PLACE_NAME = "organization";
    public static final String PLACE_UUID = "place_uuid";
    public static final String REQUEST_ID = "request_id";
    public static final String REQUEST_TYPE = "request_type";
    public static final String START_DATE = "start_date";
    public static final String THUMBNAIL_IMAGE = "thumbnail_image";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String UPC = "upc";
    public static final String VENUE_UUID = "venue_uuid";
  }

  public static class DestinationDealsRequests
    implements PIWebServicesContract.DestinationDealsRequestsColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.pointinside.cursor.item/destinationDealsRequests";
    public static final String CONTENT_TYPE = "vnd.pointinside.cursor.dir/destinationDealsRequests";
//    public static final Uri CONTENT_URI = PIWebServicesContract.access$0().buildUpon().appendPath("destinationDeals").appendPath("requests").build();
    public static final Uri CONTENT_URI = PIWebServicesContract.getBaseUri().buildUpon().appendPath("destinationDeals").appendPath("requests").build();


    public static Uri makeRequestUri(long paramLong)
    {
      return ContentUris.withAppendedId(CONTENT_URI, paramLong);
    }
  }

  public static abstract interface DestinationDealsRequestsColumns
    extends PIWebServicesContract.CommonDealsRequestsColumns
  {
    public static final String VENUE = "venue";
  }

  public static class NearbyDealsRequests
    implements PIWebServicesContract.NearbyDealsRequestsColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.pointinside.cursor.item/nearbyDealRequests";
    public static final String CONTENT_TYPE = "vnd.pointinside.cursor.dir/nearbyDealRequests";
//    public static final Uri CONTENT_URI = PIWebServicesContract.access$0().buildUpon().appendPath("nearbyDeals").appendPath("requests").build();
    public static final Uri CONTENT_URI = PIWebServicesContract.getBaseUri().buildUpon().appendPath("nearbyDeals").appendPath("requests").build();
  }

  public static abstract interface NearbyDealsRequestsColumns
    extends PIWebServicesContract.CommonDealsRequestsColumns
  {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String RADIUS = "radius";
  }

  public static class PlacesRequests
    implements PIWebServicesContract.PlacesRequestsColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.pointinside.cursor.item/placeRequests";
    public static final String CONTENT_TYPE = "vnd.pointinside.cursor.dir/placeRequests";
//    public static final Uri CONTENT_URI = PIWebServicesContract.access$0().buildUpon().appendPath("places").appendPath("requests").build();
    public static final Uri CONTENT_URI = PIWebServicesContract.getBaseUri().buildUpon().appendPath("places").appendPath("requests").build();

  }

  public static abstract interface PlacesRequestsColumns
    extends BaseColumns
  {
    public static final String CATEGORY_ID = "category_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String QUERY = "query";
    public static final String RADIUS = "radius";
    public static final String TIMESTAMP = "timestamp";
  }

  public static class PlacesResults
    implements PIWebServicesContract.PlacesResultsColumns
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.pointinside.cursor.item/placeResults";
    public static final String CONTENT_TYPE = "vnd.pointinside.cursor.dir/placeResults";
//    public static final Uri CONTENT_URI = PIWebServicesContract.access$0().buildUpon().appendPath("places").appendPath("results").build();
    public static final Uri CONTENT_URI = PIWebServicesContract.getBaseUri().buildUpon().appendPath("places").appendPath("results").build();


    public static Uri makeResultsUriById(long paramLong)
    {
      return CONTENT_URI.buildUpon().appendPath(String.valueOf(String.valueOf(paramLong))).build();
    }

    public static Uri makeResultsUriByRequest(long paramLong)
    {
      return CONTENT_URI.buildUpon().appendPath("byRequest").appendPath(String.valueOf(paramLong)).build();
    }

    public static Uri makeResultsUriByRequest(Uri paramUri)
    {
      return makeResultsUriByRequest(ContentUris.parseId(paramUri));
    }
  }

  public static abstract interface PlacesResultsColumns
    extends BaseColumns
  {
    public static final String CITY = "city";
    public static final String DESCRIPTION = "description";
    public static final String HOURS_CLOSE_FRIDAY = "hours_close_friday";
    public static final String HOURS_CLOSE_MONDAY = "hours_close_monday";
    public static final String HOURS_CLOSE_SATURDAY = "hours_close_saturday";
    public static final String HOURS_CLOSE_SUNDAY = "hours_close_sunday";
    public static final String HOURS_CLOSE_THURSDAY = "hours_close_thursday";
    public static final String HOURS_CLOSE_TUESDAY = "hours_close_tuesday";
    public static final String HOURS_CLOSE_WEDNESDAY = "hours_close_wednesday";
    public static final String HOURS_OPEN_FRIDAY = "hours_open_friday";
    public static final String HOURS_OPEN_MONDAY = "hours_open_monday";
    public static final String HOURS_OPEN_SATURDAY = "hours_open_saturday";
    public static final String HOURS_OPEN_SUNDAY = "hours_open_sunday";
    public static final String HOURS_OPEN_THURSDAY = "hours_open_thursday";
    public static final String HOURS_OPEN_TUESDAY = "hours_open_tuesday";
    public static final String HOURS_OPEN_WEDNESDAY = "hours_open_wednesday";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String PHONE = "phone";
    public static final String PLACE_UUID = "place_uuid";
    public static final String RAW_HOURS = "raw_hours";
    public static final String REQUEST_ID = "request_id";
    public static final String STATE = "state";
    public static final String STREET = "STREET";
    public static final String TITLE = "title";
    public static final String URBANQ_ID = "urbanq_id";
    public static final String VENUE_PROXIMITY = "venue_proximity";
    public static final String VENUE_UUID = "venue_uuid";
    public static final String WEB_LABEL = "web_label";
    public static final String WEB_URL = "web_url";
    public static final String ZIP = "zip";
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.provider.PIWebServicesContract
 * JD-Core Version:    0.7.0.1
 */