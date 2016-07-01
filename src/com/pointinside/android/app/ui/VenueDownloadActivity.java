package com.pointinside.android.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.PIMapVenue;
import com.pointinside.android.api.PIMapVenue.PIVenueDownloadObserver;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.DetachableAsyncTask;
import com.pointinside.android.app.util.DetachableAsyncTask.TaskCallbacks;

public class VenueDownloadActivity
  extends Activity
{
  public static final int CACHED = 300;
  private static final int DIALOG_NO_CONNECTION = 3;
  private static final int DIALOG_ON_SDCARD_PROMPT = 4;
  private static final int DIALOG_VENUE_UNAVAILABLE = 2;
  private static final int STATUS_CANCEL = 5;
  private static final int STATUS_DOWNLOADING_VENUE = 2;
  private static final int STATUS_FETCHING_VENUE = 1;
  private static final int STATUS_FINISHED = 6;
  private static final int STATUS_INITIATING_VENUE_DOWNLOAD = 4;
  private static final int STATUS_LOADING_VENUE = 3;
  public static final int SUCCESS = 200;
  private boolean mBypassSdcardCheck;
  private ProgressBar mDownloadingProgressBar;
  private boolean mForceRedownload;
  private LoadVenueTask mLoadVenueTask;
  private final DetachableAsyncTask.TaskCallbacks<PIMapVenue, Integer> mLoadVenueTaskCallback = new DetachableAsyncTask.TaskCallbacks<PIMapVenue, Integer>()
  {
    private int mStatus;

    protected void onPostExecute(PIMapVenue pimapvenue)
    {
        mPIMapVenue = pimapvenue;
        switch(mStatus)
        {
        default:
            return;

        case 200:
            loadOrUpdateVenue();
            return;

        case 3: // '\003'
            showDialog(3);
            return;

        case 300:
            setResult(-1);
            finish();
            return;

        case 4: // '\004'
            showDialog(4);
            return;
        }
    }

    protected void onProgressUpdate(Integer... paramAnonymousVarArgs)
    {
      this.mStatus = paramAnonymousVarArgs[0].intValue();
    }
  };
  private PIMapVenue mPIMapVenue;
  private TextView mProgressText;
  private String mVenueUUID;
  private ProgressBar mWorkingProgressBar;

  private void loadOrUpdateVenue()
  {
    final String str = this.mPIMapVenue.getVenueName();
    this.mPIMapVenue.checkForUpdates(new PIMapVenue.PIVenueDownloadObserver()
    {
      private int mBytesCurrent;
      private int mBytesTotal;

      public void bytesToReceive(int paramAnonymousInt)
      {
        super.bytesToReceive(paramAnonymousInt);
        this.mBytesTotal = paramAnonymousInt;
        if ((paramAnonymousInt > 0) && (this.mBytesTotal != this.mBytesCurrent)) {
          VenueDownloadActivity.this.updateProgressStatus(2, str);
        }
        VenueDownloadActivity.this.mDownloadingProgressBar.setMax(paramAnonymousInt);
        VenueDownloadActivity.this.mDownloadingProgressBar.incrementProgressBy(0);
      }

      public void dataReceived(int paramAnonymousInt)
      {
        super.dataReceived(paramAnonymousInt);
        VenueDownloadActivity.this.mDownloadingProgressBar.incrementProgressBy(paramAnonymousInt - this.mBytesCurrent);
        this.mBytesCurrent = paramAnonymousInt;
      }

      public void failedWithError(Exception paramAnonymousException)
      {
        super.failedWithError(paramAnonymousException);
        VenueDownloadActivity.this.updateProgressStatus(6, str);
        VenueDownloadActivity.this.mPIMapVenue.unRegisterPIVenueDownloadObserver();
        VenueDownloadActivity.this.showDialog(2);
      }

      public void venueDidUpdate()
      {
        super.venueDidUpdate();
        VenueDownloadActivity.this.updateProgressStatus(3, str);
      }

      public void venueNeedsUpdate()
      {
        super.venueNeedsUpdate();
        VenueDownloadActivity.this.updateProgressStatus(1, str);
      }

      public void venueReady()
      {
        super.venueReady();
        VenueDownloadActivity.this.mPIMapVenue.unRegisterPIVenueDownloadObserver();
        VenueDownloadActivity.this.updateProgressStatus(6, str);
        VenueDownloadActivity.this.setResult(-1);
        VenueDownloadActivity.this.finish();
      }
    });
  }

  public static void loadVenueForResult(Activity paramActivity, int paramInt, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    Intent localIntent = new Intent(paramActivity, VenueDownloadActivity.class);
    localIntent.putExtra("venue_uuid", paramString);
    localIntent.putExtra("force_redownload", paramBoolean1);
    localIntent.putExtra("bypass_sd", paramBoolean2);
    paramActivity.startActivityForResult(localIntent, paramInt);
  }

  private void tryLoadingVenue(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
   // this.mLoadVenueTask = ((LoadVenueTask)getLastNonConfigurationInstance());
//	  this.mLoadVenueTask =  new LoadVenueTask(param);
    if (this.mLoadVenueTask == null) {
      this.mLoadVenueTask = new LoadVenueTask(paramString, paramBoolean1, paramBoolean2);
    }
    this.mLoadVenueTask.setCallback(this.mLoadVenueTaskCallback);
    if (this.mLoadVenueTask.getStatus() == AsyncTask.Status.PENDING) {
      this.mLoadVenueTask.execute(new Void[0]);
    }
  }

  private void updateProgressStatus(int i, String s)
  {
      switch(i) {
//      JVM INSTR tableswitch 1 6: default 40
  //                   1 67
  //                   2 106
  //                   3 153
  //                   4 40
  //                   5 215
  //                   6 188;
//         goto _L1 _L2 _L3 _L4 _L1 _L5 _L6
default:
      mWorkingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setVisibility(8);
      mProgressText.setText("");
     return;
case 1:
case 4:
      mWorkingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setVisibility(8);
      mProgressText.setText(getString(0x7f060012, new Object[] {
          s
      }));
      return;
case 2:
      mWorkingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setIndeterminate(false);
      mDownloadingProgressBar.setVisibility(0);
      mProgressText.setText(getString(0x7f060013, new Object[] {
          s
      }));
      return;
case 3:
      mWorkingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setIndeterminate(true);
      mProgressText.setText(0x7f060014);
      return;
case 6:
      mWorkingProgressBar.setVisibility(4);
      mDownloadingProgressBar.setVisibility(8);
      mProgressText.setText("");
      return;
case 5:
      mWorkingProgressBar.setVisibility(4);
      mDownloadingProgressBar.setVisibility(8);
      mProgressText.setText("");
      if(mPIMapVenue != null)
      {
          mPIMapVenue.unRegisterPIVenueDownloadObserver();
          return;
      }
   }
 }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903076);
    this.mWorkingProgressBar = ((ProgressBar)findViewById(2131623995));
    this.mDownloadingProgressBar = ((ProgressBar)findViewById(2131623996));
    this.mProgressText = ((TextView)findViewById(2131623997));
    Intent localIntent = getIntent();
    this.mVenueUUID = localIntent.getStringExtra("venue_uuid");
    if (this.mVenueUUID == null) {
      throw new IllegalStateException("A Venue UUID must be provided!");
    }
    this.mForceRedownload = localIntent.getBooleanExtra("force_redownload", false);
    this.mBypassSdcardCheck = localIntent.getBooleanExtra("bypass_sd", false);
    tryLoadingVenue(this.mVenueUUID, this.mForceRedownload, this.mBypassSdcardCheck);
  }

  protected Dialog onCreateDialog(int i)
  {
      switch(i)
      {
      default:
          return null;

      case 3: // '\003'
          return (new android.app.AlertDialog.Builder(this)).setIcon(0x1080027).setTitle(0x7f060015).setMessage(0x7f060009).setPositiveButton(0x7f06000c, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  tryLoadingVenue(mVenueUUID, false, false);
              }

          }
).setNegativeButton(0x7f06000d, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  dialoginterface.dismiss();
                  setResult(0);
                  finish();
              }

          }
).create();

      case 4: // '\004'
          return (new android.app.AlertDialog.Builder(this)).setIcon(0x1080027).setTitle(0x7f060017).setMessage(0x7f060018).setPositiveButton(0x7f06000e, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  tryLoadingVenue(mVenueUUID, true, true);
              }

          }
).setNegativeButton(0x7f06000d, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  dialoginterface.dismiss();
                  setResult(0);
                  finish();
              }

          }
).create();

      case 2: // '\002'
          return (new android.app.AlertDialog.Builder(this)).setIcon(0x1080027).setTitle(0x7f060016).setMessage(0x7f06000b).setPositiveButton(0x7f06000c, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  tryLoadingVenue(mVenueUUID, true, false);
              }

          }
).setNegativeButton(0x7f06000d, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  dialoginterface.dismiss();
                  setResult(0);
                  finish();
              }

          }
).create();
      }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mLoadVenueTask.clearCallback();
  }

  public Object onRetainNonConfigurationInstance()
  {
    return this.mLoadVenueTask;
  }

  private static class LoadVenueTask
    extends DetachableAsyncTask<Void, Integer, PIMapVenue>
  {
    private boolean mByPassSdcardCheck;
    private boolean mRedownload;
    private String mVenueUUID;

    public LoadVenueTask(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mVenueUUID = paramString;
      this.mRedownload = paramBoolean1;
      this.mByPassSdcardCheck = paramBoolean2;
    }

    protected PIMapVenue doInBackground(Void... paramVarArgs)
    {
      PIMapReference localPIMapReference = PointInside.getPIMapReference();
      if (this.mRedownload)
      {
        localPIMapReference.deleteVenueDownload(this.mVenueUUID);
        this.mRedownload = false;
      }
      PIMapVenue localPIMapVenue = PointInside.getInstance().loadVenue(this.mVenueUUID);
      if ((!this.mByPassSdcardCheck) && (localPIMapVenue.isVenueOnSDCard()) && (!localPIMapVenue.isSDCardAvailable()))
      {
        Integer[] arrayOfInteger4 = new Integer[1];
        arrayOfInteger4[0] = Integer.valueOf(4);
        publishProgress(arrayOfInteger4);
        return localPIMapVenue;
      }
      if (!localPIMapReference.isNetworkAvailable())
      {
        if (localPIMapVenue.isLoaded())
        {
          Integer[] arrayOfInteger3 = new Integer[1];
          arrayOfInteger3[0] = Integer.valueOf(300);
          publishProgress(arrayOfInteger3);
          return localPIMapVenue;
        }
        Integer[] arrayOfInteger2 = new Integer[1];
        arrayOfInteger2[0] = Integer.valueOf(3);
        publishProgress(arrayOfInteger2);
        return localPIMapVenue;
      }
      Integer[] arrayOfInteger1 = new Integer[1];
      arrayOfInteger1[0] = Integer.valueOf(200);
      publishProgress(arrayOfInteger1);
      return localPIMapVenue;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.VenueDownloadActivity
 * JD-Core Version:    0.7.0.1
 */