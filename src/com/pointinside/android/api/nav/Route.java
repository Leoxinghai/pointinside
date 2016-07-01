package com.pointinside.android.api.nav;

import android.location.Location;
import android.net.Uri;
import com.pointinside.android.api.net.JSONWebRequester;
import com.pointinside.android.api.net.JSONWebRequester.RestResponseException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Route
{
  private float mDistance;
  private RouteEndpoint mEnd;
  private boolean mIsDistanceCalculated;
  private ArrayList<RoutePoint> mPoints;
  private RouteEndpoint mStart;
  private String mVenueUUID;
  private int mWalkingTimeInMinutes;

  private static void addEndpointToRequest(JSONObject paramJSONObject, String paramString, RouteEndpoint paramRouteEndpoint)
    throws JSONException
  {
    if (paramRouteEndpoint.placeUUID != null)
    {
      paramJSONObject.put(paramString, paramRouteEndpoint.placeUUID);
      return;
    }
    if (paramRouteEndpoint.point != null)
    {
      paramJSONObject.put(paramString, "");
      JSONObject localJSONObject = new JSONObject();
      RoutePoint localRoutePoint = paramRouteEndpoint.point;
      localJSONObject.put("latitude", String.valueOf(localRoutePoint.latitude));
      localJSONObject.put("longitude", String.valueOf(localRoutePoint.longitude));
      localJSONObject.put("zoneUuid", localRoutePoint.zoneUUID);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(Character.toUpperCase(paramString.charAt(0)));
      localStringBuilder.append(paramString.substring(1));
      localStringBuilder.append("ArbitraryPoint");
      paramJSONObject.put(localStringBuilder.toString(), localJSONObject);
      return;
    }
    throw new IllegalArgumentException("Invalid endpoint for " + paramString);
  }

  public static Route calculate(String paramString, RouteEndpoint paramRouteEndpoint1, RouteEndpoint paramRouteEndpoint2)
    throws JSONWebRequester.RestResponseException
  {
    HttpPost localHttpPost = getRouteRequest(paramString, paramRouteEndpoint1, paramRouteEndpoint2);
    JSONObject localJSONObject1 = RouteAPI.getWebRequester().execute(localHttpPost);
    if ((localJSONObject1 == null) || (localJSONObject1.length() == 0)) {
      throw new NoRouteException("No route provided by server");
    }
    Route localRoute = new Route();
    localRoute.mVenueUUID = paramString;
    JSONObject localJSONObject2;
    JSONArray localJSONArray;
    int i;
    try
    {
      localJSONObject2 = localJSONObject1.getJSONArray("navigation").getJSONObject(0).getJSONArray("subroutes").getJSONArray(0).getJSONObject(0);
      localJSONArray = localJSONObject2.getJSONArray("points").getJSONArray(0);
      i = localJSONArray.length();
      if (i == 0) {
        throw new NoRouteException("Empty route provided by server");
      }
      ArrayList localArrayList = new ArrayList();
      for (int j = 0;; j++)
      {
        if (j >= i)
        {
          localRoute.mStart = RouteEndpoint.fromRoutePoint((RoutePoint)localArrayList.get(0), paramRouteEndpoint1.placeUUID);
          localRoute.mEnd = RouteEndpoint.fromRoutePoint((RoutePoint)localArrayList.get(i - 1), paramRouteEndpoint2.placeUUID);
          localRoute.mPoints = localArrayList;
          localRoute.mWalkingTimeInMinutes = localJSONObject2.getJSONArray("time").getInt(0);
          return localRoute;
        }
        localArrayList.add(new RoutePoint(localJSONArray.getJSONObject(j)));
      }
    }
    catch (JSONException localJSONException)
    {
      throw new JSONWebRequester.RestResponseException(localJSONException);
    }
  }

  public static Route calculate(String paramString1, String paramString2, String paramString3)
    throws JSONWebRequester.RestResponseException
  {
    return calculate(paramString1, RouteEndpoint.fromPlaceUUID(paramString2), RouteEndpoint.fromPlaceUUID(paramString3));
  }

  private float calculateDistance()
  {
    int i = getPointsCount();
    float f = 0.0F;
    RoutePoint localObject = null;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return f;
      }
      RoutePoint localRoutePoint = getPoint(j);
      if (localObject != null)
      {
        float[] arrayOfFloat = new float[1];
        Location.distanceBetween(localObject.getLatitude(), localObject.getLongitude(), localRoutePoint.getLatitude(), localRoutePoint.getLongitude(), arrayOfFloat);
        f += arrayOfFloat[0];
      }
      localObject = localRoutePoint;
    }
  }

  private static HttpPost getRouteRequest(String paramString, RouteEndpoint paramRouteEndpoint1, RouteEndpoint paramRouteEndpoint2)
  {
    try
    {
      HttpPost localHttpPost = new HttpPost(RouteAPI.getMethodUri("jsonroute").toString());
      JSONObject localJSONObject1 = new JSONObject();
      JSONObject localJSONObject2 = new JSONObject();
      localJSONObject2.put("venue", paramString);
      addEndpointToRequest(localJSONObject2, "start", paramRouteEndpoint1);
      addEndpointToRequest(localJSONObject2, "end", paramRouteEndpoint2);
      localJSONObject1.put("navigation", localJSONObject2);
      StringEntity localStringEntity = new StringEntity(localJSONObject1.toString());
      localStringEntity.setContentType("application/json");
      localHttpPost.setEntity(localStringEntity);
      return localHttpPost;
    }
    catch (JSONException localJSONException)
    {
      throw new AssertionError();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError();
    }
  }

  public float getDistance()
  {
    if (!this.mIsDistanceCalculated)
    {
      this.mDistance = calculateDistance();
      this.mIsDistanceCalculated = true;
    }
    return this.mDistance;
  }

  public RouteEndpoint getEnd()
  {
    return this.mEnd;
  }

  public String getEndPlaceUUID()
  {
    return this.mEnd.placeUUID;
  }

  public RoutePoint getPoint(int paramInt)
  {
    return (RoutePoint)this.mPoints.get(paramInt);
  }

  public int getPointsCount()
  {
    return this.mPoints.size();
  }

  public RouteEndpoint getStart()
  {
    return this.mStart;
  }

  public String getStartPlaceUUID()
  {
    return this.mStart.placeUUID;
  }

  public String getVenueUUID()
  {
    return this.mVenueUUID;
  }

  public int getWalkingTimeInMinutes()
  {
    return this.mWalkingTimeInMinutes;
  }

  public static class NoRouteException
    extends JSONWebRequester.RestResponseException
  {
    public NoRouteException(String paramString)
    {
      super(paramString);
    }
  }

  public static class RouteEndpoint
  {
    public final String placeUUID;
    public final Route.RoutePoint point;

    private RouteEndpoint(Route.RoutePoint paramRoutePoint, String paramString)
    {
      this.point = paramRoutePoint;
      this.placeUUID = paramString;
    }

    public static RouteEndpoint fromArbitraryLocation(String paramString, double paramDouble1, double paramDouble2)
    {
      return new RouteEndpoint(new Route.RoutePoint(paramDouble1, paramDouble2, null, 0, 0, paramString), null);
    }

    public static RouteEndpoint fromPlaceUUID(String paramString)
    {
      return new RouteEndpoint(null, paramString);
    }

    private static RouteEndpoint fromRoutePoint(Route.RoutePoint paramRoutePoint, String paramString)
    {
      return new RouteEndpoint(paramRoutePoint, paramString);
    }

    public String toString()
    {
      if (this.placeUUID != null) {
        return this.placeUUID;
      }
      if (this.point != null) {
        return this.point.toString();
      }
      return "{}";
    }
  }

  public static class RoutePoint
  {
    private final double latitude;
    private final double longitude;
    private final String name;
    private final int pixelX;
    private final int pixelY;
    private final String zoneUUID;

    private RoutePoint(double paramDouble1, double paramDouble2, String paramString1, int paramInt1, int paramInt2, String paramString2)
    {
      this.latitude = paramDouble1;
      this.longitude = paramDouble2;
      this.name = paramString1;
      this.pixelX = paramInt1;
      this.pixelY = paramInt2;
      this.zoneUUID = paramString2;
    }

    private RoutePoint(RoutePoint paramRoutePoint)
    {
      this(paramRoutePoint.latitude, paramRoutePoint.longitude, paramRoutePoint.name, paramRoutePoint.pixelX, paramRoutePoint.pixelY, paramRoutePoint.zoneUUID);
    }

    private RoutePoint(JSONObject paramJSONObject)
      throws JSONException
    {
      this.latitude = paramJSONObject.getJSONArray("lat").getDouble(0);
      this.longitude = paramJSONObject.getJSONArray("lon").getDouble(0);
      this.name = paramJSONObject.getJSONArray("name").getString(0);
      this.pixelX = paramJSONObject.getJSONArray("pixelX").getInt(0);
      this.pixelY = paramJSONObject.getJSONArray("pixelY").getInt(0);
      this.zoneUUID = paramJSONObject.getJSONArray("zoneUuid").getString(0);
    }

    public double getLatitude()
    {
      return this.latitude;
    }

    public double getLongitude()
    {
      return this.longitude;
    }

    public String getName()
    {
      return this.name;
    }

    public int getPixelX()
    {
      return this.pixelX;
    }

    public int getPixelY()
    {
      return this.pixelY;
    }

    public String getZoneUUID()
    {
      return this.zoneUUID;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append('{');
      localStringBuilder.append("latitude=").append(this.latitude).append(';');
      localStringBuilder.append("longitude=").append(this.longitude).append(';');
      localStringBuilder.append("name=").append(this.name).append(';');
      localStringBuilder.append("pixelX=").append(this.pixelX).append(';');
      localStringBuilder.append("pixelY=").append(this.pixelY).append(';');
      localStringBuilder.append("zoneUUID=").append(this.zoneUUID);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.nav.Route
 * JD-Core Version:    0.7.0.1
 */