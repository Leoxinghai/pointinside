package com.pointinside.android.piwebservices.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.pointinside.android.piwebservices.util.DbUtils;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PIWebServicesProvider
  extends SQLiteContentProvider
{
  private static final String DB_NAME = "webrequests.db";
  private static final int DB_VERSION = 17;
  private static final int DEALS_RESULT = 10;
  private static final int DEALS_RESULTS = 6;
  private static final int DEALS_RESULTS_BY_REQUEST = 7;
  private static final int DEALS_RESULTS_BY_REQUEST_AND_LOCATION = 13;
  private static final int DEALS_RESULTS_BY_REQUEST_AND_VENUE = 14;
  private static final int DEALS_RESULTS_GROUP_BY_LOCATION = 12;
  private static final int DEALS_RESULTS_GROUP_BY_VENUE_UUID = 11;
  private static final int DESTINATION_DEALS_REQUEST = 9;
  private static final int DESTINATION_DEALS_REQUESTS = 8;
  private static final int NEARBY_DEALS_REQUEST = 5;
  private static final int NEARBY_DEALS_REQUESTS = 4;
  private static final int PLACES_REQUEST = 1;
  private static final int PLACES_REQUESTS = 0;
  private static final int PLACES_RESULT = 15;
  private static final int PLACES_RESULTS = 2;
  private static final int PLACES_RESULTS_BY_REQUEST = 3;
  private static final String TABLE_DEALS_RESULTS = "deals_results";
  private static final String TABLE_DESTINATION_DEALS_REQUESTS = "destination_deals_requests";
  private static final String TABLE_NEARBY_DEALS_REQUESTS = "nearby_deals_requests";
  private static final String TABLE_PLACES_REQUESTS = "places_requests";
  private static final String TABLE_PLACES_RESULTS = "places_results";
  private static final String TAG = PIWebServicesProvider.class.getSimpleName();
  private static final HashMap<String, String> sDealsResultsProjectionMap = new HashMap();
  private static UriMatcher sUriMatcher;

  static
  {
    sDealsResultsProjectionMap.put("_id", "_id");
    sDealsResultsProjectionMap.put("datasource_id", "datasource_id");
    sDealsResultsProjectionMap.put("title", "title");
    sDealsResultsProjectionMap.put("description", "description");
    sDealsResultsProjectionMap.put("brand", "brand");
    sDealsResultsProjectionMap.put("organization", "organization");
    sDealsResultsProjectionMap.put("thumbnail_image", "thumbnail_image");
    sDealsResultsProjectionMap.put("display_image", "display_image");
    sDealsResultsProjectionMap.put("start_date", "start_date");
    sDealsResultsProjectionMap.put("display_start_date", "display_start_date");
    sDealsResultsProjectionMap.put("end_date", "end_date");
    sDealsResultsProjectionMap.put("display_end_date", "display_end_date");
    sDealsResultsProjectionMap.put("upc", "upc");
    sDealsResultsProjectionMap.put("type", "type");
    sDealsResultsProjectionMap.put("category", "category");
    sDealsResultsProjectionMap.put("distance", "distance");
    sDealsResultsProjectionMap.put("latitude", "latitude");
    sDealsResultsProjectionMap.put("longitude", "longitude");
    sDealsResultsProjectionMap.put("place_uuid", "place_uuid");
    sDealsResultsProjectionMap.put("venue_uuid", "venue_uuid");
    sDealsResultsProjectionMap.put("request_id", "request_id");
    sDealsResultsProjectionMap.put("deal_count", "count(*) AS deal_count");
  }

  private static int dealDateToday()
  {
    Calendar localCalendar = Calendar.getInstance();
    return 10000 * localCalendar.get(1) + 100 * (1 + localCalendar.get(2)) + localCalendar.get(5);
  }

  private static String getSecondToLastPathSegment(Uri paramUri)
  {
    List localList = paramUri.getPathSegments();
    int i = localList.size();
    if (i < 2) {
      return null;
    }
    return (String)localList.get(i - 2);
  }

  private static final UriMatcher getUriMatcher()
  {
    String str = PIWebServicesContract.getAuthority();
    if (sUriMatcher == null)
    {
      sUriMatcher = new UriMatcher(-1);
      sUriMatcher.addURI(str, "places/requests", 0);
      sUriMatcher.addURI(str, "places/requests/#", 1);
      sUriMatcher.addURI(str, "places/results", 2);
      sUriMatcher.addURI(str, "places/results/#", 15);
      sUriMatcher.addURI(str, "places/results/byRequest/#", 3);
      sUriMatcher.addURI(str, "nearbyDeals/requests", 4);
      sUriMatcher.addURI(str, "nearbyDeals/requests/#", 5);
      sUriMatcher.addURI(str, "destinationDeals/requests", 8);
      sUriMatcher.addURI(str, "destinationDeals/requests/#", 9);
      sUriMatcher.addURI(str, "deals/results", 6);
      sUriMatcher.addURI(str, "deals/results/#", 10);
      sUriMatcher.addURI(str, "deals/results/byRequest/*/#", 7);
      sUriMatcher.addURI(str, "deals/results/groupByVenueUUID/#", 11);
      sUriMatcher.addURI(str, "deals/results/groupByLocation/#", 12);
      sUriMatcher.addURI(str, "deals/results/byRequestAndLocation/*/#", 13);
      sUriMatcher.addURI(str, "deals/results/byRequestAndVenue/*/#", 14);
    }
    return sUriMatcher;
  }

  protected SQLiteOpenHelper createDatabaseHelper(Context paramContext)
  {
    return new OpenHelper(paramContext);
  }

  protected int deleteInTransaction(Uri uri, String s, String as[])
  {
      SQLiteDatabase sqlitedatabase;
      int i;
      sqlitedatabase = getDatabaseHelper().getWritableDatabase();
      i = getUriMatcher().match(uri);
      switch(i)
      {
      case 1: // '\001'
      case 5: // '\005'
      case 9: // '\t'
      case 11: // '\013'
      case 12: // '\f'
      case 13: // '\r'
      case 14: // '\016'
      default:
          throw new IllegalArgumentException((new StringBuilder("Unsupported uri: ")).append(uri).toString());

      case 0: // '\0'
          return sqlitedatabase.delete("places_requests", s, as);

      case 2: // '\002'
      case 3: // '\003'
      case 15: // '\017'
          if(i == 15)
          {
              String as4[] = new String[2];
              as4[0] = s;
              as4[1] = (new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString();
              s = DbUtils.concatWhere(as4);
          } else
          if(i == 3)
          {
              String as3[] = new String[2];
              as3[0] = s;
              as3[1] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
              s = DbUtils.concatWhere(as3);
          }
          return sqlitedatabase.delete("places_results", s, as);

      case 4: // '\004'
          return sqlitedatabase.delete("nearby_deals_requests", s, as);

      case 8: // '\b'
          return sqlitedatabase.delete("destination_deals_requests", s, as);

      case 6: // '\006'
      case 7: // '\007'
      case 10: // '\n'
          break;
      }
      if(i == 10) {
	      String as2[] = new String[2];
	      as2[0] = s;
	      as2[1] = (new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString();
	      s = DbUtils.concatWhere(as2);
	      return sqlitedatabase.delete("deals_results", s, as);
      } else {
	      if(i == 7)
	      {
	          String as1[] = new String[3];
	          as1[0] = s;
	          as1[1] = (new StringBuilder("request_type=")).append(DatabaseUtils.sqlEscapeString(getSecondToLastPathSegment(uri))).toString();
	          as1[2] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
	          s = DbUtils.concatWhere(as1);
	      }
      }
      return i;
  }

  public String getType(Uri uri)
  {
      switch(getUriMatcher().match(uri))
      {
      case 8: // '\b'
      case 9: // '\t'
      case 10: // '\n'
      default:
          throw new IllegalArgumentException((new StringBuilder("Unsupported uri: ")).append(uri).toString());

      case 0: // '\0'
          return "vnd.pointinside.cursor.dir/placeRequests";

      case 1: // '\001'
          return "vnd.pointinside.cursor.item/placeRequests";

      case 15: // '\017'
          return "vnd.pointinside.cursor.item/placeResults";

      case 2: // '\002'
      case 3: // '\003'
          return "vnd.pointinside.cursor.dir/placeResults";

      case 4: // '\004'
          return "vnd.pointinside.cursor.dir/nearbyDealRequests";

      case 5: // '\005'
          return "vnd.pointinside.cursor.item/nearbyDealRequests";

      case 6: // '\006'
      case 7: // '\007'
      case 11: // '\013'
      case 12: // '\f'
      case 13: // '\r'
      case 14: // '\016'
          return "vnd.pointinside.cursor.dir/dealResults";
      }
  }

  protected Uri insertInTransaction(Uri uri, ContentValues contentvalues)
  {
      SQLiteDatabase sqlitedatabase = getDatabaseHelper().getWritableDatabase();
      long l;
      Uri uri1;
      switch(getUriMatcher().match(uri)) {
//      JVM INSTR tableswitch 0 8: default 64
  //                   0 88
  //                   1 64
  //                   2 129
  //                   3 64
  //                   4 147
  //                   5 64
  //                   6 183
  //                   7 64
  //                   8 165;
//         goto _L1 _L2 _L1 _L3 _L1 _L4 _L1 _L5 _L1 _L6
      case 1:
      case 3:
      case 5:
      case 7:
      default:
      throw new IllegalArgumentException((new StringBuilder("Unsupported uri: ")).append(uri).toString());
      case 0:
      l = sqlitedatabase.insert("places_requests", null, contentvalues);
      uri1 = PIWebServicesContract.PlacesRequests.CONTENT_URI;
      break;
      case 2:
      l = sqlitedatabase.insert("places_results", null, contentvalues);
      uri1 = PIWebServicesContract.PlacesResults.CONTENT_URI;
      break;
      case 4:
      l = sqlitedatabase.insert("nearby_deals_requests", null, contentvalues);
      uri1 = PIWebServicesContract.NearbyDealsRequests.CONTENT_URI;
      break;
      case 8:
      l = sqlitedatabase.insert("destination_deals_requests", null, contentvalues);
      uri1 = PIWebServicesContract.DestinationDealsRequests.CONTENT_URI;
      break;
      case 6:
      l = sqlitedatabase.insert("deals_results", null, contentvalues);
      uri1 = PIWebServicesContract.DealsResults.CONTENT_URI;
      break;
      }
      
      Uri uri2 = null;
      if(l > 0)
          uri2 = ContentUris.withAppendedId(uri1, l);
      return uri2;
  }

  protected void notifyChange()
  {
    getContext().getContentResolver().notifyChange(PIWebServicesContract.PlacesRequests.CONTENT_URI, null);
    getContext().getContentResolver().notifyChange(PIWebServicesContract.PlacesResults.CONTENT_URI, null);
    getContext().getContentResolver().notifyChange(PIWebServicesContract.NearbyDealsRequests.CONTENT_URI, null);
    getContext().getContentResolver().notifyChange(PIWebServicesContract.DealsResults.CONTENT_URI, null);
  }

  public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
  {
      SQLiteDatabase sqlitedatabase;
      SQLiteQueryBuilder sqlitequerybuilder;
      String s2;
      int i;
      sqlitedatabase = getDatabaseHelper().getReadableDatabase();
      sqlitequerybuilder = new SQLiteQueryBuilder();
      s2 = null;
     i = getUriMatcher().match(uri);
    switch (i)
    {
    default:
      throw new IllegalArgumentException("Unsupported uri: " + uri);
    case 0:
    case 1:
        sqlitequerybuilder.setTables("places_requests");
        s2 = null;
        if(i == 1)
            sqlitequerybuilder.appendWhere((new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString());
      break;
    case 2:
    case 3:
        sqlitequerybuilder.setTables("places_results");
        s2 = null;
        if(i == 3)
        {
            sqlitequerybuilder.appendWhere((new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString());
            s2 = null;
        }
        break;
    case 4:
    case 5:
        sqlitequerybuilder.setTables("nearby_deals_requests");
        s2 = null;
        if(i == 5)
        {
            sqlitequerybuilder.appendWhere((new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString());
            s2 = null;
        }
        break;

    case 6:
    case 7:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:

        String s3;
        sqlitequerybuilder.setTables("deals_results");
        s3 = (new StringBuilder("(end_date >= ")).append(dealDateToday()).append(" OR ").append("end_date").append(" IS NULL)").toString();
        if(i != 7)
            break; /* Loop/switch isn't completed */
        String as8[] = new String[3];
        as8[0] = s3;
        as8[1] = (new StringBuilder("request_type=")).append(DatabaseUtils.sqlEscapeString(getSecondToLastPathSegment(uri))).toString();
        as8[2] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
        s3 = DbUtils.concatWhere(as8);
        sqlitequerybuilder.appendWhere(s3);

        if(i == 11)
        {
            sqlitequerybuilder.setProjectionMap(sDealsResultsProjectionMap);
            String as7[] = new String[4];
            as7[0] = s3;
            as7[1] = "venue_uuid IS NOT NULL";
            as7[2] = "venue_uuid != 'null' ";
            as7[3] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
            s3 = DbUtils.concatWhere(as7);
            s2 = "venue_uuid";
        } else
        if(i == 12)
        {
            sqlitequerybuilder.setProjectionMap(sDealsResultsProjectionMap);
            String as6[] = new String[2];
            as6[0] = s3;
            as6[1] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
            s3 = DbUtils.concatWhere(as6);
            s2 = "latitude, longitude";
        } else
        if(i == 14)
        {
            String as5[] = new String[3];
            as5[0] = s3;
            as5[1] = (new StringBuilder("venue_uuid=")).append(DatabaseUtils.sqlEscapeString(getSecondToLastPathSegment(uri))).toString();
            as5[2] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
            s3 = DbUtils.concatWhere(as5);
            s2 = null;
        } else
        if(i == 13)
        {
            String as3[] = Uri.decode(getSecondToLastPathSegment(uri)).split(",");
            double d = Double.parseDouble(as3[0]);
            double d1 = Double.parseDouble(as3[1]);
            String as4[] = new String[4];
            as4[0] = s3;
            as4[1] = (new StringBuilder("latitude=")).append(d).toString();
            as4[2] = (new StringBuilder("longitude=")).append(d1).toString();
            as4[3] = (new StringBuilder("request_id=")).append(ContentUris.parseId(uri)).toString();
            s3 = DbUtils.concatWhere(as4);
            s2 = null;
        } else
        {
            s2 = null;
            if(i == 10)
            {
                String as2[] = new String[2];
                as2[0] = s3;
                as2[1] = (new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString();
                s3 = DbUtils.concatWhere(as2);
                s2 = null;
            }
        }

        Cursor cursor = sqlitequerybuilder.query(sqlitedatabase, as, s, as1, s2, null, s1);
        if(cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    case 15:
        sqlitequerybuilder.setTables("places_results");
        sqlitequerybuilder.appendWhere((new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString());
        s2 = null;
        break;

    case 8:
    case 9:
        sqlitequerybuilder.setTables("destination_deals_requests");
        s2 = null;
        if(i == 9)
        {
            sqlitequerybuilder.appendWhere((new StringBuilder("_id=")).append(ContentUris.parseId(uri)).toString());
            s2 = null;
        }
        break;
    }
    Cursor cursor = sqlitequerybuilder.query(sqlitedatabase, as, s, as1, s2, null, s1);
    if(cursor != null)
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  protected int updateInTransaction(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = getDatabaseHelper().getWritableDatabase();
    int i = getUriMatcher().match(paramUri);
    switch (i)
    {
    case 1:
    case 3:
    case 5:
    case 7:
    default:
      throw new IllegalArgumentException("Unsupported uri: " + paramUri);
    case 0:
      return localSQLiteDatabase.update("places_requests", paramContentValues, paramString, paramArrayOfString);
    case 2:
      return localSQLiteDatabase.update("places_results", paramContentValues, paramString, paramArrayOfString);
    case 4:
      return localSQLiteDatabase.update("nearby_deals_requests", paramContentValues, paramString, paramArrayOfString);
    case 8:
        return localSQLiteDatabase.update("deals_results", paramContentValues, paramString, paramArrayOfString);
    case 9:
      if (i == 9)
      {
        String[] arrayOfString = new String[2];
        arrayOfString[0] = paramString;
        arrayOfString[1] = ("_id=" + ContentUris.parseId(paramUri));
        paramString = DbUtils.concatWhere(arrayOfString);
      }
      return localSQLiteDatabase.update("destination_deals_requests", paramContentValues, paramString, paramArrayOfString);
    }
  }

  private static class OpenHelper
    extends SQLiteOpenHelper
  {
    public OpenHelper(Context paramContext)
    {
      super(paramContext, "webrequests.db", null,17);
    }

    private void createTables(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE places_requests (_id INTEGER PRIMARY KEY AUTOINCREMENT, query TEXT, category_id INTEGER, latitude INTEGER NOT NULL, longitude INTEGER NOT NULL, radius INTEGER, timestamp INTEGER NOT NULL )");
      paramSQLiteDatabase.execSQL("CREATE TABLE places_results (_id INTEGER PRIMARY KEY AUTOINCREMENT, request_id INTEGER NOT NULL REFERENCES places_requests(_id), urbanq_id TEXT, place_uuid TEXT, title TEXT NOT NULL, description TEXT, STREET TEXT, city TEXT, state TEXT, zip TEXT, phone TEXT, web_url TEXT, web_label TEXT, latitude INTEGER NOT NULL, longitude INTEGER NOT NULL, venue_proximity INTEGER, venue_uuid TEXT, raw_hours TEXT, hours_open_monday TEXT, hours_close_monday TEXT, hours_open_tuesday TEXT, hours_close_tuesday TEXT, hours_open_wednesday TEXT, hours_close_wednesday TEXT, hours_open_thursday TEXT, hours_close_thursday TEXT, hours_open_friday TEXT, hours_close_friday TEXT, hours_open_saturday TEXT, hours_close_saturday TEXT, hours_open_sunday TEXT, hours_close_sunday TEXT)");
      paramSQLiteDatabase.execSQL("CREATE INDEX idx_places_results_request ON places_results (request_id)");
      paramSQLiteDatabase.execSQL("CREATE TABLE nearby_deals_requests (_id INTEGER PRIMARY KEY AUTOINCREMENT, latitude INTEGER NOT NULL, longitude INTEGER NOT NULL, radius INTEGER, timestamp INTEGER NOT NULL )");
      paramSQLiteDatabase.execSQL("CREATE TABLE destination_deals_requests (_id INTEGER PRIMARY KEY AUTOINCREMENT, venue TEXT NOT NULL, timestamp INTEGER NOT NULL )");
      paramSQLiteDatabase.execSQL("CREATE TABLE deals_results (_id INTEGER PRIMARY KEY AUTOINCREMENT, request_id INTEGER NOT NULL REFERENCES nearby_deals_requests(_id), request_type TEXT NOT NULL, datasource TEXT NOT NULL, datasource_id TEXT NOT NULL, title TEXT NOT NULL, description TEXT, brand TEXT, distance TEXT, type TEXT, category TEXT, organization TEXT, start_date INTEGER, display_start_date TEXT, end_date INTEGER, display_end_date TEXT, upc TEXT, display_image TEXT NOT NULL, thumbnail_image TEXT, latitude INTEGER, longitude INTEGER, place_uuid TEXT, venue_uuid TEXT )");
      paramSQLiteDatabase.execSQL("CREATE INDEX idx_deals_results_request ON deals_results (request_id, request_type)");
      paramSQLiteDatabase.execSQL("CREATE INDEX idx_deals_results_end_date ON deals_results (end_date)");
    }

    private void dropTables(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS places_requests");
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS places_results");
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS nearby_deals_requests");
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS destination_deals_requests");
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS deals_results");
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      createTables(paramSQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      Log.i(PIWebServicesProvider.TAG, "Upgrading from " + paramInt1 + " to " + paramInt2);
      dropTables(paramSQLiteDatabase);
      createTables(paramSQLiteDatabase);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.provider.PIWebServicesProvider
 * JD-Core Version:    0.7.0.1
 */