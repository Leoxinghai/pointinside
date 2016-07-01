// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.net;

import android.util.Log;
import com.pointinside.android.api.PIMapReference;
import java.io.*;
import java.util.zip.*;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.*;
import org.apache.http.protocol.*;

public final class PIHttpClient
    implements HttpClient
{
    private class CurlLogger
        implements HttpRequestInterceptor
    {

        public void process(HttpRequest httprequest, HttpContext httpcontext)
            throws HttpException, IOException
        {
            LoggingConfiguration loggingconfiguration = curlConfiguration;
            if(loggingconfiguration != null && loggingconfiguration.isLoggable() && (httprequest instanceof HttpUriRequest))
                loggingconfiguration.println(PIHttpClient.toCurl((HttpUriRequest)httprequest, loggingconfiguration.isAuthLoggable()));
        }

        final PIHttpClient this$0;

        private CurlLogger()
        {
            super();
            this$0 = PIHttpClient.this;
        }

        CurlLogger(CurlLogger curllogger)
        {
            this();
        }
    }

    private static class LoggingConfiguration
    {

        private boolean isAuthLoggable()
        {
            return false;
        }

        private boolean isLoggable()
        {
            return Log.isLoggable(tag, level);
        }

        private void println(String s)
        {
            Log.println(level, tag, s);
        }

        private final int level;
        private final String tag;




        private LoggingConfiguration(String s, int i)
        {
            tag = s;
            level = i;
        }

        LoggingConfiguration(String s, int i, LoggingConfiguration loggingconfiguration)
        {
            this(s, i);
        }
    }


    private PIHttpClient(ClientConnectionManager clientconnectionmanager, HttpParams httpparams)
    {
        mLeakedException = new IllegalStateException("AndroidHttpClient created and never closed");
        _flddelegate = new DefaultHttpClient(clientconnectionmanager, httpparams) {

            protected HttpContext createHttpContext()
            {
                BasicHttpContext basichttpcontext = new BasicHttpContext();
                getCredentialsProvider().setCredentials(new AuthScope(null, -1), PIMapReference.getCredentials());
                basichttpcontext.setAttribute("http.authscheme-registry", getAuthSchemes());
                basichttpcontext.setAttribute("http.cookiespec-registry", getCookieSpecs());
                basichttpcontext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
                return basichttpcontext;
            }

            protected BasicHttpProcessor createHttpProcessor()
            {
                BasicHttpProcessor basichttpprocessor = super.createHttpProcessor();
                basichttpprocessor.addRequestInterceptor(PIHttpClient.sThreadCheckInterceptor);
                basichttpprocessor.addRequestInterceptor(new CurlLogger(null));
                return basichttpprocessor;
            }
/*
            final PIHttpClient this$0;
            {
                super(clientconnectionmanager, httpparams);
                this$0 = PIHttpClient.this;
            }
*/
        }
;
    }

    public static AbstractHttpEntity getCompressedEntity(byte abyte0[])
        throws IOException
    {
        if((long)abyte0.length < getMinGzipSize())
        {
            return new ByteArrayEntity(abyte0);
        } else
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            GZIPOutputStream gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream);
            gzipoutputstream.write(abyte0);
            gzipoutputstream.close();
            ByteArrayEntity bytearrayentity = new ByteArrayEntity(bytearrayoutputstream.toByteArray());
            bytearrayentity.setContentEncoding("gzip");
            return bytearrayentity;
        }
    }

    public static long getMinGzipSize()
    {
        return DEFAULT_SYNC_MIN_GZIP_BYTES;
    }

    public static InputStream getUngzippedContent(HttpEntity httpentity)
        throws IOException
    {
        Object obj = httpentity.getContent();
        if(obj == null)
            return ((InputStream) (obj));
        Header header = httpentity.getContentEncoding();
        if(header == null)
            return ((InputStream) (obj));
        String s = header.getValue();
        if(s == null)
            return ((InputStream) (obj));
        if(s.contains("gzip"))
            obj = new GZIPInputStream(((InputStream) (obj)));
        return ((InputStream) (obj));
    }

    public static InputStream getUnzippedContent(HttpEntity httpentity)
        throws IOException
    {
        Object obj = httpentity.getContent();
        if(obj == null)
            return ((InputStream) (obj));
        Header header = httpentity.getContentEncoding();
        if(header == null)
            return ((InputStream) (obj));
        String s = header.getValue();
        if(s == null)
            return ((InputStream) (obj));
        if(s.contains("zip"))
            obj = new ZipInputStream(((InputStream) (obj)));
        return ((InputStream) (obj));
    }

    public static void modifyRequestToAcceptGzipResponse(HttpRequest httprequest)
    {
        httprequest.addHeader("Accept-Encoding", "gzip");
    }

    public static PIHttpClient newInstance(String s)
    {
        BasicHttpParams basichttpparams = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(basichttpparams, false);
        HttpConnectionParams.setConnectionTimeout(basichttpparams, 15000);
        HttpConnectionParams.setSoTimeout(basichttpparams, 15000);
        HttpConnectionParams.setSocketBufferSize(basichttpparams, 8192);
        HttpClientParams.setRedirecting(basichttpparams, true);
        HttpProtocolParams.setUserAgent(basichttpparams, s);
        SchemeRegistry schemeregistry = new SchemeRegistry();
        schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeregistry.register(new Scheme("https", socketFactory(), 443));
        return new PIHttpClient(new ThreadSafeClientConnManager(basichttpparams, schemeregistry), basichttpparams);
    }

    public static void setThreadBlocked(boolean flag)
    {
        sThreadBlocked.set(Boolean.valueOf(flag));
    }

    private static SSLSocketFactory socketFactory()
    {
        return SSLSocketFactory.getSocketFactory();
    }

    private static String toCurl(HttpUriRequest httpurirequest, boolean flag)
        throws IOException
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("curl ");
        Header aheader[] = httpurirequest.getAllHeaders();
        int i = aheader.length;
        int j = 0;
        Header header;
        do
        {
            if(j >= i)
            {
                java.net.URI uri = httpurirequest.getURI();
                if(httpurirequest instanceof RequestWrapper)
                {
                    HttpRequest httprequest = ((RequestWrapper)httpurirequest).getOriginal();
                    if(httprequest instanceof HttpUriRequest)
                        uri = ((HttpUriRequest)httprequest).getURI();
                }
                stringbuilder.append("\"");
                stringbuilder.append(uri);
                stringbuilder.append("\"");
                if(httpurirequest instanceof HttpEntityEnclosingRequest)
                {
                    HttpEntity httpentity = ((HttpEntityEnclosingRequest)httpurirequest).getEntity();
                    if(httpentity != null && httpentity.isRepeatable())
                        if(httpentity.getContentLength() < 1024L)
                        {
                            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                            httpentity.writeTo(bytearrayoutputstream);
                            String s = bytearrayoutputstream.toString();
                            stringbuilder.append(" --data-ascii \"").append(s).append("\"");
                        } else
                        {
                            stringbuilder.append(" [TOO MUCH DATA TO INCLUDE]");
                        }
                }
                return stringbuilder.toString();
            }
            header = aheader[j];
            if(flag || !header.getName().equals("Authorization") && !header.getName().equals("Cookie"))
            {
                stringbuilder.append("--header \"");
                stringbuilder.append(header.toString().trim());
                stringbuilder.append("\" ");
            }
            j++;
        } while(true);
    }

    public void close()
    {
        if(mLeakedException != null)
        {
            getConnectionManager().shutdown();
            mLeakedException = null;
        }
    }

    public void disableCurlLogging()
    {
        curlConfiguration = null;
    }

    public void enableCurlLogging(String s, int i)
    {
        if(s == null)
            throw new NullPointerException("name");
        if(i < 2 || i > 7)
        {
            throw new IllegalArgumentException("Level is out of range [2..7]");
        } else
        {
            curlConfiguration = new LoggingConfiguration(s, i, null);
            return;
        }
    }

    public Object execute(HttpHost httphost, HttpRequest httprequest, ResponseHandler responsehandler)
        throws IOException, ClientProtocolException
    {
        return _flddelegate.execute(httphost, httprequest, responsehandler);
    }

    public Object execute(HttpHost httphost, HttpRequest httprequest, ResponseHandler responsehandler, HttpContext httpcontext)
        throws IOException, ClientProtocolException
    {
        return _flddelegate.execute(httphost, httprequest, responsehandler, httpcontext);
    }

    public Object execute(HttpUriRequest httpurirequest, ResponseHandler responsehandler)
        throws IOException, ClientProtocolException
    {
        return _flddelegate.execute(httpurirequest, responsehandler);
    }

    public Object execute(HttpUriRequest httpurirequest, ResponseHandler responsehandler, HttpContext httpcontext)
        throws IOException, ClientProtocolException
    {
        return _flddelegate.execute(httpurirequest, responsehandler, httpcontext);
    }

    public HttpResponse execute(HttpHost httphost, HttpRequest httprequest)
        throws IOException
    {
        return _flddelegate.execute(httphost, httprequest);
    }

    public HttpResponse execute(HttpHost httphost, HttpRequest httprequest, HttpContext httpcontext)
        throws IOException
    {
        return _flddelegate.execute(httphost, httprequest, httpcontext);
    }

    public HttpResponse execute(HttpUriRequest httpurirequest)
        throws IOException
    {
        return _flddelegate.execute(httpurirequest);
    }

    public HttpResponse execute(HttpUriRequest httpurirequest, HttpContext httpcontext)
        throws IOException
    {
        return _flddelegate.execute(httpurirequest, httpcontext);
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        if(mLeakedException != null)
        {
            Log.e("PIHttpClient", "Leak found", mLeakedException);
            mLeakedException = null;
        }
    }

    public ClientConnectionManager getConnectionManager()
    {
        return _flddelegate.getConnectionManager();
    }

    public HttpParams getParams()
    {
        return _flddelegate.getParams();
    }

    public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 0L;
    private static final String TAG = "PIHttpClient";
    private static final ThreadLocal sThreadBlocked = new ThreadLocal();
    private static final HttpRequestInterceptor sThreadCheckInterceptor = new HttpRequestInterceptor() {

        public void process(HttpRequest httprequest, HttpContext httpcontext)
        {
            if(PIHttpClient.sThreadBlocked.get() != null && ((Boolean)PIHttpClient.sThreadBlocked.get()).booleanValue())
                throw new RuntimeException("This thread forbids HTTP requests");
            else
                return;
        }

    }
;
    private volatile LoggingConfiguration curlConfiguration;
    private final HttpClient _flddelegate;
    private RuntimeException mLeakedException;

    static
    {
        DEFAULT_SYNC_MIN_GZIP_BYTES = 256L;
    }




}
