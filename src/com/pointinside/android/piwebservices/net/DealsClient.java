package com.pointinside.android.piwebservices.net;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.api.net.JSONWebRequester.RestResponseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DealsClient
{
  public static final String FIELD_BRAND = "brand";
  public static final String FIELD_CATEGORY = "category";
  public static final String FIELD_DESCRIPTION = "description";
  public static final String FIELD_DESTINATION_UUID = "destination_uuid";
  public static final String FIELD_DISPLAY_END_DATE = "display_end_date";
  public static final String FIELD_DISPLAY_IMAGE = "display_image";
  public static final String FIELD_DISPLAY_START_DATE = "display_start_date";
  public static final String FIELD_DISTANCE = "distance";
  public static final String FIELD_END_DATE = "end_date";
  public static final String FIELD_LATITUDE = "latitude";
  public static final String FIELD_LONGITUDE = "longitude";
  public static final String FIELD_PLACE_NAME = "organization";
  public static final String FIELD_PLACE_UUID = "place_uuid";
  public static final String FIELD_START_DATE = "start_date";
  public static final String FIELD_THUMBNAIL_IMAGE = "thumbnail_image";
  public static final String FIELD_TITLE = "title";
  public static final String FIELD_TYPE = "type";
  public static final String FIELD_UPC = "upc";
  private static final String PARAM_API_KEY = "api_key";
  private static final String PARAM_LATITUDE = "lat";
  private static final String PARAM_LONGITUDE = "long";
  private static final String PARAM_MAX_RESULTS = "max_results";
  private static final String PARAM_OUTPUT_FIELDS = "output_fields";
  private static final String PARAM_RADIUS = "radius";
  private static final String PARAM_START_INDEX = "start_index";
  private static final String REQUEST_TYPE_DESTINATION = "destination";
  private static final String REQUEST_TYPE_NEARBY = "nearby";
  private static final String REQUEST_TYPE_OFFERS = "offers";
  private static final String RESULT_ID = "id";
  private static final String RESULT_OFFERS = "offers";
  private static final String RESULT_STATUS = "status_code";
  private static final Uri URL = Uri.parse("http://interact.pointinside.com/adws/v1");
  private static WebServiceDescriptor sDescriptor;
  private static DealsClient sInstance;
  private final JSONWebRequester mRequester;

  private DealsClient(JSONWebRequester paramJSONWebRequester)
  {
    this.mRequester = paramJSONWebRequester;
  }

  private static DealsResult executeAndGetResult(JSONWebRequester paramJSONWebRequester, String paramString)
    throws JSONWebRequester.RestResponseException
  {
    HttpGet localHttpGet = new HttpGet(paramString);
    try
    {
      DealsResult localDealsResult = DealsResult.fromJSON(paramJSONWebRequester.execute(localHttpGet));
      return localDealsResult;
    }
    catch (JSONException localJSONException)
    {
      throw new JSONWebRequester.RestResponseException(localJSONException);
    }
    catch (ParseException localParseException)
    {
      throw new JSONWebRequester.RestResponseException(localParseException);
    }
  }

  public static DealsClient getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null) {
    	    sInstance = new DealsClient(PIWebServices.getWebRequester(paramContext));
      }
      if (sDescriptor == null) {
        throw new IllegalStateException("Must call DealsClient.init first");
      }
    } catch(Exception ex) {
    	ex.printStackTrace();
    }

    DealsClient localDealsClient = sInstance;
    return localDealsClient;
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

  public DealsResult getDestinationDeals(DestinationDealsRequest paramDestinationDealsRequest)
    throws JSONWebRequester.RestResponseException
  {
    Uri.Builder localBuilder = sDescriptor.getMethodUriBuilder("destination");
    localBuilder.appendPath(paramDestinationDealsRequest.venue);
    localBuilder.appendPath("offers");
    paramDestinationDealsRequest.apply(localBuilder, sDescriptor.apiKey);
    return executeAndGetResult(this.mRequester, localBuilder.build().toString());
  }

  public DealsResult getNearbyDeals(NearbyDealsRequest paramNearbyDealsRequest)
    throws JSONWebRequester.RestResponseException
  {
    Uri.Builder localBuilder = sDescriptor.getMethodUriBuilder("nearby");
    localBuilder.appendPath("offers");
    paramNearbyDealsRequest.apply(localBuilder, sDescriptor.apiKey);
    return executeAndGetResult(this.mRequester, localBuilder.build().toString());
  }

  public static class Deal
  {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
    private String brand;
    private String category;
    private String description;
    private String displayEndDate;
    private String displayImage;
    private String displayStartDate;
    private String distance;
    private String endDate;
    private boolean hasBrand;
    private boolean hasCategory;
    private boolean hasDescription;
    private boolean hasDisplayEndDate;
    private boolean hasDisplayImage;
    private boolean hasDisplayStartDate;
    private boolean hasDistance;
    private boolean hasEndDate;
    private boolean hasLatitude;
    private boolean hasLongitude;
    private boolean hasPlaceName;
    private boolean hasPlaceUUID;
    private boolean hasStartDate;
    private boolean hasThumbnailImage;
    private boolean hasTitle;
    private boolean hasType;
    private boolean hasUpc;
    private boolean hasVenueUUID;
    private final HashMap<String, String> ids = new HashMap();
    private double latitude;
    private double longitude;
    private String placeName;
    private String placeUUID;
    private String startDate;
    private String thumbnailImage;
    private String title;
    private String type;
    private String upc;
    private String venueUUID;

    private Deal(JSONObject paramJSONObject)
      throws JSONException, ParseException
    {
      JSONObject localJSONObject = paramJSONObject.getJSONObject("id");
      Iterator localIterator = localJSONObject.keys();
      for (;;)
      {
        if (!localIterator.hasNext())
        {
          boolean bool1 = paramJSONObject.has("title");
          this.hasTitle = bool1;
          if (bool1) {
            this.title = paramJSONObject.getString("title");
          }
          boolean bool2 = paramJSONObject.has("brand");
          this.hasBrand = bool2;
          if (bool2) {
            this.brand = paramJSONObject.getString("brand");
          }
          boolean bool3 = paramJSONObject.has("type");
          this.hasType = bool3;
          if (bool3) {
            this.type = paramJSONObject.getString("type");
          }
          boolean bool4 = paramJSONObject.has("category");
          this.hasCategory = bool4;
          if (bool4) {
            this.category = paramJSONObject.getString("category");
          }
          boolean bool5 = paramJSONObject.has("distance");
          this.hasDistance = bool5;
          if (bool5) {
            this.distance = paramJSONObject.getString("distance");
          }
          boolean bool6 = paramJSONObject.has("description");
          this.hasDescription = bool6;
          if (bool6) {
            this.description = paramJSONObject.getString("description");
          }
          boolean bool7 = paramJSONObject.has("display_image");
          this.hasDisplayImage = bool7;
          if (bool7) {
            this.displayImage = paramJSONObject.getString("display_image");
          }
          boolean bool8 = paramJSONObject.has("thumbnail_image");
          this.hasThumbnailImage = bool8;
          if (bool8) {
            this.thumbnailImage = paramJSONObject.getString("thumbnail_image");
          }
          boolean bool9 = paramJSONObject.has("start_date");
          this.hasStartDate = bool9;
          if (bool9) {
            this.startDate = parseDate(paramJSONObject.getString("start_date"));
          }
          boolean bool10 = paramJSONObject.has("display_start_date");
          this.hasDisplayStartDate = bool10;
          if (bool10) {
            this.displayStartDate = paramJSONObject.getString("display_start_date");
          }
          boolean bool11 = paramJSONObject.has("end_date");
          this.hasEndDate = bool11;
          if (bool11) {
            this.endDate = parseDate(paramJSONObject.getString("end_date"));
          }
          boolean bool12 = paramJSONObject.has("display_end_date");
          this.hasDisplayEndDate = bool12;
          if (bool12) {
            this.displayEndDate = paramJSONObject.getString("display_end_date");
          }
          boolean bool13 = paramJSONObject.has("upc");
          this.hasUpc = bool13;
          if (bool13) {
            this.upc = paramJSONObject.getString("upc");
          }
          boolean bool14 = paramJSONObject.has("latitude");
          this.hasLatitude = bool14;
          if (bool14) {
            this.latitude = paramJSONObject.getDouble("latitude");
          }
          boolean bool15 = paramJSONObject.has("longitude");
          this.hasLongitude = bool15;
          if (bool15) {
            this.longitude = paramJSONObject.getDouble("longitude");
          }
          boolean bool16 = paramJSONObject.has("organization");
          this.hasPlaceName = bool16;
          if (bool16) {
            this.placeName = paramJSONObject.getString("organization");
          }
          boolean bool17 = paramJSONObject.has("place_uuid");
          this.hasPlaceUUID = bool17;
          if (bool17) {
            this.placeUUID = paramJSONObject.getString("place_uuid");
          }
          boolean bool18 = paramJSONObject.has("destination_uuid");
          this.hasVenueUUID = bool18;
          if (bool18) {
            this.venueUUID = paramJSONObject.getString("destination_uuid");
          }
          return;
        }
        String str = (String)localIterator.next();
        this.ids.put(str, localJSONObject.getString(str));
      }
    }

    private static String parseDate(String paramString)
      throws ParseException
    {
      Date localDate = DATE_PARSER.parse(paramString);
      return DATE_FORMATTER.format(localDate);
    }

    private static void throwUnless(String paramString, boolean paramBoolean)
    {
      if (!paramBoolean) {
        throw new IllegalArgumentException("'" + paramString + "' does not exist in offers result");
      }
    }

    public String getBrand()
    {
      throwUnless("brand", this.hasBrand);
      return this.brand;
    }

    public String getCategory()
    {
      throwUnless("category", this.hasCategory);
      return this.category;
    }

    public Set<String> getDataSources()
    {
      return this.ids.keySet();
    }

    public String getDescription()
    {
      throwUnless("description", this.hasDescription);
      return this.description;
    }

    public String getDisplayEndDate()
    {
      throwUnless("display_end_date", this.hasDisplayEndDate);
      return this.displayEndDate;
    }

    public String getDisplayImage()
    {
      throwUnless("display_image", this.hasDisplayImage);
      return this.displayImage;
    }

    public String getDisplayStartDate()
    {
      throwUnless("display_start_date", this.hasDisplayStartDate);
      return this.displayStartDate;
    }

    public String getDistance()
    {
      throwUnless("type", this.hasDistance);
      return this.distance;
    }

    public String getEndDate()
    {
      throwUnless("end_date", this.hasEndDate);
      return this.endDate;
    }

    public String getId(String paramString)
    {
      return (String)this.ids.get(paramString);
    }

    public double getLatitude()
    {
      throwUnless("latitude", this.hasLatitude);
      return this.latitude;
    }

    public double getLongitude()
    {
      throwUnless("longitude", this.hasLongitude);
      return this.longitude;
    }

    public String getPlaceName()
    {
      throwUnless("organization", this.hasPlaceName);
      return this.placeName;
    }

    public String getPlaceUUID()
    {
      throwUnless("place_uuid", this.hasPlaceUUID);
      return this.placeUUID;
    }

    public String getStartDate()
    {
      throwUnless("start_date", this.hasStartDate);
      return this.startDate;
    }

    public String getThumbnailImage()
    {
      throwUnless("thumbnail_image", this.hasThumbnailImage);
      return this.thumbnailImage;
    }

    public String getTitle()
    {
      throwUnless("title", this.hasTitle);
      return this.title;
    }

    public String getType()
    {
      throwUnless("type", this.hasType);
      return this.type;
    }

    public String getUpc()
    {
      throwUnless("upc", this.hasUpc);
      return this.upc;
    }

    public String getVenueUUID()
    {
      throwUnless("destination_uuid", this.hasVenueUUID);
      return this.venueUUID;
    }

    public boolean hasBrand()
    {
      return this.hasBrand;
    }

    public boolean hasCategory()
    {
      return this.hasCategory;
    }

    public boolean hasDescription()
    {
      return this.hasDescription;
    }

    public boolean hasDisplayEndDate()
    {
      return this.hasDisplayEndDate;
    }

    public boolean hasDisplayStartDate()
    {
      return this.hasDisplayStartDate;
    }

    public boolean hasDistance()
    {
      return this.hasDistance;
    }

    public boolean hasEndDate()
    {
      return this.hasEndDate;
    }

    public boolean hasLatLong()
    {
      return (this.hasLatitude) && (this.hasLongitude);
    }

    public boolean hasPlaceUUID()
    {
      return this.hasPlaceUUID;
    }

    public boolean hasStartDate()
    {
      return this.hasStartDate;
    }

    public boolean hasThumbnailImage()
    {
      return this.hasThumbnailImage;
    }

    public boolean hasType()
    {
      return this.hasType;
    }

    public boolean hasUPC()
    {
      return this.hasUpc;
    }

    public boolean hasVenueUUID()
    {
      return this.hasVenueUUID;
    }
  }

  public static class DealsResult
  {
    public final ArrayList<DealsClient.Deal> deals = new ArrayList();
    public final int status;

    private DealsResult(JSONObject paramJSONObject)
      throws JSONException, ParseException
    {
      this.status = paramJSONObject.getInt("status_code");
      JSONArray localJSONArray = paramJSONObject.getJSONArray("offers");
      int i = localJSONArray.length();
      for (int j = 0;j<i; j++)
      {
        this.deals.add(new DealsClient.Deal(localJSONArray.getJSONObject(j)));
      }
    }

    public static DealsResult fromJSON(JSONObject paramJSONObject)
      throws JSONException, ParseException
    {
      return new DealsResult(paramJSONObject);
    }
  }

  public static class DestinationDealsRequest
    extends PIWebServices.CommonRequestObject
  {
    public String venue;

    protected void onApply(Uri.Builder paramBuilder) {}
  }

  public static class NearbyDealsRequest
    extends PIWebServices.CommonRequestObject
  {
    public double latitude;
    public double longitude;
    public int radius = 0;

    protected void onApply(Uri.Builder paramBuilder)
    {
      paramBuilder.appendQueryParameter("lat", String.valueOf(this.latitude)).appendQueryParameter("long", String.valueOf(this.longitude));
      if (this.radius > 0) {
        paramBuilder.appendQueryParameter("radius", String.valueOf(this.radius));
      }
      if (this.maxResults > 0) {
        paramBuilder.appendQueryParameter("max_results", String.valueOf(this.maxResults));
      }
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.net.DealsClient
 * JD-Core Version:    0.7.0.1
 */