// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.net;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.api.net.MyHttpClient;
import com.pointinside.android.app.PointInside;
import com.pointinside.android.piwebservices.net.PIWebServices;
import com.pointinside.android.piwebservices.net.WebServiceDescriptor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

public class HolidayGameClient
{
    public static class ClaimRequest extends com.pointinside.android.piwebservices.net.PIWebServices.CommonRequestObject
    {

        protected void onApply(android.net.Uri.Builder builder)
        {
            builder.appendQueryParameter("uuid", uuid);
            builder.appendQueryParameter("birthdate", BIRTHDATE_FORMAT.format(birthdate.getTime()));
            builder.appendQueryParameter("email", email);
            if(!TextUtils.isEmpty(name))
                builder.appendQueryParameter("owner", name);
            if(!TextUtils.isEmpty(phone))
                builder.appendQueryParameter("phone", phone);
        }

        private static final DateFormat BIRTHDATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        public Calendar birthdate;
        public String email;
        public String name;
        public String phone;
        public String uuid;


        public ClaimRequest()
        {
        }
    }

    public static class ClaimResponse
        implements CommonStatusCodes
    {

        public static ClaimResponse fromJSON(JSONObject jsonobject)
            throws JSONException
        {
            return new ClaimResponse(jsonobject);
        }

        public int status;
        public String status_txt;

        private ClaimResponse(JSONObject jsonobject)
            throws JSONException
        {
            status = jsonobject.getInt("status");
            status_txt = jsonobject.getString("status_txt");
        }
    }

    private static interface CommonStatusCodes
    {

        public static final int CODE_MISSING_PARAMETERS = 4;
        public static final int CODE_SUCCESS = 1;
    }

    public static enum GameState
    {
        ON("ON", 0),
        OFF("OFF", 1),
        STARTING("STARTING", 2),
        FINAL("FINAL", 3);

        public String sType;
        public int iType;
        public static GameState valueOf(int i) {
        	GameState gs =null;
        	if(i==0) {
        		gs = ON;
        	} else if(i==1){
        		gs = OFF;
        	} else if(i==2){
        		gs = STARTING;
        	} else if(i==3){
        		gs = FINAL;
        	}
        	return null;
        }
        private GameState(String s, int i)
        {
        	sType = s;
        	iType = i;
        }
    }

    public static class GlobalInfoRequest extends com.pointinside.android.piwebservices.net.PIWebServices.CommonRequestObject
    {

        protected void onApply(android.net.Uri.Builder builder)
        {
        }

        public GlobalInfoRequest()
        {
        }
    }

    public static class InfoResponse
    {

        public static InfoResponse fromJSON(JSONObject jsonobject)
            throws JSONException
        {
            return new InfoResponse(jsonobject);
        }

        private static boolean hasAndIsNotEmpty(JSONObject jsonobject, String s)
            throws JSONException
        {
            return jsonobject.has(s) && !TextUtils.isEmpty(jsonobject.getString(s));
        }

        public GameState gameState;
        public Uri linkUrl;
        public PlaceInfo placeInfo;
        public int status;
        public String status_txt;

        private InfoResponse(JSONObject jsonobject)
            throws JSONException
        {
            status = jsonobject.getInt("status");
            status_txt = jsonobject.getString("status_txt");
            JSONObject jsonobject1 = jsonobject.getJSONObject("game_details");
            gameState = GameState.valueOf(jsonobject1.getString("status"));
            if(jsonobject1.has("place_details"))
                placeInfo = new PlaceInfo(jsonobject1.getJSONObject("place_details"));
            JSONObject jsonobject2 = jsonobject1.getJSONObject("game_text");
            if(hasAndIsNotEmpty(jsonobject2, "link_url"))
                linkUrl = Uri.parse(jsonobject2.getString("link_url"));
        }
    }

    public static class PlaceInfo
    {

        public String owner;
        public int pageViews;
        public PlaceState state;

        PlaceInfo(JSONObject jsonobject)
            throws JSONException
        {
            owner = jsonobject.getString("owner");
            state = PlaceState.valueOf(jsonobject.getString("status"));
            pageViews = jsonobject.getInt("pageviews");
        }
    }

    public static class PlaceInfoRequest extends com.pointinside.android.piwebservices.net.PIWebServices.CommonRequestObject
    {

        protected void onApply(android.net.Uri.Builder builder)
        {
            builder.appendQueryParameter("uuid", uuid);
        }

        public String uuid;

        public PlaceInfoRequest()
        {
        }
    }

    public static enum PlaceState
    {
        Claimed("Claimed", 0),
        Available("Available", 1),
        Owned("Owned", 2);
        public String sType;
        public int iType;
        private PlaceState(String s, int i)
        {
        	sType = s;
        	iType = i;
        }
    }

    public static class ShareRequest extends com.pointinside.android.piwebservices.net.PIWebServices.CommonRequestObject
    {

        protected void onApply(android.net.Uri.Builder builder)
        {
            builder.appendQueryParameter("uuid", uuid);
        }

        public String uuid;

        public ShareRequest()
        {
        }
    }

    public static class ShareResponse
        implements CommonStatusCodes
    {

        public static ShareResponse fromJSON(JSONObject jsonobject)
            throws JSONException
        {
            return new ShareResponse(jsonobject);
        }

        public int status;
        public String status_txt;

        private ShareResponse(JSONObject jsonobject)
            throws JSONException
        {
            status = jsonobject.getInt("status");
            status_txt = jsonobject.getString("status_txt");
        }
    }


    private HolidayGameClient(JSONWebRequester jsonwebrequester)
    {
        MyHttpClient myhttpclient = (MyHttpClient)jsonwebrequester.getHttpClient();
        myhttpclient.setUsePreemptiveAuth(true);
        myhttpclient.getCredentialsProvider().setCredentials(sDescriptor.getAuthScope(), sDescriptor.getCredentials());
        mRequester = jsonwebrequester;
    }

    public static HolidayGameClient getInstance(Context context)
    {
        if(sInstance == null) {

        	sInstance = new HolidayGameClient(PIWebServices.getWebRequester(context));
        	sInstance.init(FIELD_OWNER);
        }
        HolidayGameClient holidaygameclient = sInstance;

        if(sDescriptor == null)
            throw new IllegalStateException("Must call HolidayGameClient.init first");

        return holidaygameclient;
    }

    public static String getSpaceUUID(String s, String s1)
    {
        if(s != null)
            return (new StringBuilder("pi:")).append(s).toString();
        else
            return (new StringBuilder("urbanq:")).append(s1).toString();
    }

    private InfoResponse info(com.pointinside.android.piwebservices.net.PIWebServices.CommonRequestObject commonrequestobject)
        throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException
    {
        android.net.Uri.Builder builder = sDescriptor.getMethodUriBuilder("info");
        commonrequestobject.apply(builder, sDescriptor.apiKey);
        HttpGet httpget = new HttpGet(builder.build().toString());
        InfoResponse inforesponse;
        try
        {
            inforesponse = InfoResponse.fromJSON(mRequester.execute(httpget));
        }
        catch(JSONException jsonexception)
        {
            throw new com.pointinside.android.api.net.JSONWebRequester.RestResponseException(jsonexception);
        }
        return inforesponse;
    }

    public static void init(String s)
    {
        if(sInstance != null) {
        	return;
            //throw new IllegalStateException();
        }
        sDescriptor = new WebServiceDescriptor(URL, PointInside.BASE_CREDENTIALS.getUserName(), PointInside.BASE_CREDENTIALS.getPassword(), s);
    }

    public ClaimResponse claimSpace(ClaimRequest claimrequest)
        throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException
    {
        android.net.Uri.Builder builder = sDescriptor.getMethodUriBuilder("claim");
        claimrequest.apply(builder, sDescriptor.apiKey);
        HttpGet httpget = new HttpGet(builder.build().toString());
        ClaimResponse claimresponse;
        try
        {
            claimresponse = ClaimResponse.fromJSON(mRequester.execute(httpget));
        }
        catch(JSONException jsonexception)
        {
            throw new com.pointinside.android.api.net.JSONWebRequester.RestResponseException(jsonexception);
        }
        return claimresponse;
    }

    public InfoResponse globalInfo()
        throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException
    {
        return info(new GlobalInfoRequest());
    }

    public ShareResponse notifyShare(ShareRequest sharerequest)
        throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException
    {
        android.net.Uri.Builder builder = sDescriptor.getMethodUriBuilder("share");
        sharerequest.apply(builder, sDescriptor.apiKey);
        HttpGet httpget = new HttpGet(builder.build().toString());
        ShareResponse shareresponse;
        try
        {
            shareresponse = ShareResponse.fromJSON(mRequester.execute(httpget));
        }
        catch(JSONException jsonexception)
        {
            throw new com.pointinside.android.api.net.JSONWebRequester.RestResponseException(jsonexception);
        }
        return shareresponse;
    }

    public ShareResponse notifyShare(String s)
        throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException
    {
        ShareRequest sharerequest = new ShareRequest();
        sharerequest.uuid = s;
        return notifyShare(sharerequest);
    }

    public InfoResponse placeInfo(String s)
        throws com.pointinside.android.api.net.JSONWebRequester.RestResponseException
    {
        PlaceInfoRequest placeinforequest = new PlaceInfoRequest();
        placeinforequest.uuid = s;
        return info(placeinforequest);
    }

    public static final String FIELD_GAME_DETAILS = "game_details";
    public static final String FIELD_GAME_TEXT = "game_text";
    public static final String FIELD_LINK_URL = "link_url";
    public static final String FIELD_OWNER = "owner";
    public static final String FIELD_PAGE_VIEWS = "pageviews";
    public static final String FIELD_PLACE_DETAILS = "place_details";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_STATUS_TXT = "status_txt";
    private static final String PARAM_BIRTHDATE = "birthdate";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_OWNER = "owner";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_UUID = "uuid";
    private static final Uri URL = Uri.parse("https://smartmaps.pointinside.com/api/game");
    private static WebServiceDescriptor sDescriptor;
    private static HolidayGameClient sInstance;
    private final JSONWebRequester mRequester;

}
