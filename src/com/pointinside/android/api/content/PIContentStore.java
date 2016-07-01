package com.pointinside.android.api.content;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.pointinside.android.api.dao.PIMapDataset;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

class PIContentStore
  implements PIMapDataset
{
  static final String DATABASE_NAME = "pi_content.db";
  private static final int DATABASE_VERSION = 4;
  static final int DOWNLOADS = 1;
  static final int DOWNLOAD_DATASET = 8;
  static final int DOWNLOAD_DATASETS = 19;
  static final int DOWNLOAD_ID = 2;
  static final int DOWNLOAD_PLACE_IMAGES = 6;
  static final int DOWNLOAD_PROMO_IMAGES = 7;
  static final int DOWNLOAD_REFERENCE = 3;
  static final int DOWNLOAD_VENUE_IMAGES = 5;
  static final int DOWNLOAD_VENUE_PDEMAP = 20;
  static final int DOWNLOAD_VENUE_ZONE_IMAGES = 4;
  static final int FILES = 9;
  static final int FILE_DATASET = 17;
  static final int FILE_DATASETS = 18;
  static final int FILE_ID = 10;
  static final int FILE_PLACE_IMAGE = 15;
  static final int FILE_PROMO_IMAGE = 16;
  static final int FILE_REFERENCE = 11;
  static final int FILE_VENUE_IMAGE = 14;
  static final int FILE_VENUE_PDEMAP = 21;
  static final int FILE_VENUE_ZONE_IMAGE = 13;
  static final int FILE_VENUE_ZONE_IMAGES = 12;
  static final Uri PI_CONTENT_STORE_URI;
  private static String TAG = "PIContentStore";
  static final UriMatcher URI_MATCHER;
  static final String URI_SEPARATOR_DATASET = "/dataset";
  static final String URI_SEPARATOR_PLACE_IMAGE = "/place_image";
  static final String URI_SEPARATOR_PROMO_IMAGE = "/promo_image";
  static final String URI_SEPARATOR_REFERENCE = "/reference";
  static final String URI_SEPARATOR_VENUE = "/venue";
  static final String URI_SEPARATOR_VENUE_IMAGE = "/venue_image";
  static final String URI_SEPARATOR_VENUE_PDEMAP = "/venue_pdemap";
  static final String URI_SEPARATOR_ZONE_IMAGE = "/zone_images";
  private static HashMap<String, String> sDownloadsProjectionMap;
  private static HashMap<String, String> sFilesProjectionMap;
  private static PIContentStore sPIContentStore;
  private WeakReference<Context> mContext;
  private DatabaseHelper mDatabaseHelper;

  static
  {
    PI_CONTENT_STORE_URI = Uri.parse("content://com.pointinside.android.api.content");
    URI_MATCHER = new UriMatcher(-1);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads", 1);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/#", 2);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/reference", 3);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/venue/*/zone_images", 4);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/venue/*/venue_pdemap", 20);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/venue/*/venue_image", 5);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/venue/*/place_image", 6);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/venue/*/promo_image", 7);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/venue/*/dataset", 8);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "downloads/dataset", 19);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files", 9);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/#", 10);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/reference", 11);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/zone_images", 12);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/zone_images/*", 13);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/venue_pdemap", 21);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/venue_image/*", 14);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/place_image/*", 15);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/promo_image/*", 16);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/venue/*/dataset", 17);
    URI_MATCHER.addURI("com.pointinside.android.api.content", "files/dataset", 18);
    sFilesProjectionMap = new HashMap();
    sFilesProjectionMap.put("_id", "_id");
    sFilesProjectionMap.put("file_uri", "file_uri");
    sFilesProjectionMap.put("size", "size");
    sFilesProjectionMap.put("file_name", "file_name");
    sFilesProjectionMap.put("content_type", "content_type");
    sFilesProjectionMap.put("venue_uuid", "venue_uuid");
    sFilesProjectionMap.put("download_id", "download_id");
    sFilesProjectionMap.put("date_added", "date_added");
    sFilesProjectionMap.put("date_modified", "date_modified");
    sFilesProjectionMap.put("scanned", "scanned");
    sFilesProjectionMap.put("datetoken", "datetoken");
    sDownloadsProjectionMap = new HashMap();
    sDownloadsProjectionMap.put("_id", "_id");
    sDownloadsProjectionMap.put("venue_uuid", "venue_uuid");
    sDownloadsProjectionMap.put("remote_uri", "remote_uri");
    sDownloadsProjectionMap.put("retry_after", "retry_after");
    sDownloadsProjectionMap.put("file_name", "file_name");
    sDownloadsProjectionMap.put("content_type", "content_type");
    sDownloadsProjectionMap.put("control", "control");
    sDownloadsProjectionMap.put("status", "status");
    sDownloadsProjectionMap.put("numfailed", "numfailed");
    sDownloadsProjectionMap.put("lastmod", "lastmod");
    sDownloadsProjectionMap.put("total_bytes", "total_bytes");
    sDownloadsProjectionMap.put("current_bytes", "current_bytes");
    sDownloadsProjectionMap.put("download_identifier", "download_identifier");
    sDownloadsProjectionMap.put("updated_identifier", "updated_identifier");
    sDownloadsProjectionMap.put("extracted", "extracted");
    sDownloadsProjectionMap.put("purged", "purged");
  }

  private PIContentStore(Context paramContext)
  {
    this.mDatabaseHelper = new DatabaseHelper(paramContext, "pi_content.db");
    this.mContext = new WeakReference(paramContext);
  }

  static PIContentStore getInstance(Context paramContext)
  {
    if ((sPIContentStore == null) || (sPIContentStore.mContext == null) || (sPIContentStore.mContext.get() == null)) {
      sPIContentStore = new PIContentStore(paramContext);
    }
    return sPIContentStore;
  }

  private static void updateDatabase(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    if (paramInt2 != 4)
    {
      Log.e(TAG, "Illegal update request. Got " + paramInt2 + ", expected " + 4);
      throw new IllegalArgumentException();
    }
    if (paramInt1 > paramInt2)
    {
      Log.e(TAG, "Illegal update request: can't downgrade from " + paramInt1 + " to " + paramInt2 + ". Did you forget to wipe data?");
      throw new IllegalArgumentException();
    }
    Log.i(TAG, "Upgrading PI content store database from version " + paramInt1 + " to " + paramInt2 + ", which will destroy all old data");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS files");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS downloads");
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS files (_id INTEGER PRIMARY KEY, file_uri TEXT UNIQUE ON CONFLICT REPLACE, size INTEGER, file_name TEXT, content_type INTEGER DEFAULT 0, venue_uuid TEXT, download_id INTEGER NOT NULL, date_added INTEGER, date_modified INTEGER, scanned INTEGER DEFAULT 0, datetoken INTEGER);");
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS downloads(_id INTEGER PRIMARY KEY,venue_uuid TEXT, remote_uri TEXT UNIQUE NOT NULL ON CONFLICT REPLACE, retry_after INTEGER, file_name TEXT, content_type INTEGER DEFAULT 0, control INTEGER, status INTEGER, numfailed INTEGER, lastmod INTEGER, total_bytes INTEGER , current_bytes INTEGER DEFAULT 0, download_identifier TEXT, updated_identifier TEXT, extracted INTEGER DEFAULT 0, purged INTEGER default 0);");
  }

  int delete(Uri uri, String s, String as[])
  {
      int i;
      SQLiteDatabase sqlitedatabase;
      long l;
      i = URI_MATCHER.match(uri);
      sqlitedatabase = mDatabaseHelper.getWritableDatabase();
      l = 0L;
      try {
	      long l1 = ContentUris.parseId(uri);
	      l = l1;
      } catch(Exception ex) {
    	  
      }
      int j =0;
//_L13:
      switch(i) {
  //    JVM INSTR tableswitch 1 17: default 116
  //                   1 144
  //                   2 181
  //                   3 116
  //                   4 281
  //                   5 116
  //                   6 116
  //                   7 116
  //                   8 213
  //                   9 349
  //                   10 363
  //                   11 116
  //                   12 463
  //                   13 116
  //                   14 116
  //                   15 116
  //                   16 116
  //                   17 395;
//         goto _L1 _L2 _L3 _L1 _L4 _L1 _L1 _L1 _L5 _L6 _L7 _L1 _L8 _L1 _L1 _L1 _L1 _L9
//3,5,6,7,11,13,14,15,16,
      case 3:
      case 5:
      case 6:
      case 7:
      case 11:
      case 13:
      case 14:
      case 15:
      case 16:
      default:
      throw new IllegalStateException((new StringBuilder("Unknown URL: ")).append(uri.toString()).toString());
      case 1:
      j = sqlitedatabase.delete("downloads", s, as);
      break;
      case 2:
      String as2[] = new String[1];
      as2[0] = String.valueOf(l);
      j = sqlitedatabase.delete("downloads", "_id = ?", as2);
      break; /* Loop/switch isn't completed */
      case 8:
      j = sqlitedatabase.delete("downloads", (new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.DATASET.ordinal()).toString(), null);
      break; /* Loop/switch isn't completed */
      case 4:
      j = sqlitedatabase.delete("downloads", (new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_ZONE_IMAGE.ordinal()).toString(), null);
      break; /* Loop/switch isn't completed */
      case 9:
      j = sqlitedatabase.delete("files", s, as);
      break; /* Loop/switch isn't completed */
      case 10:
      String as1[] = new String[1];
      as1[0] = String.valueOf(l);
      j = sqlitedatabase.delete("files", "_id = ?", as1);
      break; /* Loop/switch isn't completed */
      case 17:
      j = sqlitedatabase.delete("files", (new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.DATASET.ordinal()).toString(), null);
      break; /* Loop/switch isn't completed */
      case 12:
      j = sqlitedatabase.delete("files", (new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_ZONE_IMAGE.ordinal()).toString(), null);
      break;
      }      

      if(j > 0)
          ((Context)mContext.get()).getContentResolver().notifyChange(uri, null);
      return j;
      
  }

  

  Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    int i = URI_MATCHER.match(paramUri);
    SQLiteDatabase localSQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
    if (paramContentValues == null) {
      throw new UnsupportedOperationException("ContentValues are null for insert: " + paramUri);
    }
    long l2;
    switch (i)
    {
    case 1:
      l2 = localSQLiteDatabase.insert("downloads", null, paramContentValues);
      if (l2 > 0L)
      {
        Intent localIntent = new Intent("pointinside.intent.action.DOWNLOAD_NEW");
        if (this.mContext.get() != null) {
          ((Context)this.mContext.get()).sendBroadcast(localIntent);
        }
      }
      break;
    case 2:
    case 9:
        long l1 = System.currentTimeMillis();
        paramContentValues.put("date_added", Long.valueOf(l1));
        paramContentValues.put("date_modified", Long.valueOf(l1));
        l2 = localSQLiteDatabase.insert("files", null, paramContentValues);
        break;

    default:
        throw new UnsupportedOperationException("Invalid URI " + paramUri);
    }

    boolean bool = l2 > 0L;
      Uri localUri = null;
      if (l2 > 0)
      {
        localUri = ContentUris.withAppendedId(paramUri, l2);
        ((Context)this.mContext.get()).getContentResolver().notifyChange(localUri, null);
      }
      return localUri;
  }

  public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
  {
      int i;
      SQLiteDatabase sqlitedatabase;
      SQLiteQueryBuilder sqlitequerybuilder;
      i = URI_MATCHER.match(uri);
      sqlitedatabase = mDatabaseHelper.getReadableDatabase();
      sqlitequerybuilder = new SQLiteQueryBuilder();
      switch(i) {
//      JVM INSTR tableswitch 1 21: default 128
  //                   1 156
  //                   2 188
  //                   3 240
  //                   4 363
  //                   5 441
  //                   6 519
  //                   7 597
  //                   8 675
  //                   9 798
  //                   10 816
  //                   11 868
  //                   12 913
  //                   13 1192
  //                   14 1303
  //                   15 1414
  //                   16 1525
  //                   17 1069
  //                   18 1147
  //                   19 753
  //                   20 285
  //                   21 991;
//         goto _L1 _L2 _L3 _L4 _L5 _L6 _L7 _L8 _L9 _L10 _L11 _L12 _L13 _L14 _L15 _L16 _L17 _L18 _L19 _L20 _L21 _L22
default:
      throw new IllegalStateException((new StringBuilder("Unknown URL: ")).append(uri.toString()).toString());
case 1:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      break;
case 2:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append((String)uri.getPathSegments().get(1)).toString());
      break; /* Loop/switch isn't completed */
case 3:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("content_type = ")).append(PIContentManager.ContentType.REFERENCE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 20:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_PDEMAP.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 4:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_ZONE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 5:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 6:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.PLACE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 7:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.PROMOTION_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 8:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.DATASET.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 19:
      sqlitequerybuilder.setTables("downloads");
      sqlitequerybuilder.setProjectionMap(sDownloadsProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("content_type = ")).append(PIContentManager.ContentType.DATASET.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 9:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      break; /* Loop/switch isn't completed */
case 10:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("_id = ")).append((String)uri.getPathSegments().get(1)).toString());
      break; /* Loop/switch isn't completed */
case 11:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("content_type=")).append(PIContentManager.ContentType.REFERENCE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 12:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_ZONE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 21:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_PDEMAP.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 17:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.DATASET.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 18:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("content_type = ")).append(PIContentManager.ContentType.DATASET.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 13:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("file_name").append(" = '").append((String)uri.getPathSegments().get(4)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_ZONE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 14:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("file_name").append(" = '").append((String)uri.getPathSegments().get(4)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.VENUE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 15:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("file_name").append(" = '").append((String)uri.getPathSegments().get(4)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.PLACE_IMAGE.ordinal()).toString());
      break; /* Loop/switch isn't completed */
case 16:
      sqlitequerybuilder.setTables("files");
      sqlitequerybuilder.setProjectionMap(sFilesProjectionMap);
      sqlitequerybuilder.appendWhere((new StringBuilder("venue_uuid = '")).append((String)uri.getPathSegments().get(2)).append("' AND ").append("file_name").append(" = '").append((String)uri.getPathSegments().get(4)).append("' AND ").append("content_type").append(" = ").append(PIContentManager.ContentType.PROMOTION_IMAGE.ordinal()).toString());
      break;
      }
      return sqlitequerybuilder.query(sqlitedatabase, as, s, as1, null, null, s1, null);

  }

  int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    if (paramContentValues == null) {
      throw new UnsupportedOperationException("ContentValues are null for update: " + paramUri);
    }
    int i = URI_MATCHER.match(paramUri);
    SQLiteDatabase localSQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
    long l = ContentUris.parseId(paramUri);
    String[] arrayOfString2;
    int j=0;
    switch (i)
    {
    default:
      throw new UnsupportedOperationException("Invalid URI " + paramUri);
    case 2:
      arrayOfString2 = new String[1];
      arrayOfString2[0] = String.valueOf(l);
      j = localSQLiteDatabase.update("downloads", paramContentValues, "_id = ?", arrayOfString2);;
      break;
    case 10:
        String[] arrayOfString1;
        paramContentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
        arrayOfString1 = new String[1];
        arrayOfString1[0] = String.valueOf(l);
        j = localSQLiteDatabase.update("files", paramContentValues, "_id = ?", arrayOfString1);
    	break;
    }
    if (j > 0) {
        ((Context)this.mContext.get()).getContentResolver().notifyChange(paramUri, null);
      }
      return j;
  }

  private static final class DatabaseHelper
    extends SQLiteOpenHelper
  {
    public DatabaseHelper(Context paramContext, String paramString)
    {
//      super(paramString, null,4);
    	super(paramContext, paramString,null,4);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      PIContentStore.updateDatabase(paramSQLiteDatabase, 0, 4);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      PIContentStore.updateDatabase(paramSQLiteDatabase, paramInt1, paramInt2);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.PIContentStore
 * JD-Core Version:    0.7.0.1
 */