package com.pointinside.android.app.net;

import android.content.Context;
import android.net.Uri;
import com.pointinside.android.piwebservices.util.BuildUtils;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class PIPostFeedbackClient
{
  protected HttpClient mClient;
  protected Context mContext;
  private String mData;
  protected Uri mUri;

  public PIPostFeedbackClient(Context paramContext, Uri paramUri, String paramString)
  {
    this.mData = paramString;
    this.mUri = paramUri;
    this.mContext = paramContext;
    this.mClient = PIHttpClient.newInstance(BuildUtils.getUserAgent(paramContext));
  }

  public void run()
    throws IOException
  {
    HttpResponse localHttpResponse;
    try
    {
      HttpPost localHttpPost = new HttpPost(this.mUri.toString());
      if ((this.mData != null) && (this.mData.length() > 0))
      {
        localHttpPost.setEntity(new StringEntity(this.mData));
        localHttpResponse = this.mClient.execute(localHttpPost);
        if (localHttpResponse.getStatusLine().getStatusCode() == 200) {
          localHttpResponse.getEntity().consumeContent();
        }
      }
      else
      {
        throw new IOException("Error posting data. Posting empty data.");
      }
    }
    finally
    {
      ((PIHttpClient)this.mClient).close();
    }
    throw new IOException("Error posting data: " + localHttpResponse.getStatusLine().getStatusCode());
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.net.PIPostFeedbackClient
 * JD-Core Version:    0.7.0.1
 */