package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.content.Files;
import java.io.File;

public class PIFileDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIFileDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIFileDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIFileDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnDateAdded;
  private int mColumnDateModified;
  private int mColumnDateToken;
  private int mColumnDownloadId;
  private int mColumnFileName;
  private int mColumnFileScanned;
  private int mColumnFileUri;
  private int mColumnVenueUUID;

  private PIFileDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnVenueUUID = paramCursor.getColumnIndex("venue_uuid");
    this.mColumnFileName = paramCursor.getColumnIndex("file_name");
    this.mColumnDownloadId = paramCursor.getColumnIndex("download_id");
    this.mColumnFileUri = paramCursor.getColumnIndex("file_uri");
    this.mColumnDateAdded = paramCursor.getColumnIndex("date_added");
    this.mColumnDateModified = paramCursor.getColumnIndex("date_modified");
    this.mColumnDateToken = paramCursor.getColumnIndex("datetoken");
    this.mColumnFileScanned = paramCursor.getColumnIndex("scanned");
  }

  public static PIFileDataCursor getInstance(Cursor paramCursor)
  {
    return (PIFileDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIFileDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIFileDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public long getDateAdded()
  {
    return this.mCursor.getLong(this.mColumnDateAdded);
  }

  public long getDateModified()
  {
    return this.mCursor.getLong(this.mColumnDateModified);
  }

  public long getDateToken()
  {
    return this.mCursor.getLong(this.mColumnDateToken);
  }

  public long getDownloadId()
  {
    return this.mCursor.getLong(this.mColumnDownloadId);
  }

  public String getFileName()
  {
    return this.mCursor.getString(this.mColumnFileName);
  }

  public File getFilePath()
  {
    return new File(getFileUri());
  }

  public String getFileUri()
  {
    return this.mCursor.getString(this.mColumnFileUri);
  }

  public Uri getUri()
  {
    return Files.getFileUri(getId());
  }

  public String getVenueUUID()
  {
    return this.mCursor.getString(this.mColumnVenueUUID);
  }

  public boolean isFileScanned()
  {
    return this.mCursor.getInt(this.mColumnFileScanned) == 1;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIFileDataCursor
 * JD-Core Version:    0.7.0.1
 */