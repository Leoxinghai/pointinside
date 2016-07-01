package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.net.Uri.Builder;
import com.pointinside.android.api.PIMapReference;

import java.util.Arrays;

public class PIReference
{
  static final String AUTHORITY = "com.pointinside.android.api.reference";
  static final String CONTENT_AUTHORITY_SLASH = "content://com.pointinside.android.api.reference/";
  public static final Uri GEO_CITY_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.reference/geo_city");
  public static final Uri GEO_COUNTRY_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.reference/geo_country");
  public static final Uri GEO_COUNTRY_SUBDIVISION_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.reference/geo_country_subdivision");
  public static final Uri VENUE_SUMMARY_CONTENT_URI = Uri.parse("content://com.pointinside.android.api.reference/venue_summary");

  public PIReference(PIMapReference.PIReferenceAccess paramPIReferenceAccess) {}

  public static Uri getGeoCityUri(long paramLong)
  {
    return GEO_CITY_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getGeoCountrySubdivisionUri(long paramLong)
  {
    return GEO_COUNTRY_SUBDIVISION_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getGeoCountryUri(long paramLong)
  {
    return GEO_COUNTRY_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  private String getVenueSummarySortOrder(Location paramLocation)
  {
    if (paramLocation == null) {
      return "venue_name ASC";
    }
    String str1 = "(" + paramLocation.getLatitude() + "-" + "latitude" + ")";
    String str2 = "(" + paramLocation.getLongitude() + "-" + "longitude" + ")";
    return str1 + "*" + str1 + "+" + str2 + "*" + str2;
  }

  public static Uri getVenueSummaryUri(long paramLong)
  {
    return VENUE_SUMMARY_CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getVenueSummaryUri(String paramString)
  {
    return VENUE_SUMMARY_CONTENT_URI.buildUpon().appendPath(paramString).build();
  }

  public PIMapGeoCityDataCursor getGeoCities(PIReferenceDataset paramPIReferenceDataset)
  {
    return PIMapGeoCityDataCursor.getInstance(paramPIReferenceDataset.query(GEO_CITY_CONTENT_URI, null, null, null, "name ASC"));
  }

  public PIMapGeoCityDataCursor getGeoCitiesForSubdivision(PIReferenceDataset paramPIReferenceDataset, long paramLong)
  {
    Uri localUri = GEO_CITY_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    return PIMapGeoCityDataCursor.getInstance(paramPIReferenceDataset.query(localUri, null, "subdivision_id=?", arrayOfString, "name ASC"));
  }

  public PIMapGeoCountryDataCursor getGeoCountries(PIReferenceDataset paramPIReferenceDataset)
  {
    return PIMapGeoCountryDataCursor.getInstance(paramPIReferenceDataset.query(GEO_COUNTRY_CONTENT_URI, null, null, null, "country_code ASC"));
  }

  public PIMapGeoCountrySubdivisionDataCursor getGeoCountrySubdivisions(PIReferenceDataset paramPIReferenceDataset)
  {
    return PIMapGeoCountrySubdivisionDataCursor.getInstance(paramPIReferenceDataset.query(GEO_COUNTRY_SUBDIVISION_CONTENT_URI, null, null, null, "subdivision_code ASC"));
  }

  public PIMapGeoCountrySubdivisionDataCursor getGeoCountrySubdivisionsForCountry(PIReferenceDataset paramPIReferenceDataset, long paramLong)
  {
    Uri localUri = GEO_COUNTRY_SUBDIVISION_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    return PIMapGeoCountrySubdivisionDataCursor.getInstance(paramPIReferenceDataset.query(localUri, null, "country_id=?", arrayOfString, "subdivision_code ASC"));
  }

  public PIMapVenueSummaryDataCursor getVenueSummaries(PIReferenceDataset paramPIReferenceDataset, Location paramLocation)
  {
    return PIMapVenueSummaryDataCursor.getInstance(paramPIReferenceDataset.query(VENUE_SUMMARY_CONTENT_URI, null, null, null, getVenueSummarySortOrder(paramLocation)));
  }

  public PIMapVenueSummaryDataCursor getVenueSummariesForCity(PIReferenceDataset paramPIReferenceDataset, long paramLong)
  {
    Uri localUri = VENUE_SUMMARY_CONTENT_URI;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramLong);
    return PIMapVenueSummaryDataCursor.getInstance(paramPIReferenceDataset.query(localUri, null, "city_id=?", arrayOfString, "venue_name ASC"));
  }

  public PIMapVenueSummaryDataCursor getVenueSummary(PIReferenceDataset paramPIReferenceDataset, long paramLong)
  {
    return PIMapVenueSummaryDataCursor.getInstance(paramPIReferenceDataset.query(getVenueSummaryUri(paramLong), null, null, null, "venue_name ASC"));
  }

  public PIMapVenueSummaryDataCursor getVenueSummary(PIReferenceDataset paramPIReferenceDataset, String paramString)
  {
    return PIMapVenueSummaryDataCursor.getInstance(paramPIReferenceDataset.query(getVenueSummaryUri(paramString), null, null, null, "venue_name ASC"));
  }

  public PIMapVenueSummaryDataCursor getVenueSummarySearchForName(PIReferenceDataset paramPIReferenceDataset, String paramString)
  {
    return PIMapVenueSummaryDataCursor.getInstance(paramPIReferenceDataset.query(VENUE_SUMMARY_CONTENT_URI, null, String.format("venue_name LIKE '%%%s%%'", new Object[] { paramString }), null, "venue_name ASC"));
  }

  public PIMapVenueSummaryDataCursor getVenueSummarySearchForText(PIReferenceDataset paramPIReferenceDataset, String paramString, Location paramLocation)
  {
    String str = '%' + paramString + '%';
    String[] arrayOfString = new String[5];
    Arrays.fill(arrayOfString, str);
    return PIMapVenueSummaryDataCursor.getInstance(paramPIReferenceDataset.query(VENUE_SUMMARY_CONTENT_URI, null, "venue_name LIKE ? OR description LIKE ? OR city LIKE ? OR state_code LIKE ? OR country LIKE ?", arrayOfString, getVenueSummarySortOrder(paramLocation)));
  }

  public Cursor getVenuesForSearch(PIReferenceDataset paramPIReferenceDataset, String paramString)
  {
    return paramPIReferenceDataset.query(VENUE_SUMMARY_CONTENT_URI, null, "venue_name LIKE '%" + paramString + "%' OR city LIKE '%" + paramString + "%' OR description LIKE '%" + paramString + "%'", null, "venue_name ASC");
  }

  static abstract interface GeoCityColumns
  {
    public static final String CITY_ID = "city_id";
    public static final String COUNTRY_ID = "country_id";
    public static final String CREATION_DATE = "creation_date";
    public static final String MODIFICATION_DATE = "modification_date";
    public static final String NAME = "name";
    public static final String SUBDIVISION_ID = "subdivision_id";
  }

  static abstract interface GeoCountryColumns
  {
    public static final String COUNTRY_CODE = "country_code";
    public static final String COUNTRY_ID = "country_id";
    public static final String CREATION_DATE = "creation_date";
    public static final String MODIFICATION_DATE = "modification_date";
    public static final String NAME = "name";
  }

  static abstract interface GeoCountrySubdivisionColumns
  {
    public static final String COUNTRY_ID = "country_id";
    public static final String CREATION_DATE = "creation_date";
    public static final String MODIFICATION_DATE = "modification_date";
    public static final String NAME = "name";
    public static final String SUBDIVISION_CODE = "subdivision_code";
    public static final String SUBDIVISION_ID = "subdivision_id";
  }

  static abstract interface VenueSummaryColumns
  {
    public static final String ADDRESS_LINE1 = "address_line1";
    public static final String ADDRESS_LINE2 = "address_line2";
    public static final String CITY = "city";
    public static final String CITY_ID = "city_id";
    public static final String COUNTRY = "country";
    public static final String DESCRIPTION = "description";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String POSTAL_CODE = "postal_code";
    public static final String STATE_CODE = "state_code";
    public static final String VENUE_DATASET_DATE = "venue_dataset_date";
    public static final String VENUE_DATASET_FILE = "venue_dataset_file";
    public static final String VENUE_DATASET_MD5 = "venue_dataset_md5";
    public static final String VENUE_ID = "venue_id";
    public static final String VENUE_IMAGES_DATE = "venue_images_date";
    public static final String VENUE_IMAGES_FILE = "venue_images_file";
    public static final String VENUE_IMAGES_MD5 = "venue_images_md5";
    public static final String VENUE_NAME = "venue_name";
    public static final String VENUE_PDEMAP_DATE = "venue_pdemap_date";
    public static final String VENUE_PDEMAP_FILE = "venue_pdemap_file";
    public static final String VENUE_PDEMAP_MD5 = "venue_pdemap_md5";
    public static final String VENUE_PLACE_IMAGES_DATE = "venue_place_images_date";
    public static final String VENUE_PLACE_IMAGES_FILE = "venue_place_images_file";
    public static final String VENUE_PLACE_IMAGES_MD5 = "venue_place_images_md5";
    public static final String VENUE_PROMOTION_IMAGES_DATE = "venue_promotion_images_date";
    public static final String VENUE_PROMOTION_IMAGES_FILE = "venue_promotion_images_file";
    public static final String VENUE_PROMOTION_IMAGES_MD5 = "venue_promotion_images_md5";
    public static final String VENUE_SUMMARY_ID = "venue_summary_id";
    public static final String VENUE_TYPE_ID = "venue_type_id";
    public static final String VENUE_UUID = "venue_uuid";
    public static final String VENUE_ZONE_IMAGES_DATE = "venue_zone_images_date";
    public static final String VENUE_ZONE_IMAGES_FILE = "venue_zone_images_file";
    public static final String VENUE_ZONE_IMAGES_MD5 = "venue_zone_images_md5";
    public static final String WEBSITE = "website";
  }
}

