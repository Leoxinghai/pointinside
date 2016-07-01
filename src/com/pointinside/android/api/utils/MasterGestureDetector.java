package com.pointinside.android.api.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.os.Build;

public class MasterGestureDetector
{
  private boolean mFlingCalled;
  private final GestureDetector mGestureDetector;
  private final OnMasterGestureListener mListener;
  private final ScaleGestureDetectorCompat mScaleDetector;
  private boolean mScrolling;

  public MasterGestureDetector(Context paramContext, OnMasterGestureListener paramOnMasterGestureListener)
  {
    GestureInterceptor localGestureInterceptor = new GestureInterceptor();
    this.mGestureDetector = createGestureDetector(paramContext, localGestureInterceptor, null, true);
    this.mScaleDetector = ScaleGestureDetectorCompat.newInstance(paramContext, localGestureInterceptor);
    this.mListener = paramOnMasterGestureListener;
  }

  private static GestureDetector createGestureDetector(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler, boolean paramBoolean)
  {
    if (Build.VERSION.SDK_INT >= 8) {
      return FroyoAndBeyondGestureDetector.createGestureDetector(paramContext, paramOnGestureListener, paramHandler, paramBoolean);
    }
    return new GestureDetector(paramContext, paramOnGestureListener);
  }

  public boolean isMultitouchSupported()
  {
    return (this.mScaleDetector != null) && (this.mScaleDetector.isMultitouchSupported());
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 1) {}
    for (int i = 1;; i = 0)
    {
      if ((i != 0) && (this.mScrolling)) {
        this.mFlingCalled = false;
      }
      this.mGestureDetector.onTouchEvent(paramMotionEvent);
      if (this.mScaleDetector != null) {
        this.mScaleDetector.onTouchEvent(paramMotionEvent);
      }
      if ((i != 0) && (this.mScrolling) && (!this.mFlingCalled))
      {
        this.mScrolling = false;
        this.mListener.onScrollFinished(paramMotionEvent);
      }
      return true;
    }
  }

  private static abstract interface CompatOnScaleGestureListener
  {
    public abstract boolean onScale(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat);

    public abstract boolean onScaleBegin(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat);

    public abstract void onScaleEnd(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat);
  }

  private static class FroyoAndBeyondGestureDetector
  {
    public static GestureDetector createGestureDetector(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler, boolean paramBoolean)
    {
      return new GestureDetector(paramContext, paramOnGestureListener, paramHandler, paramBoolean);
    }
  }

  private class GestureInterceptor
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, MasterGestureDetector.CompatOnScaleGestureListener
  {
    private GestureInterceptor() {}

    public boolean onDoubleTap(MotionEvent paramMotionEvent)
    {
      return MasterGestureDetector.this.mListener.onDoubleTap(paramMotionEvent);
    }

    public boolean onDoubleTapEvent(MotionEvent paramMotionEvent)
    {
      return MasterGestureDetector.this.mListener.onDoubleTapEvent(paramMotionEvent);
    }

    public boolean onDown(MotionEvent paramMotionEvent)
    {
      return MasterGestureDetector.this.mListener.onDown(paramMotionEvent);
    }

    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      MasterGestureDetector.this.mFlingCalled = true;
      return MasterGestureDetector.this.mListener.onFling(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2);
    }

    public void onLongPress(MotionEvent paramMotionEvent)
    {
      MasterGestureDetector.this.mListener.onLongPress(paramMotionEvent);
    }

    public boolean onScale(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat)
    {
      return MasterGestureDetector.this.mListener.onScale(paramScaleGestureDetectorCompat);
    }

    public boolean onScaleBegin(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat)
    {
      MasterGestureDetector.this.mScrolling = false;
      return MasterGestureDetector.this.mListener.onScaleBegin(paramScaleGestureDetectorCompat);
    }

    public void onScaleEnd(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat)
    {
      MasterGestureDetector.this.mListener.onScaleEnd(paramScaleGestureDetectorCompat);
    }

    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      MasterGestureDetector.this.mScrolling = true;
      return MasterGestureDetector.this.mListener.onScroll(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2);
    }

    public void onShowPress(MotionEvent paramMotionEvent)
    {
      MasterGestureDetector.this.mListener.onShowPress(paramMotionEvent);
    }

    public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent)
    {
      return MasterGestureDetector.this.mListener.onSingleTapConfirmed(paramMotionEvent);
    }

    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
      return MasterGestureDetector.this.mListener.onSingleTapUp(paramMotionEvent);
    }
  }

  public static abstract interface OnMasterGestureListener
    extends GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, MasterGestureDetector.CompatOnScaleGestureListener
  {
    public abstract boolean onScrollFinished(MotionEvent paramMotionEvent);
  }

  public static abstract class ScaleGestureDetectorCompat
  {
    private boolean mMultitouchSupported;

    public ScaleGestureDetectorCompat(Context paramContext)
    {
      this.mMultitouchSupported = SystemFeatureTester.getInstance().hasSystemFeature(paramContext, "android.hardware.touchscreen.multitouch");
    }

    private static ScaleGestureDetectorCompat newInstance(Context paramContext, MasterGestureDetector.CompatOnScaleGestureListener paramCompatOnScaleGestureListener)
    {
      int i = Build.VERSION.SDK_INT;
      if (i >= 8) {
        return new FroyoAndBeyond(paramContext, paramCompatOnScaleGestureListener);
      }
      if (i >= 5) {
        return new Eclair(paramContext, paramCompatOnScaleGestureListener);
      }
      return null;
    }

    public abstract float getFocusX();

    public abstract float getFocusY();

    public abstract float getScaleFactor();

    public boolean isMultitouchSupported()
    {
      return this.mMultitouchSupported;
    }

    public abstract boolean onTouchEvent(MotionEvent paramMotionEvent);

    private static class Eclair
      extends MasterGestureDetector.ScaleGestureDetectorCompat
    {
      private final ScaleGestureDetector mDetector;

      public Eclair(Context paramContext, final MasterGestureDetector.CompatOnScaleGestureListener paramCompatOnScaleGestureListener)
      {
        super(paramContext);
        this.mDetector = new ScaleGestureDetector(paramContext, new ScaleGestureDetector.OnScaleGestureListener()
        {
          public boolean onScale(ScaleGestureDetector paramAnonymousScaleGestureDetector)
          {
            return paramCompatOnScaleGestureListener.onScale(MasterGestureDetector.ScaleGestureDetectorCompat.Eclair.this);
          }

          public boolean onScaleBegin(ScaleGestureDetector paramAnonymousScaleGestureDetector)
          {
            return paramCompatOnScaleGestureListener.onScaleBegin(MasterGestureDetector.ScaleGestureDetectorCompat.Eclair.this);
          }

          public void onScaleEnd(ScaleGestureDetector paramAnonymousScaleGestureDetector)
          {
            paramCompatOnScaleGestureListener.onScaleEnd(MasterGestureDetector.ScaleGestureDetectorCompat.Eclair.this);
          }
        });
      }

      public float getFocusX()
      {
        return this.mDetector.getFocusX();
      }

      public float getFocusY()
      {
        return this.mDetector.getFocusY();
      }

      public float getScaleFactor()
      {
        return this.mDetector.getScaleFactor();
      }

      public boolean onTouchEvent(MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getPointerCount() > 1) {
          return this.mDetector.onTouchEvent(paramMotionEvent);
        }
        return false;
      }
    }

    private static class FroyoAndBeyond
      extends MasterGestureDetector.ScaleGestureDetectorCompat
    {
      private final android.view.ScaleGestureDetector mDetector;

      public FroyoAndBeyond(Context paramContext, final MasterGestureDetector.CompatOnScaleGestureListener paramCompatOnScaleGestureListener)
      {
        super(paramContext);
        this.mDetector = new android.view.ScaleGestureDetector(paramContext, new android.view.ScaleGestureDetector.OnScaleGestureListener()
        {
          public boolean onScale(android.view.ScaleGestureDetector paramAnonymousScaleGestureDetector)
          {
            return paramCompatOnScaleGestureListener.onScale(MasterGestureDetector.ScaleGestureDetectorCompat.FroyoAndBeyond.this);
          }

          public boolean onScaleBegin(android.view.ScaleGestureDetector paramAnonymousScaleGestureDetector)
          {
            return paramCompatOnScaleGestureListener.onScaleBegin(MasterGestureDetector.ScaleGestureDetectorCompat.FroyoAndBeyond.this);
          }

          public void onScaleEnd(android.view.ScaleGestureDetector paramAnonymousScaleGestureDetector)
          {
            paramCompatOnScaleGestureListener.onScaleEnd(MasterGestureDetector.ScaleGestureDetectorCompat.FroyoAndBeyond.this);
          }
        });
      }

      public float getFocusX()
      {
        return this.mDetector.getFocusX();
      }

      public float getFocusY()
      {
        return this.mDetector.getFocusY();
      }

      public float getScaleFactor()
      {
        return this.mDetector.getScaleFactor();
      }

      public boolean onTouchEvent(MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getPointerCount() > 1) {
          return this.mDetector.onTouchEvent(paramMotionEvent);
        }
        return false;
      }
    }
  }

  public static class SimpleOnMasterGestureListener
    implements MasterGestureDetector.OnMasterGestureListener
  {
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener();

    public boolean onDoubleTap(MotionEvent paramMotionEvent)
    {
      return this.mGestureListener.onDoubleTap(paramMotionEvent);
    }

    public boolean onDoubleTapEvent(MotionEvent paramMotionEvent)
    {
      return this.mGestureListener.onDoubleTapEvent(paramMotionEvent);
    }

    public boolean onDown(MotionEvent paramMotionEvent)
    {
      return this.mGestureListener.onDown(paramMotionEvent);
    }

    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      return this.mGestureListener.onFling(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2);
    }

    public void onLongPress(MotionEvent paramMotionEvent)
    {
      this.mGestureListener.onLongPress(paramMotionEvent);
    }

    public boolean onScale(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat)
    {
      return false;
    }

    public boolean onScaleBegin(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat)
    {
      return true;
    }

    public void onScaleEnd(MasterGestureDetector.ScaleGestureDetectorCompat paramScaleGestureDetectorCompat) {}

    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      return this.mGestureListener.onScroll(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2);
    }

    public boolean onScrollFinished(MotionEvent paramMotionEvent)
    {
      return true;
    }

    public void onShowPress(MotionEvent paramMotionEvent)
    {
      this.mGestureListener.onShowPress(paramMotionEvent);
    }

    public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent)
    {
      return this.mGestureListener.onSingleTapConfirmed(paramMotionEvent);
    }

    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
      return this.mGestureListener.onSingleTapUp(paramMotionEvent);
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.utils.MasterGestureDetector
 * JD-Core Version:    0.7.0.1
 */