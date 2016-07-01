package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.net.Uri;
import com.pointinside.android.api.content.Downloads;
import com.pointinside.android.api.content.PIContentManager;

public class PIDownloadDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIDownloadDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIDownloadDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIDownloadDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnControl;
  private int mColumnCurrentBytes;
  private int mColumnFileExtracted;
  private int mColumnFileName;
  private int mColumnFilePurged;
  private int mColumnIdentifier;
  private int mColumnLastMod;
  private int mColumnNumFailed;
  private int mColumnRemoteUri;
  private int mColumnRetryAfter;
  private int mColumnStatus;
  private int mColumnTotalBytes;
  private int mColumnType;
  private int mColumnUpdatedIdentifier;
  private int mColumnVenueUUID;

  private PIDownloadDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnVenueUUID = paramCursor.getColumnIndex("venue_uuid");
    this.mColumnFileName = paramCursor.getColumnIndex("file_name");
    this.mColumnRemoteUri = paramCursor.getColumnIndex("remote_uri");
    this.mColumnType = paramCursor.getColumnIndex("content_type");
    this.mColumnControl = paramCursor.getColumnIndex("control");
    this.mColumnStatus = paramCursor.getColumnIndex("status");
    this.mColumnNumFailed = paramCursor.getColumnIndex("numfailed");
    this.mColumnRetryAfter = paramCursor.getColumnIndex("retry_after");
    this.mColumnLastMod = paramCursor.getColumnIndex("lastmod");
    this.mColumnTotalBytes = paramCursor.getColumnIndex("total_bytes");
    this.mColumnCurrentBytes = paramCursor.getColumnIndex("current_bytes");
    this.mColumnFilePurged = paramCursor.getColumnIndex("purged");
    this.mColumnFileExtracted = paramCursor.getColumnIndex("extracted");
    this.mColumnIdentifier = paramCursor.getColumnIndex("download_identifier");
    this.mColumnUpdatedIdentifier = paramCursor.getColumnIndex("updated_identifier");
  }

  public static PIDownloadDataCursor getInstance(Cursor paramCursor)
  {
    return (PIDownloadDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIDownloadDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIDownloadDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public int getControl()
  {
    return this.mCursor.getInt(this.mColumnControl);
  }

  public int getCurrentBytes()
  {
    return this.mCursor.getInt(this.mColumnCurrentBytes);
  }

  public PIContentManager.ContentType getDownloadType()
  {
    return PIContentManager.ContentType.values()[this.mCursor.getInt(this.mColumnType)];
  }

  public String getFileName()
  {
    return this.mCursor.getString(this.mColumnFileName);
  }

  public String getIdentifier()
  {
    return this.mCursor.getString(this.mColumnIdentifier);
  }

  public long getLastMod()
  {
    return this.mCursor.getLong(this.mColumnLastMod);
  }

  public int getNumFailed()
  {
    return this.mCursor.getInt(this.mColumnNumFailed);
  }

  public PIMapDownload getPIMapDownload()
  {
    return new PIMapDownload(getId(), getVenueUUID(), getFileName(), getUri(), getRemoteUri(), getDownloadType(), getControl(), getStatus(), getNumFailed(), getRetryAfter(), getLastMod(), getTotalBytes(), getCurrentBytes(), isFilePurged(), isFileExtracted(), isExtractionError(), getIdentifier(), getUpdatedIdentifier());
  }

  public String getRemoteUri()
  {
    return this.mCursor.getString(this.mColumnRemoteUri);
  }

  public int getRetryAfter()
  {
    return this.mCursor.getInt(this.mColumnRetryAfter);
  }

  public int getStatus()
  {
    return this.mCursor.getInt(this.mColumnStatus);
  }

  public int getTotalBytes()
  {
    return this.mCursor.getInt(this.mColumnTotalBytes);
  }

  public String getUpdatedIdentifier()
  {
    return this.mCursor.getString(this.mColumnUpdatedIdentifier);
  }

  public Uri getUri()
  {
    return Downloads.getDownloadUri(getId());
  }

  public String getVenueUUID()
  {
    return this.mCursor.getString(this.mColumnVenueUUID);
  }

  public boolean isExtractionError()
  {
    return this.mCursor.getInt(this.mColumnFileExtracted) == 3;
  }

  public boolean isFileExtracted()
  {
    return this.mCursor.getInt(this.mColumnFileExtracted) == 1;
  }

  public boolean isFilePurged()
  {
    return this.mCursor.getInt(this.mColumnFilePurged) == 1;
  }

  public static class PIMapDownload
  {
    private int mControl;
    private int mCurrentBytes;
    private boolean mExtractionError;
    private boolean mFileExtracted;
    private String mFileName;
    private boolean mFilePurged;
    private long mId;
    private String mIdentifier;
    private long mLastMod;
    private int mNumFailed;
    private String mRemoteUri;
    private int mRetryAfter;
    private int mStatus;
    private int mTotalBytes;
    private PIContentManager.ContentType mType;
    private String mUpdatedIdentifier;
    private Uri mUri;
    private String mVenueUUID;

    public PIMapDownload(long paramLong1, String paramString1, String paramString2, Uri paramUri, String paramString3, PIContentManager.ContentType paramContentType, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong2, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString4, String paramString5)
    {
      this.mId = paramLong1;
      this.mVenueUUID = paramString1;
      this.mFileName = paramString2;
      this.mUri = paramUri;
      this.mRemoteUri = paramString3;
      this.mType = paramContentType;
      this.mControl = paramInt1;
      this.mStatus = paramInt2;
      this.mNumFailed = paramInt3;
      this.mRetryAfter = paramInt4;
      this.mLastMod = paramLong2;
      this.mTotalBytes = paramInt5;
      this.mCurrentBytes = paramInt6;
      this.mFilePurged = paramBoolean1;
      this.mFileExtracted = paramBoolean2;
      this.mExtractionError = paramBoolean3;
      this.mIdentifier = paramString4;
      this.mUpdatedIdentifier = paramString5;
    }

    public int getControl()
    {
      return this.mControl;
    }

    public int getCurrentBytes()
    {
      return this.mCurrentBytes;
    }

    public PIContentManager.ContentType getDownloadType()
    {
      return this.mType;
    }

    public String getFileName()
    {
      return this.mFileName;
    }

    public long getId()
    {
      return this.mId;
    }

    public String getIdentifier()
    {
      return this.mIdentifier;
    }

    public long getLastMod()
    {
      return this.mLastMod;
    }

    public int getNumFailed()
    {
      return this.mNumFailed;
    }

    public String getRemoteUri()
    {
      return this.mRemoteUri;
    }

    public int getRetryAfter()
    {
      return this.mRetryAfter;
    }

    public int getStatus()
    {
      return this.mStatus;
    }

    public int getTotalBytes()
    {
      return this.mTotalBytes;
    }

    public String getUpdatedIdentifier()
    {
      return this.mUpdatedIdentifier;
    }

    public Uri getUri()
    {
      return this.mUri;
    }

    public String getVenueUUID()
    {
      return this.mVenueUUID;
    }

    public boolean isExtractionError()
    {
      return this.mExtractionError;
    }

    public boolean isFileExtracted()
    {
      return this.mFileExtracted;
    }

    public boolean isFilePurged()
    {
      return this.mFilePurged;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIDownloadDataCursor
 * JD-Core Version:    0.7.0.1
 */