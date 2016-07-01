package com.pointinside.android.piwebservices.net;

import android.content.Context;
import android.net.Uri;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.api.net.MyHttpClient;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PIWebServicesContract;
import com.pointinside.android.piwebservices.util.DevIdUtils;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;

public class PIWebServices
{
  private static WeakReference<Context> sContext;
  private static boolean sHttpLogging;
  private static JSONWebRequester sRequester;
  private static String sUserAgent;

  static MyHttpClient createHttpClient(Context paramContext)
  {
    MyHttpClient localMyHttpClient = MyHttpClient.newInstance(sUserAgent, paramContext);
    localMyHttpClient.setLoggingEnabled(sHttpLogging);
    localMyHttpClient.setUsePreemptiveAuth(true);
    return localMyHttpClient;
  }

  private static Context getContext()
  {
    Context localContext = (Context)sContext.get();
    if (localContext != null) {
      return localContext;
    }
    throw new IllegalStateException("Inconsistent JSONWebRequester state");
  }

  public static String getUserAgent(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append('/');
    localStringBuilder.append(paramString2);
    localStringBuilder.append(" (");
    localStringBuilder.append(MyHttpClient.getHostIdentifierString());
    localStringBuilder.append(")");
    localStringBuilder.append(" PIMapsAPI/1.7.0");
    return localStringBuilder.toString();
  }

  public static JSONWebRequester getWebRequester(Context paramContext)
  {
    try
    {
      if (sRequester == null) {
    	    sContext = new WeakReference(paramContext.getApplicationContext());
    	    sRequester = new JSONWebRequester(createHttpClient(paramContext));
      }
      if (sUserAgent == null) {
        throw new IllegalStateException("PIWebServices.init must be called before using services");
      }
    }
    catch(Exception ex)
    {
    }
    JSONWebRequester localJSONWebRequester = sRequester;
    return localJSONWebRequester;
  }

  public static void init(Context paramContext, String paramString1, String paramString2, boolean paramBoolean)
  {
    try
    {
      if (sRequester != null) {
        throw new IllegalStateException("PIWebServices.init must be called before web requests are made");
      }
    }
    finally {}
    PIWebServicesContract.init(paramContext);
    PITouchstreamContract.init(paramContext);
    sUserAgent = getUserAgent(paramString1, paramString2);
    sHttpLogging = paramBoolean;
  }

  public static abstract class CommonRequestObject
  {
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_DEV_ID = "devid";
    public int maxResults = -1;
    public final HashSet<String> outputFields = new HashSet();
    public int startIndex = 0;

    protected static String join(String paramString, HashSet<String> paramHashSet)
    {
      Iterator localIterator = paramHashSet.iterator();
      StringBuilder localStringBuilder = new StringBuilder();
      if (localIterator.hasNext()) {
        localStringBuilder.append((String)localIterator.next());
      }
      for (;;)
      {
        if (!localIterator.hasNext()) {
          return localStringBuilder.toString();
        }
        localStringBuilder.append(',');
        localStringBuilder.append((String)localIterator.next());
      }
    }

    public void apply(Uri.Builder paramBuilder, String paramString)
    {
      paramBuilder.appendQueryParameter("devid", DevIdUtils.getHashedUUID(PIWebServices.getContext()));
      paramBuilder.appendQueryParameter("api_key", paramString);
      if (this.startIndex > 0) {
        throw new UnsupportedOperationException("TODO");
      }
      onApply(paramBuilder);
    }

    protected abstract void onApply(Uri.Builder paramBuilder);

    public void setOutputFields(String... paramVarArgs)
    {
      this.outputFields.clear();
      int i = paramVarArgs.length;
      for (int j = 0;; j++)
      {
        if (j >= i) {
          return;
        }
        String str = paramVarArgs[j];
        this.outputFields.add(str);
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.net.PIWebServices
 * JD-Core Version:    0.7.0.1
 */