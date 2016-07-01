// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.piwebservices.net;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.net.MyHttpClient;
import com.pointinside.android.api.utils.IOUtils;
import java.io.*;
import java.util.zip.GZIPOutputStream;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.message.BasicHeader;

// Referenced classes of package com.pointinside.android.piwebservices.net:
//            PIWebServices

public class PIPostTouchstreamClient
{
    private static class GzipCompressingEntity extends HttpEntityWrapper
    {

        public InputStream getContent()
            throws IOException
        {
            PipedInputStream pipedinputstream = new PipedInputStream();
            PipedOutputStream pipedoutputstream = new PipedOutputStream();
            pipedinputstream.connect(pipedoutputstream);
            (new WriterThread(wrappedEntity.getContent(), new GZIPOutputStream(pipedoutputstream))).start();
            return pipedinputstream;
        }

        public Header getContentEncoding()
        {
            return new BasicHeader("Content-Encoding", "gzip");
        }

        public long getContentLength()
        {
            return -1L;
        }

        public void writeTo(OutputStream outputstream)
            throws IOException
        {
            super.writeTo(new GZIPOutputStream(outputstream));
        }

        public GzipCompressingEntity(HttpEntity httpentity)
        {
            super(httpentity);
        }
        private static class WriterThread extends Thread
        {

            private void doWrite()
                throws IOException
            {
                byte abyte0[] = new byte[4096];

                while(true) {
                	int i = in.read(abyte0);
	                if(i != -1) {
	                    out.write(abyte0, 0, i);
	                }
	                else
	                	break;
            	}

                out.flush();
                IOUtils.closeQuietly(in);
                out.close();
                return;
            }

            public void run()
            {
                try
                {
                    doWrite();
                    return;
                }
                catch(IOException ioexception)
                {
                    Log.w(PIPostTouchstreamClient.TAG, "Error writing", ioexception);
                }
            }

            private final InputStream in;
            private final OutputStream out;

            public WriterThread(InputStream inputstream, OutputStream outputstream)
            {
                in = inputstream;
                out = outputstream;
            }
        }
    }




    public PIPostTouchstreamClient(Context context, String s)
    {
        mPath = s;
        mContext = context;
        mClient = PIWebServices.createHttpClient(context);
        mClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1), PIMapReference.getCredentials());
    }

    public void run()
        throws IOException
    {
        HttpResponse httpresponse;
        HttpPost httppost = new HttpPost(PI_TOUCHSTREAM_URI.toString());
        if(mPath == null || mPath.length() <= 0)
        	throw new IOException("Error posting touchstream. No file provided.");
        File file = new File(mPath);
        if(!file.exists() || file.length() <= 0L)
            throw new IOException("Error posting touchstream. Posting empty data.");
        httppost.setEntity(new FileEntity(file, "text/plain"));
        httpresponse = mClient.execute(httppost);
        if(httpresponse.getStatusLine().getStatusCode() != 200)
            throw new IOException((new StringBuilder("Error posting data: ")).append(httpresponse.getStatusLine().getStatusCode()).toString());        httpresponse.getEntity().consumeContent();
        mClient.close();
        return;
    }

    public static final Uri PI_TOUCHSTREAM_URI = Uri.parse("https://smartmaps.pointinside.com/iphone/v2_0/touchstream");
    private static final String TAG = PIPostTouchstreamClient.class.getSimpleName();
    protected MyHttpClient mClient;
    protected Context mContext;
    private String mPath;
    protected Uri mUri;


}
