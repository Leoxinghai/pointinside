package com.pointinside.android.api.maps;

import android.graphics.Point;

class PIPoint
  extends Point
  implements IPoint
{
  PIPoint() {}

  PIPoint(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }

  PIPoint(Point paramPoint)
  {
    super(paramPoint);
  }

  public int getX()
  {
    return this.x;
  }

  public int getY()
  {
    return this.y;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.maps.PIPoint
 * JD-Core Version:    0.7.0.1
 */