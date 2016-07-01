package com.pointinside.android.api.utils;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build.VERSION;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import android.os.Build;

public abstract class HttpSSLSocketFactoryHelper
{
  public static HttpSSLSocketFactoryHelper newInstance(Context paramContext)
  {
    if (Build.VERSION.SDK_INT >= 8) {
      return new FroyoAndBeyond(paramContext);
    }
    return new PreFroyo(paramContext);
  }

  public abstract SSLSocketFactory getHttpSocketFactory(int paramInt);

  private static class FroyoAndBeyond
    extends HttpSSLSocketFactoryHelper
  {
    private final SSLSessionCache mCache;

    public FroyoAndBeyond(Context paramContext)
    {
      this.mCache = new SSLSessionCache(paramContext);
    }

    public SSLSocketFactory getHttpSocketFactory(int paramInt)
    {
      return SSLCertificateSocketFactory.getHttpSocketFactory(paramInt, this.mCache);
    }
  }

  private static class PreFroyo
    extends HttpSSLSocketFactoryHelper
  {
    public PreFroyo(Context paramContext) {}

    public SSLSocketFactory getHttpSocketFactory(int paramInt)
    {
      try
      {
        SSLSocketFactory localSSLSocketFactory = new SSLSocketFactory(null);
        return localSSLSocketFactory;
      }
      catch (KeyManagementException localKeyManagementException)
      {
        throw new RuntimeException(localKeyManagementException);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new RuntimeException(localNoSuchAlgorithmException);
      }
      catch (KeyStoreException localKeyStoreException)
      {
        throw new RuntimeException(localKeyStoreException);
      }
      catch (UnrecoverableKeyException localUnrecoverableKeyException)
      {
        throw new RuntimeException(localUnrecoverableKeyException);
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.HttpSSLSocketFactoryHelper
 * JD-Core Version:    0.7.0.1
 */