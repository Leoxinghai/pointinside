// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.piwebservices.provider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Process;
import android.provider.BaseColumns;
import com.pointinside.android.piwebservices.service.AnalyticsService;

// Referenced classes of package com.pointinside.android.piwebservices.provider:
//            DynamicAuthorityHelper, PITouchstreamProvider

public class PITouchstreamContract
{
    public static class Touchstream
        implements TouchstreamColumns
    {

        public static int clearTouchStreamData(Context context)
        {
            return context.getContentResolver().delete(CONTENT_URI, null, null);
        }

        public static int deleteTouchStreamData(Context context, long l)
        {
            ContentResolver contentresolver = context.getContentResolver();
            Uri uri = CONTENT_URI;
            String as[] = new String[1];
            as[0] = String.valueOf(l);
            return contentresolver.delete(uri, "session_id = ?", as);
        }

        public static Cursor getTouchStreamData(Context context)
        {
            return context.getContentResolver().query(CONTENT_URI, null, null, null, "session_id ,_id");
        }

        public static Cursor getTouchStreamData(Context context, long l)
        {
            ContentResolver contentresolver = context.getContentResolver();
            Uri uri = CONTENT_URI;
            String as[] = new String[1];
            as[0] = String.valueOf(l);
            return contentresolver.query(uri, null, "session_id = ?", as, "session_id ,_id");
        }

        public static Uri getTouchstreamUri(long l)
        {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(l)).build();
        }

        public static final Uri CONTENT_URI = PITouchstreamContract.getBaseUri().buildUpon().appendPath("touchstream").build();


        public Touchstream()
        {
        }
    }

    public static interface TouchstreamColumns
        extends BaseColumns
    {

        public static final String ALTITUDE = "altitude";
        public static final String CONTENT_ITEM_TYPE = "vnd.tmobile.cursor.item/touchstream";
        public static final String CONTENT_TYPE = "vnd.tmobile.cursor.dir/touchstream";
        public static final String DATA = "_data";
        public static final String DATA_TYPE = "data_type";
        public static final String LATITUDE = "latitude";
        public static final String LOCATION_TIME = "loc_time";
        public static final String LOC_ACCURACY = "loc_accuracy";
        public static final String LOC_PROVIDER = "loc_provider";
        public static final String LONGITUDE = "longitude";
        public static final String SESSION_ID = "session_id";
        public static final String VENUE_ID = "venue_id";
        public static final String VISIT_TIME = "visit_time";
    }

    public static enum TouchstreamType
    {

            SESSION_START("SESSION_START", 0),
            VISIT_VENUE_ID("VISIT_VENUE_ID", 1),
            SHOW_PLACE_DETAIL("SHOW_PLACE_DETAIL", 2),
            SHOW_PROMO_DETAIL("SHOW_PROMO_DETAIL", 3),
            SHOW_EVENT_DETAIL("SHOW_EVENT_DETAIL", 4),
            SHOW_FEATURED_DETAIL("SHOW_FEATURED_DETAIL", 5),
            SHOW_PLACES("SHOW_PLACES", 6),
            SHOW_EVENTS("SHOW_EVENTS", 7),
            SHOW_PROMOS("SHOW_PROMOS", 8),
            SHOW_SERVICES("SHOW_SERVICES", 9),
            SHOW_DINING("SHOW_DINING", 10),
            SEARCH_FOR_VENUE("SEARCH_FOR_VENUE", 11),
            SEARCH_FOR_PLACE("SEARCH_FOR_PLACE", 12),
            REMOTE_SEARCH_BY_CATEGORY("REMOTE_SEARCH_BY_CATEGORY", 13),
            REMOTE_SEARCH_BY_KEYWORD("REMOTE_SEARCH_BY_KEYWORD", 14),
            SHOW_CATEGORY_BROWSE("SHOW_CATEGORY_BROWSE", 15),
            SHOW_SEARCH("SHOW_SEARCH", 16),
            TOGGLE_DEALS_ON("TOGGLE_DEALS_ON", 17),
            TOGGLE_DEALS_OFF("TOGGLE_DEALS_OFF", 18),
            SHOW_DEAL("SHOW_DEAL", 19),
            SHOW_DEALS_LIST("SHOW_DEALS_LIST", 20),
            SHOW_FEEDBACK("SHOW_FEEDBACK", 21),
            SHOW_ABOUT("SHOW_ABOUT", 22),
            MAP_TAP_PLACE("MAP_TAP_PLACE", 23);

            private String sType;
            private int iType;
            public String toString() {
            	return sType;
            }

        private TouchstreamType(String s, int i)
        {
        	sType = s;
        	iType = i;
        }
        public TouchstreamType getType(int type){
        	switch(type){
					case 0:
					 return SESSION_START;
					case 1:
					 return VISIT_VENUE_ID;
					case 2:
					 return SHOW_PLACE_DETAIL;
					case 3:
					 return SHOW_PROMO_DETAIL;
					case 4:
					 return SHOW_EVENT_DETAIL;
					case 5:
					 return SHOW_FEATURED_DETAIL;
					case 6:
					 return SHOW_PLACES;
					case 7:
					 return SHOW_EVENTS;
					case 8:
					 return SHOW_PROMOS;
					case 9:
					 return SHOW_SERVICES;
					case 10:
					 return SHOW_DINING;
					case 11:
					 return SEARCH_FOR_VENUE;
					case 12:
					 return SEARCH_FOR_PLACE;
					case 13:
					 return REMOTE_SEARCH_BY_CATEGORY;
					case 14:
					 return REMOTE_SEARCH_BY_KEYWORD;
					case 15:
					 return SHOW_CATEGORY_BROWSE;
					case 16:
					 return SHOW_SEARCH;
					case 17:
					 return TOGGLE_DEALS_ON;
					case 18:
					 return TOGGLE_DEALS_OFF;
					case 19:
					 return SHOW_DEAL;
					case 20:
					 return SHOW_DEALS_LIST;
					case 21:
					 return SHOW_FEEDBACK;
					case 22:
					 return SHOW_ABOUT;
					case 23:
					 return MAP_TAP_PLACE;
					default:
						 return null;
        	}
        }
    }


    public PITouchstreamContract()
    {
    }

    public static void addEvent(Context context, Location location, String s, long l, TouchstreamType touchstreamtype)
    {
        addEventToSession(context, location, getCurrentSessionId(), s, l, touchstreamtype);
    }

    public static void addEventAtTime(Context context, Location location, String s, long l, long l1, TouchstreamType touchstreamtype)
    {
        addEventToSessionAtTime(context, location, getCurrentSessionId(), s, l, l1, touchstreamtype);
    }

    public static void addEventToSession(Context context, Location location, long l, String s, long l1, TouchstreamType touchstreamtype)
    {
        addEventToSessionAtTime(context, location, l, s, l1, System.currentTimeMillis(), touchstreamtype);
    }

    public static void addEventToSessionAtTime(final Context context, Location location, long l, final String data, final long venueId, final long visitTime, final TouchstreamType type)
    {
        final long submittingSessionId;
        final Location locationCopy;
        if(l == 0L)
        {
            submittingSessionId = getSessionId();
            addEventToSessionAtTime(context, location, submittingSessionId, "", 0L, visitTime, TouchstreamType.SESSION_START);
        } else
        {
            submittingSessionId = l;
        }
        if(location != null)
            locationCopy = new Location(location);
        else
            locationCopy = null;
        (new Thread() {

            public void run()
            {
                Process.setThreadPriority(10);
                ContentValues contentvalues = PITouchstreamContract.makeTouchstreamValues(submittingSessionId, data, locationCopy, venueId, visitTime, type);
                if(context.getContentResolver().insert(Touchstream.CONTENT_URI, contentvalues) != null)
                    PITouchstreamContract.schedule(context, submittingSessionId);
            }
/*
            private final Context val$context;
            private final String val$data;
            private final Location val$locationCopy;
            private final long val$submittingSessionId;
            private final TouchstreamType val$type;
            private final long val$venueId;
            private final long val$visitTime;
*/

            {
//                super();
/*
                submittingSessionId = 0;
//                submittingSessionId = l;
                data = s;
                locationCopy = location;
                venueId = l1;
                visitTime = l2;
                type = touchstreamtype;
                context = context1;
                */
            }
        }
).start();
    }

    public static final String getAuthority()
    {
//        com/pointinside/android/piwebservices/provider/PITouchstreamContract;
//        JVM INSTR monitorenter ;
    	try {
        String s = sAuthority.getAuthority();
//        com/pointinside/android/piwebservices/provider/PITouchstreamContract;
//        JVM INSTR monitorexit ;
        return s;
    	} catch(Exception exception) {
    		exception.printStackTrace();
        //throw exception;
    	}
    	return "";
    }

    private static final Uri getBaseUri()
    {
//        com/pointinside/android/piwebservices/provider/PITouchstreamContract;
//        JVM INSTR monitorenter ;
    	try {
        Uri uri = sAuthority.getBaseUri();
//        com/pointinside/android/piwebservices/provider/PITouchstreamContract;
//        JVM INSTR monitorexit ;
        return uri;
    	} catch(Exception exception) {
        exception.printStackTrace();
        //throw exception;
    	}
    	return null;
    }

    public static long getCurrentSessionId()
    {
        return sPrefs.getLong("session_id", 0L);
    }

    public static long getSessionId()
    {
        long l = sPrefs.getLong("session_id", 0L);
        if(l == 0L)
        {
            l = System.currentTimeMillis();
            sPrefs.edit().putLong("session_id", l).commit();
        }
        return l;
    }

    public static void init(Context context)
    {
    	try {
//        com/pointinside/android/piwebservices/provider/PITouchstreamContract;
//        JVM INSTR monitorenter ;
        sAuthority = new DynamicAuthorityHelper(context, PITouchstreamProvider.class, ".provider.PITouchstreamProvider");
        sPrefs = context.getSharedPreferences(TAG, 0);
//        com/pointinside/android/piwebservices/provider/PITouchstreamContract;
//        JVM INSTR monitorexit ;
        return;
    	} catch(Exception exception) {
    		exception.printStackTrace();
        //throw exception;
    	}
    }

    private static ContentValues makeTouchstreamValues(long l, String s, Location location, long l1, long l2,
            TouchstreamType touchstreamtype)
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put("_data", s);
        contentvalues.put("data_type", Integer.valueOf(touchstreamtype.ordinal()));
        if(l2 > 0L)
            contentvalues.put("visit_time", Long.valueOf(l2));
        if(l1 > 0L)
            contentvalues.put("venue_id", Long.valueOf(l1));
        if(location != null)
        {
            contentvalues.put("loc_time", Long.valueOf(location.getTime()));
            contentvalues.put("latitude", Double.valueOf(location.getLatitude()));
            contentvalues.put("longitude", Double.valueOf(location.getLongitude()));
            contentvalues.put("altitude", Double.valueOf(location.getAltitude()));
            contentvalues.put("loc_accuracy", String.valueOf(location.getAccuracy()));
            contentvalues.put("loc_provider", location.getProvider());
        }
        contentvalues.put("session_id", Long.valueOf(l));
        return contentvalues;
    }

    public static void resetSessionId()
    {
        sPrefs.edit().remove("session_id").commit();
    }

    public static void schedule(Context context, long l)
    {
        schedule(context, l, 0x2bf20L);
    }

    public static void schedule(Context context, long l, long l1)
    {
        Intent intent = new Intent(context, AnalyticsService.class);
        intent.putExtra("session_id", l);
        PendingIntent pendingintent = PendingIntent.getService(context, 0, intent, 0x10000000);
        AlarmManager alarmmanager = (AlarmManager)context.getSystemService("alarm");
        alarmmanager.cancel(pendingintent);
        alarmmanager.set(0, l1 + System.currentTimeMillis(), pendingintent);
    }

    private static final String SESSION_ID_KEY = "session_id";
    static final int SUBMIT_DELAY = 0x2bf20;
    private static final String TAG = PITouchstreamContract.class.getSimpleName();
    private static DynamicAuthorityHelper sAuthority;
    private static SharedPreferences sPrefs;



}
