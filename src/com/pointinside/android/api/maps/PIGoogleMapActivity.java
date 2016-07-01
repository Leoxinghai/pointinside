// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.api.maps;

import android.os.Bundle;
//import com.google.android.gms.maps.MapActivity;
import android.app.Activity;

// Referenced classes of package com.pointinside.android.api.maps:
//            PIMapActivity, PIMapView

public abstract class PIGoogleMapActivity extends Activity
{
    private static class PIMapActivityDelagate extends PIMapActivity
    {

        protected void onDestroy()
        {
            if(mPIMapView != null)
                mPIMapView.cleanUp();
            sPIMapActivity = null;
        }

        private PIMapActivityDelagate()
        {
        }

        PIMapActivityDelagate(PIMapActivityDelagate pimapactivitydelagate)
        {
            this();
        }
    }


    public PIGoogleMapActivity()
    {
        mDelegate = new PIMapActivityDelagate(null);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public final void onDestroy()
    {
        super.onDestroy();
        mDelegate.onDestroy();
    }

    private PIMapActivityDelagate mDelegate;
}
