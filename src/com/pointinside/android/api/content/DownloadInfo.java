package com.pointinside.android.api.content;

import com.pointinside.android.api.dao.PIDownloadDataCursor;

public class DownloadInfo
{
  int mControl;
  int mCurrentBytes;
  boolean mFileExtracted;
  String mFileName;
  boolean mFilePurged;
  volatile boolean mHasActiveThread;
  long mId;
  String mIdentifier;
  long mLastMod;
  String mNewMD5;
  int mNumFailed;
  int mRedirectCount;
  String mRemoteUri;
  int mRetryAfter;
  int mStatus;
  int mTotalBytes;
  PIContentManager.ContentType mType;
  String mVenueUUID;

  String mUri;

  public DownloadInfo(long paramLong1, String paramString1, String paramString2, PIContentManager.ContentType paramContentType, String paramString3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong2, int paramInt6, int paramInt7, String paramString4, String paramString5, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mId = paramLong1;
    this.mVenueUUID = paramString1;
    this.mRemoteUri = paramString2;
    this.mType = paramContentType;
    this.mFileName = paramString3;
    try
    {
      this.mControl = paramInt1;
      this.mStatus = paramInt2;
      this.mNumFailed = paramInt3;
      this.mRetryAfter = paramInt4;
      this.mRedirectCount = paramInt5;
      this.mLastMod = paramLong2;
      this.mTotalBytes = paramInt6;
      this.mCurrentBytes = paramInt7;
      this.mIdentifier = paramString4;
      this.mNewMD5 = paramString5;
      this.mFilePurged = paramBoolean1;
      this.mFileExtracted = paramBoolean2;
      return;
    }
    finally {}
  }

  public DownloadInfo(PIDownloadDataCursor paramPIDownloadDataCursor)
  {
    populate(paramPIDownloadDataCursor);
  }

  public boolean canUseNetwork(boolean paramBoolean1, boolean paramBoolean2)
  {
    return paramBoolean1;
  }

  public boolean isReadyToRestart(long paramLong)
  {
    if (this.mControl == 1) {}
      if (this.mStatus == 0) {
        return true;
      }
      if (this.mStatus == 190) {
        return true;
      }
      if (this.mStatus == 193) {
          return true;
      }
    return false;
  }

  public boolean isReadyToStart(long paramLong)
  {
    if (this.mControl == 1) {}
    if(restartTime() >= paramLong) {
    	if (this.mStatus == 0) {
          return true;
        }
        if (this.mStatus == 190) {
          return true;
        }
        if (this.mStatus == 192) {
          return true;
        }
        if (this.mStatus == 193) {
            return true;
        }
      if (this.mNumFailed == 0) {
        return true;
      }
      return false;
    }
    return false;
  }

  public void populate(PIDownloadDataCursor paramPIDownloadDataCursor)
  {
    if (paramPIDownloadDataCursor == null) {
      throw new IllegalArgumentException("The PIDownloadDAOItem is null.");
    }
    this.mId = paramPIDownloadDataCursor.getId();
    this.mVenueUUID = paramPIDownloadDataCursor.getVenueUUID();
    this.mRemoteUri = paramPIDownloadDataCursor.getRemoteUri();
    this.mType = paramPIDownloadDataCursor.getDownloadType();
    this.mFileName = paramPIDownloadDataCursor.getFileName();
    try
    {
      this.mControl = paramPIDownloadDataCursor.getControl();
      this.mStatus = paramPIDownloadDataCursor.getStatus();
      this.mNumFailed = paramPIDownloadDataCursor.getNumFailed();
      this.mRetryAfter = (0xFFFFFFF & paramPIDownloadDataCursor.getRetryAfter());
      this.mRedirectCount = (this.mRetryAfter >> 28);
      this.mLastMod = paramPIDownloadDataCursor.getLastMod();
      this.mTotalBytes = paramPIDownloadDataCursor.getTotalBytes();
      this.mCurrentBytes = paramPIDownloadDataCursor.getCurrentBytes();
      this.mIdentifier = paramPIDownloadDataCursor.getIdentifier();
      this.mNewMD5 = paramPIDownloadDataCursor.getUpdatedIdentifier();
      this.mFilePurged = paramPIDownloadDataCursor.isFilePurged();
      this.mFileExtracted = paramPIDownloadDataCursor.isFileExtracted();
      return;
    }
    finally {}
  }

  public long restartTime()
  {
    if (this.mRetryAfter > 0) {
      return this.mLastMod + this.mRetryAfter;
    }
    return this.mLastMod + 2000 * (1 << -1 + this.mNumFailed);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.DownloadInfo
 * JD-Core Version:    0.7.0.1
 */