package com.pointinside.android.api.dao;

import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;
import java.util.List;

import com.pointinside.android.api.dao.*;

public class PIReferenceDataset
  implements PIMapDataset
{
  private static final int CITIES = 7;
  private static final int CITY_ID = 8;
  private static final int COUNTRIES = 3;
  private static final int COUNTRY_ID = 4;
  private static final int COUNTRY_SUBDIVISIONS = 5;
  private static final int COUNTRY_SUBDIVISION_ID = 6;
  static final String TABLE_GEO_CITY = "geo_city";
  static final String TABLE_GEO_COUNTRY = "geo_country";
  static final String TABLE_GEO_COUNTRY_SUBDIVISION = "geo_country_subdivision";
  static final String TABLE_VENUE_SUMMARY = "venue_summary";
  private static final UriMatcher URI_MATCHER = new UriMatcher(-1);
  private static final int VENUES = 1;
  private static final int VENUE_ID = 2;
  private static final int VENUE_UUID = 9;
  private static HashMap<String, String> sGeoCityProjectionMap;
  private static HashMap<String, String> sGeoCountryProjectionMap;
  private static HashMap<String, String> sGeoCountrySubdivisionProjectionMap;
  private static HashMap<String, String> sVenueSummaryProjectionMap;
  private PISQLiteHelper mPISQLiteHelper;

  static
  {
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "venue_summary", 1);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "venue_summary/#", 2);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "venue_summary/*", 9);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "geo_city", 7);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "geo_city/#", 8);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "geo_country", 3);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "geo_country/#", 4);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "geo_country_subdivision", 5);
    URI_MATCHER.addURI("com.pointinside.android.api.reference", "geo_country_subdivision/#", 6);
    sVenueSummaryProjectionMap = new HashMap();
    sVenueSummaryProjectionMap.put("venue_summary_id", "venue_summary_id AS _id");
    sVenueSummaryProjectionMap.put("address_line1", "address_line1");
    sVenueSummaryProjectionMap.put("address_line2", "address_line2");
    sVenueSummaryProjectionMap.put("city", "city");
    sVenueSummaryProjectionMap.put("country", "country");
    sVenueSummaryProjectionMap.put("description", "description");
    sVenueSummaryProjectionMap.put("latitude", "latitude");
    sVenueSummaryProjectionMap.put("longitude", "longitude");
    sVenueSummaryProjectionMap.put("phone_number", "phone_number");
    sVenueSummaryProjectionMap.put("postal_code", "postal_code");
    sVenueSummaryProjectionMap.put("state_code", "state_code");
    sVenueSummaryProjectionMap.put("venue_dataset_date", "venue_dataset_date");
    sVenueSummaryProjectionMap.put("venue_dataset_file", "venue_dataset_file");
    sVenueSummaryProjectionMap.put("venue_dataset_md5", "venue_dataset_md5");
    sVenueSummaryProjectionMap.put("venue_images_date", "venue_images_date");
    sVenueSummaryProjectionMap.put("venue_images_file", "venue_images_file");
    sVenueSummaryProjectionMap.put("venue_images_md5", "venue_images_md5");
    sVenueSummaryProjectionMap.put("venue_zone_images_date", "venue_zone_images_date");
    sVenueSummaryProjectionMap.put("venue_zone_images_file", "venue_zone_images_file");
    sVenueSummaryProjectionMap.put("venue_zone_images_md5", "venue_zone_images_md5");
    sVenueSummaryProjectionMap.put("venue_name", "venue_name");
    sVenueSummaryProjectionMap.put("venue_place_images_date", "venue_place_images_date");
    sVenueSummaryProjectionMap.put("venue_place_images_file", "venue_place_images_file");
    sVenueSummaryProjectionMap.put("venue_place_images_md5", "venue_place_images_md5");
    sVenueSummaryProjectionMap.put("venue_promotion_images_date", "venue_promotion_images_date");
    sVenueSummaryProjectionMap.put("venue_promotion_images_file", "venue_promotion_images_file");
    sVenueSummaryProjectionMap.put("venue_promotion_images_md5", "venue_promotion_images_md5");
    sVenueSummaryProjectionMap.put("venue_pdemap_date", "venue_pdemap_date");
    sVenueSummaryProjectionMap.put("venue_pdemap_file", "venue_pdemap_file");
    sVenueSummaryProjectionMap.put("venue_pdemap_md5", "venue_pdemap_md5");
    sVenueSummaryProjectionMap.put("website", "website");
    sVenueSummaryProjectionMap.put("city_id", "city_id");
    sVenueSummaryProjectionMap.put("venue_id", "venue_id");
    sVenueSummaryProjectionMap.put("venue_uuid", "venue_uuid");
    sVenueSummaryProjectionMap.put("venue_type_id", "venue_type_id");
    sGeoCityProjectionMap = new HashMap();
    sGeoCityProjectionMap.put("city_id", "city_id AS _id");
    sGeoCityProjectionMap.put("country_id", "country_id");
    sGeoCityProjectionMap.put("subdivision_id", "subdivision_id");
    sGeoCityProjectionMap.put("name", "name");
    sGeoCityProjectionMap.put("creation_date", "creation_date");
    sGeoCityProjectionMap.put("modification_date", "modification_date");
    sGeoCountryProjectionMap = new HashMap();
    sGeoCountryProjectionMap.put("country_id", "country_id AS _id");
    sGeoCountryProjectionMap.put("country_code", "country_code");
    sGeoCountryProjectionMap.put("name", "name");
    sGeoCountryProjectionMap.put("creation_date", "creation_date");
    sGeoCountryProjectionMap.put("modification_date", "modification_date");
    sGeoCountrySubdivisionProjectionMap = new HashMap();
    sGeoCountrySubdivisionProjectionMap.put("subdivision_id", "subdivision_id AS _id");
    sGeoCountrySubdivisionProjectionMap.put("country_id", "country_id");
    sGeoCountrySubdivisionProjectionMap.put("subdivision_code", "subdivision_code");
    sGeoCountrySubdivisionProjectionMap.put("name", "name");
    sGeoCountrySubdivisionProjectionMap.put("creation_date", "creation_date");
    sGeoCountrySubdivisionProjectionMap.put("modification_date", "modification_date");
  }

  public PIReferenceDataset(PISQLiteHelper paramPISQLiteHelper)
  {
    this.mPISQLiteHelper = paramPISQLiteHelper;
  }

  public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
  {
      SQLiteQueryBuilder sqlitequerybuilder = new SQLiteQueryBuilder();
      switch(URI_MATCHER.match(uri)) {
//      JVM INSTR tableswitch 1 9: default 68
  //                   1 92
  //                   2 128
  //                   3 236
  //                   4 254
  //                   5 374
  //                   6 392
  //                   7 305
  //                   8 323
  //                   9 179;
  //       goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10
default:
      throw new IllegalArgumentException((new StringBuilder("Unknown URI ")).append(uri).toString());
case 1:
      sqlitequerybuilder.setTables("venue_summary");
      sqlitequerybuilder.setProjectionMap(sVenueSummaryProjectionMap);
      break;
case 2:
      sqlitequerybuilder.setTables("venue_summary");
      sqlitequerybuilder.setProjectionMap(sVenueSummaryProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id =")).append((String)uri.getPathSegments().get(1)).toString());
      break; /* Loop/switch isn't completed */
case 9:
      sqlitequerybuilder.setTables("venue_summary");
      sqlitequerybuilder.setProjectionMap(sVenueSummaryProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = \"")).append((String)uri.getPathSegments().get(1)).append("\"").toString());
      break; /* Loop/switch isn't completed */
case 3:
      sqlitequerybuilder.setTables("geo_country");
      sqlitequerybuilder.setProjectionMap(sGeoCountryProjectionMap);
      break; /* Loop/switch isn't completed */
case 4:
      sqlitequerybuilder.setTables("geo_country");
      sqlitequerybuilder.setProjectionMap(sGeoCountryProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id =")).append((String)uri.getPathSegments().get(1)).toString());
      break; /* Loop/switch isn't completed */
case 7:
      sqlitequerybuilder.setTables("geo_city");
      sqlitequerybuilder.setProjectionMap(sGeoCityProjectionMap);
      break; /* Loop/switch isn't completed */
case 8:
      sqlitequerybuilder.setTables("geo_city");
      sqlitequerybuilder.setProjectionMap(sGeoCityProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id =")).append((String)uri.getPathSegments().get(1)).toString());
      break; /* Loop/switch isn't completed */
case 5:
      sqlitequerybuilder.setTables("geo_country_subdivision");
      sqlitequerybuilder.setProjectionMap(sGeoCountrySubdivisionProjectionMap);
      break; /* Loop/switch isn't completed */
case 6:
      sqlitequerybuilder.setTables("geo_country_subdivision");
      sqlitequerybuilder.setProjectionMap(sGeoCountrySubdivisionProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id =")).append((String)uri.getPathSegments().get(1)).toString());
      break;
      }
      return sqlitequerybuilder.query(mPISQLiteHelper.getReadableDatabase(), as, s, as1, null, null, s1);
  }


}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIReferenceDataset
 * JD-Core Version:    0.7.0.1
 */