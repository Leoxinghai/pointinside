package com.pointinside.android.api.content;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.PIMapReference.NotInitializedException;
import com.pointinside.android.api.dao.PIDownloadDataCursor;
import java.io.File;
import java.util.ArrayList;

public class DownloadService
{
  private static final DownloadService mInstance = new DownloadService();
  private Context mContext;
  private final ArrayList<DownloadInfo> mDownloads = new ArrayList();
  private final Handler mHandler = new Handler();
  private volatile boolean mIsRunning = false;
  private CharArrayBuffer mNewChars;
  private DownloadManagerContentObserver mObserver;
  private PIContentStore mPIContentStore;
  private boolean mPendingUpdate;
  private volatile boolean mStopService = false;
  private UpdateThread mUpdateThread;
  private CharArrayBuffer oldChars;

  private void deleteDownload(int paramInt)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    if (localDownloadInfo.mStatus == 192) {
      localDownloadInfo.mStatus = 490;
    }
    if (localDownloadInfo.mFileName != null) {
        new File(localDownloadInfo.mFileName).delete();
      }
    this.mDownloads.remove(paramInt);
    return;
  }

  private boolean extractFile(PIDownloadDataCursor pidownloaddatacursor, int i)
  {
      try {
		  boolean flag;
	      flag = false;
	      if(pidownloaddatacursor == null)
	          return flag;
	      flag = Files.extractDownload(mContext, pidownloaddatacursor);
	      if(!flag) {
	          ContentValues contentvalues1 = new ContentValues();
	          contentvalues1.put("extracted", Integer.valueOf(3));
	          mPIContentStore.update(pidownloaddatacursor.getUri(), contentvalues1, null, null);
	          return flag;
	      }          
	      ContentValues contentvalues = new ContentValues();
	      contentvalues.put("extracted", Integer.valueOf(1));
	      mPIContentStore.update(pidownloaddatacursor.getUri(), contentvalues, null, null);
	      return flag;
      } catch(Exception exception) {
    	  exception.printStackTrace();
    	  return false;
      }
  }



  public static DownloadService getInstance()
  {
    return mInstance;
  }

  private void insertDownload(PIDownloadDataCursor pidownloaddatacursor, int keepService, boolean flag, boolean flag1, long l)
  {
      DownloadInfo downloadinfo;
      downloadinfo = new DownloadInfo(pidownloaddatacursor);
      mDownloads.add(keepService, downloadinfo);
      if(downloadinfo.canUseNetwork(flag, flag1)) {

    	  if(downloadinfo.isReadyToStart(l) && !downloadinfo.mHasActiveThread) {
    		  return;
    	  } else {
		      if(downloadinfo.mStatus != 192)
		      {
		          downloadinfo.mStatus = 192;
		          ContentValues contentvalues1 = new ContentValues();
		          contentvalues1.put("status", Integer.valueOf(downloadinfo.mStatus));
		          mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, downloadinfo.mId), contentvalues1, null, null);
		      }
		      DownloadThread downloadthread = new DownloadThread(mContext, downloadinfo);
		      downloadinfo.mHasActiveThread = true;
		      downloadthread.start();
    	  }
    	  return;
      } else {
	      if(downloadinfo.mStatus == 0 || downloadinfo.mStatus == 190 || downloadinfo.mStatus == 192)
	      {
	          downloadinfo.mStatus = 193;
	          android.net.Uri uri = ContentUris.withAppendedId(Downloads.CONTENT_URI, downloadinfo.mId);
	          ContentValues contentvalues = new ContentValues();
	          contentvalues.put("status", Integer.valueOf(193));
	          mPIContentStore.update(uri, contentvalues, null, null);
	          return;
	      }
      }
  }

  private long nextAction(int keepService, long l)
  {
      long wakeUp = 0L;
      DownloadInfo downloadinfo = (DownloadInfo)mDownloads.get(keepService);
      if(Downloads.isStatusCompleted(downloadinfo.mStatus))
          wakeUp = -1L;
      else
      if(downloadinfo.mStatus == 193 && downloadinfo.mNumFailed != 0)
      {
          long now = downloadinfo.restartTime();
          if(now > l)
              return now - l;
      }
      return wakeUp;
  }


  private boolean shouldExtractFile(int paramInt)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    return (!localDownloadInfo.mFilePurged) && (!localDownloadInfo.mFileExtracted) && (Downloads.isStatusSuccess(localDownloadInfo.mStatus));
  }

  private void stopSelf()
  {
    this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
    this.mDownloads.clear();
    this.mIsRunning = false;
  }

  private void updateDownload(PIDownloadDataCursor paramPIDownloadDataCursor, int paramInt, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
  {
    DownloadInfo localDownloadInfo = (DownloadInfo)this.mDownloads.get(paramInt);
    localDownloadInfo.populate(paramPIDownloadDataCursor);
    if ((!localDownloadInfo.canUseNetwork(paramBoolean1, paramBoolean2)) || (!localDownloadInfo.isReadyToRestart(paramLong)) || (localDownloadInfo.mHasActiveThread)) {
      return;
    }
    localDownloadInfo.mStatus = 192;
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("status", Integer.valueOf(localDownloadInfo.mStatus));
    this.mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, localDownloadInfo.mId), localContentValues, null, null);
    DownloadThread localDownloadThread = new DownloadThread(this.mContext, localDownloadInfo);
    localDownloadInfo.mHasActiveThread = true;
    localDownloadThread.start();
  }

  private void updateFromProvider()
  {
    try
    {
      this.mPendingUpdate = true;
      if ((this.mUpdateThread == null) && (this.mContext != null))
      {
        this.mUpdateThread = new UpdateThread();
        this.mUpdateThread.start();
      }
      return;
    }
    finally {}
  }

  public void start()
  {
    try
    {
      if (!this.mIsRunning)
      {
        this.mIsRunning = true;
        this.mStopService = false;
        this.mContext = PIMapReference.getInstance().getContext();
        this.mPIContentStore = PIContentStore.getInstance(this.mContext);
        this.mObserver = new DownloadManagerContentObserver();
        this.mContext.getContentResolver().registerContentObserver(Downloads.CONTENT_URI, true, this.mObserver);
        updateFromProvider();
      }
      return;
    }
    catch (PIMapReference.NotInitializedException localNotInitializedException)
    {
      this.mIsRunning = false;
      this.mStopService = true;
    }
  }

  public void stop()
  {
    this.mStopService = true;
  }

  private class DownloadManagerContentObserver
    extends ContentObserver
  {
    public DownloadManagerContentObserver()
    {
        super(new Handler());
    }

    public void onChange(boolean paramBoolean)
    {
      DownloadService.this.updateFromProvider();
    }
  }

  private class UpdateThread
    extends Thread
  {
    private volatile boolean forceStop = false;

    public UpdateThread()
    {
      super();
    }

    private void doUpdateLoop()
    {
      long wakeUp = Long.MAX_VALUE;//9223372036854775807L;
      boolean networkAvailable;
      boolean networkRoaming;
      long now;
      PIDownloadDataCursor localPIDownloadDataCursor = null;
      int arrayPos;
      boolean isAfterLast;
      long next;
      boolean keepService = false;
      AlarmManager localAlarmManager = null;
//      localAlarmManager = (AlarmManager)DownloadService.this.mContext.getSystemService("alarm");
      for (;;)
      {
        synchronized (DownloadService.this)
        {
          if (mUpdateThread != this) {
        	 throw new IllegalStateException("multiple UpdateThreads in DownloadService");
            //return;
          }
          if (!mPendingUpdate) {
	          mUpdateThread = null;
	          if (!keepService) {
	            stopSelf();
	          }
	          if (wakeUp != Long.MAX_VALUE)
	          {
	            localAlarmManager = (AlarmManager)DownloadService.this.mContext.getSystemService("alarm");
	            if (localAlarmManager == null) {
	              Log.e("PIMaps", "couldn't get alarm manager");
	            } else {
		            Log.e("PIMaps", "scheduling retry in "+ wakeUp+"ms");

	    	        Intent localIntent = new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP");
	    	        localIntent.setClassName(DownloadReceiver.class.getPackage().getName(), DownloadReceiver.class.getName());
	    	        localAlarmManager.set(0, wakeUp + System.currentTimeMillis(), PendingIntent.getBroadcast(DownloadService.this.mContext, 0, localIntent, PendingIntent.FLAG_ONE_SHOT));
	            }
	          }
	            oldChars = null;
	            mNewChars = null;
	            localPIDownloadDataCursor.close();
	            return;
	      }
          mPendingUpdate = false;
            //continue;
        }


        networkAvailable = ContentManagerUtils.isNetworkAvailable(DownloadService.this.mContext);
        networkRoaming = ContentManagerUtils.isNetworkRoaming(DownloadService.this.mContext);
        now = System.currentTimeMillis();

        Cursor localCursor = mPIContentStore.query(Downloads.CONTENT_URI, null, null, null, "_id");
        if(localCursor == null) {
        	return;
        }
        if (!localCursor.moveToFirst())
        {
          localCursor.close();
          return;
        }

        localPIDownloadDataCursor = PIDownloadDataCursor.getInstance(localCursor);

        if (localPIDownloadDataCursor == null) {
            localCursor.close();
            return;
        }
        arrayPos = 0;
        keepService = false;
        wakeUp = Long.MAX_VALUE;
        try
        {
          isAfterLast = localPIDownloadDataCursor.isAfterLast();
          long id;
          long arrayId;
          while (!isAfterLast || arrayPos < mDownloads.size())
          {
            if (isAfterLast)
            {
	              if (arrayPos >= mDownloads.size())
	              {
	                localPIDownloadDataCursor.close();
	                break;
	              }
	              if (shouldExtractFile(arrayPos)) {
	                extractFile(null, arrayPos);
	              }
	              deleteDownload(arrayPos);
	              if(this.forceStop)
	              {
	                stopSelf();
	              }
	              continue;
            }

            id = localPIDownloadDataCursor.getId();
            if (arrayPos == mDownloads.size())
            {
                insertDownload(localPIDownloadDataCursor, arrayPos, networkAvailable, networkRoaming, now);
                if ((shouldExtractFile(arrayPos)) && (!extractFile(localPIDownloadDataCursor, arrayPos))) {
                  keepService = true;
                }
                next = nextAction(arrayPos, now);
                if (next == 0) {
                	keepService = true;
                } else if (next > 0 && next < wakeUp) {
                    wakeUp = next;
                }
                arrayPos++;
                localPIDownloadDataCursor.moveToNext();
                isAfterLast = localPIDownloadDataCursor.isAfterLast();
            }
            else
            {
                arrayId = ((DownloadInfo)mDownloads.get(arrayPos)).mId;
                if (arrayId == id) {
                        updateDownload(localPIDownloadDataCursor, arrayPos, networkAvailable, networkRoaming, now);
                        if ((shouldExtractFile(arrayPos)) && (!extractFile(localPIDownloadDataCursor, arrayPos))) {
                          keepService = true;
                        }
                        long next2 = nextAction(arrayPos, now);
                        if (next2 == 0L) {
                            keepService = true;
                          } else if(next2 >0 && next2 < wakeUp){
                          	wakeUp = next2;
                          }
                        arrayPos++;
                        localPIDownloadDataCursor.moveToNext();
                        isAfterLast = localPIDownloadDataCursor.isAfterLast();
                } else if (arrayId > id) {
                        insertDownload(localPIDownloadDataCursor, arrayPos, networkAvailable, networkRoaming, now);
                        if ((shouldExtractFile(arrayPos)) && (!extractFile(localPIDownloadDataCursor, arrayPos))) {
                          keepService = true;
                        }
                        long next2 = nextAction(arrayPos, now);
                        if (next2 == 0L) {
                          keepService = true;
                        } else if(next2 >0 && next2 < wakeUp){
                        	wakeUp = next2;
                        }
                        arrayPos++;
                        localPIDownloadDataCursor.moveToNext();
                        isAfterLast = localPIDownloadDataCursor.isAfterLast();
                } else if(arrayId < id ){
                    if (DownloadService.this.shouldExtractFile(arrayPos)) {
                        DownloadService.this.extractFile(null, arrayPos);
                      }
                    DownloadService.this.deleteDownload(arrayPos);
                }
              }
            }
          Intent localIntent = new Intent("pointinside.intent.action.DOWNLOAD_WAKEUP");
          localIntent.setClassName(DownloadReceiver.class.getPackage().getName(), DownloadReceiver.class.getName());

          if(localAlarmManager != null)
        	  localAlarmManager.set(0, wakeUp + System.currentTimeMillis(), PendingIntent.getBroadcast(DownloadService.this.mContext, 0, localIntent, 1073741824));

        } finally
        {
            System.out.println("downloads." + mDownloads.size());
            return;
        }
      }

    }

    public void run()
    {
      Process.setThreadPriority(10);
      doUpdateLoop();
      synchronized (DownloadService.this)
      {
        DownloadService.this.mUpdateThread = null;
        return;
      }
    }
  }
}

