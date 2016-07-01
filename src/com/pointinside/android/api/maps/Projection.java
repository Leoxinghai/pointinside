package com.pointinside.android.api.maps;

import android.graphics.PointF;

public abstract interface Projection
{
  public abstract PIMapLocation fromCoordinates(double paramDouble1, double paramDouble2);

  public abstract PIMapLocation fromPixels(int paramInt1, int paramInt2);

  public abstract PointF getViewPoint(float paramFloat1, float paramFloat2, PointF paramPointF);
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.Projection
 * JD-Core Version:    0.7.0.1
 */