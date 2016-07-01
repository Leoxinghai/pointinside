package com.pointinside.android.api.content;


import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.Header;
import org.apache.http.HttpResponse;



import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
//import android.drm.mobile1.DrmRawContent;
import android.net.http.AndroidHttpClient;
import android.net.Uri;
//import android.os.FileUtils;
import android.os.PowerManager;
import android.os.Process;
//import android.provider.Downloads;
//import android.provider.DrmStore;
//import android.util.Config;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Locale;

import com.pointinside.android.api.net.MyHttpClient;

/**
 * Runs an actual download
 */
public class DownloadThread extends Thread {

    private Context mContext;
    private DownloadInfo mInfo;

    private PIContentStore mPIContentStore;

    public DownloadThread(Context paramContext, DownloadInfo paramDownloadInfo)
    {
      this.mContext = paramContext;
      this.mInfo = paramDownloadInfo;
      this.mPIContentStore = PIContentStore.getInstance(paramContext);
    }


    /**
     * Executes the download in a separate thread
     */
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        int finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
        boolean countRetry = false;
        int retryAfter = 0;
        int redirectCount = mInfo.mRedirectCount;//redirectCount;
        String newUri = null;
        boolean gotData = false;
        String filename = null;
        FileOutputStream stream = null;
//        AndroidHttpClient client = null;
        MyHttpClient client = null;
        PIContentManager.ContentType contenttype;
        ContentManagerUtils.DownloadFileInfo downloadfileinfo;
        Context context = mContext;
        String s5 = mInfo.mRemoteUri;
        contenttype = mInfo.mType;
        int statusCode  = 0;

        PowerManager.WakeLock wakeLock = null;
        Uri contentUri = Uri.parse(Downloads.CONTENT_URI + "/" + mInfo.mId);//id);

        try {
            boolean continuingDownload = false;
            String headerAcceptRanges = null;
            String headerContentDisposition = null;
            String headerContentLength = null;
            String headerContentLocation = null;
            String headerETag = null;
            String headerTransferEncoding = null;

            byte data[] = new byte[XHConstants2.BUFFER_SIZE];

            int bytesSoFar = 0;

            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, XHConstants2.TAG);
            wakeLock.acquire();

            filename = mInfo.mFileName;
            contenttype = mInfo.mType;

            if (filename != null) {
                if (!ContentManagerUtils.isFilenameValid(mContext,filename)) {
                    finalStatus = Downloads.STATUS_FILE_ERROR;
                    notifyDownloadCompleted(
                            finalStatus, false, 0, 0, false, filename, null);
                    return;
                }
                // We're resuming a download that got interrupted
                File f = new File(filename);
                if (f.exists()) {
                    long fileLength = f.length();
                    if (fileLength == 0) {
                        // The download hadn't actually started, we can restart from scratch
                        f.delete();
                        filename = null;
//                    } else if (mInfo.mIdentifier == null && !mInfo.noIntegrity) {
/*                    } else if (mInfo.mIdentifier == null) {
                        // Tough luck, that's not a resumable download
                            Log.d(XHConstants2.TAG,
                                    "can't resume interrupted non-resumable download");
                        f.delete();
                        finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
                        notifyDownloadCompleted(
                                finalStatus, false, 0, 0, false, filename, null);
                        return;
*/                        
                    } else {
                        // All right, we'll be able to resume this download
                        stream = new FileOutputStream(filename, true);
                        bytesSoFar = (int) fileLength;
                        if (mInfo.mTotalBytes != -1) {
                            headerContentLength = Integer.toString(mInfo.mTotalBytes);
                        }
                        headerETag = mInfo.mIdentifier;
                        continuingDownload = true;
                    }
                }
            }

            int bytesNotified = bytesSoFar;
            // starting with MIN_VALUE means that the first write will commit
            //     progress to the database
            long timeLastNotification = 0;

//            client = AndroidHttpClient.newInstance(userAgent());
            client = PIHttpClient.newInstance(Downloads.DownloadConstants.DEFAULT_USER_AGENT, mContext);
 /*
            if (stream != null && mInfo.destination == Downloads.DESTINATION_EXTERNAL
                        && !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
                        .equalsIgnoreCase(mimeType)) {
                try {
                    stream.close();
                    stream = null;
                } catch (IOException ex) {
                    Log.v(XHConstants2.TAG, "exception when closing the file before download : " +
                            ex);
                    // nothing can really be done if the file can't be closed
                }
            }
            */

            /*
             * This loop is run once for every individual HTTP request that gets sent.
             * The very first HTTP request is a "virgin" request, while every subsequent
             * request is done with the original ETag and a byte-range.
             */
http_request_loop:
            while (true) {
                // Prepares the request and fires it.
            	System.out.println("mInfo.mRemoteUri. "+mInfo.mRemoteUri);
                HttpGet request = new HttpGet(mInfo.mRemoteUri);//mUri);

                if (XHConstants2.LOGV) {
                    Log.v(XHConstants2.TAG, "initiating download for " + mInfo.mRemoteUri);
                }

                if (continuingDownload) {
                    if (headerETag != null) {
                        request.addHeader("If-Match", headerETag);
                    }
                    request.addHeader("Range", "bytes=" + bytesSoFar + "-");
                }

                HttpResponse response;
                try {
                    response = client.execute(request);
                } catch (IllegalArgumentException ex) {
                    Log.d(XHConstants2.TAG, "Arg exception trying to execute request for " +
                            mInfo.mUri + " : " + ex);
                    finalStatus = Downloads.STATUS_BAD_REQUEST;
                    request.abort();
                    break http_request_loop;
                } catch (IOException ex) {
                    if (!ContentManagerUtils.isNetworkAvailable(mContext)) {
                        finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                    } else if (mInfo.mNumFailed < XHConstants2.MAX_RETRIES) {
                        finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                        countRetry = true;
                    } else {
	                    Log.d(XHConstants2.TAG, "IOException trying to execute request for " +
	                            mInfo.mUri + " : " + ex);
                        finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
                    }
                    request.abort();
                    break http_request_loop;
                }

                statusCode = response.getStatusLine().getStatusCode();
            	System.out.println("download " +statusCode +":"+ mInfo.mRemoteUri+";"+bytesSoFar);
                if(statusCode == 404){
                	System.out.println("404 error "+ mInfo.mRemoteUri);
                } else if(statusCode == 412){
                	System.out.println("412 error "+ mInfo.mRemoteUri);
                }

                if ((statusCode ==404 || statusCode == 503) && mInfo.mNumFailed < XHConstants2.MAX_RETRIES) {
                    Log.v(XHConstants2.TAG, "got HTTP response code 503");
                    finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                    countRetry = true;
                    Header header = response.getFirstHeader("Retry-After");
                    if (header != null) {
                       try {
                           Log.v(XHConstants2.TAG, "Retry-After :" + header.getValue());
                           retryAfter = Integer.parseInt(header.getValue());
                           if (retryAfter < 0) {
                               retryAfter = 0;
                           } else {
                               if (retryAfter < XHConstants2.MIN_RETRY_AFTER) {
                                   retryAfter = XHConstants2.MIN_RETRY_AFTER;
                               } else if (retryAfter > XHConstants2.MAX_RETRY_AFTER) {
                                   retryAfter = XHConstants2.MAX_RETRY_AFTER;
                               }
                               retryAfter += ContentManagerUtils.sRandom.nextInt(XHConstants2.MIN_RETRY_AFTER + 1);
                               retryAfter *= 1000;
                           }
                       } catch (NumberFormatException ex) {
                    	   ex.printStackTrace();
                           // ignored - retryAfter stays 0 in this case.
                       }
                    }
                    request.abort();
                    break http_request_loop;
                }
                if (statusCode == 301 ||
                        statusCode == 302 ||
                        statusCode == 303 ||
                        statusCode == 307) {
                        Log.v(XHConstants2.TAG, "got HTTP redirect " + statusCode);
                    if (redirectCount >= XHConstants2.MAX_REDIRECTS) {
                        Log.d(XHConstants2.TAG, "too many redirects for download " + mInfo.mId +
                                " at " + mInfo.mUri);
                        finalStatus = Downloads.STATUS_TOO_MANY_REDIRECTS;
                        request.abort();
                        break http_request_loop;
                    }
                    Header header = response.getFirstHeader("Location");
                    if (header != null) {
                        Log.v(XHConstants2.TAG, "Location :" + header.getValue());
                        newUri = new URI(mInfo.mUri).resolve(new URI(header.getValue())).toString();
                        ++redirectCount;
                        finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                        request.abort();
                        break http_request_loop;
                    }
                }
                if ((!continuingDownload && statusCode != Downloads.STATUS_SUCCESS)
                        || (continuingDownload && statusCode != 206)) {
                    Log.d(XHConstants2.TAG, "http error " + statusCode + " for " + mInfo.mUri);
                    if (Downloads.isStatusError(statusCode)) {
                        finalStatus = statusCode;
                    } else if (statusCode >= 300 && statusCode < 400) {
                        finalStatus = Downloads.STATUS_UNHANDLED_REDIRECT;
                    } else if (continuingDownload && statusCode == Downloads.STATUS_SUCCESS) {
                        finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
                    } else {
                        finalStatus = Downloads.STATUS_UNHANDLED_HTTP_CODE;
                    }
                    request.abort();
                    break http_request_loop;
                } else {
                    // Handles the response, saves the file
                    Log.v(XHConstants2.TAG, "received response for " + mInfo.mUri);

                    if (!continuingDownload) {
                        Header header = response.getFirstHeader("Accept-Ranges");
                        if (header != null) {
                            headerAcceptRanges = header.getValue();
                        }
                        header = response.getFirstHeader("Content-Disposition");
                        if (header != null) {
                            headerContentDisposition = header.getValue();
                        }
                        header = response.getFirstHeader("Content-Location");
                        if (header != null) {
                            headerContentLocation = header.getValue();
                        }
                        header = response.getFirstHeader("ETag");
                        if (header != null) {
                            headerETag = header.getValue();
                        }
                        header = response.getFirstHeader("Transfer-Encoding");
                        if (header != null) {
                            headerTransferEncoding = header.getValue();
                        }
                        if (headerTransferEncoding == null) {
                            header = response.getFirstHeader("Content-Length");
                            if (header != null) {
                                headerContentLength = header.getValue();
                            }
                        } else {
                            // Ignore content-length with transfer-encoding - 2616 4.4 3
                            Log.v(XHConstants2.TAG,
                                    "ignoring content-length because of xfer-encoding");
                        }
                        if (XHConstants2.LOGVV) {
                            Log.v(XHConstants2.TAG, "Accept-Ranges: " + headerAcceptRanges);
                            Log.v(XHConstants2.TAG, "Content-Disposition: " +
                                    headerContentDisposition);
                            Log.v(XHConstants2.TAG, "Content-Length: " + headerContentLength);
                            Log.v(XHConstants2.TAG, "Content-Location: " + headerContentLocation);
                            Log.v(XHConstants2.TAG, "ETag: " + headerETag);
                            Log.v(XHConstants2.TAG, "Transfer-Encoding: " + headerTransferEncoding);
                        }
 /*
                        if (!mInfo.noIntegrity && headerContentLength == null &&
                                (headerTransferEncoding == null
                                        || !headerTransferEncoding.equalsIgnoreCase("chunked"))
                                ) {

                        	Log.d(XHConstants2.TAG, "can't know size of download, giving up");
                            finalStatus = Downloads.STATUS_LENGTH_REQUIRED;
                            request.abort();
                            break http_request_loop;
                        }
*/

                        /*
                        DownloadFileInfo fileInfo = ContentManagerUtils.generateSaveFile(
                                mContext,
                                mInfo.mUri,
                                mInfo.hint,
                                headerContentDisposition,
                                headerContentLocation,
                                mimeType,
                                mInfo.destination,
                                (headerContentLength != null) ?
                                        Integer.parseInt(headerContentLength) : 0);
                        */
                      downloadfileinfo = ContentManagerUtils.generateSaveFile(mContext, s5, contenttype,
                              (headerContentLength != null) ?
                                      Integer.parseInt(headerContentLength) : 0);


                        if (downloadfileinfo.mFileName == null) {
                            finalStatus = downloadfileinfo.mStatus;
                            request.abort();
                            break http_request_loop;
                        }
                        filename = downloadfileinfo.mFileName;
                        stream = downloadfileinfo.mStream;

                        Log.v(XHConstants2.TAG, "writing " + mInfo.mUri + " to " + filename);

                        ContentValues values = new ContentValues();
                        int contentLength = -1;
                        if (headerContentLength != null) {
                            contentLength = Integer.parseInt(headerContentLength);
                        }

                        ContentValues contentvalues = new ContentValues();
            	         contentvalues.put("file_name", filename);

            	        contentvalues.put("total_bytes", Integer.valueOf(contentLength));
            	        mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues, null, null);
//            	        this.mPIContentStore.update(contentUri, values, null, null);
                    }

                    InputStream entityStream;
                    try {
                        entityStream = response.getEntity().getContent();
                    } catch (IOException ex) {
                        //if (!ContentManagerUtils.isNetworkAvailable(mContext)) {
                        if(!ContentManagerUtils.isNetworkAvailable(mContext)) {
                            finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                        } else if (mInfo.mNumFailed < XHConstants2.MAX_RETRIES) {
                            finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                            countRetry = true;
                        } else {
                            Log.d(XHConstants2.TAG, "IOException getting entity for " + mInfo.mUri +
                                " : " + ex);
                            finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
                        }
                        request.abort();
                        break http_request_loop;
                    }
                    for (;;) {
                        int bytesRead;
                        try {
                            bytesRead = entityStream.read(data);
                        } catch (IOException ex) {
                            ContentValues values = new ContentValues();
                            values.put("current_bytes", bytesSoFar);
                            this.mPIContentStore.update(contentUri, values, null, null);
//                            if (!mInfo.noIntegrity && headerETag == null) {
                            if ( headerETag == null) {
                                    Log.v(XHConstants2.TAG, "download IOException for " + mInfo.mUri +
                                            " : " + ex);
                                    Log.d(XHConstants2.TAG,
                                            "can't resume interrupted download with no ETag");
                                finalStatus = Downloads.STATUS_PRECONDITION_FAILED;
                            } else if (!ContentManagerUtils.isNetworkAvailable(mContext)) {
                                finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                            } else if (mInfo.mNumFailed < XHConstants2.MAX_RETRIES) {
                                finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                                countRetry = true;
                            } else {
                                    Log.v(XHConstants2.TAG, "download IOException for " + mInfo.mUri +
                                            " : " + ex);
                                finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
                            }
                            request.abort();
                            break http_request_loop;
                        }
                        if (bytesRead == -1) { // success
                            ContentValues values = new ContentValues();
                            values.put(Downloads.DownloadColumns.COLUMN_CURRENT_BYTES, bytesSoFar);
                            if (headerContentLength == null) {
                                values.put("total_bytes", bytesSoFar);
                            }
                            this.mPIContentStore.update(contentUri, values, null, null);
                            if ((headerContentLength != null)
                                    && (bytesSoFar
                                            != Integer.parseInt(headerContentLength))) {
//                                if (!mInfo.noIntegrity && headerETag == null) {
                                if (headerETag == null) {
                                        Log.d(XHConstants2.TAG, "mismatched content length " +
                                                mInfo.mUri);
                                    finalStatus = Downloads.STATUS_LENGTH_REQUIRED;
                                } else if (!ContentManagerUtils.isNetworkAvailable(mContext)) {
                                    finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                                } else if (mInfo.mNumFailed < XHConstants2.MAX_RETRIES) {
                                    finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                                    countRetry = true;
                                } else {
                                    Log.v(XHConstants2.TAG, "closed socket for " + mInfo.mUri);
                                    finalStatus = Downloads.STATUS_HTTP_DATA_ERROR;
                                }
                                break http_request_loop;
                            }
                            break;
                        }
                        gotData = true;
                        for (;;) {
                            try {
                                if (stream == null) {
                                    stream = new FileOutputStream(filename, true);
                                }
                                stream.write(data, 0, bytesRead);
                                /*
                                if (mInfo.destination == XHDownloads.DESTINATION_EXTERNAL
                                            && !DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
                                            .equalsIgnoreCase(mimeType)) {
                                    try {
                                        stream.close();
                                        stream = null;
                                    } catch (IOException ex) {
                                        if (XHConstants2.LOGV) {
                                            Log.v(XHConstants2.TAG,
                                                    "exception when closing the file " +
                                                    "during download : " + ex);
                                        }
                                        // nothing can really be done if the file can't be closed
                                    }
                                }
                                */
                                break;
                            } catch (IOException ex) {
                                if (!ContentManagerUtils.discardPurgeableFiles(
                                        mContext, XHConstants2.BUFFER_SIZE)) {
                                    finalStatus = Downloads.STATUS_FILE_ERROR;
                                    break http_request_loop;
                                }
                            }
                        }
                        bytesSoFar += bytesRead;
                        long now = System.currentTimeMillis();
                        if (bytesSoFar - bytesNotified > XHConstants2.MIN_PROGRESS_STEP
                                && now - timeLastNotification
                                        > XHConstants2.MIN_PROGRESS_TIME) {
                            ContentValues values = new ContentValues();
                            values.put(Downloads.DownloadColumns.COLUMN_CURRENT_BYTES, bytesSoFar);
                            this.mPIContentStore.update(
                                    contentUri, values, null, null);
                            bytesNotified = bytesSoFar;
                            timeLastNotification = now;
                        }

                        if (XHConstants2.LOGVV) {
                            Log.v(XHConstants2.TAG, "downloaded " + bytesSoFar + " for " + mInfo.mUri);
                        }
                        //synchronized(mInfo) {
                            if (mInfo.mControl == Downloads.CONTROL_PAUSED) {
                                if (XHConstants2.LOGV) {
                                    Log.v(XHConstants2.TAG, "paused " + mInfo.mUri);
                                }
                                finalStatus = Downloads.STATUS_RUNNING_PAUSED;
                                request.abort();
                                break http_request_loop;
                            }
                        //}
                        if (mInfo.mStatus == Downloads.STATUS_CANCELED) {
                            Log.d(XHConstants2.TAG, "canceled " + mInfo.mUri);
                            finalStatus = Downloads.STATUS_CANCELED;
                            break http_request_loop;
                        }
                    }
                    if (XHConstants2.LOGV) {
                        Log.v(XHConstants2.TAG, "download completed for " + mInfo.mUri);
                    }
                    finalStatus = Downloads.STATUS_SUCCESS;
                }
            	System.out.println("successful " +statusCode +":"+ mInfo.mRemoteUri+";"+bytesSoFar);
                break;
            }
        } catch (FileNotFoundException ex) {
            Log.d(XHConstants2.TAG, "FileNotFoundException for " + filename + " : " +  ex);
            finalStatus = Downloads.STATUS_FILE_ERROR;
            // falls through to the code that reports an error
        } catch (Exception ex) { //sometimes the socket code throws unchecked exceptions
            Log.d(XHConstants2.TAG, "Exception for " + mInfo.mUri, ex);
            finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
            // falls through to the code that reports an error
        } finally {
            mInfo.mHasActiveThread = false;
            if (wakeLock != null) {
                wakeLock.release();
                wakeLock = null;
            }
            if (client != null) {
                client.close();
                client = null;
            }
            try {
                // close the file
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                Log.v(XHConstants2.TAG, "exception when closing the file after download : " + ex);
                // nothing can really be done if the file can't be closed
            }
            if (filename != null) {
                // if the download wasn't successful, delete the file
                if (Downloads.isStatusError(finalStatus)) {
                    new File(filename).delete();
                    filename = null;
/*
                } else if (Downloads.isStatusSuccess(finalStatus) &&
                        DrmRawContent.DRM_MIMETYPE_MESSAGE_STRING
                        .equalsIgnoreCase(mimeType)) {
                    // transfer the file to the DRM content provider
                    File file = new File(filename);
                    Intent item = DrmStore.addDrmFile(this.mPIContentStore, file, null);
                    if (item == null) {
                        Log.w(XHConstants2.TAG, "unable to add file " + filename + " to DrmProvider");
                        finalStatus = Downloads.STATUS_UNKNOWN_ERROR;
                    } else {
                        filename = item.getDataString();
                    }

                    file.delete();
*/
                } else if (Downloads.isStatusSuccess(finalStatus)) {
                    // make sure the file is readable
                    //FileUtils.setPermissions(filename, 0644, -1, -1);
                }
            }
            if(statusCode == 416) {
            	finalStatus = Downloads.STATUS_SUCCESS;
            }
            notifyDownloadCompleted(finalStatus, countRetry, retryAfter, redirectCount,
                    gotData, filename, newUri);
        }
    }


    private void notifyDownloadCompleted(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, boolean paramBoolean2, String paramString1, String paramString2)
    {
      notifyThroughDatabase(paramInt1, paramBoolean1, paramInt2, paramInt3, paramBoolean2, paramString1, paramString2);
      if (Downloads.isStatusCompleted(paramInt1)) {
        notifyThroughBroadcast();
      }
    }

    private void notifyThroughBroadcast()
    {
      Intent localIntent = new Intent("pointinside.intent.action.DOWNLOAD_COMPLETE");
      localIntent.putExtra("download_id", this.mInfo.mId);
      localIntent.putExtra("venue_code", this.mInfo.mVenueUUID);
      System.out.println("notifyThroughBroadcast."+ this.mContext);
      this.mContext.sendBroadcast(localIntent);
    }

    private void notifyThroughDatabase(int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, boolean paramBoolean2, String paramString1, String paramString2)
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("status", Integer.valueOf(paramInt1));
      if ((paramInt1 != 304) && (Downloads.isStatusSuccess(paramInt1))) {
        localContentValues.put("extracted", Integer.valueOf(0));
      }
      if (paramString2 != null) {
        localContentValues.put("remote_uri", paramString2);
      }
      localContentValues.put("lastmod", Long.valueOf(System.currentTimeMillis()));
      localContentValues.put("retry_after", Integer.valueOf(paramInt2 + (paramInt3 << 28)));
      if (!paramBoolean1) {
        localContentValues.put("numfailed", Integer.valueOf(0));
      }
      if (paramBoolean2) {
          localContentValues.put("numfailed", Integer.valueOf(1));
        } else {
          localContentValues.put("numfailed", Integer.valueOf(1 + this.mInfo.mNumFailed));
        }
        this.mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, this.mInfo.mId), localContentValues, null, null);
        return;
    }



}
