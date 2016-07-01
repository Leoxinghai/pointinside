package com.pointinside.android.api.nav;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.pointinside.android.api.content.Downloads.DownloadConstants;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.api.net.MyHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;

import com.pointinside.android.api.content.*;

public class RouteAPI
{
  private static Uri sBaseUri;
  private static JSONWebRequester sWebRequester;

  private static HttpClient createHttpClient(Context paramContext, Uri paramUri, Credentials paramCredentials)
  {
    MyHttpClient localMyHttpClient = MyHttpClient.newInstance(Downloads.DownloadConstants.DEFAULT_USER_AGENT, paramContext);
    localMyHttpClient.setLoggingEnabled(false);
    localMyHttpClient.setUsePreemptiveAuth(true);
    localMyHttpClient.getCredentialsProvider().setCredentials(new AuthScope(paramUri.getHost(), paramUri.getPort()), paramCredentials);
    return localMyHttpClient;
  }

  private static void ensureInitialized()
  {
    try
    {
      if (sWebRequester == null) {
        throw new IllegalStateException("Must call RouteAPI.init first");
      }
    }
    finally {}
  }

  static Uri getMethodUri(String paramString)
  {
    try
    {
      ensureInitialized();
      Uri localUri = sBaseUri.buildUpon().appendPath(paramString).build();
      return localUri;
    }
    finally
    {
//      localObject = finally;
//      throw localObject;
    }
  }

  static JSONWebRequester getWebRequester()
  {
    try
    {
      ensureInitialized();
      JSONWebRequester localJSONWebRequester = sWebRequester;
      return localJSONWebRequester;
    }
    finally
    {
//      localObject = finally;
//      throw localObject;
    }
  }

  public static void init(Context paramContext, Uri paramUri, Credentials paramCredentials)
  {
    try
    {
      sBaseUri = paramUri;
      sWebRequester = new JSONWebRequester(createHttpClient(paramContext, paramUri, paramCredentials));
      return;
    }
    finally
    {
//      localObject = finally;
//      throw localObject;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.nav.RouteAPI
 * JD-Core Version:    0.7.0.1
 */