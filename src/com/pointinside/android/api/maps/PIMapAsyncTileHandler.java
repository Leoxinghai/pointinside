package com.pointinside.android.api.maps;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.pointinside.android.api.content.PIContentManager;

import java.io.IOException;

abstract class PIMapAsyncTileHandler
  extends Handler
{
  private static final boolean DEBUG_MESSAGES = false;
  static final String TAG = "PIMapAsyncTileLoader";
  private static Looper sLooper;
  private boolean mCancel = true;
  private int mCurrentSerial = 0;
  private Uri mCurrentUri;
  private HandlerThread mThread;
  private final WorkerHandler mWorkerThreadHandler;

  public PIMapAsyncTileHandler()
  {
    try
    {
      if (sLooper == null)
      {
        this.mThread = new HandlerThread("PIMapAsyncTileHandler", 10);
        this.mThread.start();
        sLooper = this.mThread.getLooper();
      }
      this.mWorkerThreadHandler = new WorkerHandler(sLooper);
      return;
    }
    finally {}
  }

  public void cancelOperation(int paramInt)
  {
    this.mWorkerThreadHandler.removeMessages(paramInt);
  }

  public void cancelOperations()
  {
    this.mWorkerThreadHandler.removeCallbacksAndMessages(null);
  }

  public void handleMessage(Message message)
  {
      WorkerArgs workerargs;
      int i;
      Uri uri;
      Bitmap bitmap;
      workerargs = (WorkerArgs)message.obj;
      i = message.what;
      int j = message.arg1;
      uri = workerargs.uri;
      if(workerargs.result != null)
          bitmap = (Bitmap)workerargs.result;
      else
          bitmap = null;
      if(bitmap == null)
          j = workerargs.error;


      switch(j) {
      case 0:
          if(workerargs.serial == mCurrentSerial)
          {
              mCurrentUri = null;
              onDecodeComplete(i, uri, bitmap);
              return;
          }
          if(bitmap != null)
          {
              bitmap.recycle();
              return;
          }
          break;
      case 6:
          onOOM();
          return;
      default:

      }
      return;
  }

  protected abstract void onDecodeComplete(int paramInt, Uri paramUri, Bitmap paramBitmap);

  protected abstract void onOOM();

  public void startDecode(int paramInt, Uri paramUri)
  {
    Message localMessage = this.mWorkerThreadHandler.obtainMessage(paramInt, 0, 0);
    if ((paramUri != null) && (!paramUri.equals(this.mCurrentUri)))
    {
      this.mCurrentSerial = (1 + this.mCurrentSerial);
      this.mCurrentUri = paramUri;
      WorkerArgs localWorkerArgs = new WorkerArgs(this);
      localWorkerArgs.uri = paramUri;
      localWorkerArgs.serial = this.mCurrentSerial;
      localMessage.obj = localWorkerArgs;
      localMessage.sendToTarget();
    }
  }

  private static class WorkerArgs
  {
    public int error;
    public Handler handler;
    public Object result;
    public int serial;
    public Uri uri;

    public WorkerArgs(Handler paramHandler)
    {
      this.handler = paramHandler;
    }
  }

  private class WorkerHandler
    extends Handler
  {
    private static final int MSG_DECODE_URI = 0;
    private static final int MSG_OOM = 6;

    public WorkerHandler(Looper paramLooper)
    {
      super();
    }

    public void handleMessage(Message message)
    {
        WorkerArgs workerargs;
        int i;
        int j;
        workerargs = (WorkerArgs)message.obj;
        i = message.what;
        j = message.arg1;
        switch(j) {
        case 0:
            if(workerargs.uri != null)
                try
                {
                    workerargs.result = PIContentManager.decodeUri(workerargs.uri);
                }
                catch(OutOfMemoryError outofmemoryerror)
                {
                    workerargs.error = 6;
                }
                catch(IOException ioexception) { }
            break;
            default:
        }
        Message message1 = workerargs.handler.obtainMessage(i, j, 0);
        message1.obj = workerargs;
        message1.sendToTarget();
        return;
    }
  }
}


