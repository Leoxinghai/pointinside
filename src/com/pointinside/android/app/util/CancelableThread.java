package com.pointinside.android.app.util;

public abstract class CancelableThread
  extends Thread
{
  public static final String TAG = "CancelableThread";
  private volatile boolean mCanceled = false;

  public CancelableThread() {}

  public CancelableThread(Runnable paramRunnable)
  {
    super(paramRunnable);
  }

  public CancelableThread(Runnable paramRunnable, String paramString)
  {
    super(paramRunnable, paramString);
  }

  public CancelableThread(String paramString)
  {
    super(paramString);
  }

  public boolean hasCanceled()
  {
    return this.mCanceled;
  }

  public void joinUninterruptibly()
  {
    for (;;)
    {
      try
      {
        join();
        return;
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }

  protected void onRequestCancel() {}

  public void requestCancel()
  {
    if (this.mCanceled) {
      return;
    }
    this.mCanceled = true;
    interrupt();
    onRequestCancel();
  }

  public void requestCancelAndWait()
  {
    requestCancel();
    joinUninterruptibly();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.CancelableThread
 * JD-Core Version:    0.7.0.1
 */