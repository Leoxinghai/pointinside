package com.pointinside.android.api.maps;

import android.graphics.Point;

class PIMapUtil
{
  static final float DEG2RAD = 0.01745329F;
  static final float FOOT_IN_METER = 3.28084F;
  static final float METERS_IN_FOOT = 0.3048F;
  static final float PI = 3.141593F;
  static final float PI_2 = 1.570796F;
  static final float PI_4 = 0.7853982F;
  static final float RAD2DEG = 57.29578F;

  public static int getNextSquareNumberAbove(float paramFloat)
  {
    int i = 0;
    int j = 1;
    for (int k = 1;; k++)
    {
      if (j > paramFloat) {
        return i;
      }
      i = k;
      j *= 2;
    }
  }

  public static boolean isPointInPolygon(int x, int y, IPoint apoint[]) {
	    int i;
	    int j;
	    boolean result = false;
	    for (i = 0, j = apoint.length - 1; i < apoint.length; j = i++) {
	      if ((apoint[i].getY() > y) != (apoint[j].getY() > y) &&
	          (x < (apoint[j].getX() - apoint[i].getX()) * (y - apoint[i].getY()) / (apoint[j].getY()-apoint[i].getY()) + apoint[i].getX())) {
	        result = !result;
	       }
	    }
	    return result;
	  }

  public static int mod(int paramInt1, int paramInt2)
  {
    if (paramInt1 > 0) {
      return paramInt1 % paramInt2;
    }
    while (paramInt1 < 0) {
      paramInt1 += paramInt2;
    }
    return paramInt1;
  }

  public static Point projectPIMapLocation(PIMapLocation paramPIMapLocation, int paramInt, Point paramPoint)
  {
    if (paramPoint != null) {}
    for (Point localPoint = paramPoint;; localPoint = new Point())
    {
      localPoint.x = paramPIMapLocation.getPixelX();
      localPoint.y = paramPIMapLocation.getPixelY();
      return localPoint;
    }
  }

  public static double tile2lat(int paramInt1, int paramInt2)
  {
    double d = 3.141592653589793D - 6.283185307179586D * paramInt1 / (1 << paramInt2);
    return 57.295779513082323D * Math.atan(0.5D * (Math.exp(d) - Math.exp(-d)));
  }

  public static double tile2lon(int paramInt1, int paramInt2)
  {
    return 360.0D * (paramInt1 / (1 << paramInt2)) - 180.0D;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIMapUtil
 * JD-Core Version:    0.7.0.1
 */