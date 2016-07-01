package com.pointinside.android.app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.app.net.HolidayGameClient;
import com.pointinside.android.app.net.HolidayGameClient.ClaimRequest;
import com.pointinside.android.app.net.HolidayGameClient.ClaimResponse;
import com.pointinside.android.app.util.ActionBarHelper;
import com.pointinside.android.app.util.DetachableAsyncTask;
import com.pointinside.android.app.util.DetachableAsyncTask.TaskCallbacks;
import java.util.Calendar;

public class GamePlaceClaimActivity
  extends Activity
{
  static final String EXTRA_PLACE_NAME = "place-name";
  static final String EXTRA_SPACE_UUID = "this-space-uuid";
  static final String EXTRA_VENUE_NAME = "venue-name";
  private static final String PREF_AGE_CHECK = "age-check";
  private static final String PREF_EMAIL = "email";
  private static final String PREF_NAME = "name";
  private static final String PREF_SUCCESSFUL_CLAIM = "successful-claim";
  private static final String PRIVACY_URL = "http://www.pointinside.com/m/privacy-policy";
  private static final String TAG = GamePlaceClaimActivity.class.getSimpleName();
  private static final String TERMS_URL = "http://www.pointinside.com/m/mallopoly-official-rules";
  private final ActionBarHelper mActionBarHelper = ActionBarHelper.createInstance(this);
  private CheckBox mAgeCheck;
  private EditText mEmail;
  private EditText mName;
  private final View.OnClickListener mSubmitClicked = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (GamePlaceClaimActivity.this.verifyAndReportError()) {
        GamePlaceClaimActivity.this.submitClaim();
      }
    }
  };
  private ProgressDialog mSubmitting;

  private static Calendar createBirthdateAtLeast18()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.add(1, -19);
    return localCalendar;
  }


  private boolean isValidEmail(String s)
  {
      String as[];
      String as1[];
      if(!TextUtils.isEmpty(s))
          if((as = s.split("@", 2)).length >= 2 && !TextUtils.isEmpty(as[0]) && !TextUtils.isEmpty(as[1]) && ((as1 = as[1].split("\\.")).length >= 2 && !TextUtils.isEmpty(as1[0]) && !TextUtils.isEmpty(as1[1])))
              return true;
      return false;
  }

  private boolean restoreEntry()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences(TAG, 0);
    boolean bool = localSharedPreferences.getBoolean("successful-claim", false);
    if (bool)
    {
      this.mName.setText(localSharedPreferences.getString("name", null));
      this.mEmail.setText(localSharedPreferences.getString("email", null));
      this.mAgeCheck.setChecked(localSharedPreferences.getBoolean("age-check", false));
    }
    return bool;
  }

  private void setupLink(TextView paramTextView, int paramInt, String paramString)
  {
    paramTextView.setMovementMethod(LinkMovementMethod.getInstance());
    paramTextView.setText(PlaceDetailActivity.getWebLink(getString(paramInt), paramString));
  }

  public static void show(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    Intent localIntent = new Intent(paramContext, GamePlaceClaimActivity.class);
    localIntent.putExtra("this-space-uuid", paramString1);
    localIntent.putExtra("place-name", paramString2);
    localIntent.putExtra("venue-name", paramString3);
    paramContext.startActivity(localIntent);
  }

  private void showSubmitError(String paramString)
  {
    new AlertDialog.Builder(this).setPositiveButton(17039370, null).setTitle(2131099819).setMessage(paramString).show();
  }

  private void storeEntry()
  {
    SharedPreferences.Editor localEditor = getSharedPreferences(TAG, 0).edit();
    localEditor.putBoolean("successful-claim", true);
    localEditor.putString("name", this.mName.getText().toString());
    localEditor.putString("email", this.mEmail.getText().toString());
    localEditor.putBoolean("age-check", this.mAgeCheck.isChecked());
    localEditor.commit();
  }

  private void submitClaim()
  {
      final com.pointinside.android.app.net.HolidayGameClient.ClaimRequest request = new com.pointinside.android.app.net.HolidayGameClient.ClaimRequest();
      request.uuid = getIntent().getStringExtra("this-space-uuid");
      request.name = mName.getText().toString();
      request.email = mEmail.getText().toString();
      request.phone = null;
      final SubmitClaimTask submitTask;
      if(mAgeCheck.isChecked())
          request.birthdate = createBirthdateAtLeast18();
      else
          request.birthdate = Calendar.getInstance();
      submitTask = new SubmitClaimTask(this, request);
      submitTask.setCallback(new DetachableAsyncTask.TaskCallbacks<HolidayGameClient.ClaimResponse,Void>() {

          protected void onPostExecute(HolidayGameClient.ClaimResponse claimresponse)
          {
              if(mSubmitting != null)
              {
                  mSubmitting.dismiss();
                  mSubmitting = null;
              }
              String s;
              if(claimresponse == null)
              {
                  s = getString(0x7f0600a5);
              } else
              {
                  int i = claimresponse.status;
                  s = null;
                  if(i != 1)
                      s = claimresponse.status_txt;
              }
              if(s != null)
              {
                  showSubmitError(s);
                  return;
              } else
              {
                  Log.i(GamePlaceClaimActivity.TAG, (new StringBuilder("User has successfully claimed space=")).append(request.uuid).toString());
                  storeEntry();
                  finish();
                  GameClaimCongratsActivity.show(GamePlaceClaimActivity.this, getIntent().getExtras());
                  return;
              }
          }

//          protected void onPostExecute(Object obj)
//          {
//              onPostExecute((com.pointinside.android.app.net.HolidayGameClient.ClaimResponse)obj);
//          }

      }
);
      mSubmitting = ProgressDialog.show(this, null, getString(0x7f0600a4), true, true, new android.content.DialogInterface.OnCancelListener() {

          public void onCancel(DialogInterface dialoginterface)
          {
              submitTask.clearCallback();
          }

      }
);
      submitTask.execute(new Void[0]);
  }

  private boolean verifyAndReportError()
  {
    if (!isValidEmail(this.mEmail.getText().toString()))
    {
      showSubmitError(getString(2131099820));
      return false;
    }
    return true;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903062);
    LayoutInflater.from(this).inflate(2130903063, (ViewGroup)findViewById(2131623967));
    this.mActionBarHelper.setActionBarTitle(getTitle());
    this.mName = ((EditText)findViewById(2131623968));
    this.mEmail = ((EditText)findViewById(2131623969));
    this.mAgeCheck = ((CheckBox)findViewById(2131623970));
    restoreEntry();
    setupLink((TextView)findViewById(2131623973), 2131099810, "http://www.pointinside.com/m/mallopoly-official-rules");
    setupLink((TextView)findViewById(2131623972), 2131099811, "http://www.pointinside.com/m/privacy-policy");
    findViewById(2131623971).setOnClickListener(this.mSubmitClicked);
  }

  private static class SubmitClaimTask
    extends DetachableAsyncTask<Void, Void, HolidayGameClient.ClaimResponse>
  {
    private final Context mContext;
    private final HolidayGameClient.ClaimRequest mRequest;

    public SubmitClaimTask(Context paramContext, HolidayGameClient.ClaimRequest paramClaimRequest)
    {
      this.mContext = paramContext;
      this.mRequest = paramClaimRequest;
    }

    protected HolidayGameClient.ClaimResponse doInBackground(Void... paramVarArgs)
    {
      try
      {
        HolidayGameClient.ClaimResponse localClaimResponse = HolidayGameClient.getInstance(this.mContext).claimSpace(this.mRequest);
        return localClaimResponse;
      }
      catch (JSONWebRequester.RestResponseException localRestResponseException)
      {
        Log.e(GamePlaceClaimActivity.TAG, "Response error claiming space", localRestResponseException);
      }
      return null;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.GamePlaceClaimActivity
 * JD-Core Version:    0.7.0.1
 */