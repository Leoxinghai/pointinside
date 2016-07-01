// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import java.lang.ref.WeakReference;
import com.pointinside.android.app.net.HolidayGameClient;

public class HolidayGameCache
{

    public HolidayGameCache(Context context)
    {
        mContext = new WeakReference(context.getApplicationContext());
        recoverState();
    }

    private Context getContext()
    {
        return (Context)mContext.get();
    }

    public static HolidayGameCache getInstance(Context context)
    {
        if(Looper.getMainLooper() != Looper.myLooper())
            throw new IllegalStateException("HolidayGameCache is not thread-safe, call only from the main thread");
        if(sInstance == null)
            sInstance = new HolidayGameCache(context);
        return sInstance;
    }

    private SharedPreferences getPrefs()
    {
        return getContext().getSharedPreferences(TAG, 0);
    }

    private void recoverState()
    {
        mState = HolidayGameClient.GameState.valueOf(getPrefs().getInt("game-state", -1));
    }

    private void saveState()
    {
        android.content.SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt("game-state", mState.ordinal());
        editor.commit();
    }

    public boolean hasGameState()
    {
        return mState != null;
    }

    public boolean isGameOn()
    {
        return mState == com.pointinside.android.app.net.HolidayGameClient.GameState.ON;
    }

    public boolean shouldShowGame()
    {
        return mState != com.pointinside.android.app.net.HolidayGameClient.GameState.OFF;
    }

    public void updateGlobalState(com.pointinside.android.app.net.HolidayGameClient.InfoResponse inforesponse)
    {
        com.pointinside.android.app.net.HolidayGameClient.GameState gamestate;
        if(inforesponse != null)
            gamestate = inforesponse.gameState;
        else
            gamestate = null;
        if(gamestate != null && mState != gamestate)
        {
            mState = gamestate;
            saveState();
        }
    }

    private static final String KEY_GAME_STATE = "game-state";
    private static final String TAG = HolidayGameCache.class.getSimpleName();
    private static HolidayGameCache sInstance;
    private WeakReference mContext;
    private com.pointinside.android.app.net.HolidayGameClient.GameState mState;

}
