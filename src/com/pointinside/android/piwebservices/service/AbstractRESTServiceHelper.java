package com.pointinside.android.piwebservices.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Log;
import com.pointinside.android.api.net.JSONWebRequester;
import java.io.PrintStream;
import java.util.HashMap;

public abstract class AbstractRESTServiceHelper
  extends IntentService
{
  private static final String ACTION_CLEANUP = "com.pointinside.android.app.service.AbstractRESTServiceHelper.CLEANUP";
  public static final String EXTRA_ERROR_TEXT = "error-text";
  public static final String EXTRA_EXTRAS = "extras";
  public static final String EXTRA_RESULT_RECEIVER = "result-receiver";
  public static final String EXTRA_RESULT_URI = "result-uri";
  private static final int POST_METHOD_CLEANUP_DELAY = 60000;
  public static final int SUGGESTED_MAX_RECORD_AGE = 172800000;
  private final Handler mHandler = new Handler();
  private final Runnable mInitiateCleanup = new Runnable()
  {
    public void run()
    {
      AbstractRESTServiceHelper localAbstractRESTServiceHelper = AbstractRESTServiceHelper.this;
      Intent localIntent = new Intent("com.pointinside.android.app.service.AbstractRESTServiceHelper.CLEANUP", null, localAbstractRESTServiceHelper, localAbstractRESTServiceHelper.getClass());
      System.out.println("intent=" + localIntent);
      AbstractRESTServiceHelper.this.startService(localIntent);
    }
  };
  private final HashMap<String, MethodHandlerContainer> mMethodHandlers = new HashMap();
  private final String mTag;

  public AbstractRESTServiceHelper(String paramString)
  {
    super(paramString);
    this.mTag = paramString;
  }

  private void runRegisteredMethod(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    ResultReceiver localResultReceiver = (ResultReceiver)paramIntent.getParcelableExtra("result-receiver");
    Bundle localBundle = createResultData(paramIntent);
    MethodHandlerContainer localMethodHandlerContainer = (MethodHandlerContainer)this.mMethodHandlers.get(str);
    int i = localMethodHandlerContainer.resultCode;
    MethodHandler localMethodHandler = localMethodHandlerContainer.handler;
    if (localMethodHandler != null) {
      try
      {
        localMethodHandler.executeMethod(i, paramIntent, localBundle);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        return;
      }
      catch (OperationApplicationException localOperationApplicationException)
      {
        Log.d(this.mTag, str + ": applying results error", localOperationApplicationException);
        if (localResultReceiver != null)
        {
          localBundle.putString("error-text", localOperationApplicationException.getMessage());
          localResultReceiver.send(i, localBundle);
        }
        return;
      }
      catch (JSONWebRequester.RestResponseException localRestResponseException)
      {
        Log.d(this.mTag, str + ": service error: " + localRestResponseException, localRestResponseException);
        if (localResultReceiver != null)
        {
          localBundle.putString("error-text", localRestResponseException.getCause().getMessage());
          localResultReceiver.send(i, localBundle);
        }
        return;
      }
      finally
      {
        scheduleCleanup();
      }
    }
    Log.d(this.mTag, "Unsupported action=" + str);
  }

  private void runResultsCleanup(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    try
    {
      onResultsCleanup();
      return;
    }
    catch (OperationApplicationException localOperationApplicationException)
    {
      Log.w(this.mTag, str + ": cannot clean results: " + localOperationApplicationException);
      return;
    }
    catch (RemoteException localRemoteException) {}
  }

  private void scheduleCleanup()
  {
    this.mHandler.removeCallbacks(this.mInitiateCleanup);
    this.mHandler.postDelayed(this.mInitiateCleanup, 60000L);
  }

  protected Bundle createResultData(Intent paramIntent)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("extras", paramIntent.getParcelableExtra("extras"));
    return localBundle;
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    Log.d(this.mTag, "onHandleIntent: intent=" + paramIntent);
    if ("com.pointinside.android.app.service.AbstractRESTServiceHelper.CLEANUP".equals(str))
    {
      runResultsCleanup(paramIntent);
      return;
    }
    runRegisteredMethod(paramIntent);
  }

  protected abstract void onResultsCleanup()
    throws RemoteException, OperationApplicationException;

  protected final void registerMethodHandler(String paramString, int paramInt, MethodHandler paramMethodHandler)
  {
    this.mMethodHandlers.put(paramString, new MethodHandlerContainer(paramInt, paramMethodHandler));
  }

  public static abstract interface MethodHandler
  {
    public abstract void executeMethod(int paramInt, Intent paramIntent, Bundle paramBundle)
      throws JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException;
  }

  private static class MethodHandlerContainer
  {
    public AbstractRESTServiceHelper.MethodHandler handler;
    public int resultCode;

    public MethodHandlerContainer(int paramInt, AbstractRESTServiceHelper.MethodHandler paramMethodHandler)
    {
      this.resultCode = paramInt;
      this.handler = paramMethodHandler;
    }
  }

  public static abstract class SimpleMethodHandler
    implements AbstractRESTServiceHelper.MethodHandler
  {
    public void executeMethod(int paramInt, Intent paramIntent, Bundle paramBundle)
      throws JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException
    {
      ResultReceiver localResultReceiver = (ResultReceiver)paramIntent.getParcelableExtra("result-receiver");
      Uri localUri = onExecuteMethod(paramIntent);
      if (localResultReceiver != null)
      {
        paramBundle.putParcelable("result-uri", localUri);
        localResultReceiver.send(paramInt, paramBundle);
      }
    }

    protected abstract Uri onExecuteMethod(Intent paramIntent)
      throws JSONWebRequester.RestResponseException, OperationApplicationException, RemoteException;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.service.AbstractRESTServiceHelper
 * JD-Core Version:    0.7.0.1
 */