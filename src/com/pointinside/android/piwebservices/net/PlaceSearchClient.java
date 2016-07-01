package com.pointinside.android.piwebservices.net;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.api.net.JSONWebRequester.RestResponseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceSearchClient
{
  public static final String DATASOURCE_PI = "pi";
  public static final String DATASOURCE_RETAILIGENCE = "retailigence";
  public static final String DATASOURCE_URBANQ = "urbanQ";
  public static final String FIELD_ADDRESS = "address";
  public static final String FIELD_ATTRIBUTES = "attributes";
  public static final String FIELD_ATTRIBUTE_NAME = "name";
  public static final String FIELD_ATTRIBUTE_VALUE = "value";
  public static final String FIELD_CATEGORY = "category";
  public static final String FIELD_CITY = "city";
  public static final String FIELD_CLOSE_FRIDAY_HOURS = "friClose";
  public static final String FIELD_CLOSE_MONDAY_HOURS = "monClose";
  public static final String FIELD_CLOSE_SATURDAY_HOURS = "satClose";
  public static final String FIELD_CLOSE_SUNDAY_HOURS = "sunClose";
  public static final String FIELD_CLOSE_THURSDAY_HOURS = "thuClose";
  public static final String FIELD_CLOSE_TUESDAY_HOURS = "tueClose";
  public static final String FIELD_CLOSE_WEDNESDAY_HOURS = "wedClose";
  public static final String FIELD_DESCRIPTION = "description";
  public static final String FIELD_HOURS = "hours";
  public static final String FIELD_HOURS_RAW = "rawHours";
  public static final String FIELD_ID = "id";
  public static final String FIELD_LATITUDE = "lat";
  public static final String FIELD_LIKE_COUNT = "likeCount";
  public static final String FIELD_LOGO_SMALL = "logoSmall";
  public static final String FIELD_LONGITUDE = "long";
  public static final String FIELD_OPEN_FRIDAY_HOURS = "friOpen";
  public static final String FIELD_OPEN_MONDAY_HOURS = "monOpen";
  public static final String FIELD_OPEN_SATURDAY_HOURS = "satOpen";
  public static final String FIELD_OPEN_SUNDAY_HOURS = "sunOpen";
  public static final String FIELD_OPEN_THURSDAY_HOURS = "thuOpen";
  public static final String FIELD_OPEN_TUESDAY_HOURS = "tueOpen";
  public static final String FIELD_OPEN_WEDNESDAY_HOURS = "wedOpen";
  public static final String FIELD_PHONE = "phone";
  public static final String FIELD_PRICE = "price";
  public static final String FIELD_RATING = "rating";
  public static final String FIELD_STATE = "state";
  public static final String FIELD_STREET = "street1";
  public static final String FIELD_TITLE = "title";
  public static final String FIELD_USER_RATING = "userRating";
  public static final String FIELD_VENUE_PROXIMITY = "venueProximity";
  public static final String FIELD_VENUE_PROXIMITY_RELATIONSHIP = "relationship";
  public static final String FIELD_VENUE_PROXIMITY_VENUE_UUID = "venueUUID";
  public static final String FIELD_WEBSITE_LABEL = "websiteLabel";
  public static final String FIELD_WEBSITE_URL = "websiteUrl";
  public static final String FIELD_ZIP = "zip";
  private static final String LITERAL_VENUE_PROXIMITY_ISVENUE = "isVenue";
  private static final String LITERAL_VENUE_PROXIMITY_MAPPED = "mapped";
  private static final String LITERAL_VENUE_PROXIMITY_NEARBY = "nearby";
  private static final String PARAM_ACTION_TYPE = "actionType";
  private static final String PARAM_API_KEY = "api_key";
  private static final String PARAM_CATEGORY_ID = "categoryId";
  private static final String PARAM_IN_VENUE = "inVenue";
  private static final String PARAM_LATITUDE = "lat";
  private static final String PARAM_LONGITUDE = "long";
  private static final String PARAM_MAX_RESULTS = "maxResults";
  private static final String PARAM_OUTPUT_FIELDS = "outputFields";
  private static final String PARAM_PI_PLACE_ID = "pi:id";
  private static final String PARAM_QUERY = "q";
  private static final String PARAM_RADIUS = "radius";
  private static final String PARAM_START_INDEX = "startIndex";
  private static final String PARAM_URBANQ_ID = "urbanq:id";
  private static final String RESULT_ID = "id";
  private static final String RESULT_PLACE = "place";
  private static final String RESULT_PLACES = "places";
  private static final String RESULT_STATUS = "status";
  private static final int STATUS_PLACE_NOT_FOUND = 404;
  private static final Uri URL = Uri.parse("http://interact.pointinside.com/sapi/v1");
  public static final int VENUE_PROXIMITY_IS_VENUE = 3;
  public static final int VENUE_PROXIMITY_MAPPED = 1;
  public static final int VENUE_PROXIMITY_NEARBY = 2;
  private static final int VENUE_PROXIMITY_UNKNOWN = 0;
  private static WebServiceDescriptor sDescriptor;
  private static PlaceSearchClient sInstance;
  private final JSONWebRequester mRequester;

  private PlaceSearchClient(JSONWebRequester paramJSONWebRequester)
  {
    this.mRequester = paramJSONWebRequester;
  }

  public static PlaceSearchClient getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null) {
    	    sInstance = new PlaceSearchClient(PIWebServices.getWebRequester(paramContext));
      }
      if (sDescriptor == null) {
        throw new IllegalStateException("Must call PlaceSearchClient.init first");
      }
    } catch(Exception ex){
    	ex.printStackTrace();
    }
    PlaceSearchClient localPlaceSearchClient = sInstance;
    return localPlaceSearchClient;
  }

  public static void init(String paramString)
  {
    try
    {
      if (sInstance != null) {
        throw new IllegalStateException();
      }
    }
    finally {}
    sDescriptor = new WebServiceDescriptor(URL, paramString);
  }

  public PlaceResult getPlace(PlaceRequest paramPlaceRequest)
    throws JSONWebRequester.RestResponseException
  {
    Uri.Builder localBuilder = sDescriptor.getMethodUriBuilder("place");
    paramPlaceRequest.apply(localBuilder, sDescriptor.apiKey);
    HttpGet localHttpGet = new HttpGet(localBuilder.build().toString());
    try
    {
      PlaceResult localPlaceResult = PlaceResult.fromJSON(this.mRequester.execute(localHttpGet));
      return localPlaceResult;
    }
    catch (JSONException localJSONException)
    {
      throw new JSONWebRequester.RestResponseException(localJSONException);
    }
  }

  public PlacesResult getPlaces(PlacesRequest paramPlacesRequest)
    throws JSONWebRequester.RestResponseException
  {
    if (paramPlacesRequest.query != null) {}
    for (String str = "places";; str = "placesByCategory")
    {
      Uri.Builder localBuilder = sDescriptor.getMethodUriBuilder(str);
      paramPlacesRequest.apply(localBuilder, sDescriptor.apiKey);
      HttpGet localHttpGet = new HttpGet(localBuilder.build().toString());
      try
      {
        PlacesResult localPlacesResult = PlacesResult.fromJSON(this.mRequester.execute(localHttpGet));
        return localPlacesResult;
      }
      catch (JSONException localJSONException)
      {
        throw new JSONWebRequester.RestResponseException(localJSONException);
      }
    }
  }

  public PlaceFeedbackResult sendFeedback(PlaceFeedbackRequest paramPlaceFeedbackRequest)
    throws JSONWebRequester.RestResponseException
  {
    Uri.Builder localBuilder = sDescriptor.getMethodUriBuilder("placeFeedback");
    paramPlaceFeedbackRequest.apply(localBuilder, sDescriptor.apiKey);
    HttpGet localHttpGet = new HttpGet(localBuilder.build().toString());
    try
    {
      PlaceFeedbackResult localPlaceFeedbackResult = PlaceFeedbackResult.fromJSON(this.mRequester.execute(localHttpGet));
      return localPlaceFeedbackResult;
    }
    catch (JSONException localJSONException)
    {
      throw new JSONWebRequester.RestResponseException(localJSONException);
    }
  }

  public static class Place
  {
    private Map<String, String> attributes;
    private String category;
    private String city;
    private String description;
    private boolean hasAttributes;
    private boolean hasCategory;
    private boolean hasCity;
    private boolean hasDescription;
    private boolean hasHoursCloseFriday;
    private boolean hasHoursCloseMonday;
    private boolean hasHoursCloseSaturday;
    private boolean hasHoursCloseSunday;
    private boolean hasHoursCloseThursday;
    private boolean hasHoursCloseTuesday;
    private boolean hasHoursCloseWednesday;
    private boolean hasHoursOpenFriday;
    private boolean hasHoursOpenMonday;
    private boolean hasHoursOpenSaturday;
    private boolean hasHoursOpenSunday;
    private boolean hasHoursOpenThursday;
    private boolean hasHoursOpenTuesday;
    private boolean hasHoursOpenWednesday;
    private boolean hasLatitude;
    private boolean hasLikeCount;
    private boolean hasLongitude;
    private boolean hasPhoneNumber;
    private boolean hasPrice;
    private boolean hasRawHours;
    private boolean hasState;
    private boolean hasStreet;
    private boolean hasTitle;
    private boolean hasUserRating;
    private boolean hasVenueProximity;
    private boolean hasWebLabel;
    private boolean hasWebUrl;
    private boolean hasZip;
    private String hoursCloseFriday;
    private String hoursCloseMonday;
    private String hoursCloseSaturday;
    private String hoursCloseSunday;
    private String hoursCloseThursday;
    private String hoursCloseTuesday;
    private String hoursCloseWednesday;
    private String hoursOpenFriday;
    private String hoursOpenMonday;
    private String hoursOpenSaturday;
    private String hoursOpenSunday;
    private String hoursOpenThursday;
    private String hoursOpenTuesday;
    private String hoursOpenWednesday;
    private final HashMap<String, String> ids = new HashMap();
    private double latitude;
    private int likeCount;
    private double longitude;
    private String phoneNumber;
    private String price;
    private String rawHours;
    private String state;
    private String street;
    private String title;
    private float userRating;
    private int venueProximity;
    private String venueUUID;
    private String webLabel;
    private String webUrl;
    private String zip;

    private Place(JSONObject jsonobject)
            throws JSONException
        {
            JSONObject jsonobject1;
            Iterator iterator;
            HashMap ids = new HashMap();
            jsonobject1 = jsonobject.getJSONObject("id");
            iterator = jsonobject1.keys();
            String s;
            boolean flag10;
            boolean flag11;
            boolean flag12;
            int i;
            JSONObject jsonobject3;
            boolean flag13;
            boolean flag14;
            boolean flag15;
            boolean flag16;
            boolean flag17;
            boolean flag18;
            boolean flag19;
            boolean flag20;
            boolean flag21;
            boolean flag22;
            boolean flag23;
            boolean flag24;
            boolean flag25;
            boolean flag26;
            JSONObject jsonobject4;
            boolean flag27;
            boolean flag28;
            boolean flag29;
            boolean flag30;

            while(iterator.hasNext()) {
                s = (String)iterator.next();
                ids.put(s, jsonobject1.getString(s));
            }

            JSONArray jsonarray;
            int j;
            boolean flag = jsonobject.has("title");
            hasTitle = flag;
            if(flag)
                title = jsonobject.getString("title");
            boolean flag1 = hasAndIsNotEmpty(jsonobject, "description");
            hasDescription = flag1;
            if(flag1)
            {
                description = jsonobject.getString("description");
                if(description.startsWith("\"") && description.endsWith("\""))
                    description = description.substring(1, -1 + description.length());
            }
            boolean flag2 = hasAndIsNotEmpty(jsonobject, "phone");
            hasPhoneNumber = flag2;
            if(flag2)
                phoneNumber = jsonobject.getString("phone");
            boolean flag3 = hasAndIsNotEmpty(jsonobject, "websiteLabel");
            hasWebLabel = flag3;
            if(flag3)
                webLabel = jsonobject.getString("websiteLabel");
            boolean flag4 = hasAndIsNotEmpty(jsonobject, "websiteUrl");
            hasWebUrl = flag4;
            if(flag4)
                webUrl = jsonobject.getString("websiteUrl");
            boolean flag5 = jsonobject.has("lat");
            hasLatitude = flag5;
            if(flag5)
                latitude = jsonobject.getDouble("lat");
            boolean flag6 = jsonobject.has("long");
            hasLongitude = flag6;
            if(flag6)
                longitude = jsonobject.getDouble("long");
            boolean flag7 = jsonobject.has("likeCount");
            hasLikeCount = flag7;
            if(flag7)
                likeCount = jsonobject.getInt("likeCount");
            boolean flag8 = hasAndIsNotEmpty(jsonobject, "userRating");
            hasUserRating = flag8;
            if(flag8)
                userRating = (float)jsonobject.getDouble("userRating");
            boolean flag9 = jsonobject.has("rawHours");
            hasRawHours = flag9;
            if(flag9)
                rawHours = jsonobject.getString("rawHours");
            if(jsonobject.has("venueProximity"))
            {
                JSONObject jsonobject5 = jsonobject.getJSONObject("venueProximity");
                String s3 = jsonobject5.getString("relationship");
                if("mapped".equals(s3))
                    venueProximity = 1;
                else
                if("nearby".equals(s3))
                    venueProximity = 2;
                else
                if("isVenue".equals(s3))
                    venueProximity = 3;
                else
                    venueProximity = 0;
                if(venueProximity != 0)
                {
              if(venueProximity != 3 || jsonobject5.has("venueUUID"))
                        venueUUID = jsonobject5.getString("venueUUID");
                    else
                        venueUUID = (String)ids.get("pi");
                    hasVenueProximity = true;
                } else
                {
                    hasVenueProximity = false;
                }
            }
            if(jsonobject.has("address"))
            {
                jsonobject4 = jsonobject.getJSONObject("address");
                flag27 = jsonobject4.has("street1");
                hasStreet = flag27;
                if(flag27)
                    street = jsonobject4.getString("street1");
                flag28 = jsonobject4.has("city");
                hasCity = flag28;
                if(flag28)
                    city = jsonobject4.getString("city");
                flag29 = jsonobject4.has("state");
                hasState = flag29;
                if(flag29)
                    state = jsonobject4.getString("state");
                flag30 = jsonobject4.has("zip");
                hasZip = flag30;
                if(flag30)
                    zip = jsonobject4.getString("zip");
            }
            if(jsonobject.has("hours"))
            {
                jsonobject3 = jsonobject.getJSONObject("hours");
                flag13 = jsonobject3.has("monOpen");
                hasHoursOpenMonday = flag13;
                if(flag13)
                    hoursOpenMonday = jsonobject3.getString("monOpen");
                flag14 = jsonobject3.has("monClose");
                hasHoursCloseMonday = flag14;
                if(flag14)
                    hoursCloseMonday = jsonobject3.getString("monClose");
                flag15 = jsonobject3.has("tueOpen");
                hasHoursOpenTuesday = flag15;
                if(flag15)
                    hoursOpenTuesday = jsonobject3.getString("tueOpen");
                flag16 = jsonobject3.has("tueClose");
                hasHoursCloseTuesday = flag16;
                if(flag16)
                    hoursCloseTuesday = jsonobject3.getString("tueClose");
                flag17 = jsonobject3.has("wedOpen");
                hasHoursOpenWednesday = flag17;
                if(flag17)
                    hoursOpenWednesday = jsonobject3.getString("wedOpen");
                flag18 = jsonobject3.has("wedClose");
                hasHoursCloseWednesday = flag18;
                if(flag18)
                    hoursCloseWednesday = jsonobject3.getString("wedClose");
                flag19 = jsonobject3.has("thuOpen");
                hasHoursOpenThursday = flag19;
                if(flag19)
                    hoursOpenThursday = jsonobject3.getString("thuOpen");
                flag20 = jsonobject3.has("thuClose");
                hasHoursCloseThursday = flag20;
                if(flag20)
                    hoursCloseThursday = jsonobject3.getString("thuClose");
                flag21 = jsonobject3.has("friOpen");
                hasHoursOpenFriday = flag21;
                if(flag21)
                    hoursOpenFriday = jsonobject3.getString("friOpen");
                flag22 = jsonobject3.has("friClose");
                hasHoursCloseFriday = flag22;
                if(flag22)
                    hoursCloseFriday = jsonobject3.getString("friClose");
                flag23 = jsonobject3.has("satOpen");
                hasHoursOpenSaturday = flag23;
                if(flag23)
                    hoursOpenSaturday = jsonobject3.getString("satOpen");
                flag24 = jsonobject3.has("satClose");
                hasHoursCloseSaturday = flag24;
                if(flag24)
                    hoursCloseSaturday = jsonobject3.getString("satClose");
                flag25 = jsonobject3.has("sunOpen");
                hasHoursOpenSunday = flag25;
                if(flag25)
                    hoursOpenSunday = jsonobject3.getString("sunOpen");
                flag26 = jsonobject3.has("sunClose");
                hasHoursCloseSunday = flag26;
                if(flag26)
                    hoursCloseSunday = jsonobject3.getString("sunClose");
            }
            flag10 = jsonobject.has("attributes");
            hasAttributes = flag10;
            if(flag10) {
            attributes = new LinkedHashMap();
            jsonarray = jsonobject.getJSONArray("attributes");
            i = jsonarray.length();
            j = 0;
            for(;j < i;){
                JSONObject jsonobject2 = jsonarray.getJSONObject(j);
                String s1 = jsonobject2.getString("name");
                String s2 = jsonobject2.getString("value");
                if(!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2))
                    attributes.put(s1, s2);
                j++;
            }
            flag11 = hasAndIsNotEmpty(jsonobject, "category");
            hasCategory = flag11;
            if(flag11)
                category = jsonobject.getString("category");
            flag12 = hasAndIsNotEmpty(jsonobject, "price");
            hasPrice = flag12;
            if(flag12)
                price = jsonobject.getString("price");
            return;
        }
    }

    private static boolean hasAndIsNotEmpty(JSONObject paramJSONObject, String paramString)
      throws JSONException
    {
      return (paramJSONObject.has(paramString)) && (!TextUtils.isEmpty(paramJSONObject.getString(paramString)));
    }

    private static void throwUnless(String paramString, boolean paramBoolean)
    {
      if (!paramBoolean) {
        throw new IllegalArgumentException("'" + paramString + "' does not exist in place result");
      }
    }

    public Map<String, String> getAttributes()
    {
      throwUnless("attributes", this.hasAttributes);
      return Collections.unmodifiableMap(this.attributes);
    }

    public String getCategory()
    {
      throwUnless("category", this.hasCategory);
      return this.category;
    }

    public String getCity()
    {
      throwUnless("city", this.hasCity);
      return this.city;
    }

    public String getDescription()
    {
      throwUnless("description", this.hasDescription);
      return this.description;
    }

    public String getHoursCloseFriday()
    {
      throwUnless("friClose", this.hasHoursCloseFriday);
      return this.hoursCloseFriday;
    }

    public String getHoursCloseMonday()
    {
      throwUnless("monClose", this.hasHoursCloseMonday);
      return this.hoursCloseMonday;
    }

    public String getHoursCloseSaturday()
    {
      throwUnless("satClose", this.hasHoursCloseSaturday);
      return this.hoursCloseSaturday;
    }

    public String getHoursCloseSunday()
    {
      throwUnless("sunClose", this.hasHoursCloseSunday);
      return this.hoursCloseSunday;
    }

    public String getHoursCloseThursday()
    {
      throwUnless("thuClose", this.hasHoursCloseThursday);
      return this.hoursCloseThursday;
    }

    public String getHoursCloseTuesday()
    {
      throwUnless("tueClose", this.hasHoursCloseTuesday);
      return this.hoursCloseTuesday;
    }

    public String getHoursCloseWednesday()
    {
      throwUnless("wedClose", this.hasHoursCloseWednesday);
      return this.hoursCloseWednesday;
    }

    public String getHoursOpenFriday()
    {
      throwUnless("friOpen", this.hasHoursOpenFriday);
      return this.hoursOpenFriday;
    }

    public String getHoursOpenMonday()
    {
      throwUnless("monOpen", this.hasHoursOpenMonday);
      return this.hoursOpenMonday;
    }

    public String getHoursOpenSaturday()
    {
      throwUnless("satOpen", this.hasHoursOpenSaturday);
      return this.hoursOpenSaturday;
    }

    public String getHoursOpenSunday()
    {
      throwUnless("sunOpen", this.hasHoursOpenSunday);
      return this.hoursOpenSunday;
    }

    public String getHoursOpenThursday()
    {
      throwUnless("thuOpen", this.hasHoursOpenThursday);
      return this.hoursOpenThursday;
    }

    public String getHoursOpenTuesday()
    {
      throwUnless("tueOpen", this.hasHoursOpenTuesday);
      return this.hoursOpenTuesday;
    }

    public String getHoursOpenWednesday()
    {
      throwUnless("wedOpen", this.hasHoursOpenWednesday);
      return this.hoursOpenWednesday;
    }

    public String getId(String paramString)
    {
      return (String)this.ids.get(paramString);
    }

    public double getLatitude()
    {
      throwUnless("lat", this.hasLatitude);
      return this.latitude;
    }

    public int getLikeCount()
    {
      throwUnless("likeCount", this.hasLikeCount);
      return this.likeCount;
    }

    public double getLongitude()
    {
      throwUnless("long", this.hasLongitude);
      return this.longitude;
    }

    public String getPhoneNumber()
    {
      throwUnless("phone", this.hasPhoneNumber);
      return this.phoneNumber;
    }

    public String getPrice()
    {
      throwUnless("price", this.hasPrice);
      return this.price;
    }

    public int getProximityVenueRelationship()
    {
      throwUnless("venueProximity", this.hasVenueProximity);
      return this.venueProximity;
    }

    public String getProximityVenueUUID()
    {
      throwUnless("venueProximity", this.hasVenueProximity);
      return this.venueUUID;
    }

    public String getRawHours()
    {
      throwUnless("rawHours", this.hasRawHours);
      return this.rawHours;
    }

    public String getState()
    {
      throwUnless("state", this.hasState);
      return this.state;
    }

    public String getStreet()
    {
      throwUnless("street1", this.hasStreet);
      return this.street;
    }

    public String getTitle()
    {
      throwUnless("title", this.hasTitle);
      return this.title;
    }

    public float getUserRating()
    {
      throwUnless("userRating", this.hasUserRating);
      return this.userRating;
    }

    public String getWebLabel()
    {
      throwUnless("websiteLabel", this.hasWebLabel);
      return this.webLabel;
    }

    public String getWebUrl()
    {
      throwUnless("websiteUrl", this.hasWebUrl);
      return this.webUrl;
    }

    public String getZip()
    {
      throwUnless("zip", this.hasZip);
      return this.zip;
    }

    public boolean hasAttributes()
    {
      return this.hasAttributes;
    }

    public boolean hasCategory()
    {
      return this.hasCategory;
    }

    public boolean hasCity()
    {
      return this.hasCity;
    }

    public boolean hasDescription()
    {
      return this.hasDescription;
    }

    public boolean hasHoursCloseFriday()
    {
      return this.hasHoursCloseFriday;
    }

    public boolean hasHoursCloseMonday()
    {
      return this.hasHoursCloseMonday;
    }

    public boolean hasHoursCloseSaturday()
    {
      return this.hasHoursCloseSaturday;
    }

    public boolean hasHoursCloseSunday()
    {
      return this.hasHoursCloseSunday;
    }

    public boolean hasHoursCloseThursday()
    {
      return this.hasHoursCloseThursday;
    }

    public boolean hasHoursCloseTuesday()
    {
      return this.hasHoursCloseTuesday;
    }

    public boolean hasHoursCloseWednesday()
    {
      return this.hasHoursCloseWednesday;
    }

    public boolean hasHoursOpenFriday()
    {
      return this.hasHoursOpenFriday;
    }

    public boolean hasHoursOpenMonday()
    {
      return this.hasHoursOpenMonday;
    }

    public boolean hasHoursOpenSaturday()
    {
      return this.hasHoursOpenSaturday;
    }

    public boolean hasHoursOpenSunday()
    {
      return this.hasHoursOpenSunday;
    }

    public boolean hasHoursOpenThursday()
    {
      return this.hasHoursOpenThursday;
    }

    public boolean hasHoursOpenTuesday()
    {
      return this.hasHoursOpenTuesday;
    }

    public boolean hasHoursOpenWednesday()
    {
      return this.hasHoursOpenWednesday;
    }

    public boolean hasLikeCount()
    {
      return this.hasLikeCount;
    }

    public boolean hasPhoneNumber()
    {
      return this.hasPhoneNumber;
    }

    public boolean hasPrice()
    {
      return this.hasPrice;
    }

    public boolean hasProximityVenueRelationship()
    {
      return this.hasVenueProximity;
    }

    public boolean hasRawHours()
    {
      return this.hasRawHours;
    }

    public boolean hasState()
    {
      return this.hasState;
    }

    public boolean hasStreet()
    {
      return this.hasStreet;
    }

    public boolean hasUserRating()
    {
      return this.hasUserRating;
    }

    public boolean hasWebLabel()
    {
      return this.hasWebLabel;
    }

    public boolean hasWebUrl()
    {
      return this.hasWebUrl;
    }

    public boolean hasZip()
    {
      return this.hasZip;
    }
  }

  public static class PlaceFeedbackRequest
    extends PIWebServices.CommonRequestObject
  {
    public String actionType;
    public String placeUUID;
    public String urbanQId;

    protected void onApply(Uri.Builder paramBuilder)
    {
      if (this.placeUUID != null) {
        paramBuilder.appendQueryParameter("pi:id", this.placeUUID);
      }
      if (this.urbanQId != null) {
        paramBuilder.appendQueryParameter("urbanq:id", this.urbanQId);
      }
      if (this.actionType != null) {
        paramBuilder.appendQueryParameter("actionType", this.actionType);
      }
    }
  }

  public static class PlaceFeedbackResult
  {
    public final int status;

    private PlaceFeedbackResult(JSONObject paramJSONObject)
      throws JSONException
    {
      this.status = paramJSONObject.getInt("status");
    }

    public static PlaceFeedbackResult fromJSON(JSONObject paramJSONObject)
      throws JSONException
    {
      return new PlaceFeedbackResult(paramJSONObject);
    }
  }

  public static class PlaceRequest
    extends PIWebServices.CommonRequestObject
  {
    public String placeUUID;
    public String urbanQId;

    protected void onApply(Uri.Builder paramBuilder)
    {
      if (this.placeUUID != null) {
        paramBuilder.appendQueryParameter("pi:id", this.placeUUID);
      }
      if (this.urbanQId != null) {
        paramBuilder.appendQueryParameter("urbanq:id", this.urbanQId);
      }
    }
  }

  public static class PlaceResult
  {
    public PlaceSearchClient.Place place;
    public final int status;

    private PlaceResult(JSONObject paramJSONObject)
      throws JSONException
    {
      this.status = paramJSONObject.getInt("status");
      this.place = new PlaceSearchClient.Place(paramJSONObject.getJSONObject("place"));
    }

    public static PlaceResult fromJSON(JSONObject paramJSONObject)
      throws JSONException
    {
      if (paramJSONObject.getInt("status") == 404) {
        return null;
      }
      return new PlaceResult(paramJSONObject);
    }
  }

  public static class PlacesRequest
    extends PIWebServices.CommonRequestObject
  {
    public int categoryServerId;
    public String inVenue;
    public double latitude;
    public double longitude;
    public String query;
    public int radius = 0;

    protected void onApply(android.net.Uri.Builder builder)
    {
        if(query != null)
            builder.appendQueryParameter("q", query);
        else
            builder.appendQueryParameter("categoryId", String.valueOf(categoryServerId));
        builder.appendQueryParameter("lat", String.valueOf(latitude)).appendQueryParameter("long", String.valueOf(longitude));
        if(inVenue != null)
            builder.appendQueryParameter("inVenue", inVenue);
        if(radius > 0)
            builder.appendQueryParameter("radius", String.valueOf(radius));
        if(outputFields.size() > 0)
        {
            builder.appendQueryParameter("outputFields", join(",", outputFields));
            return;
        } else
        {
            outputFields.add("title");
            outputFields.add("lat");
            outputFields.add("long");
            return;
        }
    }
  }

  public static class PlacesResult
  {
    public final ArrayList<PlaceSearchClient.Place> places = new ArrayList();
    public final int status;

    private PlacesResult(JSONObject paramJSONObject)
      throws JSONException
    {
      this.status = paramJSONObject.getInt("status");
      JSONArray localJSONArray = paramJSONObject.getJSONArray("places");
      int i = localJSONArray.length();
      for (int j = 0;; j++)
      {
        if (j >= i) {
          return;
        }
        this.places.add(new PlaceSearchClient.Place(localJSONArray.getJSONObject(j)));
      }
    }

    public static PlacesResult fromJSON(JSONObject paramJSONObject)
      throws JSONException
    {
      return new PlacesResult(paramJSONObject);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.net.PlaceSearchClient
 * JD-Core Version:    0.7.0.1
 */