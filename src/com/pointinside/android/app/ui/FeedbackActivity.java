// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.ui;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.app.net.PIPostFeedbackClient;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import java.io.IOException;

// Referenced classes of package com.pointinside.android.app.ui:
//            AboutActivity

public class FeedbackActivity extends Activity
{
    private class PostFeedbackTask extends AsyncTask
    {

        protected  Boolean doInBackground(Void avoid[])
        {
            if(mFeedbackTxt.getText().length() > 0)
            {
                mFeedbackData.append(mFeedbackTxt.getText().toString());
                mFeedbackData.append("\n");
                mFeedbackData.append((new StringBuilder("venue_id=")).append(mVenueId).toString());
                mFeedbackData.append("\n");
                mFeedbackData.append("source=Android");
                if(mEmailTxt.getText().length() > 0)
                {
                    mFeedbackData.append("\n");
                    mFeedbackData.append((new StringBuilder("email=")).append(mEmailTxt.getText().toString()).toString());
                }
                PIPostFeedbackClient pipostfeedbackclient = new PIPostFeedbackClient(FeedbackActivity.this, Uri.parse("https://smartmaps.pointinside.com/iphone/v2_0/feedback"), mFeedbackData.toString());
                try
                {
                    pipostfeedbackclient.run();
                }
                catch(IOException ioexception)
                {
                    return Boolean.valueOf(false);
                }
                return Boolean.valueOf(true);
            } else
            {
                return Boolean.valueOf(false);
            }
        }

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected void onPostExecute(Boolean boolean1)
        {
            removeDialog(1);
            if(boolean1.booleanValue())
            {
                finish();
                return;
            } else
            {
                mSendButton.setEnabled(true);
                showDialog(2);
                return;
            }
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((Boolean)obj);
        }

        protected void onPreExecute()
        {
            mSendButton.setEnabled(false);
            showDialog(1);
        }

        private StringBuffer mFeedbackData;
        final FeedbackActivity this$0;

        private PostFeedbackTask()
        {
            super();
            this$0 = FeedbackActivity.this;
            mFeedbackData = new StringBuffer();
        }

        PostFeedbackTask(PostFeedbackTask postfeedbacktask)
        {
            this();
        }
    }


    public FeedbackActivity()
    {
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030012);
        mCancelBtn = findViewById(0x7f0e0013);
        mSendButton = findViewById(0x7f0e0014);
        mEmailTxt = (EditText)findViewById(0x7f0e0015);
        mFeedbackTxt = (EditText)findViewById(0x7f0e0016);
        mVenueId = getIntent().getIntExtra("venue_id", -1);
        mCancelBtn.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

//            final FeedbackActivity this$0;


        }
);
        mSendButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mPostFeedbackTask != null && mPostFeedbackTask.getStatus() == android.os.AsyncTask.Status.RUNNING)
                {
                    Log.w("PointInside", "Already sending feedback.");
                    return;
                } else
                {
                    FeedbackActivity feedbackactivity = FeedbackActivity.this;
                    PostFeedbackTask postfeedbacktask = new PostFeedbackTask(null);
                    feedbackactivity.mPostFeedbackTask = postfeedbacktask;
                    postfeedbacktask.execute(new Void[0]);
                    return;
                }
            }

          }
);
        PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", PointInside.getInstance().getCurrentVenueId(), com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType.SHOW_FEEDBACK);
    }

    protected Dialog onCreateDialog(int i)
    {
        switch(i)
        {
        default:
            return null;

        case 1: // '\001'
            ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setMessage(getString(0x7f060028));
            progressdialog.setIndeterminate(true);
            progressdialog.setCancelable(false);
            return progressdialog;

        case 2: // '\002'
            return (new android.app.AlertDialog.Builder(this)).setTitle(0x7f060029).setMessage(0x7f06002b).setPositiveButton(0x7f06002a, new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int j)
                {
                    removeDialog(2);
                }

              }
).create();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(0x7f0d0002, menu);
        return true;
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        switch(menuitem.getItemId())
        {
        default:
            return false;

        case 2131624050:
            startActivity(new Intent(this, AboutActivity.class));
            break;
        }
        return true;
    }

    private static final int DIALOG_KEY_ERROR = 2;
    private static final int DIALOG_KEY_SENDING = 1;
    public static final String PI_FEEDBACK_URI = "https://smartmaps.pointinside.com/iphone/v2_0/feedback";
    private View mCancelBtn;
    private EditText mEmailTxt;
    private EditText mFeedbackTxt;
    private AsyncTask mPostFeedbackTask;
    private View mSendButton;
    private int mVenueId;






}
