package com.pointinside.android.api.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.BaseColumns;
import com.pointinside.android.api.dao.PIDownloadDataCursor;
import java.io.File;
import java.io.FileInputStream;

public class Files
{
  static final Uri CONTENT_URI = Uri.parse("content://com.pointinside.android.api.content/files");
  static final String STORE = "files";

  static boolean extractDownload(Context paramContext, PIDownloadDataCursor paramPIDownloadDataCursor)
  {
    boolean bool1 = true;
    if (paramPIDownloadDataCursor != null) {}
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramPIDownloadDataCursor.getFileName());
      File localFile = ContentManagerUtils.getStorageDirectory(paramContext, paramPIDownloadDataCursor.getVenueUUID(), paramPIDownloadDataCursor.getDownloadType());
      boolean bool2 = ContentManagerUtils.unzipFile(paramContext, paramPIDownloadDataCursor.getVenueUUID(), paramPIDownloadDataCursor.getId(), paramPIDownloadDataCursor.getDownloadType(), localFileInputStream, localFile);
      bool1 = bool2;
      return bool1;
    }
    catch (Exception localException1) {}
    try
    {
      ContentManagerUtils.deleteDir(new File(paramPIDownloadDataCursor.getFileName()));
      label75:
      PIContentManager.deleteDownloadItem(paramContext, paramPIDownloadDataCursor.getUri());
      return false;
    }
    catch (Exception localException2)
    {
      return false;
    }
  }

  static File getFile(Context paramContext, String paramString)
  {
    Cursor localCursor = PIContentStore.getInstance(paramContext).query(CONTENT_URI, new String[] { "file_uri" }, "file_name=?", new String[] { paramString }, null);
    File localFile = null;
    if (localCursor != null) {}
    try
    {
      if (localCursor.moveToFirst())
      {
        String str = localCursor.getString(localCursor.getColumnIndex("file_uri"));
        if (str != null)
        {
          localFile = new File(str);
          return localFile;
        }
      }
    }
    finally
    {
      localCursor.close();
    }
    localCursor.close();
    return null;
  }

  public static Uri getFileUri(long paramLong)
  {
    return CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static Uri getZoneImagesUriForVenue(String paramString)
  {
    return CONTENT_URI.buildUpon().appendPath("venue").appendPath(paramString).appendPath("zone_images").build();
  }

  public static abstract interface FileColumns
    extends BaseColumns
  {
    public static final String COLUMN_CONTENT_TYPE = "content_type";
    public static final String COLUMN_DATE_ADDED = "date_added";
    public static final String COLUMN_DATE_MODIFIED = "date_modified";
    public static final String COLUMN_DATE_TOKEN = "datetoken";
    public static final String COLUMN_DOWNLOAD_ID = "download_id";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_FILE_SCANNED = "scanned";
    public static final String COLUMN_FILE_SIZE = "size";
    public static final String COLUMN_FILE_URI = "file_uri";
    public static final String COLUMN_FILE_VENUE_UUID = "venue_uuid";
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.Files
 * JD-Core Version:    0.7.0.1
 */