package com.pointinside.android.app.activity;

import com.pointinside.android.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
//import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.PIMapReference.PIReferenceDownloadObserver;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.net.HolidayGameClient;
import com.pointinside.android.app.net.HolidayGameClient.InfoResponse;
import com.pointinside.android.app.ui.GoogleMapActivity;
import com.pointinside.android.app.util.HolidayGameCache;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import com.pointinside.android.piwebservices.util.BuildUtils;

public class SplashActivity
  extends Activity
{
  private static final int DIALOG_NO_CONNECTION = 2;
  private static final int DIALOG_REFERENCE_UNAVAILABLE = 1;
  private static final int SPLASH_WAIT_TIME = 2000;
  private static final int STATUS_CHECKING_REFERENCE = 5;
  private static final int STATUS_DOWNLOADING_REFERENCE = 2;
  private static final int STATUS_FETCHING_REFERENCE = 1;
  private static final int STATUS_FINISHED = 6;
  private static final int STATUS_INITIATING_REFERENCE_DOWNLOAD = 4;
  private static final int STATUS_LOADING_REFERENCE = 3;
  private static final String TAG = SplashActivity.class.getSimpleName();
  private ProgressBar mDownloadingProgressBar;
  private final Handler mHandler = new Handler();
  private boolean mIsPaused;
  private Runnable mLoadReferenceRunner = new Runnable()
  {
    public void run()
    {
    	SplashActivity.this.loadReference();
    }
  };
  
  private Runnable mMapActivityStarter = new Runnable()
  {
    public void run()
    {
      SplashActivity.this.setProgessStatus(3);
      if (!SplashActivity.this.mIsPaused) {}
      try
      {
        SplashActivity.this.startActivity(new Intent(SplashActivity.this, GoogleMapActivity.class));
        SplashActivity.this.finish();
        return;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          Toast.makeText(SplashActivity.this, "Point Inside requires the google maps library", 1);
        }
      }
    }
  };
  private PIMapReference mPIMapReference;
  private TextView mProgressText;
  private View mProgressView;
  private ProgressBar mWorkingProgressBar;
  private long checkStartTime =0;
  
  private void loadReference()
  {
      setProgessStatus(4);
      showProgress(true);
      boolean flag = mPIMapReference.isLoaded();
      if(mPIMapReference.isNetworkAvailable())
      {
          if(flag)
              setProgessStatus(5);
          else
              setProgessStatus(1);
          checkStartTime = System.currentTimeMillis();
          mPIMapReference.checkForUpdates(new PIMapReference.PIReferenceDownloadObserver() {

              public void bytesToReceive(int i)
              {
                  super.bytesToReceive(i);
                  mBytesTotal = i;
                  if(i > 0 && mBytesTotal != mBytesCurrent)
                      setProgessStatus(2);
                  mDownloadingProgressBar.setMax(i);
              }

              public void dataReceived(int i)
              {
                  super.dataReceived(i);
                  mDownloadingProgressBar.setProgress(i);
                  mBytesCurrent = i;
              }

              public void failedWithError(Exception exception)
              {
                  super.failedWithError(exception);
                  if (!SplashActivity.this.isFinishing())
                  {
                    if (!SplashActivity.this.mPIMapReference.isLoaded()) {
                            SplashActivity.this.showDialog(1);
                            return;
                    }
                    long l = 2000L - (System.currentTimeMillis() - checkStartTime);
                    if (l > 0L)
                    {
                      SplashActivity.this.mHandler.postDelayed(SplashActivity.this.mMapActivityStarter, l);
                      SplashActivity.this.showProgress(false);
                      checkStartTime = l;
                    }
                  }
                  else
                  {
                    return;
                  }
                  SplashActivity.this.mHandler.post(SplashActivity.this.mMapActivityStarter);
                  return;
              }

              public void fileDidUpdate()
              {
                  super.fileDidUpdate();
                  setProgessStatus(3);
              }

              public void fileNeedsUpdate()
              {
                  super.fileNeedsUpdate();
                  setProgessStatus(3);
              }

              public void fileReady()
              {
                  super.fileReady();
                  if (!SplashActivity.this.isFinishing())
                  {
                    SplashActivity.this.setProgessStatus(3);
                    long l = 2000L - (System.currentTimeMillis());
                    if (l > 0L) {
                      SplashActivity.this.mHandler.postDelayed(SplashActivity.this.mMapActivityStarter, l);
                    } else
                        SplashActivity.this.mHandler.post(SplashActivity.this.mMapActivityStarter);
                    	
                  }
                  else
                  {
                     SplashActivity.this.mHandler.post(SplashActivity.this.mMapActivityStarter);
                    return;
                  }

              }

              private int mBytesCurrent;
              private int mBytesTotal;
          }
);
      } else
      {
          if(flag)
          {
              setProgessStatus(3);
              mHandler.postDelayed(mMapActivityStarter, 2000L);
              return;
          }
          showProgress(false);
          if(!isFinishing())
          {
              showDialog(2);
              return;
          }
      }
  }

  
  private void setProgessStatus(int i)
  {
      switch(i) {
//      JVM INSTR tableswitch 1 6: default 40
  //                   1 75
  //                   2 145
  //                   3 180
  //                   4 40
  //                   5 110
  //                   6 215;
//         goto _L1 _L2 _L3 _L4 _L1 _L5 _L6
default:
case 4:
      mWorkingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setIndeterminate(true);
      mProgressText.setText("");
      return;
case 1:
      mWorkingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setIndeterminate(true);
      mProgressText.setText(0x7f060004);
      return;
case 5:
      mWorkingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setIndeterminate(true);
      mProgressText.setText(0x7f060005);
      return;
case 2:
      mWorkingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setIndeterminate(false);
      mProgressText.setText(0x7f060006);
      return;
case 3:
      mWorkingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setVisibility(0);
      mDownloadingProgressBar.setIndeterminate(true);
      mProgressText.setText(0x7f060014);
      return;
case 6:
      showProgress(false);
      mWorkingProgressBar.setVisibility(4);
      mDownloadingProgressBar.setVisibility(8);
      mDownloadingProgressBar.setIndeterminate(true);
      mProgressText.setText("");
      
      }
 }
  
  private void setWhatsNewImage()
  {
    ImageView localImageView = (ImageView)findViewById(2131624006);
    if (localImageView != null)
    {
      HolidayGameCache localHolidayGameCache = HolidayGameCache.getInstance(this);
      if ((!localHolidayGameCache.hasGameState()) || (localHolidayGameCache.shouldShowGame())) {
        localImageView.setImageResource(2130837725);
      }
    }
    else
    {
      return;
    }
    localImageView.setImageDrawable(null);
  }
  
  private void showProgress(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mProgressView.setVisibility(0);
      this.mProgressView.startAnimation(AnimationUtils.loadAnimation(this, 2130968581));
      return;
    }
    this.mProgressView.setVisibility(4);
    this.mProgressView.startAnimation(AnimationUtils.loadAnimation(this, 2130968582));
    setProgessStatus(4);
  }
  
  private void updateHolidayGameState()
  {
    new UpdateGameCacheTask(getApplicationContext()).execute(new Void[0]);
  }
  
  private void updateSession()
  {
    long l1 = PITouchstreamContract.getCurrentSessionId();
    if (l1 == 0L)
    {
      long l3 = PITouchstreamContract.getSessionId();
      PITouchstreamContract.addEventToSession(this, PointInside.getInstance().getUserLocation(), l3, "", 0L, PITouchstreamContract.TouchstreamType.SESSION_START);
      return;
    }
    PITouchstreamContract.schedule(this, l1, 0L);
    PITouchstreamContract.resetSessionId();
    long l2 = PITouchstreamContract.getSessionId();
    PITouchstreamContract.addEventToSession(this, PointInside.getInstance().getUserLocation(), l2, "", 0L, PITouchstreamContract.TouchstreamType.SESSION_START);
  }
  
  public void onBackPressed()
  {
    this.mPIMapReference.cancelDownload();
    finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    
    setContentView(R.layout.splash);
    setWhatsNewImage();
    this.mProgressView = findViewById(R.id.inc_progress);
    this.mWorkingProgressBar = ((ProgressBar)findViewById(R.id.progress_working));
    this.mDownloadingProgressBar = ((ProgressBar)findViewById(R.id.progress_downloading));
    this.mProgressText = ((TextView)findViewById(R.id.txt_progress));
    if (BuildUtils.isDebuggable(this)) {
      ((TextView)findViewById(R.id.splash_terms)).setText("[" + BuildUtils.getAppVersionLabel(this) + "] " + getString(2131099655));
    }
    updateSession();
    this.mPIMapReference = PointInside.getPIMapReference();

    this.mHandler.post(this.mLoadReferenceRunner);
    updateHolidayGameState();
  }
  
  protected Dialog onCreateDialog(int i)
  {
	  switch(i)
      {
      default:
          return null;

      case 2: // '\002'
          return (new android.app.AlertDialog.Builder(this)).setIcon(0x1080027).setTitle(0x7f060015).setMessage(0x7f060008).setPositiveButton(0x7f06000c, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  mHandler.postDelayed(mLoadReferenceRunner, 2000L);
                  showProgress(true);
              }

          }
).setNegativeButton(0x7f06000d, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  dialoginterface.dismiss();
                  finish();
              }

          }
).create();

      case 1: // '\001'
          return (new android.app.AlertDialog.Builder(this)).setIcon(0x1080027).setTitle(0x7f060016).setMessage(0x7f06000a).setPositiveButton(0x7f06000c, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  mHandler.post(mLoadReferenceRunner);
                  showProgress(true);
              }

          }
).setNegativeButton(0x7f06000d, new android.content.DialogInterface.OnClickListener() {

              public void onClick(DialogInterface dialoginterface, int j)
              {
                  dialoginterface.dismiss();
                  finish();
              }

          }
).create();
      }
	  
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    this.mPIMapReference.unRegisterPIReferenceDownloadObserver();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
//    if ((Integer.parseInt(Build.VERSION.SDK) < 5) && (paramInt == 4) && (paramKeyEvent.getRepeatCount() == 0)) {
//      onBackPressed();
//    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onPause()
  {
    super.onPause();
    this.mIsPaused = true;
  }
  
  protected void onResume()
  {
    super.onResume();
    this.mIsPaused = false;
  }
  
  private static class UpdateGameCacheTask
    extends AsyncTask<Void, Void, HolidayGameClient.InfoResponse>
  {
    private final Context mContext;
    
    public UpdateGameCacheTask(Context paramContext)
    {
      this.mContext = paramContext.getApplicationContext();
    }
    
    protected HolidayGameClient.InfoResponse doInBackground(Void... paramVarArgs)
    {
      try
      {
        HolidayGameClient.InfoResponse localInfoResponse = HolidayGameClient.getInstance(this.mContext).globalInfo();
        return localInfoResponse;
      }
      catch (JSONWebRequester.RestResponseException localRestResponseException)
      {
        Log.w(SplashActivity.TAG, "Error fetching game state", localRestResponseException);
      }
      return null;
    }
    
    protected void onPostExecute(HolidayGameClient.InfoResponse paramInfoResponse)
    {
      HolidayGameCache.getInstance(this.mContext).updateGlobalState(paramInfoResponse);
    }
  }
}


