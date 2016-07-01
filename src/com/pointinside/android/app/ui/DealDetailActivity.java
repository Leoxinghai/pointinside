package com.pointinside.android.app.ui;

import com.pointinside.android.app.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Toast;

import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DateUtils;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class DealDetailActivity
  extends Activity
{
  private static final String INTERACT_DEAL_TEMPLATE_ASSET = "interact_deal.template.html";
  private static final String TAG = DealDetailActivity.class.getSimpleName();
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private ProgressDialog mLoading;
  private DialogInterface.OnCancelListener mLoadingCanceled = new DialogInterface.OnCancelListener()
  {
    public void onCancel(DialogInterface paramAnonymousDialogInterface)
    {
      DealDetailActivity.this.finish();
    }
  };
  private QueryHandler mQueryHandler;
  private WebView mWebView;

  private void dumpDealData(Cursor paramCursor)
  {
    int i = paramCursor.getColumnCount();
    Log.d(TAG, "Displaying deal:");
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return;
      }
      Log.d(TAG, "   " + paramCursor.getColumnName(j) + ": " + paramCursor.getString(j));
    }
  }

  public static String getDisplayEndDate(Context paramContext, Cursor paramCursor)
  {
    String str1 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("display_end_date"));
    if (!TextUtils.isEmpty(str1)) {
      return str1;
    }
    String str2 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("end_date"));
    if (TextUtils.isEmpty(str2)) {
      return null;
    }
    try
    {
      String str3 = DateUtils.formatRelativeDate(paramContext, DateUtils.DEAL_PARSER.parse(str2).getTime(), System.currentTimeMillis());
      return str3;
    }
    catch (ParseException localParseException)
    {
      Log.d(TAG, "Can't parse endDate: " + str2);
    }
    return str2;
  }

  private void handleDealQuery(Cursor cursor)
  {
      if(!cursor.moveToFirst())
      {
          Toast.makeText(this, 0x7f06005d, 0).show();
          finish();
      }
      mActionBarHelper.setActionBarTitle(cursor.getString(cursor.getColumnIndexOrThrow("organization")));
      String s = cursor.getString(cursor.getColumnIndexOrThrow("datasource"));
      String s1;
      if(s.equals("geoqpon"))
          handleGeoqponDisplay(cursor);
      else
          handleInteractDisplay(cursor);
      s1 = cursor.getString(cursor.getColumnIndexOrThrow("datasource_id"));
      PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), (new StringBuilder(String.valueOf(s))).append(":").append(s1).toString(), PointInside.getInstance().getCurrentVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SHOW_DEAL);
  }

  private void handleGeoqponDisplay(Cursor paramCursor)
  {
    this.mWebView.getSettings().setBuiltInZoomControls(true);
    this.mWebView.getSettings().setLoadWithOverviewMode(true);
    this.mWebView.getSettings().setUseWideViewPort(true);
    String str = paramCursor.getString(paramCursor.getColumnIndexOrThrow("display_image"));
    this.mWebView.loadUrl(str);
  }

  private void handleInteractDisplay(Cursor paramCursor)
  {
    this.mWebView.getSettings().setBuiltInZoomControls(false);
    this.mWebView.getSettings().setSupportZoom(false);
    try
    {
      String str1 = slurp(getAssets().open("interact_deal.template.html"));
      String str2 = str1.replace("%title%", paramCursor.getString(paramCursor.getColumnIndexOrThrow("title"))).replace("%display_image%", paramCursor.getString(paramCursor.getColumnIndexOrThrow("display_image"))).replace("%date_label%", getDisplayEndDate(this, paramCursor)).replace("%description%", paramCursor.getString(paramCursor.getColumnIndexOrThrow("description")));
      Log.d(TAG, "template=" + str2);
      this.mWebView.loadDataWithBaseURL(null, str2, "text/html", "utf-8", null);
      return;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException);
    }
  }

  public static void show(Context paramContext, Uri paramUri)
  {
    paramContext.startActivity(new Intent("android.intent.action.VIEW", paramUri, paramContext, DealDetailActivity.class));
  }

  private static String slurp(InputStream inputstream)
	        throws IOException
	    {
	        ByteArrayOutputStream bytearrayoutputstream;
	        byte abyte0[];
	        bytearrayoutputstream = new ByteArrayOutputStream();
	        abyte0 = new byte[4096];
	        int i =0;
	        try {
		        while(i!=-1) {
			        i = inputstream.read(abyte0);
			        if(i != -1) {
			        	bytearrayoutputstream.write(abyte0, 0, i);
			        }
		        }
		        String s = bytearrayoutputstream.toString();
		        inputstream.close();
		        return s;
	        } catch(IOException exception) {
		        inputstream.close();
		        throw exception;
	        }
	    }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903052);
    this.mWebView = ((WebView)findViewById(2131623947));
    this.mWebView.setWebChromeClient(new ProgressChromeClient());
    this.mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
    this.mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    Uri localUri = getIntent().getData();
    if (localUri == null)
    {
      Log.d(TAG, "No deal result for display");
      finish();
    }
    this.mQueryHandler = new QueryHandler();
    this.mQueryHandler.startQuery(1, null, localUri, null, null, null, null);
    this.mLoading = ProgressDialog.show(this, null, getString(2131099742), false, true, this.mLoadingCanceled);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    return this.mActionBarHelper.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mQueryHandler.cancelOperation(1);
  }

  public boolean onOptionsItemSelected(MenuItem menuitem)
  {
      switch(menuitem.getItemId())
      {
      default:
          return false;

      case R.id.search:
          onSearchRequested();
          return true;

      case R.id.feedback:
          startActivity(new Intent(this, FeedbackActivity.class));
          return true;

      case R.id.about:
          startActivity(new Intent(this, AboutActivity.class));
          return true;
      }
  }

  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    this.mActionBarHelper.onPostCreate(paramBundle);
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    return this.mActionBarHelper.onPrepareOptionsMenu(paramMenu);
  }

  private class ProgressChromeClient
    extends WebChromeClient
  {
    private ProgressChromeClient() {}

    public void onProgressChanged(WebView paramWebView, int paramInt)
    {
      DealDetailActivity.this.mLoading.setProgress(paramInt);
      if (paramInt >= 100) {
        DealDetailActivity.this.mLoading.dismiss();
      }
    }
  }

  private class QueryHandler extends AsyncQueryHandler
  {

      protected void onQueryComplete(int i, Object obj, Cursor cursor)
      {
          switch(i)
          {
          default:
              throw new IllegalArgumentException((new StringBuilder("Unknown token=")).append(i).toString());

          case 1: // '\001'
              handleDealQuery(cursor);
              break;
          }
      }

      public static final int TOKEN_DEAL = 1;
      final DealDetailActivity this$0;

      public QueryHandler()
      {
          super(getContentResolver());
          this$0 = DealDetailActivity.this;
      }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.DealDetailActivity
 * JD-Core Version:    0.7.0.1
 */