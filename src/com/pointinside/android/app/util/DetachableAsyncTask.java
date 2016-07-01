package com.pointinside.android.app.util;

import android.os.AsyncTask;
import android.util.Log;

public abstract class DetachableAsyncTask<Params, Progress, Result>
  extends AsyncTask<Params, Progress, Result>
{
  private static final String TAG = DetachableAsyncTask.class.getSimpleName();
  private TaskCallbacks<Result, Progress> mCallback;

  public void clearCallback()
  {
    this.mCallback = null;
  }

  protected final void onCancelled()
  {
    if (this.mCallback != null) {
      this.mCallback.onCancelled();
    }
  }

  protected final void onPostExecute(Result paramResult)
  {
    if (this.mCallback != null)
    {
      this.mCallback.onPostExecute(paramResult);
      return;
    }
    Log.w(TAG, "Dropping async task result on floor");
  }

  protected final void onPreExecute()
  {
    if (this.mCallback != null) {
      this.mCallback.onPreExecute();
    }
  }

  protected final void onProgressUpdate(Progress... paramVarArgs)
  {
    if (this.mCallback != null) {
      this.mCallback.onProgressUpdate(paramVarArgs);
    }
  }

  public void setCallback(TaskCallbacks<Result, Progress> paramTaskCallbacks)
  {
    this.mCallback = paramTaskCallbacks;
  }

  public static abstract class TaskCallbacks<R, P>
  {
    protected void onCancelled() {}

    protected void onPostExecute(R paramR) {}

    protected void onPreExecute() {}

    protected void onProgressUpdate(P... paramVarArgs) {}
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.DetachableAsyncTask
 * JD-Core Version:    0.7.0.1
 */