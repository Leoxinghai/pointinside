package com.pointinside.android.api.content;


import static android.text.format.DateUtils.SECOND_IN_MILLIS;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;
import static java.net.HttpURLConnection.HTTP_USE_PROXY;
import static java.net.HttpURLConnection.HTTP_UNSUPPORTED_TYPE;
import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_UNSUPPORTED_TYPE;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import java.io.*;
import com.pointinside.android.api.net.MyHttpClient;
import android.os.PowerManager;
//import org.apache.http.Header;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import android.util.Log;
import android.os.Process;
import java.net.*;

class XHDownloadThread
  extends Thread
{
  private Context mContext;
  private DownloadInfo mInfo;
  private PIContentStore mPIContentStore;

  public XHDownloadThread(Context paramContext, DownloadInfo paramDownloadInfo)
  {
    this.mContext = paramContext;
    this.mInfo = paramDownloadInfo;
    this.mPIContentStore = PIContentStore.getInstance(paramContext);
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

  public void run()
  {
      /*
	  int i;
      boolean flag;
      int j;
      int k;
      String s;
      String s6;
      boolean flag1;
      String s1;
      FileOutputStream fileoutputstream;
      MyHttpClient myhttpclient;
      android.os.PowerManager.WakeLock wakelock;
      Process.setThreadPriority(10);
      i = Downloads.STATUS_UNKNOWN_ERROR;
      flag = false;
      j = 0;
      k = mInfo.mRedirectCount;
      s = null;
      flag1 = false;
      s1 = null;
      fileoutputstream = null;
      myhttpclient = null;
      wakelock = null;
      String s2;
      byte abyte0[];
      s2 = mInfo.mIdentifier;
      abyte0 = new byte[4096];
      int l = 0;
      wakelock = ((PowerManager)mContext.getSystemService("power")).newWakeLock(1, "PIMaps");
      wakelock.acquire();
      int i1;
      long l1;
      i1 = 0;
      l1 = 0L;
      Header header;
      int k1;
      int i2;
      int j2;

      Header header1;
      String s3;
      Header header2;
      Header header3;
      Header header4;
      Header header5;
      Header header6;
      String s4;
      Header header7;
      Context context;
      String s5;
      PIContentManager.ContentType contenttype;
      ContentManagerUtils.DownloadFileInfo downloadfileinfo;
      ContentValues contentvalues;
      ContentValues contentvalues1;
      int l2;
      boolean flag2;
      long l3;
      DownloadInfo downloadinfo;
      int i3;
      ContentValues contentvalues2;
      ContentValues contentvalues3;
      ContentValues contentvalues4;

      int k2;

      MyHttpClient myhttpclient1 = PIHttpClient.newInstance(Downloads.DownloadConstants.DEFAULT_USER_AGENT, mContext);
      myhttpclient = myhttpclient1;
      fileoutputstream = null;

      //_L51:
      HttpGet httpget = new HttpGet(mInfo.mRemoteUri);

      if(s2 != null) {
          httpget.addHeader("If-None-Match", s2);
      } else
    	  httpget.addHeader("If-Match", s2);
      httpget.addHeader("Range", (new StringBuilder("bytes=")).append(0).append("-").toString());

      HttpResponse httpresponse = myhttpclient.execute(httpget);
	  int j1 = httpresponse.getStatusLine().getStatusCode();
      if(j1 == HTTP_NOT_MODIFIED) {
	      i = HTTP_NOT_MODIFIED;
	      httpget.abort();
	      return;
      }
      if(j1 == HTTP_UNAVAILABLE) {
	      if(mInfo.mNumFailed >= 1) {
	    		  if(j1 == HTTP_MOVED_PERM || j1 == HTTP_MOVED_TEMP || j1 == HTTP_SEE_OTHER || j1 == 307) {
	    			  _L18:
	    			      if(k < 5)
	    			          break MISSING_BLOCK_LABEL_898;
	    			      Log.d("PIMaps", (new StringBuilder("too many redirects for download ")).append(mInfo.mId).toString());
	    			      i = 497;
	    			      httpget.abort();
	    			      flag = false;
	    			      j = 0;
	    			        goto _L7
	    			      header1 = httpresponse.getFirstHeader("Location");
	    			      if(header1 == null) goto _L19; else goto _L20
	    	      } else {
	    	    	  goto _L19;
	    	      }


	      } else {
	    	  goto _L13
	      }
      }
      if(j1 == HTTP_MOVED_PERM || j1 == HTTP_MOVED_TEMP || j1 == HTTP_SEE_OTHER || j1 == 307) {
    	      if(k < 5) {
	    	      header1 = httpresponse.getFirstHeader("Location");
	    	      if(header1 != null) {
	    	    	      s6 = (new URI(mInfo.mRemoteUri)).resolve(new URI(header1.getValue())).toString();
	    	    	      s = s6;
	    	    	      k++;
	    	    	      i = Downloads.STATUS_RUNNING_PAUSED;
	    	    	      httpget.abort();
	    	    	      flag = false;
	    	    	      j = 0;
	    	    	      // return;
	    	    	      //urisyntaxexception;
	    	    	      Log.d("PIMaps", (new StringBuilder("Couldn't resolve redirect URI for download ")).append(mInfo.mId).toString());
	    	    	      i = 400;
	    	    	      httpget.abort();
	    	    	      flag = false;
	    	    	      j = 0;
	    	    	      flag1 = false;
	    	    	      return;
	    	      }

    	      }
    	      Log.d("PIMaps", (new StringBuilder("too many redirects for download ")).append(mInfo.mId).toString());
    	      i = 497;
    	      httpget.abort();
    	      flag = false;
    	      j = 0;
    	      flag1 = false;
    	      return;
      }

      _L19:
      if(j1 == 200  || j1 == 206 ) {
    	      _L21:
    	          flag = false;
    	          j = 0;
    	          header2 = httpresponse.getFirstHeader("Accept-Ranges");
    	          if(header2 == null) {
    	              break MISSING_BLOCK_LABEL_1131;
    	          }
    	          header2.getValue();
    	          header3 = httpresponse.getFirstHeader("Content-Disposition");
    	          if(header3 == null)
    	              break MISSING_BLOCK_LABEL_1172;
    	          header3.getValue();
    	          header4 = httpresponse.getFirstHeader("Content-Location");
    	          flag = false;
    	          j = 0;
    	          if(header4 == null)
    	              break MISSING_BLOCK_LABEL_1213;
    	          header4.getValue();
    	          header5 = httpresponse.getFirstHeader("ETag");
    	          flag = false;
    	          j = 0;
    	          if(header5 == null)
    	              break MISSING_BLOCK_LABEL_1255;
    	          s2 = header5.getValue();
    	          header6 = httpresponse.getFirstHeader("Transfer-Encoding");
    	          if(header6 == null)
    	              break MISSING_BLOCK_LABEL_1300;
    	          s4 = header6.getValue();
    	          if(s4 != null)
    	              break MISSING_BLOCK_LABEL_1353;
    	          header7 = httpresponse.getFirstHeader("Content-Length");
    	          if(header7 == null)
    	              break MISSING_BLOCK_LABEL_1353;
    	          s3 = header7.getValue();
    	          if(s3 != null)
    	              break MISSING_BLOCK_LABEL_1402;
    	          if(s4 == null)
    	              break MISSING_BLOCK_LABEL_1374;
    	          if(s4.equalsIgnoreCase("chunked"))
    	              break MISSING_BLOCK_LABEL_1402;
    	          i = 411;
    	          httpget.abort();
    	          flag = false;
    	          j = 0;
    	          flag1 = false;
    	            goto _L7


    	          context = mContext;
    	          s5 = mInfo.mRemoteUri;
    	          contenttype = mInfo.mType;
    	          flag = false;
    	          j = 0;
    	          if(s3 == null) {
    	              j2 = 0;
    	              goto _L61
    	          } else {
    	        	  j2 = Integer.parseInt(s3);
    	          }

      } else {
    	  goto _L59
      }

  _L61:
      downloadfileinfo = ContentManagerUtils.generateSaveFile(context, s5, contenttype, j2);
      if(downloadfileinfo.mFileName != null) {
    	        s1 = downloadfileinfo.mFileName;
    	        fileoutputstream = downloadfileinfo.mStream;
    	        contentvalues = new ContentValues();
    	        contentvalues.put("file_name", s1);
    	        k2 = -1;
    	        flag = false;
    	        j = 0;
    	        if(s3 == null)
    	            break MISSING_BLOCK_LABEL_1563;
    	        k2 = Integer.parseInt(s3);
    	        contentvalues.put("total_bytes", Integer.valueOf(k2));
    	        mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues, null, null);
    	_L22:
    	        inputstream = httpresponse.getEntity().getContent();
    	_L45:
    	        l2 = inputstream.read(abyte0);
    	        if(l2 != -1) {
    	                flag1 = true;
    	                fileoutputstream1 = fileoutputstream;

    	                if(fileoutputstream1 != null) {
    	                	goto _L39;
    	                	_L39:
    	                        fileoutputstream = fileoutputstream1;
    	                          goto _L58
    	                          _L58:
    	                              fileoutputstream.write(abyte0, 0, l2);
    	                              fileoutputstream.close();
    	                              fileoutputstream = null;
    	                      _L52:
    	                              l += l2;
    	                              l3 = System.currentTimeMillis();
    	                              if(l - i1 <= 500)
    	                                  break MISSING_BLOCK_LABEL_2309;
    	                              i3 = l3 - l1 != 500L;
    	                              flag = false;
    	                              j = 0;
    	                              s = null;
    	                              if(i3 <= 0)
    	                                  break MISSING_BLOCK_LABEL_2309;
    	                              contentvalues2 = new ContentValues();
    	                              contentvalues2.put("current_bytes", Integer.valueOf(l));
    	                              mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues2, null, null);
    	                              i1 = l;
    	                              l1 = l3;
    	                              downloadinfo = mInfo;
    	                              downloadinfo;
    	                              JVM INSTR monitorenter ;
    	                              if(mInfo.mControl != 1) goto _L41; else goto _L40


    	                } else{
    	                   goto _L38
    	                }

    	        } else {
    	        	goto _L27
    	        	_L27:
    	                contentvalues3 = new ContentValues();
    	                contentvalues3.put("current_bytes", Integer.valueOf(l));
    	                if(s3 != null)
    	                    break MISSING_BLOCK_LABEL_1670;
    	                contentvalues3.put("total_bytes", Integer.valueOf(l));
    	                mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues3, null, null);
    	                flag = false;
    	                j = 0;
    	                s = null;
    	                if(s3 == null) goto _L30; else goto _L29

    	        }

      }else {
    	  goto _L25
      }


_L7:
      Exception exception1;
      FileOutputStream fileoutputstream1;
      mInfo.mHasActiveThread = false;
      if(wakelock != null)
          wakelock.release();
      if(myhttpclient != null)
          myhttpclient.close();
      Exception exception;
      RuntimeException runtimeexception;
      IOException ioexception;
      SyncFailedException syncfailedexception;
      FileNotFoundException filenotfoundexception;
      IOException ioexception1;
      RuntimeException runtimeexception1;
      RuntimeException runtimeexception2;
      IOException ioexception2;
      SyncFailedException syncfailedexception1;
      FileNotFoundException filenotfoundexception1;
      IOException ioexception3;
      FileNotFoundException filenotfoundexception2;
      RuntimeException runtimeexception3;
      IOException ioexception4;
      SyncFailedException syncfailedexception2;
      FileNotFoundException filenotfoundexception3;
      IOException ioexception5;
      IOException ioexception6;
      IllegalArgumentException illegalargumentexception;

      IOException ioexception9;
      InputStream inputstream;
      IOException ioexception10;

      IOException ioexception12;
      Exception exception2;
      URISyntaxException urisyntaxexception;

      IOException ioexception14;
      if(fileoutputstream != null)
          try
          {
              fileoutputstream.close();
          }
          catch(IOException ioexception8) { }
      if(s1 != null)
          if(Downloads.isStatusError(i))
          {
              (new File(s1)).delete();
              s1 = null;
          } else
          if(Downloads.isStatusSuccess(i))
              try
              {
                  (new FileOutputStream(s1, true)).getFD().sync();
              }
              catch(FileNotFoundException filenotfoundexception4)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" not found: ").append(filenotfoundexception4).toString());
              }
              catch(SyncFailedException syncfailedexception3)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" sync failed: ").append(syncfailedexception3).toString());
              }
              catch(IOException ioexception7)
              {
                  Log.w("PIMaps", (new StringBuilder("IOException trying to sync ")).append(s1).append(": ").append(ioexception7).toString());
              }
              catch(RuntimeException runtimeexception4)
              {
                  Log.w("PIMaps", "exception while syncing file: ", runtimeexception4);
              }
      notifyDownloadCompleted(i, flag, j, k, flag1, s1, s);
      return;


_L55:
      mInfo.mHasActiveThread = false;
      if(wakelock != null)
          wakelock.release();
      if(myhttpclient != null)
          myhttpclient.close();
      if(fileoutputstream != null)
          try
          {
              fileoutputstream.close();
          }
          // Misplaced declaration of an exception variable
          catch(IOException ioexception5) { }
      if(s1 != null)
          if(Downloads.isStatusError(492))
          {
              (new File(s1)).delete();
              s1 = null;
          } else
          if(Downloads.isStatusSuccess(492))
              try
              {
                  (new FileOutputStream(s1, true)).getFD().sync();
              }
              // Misplaced declaration of an exception variable
              catch(FileNotFoundException filenotfoundexception3)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" not found: ").append(filenotfoundexception3).toString());
              }
              // Misplaced declaration of an exception variable
              catch(SyncFailedException syncfailedexception2)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" sync failed: ").append(syncfailedexception2).toString());
              }
              // Misplaced declaration of an exception variable
              catch(IOException ioexception4)
              {
                  Log.w("PIMaps", (new StringBuilder("IOException trying to sync ")).append(s1).append(": ").append(ioexception4).toString());
              }
              // Misplaced declaration of an exception variable
              catch(RuntimeException runtimeexception3)
              {
                  Log.w("PIMaps", "exception while syncing file: ", runtimeexception3);
              }
      notifyDownloadCompleted(492, flag, j, k, flag1, s1, s);
      return;

      illegalargumentexception;
      i = 400;
      httpget.abort();
      flag = false;
      j = 0;
        goto _L7
      runtimeexception1;
_L54:
      mInfo.mHasActiveThread = false;
      if(wakelock != null)
          wakelock.release();
      if(myhttpclient != null)
          myhttpclient.close();
      if(fileoutputstream != null)
          try
          {
              fileoutputstream.close();
          }
          // Misplaced declaration of an exception variable
          catch(IOException ioexception3) { }
      if(s1 != null)
          if(Downloads.isStatusError(491))
          {
              (new File(s1)).delete();
              s1 = null;
          } else
          if(Downloads.isStatusSuccess(491))
              try
              {
                  (new FileOutputStream(s1, true)).getFD().sync();
              }
              // Misplaced declaration of an exception variable
              catch(FileNotFoundException filenotfoundexception1)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" not found: ").append(filenotfoundexception1).toString());
              }
              // Misplaced declaration of an exception variable
              catch(SyncFailedException syncfailedexception1)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" sync failed: ").append(syncfailedexception1).toString());
              }
              // Misplaced declaration of an exception variable
              catch(IOException ioexception2)
              {
                  Log.w("PIMaps", (new StringBuilder("IOException trying to sync ")).append(s1).append(": ").append(ioexception2).toString());
              }
              // Misplaced declaration of an exception variable
              catch(RuntimeException runtimeexception2)
              {
                  Log.w("PIMaps", "exception while syncing file: ", runtimeexception2);
              }
      notifyDownloadCompleted(491, flag, j, k, flag1, s1, s);
      return;
      ioexception6;
      if(ContentManagerUtils.isNetworkAvailable(mContext)) goto _L9; else goto _L8
_L8:
      i = STATUS_RUNNING_PAUSED;
_L10:
      httpget.abort();
      j = 0;
      flag1 = false;
        goto _L7
      exception;
      exception1 = exception;
_L53:
      mInfo.mHasActiveThread = false;
      if(wakelock != null)
          wakelock.release();
      if(myhttpclient != null)
          myhttpclient.close();
      if(fileoutputstream != null)
          try
          {
              fileoutputstream.close();
          }
          // Misplaced declaration of an exception variable
          catch(IOException ioexception1) { }
      if(s1 != null)
          if(Downloads.isStatusError(i))
          {
              (new File(s1)).delete();
              s1 = null;
          } else
          if(Downloads.isStatusSuccess(i))
              try
              {
                  (new FileOutputStream(s1, true)).getFD().sync();
              }
              // Misplaced declaration of an exception variable
              catch(FileNotFoundException filenotfoundexception)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" not found: ").append(filenotfoundexception).toString());
              }
              // Misplaced declaration of an exception variable
              catch(SyncFailedException syncfailedexception)
              {
                  Log.w("PIMaps", (new StringBuilder("file ")).append(s1).append(" sync failed: ").append(syncfailedexception).toString());
              }
              // Misplaced declaration of an exception variable
              catch(IOException ioexception)
              {
                  Log.w("PIMaps", (new StringBuilder("IOException trying to sync ")).append(s1).append(": ").append(ioexception).toString());
              }
              // Misplaced declaration of an exception variable
              catch(RuntimeException runtimeexception)
              {
                  Log.w("PIMaps", "exception while syncing file: ", runtimeexception);
              }
      notifyDownloadCompleted(i, flag, j, k, flag1, s1, s);
      throw exception1;
_L9:
      NumberFormatException numberformatexception;
      IOException ioexception11;
      FileNotFoundException filenotfoundexception5;
      RuntimeException runtimeexception5;
      Exception exception3;
      if(mInfo.mNumFailed < 1)
      {
          i = STATUS_RUNNING_PAUSED;
          flag = true;
      } else
      {
          i = 495;
          flag = false;
      }
        goto _L10
_L4:
      if(j1 != 503) goto _L12; else goto _L11
_L11:
      if(mInfo.mNumFailed >= 1) goto _L12; else goto _L13
_L13:
      i = Downloads.STATUS_RUNNING_PAUSED;
      flag = true;
      header = httpresponse.getFirstHeader("Retry-After");
      j = 0;
      if(header == null) goto _L15; else goto _L14
_L14:
      k1 = Integer.parseInt(header.getValue());
      j = k1;
      if(j >= 0) goto _L17; else goto _L16
_L16:
      j = 0;
_L15:
      httpget.abort();
      flag1 = false;
        goto _L7
_L17:
      if(j < 30)
          j = 30;
      else
      if(j > 43200)
          j = 43200;
      i2 = ContentManagerUtils.sRandom.nextInt(31);
      j = 1000 * (j + i2);
        goto _L15
_L12:
      if(j1 != 301 && j1 != 302 && j1 != 303 && j1 != 307) goto _L19; else goto _L18
_L18:
      if(k < 5)
          break MISSING_BLOCK_LABEL_898;
      Log.d("PIMaps", (new StringBuilder("too many redirects for download ")).append(mInfo.mId).toString());
      i = 497;
      httpget.abort();
      flag = false;
      j = 0;
        goto _L7
      header1 = httpresponse.getFirstHeader("Location");
      if(header1 == null) goto _L19; else goto _L20
_L20:
      s6 = (new URI(mInfo.mRemoteUri)).resolve(new URI(header1.getValue())).toString();
      s = s6;
      k++;
      i = STATUS_RUNNING_PAUSED;
      httpget.abort();
      flag = false;
      j = 0;
      flag1 = false;
      s1 = null;
      fileoutputstream = null;
        goto _L7
      urisyntaxexception;
      Log.d("PIMaps", (new StringBuilder("Couldn't resolve redirect URI for download ")).append(mInfo.mId).toString());
      i = 400;
      httpget.abort();
      flag = false;
      j = 0;
        goto _L7
_L59:
      if(Downloads.isStatusError(j1))
          i = j1;
      else
      if(j1 >= 300 && j1 < 400)
          i = 493;
      else
      if(false && j1 == 200)
          i = 412;
      else
          i = 494;
      httpget.abort();
      flag = false;
      j = 0;
        goto _L7
_L60:
      s1 = null;
      s3 = null;
      fileoutputstream = null;
      if(false) goto _L22; else goto _L21
_L23:
      j2 = Integer.parseInt(s3);
_L61:
      downloadfileinfo = ContentManagerUtils.generateSaveFile(context, s5, contenttype, j2);
      if(downloadfileinfo.mFileName != null) goto _L26; else goto _L25
_L25:
      i = downloadfileinfo.mStatus;
      httpget.abort();
      flag = false;
      j = 0;
      flag1 = false;
      s1 = null;
      s = null;
      fileoutputstream = null;
        goto _L7
_L26:
      s1 = downloadfileinfo.mFileName;
      fileoutputstream = downloadfileinfo.mStream;
      contentvalues = new ContentValues();
      contentvalues.put("file_name", s1);
      k2 = -1;
      flag = false;
      j = 0;
      flag1 = false;
      s = null;
      if(s3 == null)
          break MISSING_BLOCK_LABEL_1563;
      k2 = Integer.parseInt(s3);
      contentvalues.put("total_bytes", Integer.valueOf(k2));
      mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues, null, null);
_L22:
      inputstream = httpresponse.getEntity().getContent();
_L45:
      l2 = inputstream.read(abyte0);
      if(l2 != -1) goto _L28; else goto _L27
_L27:
      contentvalues3 = new ContentValues();
      contentvalues3.put("current_bytes", Integer.valueOf(l));
      if(s3 != null)
          break MISSING_BLOCK_LABEL_1670;
      contentvalues3.put("total_bytes", Integer.valueOf(l));
      mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues3, null, null);
      flag = false;
      j = 0;
      s = null;
      if(s3 == null) goto _L30; else goto _L29
_L29:
      if(l == Integer.parseInt(s3)) goto _L30; else goto _L31
_L31:
      if(s2 != null)
          break MISSING_BLOCK_LABEL_2074;
      Log.d("PIMaps", (new StringBuilder("mismatched content length for ")).append(mInfo.mId).toString());
      i = 411;
      flag = false;
      j = 0;
      s = null;
        goto _L7
      ioexception9;
      if(ContentManagerUtils.isNetworkAvailable(mContext)) goto _L33; else goto _L32
_L32:
      i = STATUS_RUNNING_PAUSED;
_L34:
      httpget.abort();
      j = 0;
      flag1 = false;
      s = null;
        goto _L7
_L33:
      if(mInfo.mNumFailed >= 1)
          break MISSING_BLOCK_LABEL_1817;
      i = STATUS_RUNNING_PAUSED;
      flag = true;
        goto _L34
      Log.d("PIMaps", (new StringBuilder("IOException getting entity for download ")).append(mInfo.mId).append(" : ").append(ioexception9).toString());
      i = 495;
      flag = false;
        goto _L34
      ioexception10;
      contentvalues1 = new ContentValues();
      contentvalues1.put("current_bytes", Integer.valueOf(l));
      mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues1, null, null);
      if(s2 != null) goto _L36; else goto _L35
_L35:
      Log.d("PIMaps", (new StringBuilder("download IOException for download ")).append(mInfo.mId).append(" : ").append(ioexception10).toString());
      Log.d("PIMaps", "can't resume interrupted download with no ETag");
      i = 412;
_L37:
      httpget.abort();
      j = 0;
      s = null;
        goto _L7
_L36:
      if(ContentManagerUtils.isNetworkAvailable(mContext))
          break MISSING_BLOCK_LABEL_2005;
      i = STATUS_RUNNING_PAUSED;
      flag = false;
        goto _L37
      if(mInfo.mNumFailed >= 1)
          break MISSING_BLOCK_LABEL_2025;
      i = STATUS_RUNNING_PAUSED;
      flag = true;
        goto _L37
      Log.d("PIMaps", (new StringBuilder("download IOException for download ")).append(mInfo.mId).append(" : ").append(ioexception10).toString());
      i = 495;
      flag = false;
        goto _L37
      if(ContentManagerUtils.isNetworkAvailable(mContext))
          break MISSING_BLOCK_LABEL_2098;
      i = STATUS_RUNNING_PAUSED;
      flag = false;
      j = 0;
      s = null;
        goto _L7
      if(mInfo.mNumFailed >= 1)
          break MISSING_BLOCK_LABEL_2123;
      i = STATUS_RUNNING_PAUSED;
      flag = true;
      j = 0;
      s = null;
        goto _L7
      Log.d("PIMaps", (new StringBuilder("closed socket for download ")).append(mInfo.mId).toString());
      i = 495;
      flag = false;
      j = 0;
      s = null;
        goto _L7
_L28:
      flag1 = true;
      fileoutputstream1 = fileoutputstream;
_L57:
      if(fileoutputstream1 != null) goto _L39; else goto _L38
_L38:
      fileoutputstream = new FileOutputStream(s1, true);
_L58:
      fileoutputstream.write(abyte0, 0, l2);
      fileoutputstream.close();
      fileoutputstream = null;
_L52:
      l += l2;
      l3 = System.currentTimeMillis();
      if(l - i1 <= 500)
          break MISSING_BLOCK_LABEL_2309;
      i3 = l3 - l1 != 500L;
      flag = false;
      j = 0;
      s = null;
      if(i3 <= 0)
          break MISSING_BLOCK_LABEL_2309;
      contentvalues2 = new ContentValues();
      contentvalues2.put("current_bytes", Integer.valueOf(l));
      mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues2, null, null);
      i1 = l;
      l1 = l3;
      downloadinfo = mInfo;
      downloadinfo;
      JVM INSTR monitorenter ;
      if(mInfo.mControl != 1) goto _L41; else goto _L40
_L40:
      i = STATUS_RUNNING_PAUSED;
      httpget.abort();
      downloadinfo;
      JVM INSTR monitorexit ;
      flag = false;
      j = 0;
      s = null;
        goto _L7
      exception2;
      downloadinfo;
      JVM INSTR monitorexit ;
      throw exception2;
_L56:
      flag2 = ContentManagerUtils.discardPurgeableFiles(mContext, 4096L);
      if(flag2) goto _L43; else goto _L42
_L42:
      i = 492;
      flag = false;
      j = 0;
      s = null;
        goto _L7
_L41:
      downloadinfo;
      JVM INSTR monitorexit ;
      if(mInfo.mStatus != 490) goto _L45; else goto _L44
_L44:
      i = 490;
      flag = false;
      j = 0;
      s = null;
        goto _L7
_L30:
      contentvalues4 = new ContentValues();
      if(s2 == null) goto _L47; else goto _L46
_L46:
      if(mInfo.mType != PIContentManager.ContentType.REFERENCE) goto _L47; else goto _L48
_L48:
      contentvalues4.put("download_identifier", s2);
_L50:
      mPIContentStore.update(ContentUris.withAppendedId(Downloads.CONTENT_URI, mInfo.mId), contentvalues4, null, null);
      i = 200;
      flag = false;
      j = 0;
      s = null;
        goto _L7
_L47:
      if(mInfo.mNewMD5 == null || mInfo.mType == PIContentManager.ContentType.REFERENCE) goto _L50; else goto _L49
_L49:
      contentvalues4.put("download_identifier", mInfo.mNewMD5);
        goto _L50
      ioexception14;
        goto _L51
      ioexception12;
        goto _L52
      exception3;
      exception1 = exception3;
      fileoutputstream = fileoutputstream1;
      flag = false;
      j = 0;
      s = null;
        goto _L53
      runtimeexception5;
      fileoutputstream = fileoutputstream1;
      flag = false;
      j = 0;
      s = null;
        goto _L54
      filenotfoundexception5;
      fileoutputstream = fileoutputstream1;
      flag = false;
      j = 0;
      s = null;
        goto _L55
      ioexception11;
        goto _L56
      numberformatexception;
        goto _L15
_L43:
      fileoutputstream1 = fileoutputstream;
        goto _L57
_L39:
      fileoutputstream = fileoutputstream1;
        goto _L58
_L19:
      if((false || j1 == 200) && (true || j1 == 206)) goto _L60; else goto _L59
_L24:
      j2 = 0;
        goto _L61
      IOException ioexception13;
      ioexception13;
      fileoutputstream = fileoutputstream1;
        goto _L56
        */
  }

}



/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar

 * Qualified Name:     com.pointinside.android.api.content.DownloadThread

 * JD-Core Version:    0.7.0.1

 */
