package com.pointinside.android.api.content;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class TrustAllSSLSocketFactory
  implements LayeredSocketFactory
{
  public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
  public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
  public static final String SSL = "SSL";
  public static final String SSLV2 = "SSLv2";
  public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
  public static final String TLS = "TLS";
  private X509HostnameVerifier hostnameVerifier = ALLOW_ALL_HOSTNAME_VERIFIER;
  private final SSLSocketFactory socketfactory;
  private final SSLContext sslcontext = SSLContext.getInstance("TLS");

  public TrustAllSSLSocketFactory(TrustManager[] paramArrayOfTrustManager)
    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
  {
    this.sslcontext.init(null, paramArrayOfTrustManager, null);
    this.socketfactory = this.sslcontext.getSocketFactory();
  }

  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Target host may not be null.");
    }
    if (paramHttpParams == null) {
      throw new IllegalArgumentException("Parameters may not be null.");
    }
    if (paramSocket != null) {}
    SSLSocket localSSLSocket;
    for (Socket localSocket = paramSocket;; localSocket = createSocket())
    {
      localSSLSocket = (SSLSocket)localSocket;
      if ((paramInetAddress != null) || (paramInt2 > 0))
      {
        if (paramInt2 < 0) {
          paramInt2 = 0;
        }
        localSSLSocket.bind(new InetSocketAddress(paramInetAddress, paramInt2));
      }
      int i = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
      int j = HttpConnectionParams.getSoTimeout(paramHttpParams);
      localSSLSocket.connect(new InetSocketAddress(paramString, paramInt1), i);
      localSSLSocket.setSoTimeout(j);
      try
      {
        this.hostnameVerifier.verify(paramString, localSSLSocket);
        return localSSLSocket;
      }
      catch (IOException localIOException) {}
    }
    /*
    try
    {
      localSSLSocket.close();
  //    label145:
//      throw localIOException;
    }
    catch (Exception localException)
    {
      break label145;
    }
    */
  }

  public Socket createSocket()
    throws IOException
  {
    return (SSLSocket)this.socketfactory.createSocket();
  }

  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException, UnknownHostException
  {
    SSLSocket localSSLSocket = (SSLSocket)this.socketfactory.createSocket(paramSocket, paramString, paramInt, paramBoolean);
    this.hostnameVerifier.verify(paramString, localSSLSocket);
    return localSSLSocket;
  }

  public X509HostnameVerifier getHostnameVerifier()
  {
    return this.hostnameVerifier;
  }

  public boolean isSecure(Socket paramSocket)
    throws IllegalArgumentException
  {
    if (paramSocket == null) {
      throw new IllegalArgumentException("Socket may not be null.");
    }
    if (!(paramSocket instanceof SSLSocket)) {
      throw new IllegalArgumentException("Socket not created by this factory.");
    }
    if (paramSocket.isClosed()) {
      throw new IllegalArgumentException("Socket is closed.");
    }
    return true;
  }

  public void setHostnameVerifier(X509HostnameVerifier paramX509HostnameVerifier)
  {
    if (paramX509HostnameVerifier == null) {
      throw new IllegalArgumentException("Hostname verifier may not be null");
    }
    this.hostnameVerifier = paramX509HostnameVerifier;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.TrustAllSSLSocketFactory
 * JD-Core Version:    0.7.0.1
 */