package com.pointinside.android.api.net;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONWebRequester
{
  private final HttpClient mClient;

  public JSONWebRequester(HttpClient paramHttpClient)
  {
    this.mClient = paramHttpClient;
  }

  private String executeForString(HttpUriRequest paramHttpUriRequest)
    throws JSONWebRequester.RestResponseException
  {
    try
    {
      String str = executeUnwrappedForString(paramHttpUriRequest);
      return str;
    }
    catch (ClientProtocolException localClientProtocolException)
    {
      throw new RestResponseException(localClientProtocolException);
    }
    catch (IOException localIOException)
    { 
      throw new RestResponseException(localIOException);
    }
  }

  private String executeUnwrappedForString(HttpUriRequest paramHttpUriRequest)
    throws ClientProtocolException, IOException
  {
    return (String)this.mClient.execute(paramHttpUriRequest, new BasicResponseHandler());
  }

  public JSONObject execute(HttpUriRequest paramHttpUriRequest)
    throws JSONWebRequester.RestResponseException
  {
    try
    {
      String str = executeForString(paramHttpUriRequest);
      if (str != null)
      {
        JSONObject localJSONObject = new JSONObject(str);
        return localJSONObject;
      }
      return null;
    }
    catch (JSONException localJSONException)
    {
      throw new RestResponseException(localJSONException);
    }
  }

  public JSONArray executeForArray(HttpUriRequest paramHttpUriRequest)
    throws JSONWebRequester.RestResponseException
  {
    try
    {
      String str = executeForString(paramHttpUriRequest);
      if (str != null)
      {
        JSONArray localJSONArray = new JSONArray(str);
        return localJSONArray;
      }
      return null;
    }
    catch (JSONException localJSONException)
    {
      throw new RestResponseException(localJSONException);
    }
  }

  public final HttpClient getHttpClient()
  {
    return this.mClient;
  }

  public static class RestResponseException
    extends Exception
  {
    public RestResponseException(String paramString)
    {
      super();
    }

    public RestResponseException(Throwable paramThrowable)
    {
      super();
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.net.JSONWebRequester
 * JD-Core Version:    0.7.0.1
 */