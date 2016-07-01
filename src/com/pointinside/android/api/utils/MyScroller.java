package com.pointinside.android.api.utils;

import android.content.Context;
import android.widget.Scroller;

public class MyScroller
{
  private final Scroller mScrollerX;
  private final Scroller mScrollerY;

  public MyScroller(Context paramContext)
  {
    this.mScrollerX = new Scroller(paramContext);
    this.mScrollerY = new Scroller(paramContext);
  }

  private static int clamp(int paramInt1, int paramInt2, int paramInt3)
  {
    return Math.max(paramInt2, Math.min(paramInt3, paramInt1));
  }

  private static boolean needsClamp(int paramInt1, int paramInt2, int paramInt3)
  {
    return (paramInt1 < paramInt2) || (paramInt1 > paramInt3);
  }

  private boolean springBackX(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = needsClamp(paramInt1, paramInt2, paramInt3);
    boolean bool2 = false;
    if (bool1)
    {
      this.mScrollerX.startScroll(paramInt1, 0, -(paramInt1 - clamp(paramInt1, paramInt2, paramInt3)), 0);
      bool2 = true;
    }
    return bool2;
  }

  private boolean springBackY(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = needsClamp(paramInt1, paramInt2, paramInt3);
    boolean bool2 = false;
    if (bool1)
    {
      this.mScrollerY.startScroll(0, paramInt1, 0, -(paramInt1 - clamp(paramInt1, paramInt2, paramInt3)));
      bool2 = true;
    }
    return bool2;
  }

  public void abortAnimation()
  {
    this.mScrollerX.abortAnimation();
    this.mScrollerY.abortAnimation();
  }

  public boolean computeScrollOffset()
  {
    return false | this.mScrollerX.computeScrollOffset() | this.mScrollerY.computeScrollOffset();
  }

  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if (!springBackX(paramInt1, paramInt5, paramInt6)) {
      this.mScrollerX.fling(paramInt1, 0, paramInt3, paramInt4, paramInt5, paramInt6, 0, 0);
    }
    if (!springBackY(paramInt2, paramInt7, paramInt8)) {
      this.mScrollerY.fling(0, paramInt2, paramInt3, paramInt4, 0, 0, paramInt7, paramInt8);
    }
  }

  public final void forceFinished(boolean paramBoolean)
  {
    this.mScrollerX.forceFinished(paramBoolean);
    this.mScrollerY.forceFinished(paramBoolean);
  }

  public final int getCurrX()
  {
    return this.mScrollerX.getCurrX();
  }

  public final int getCurrY()
  {
    return this.mScrollerY.getCurrY();
  }

  public final int getDuration()
  {
    return Math.max(this.mScrollerX.getDuration(), this.mScrollerY.getDuration());
  }

  public final int getFinalX()
  {
    return this.mScrollerX.getFinalX();
  }

  public final int getFinalY()
  {
    return this.mScrollerY.getFinalY();
  }

  public final int getStartX()
  {
    return this.mScrollerX.getStartX();
  }

  public final int getStartY()
  {
    return this.mScrollerY.getStartY();
  }

  public final boolean isFinished()
  {
    int i;
    int j;
    if (this.mScrollerX.isFinished())
    {
      i = 0;
      j = 0x0 | i;
      if (!this.mScrollerY.isFinished()) {
    	    for (int k = 0;; k = 1)
    	    {
    	      if ((j | k) == 0) {
    	    	    return true;
    	      }
    	      return false;
    	    }
      }
    }
    i = 1;
    return true;
  }

  public boolean springBack(int i, int j, int k, int l, int i1, int j1)
  {
      boolean flag = springBackX(i, k, l);
      boolean flag1 = springBackY(j, i1, j1);
      boolean flag2;
      if(!flag && !flag1)
          flag2 = false;
      else
          flag2 = true;
      if(flag2)
          if(!flag)
              mScrollerX.startScroll(i, 0, 0, 0, 1);
          else
          if(!flag1)
          {
              mScrollerY.startScroll(0, j, 0, 0, 1);
              return flag2;
          }
      return flag2;
  }

  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mScrollerX.startScroll(paramInt1, 0, paramInt3, 0);
    this.mScrollerY.startScroll(0, paramInt2, 0, paramInt4);
  }

  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.mScrollerX.startScroll(paramInt1, 0, paramInt3, 0, paramInt5);
    this.mScrollerY.startScroll(0, paramInt2, 0, paramInt4, paramInt5);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.MyScroller
 * JD-Core Version:    0.7.0.1
 */