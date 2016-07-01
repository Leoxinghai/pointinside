package com.pointinside.android.api.net;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.pointinside.android.api.content.TrustAllSSLSocketFactory;
import com.pointinside.android.api.content.UnsecureTrustManagerFactory;
import com.pointinside.android.api.utils.CountingInputStream;
import com.pointinside.android.api.utils.HttpSSLSocketFactoryHelper;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.*;

public class MyHttpClient
  extends DefaultHttpClient
{
  private static final int CONNECT_TIMEOUT = 15000;
  private static final int SOCKET_OPERATION_TIMEOUT = 20000;
  private static final String TAG = MyHttpClient.class.getSimpleName();
  private static  HttpRequestInterceptor sThreadCheckInterceptor = new HttpRequestInterceptor()
  {
    public void process(HttpRequest paramAnonymousHttpRequest, HttpContext paramAnonymousHttpContext)
    {
      Looper localLooper = Looper.myLooper();
      if ((localLooper != null) && (localLooper == Looper.getMainLooper())) {
        throw new RuntimeException("This thread forbids HTTP requests");
      }
    }
  };
  boolean mAcceptsGzip;
  final HttpLogger mLogger = new HttpLogger();
  boolean mLoggingEnabled;
  private final HttpRequestInterceptor mPreemptiveAuthInterceptor = new HttpRequestInterceptor()
  {
    public void process(HttpRequest paramAnonymousHttpRequest, HttpContext paramAnonymousHttpContext)
      throws HttpException, IOException
    {
      if (MyHttpClient.this.mUsePreemptiveAuth)
      {
        AuthState localAuthState = (AuthState)paramAnonymousHttpContext.getAttribute("http.auth.target-scope");
        if (localAuthState.getAuthScheme() == null)
        {
          CredentialsProvider localCredentialsProvider = (CredentialsProvider)paramAnonymousHttpContext.getAttribute("http.auth.credentials-provider");
          HttpHost localHttpHost = (HttpHost)paramAnonymousHttpContext.getAttribute("http.target_host");
          Credentials localCredentials = localCredentialsProvider.getCredentials(new AuthScope(localHttpHost.getHostName(), localHttpHost.getPort()));
          if (localCredentials != null)
          {
            localAuthState.setAuthScheme(new BasicScheme());
            localAuthState.setCredentials(localCredentials);
          }
        }
      }
    }
  };
  private final HttpSSLSocketFactoryHelper mSSLSocketHelper;
  boolean mTrustAllSSLCerts;
  boolean mUsePreemptiveAuth;
  private final String mUserAgent;

  private MyHttpClient(String paramString, HttpSSLSocketFactoryHelper paramHttpSSLSocketFactoryHelper)
  {
    this.mUserAgent = paramString;
    this.mSSLSocketHelper = paramHttpSSLSocketFactoryHelper;
    setAcceptsGzip(true);
  }

  public static String getHostIdentifierString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Linux");
    localStringBuilder.append("; U");
    localStringBuilder.append("; Android ").append(Build.VERSION.RELEASE);
    localStringBuilder.append("; ");
    Locale localLocale = Locale.getDefault();
    String str1 = localLocale.getLanguage();
    if (str1 != null)
    {
      localStringBuilder.append(str1.toLowerCase());
      String str4 = localLocale.getCountry();
      if (str4 != null)
      {
        localStringBuilder.append("-");
        localStringBuilder.append(str4.toLowerCase());
      }
    }
    for (;;)
    {
      if ("REL".equals(Build.VERSION.CODENAME))
      {
        String str3 = Build.MODEL;
        if (!TextUtils.isEmpty(str3))
        {
          localStringBuilder.append("; ");
          localStringBuilder.append(str3);
        }
      }
      String str2 = Build.ID;
      if (!TextUtils.isEmpty(str2))
      {
        localStringBuilder.append(" Build/");
        localStringBuilder.append(str2);
      }
      localStringBuilder.append("en");
      return localStringBuilder.toString();
    }
  }

  private static boolean hasGzipEncoding(HttpEntity paramHttpEntity)
  {
    Header localHeader = paramHttpEntity.getContentEncoding();
    HeaderElement[] arrayOfHeaderElement;
    if (localHeader != null) {
    	arrayOfHeaderElement = localHeader.getElements();
	    for (int i = 0;; i++)
	    {
	      if (i >= arrayOfHeaderElement.length) {
	        return false;
	      }
	      if (arrayOfHeaderElement[i].getName().equalsIgnoreCase("gzip")) {
	        return true;
	      }
	    }
    }
    return false;
  }

  public static MyHttpClient newInstance(String paramString, Context paramContext)
  {
    return new MyHttpClient(paramString, HttpSSLSocketFactoryHelper.newInstance(paramContext));
  }

  public void close()
  {
    getConnectionManager().shutdown();
  }

  protected ClientConnectionManager createClientConnectionManager()
  {
    SchemeRegistry localSchemeRegistry = new SchemeRegistry();
    localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    localSchemeRegistry.register(new Scheme("https", this.mSSLSocketHelper.getHttpSocketFactory(20000), 443));
    TrustManager[] arrayOfTrustManager = null;
    if (this.mTrustAllSSLCerts) {
		  arrayOfTrustManager = new TrustManager[] { UnsecureTrustManagerFactory.getTrustManager() };
    }
	for (;;)
	{
	  try
	  {
	    localSchemeRegistry.register(new Scheme("https", new TrustAllSSLSocketFactory(arrayOfTrustManager), 443));
	    return new ThreadSafeClientConnManager(getParams(), localSchemeRegistry);
	  }
	  catch (KeyManagementException localKeyManagementException)
	  {
	    Log.e(TAG, localKeyManagementException.toString(), localKeyManagementException);
	    continue;
	  }
	  catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
	  {
	    Log.e(TAG, localNoSuchAlgorithmException.toString(), localNoSuchAlgorithmException);
	    continue;
	  }
	  catch (KeyStoreException localKeyStoreException)
	  {
	    Log.e(TAG, localKeyStoreException.toString(), localKeyStoreException);
	    continue;
	  }
	  catch (UnrecoverableKeyException localUnrecoverableKeyException)
	  {
	    Log.e(TAG, localUnrecoverableKeyException.toString(), localUnrecoverableKeyException);
	    continue;
	  }
	}
  }

  protected HttpContext createHttpContext()
  {
    HttpContext localHttpContext = super.createHttpContext();
    localHttpContext.removeAttribute("http.cookie-store");
    return localHttpContext;
  }

  protected HttpParams createHttpParams()
  {
    HttpParams localHttpParams = super.createHttpParams();
    HttpConnectionParams.setStaleCheckingEnabled(localHttpParams, false);
    HttpConnectionParams.setConnectionTimeout(localHttpParams, 15000);
    HttpConnectionParams.setSoTimeout(localHttpParams, 20000);
    HttpConnectionParams.setSocketBufferSize(localHttpParams, 8192);
    HttpClientParams.setRedirecting(localHttpParams, false);
    HttpProtocolParams.setUserAgent(localHttpParams, this.mUserAgent);
    return localHttpParams;
  }

  protected BasicHttpProcessor createHttpProcessor()
  {
    BasicHttpProcessor localBasicHttpProcessor = super.createHttpProcessor();
    localBasicHttpProcessor.addRequestInterceptor(this.mPreemptiveAuthInterceptor, 0);
    localBasicHttpProcessor.addRequestInterceptor(sThreadCheckInterceptor);
    localBasicHttpProcessor.addRequestInterceptor(new GzipAcceptedInterceptor());
    localBasicHttpProcessor.addResponseInterceptor(new GzipInflateInterceptor());
    LoggingInterceptor localLoggingInterceptor = new LoggingInterceptor();
    localBasicHttpProcessor.addRequestInterceptor(localLoggingInterceptor);
    localBasicHttpProcessor.addResponseInterceptor(localLoggingInterceptor);
    return localBasicHttpProcessor;
  }

  public boolean isLoggingEnabled()
  {
    return this.mLoggingEnabled;
  }

  public void setAcceptsGzip(boolean paramBoolean)
  {
    this.mAcceptsGzip = paramBoolean;
  }

  public void setLoggingEnabled(boolean paramBoolean)
  {
    this.mLoggingEnabled = paramBoolean;
  }

  public void setTrustAllSSLCertificates(boolean paramBoolean)
  {
    this.mTrustAllSSLCerts = paramBoolean;
  }

  public void setUsePreemptiveAuth(boolean paramBoolean)
  {
    this.mUsePreemptiveAuth = paramBoolean;
  }

  private static class CountingGZIPInputStream
    extends GZIPInputStream
  {
    private CountingInputStream mIn;

    private CountingGZIPInputStream(CountingInputStream paramCountingInputStream)
      throws IOException
    {
      super(paramCountingInputStream);
      this.mIn = paramCountingInputStream;
    }

    public CountingGZIPInputStream(InputStream paramInputStream)
      throws IOException
    {
      this(new CountingInputStream(paramInputStream));
    }

    public long compressedBytesRead()
    {
      return this.mIn.bytesRead();
    }
  }

  private class GzipAcceptedInterceptor
    implements HttpRequestInterceptor
  {
    private GzipAcceptedInterceptor() {}

    public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
      throws HttpException, IOException
    {
      if ((MyHttpClient.this.mAcceptsGzip) && (!paramHttpRequest.containsHeader("Accept-Encoding"))) {
        paramHttpRequest.addHeader("Accept-Encoding", "gzip");
      }
    }
  }

  private static class GzipDecompressingEntity
    extends HttpEntityWrapper
  {
    private volatile MyHttpClient.CountingGZIPInputStream mRecentStream;

    public GzipDecompressingEntity(HttpEntity paramHttpEntity)
    {
      super(paramHttpEntity);
    }

    public InputStream getContent()
      throws IOException
    {
      MyHttpClient.CountingGZIPInputStream localCountingGZIPInputStream = new MyHttpClient.CountingGZIPInputStream(this.wrappedEntity.getContent());
      this.mRecentStream = localCountingGZIPInputStream;
      return localCountingGZIPInputStream;
    }

    public long getContentLength()
    {
      return -1L;
    }

    public MyHttpClient.CountingGZIPInputStream getRecentStream()
    {
      return this.mRecentStream;
    }
  }

  private class GzipInflateInterceptor
    implements HttpResponseInterceptor
  {
    private GzipInflateInterceptor() {}

    public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
      throws HttpException, IOException
    {
      HttpEntity localHttpEntity = paramHttpResponse.getEntity();
      if ((localHttpEntity != null) && (MyHttpClient.hasGzipEncoding(localHttpEntity))) {
        paramHttpResponse.setEntity(new MyHttpClient.GzipDecompressingEntity(localHttpEntity));
      }
    }
  }

  private static class HttpLogger
  {
    private static final long MAX_SLURP_LENGTH = 4096L;
    private final LineFormatter mFormatter;

    public HttpLogger()
    {
      this(BasicLineFormatter.DEFAULT);
    }

    public HttpLogger(LineFormatter paramLineFormatter)
    {
      this.mFormatter = paramLineFormatter;
    }

    private static String getContentType(HttpEntity paramHttpEntity)
    {
      Header localHeader = paramHttpEntity.getContentType();
      if (localHeader != null)
      {
        String str = localHeader.getValue();
        if (str != null)
        {
          String[] arrayOfString = str.split(";");
          if (arrayOfString.length > 0) {
            return arrayOfString[0];
          }
        }
      }
      return null;
    }

    private static boolean isPrintableMimeType(String paramString)
    {
      if (paramString != null)
      {
        if (paramString.startsWith("text/")) {}
        while (paramString.equals("application/json")) {
          return true;
        }
      }
      return false;
    }

    private void log(String paramString1, String paramString2)
    {
      Log.d(MyHttpClient.TAG + ' ' + paramString1, paramString1 + ' ' + paramString2);
    }

    private void logEntity(String paramString, HttpEntity paramHttpEntity)
      throws IOException
    {
      log(paramString, EntityUtils.toString(paramHttpEntity));
    }

    private void logHeaders(String paramString, HttpMessage paramHttpMessage)
    {
      HeaderIterator localHeaderIterator = paramHttpMessage.headerIterator();
      for (;;)
      {
        if (!localHeaderIterator.hasNext()) {
          return;
        }
        log(paramString, BasicLineFormatter.formatHeader(localHeaderIterator.nextHeader(), this.mFormatter));
      }
    }

    private void reportDeflateStats(long paramLong1, long paramLong2)
    {
      reportGzipStats("deflated", paramLong2, paramLong1);
    }

    private void reportGzipStats(String paramString, long paramLong1, long paramLong2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" ");
      localStringBuilder.append(String.valueOf(paramLong1));
      localStringBuilder.append(" to ");
      localStringBuilder.append(String.valueOf(paramLong2));
      localStringBuilder.append(" (");
      if (paramLong2 > 0L)
      {
        localStringBuilder.append(String.valueOf((int)(100.0F * ((float)paramLong1 / (float)paramLong2))));
        localStringBuilder.append("% of original");
      }
      for (;;)
      {
        localStringBuilder.append(")");
        localStringBuilder.append("]");
        Log.d(MyHttpClient.TAG, localStringBuilder.toString());
        localStringBuilder.append("--");
        return;
      }
    }

    private void reportInflateStats(MyHttpClient.GzipDecompressingEntity paramGzipDecompressingEntity, long paramLong)
    {
      reportGzipStats("inflated", paramGzipDecompressingEntity.getRecentStream().compressedBytesRead(), paramLong);
    }

    private static boolean shouldSlurpAndPrintEntity(HttpEntity paramHttpEntity)
    {
      if (!isPrintableMimeType(getContentType(paramHttpEntity))) {}
      while (paramHttpEntity.getContentLength() > 4096L) {
        return false;
      }
      return true;
    }

    public void logRequest(HttpRequest httprequest)
            throws IOException
        {
            log(">>", BasicLineFormatter.formatRequestLine(httprequest.getRequestLine(), mFormatter));
            logHeaders(">>", httprequest);
            BufferedHttpEntity bufferedhttpentity;
            BufferedHttpEntity bufferedhttpentity1;
            if(httprequest instanceof HttpEntityEnclosingRequest) {
	            HttpEntityEnclosingRequest httpentityenclosingrequest;
	            HttpEntity httpentity;
	            httpentityenclosingrequest = (HttpEntityEnclosingRequest)httprequest;
	            httpentity = httpentityenclosingrequest.getEntity();
	            if(httpentity != null) {
		            if(shouldSlurpAndPrintEntity(httpentity)) {
			            bufferedhttpentity = new BufferedHttpEntity(httpentity);
			            if(!MyHttpClient.hasGzipEncoding(httpentity)) {
				            bufferedhttpentity1 = bufferedhttpentity;
				            logEntity(">>", bufferedhttpentity1);
				            httpentityenclosingrequest.setEntity(bufferedhttpentity);
			                return;
			            } else {
				            bufferedhttpentity1 = new BufferedHttpEntity(new GzipDecompressingEntity(bufferedhttpentity));
				            reportDeflateStats(bufferedhttpentity.getContentLength(), bufferedhttpentity1.getContentLength());
			            }

		            }
	            }
            }
            return;
        }

    public void logResponse(HttpResponse paramHttpResponse)
      throws IOException
    {
      try
      {
        log("<<", BasicLineFormatter.formatStatusLine(paramHttpResponse.getStatusLine(), this.mFormatter));
        logHeaders("<<", paramHttpResponse);
        HttpEntity localHttpEntity = paramHttpResponse.getEntity();
        if ((localHttpEntity != null) && (shouldSlurpAndPrintEntity(localHttpEntity)))
        {
          BufferedHttpEntity localBufferedHttpEntity = new BufferedHttpEntity(localHttpEntity);
          if ((localHttpEntity instanceof MyHttpClient.GzipDecompressingEntity)) {
            reportInflateStats((MyHttpClient.GzipDecompressingEntity)localHttpEntity, localBufferedHttpEntity.getContentLength());
          }
          logEntity("<<", localBufferedHttpEntity);
          paramHttpResponse.setEntity(localBufferedHttpEntity);
        }
        return;
      }
      finally {}
    }
  }

  private class LoggingInterceptor
    implements HttpRequestInterceptor, HttpResponseInterceptor
  {
    private LoggingInterceptor() {}

    public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
      throws HttpException, IOException
    {
      if (MyHttpClient.this.mLoggingEnabled) {
        MyHttpClient.this.mLogger.logRequest(paramHttpRequest);
      }
    }

    public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
      throws HttpException, IOException
    {
      if (MyHttpClient.this.mLoggingEnabled) {
        MyHttpClient.this.mLogger.logResponse(paramHttpResponse);
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.net.MyHttpClient
 * JD-Core Version:    0.7.0.1
 */