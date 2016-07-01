package com.pointinside.android.piwebservices.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import com.pointinside.android.api.PIMapReference;
import java.util.List;

public class PITouchstreamProvider
  extends ContentProvider
{
  static final String TABLE_ACCOUNTS = "accounts";
  static final String TABLE_FAVORITES = "favorites";
  static final String TABLE_TOUCHSTREAM = "touchstream";
  private static final int TOUCHSTREAM = 1;
  private static final int TOUCHSTREAM_ID = 2;
  private static Context sContext;
  private static PIMapReference sPIMapReference;
  private static SharedPreferences sPrefs;
  private final Handler mHandler = new Handler();
  private UriMatcher mMatcher;
  private SQLiteOpenHelper mOpenHelper;

  public int delete(Uri uri, String s, String as[])
  {
      SQLiteDatabase sqlitedatabase;
      if(Binder.getCallingPid() != Process.myPid())
          throw new SecurityException("Cannot insert into this provider");
      sqlitedatabase = mOpenHelper.getWritableDatabase();
      int i =0;
      switch(getMatcher().match(uri)) {
      //JVM INSTR tableswitch 1 2: default 60
  //                   1 84
  //                   2 117;
//         goto _L1 _L2 _L3
      default:
      throw new IllegalArgumentException((new StringBuilder("Unknown URI: ")).append(uri).toString());
      case 1:
	      i = sqlitedatabase.delete("touchstream", s, as);
	      break;

      case 2:
	      String s1 = (String)uri.getPathSegments().get(1);
	      StringBuilder stringbuilder = (new StringBuilder("_id=")).append(s1);
	      String s2;
	      if(!TextUtils.isEmpty(s))
	          s2 = (new StringBuilder(" AND (")).append(s).append(')').toString();
	      else
	          s2 = "";
	      i = sqlitedatabase.delete("touchstream", stringbuilder.append(s2).toString(), as);
	      break;
      }
      if(i > 0)
          getContext().getContentResolver().notifyChange(PITouchstreamContract.Touchstream.CONTENT_URI, null);
      return i;

  }

  public UriMatcher getMatcher()
  {
    if (this.mMatcher == null)
    {
      UriMatcher localUriMatcher = new UriMatcher(-1);
      localUriMatcher.addURI(PITouchstreamContract.getAuthority(), "touchstream", 1);
      localUriMatcher.addURI(PITouchstreamContract.getAuthority(), "touchstream/#", 2);
      this.mMatcher = localUriMatcher;
    }
    return this.mMatcher;
  }

  public String getType(Uri uri)
  {
      switch(getMatcher().match(uri))
      {
      default:
          throw new IllegalArgumentException((new StringBuilder("Unknown URI: ")).append(uri).toString());

      case 1: // '\001'
          return "vnd.tmobile.cursor.dir/touchstream";

      case 2: // '\002'
          return "vnd.tmobile.cursor.item/touchstream";
      }
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    if (Binder.getCallingPid() != Process.myPid()) {
      throw new SecurityException("Cannot insert into this provider");
    }
    if (getMatcher().match(paramUri) != 1) {
      throw new IllegalArgumentException("Unknown URI " + paramUri);
    }
    long l = this.mOpenHelper.getWritableDatabase().insert("touchstream", null, paramContentValues);
    if (l > 0L)
    {
      Uri localUri = ContentUris.withAppendedId(PITouchstreamContract.Touchstream.CONTENT_URI, l);
      getContext().getContentResolver().notifyChange(PITouchstreamContract.Touchstream.CONTENT_URI, null);
      return localUri;
    }
    throw new SQLException("Failed to insert row into " + paramUri);
  }

  public boolean onCreate()
  {
    this.mOpenHelper = new DatabaseHelper(getContext());
    return true;
  }

  public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
  {
      SQLiteQueryBuilder sqlitequerybuilder = new SQLiteQueryBuilder();
      switch(getMatcher().match(uri)) {
//      JVM INSTR tableswitch 1 2: default 40
  //                   1 64
  //                   2 113;
  //       goto _L1 _L2 _L3
      default:
      throw new IllegalArgumentException((new StringBuilder("Unknown URI ")).append(uri).toString());
      case 1:
      sqlitequerybuilder.setTables("touchstream");
      break;
      case 2:
      sqlitequerybuilder.setTables("touchstream");
      sqlitequerybuilder.appendWhere((new StringBuilder("_id=")).append((String)uri.getPathSegments().get(1)).toString());
      break;
      }
      Cursor cursor = sqlitequerybuilder.query(mOpenHelper.getReadableDatabase(), as, s, as1, null, null, s1);
      cursor.setNotificationUri(getContext().getContentResolver(), PITouchstreamContract.Touchstream.CONTENT_URI);
      return cursor;
  }

  public int update(Uri uri, ContentValues contentvalues, String s, String as[])
  {
      SQLiteDatabase sqlitedatabase;
      if(Binder.getCallingPid() != Process.myPid())
          throw new SecurityException("Cannot insert into this provider");
      sqlitedatabase = mOpenHelper.getWritableDatabase();
      int i =0;
      switch(getMatcher().match(uri)) {
      //JVM INSTR tableswitch 1 2: default 60
  //                   1 84
  //                   2 119;
//         goto _L1 _L2 _L3
default:
      throw new IllegalArgumentException((new StringBuilder("Unknown URI ")).append(uri).toString());
case 1:
      i = sqlitedatabase.update("touchstream", contentvalues, s, as);
      break;
case 2:
      long l = ContentUris.parseId(uri);
      StringBuilder stringbuilder = (new StringBuilder("_id=")).append(l);
      String s1;
      if(!TextUtils.isEmpty(s))
          s1 = (new StringBuilder(" AND (")).append(s).append(')').toString();
      else
          s1 = "";
      i = sqlitedatabase.update("touchstream", contentvalues, stringbuilder.append(s1).toString(), as);
      break;
   }

      if(i > 0)
          getContext().getContentResolver().notifyChange(PITouchstreamContract.Touchstream.CONTENT_URI, null);
      return i;
  }

  private class DatabaseHelper
    extends SQLiteOpenHelper
  {
    private static final String DATABASE_NAME = "pointinside.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context paramContext)
    {
      super(paramContext, "pointinside.db", null, 4);
    }

    private void createTables(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS touchstream (_id INTEGER PRIMARY KEY AUTOINCREMENT,_data TEXT NOT NULL,visit_time INTEGER,data_type INTEGER,loc_time INTEGER DEFAULT 0,latitude TEXT,longitude TEXT,altitude TEXT,loc_accuracy TEXT,loc_provider TEXT,venue_id INTEGER DEFAULT 0,session_id INTEGER);");
      paramSQLiteDatabase.execSQL("CREATE INDEX touchstream_package ON touchstream (_data)");
    }

    private void dropTables(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS touchstream");
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      createTables(paramSQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      dropTables(paramSQLiteDatabase);
      onCreate(paramSQLiteDatabase);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.provider.PITouchstreamProvider
 * JD-Core Version:    0.7.0.1
 */