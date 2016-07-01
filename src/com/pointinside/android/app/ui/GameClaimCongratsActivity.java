package com.pointinside.android.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.app.net.HolidayGameClient;
import com.pointinside.android.app.net.HolidayGameClient.ShareResponse;

public class GameClaimCongratsActivity
  extends Activity
{
  private static final String TAG = GameClaimCongratsActivity.class.getSimpleName();
  private final View.OnClickListener mOkClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      GameClaimCongratsActivity.this.finish();
    }
  };
  private final View.OnClickListener mShareClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      String str = GameClaimCongratsActivity.this.getIntent().getStringExtra("this-space-uuid");
      GameClaimCongratsActivity.sharePlace(GameClaimCongratsActivity.this, str);
    }
  };

  public static void sharePlace(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.SUBJECT", paramContext.getString(2131099817));
    localIntent.putExtra("android.intent.extra.TEXT", paramContext.getString(2131099818));
    paramContext.startActivity(Intent.createChooser(localIntent, paramContext.getString(2131099816)));
    new SubmitShareThread(paramContext.getApplicationContext(), paramString).start();
  }

  public static void show(Context paramContext, Bundle paramBundle)
  {
    Intent localIntent = new Intent(paramContext, GameClaimCongratsActivity.class);
    localIntent.putExtras(paramBundle);
    paramContext.startActivity(localIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903062);
    LayoutInflater.from(this).inflate(2130903061, (ViewGroup)findViewById(2131623967));
    String str = getString(2131099815, new Object[] { getIntent().getStringExtra("place-name"), getIntent().getStringExtra("venue-name") });
    ((TextView)findViewById(2131623963)).setText(str);
    ((TextView)findViewById(2131623964)).setMovementMethod(LinkMovementMethod.getInstance());
    findViewById(2131623966).setOnClickListener(this.mOkClicked);
    findViewById(2131623965).setOnClickListener(this.mShareClicked);
  }

  private static class SubmitShareThread
    extends Thread
  {
    private final Context mContext;
    private final String mSpaceUUID;

    public SubmitShareThread(Context paramContext, String paramString)
    {
      this.mContext = paramContext.getApplicationContext();
      this.mSpaceUUID = paramString;
    }

    public void run()
    {
      try
      {
        HolidayGameClient.ShareResponse localShareResponse = HolidayGameClient.getInstance(this.mContext).notifyShare(this.mSpaceUUID);
        if ((localShareResponse == null) || (localShareResponse.status != 1)) {
          Log.w(GameClaimCongratsActivity.TAG, "Server refused share request...");
        }
        return;
      }
      catch (JSONWebRequester.RestResponseException localRestResponseException)
      {
        Log.w(GameClaimCongratsActivity.TAG, "Error submitting game share for spaceUUID=" + this.mSpaceUUID, localRestResponseException);
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.GameClaimCongratsActivity
 * JD-Core Version:    0.7.0.1
 */