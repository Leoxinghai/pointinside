package com.pointinside.android.api.content;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public final class UnsecureTrustManagerFactory
{
  private static X509TrustManager sUnsecureTrustManager = new SimpleX509TrustManager();

  public static X509TrustManager getTrustManager()
  {
    return sUnsecureTrustManager;
  }

  private static void logCertificates(X509Certificate[] paramArrayOfX509Certificate, String paramString, boolean paramBoolean) {}

  private static class SimpleX509TrustManager
    implements X509TrustManager
  {
    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    {
      UnsecureTrustManagerFactory.logCertificates(paramArrayOfX509Certificate, "Trusting client", false);
    }

    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    {
      UnsecureTrustManagerFactory.logCertificates(paramArrayOfX509Certificate, "Trusting server", false);
    }

    public X509Certificate[] getAcceptedIssuers()
    {
      return null;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.UnsecureTrustManagerFactory
 * JD-Core Version:    0.7.0.1
 */