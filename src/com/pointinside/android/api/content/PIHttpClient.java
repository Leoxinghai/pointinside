package com.pointinside.android.api.content;

import android.content.Context;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.net.MyHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;

final class PIHttpClient
{
  private static final boolean TRUST_ALL_SSL = true;

  static MyHttpClient newInstance(String paramString, Context paramContext)
  {
    MyHttpClient localMyHttpClient = MyHttpClient.newInstance(paramString, paramContext);
    localMyHttpClient.setLoggingEnabled(false);
    localMyHttpClient.setUsePreemptiveAuth(true);
    localMyHttpClient.setTrustAllSSLCertificates(true);
    localMyHttpClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1), PIMapReference.getCredentials());
    return localMyHttpClient;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.PIHttpClient
 * JD-Core Version:    0.7.0.1
 */