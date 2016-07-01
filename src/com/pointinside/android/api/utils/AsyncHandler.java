package com.pointinside.android.api.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public abstract class AsyncHandler<Params, Result>
{
  private static Looper sLooper;
  private final AsyncHandler<Params, Result>.ReceiverHandler mReceiverHandler;
  private final AsyncHandler<Params, Result>.WorkerHandler mWorkerThreadHandler;

  public AsyncHandler()
  {
    try
    {
      if (sLooper == null)
      {
        HandlerThread localHandlerThread = createWorkerThread();
        localHandlerThread.start();
        sLooper = localHandlerThread.getLooper();
      }
      this.mReceiverHandler = new ReceiverHandler();
      this.mWorkerThreadHandler = createWorkerHandler(sLooper);
      return;
    }
    finally {}
  }

  public void cancelWork(int paramInt)
  {
    this.mWorkerThreadHandler.removeMessages(paramInt);
  }

  protected AsyncHandler<Params, Result>.WorkerHandler createWorkerHandler(Looper paramLooper)
  {
    return new WorkerHandler(paramLooper);
  }

  protected abstract HandlerThread createWorkerThread();

  protected abstract Result doInWorkerThread(int paramInt, Object paramParams);

  protected abstract void onWorkComplete(int paramInt, Result paramResult);

  protected void sendWork(int paramInt, Params paramParams)
  {
    Message localMessage = this.mWorkerThreadHandler.obtainMessage();
    localMessage.what = paramInt;
    localMessage.obj = new WorkerArgs(this.mReceiverHandler, paramParams);
    localMessage.sendToTarget();
  }

  private class ReceiverHandler
    extends Handler
  {
    private ReceiverHandler() {}

    public void handleMessage(Message paramMessage)
    {
      AsyncHandler.this.onWorkComplete(paramMessage.what, (Result)paramMessage.obj);
    }
  }

  private static class WorkerArgs<Object>
  {
    public Object params;
    public Handler receiver;

    public WorkerArgs(Handler paramHandler, Object paramP)
    {
      this.receiver = paramHandler;
      this.params = paramP;
    }
  }

  protected class WorkerHandler
    extends Handler
  {
    public WorkerHandler(Looper paramLooper)
    {
      super();
    }

    public void handleMessage(Message paramMessage)
    {
      int i = paramMessage.what;
      AsyncHandler.WorkerArgs localWorkerArgs = (AsyncHandler.WorkerArgs)paramMessage.obj;
      Object localObject = AsyncHandler.this.doInWorkerThread(i, localWorkerArgs.params);
      Message localMessage = localWorkerArgs.receiver.obtainMessage();
      localMessage.what = i;
      localMessage.obj = localObject;
      localMessage.sendToTarget();
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.AsyncHandler
 * JD-Core Version:    0.7.0.1
 */