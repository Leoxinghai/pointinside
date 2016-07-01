package com.pointinside.android.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract;
import com.pointinside.android.piwebservices.provider.PITouchstreamContract.TouchstreamType;
import com.pointinside.android.piwebservices.util.BuildUtils;

public class AboutActivity
  extends Activity
{
  private TextView mAppVersion;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2130903040);
    this.mAppVersion = ((TextView)findViewById(2131623936));
    TextView localTextView = this.mAppVersion;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = BuildUtils.getAppVersionLabel(this);
    localTextView.setText(getString(2131099678, arrayOfObject));
    PITouchstreamContract.addEvent(this, PointInside.getInstance().getUserLocation(), "", PointInside.getInstance().getCurrentVenueId(), PITouchstreamContract.TouchstreamType.SHOW_ABOUT);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.ui.AboutActivity
 * JD-Core Version:    0.7.0.1
 */