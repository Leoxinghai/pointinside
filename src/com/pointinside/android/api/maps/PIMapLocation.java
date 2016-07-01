// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.pointinside.android.api.maps;

import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import java.util.LinkedHashMap;

// Referenced classes of package com.pointinside.android.api.maps:
//            PIPoint, PIMapVenueZone, PIMapView

public class PIMapLocation
{

    public PIMapLocation(double d, double d1)
    {
        this(d, d1, 0, 0);
    }

    public PIMapLocation(double d, double d1, int i, int j)
    {
        mLatitude = d;
        mLongitude = d1;
        mTranslatedPixelX = i;
        mPixelX = i;
        mTranslatedPixelY = j;
        mPixelY = j;
    }

    public PIMapLocation(int i, int j)
    {
        this(0.0D, 0.0D, i, j);
    }

    protected static double calculateAngleOffAxisFromDeltaXYFeet()
    {
        double d = Math.toDegrees(Math.atan(sMinDistanceYFeet / sMinDistanceXFeet));
        double d1 = 0.0D;
        if(sMinDistanceXFeet >= 0.0D && sMinDistanceYFeet >= 0.0D || sMinDistanceXFeet >= 0.0D && sMinDistanceYFeet < 0.0D)
            d1 = 90D + d;
        else
        if(sMinDistanceXFeet < 0.0D && sMinDistanceYFeet >= 0.0D || sMinDistanceXFeet < 0.0D && sMinDistanceYFeet < 0.0D)
            return -(-90D - d);
        return d1;
    }

    protected static double calculateAngleOffYAxis(double d, double d1)
    {
        double d2 = 0.0D;
        double d3 = Math.toDegrees(Math.atan(d1 / d));
        if(d >= 0.0D && d1 >= 0.0D)
        {
            d2 = 90D + d3;
        } else
        {
            if(d < 0.0D && d1 >= 0.0D)
                return -(90D - d3);
            if(d < 0.0D && d1 < 0.0D)
                return -(90D - d3);
            if(d >= 0.0D && d1 < 0.0D)
                return 90D + d3;
        }
        return d2;
    }

    protected static double calculateDistance(double d, double d1, double d2, double d3)
    {
        double d4 = Math.toRadians(d);
        double d5 = Math.toRadians(d1);
        double d6 = Math.toRadians(d2);
        double d7 = Math.toRadians(d3) - d5;
        double d8 = Math.atan(0.99664718933525254D * Math.tan(d4));
        double d9 = Math.atan(0.99664718933525254D * Math.tan(d6));
        double d10 = Math.sin(d8);
        double d11 = Math.cos(d8);
        double d12 = Math.sin(d9);
        double d13 = Math.cos(d9);
        double d14 = d7;
        double d15 = 6.2831853071795862D;
        int i = 20;
        double d16 = 0.0D;
        double d17 = 0.0D;
        double d18 = 0.0D;
        double d19 = 0.0D;
        double d20 = 0.0D;
        double d21 = 0.0D;
        double d22 = 0.0D;
        do
        {
            if(Math.abs(d14 - d15) <= 9.9999999999999998E-013D || i <= 1)
            {
                double d23 = (272331606681.94531D * d16) / 40408299984087.055D;
                double d24 = 1.0D + (d23 / 16384D) * (4096D + d23 * (-768D + d23 * (320D - 175D * d23)));
                double d25 = (d23 / 1024D) * (256D + d23 * (-128D + d23 * (74D - 47D * d23)));
                double d26 = d25 * d17 * (d18 + (d25 / 4D) * (d19 * (-1D + d18 * (2D * d18)) - d18 * (d25 / 6D) * (-3D + d17 * (4D * d17)) * (-3D + d18 * (4D * d18))));
                double d27 = 6356752.3141999999D * d24 * (d20 - d26);
                sInitialBearingDeg = Math.toDegrees(Math.atan2(d13 * d21, d11 * d12 - d22 * (d10 * d13)));
                return 3.2808399000000001D * d27;
            }
            i--;
            d21 = Math.sin(d14);
            d22 = Math.cos(d14);
            d17 = Math.sqrt(d13 * d21 * (d13 * d21) + (d11 * d12 - d22 * (d10 * d13)) * (d11 * d12 - d22 * (d10 * d13)));
            d19 = d10 * d12 + d22 * (d11 * d13);
            d20 = Math.atan2(d17, d19);
            double d28 = Math.asin((d21 * (d11 * d13)) / d17);
            d16 = Math.cos(d28) * Math.cos(d28);
            d18 = d19 - (d12 * (2D * d10)) / d16;
            double d29 = 0.00020955066654671753D * d16 * (4D + 0.0033528106647474805D * (4D - 3D * d16));
            d15 = d14;
            d14 = d7 + 0.0033528106647474805D * (1.0D - d29) * Math.sin(d28) * (d20 + d29 * d17 * (d18 + d29 * d19 * (-1D + d18 * (2D * d18))));
        } while(true);
    }

    public static PIMapLocation getLatLonDestFromStartBearingRange(double d, double d1, double d2, double d3)
    {
        double d4 = d2 * 0.304799999536704D;
        if(Math.abs(d2) < 1.0D)
            return new PIMapLocation(d, d1, 0, 0);
        double d5 = Math.toRadians(d);
        double d6 = Math.toRadians(d1);
        double d7 = Math.toRadians(d3);
        double d8 = Math.sin(d7);
        double d9 = Math.cos(d7);
        double d10 = 0.99664718933525254D * Math.tan(d5);
        double d11 = 1.0D / Math.sqrt(1.0D + d10 * d10);
        double d12 = d10 * d11;
        double d13 = Math.atan2(d10, d9);
        double d14 = d11 * d8;
        double d15 = 1.0D - d14 * d14;
        double d16 = (272331606681.94531D * d15) / 40408299984087.055D;
        double d17 = 1.0D + (d16 / 16384D) * (4096D + d16 * (-768D + d16 * (320D - 175D * d16)));
        double d18 = (d16 / 1024D) * (256D + d16 * (-128D + d16 * (74D - 47D * d16)));
        double d19 = d4 / (6356752.3141999999D * d17);
        double d20 = 6.2831853071795862D;
        double d21 = 0.0D;
        double d22 = 0.0D;
        double d23 = 0.0D;
        do
        {
            if(Math.abs(d19 - d20) <= 9.9999999999999998E-013D)
            {
                double d25 = d12 * d21 - d9 * (d11 * d22);
                double d26 = Math.atan2(d12 * d22 + d9 * (d11 * d21), 0.99664718933525254D * Math.sqrt(d14 * d14 + d25 * d25));
                double d27 = Math.atan2(d21 * d8, d11 * d22 - d9 * (d12 * d21));
                double d28 = 0.00020955066654671753D * d15 * (4D + 0.0033528106647474805D * (4D - 3D * d15));
                double d29 = d27 - d14 * (0.0033528106647474805D * (1.0D - d28)) * (d19 + d28 * d21 * (d23 + d28 * d22 * (-1D + d23 * (2D * d23))));
                Math.atan2(d14, -d25);
                return new PIMapLocation(Math.toDegrees(d26), Math.toDegrees(d6 + d29), 0, 0);
            }
            d23 = Math.cos(d19 + 2D * d13);
            d21 = Math.sin(d19);
            d22 = Math.cos(d19);
            double d24 = d18 * d21 * (d23 + (d18 / 4D) * (d22 * (-1D + d23 * (2D * d23)) - d23 * (d18 / 6D) * (-3D + d21 * (4D * d21)) * (-3D + d23 * (4D * d23))));
            d20 = d19;
            d19 = d24 + d4 / (6356752.3141999999D * d17);
        } while(true);
    }

    public static PIMapLocation getLatLonOfXY(PIMapVenueZone pimapvenuezone, int i, int j)
    {
        int k = i - pimapvenuezone.getPoint1PixelX();
        int l = j - pimapvenuezone.getPoint1PixelY();
        double d = (double)k * pimapvenuezone.getFeetPerPixelX();
        double d1 = (double)l * pimapvenuezone.getFeetPerPixelY();
        double d2 = Math.sqrt(d * d + d1 * d1);
        double d3 = d;
        double d4 = d1;
        double d5;
        double d6;
        double d7;
        int i1 = 1;
        int j1 = 2;
        do
        {
            if(j1 > 4)
            {
                calculateDistance(pimapvenuezone.getPoint2Latitude(), pimapvenuezone.getPoint2Longitude(), pimapvenuezone.getPoint1Latitude(), pimapvenuezone.getPoint1Longitude());
                double d8 = sInitialBearingDeg;
                Math.toRadians(d8);
                double d9 = d8 + calculateAngleOffYAxis(d3, d4);
                double d10;
                double d11;
                if(i1 == 1)
                {
                    d11 = pimapvenuezone.getPoint1Latitude();
                    d10 = pimapvenuezone.getPoint1Longitude();
                } else
                if(i1 == 2)
                {
                    d11 = pimapvenuezone.getPoint2Latitude();
                    d10 = pimapvenuezone.getPoint2Longitude();
                } else
                if(i1 == 3)
                {
                    d11 = pimapvenuezone.getPoint3Latitude();
                    d10 = pimapvenuezone.getPoint3Longitude();
                } else
                if(i1 == 4)
                {
                    d11 = pimapvenuezone.getPoint4Latitude();
                    d10 = pimapvenuezone.getPoint4Longitude();
                } else
                {
                    Log.v("PIMaps", "Failed to get a proper point number");
                    d10 = 0.0D;
                    d11 = 0.0D;
                }
                return getLatLonDestFromStartBearingRange(d11, d10, d2, d9);
            }
            if(j1 == 2)
            {
                k = i - pimapvenuezone.getPoint2PixelX();
                l = j - pimapvenuezone.getPoint2PixelY();
            } else
            if(j1 == 3)
            {
                k = i - pimapvenuezone.getPoint3PixelX();
                l = j - pimapvenuezone.getPoint3PixelY();
            } else
            if(j1 == 4)
            {
                k = i - pimapvenuezone.getPoint4PixelX();
                l = j - pimapvenuezone.getPoint4PixelY();
            } else
            {
                Log.e("PIMaps", "Error with pixel number");
            }
            d5 = (double)k * pimapvenuezone.getFeetPerPixelX();
            d6 = (double)l * pimapvenuezone.getFeetPerPixelY();
            d7 = Math.sqrt(d5 * d5 + d6 * d6);
            if(d7 < d2)
            {
                d2 = d7;
                i1 = j1;
                d3 = d5;
                d4 = d6;
            }
            j1++;
        } while(true);
    }

    public static PIMapLocation getXYOfLatLon(PIMapVenueZone pimapvenuezone, double d, double d1)
    {
        double d2 = 0.0D;
        double d3 = calculateDistance(pimapvenuezone.getPoint1Latitude(), pimapvenuezone.getPoint1Longitude(), d, d1);
        int i = 1;
        double d4 = sInitialBearingDeg;
        int j = 2;
        do
        {
            if(j > 4)
            {
                calculateDistance(pimapvenuezone.getPoint2Latitude(), pimapvenuezone.getPoint2Longitude(), pimapvenuezone.getPoint1Latitude(), pimapvenuezone.getPoint1Longitude());
                double d5 = sInitialBearingDeg;
                Math.toRadians(d5);
                double d6 = d4 - d5;
                if(d6 > 180D)
                    d6 -= 360D;
                if(d6 < -180D)
                    d6 += 360D;
                int k;
                int l;
                int i1;
                int j1;
                if(d6 > 0.0D && d6 < 90D)
                {
                    double d10 = Math.toRadians(d6);
                    k = (int)((d3 * Math.sin(d10)) / pimapvenuezone.getFeetPerPixelX());
                    l = (int)((d3 * Math.cos(d10)) / pimapvenuezone.getFeetPerPixelY());
                } else
                if(d6 < 0.0D && d6 > -90D)
                {
                    double d9 = Math.toRadians(d6);
                    k = (int)((d3 * Math.sin(d9)) / pimapvenuezone.getFeetPerPixelX());
                    l = (int)((d3 * Math.cos(d9)) / pimapvenuezone.getFeetPerPixelY());
                } else
                if(d6 < -90D && d6 > -180D)
                {
                    double d8 = Math.toRadians(-d6 - 90D);
                    k = -(int)((d3 * Math.cos(d8)) / pimapvenuezone.getFeetPerPixelX());
                    l = -(int)((d3 * Math.sin(d8)) / pimapvenuezone.getFeetPerPixelY());
                } else
                if(d6 > 90D && d6 < 180D)
                {
                    double d7 = Math.toRadians(d6 - 90D);
                    k = (int)((d3 * Math.cos(d7)) / pimapvenuezone.getFeetPerPixelX());
                    l = -(int)((d3 * Math.sin(d7)) / pimapvenuezone.getFeetPerPixelY());
                } else
                if(d6 == 0.0D)
                {
                    l = (int)(d3 / pimapvenuezone.getFeetPerPixelY());
                    k = 0;
                } else
                if(d6 == 90D)
                {
                    k = (int)(d3 / pimapvenuezone.getFeetPerPixelX());
                    l = 0;
                } else
                if(d6 == -90D)
                {
                    k = -(int)(d3 / pimapvenuezone.getFeetPerPixelX());
                    l = 0;
                } else
                if(d6 == 180D)
                {
                    l = -(int)(d3 / pimapvenuezone.getFeetPerPixelY());
                    k = 0;
                } else
                {
                    Log.v("PIMaps", "Could not get the correct angle");
                    k = 0;
                    l = 0;
                }
                if(i == 1)
                {
                    i1 = pimapvenuezone.getPoint1PixelX();
                    j1 = pimapvenuezone.getPoint1PixelY();
                } else
                if(i == 2)
                {
                    i1 = pimapvenuezone.getPoint2PixelX();
                    j1 = pimapvenuezone.getPoint2PixelY();
                } else
                if(i == 3)
                {
                    i1 = pimapvenuezone.getPoint3PixelX();
                    j1 = pimapvenuezone.getPoint3PixelY();
                } else
                if(i == 4)
                {
                    i1 = pimapvenuezone.getPoint4PixelX();
                    j1 = pimapvenuezone.getPoint4PixelY();
                } else
                {
                    Log.v("PIMaps", "Didn't get a valid point");
                    i1 = 0;
                    j1 = 0;
                }
                return new PIMapLocation(d, d1, i1 + k, j1 - l);
            }
            if(j == 2)
                d2 = calculateDistance(pimapvenuezone.getPoint2Latitude(), pimapvenuezone.getPoint2Longitude(), d, d1);
            else
            if(j == 3)
                d2 = calculateDistance(pimapvenuezone.getPoint3Latitude(), pimapvenuezone.getPoint3Longitude(), d, d1);
            else
            if(j == 4)
                d2 = calculateDistance(pimapvenuezone.getPoint4Latitude(), pimapvenuezone.getPoint4Longitude(), d, d1);
            else
                Log.v("PIMaps", "getXYOfLatLon() failed to get valid point");
            if(d2 < d3)
            {
                d3 = d2;
                i = j;
                d4 = sInitialBearingDeg;
            }
            j++;
        } while(true);
    }

    public static boolean isLocationInZone(PIMapVenueZone pimapvenuezone, double d, double d1)
    {
        PIMapLocation pimaplocation = getXYOfLatLon(pimapvenuezone, d, d1);
        int i = pimaplocation.getPixelX();
        int j = pimaplocation.getPixelY();
        return (new Rect(0, 0, pimapvenuezone.getImageSizeX(), pimapvenuezone.getImageSizeY())).intersects(i, j, i, j);
    }

    public int distanceTo(PIMapLocation pimaplocation)
    {
        return 0;
    }

    public boolean equals(Object obj)
    {
        PIMapLocation pimaplocation;
        if(obj instanceof PIMapLocation)
            if((pimaplocation = (PIMapLocation)obj).mPixelX == mPixelX && pimaplocation.mPixelY == mPixelY)
                return true;
        return false;
    }

    public Bundle getExtras()
    {
        if(mExtras == null)
            mExtras = new Bundle();
        return mExtras;
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }

    public int getPixelX()
    {
        return mPixelX;
    }

    public int getPixelY()
    {
        return mPixelY;
    }

    public int getTranslatedPixelX()
    {
        return mTranslatedPixelX;
    }

    public int getTranslatedPixelY()
    {
        return mTranslatedPixelY;
    }

    public boolean isContainedInVenueZone()
    {
        return false;
    }

    public void setExtras(Bundle bundle)
    {
        mExtras = new Bundle(bundle);
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append((new StringBuilder("PIMapLocation { latitude = ")).append(mLatitude).append(", longitude = ").append(mLongitude).append(", pixelX = ").append(mPixelX).append(", pixelY = ").append(mPixelY).append(", translatedPixelX = ").append(mTranslatedPixelX).append(", translatedPixelY = ").append(mTranslatedPixelY).append(" }").toString());
        return stringbuffer.toString();
    }

    void translate(PIMapView pimapview)
    {
        translate(pimapview, -1);
    }

    void translate(PIMapView pimapview, int i)
    {
        if(i == -1)
            i = pimapview.getCurrentZoneIndex();
        PIMapVenueZone pimapvenuezone = (PIMapVenueZone)pimapview.mVenueZones.get(Integer.valueOf(i));
        if(pimapvenuezone != null && pimapview.isOverview())
        {
            PIMapLocation pimaplocation = getLatLonOfXY(pimapvenuezone, mPixelX, mPixelY);
            mLatitude = pimaplocation.mLatitude;
            mLongitude = pimaplocation.mLongitude;
            PIMapLocation pimaplocation1 = getXYOfLatLon(pimapview.mOverviewZone, mLatitude, mLongitude);
            PointF pointf1 = pimapview.computePointforScale(pimaplocation1.mPixelX, pimaplocation1.mPixelY);
            mTranslatedPixelX = Math.round(pointf1.x);
            mTranslatedPixelY = Math.round(pointf1.y);
            return;
        } else
        {
            PointF pointf = pimapview.computePointforScale(mPixelX, mPixelY);
            mTranslatedPixelX = Math.round(pointf.x);
            mTranslatedPixelY = Math.round(pointf.y);
            return;
        }
    }

    static final double FEET_TO_METERS = 0.304799999536704D;
    static final double FLATTENING = 0.0033528106647474805D;
    static final double MAJOR_AXIS = 6378137D;
    static final double METERS_TO_FEET = 3.2808399000000001D;
    static final double MINOR_AXIS = 6356752.3141999999D;
    private static final PIPoint PI_POINT[];
    private static double sDistance;
    private static double sInitialBearingDeg;
    private static double sMinDistanceXFeet;
    private static double sMinDistanceYFeet;
    private Bundle mExtras;
    private double mLatitude;
    private double mLongitude;
    private final int mPixelX;
    private final int mPixelY;
    private int mTranslatedPixelX;
    private int mTranslatedPixelY;

    static 
    {
        PIPoint apipoint[] = new PIPoint[4];
        apipoint[0] = new PIPoint();
        apipoint[1] = new PIPoint();
        apipoint[2] = new PIPoint();
        apipoint[3] = new PIPoint();
        PI_POINT = apipoint;
    }
}
