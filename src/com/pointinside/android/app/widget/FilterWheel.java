package com.pointinside.android.app.widget;

import com.pointinside.android.app.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

//import android.text.Layout.Alignment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import com.pointinside.android.app.util.FloatMathExtras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FilterWheel
  extends View
{
  private static final int COLLAPSE_DURATION = 200;
  private static final boolean DEBUG = false;
  private static final int ITEM_SPACE_ANGULAR_WIDTH = 80;
  private static final float RAD_270 = FloatMathExtras.toRadians(270.0F);
  private static final String TAG = FilterWheel.class.getSimpleName();
  private static final int TOGGLE_AREA_ANGLE_START = 44;
  private static final int TOGGLE_AREA_ANGLE_STOP = 78;
  private static final int TOGGLE_AREA_ANGULAR_WIDTH = 34;
  private static final boolean VISUAL_DEBUG = false;
  private static final int WHEEL_AREA_ANGLE_START = -42;
  private static final int WHEEL_AREA_ANGULAR_WIDTH = 264;
  private static final float[] sTempPointFA = new float[2];
  private final int mBetweenRadius;
  private final int mBottomRadius;
  private final Drawable mCenterButtonPressed;
  private TranslateAnimation mCollapseAnimation;
  private Rect mCollapseHitArea;
  private boolean mCollapsePressed;
  private final Transformation mCollapseTransformation;
  private int mCollapsedYOffset;
  private final Drawable mDownArrowNormal;
  private final Drawable mDownArrowPressed;
  private boolean mDragging;
  private final int mDraggingThreshold;
  private boolean mExpanded;
  private boolean mFingerDownInWheel;
  private float mFingerDownX;
  private float mFingerDownY;
  private final int mFullyCollapsedYOffset;
  private final int mInnerRadius;
  private final Paint mItemBitmapPaint;
  private final int mItemSpaceArcLength;
  private final float mItemTextHeight;
  private final int mItemTextPadding;
  private final Drawable mLeftButtonDisabled;
  private final Drawable mLeftButtonPressed;
  private final Drawable mLeftButtonToggled;
  public boolean mLeftDisabled;
  private final ItemsGallery mLeftGallery;
  private boolean mLeftSelected;
  private String mLeftText;
  private boolean mLeftTogglePressed;
  private OnFilterEventListener mListener;
  private final int mMinimumFlingVelocity;
  private final int mOuterRadius;
  private int mPivotItemIndex;
  private int mPivotOffsetX;
  private final Drawable mRightButtonDisabled;
  private final Drawable mRightButtonPressed;
  private final Drawable mRightButtonToggled;
  public boolean mRightDisabled;
  private final ItemsGallery mRightGallery;
  private String mRightText;
  private boolean mRightTogglePressed;
  private final TextPaint mSelectedItemTextPaint;
  private int mSlideYOffset;
  private boolean mSliding;
  private final int mSlidingThreshold;
  private float mTilt;
  private final int mToggleAreaArcLength;
  private final Paint mToggleDisabledTextPaint;
  private Point[] mToggleLeftHitArea;
  private final Path mToggleLeftPath = new Path();
  private Point[] mToggleRightHitArea;
  private final Path mToggleRightPath = new Path();
  private final float mToggleTextHeight;
  private final Paint mToggleTextPaint;
  private final int mTopRadius;
  private final TextPaint mUnselectedItemTextPaint;
  private final Drawable mUpArrowNormal;
  private final Drawable mUpArrowPressed;
  private final VelocityTracker mVelocityTracker;
  private final Path mWheelAreaPath = new Path();
  private final Drawable mWheelGraphic;

  public FilterWheel(Context paramContext)
  {
    this(paramContext, null);
  }

  public FilterWheel(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public FilterWheel(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    ItemsGallery localItemsGallery1 = new ItemsGallery();
    this.mLeftGallery = localItemsGallery1;
    ItemsGallery localItemsGallery2 = new ItemsGallery();
    this.mRightGallery = localItemsGallery2;
    this.mCollapseTransformation = new Transformation();
    this.mPivotItemIndex = -1;
    Resources localResources = getResources();
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    this.mDraggingThreshold = localViewConfiguration.getScaledTouchSlop();
    this.mSlidingThreshold = localViewConfiguration.getScaledTouchSlop();
    this.mVelocityTracker = VelocityTracker.obtain();
    this.mMinimumFlingVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mWheelGraphic = localResources.getDrawable(R.drawable.filterwheel);
    this.mUpArrowNormal = localResources.getDrawable(R.drawable.up_arrow_normal);
    this.mUpArrowPressed = localResources.getDrawable(R.drawable.up_arrow_pressed);
    this.mDownArrowNormal = localResources.getDrawable(R.drawable.down_arrow_normal);
    this.mDownArrowPressed = localResources.getDrawable(R.drawable.down_arrow_pressed);
    this.mLeftButtonPressed = localResources.getDrawable(R.drawable.left_pressed);
    this.mLeftButtonToggled = localResources.getDrawable(R.drawable.left_toggled);
    this.mLeftButtonDisabled = localResources.getDrawable(R.drawable.left_disabled);
    this.mRightButtonPressed = localResources.getDrawable(R.drawable.right_pressed);
    this.mRightButtonToggled = localResources.getDrawable(R.drawable.right_toggle);
    this.mRightButtonDisabled = localResources.getDrawable(R.drawable.right_disabled);
    this.mCenterButtonPressed = localResources.getDrawable(R.drawable.center_pressed);
    this.mFullyCollapsedYOffset = localResources.getDimensionPixelSize(R.dimen.filter_wheel_collapsed_offset);
    this.mOuterRadius = localResources.getDimensionPixelSize(R.dimen.filter_wheel_outer_radius);
    this.mBetweenRadius = localResources.getDimensionPixelSize(R.dimen.filter_wheel_between_radius);
    this.mInnerRadius = localResources.getDimensionPixelSize(R.dimen.filter_wheel_inner_radius);
    this.mTopRadius = (this.mBetweenRadius + (this.mOuterRadius - this.mBetweenRadius) / 2);
    this.mBottomRadius = (this.mInnerRadius + (this.mBetweenRadius - this.mInnerRadius) / 2);
    this.mToggleAreaArcLength = ((int)(this.mTopRadius * Math.toRadians(34.0D)));
    this.mItemSpaceArcLength = ((int)(this.mBottomRadius * Math.toRadians(80.0D)));
    float f1 = localResources.getDimensionPixelSize(R.dimen.filter_wheel_label_text_size);
    int i = localResources.getColor(R.color.filter_wheel_toggle_text_color);
    int j = localResources.getColor(R.color.filter_wheel_toggle_disabled_color);
    int k = localResources.getColor(R.color.filter_wheel_toggle_shadow_color);
    float f2 = localResources.getDimension(R.dimen.filter_wheel_toggle_shadow_radius);
    float f3 = localResources.getDimension(R.dimen.filter_wheel_toggle_shadow_offset);
    float f4 = localResources.getDimensionPixelSize(R.dimen.filter_wheel_item_text_size);
    int m = localResources.getColor(R.color.filter_wheel_item_text_color);
    int n = localResources.getColor(R.color.filter_wheel_item_selected_text_color);
    this.mToggleTextPaint = getTextPaint(f1, i, k, f2, f3);
    this.mToggleDisabledTextPaint = getTextPaint(f1, j, k, f2, f3);
    this.mToggleTextHeight = getTextPaintHeight(this.mToggleTextPaint);
    this.mSelectedItemTextPaint = getTextPaint(f4, n, k, f2, f3);
    this.mUnselectedItemTextPaint = getTextPaint(f4, m, k, f2, f3);
    this.mItemTextHeight = getTextPaintHeight(this.mSelectedItemTextPaint);
    this.mItemTextPadding = localResources.getDimensionPixelSize(R.dimen.filter_wheel_item_text_padding);
    this.mItemBitmapPaint = new Paint();
    this.mItemBitmapPaint.setFilterBitmap(true);
    this.mItemBitmapPaint.setDither(true);
  }

  private static void applyHorizontalGravity(Drawable drawable, int i, int j)
  {
      int k;
      int l;
      int i1;
      k = i & 7;
      l = drawable.getIntrinsicWidth();
      i1 = drawable.getIntrinsicHeight();
      int j1 =0;
      switch(k) {
//      JVM INSTR tableswitch 1 5: default 52
  //                   1 78
  //                   2 52
  //                   3 60
  //                   4 52
  //                   5 89;
//         goto _L1 _L2 _L1 _L3 _L1 _L4

	case 2:
	case 4:
	default:
      throw new IllegalArgumentException();
	case 3:
      j1 = 0;
	case 1:
      j1 = (j - l) / 2;
      break;
	case 5:
      j1 = j - l;
      break;
    }

	drawable.setBounds(j1, 0, j1 + l, i1);
	return;

  }

  private boolean applyVelocityOnUp()
  {
    this.mVelocityTracker.computeCurrentVelocity(500);
    float f = this.mVelocityTracker.getXVelocity();
    boolean bool1 = Math.abs(f) < this.mMinimumFlingVelocity;
    boolean bool2 = false;
    int k =0;
    if (bool1)
    {
      if (f <= 0.0F) {
          int i = computeViewOffsetX(this.mPivotOffsetX, 1);
          int j = this.mPivotOffsetX;
          k = 0;
          if (j < 0)
          {
            boolean bool3 = -f < i;
            k = 0;
            if (bool3) {
              k = 1;
            }
          }
      }
      int m = computeViewOffsetX(this.mPivotOffsetX, -1);
      int n = this.mPivotOffsetX;
      k = 0;
      if (n > 0)
      {
        boolean bool4 = f < Math.abs(m);
        k = 0;
        if (bool4) {
          k = -1;
        }
      }
    }
      bool2 = false;
      if (k != 0)
      {
        this.mPivotItemIndex = getCurrentGallery().getRelativeItemIndex(this.mPivotItemIndex, k);
        bool2 = true;
      }
      return bool2;
  }

  private void assertLegalPivotIndex()
  {
    if ((this.mPivotItemIndex < 0) || (this.mPivotItemIndex >= getCurrentGallery().getItemCount())) {
      throw new AssertionError("Illegal pivot index " + this.mPivotItemIndex);
    }
  }

  private void beginCollapseAnimation(int paramInt)
  {
    int i = this.mCollapsedYOffset;
    if (this.mCollapsePressed) {
      i += this.mSlideYOffset;
    }
    int j = (int)(200.0F * (Math.abs(i - paramInt) / this.mFullyCollapsedYOffset));
    this.mCollapseAnimation = new TranslateAnimation(0.0F, 0.0F, i, paramInt);
    this.mCollapseAnimation.setDuration(j);
    this.mCollapseAnimation.setInterpolator(new DecelerateInterpolator());
    this.mCollapseAnimation.initialize(0, 0, 0, 0);
    this.mCollapseAnimation.start();
    invalidate();
  }

  private void cancelDrag()
  {
    this.mDragging = false;
    this.mPivotOffsetX = 0;
    this.mPivotItemIndex = getCurrentGallery().getSelectedIndex();
  }

  private static int clamp(int paramInt1, int paramInt2, int paramInt3)
  {
    return Math.max(paramInt2, Math.min(paramInt3, paramInt1));
  }

  private int clampSlideMovement(int paramInt)
  {
    if (isExpanded()) {
      return clamp(paramInt, 0, this.mFullyCollapsedYOffset);
    }
    return clamp(paramInt, -this.mFullyCollapsedYOffset, 0);
  }

  private void computeTogglePath(Path paramPath, int paramInt)
  {
    paramPath.reset();
    paramPath.arcTo(getRectForCircle(getWidth() / 2, this.mOuterRadius + getFilterWheelGraphicTopPadding(), this.mTopRadius, new RectF()), paramInt, 34.0F);
  }

  private int computeViewOffsetX(int paramInt1, int paramInt2)
  {
    float f = FloatMathExtras.toRadians((float)Math.toDegrees(Math.asin(paramInt1 / this.mBottomRadius)) + paramInt2 * 40);
    return (int)(this.mBottomRadius * FloatMath.cos(f + RAD_270));
  }

  private void computeWheelPath(Path paramPath, int paramInt)
  {
    paramPath.reset();
    paramPath.arcTo(getRectForCircle(getWidth() / 2, this.mOuterRadius + getFilterWheelGraphicTopPadding(), this.mBottomRadius, new RectF()), paramInt, 264.0F);
  }

  private void doCollapse()
  {
    this.mExpanded = false;
    this.mListener.onCollapse();
    beginCollapseAnimation(this.mFullyCollapsedYOffset);
  }

  private void doDraw(Canvas canvas)
  {
      boolean flag = isExpanded();
      int j;
      if(stepCollapseAnimation())
      {
          mCollapsedYOffset = (int)mapPointY(mCollapseTransformation.getMatrix(), 0.0F, sTempPointFA);
          invalidate();
      } else
      {
          int i;
          if(flag)
              i = 0;
          else
              i = mFullyCollapsedYOffset;
          mCollapsedYOffset = i;
      }
      j = mCollapsedYOffset;
      if(mCollapsePressed)
          j += mSlideYOffset;
      canvas.save();
      canvas.translate(0.0F, j);
      drawBackground(canvas);
      drawToggleLabels(canvas);
      if(flag || mCollapseAnimation != null || mCollapsePressed)
          drawItems(canvas);
      canvas.restore();
  }


  private void doExpand()
  {
    this.mExpanded = true;
    this.mListener.onExpand();
    beginCollapseAnimation(0);
  }

  private void drawBackground(Canvas paramCanvas)
  {
    this.mWheelGraphic.draw(paramCanvas);
    if (this.mLeftDisabled)
    {
      this.mLeftButtonDisabled.draw(paramCanvas);
      if (!this.mRightDisabled) {
          if (this.mRightTogglePressed) {
            this.mRightButtonPressed.draw(paramCanvas);
          } else if (!this.mLeftSelected) {
            this.mRightButtonToggled.draw(paramCanvas);
          }
      }
      this.mRightButtonDisabled.draw(paramCanvas);
    }
      if (this.mCollapsePressed) {
        this.mCenterButtonPressed.draw(paramCanvas);
      }
      if (!isExpanded()) {
    	        if (this.mCollapsePressed)
    	        {
    	          this.mUpArrowPressed.draw(paramCanvas);
    	          return;
    	        }
    	        this.mUpArrowNormal.draw(paramCanvas);
      }
      if (!this.mCollapsePressed) {
    	        this.mDownArrowNormal.draw(paramCanvas);
    	        return;
      }
      this.mDownArrowPressed.draw(paramCanvas);
      if (this.mLeftTogglePressed)
      {
        this.mLeftButtonPressed.draw(paramCanvas);
      }
      if (!this.mLeftSelected) {
      }
      this.mLeftButtonToggled.draw(paramCanvas);
      return;
  }

  private void drawItem(Canvas paramCanvas, ItemView paramItemView)
  {

    switch (paramItemView.item.type)
    {
    default:
      throw new IllegalStateException("Unknown item type");
    case TEXT:
        drawItemAsText(paramCanvas, paramItemView);
        break;
    case BITMAP:
    	drawItemAsBitmap(paramCanvas, paramItemView);
      break;
    }
  }

  private void drawItemAsBitmap(Canvas paramCanvas, ItemView paramItemView)
  {
    if (paramItemView.isSelected) {}
    for (Bitmap localBitmap = paramItemView.item.bitmapSelected;; localBitmap = paramItemView.item.bitmapNormal)
    {
      paramCanvas.save();
      paramCanvas.translate(paramItemView.x, paramItemView.y);
      paramCanvas.rotate(paramItemView.angleFromCenter);
      paramCanvas.drawBitmap(localBitmap, -localBitmap.getWidth() / 2, -localBitmap.getWidth() / 2, this.mItemBitmapPaint);
      paramCanvas.restore();
      return;
    }
  }

  private void drawItemAsText(Canvas canvas, ItemView itemview)
  {
      float f = (270F + itemview.angleFromCenter) - 138F;
      float f1 = (float)mBottomRadius * FloatMathExtras.toRadians(f);
      TextPaint textpaint;
      float f2;
      String s;
      StaticLayout staticlayout;
      int i;
      boolean flag;
      float f3;
      int j;
      if(itemview.isSelected)
          textpaint = mSelectedItemTextPaint;
      else
          textpaint = mUnselectedItemTextPaint;
      f2 = (float)mBottomRadius * FloatMathExtras.toRadians(40F) - (float)mItemTextPadding;
      s = itemview.item.text;
      staticlayout = new StaticLayout(s, textpaint, (int)f2, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
      i = staticlayout.getLineCount();
      if(i > 2)
          flag = true;
      else
          flag = false;
      if(i == 1)
          f3 = mItemTextHeight / 4F;
      else
          f3 = -textpaint.descent();
      j = 0;
      do
      {
          if(j >= Math.min(i, 2))
              return;
          int k = staticlayout.getLineStart(j);
          int l = staticlayout.getLineEnd(j);
          int i1 = l - k;
          if(flag && j == 1)
              l = k + Math.max(0, i1 - 3);
          String s1 = s.substring(k, l).trim();
          if(flag && j == 1)
              s1 = (new StringBuilder(String.valueOf(s1))).append("...").toString();
          float f4 = -textpaint.measureText(s1) / 2.0F;
          canvas.drawTextOnPath(s1, mWheelAreaPath, f1 + f4, f3, textpaint);
          f3 += mItemTextHeight;
          j++;
      } while(true);
  }

  private void drawItems(Canvas paramCanvas)
  {
    ItemsGallery localItemsGallery = getCurrentGallery();
    if (localItemsGallery.isEmpty()) {
      throw new IllegalStateException("Empty gallery became selected");
    }
    layoutItems(localItemsGallery);
    int i = localItemsGallery.getViewCount();
    for (int j = 0;j<i; j++)
    {
      drawItem(paramCanvas, localItemsGallery.getViewAt(j));
    }
  }

  private void drawToggleLabel(Canvas paramCanvas, String paramString, Path paramPath, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (Paint localPaint = this.mToggleDisabledTextPaint;; localPaint = this.mToggleTextPaint)
    {
      paramCanvas.drawTextOnPath(paramString, paramPath, (this.mToggleAreaArcLength - localPaint.measureText(paramString)) / 2.0F, this.mToggleTextHeight / 4.0F, localPaint);
      return;
    }
  }

  private void drawToggleLabels(Canvas paramCanvas)
  {
    drawToggleLabel(paramCanvas, this.mLeftText, this.mToggleLeftPath, this.mLeftDisabled);
    drawToggleLabel(paramCanvas, this.mRightText, this.mToggleRightPath, this.mRightDisabled);
  }

  private boolean fireButtonClicks()
  {
      boolean flag = true;
      if(mCollapsePressed) {
	      if(isExpanded())
	          onCollapseClicked();
	      else
	          onExpandClicked();
	      mRightTogglePressed = false;
	      mLeftTogglePressed = false;
	      mCollapsePressed = false;
	      return flag;
      } else {
	      if(mLeftTogglePressed)
	      {
	          if(!mLeftDisabled)
	              onLeftToggleClicked();
	      } else
	      if(mRightTogglePressed)
	      {
	          if(!mRightDisabled)
	              onRightToggleClicked();
	      } else
	      {
	          flag = false;
	      }
	      return false;
      }
  }

  private ItemsGallery getCurrentGallery()
  {
    if (this.mLeftSelected) {
      return this.mLeftGallery;
    }
    return this.mRightGallery;
  }

  private float getFilterWheelGraphicTopPadding()
  {
    return getResources().getDimension(2131296276);
  }

  private static RectF getRectForCircle(float paramFloat1, float paramFloat2, float paramFloat3, RectF paramRectF)
  {
    paramRectF.left = (paramFloat1 - paramFloat3);
    paramRectF.top = (paramFloat2 - paramFloat3);
    paramRectF.right = (paramFloat1 + paramFloat3);
    paramRectF.bottom = (paramFloat2 + paramFloat3);
    return paramRectF;
  }

  private static TextPaint getTextPaint(float paramFloat1, int paramInt1, int paramInt2, float paramFloat2, float paramFloat3)
  {
    TextPaint localTextPaint = new TextPaint();
    localTextPaint.setAntiAlias(true);
    localTextPaint.setTextAlign(Paint.Align.LEFT);
    localTextPaint.setTypeface(Typeface.DEFAULT);
    localTextPaint.setTextSize(paramFloat1);
    localTextPaint.setColor(paramInt1);
    localTextPaint.setShadowLayer(paramFloat2, paramFloat3, paramFloat3, paramInt2);
    return localTextPaint;
  }

  private static float getTextPaintHeight(Paint paramPaint)
  {
    return -paramPaint.ascent() + paramPaint.descent();
  }

  private boolean handleItemClick()
  {
      ItemsGallery itemsgallery = getCurrentGallery();
      if(itemsgallery.getViewCount() <= 0)
    	  return false;

      int i;
      int j;
      int k;
      ItemView itemview = itemsgallery.getViewAt(0);
      ItemView itemview1 = itemsgallery.getViewAt(1);
      ItemView itemview2 = itemsgallery.getViewAt(2);
      i = (int)Math.abs(mFingerDownX - (float)itemview.x);
      j = (int)Math.abs(mFingerDownX - (float)itemview1.x);
      k = (int)Math.abs(mFingerDownX - (float)itemview2.x);
      int index0;
      if(j >= i) {
    	  index0 = 0;
	      if(k < i)
	    	  index0 = 1;
      } else {
    	  index0 = -1;
      }
      if(index0 != 0)
      {
          mPivotItemIndex = getCurrentGallery().getRelativeItemIndex(mPivotItemIndex, index0);
          return true;
      }
      return false;
  }

  private boolean handleMoveInWheel(int i, int j)
  {
      if(mDragging && Math.abs((float)i - mFingerDownX) >= (float)mDraggingThreshold) {
	      mDragging = true;
	      mFingerDownX = i;
	      return true;
      } else {
	      if(!mDragging) return true;
	      else {
	          int k;
	          int l;
	          int i1;
	          byte byte0;
	          k = (int)((float)i - mFingerDownX);
	          l = computeViewOffsetX(k, -1);
	          i1 = computeViewOffsetX(k, 1);
	          if(Math.abs(k) > Math.abs(l)) {
		          byte0 = -1;
		          k = l;
	          } else {
		          int j1 = Math.abs(k);
		          int k1 = Math.abs(i1);
		          byte0 = 0;
		          if(j1 > k1)
		          {
		              byte0 = 1;
		              k = i1;
		          }
	          }
	          if(byte0 != 0)
	          {
	              mPivotItemIndex = getCurrentGallery().getRelativeItemIndex(mPivotItemIndex, byte0);
	              mFingerDownX = i - k;
	          }
	          mPivotOffsetX = k;
	          return true;
	      }
      }

  }

  private boolean handleMoveOutsideWheel(int i, int j)
  {
      int k = (int)((float)i - mFingerDownX);
      boolean flag;
      if((!isExpanded() || !mLeftTogglePressed || k <= mDraggingThreshold) && (!mRightTogglePressed || -k <= mDraggingThreshold)) {
          int l;
          if(!mCollapsePressed)
              return false;
          l = clampSlideMovement((int)((float)j - mFingerDownY));
          if(!isExpanded()) {
              flag = false;
              if(l < 0)
                  flag = true;
              if(!mSliding && flag && Math.abs(l) > mSlidingThreshold)
                  mSliding = true;
              mSlideYOffset = l;
              return true;
          } else {
              flag = false;
              if(l > 0)
                  flag = true;
              if(updateButtonPressedStates(i, j) || !isExpanded() || !isTouchInWheelArea(i, j)) {
                  return true;

              } else {
                  mFingerDownInWheel = true;
                  return true;

              }
          }
      } else {
	      mFingerDownInWheel = true;
	      mLeftTogglePressed = false;
	      mRightTogglePressed = false;
      }
      return true;
  }

  private void handleSlideUp(int i)
  {
      mVelocityTracker.computeCurrentVelocity(200);
      float f = mVelocityTracker.getYVelocity();
      if(Math.abs(f) > (float)mMinimumFlingVelocity)
          i = (int)(f + (float)i);
      boolean flag;
      if(Math.abs(i) > mFullyCollapsedYOffset / 2)
          flag = true;
      else
          flag = false;
      if(flag)
          if(isExpanded())
          {
              doCollapse();
              return;
          } else
          {
              doExpand();
              return;
          }
      if(isExpanded())
      {
          beginCollapseAnimation(0);
          return;
      } else
      {
          beginCollapseAnimation(mFullyCollapsedYOffset);
          return;
      }
  }

  private boolean isItemVisible(ItemsGallery paramItemsGallery, int paramInt)
  {
    assertLegalPivotIndex();
    return Math.abs(paramInt) <= 2;
  }

  private static boolean isPointInPolygon(int x, int y, Point apoint[]) {
    int i;
    int j;
    boolean result = false;
    for (i = 0, j = apoint.length - 1; i < apoint.length; j = i++) {
      if ((apoint[i].y > y) != (apoint[j].y > y) &&
          (x < (apoint[j].x - apoint[i].x) * (y - apoint[i].y) / (apoint[j].y-apoint[i].y) + apoint[i].x)) {
        result = !result;
       }
    }
    return result;
  }

  private boolean isTouchInWheelArea(int paramInt1, int paramInt2)
  {
    return paramInt2 > this.mCollapseHitArea.bottom;
  }

  private void layoutItem(ItemsGallery paramItemsGallery, int paramInt)
  {
    ItemView localItemView = paramItemsGallery.obtainView();
    int i = paramItemsGallery.getRelativeItemIndex(this.mPivotItemIndex, paramInt);
    localItemView.item = paramItemsGallery.getItem(i);
    if ((!this.mDragging) && (paramItemsGallery.getSelectedIndex() == i)) {}
    for (boolean bool = true;; bool = false)
    {
      localItemView.isSelected = bool;
      float f1 = this.mTilt + paramInt * 40;
      float f2 = FloatMathExtras.toRadians(f1);
      localItemView.angleFromCenter = f1;
      int j = (int)(this.mBottomRadius * FloatMath.cos(f2 + RAD_270));
      localItemView.x = (j + getWidth() / 2);
      localItemView.y = (this.mBottomRadius - FloatMath.sqrt(this.mBottomRadius * this.mBottomRadius - j * j) + getFilterWheelGraphicTopPadding() + (this.mOuterRadius - this.mBottomRadius));
      return;
    }
  }

  private void layoutItems(ItemsGallery paramItemsGallery)
  {
    this.mTilt = ((float)Math.toDegrees(Math.asin(this.mPivotOffsetX / this.mBottomRadius)));
    paramItemsGallery.beginLayout();
    layoutItem(paramItemsGallery, 0);
    int i = -1;
    for (int j = 1;; j++)
    {
      if ((!isItemVisible(paramItemsGallery, i)) || (!isItemVisible(paramItemsGallery, j)))
      {
        paramItemsGallery.endLayout();
        return;
      }
      layoutItem(paramItemsGallery, i);
      layoutItem(paramItemsGallery, j);
      i--;
    }
  }

  private static Rect makeCollapseHitArea(Resources paramResources, Point[] paramArrayOfPoint1, Point[] paramArrayOfPoint2)
  {
    return new Rect(paramArrayOfPoint1[1].x, paramArrayOfPoint1[1].y, paramArrayOfPoint2[2].x, paramArrayOfPoint2[2].y);
  }

  private static Point[] makeToggleHitArea(Resources paramResources, int paramInt1, int paramInt2)
  {
    int i = paramResources.getDimensionPixelSize(2131296281);
    int j = paramResources.getDimensionPixelSize(2131296282);
    int k = paramResources.getDimensionPixelSize(2131296283);
    int m = paramResources.getDimensionPixelSize(2131296284);
    int n = paramResources.getDimensionPixelSize(2131296285);
    int i1 = paramInt1 + paramInt2 * paramResources.getDimensionPixelSize(2131296286);
    int i2 = n * paramInt2;
    Point[] arrayOfPoint = new Point[5];
    arrayOfPoint[0] = new Point(i1, i);
    arrayOfPoint[1] = new Point(i1 + i2, k);
    arrayOfPoint[2] = new Point(i1 + i2, m);
    arrayOfPoint[3] = new Point(i1, j);
    arrayOfPoint[4] = new Point(arrayOfPoint[0]);
    return arrayOfPoint;
  }

  private static float mapPointY(Matrix paramMatrix, float paramFloat, float[] paramArrayOfFloat)
  {
    paramArrayOfFloat[0] = 0.0F;
    paramArrayOfFloat[1] = paramFloat;
    paramMatrix.mapPoints(paramArrayOfFloat);
    return paramArrayOfFloat[1];
  }

  private void onCollapseClicked()
  {
    doCollapse();
  }

  private void onExpandClicked()
  {
    doExpand();
  }

  private void onLeftToggleClicked()
  {
    setLeftSelected(true);
  }

  private void onRightToggleClicked()
  {
    setLeftSelected(false);
  }

  private static Path pathFromPoints(Point[] paramArrayOfPoint)
  {
    Path localPath = new Path();
    Object localObject = null;
    int i = paramArrayOfPoint.length;
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        localPath.close();
        return localPath;
      }
      Point localPoint = paramArrayOfPoint[j];
      if (localObject != null) {
        localPath.lineTo(localPoint.x, localPoint.y);
      }
      localPath.moveTo(localPoint.x, localPoint.y);
      localObject = localPoint;
    }
  }

  private void snapSelectionToPivot()
  {
      int i;
      i = mPivotItemIndex;
      ItemsGallery itemsgallery = getCurrentGallery();
      if(itemsgallery.getSelectedIndex() != i)
      {
          itemsgallery.setSelectedIndex(i);
          if(!mLeftSelected)
              mListener.onRightItemSelectionChange(i);
          else
        	  mListener.onLeftItemSelectionChange(i);
      }
      return;
  }

  private boolean stepCollapseAnimation()
  {
    if ((this.mCollapseAnimation != null) && (this.mCollapseAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), this.mCollapseTransformation))) {
      return true;
    }
    this.mCollapseAnimation = null;
    return false;
  }

  private boolean updateButtonPressedStates(int paramInt1, int paramInt2)
  {
    this.mRightTogglePressed = false;
    this.mLeftTogglePressed = false;
    this.mCollapsePressed = false;
    boolean bool1 = this.mCollapseHitArea.contains(paramInt1, paramInt2);
    this.mCollapsePressed = bool1;
    if ((!bool1) && (isExpanded()))
    {
      boolean bool2 = isPointInPolygon(paramInt1, paramInt2, this.mToggleLeftHitArea);
      this.mLeftTogglePressed = bool2;
      if (!bool2) {
        this.mRightTogglePressed = isPointInPolygon(paramInt1, paramInt2, this.mToggleRightHitArea);
      }
    }
    return (this.mCollapsePressed) || (this.mLeftTogglePressed) || (this.mRightTogglePressed);
  }

  public void collapse()
  {
    if (isExpanded()) {
      doCollapse();
    }
  }

  public void expand()
  {
    if (!isExpanded()) {
      doExpand();
    }
  }

  public List<FilterWheelItem> getLeftItems()
  {
    return Collections.unmodifiableList(this.mLeftGallery.mItems);
  }

  public int getLeftSelectedPosition()
  {
    return this.mLeftGallery.getSelectedIndex();
  }

  public OnFilterEventListener getOnFilterEventListener()
  {
    return this.mListener;
  }

  public List<FilterWheelItem> getRightItems()
  {
    return Collections.unmodifiableList(this.mRightGallery.mItems);
  }

  public int getRightSelectedPosition()
  {
    return this.mRightGallery.getSelectedIndex();
  }

  public boolean isExpanded()
  {
    return this.mExpanded;
  }

  public boolean isLeftSelected()
  {
    return this.mLeftSelected;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    doDraw(paramCanvas);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = this.mWheelGraphic.getIntrinsicWidth();
    int j = this.mWheelGraphic.getIntrinsicHeight();
    int k = resolveSize(i, paramInt1);
    int m = resolveSize(j, paramInt2);
    if (k != i) {
      throw new IllegalStateException("Perhaps you should place me in a CenterWideChildLayout?");
    }
    setMeasuredDimension(k, m);
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mWheelGraphic.setBounds(0, 0, this.mWheelGraphic.getIntrinsicWidth(), this.mWheelGraphic.getIntrinsicHeight());
    int i = getWidth();
    applyHorizontalGravity(this.mUpArrowNormal, 1, i);
    applyHorizontalGravity(this.mUpArrowPressed, 1, i);
    applyHorizontalGravity(this.mDownArrowNormal, 1, i);
    applyHorizontalGravity(this.mDownArrowPressed, 1, i);
    applyHorizontalGravity(this.mLeftButtonPressed, 3, i);
    applyHorizontalGravity(this.mLeftButtonToggled, 3, i);
    applyHorizontalGravity(this.mLeftButtonDisabled, 3, i);
    applyHorizontalGravity(this.mRightButtonPressed, 5, i);
    applyHorizontalGravity(this.mRightButtonToggled, 5, i);
    applyHorizontalGravity(this.mRightButtonDisabled, 5, i);
    applyHorizontalGravity(this.mCenterButtonPressed, 1, i);
    computeTogglePath(this.mToggleLeftPath, 224);
    computeTogglePath(this.mToggleRightPath, 282);
    computeWheelPath(this.mWheelAreaPath, 138);
    this.mToggleLeftHitArea = makeToggleHitArea(getResources(), 0, 1);
    this.mToggleRightHitArea = makeToggleHitArea(getResources(), getWidth(), -1);
    this.mCollapseHitArea = makeCollapseHitArea(getResources(), this.mToggleLeftHitArea, this.mToggleRightHitArea);
  }

  public boolean onTouchEvent(MotionEvent motionevent)
  {
      int i;
      int j;
      int k;
      boolean flag;
      mVelocityTracker.addMovement(motionevent);
      i = (int)motionevent.getX();
      j = (int)motionevent.getY();
      if(!isExpanded())
          j -= mCollapsedYOffset;
      k = motionevent.getAction();
      flag = false;
      switch(k) {
//      JVM INSTR tableswitch 0 2: default 72
  //                   0 84
  //                   1 172
  //                   2 142;
//         goto _L1 _L2 _L3 _L4
//_L1:
	default:
      if(flag)
          invalidate();
      return flag;
//_L2:
	case 0:
      flag = updateButtonPressedStates(i, j);
      if(!flag && isExpanded())
      {
          flag = isTouchInWheelArea(i, j);
          if(flag)
              mFingerDownInWheel = true;
      }
      if(flag)
      {
          mFingerDownX = i;
          mFingerDownY = j;
      }
	  if(mSliding)
	  {
	      handleSlideUp(mSlideYOffset);
	      mSliding = false;
	      mCollapsePressed = false;
	      flag = true;
	  } else
	  {
	      flag = fireButtonClicks();
	  }
	  flag = true;
      break;
//_L4:
	case 2:
      if(mFingerDownInWheel)
          handleMoveInWheel(i, j);
      else
          handleMoveOutsideWheel(i, j);
      flag = true;
      break;
//_L3:
	case 1:
      if(!mFingerDownInWheel)
          break; /* Loop/switch isn't completed */
      if(mDragging)
      {
          applyVelocityOnUp();
          mDragging = false;
          mPivotOffsetX = 0;
      } else
      {
          handleItemClick();
      }
      snapSelectionToPivot();
      mFingerDownInWheel = false;
      flag = true;
      break;
   }
      mSlideYOffset = 0;
//   if(flag)
//       invalidate();
/*
	  */
      System.out.println("onTouchEvent."+ k+":"+flag);
   return flag;
  }

  public void setLeftDisabled(boolean paramBoolean)
  {
    this.mLeftDisabled = paramBoolean;
  }

  public void setLeftItems(ArrayList<FilterWheelItem> paramArrayList, boolean paramBoolean)
  {
    this.mLeftGallery.setItems(paramArrayList, paramBoolean);
    cancelDrag();
    this.mListener.onLeftItemSelectionChange(this.mLeftGallery.getSelectedIndex());
    invalidate();
  }

  public void setLeftSelected(boolean paramBoolean)
  {
    if (this.mLeftSelected != paramBoolean)
    {
      this.mLeftSelected = paramBoolean;
      cancelDrag();
      if (!paramBoolean) {
          this.mListener.onRightSelected();
      }
      this.mListener.onLeftSelected();
    }

    invalidate();
  }

  public void setLeftSelectedPosition(int paramInt)
  {
    if (getLeftSelectedPosition() != paramInt)
    {
      this.mLeftGallery.setSelectedIndex(paramInt);
      cancelDrag();
      invalidate();
    }
  }

  public void setLeftText(int paramInt)
  {
    this.mLeftText = getResources().getString(paramInt);
  }

  public void setOnFilterEventListener(OnFilterEventListener paramOnFilterEventListener)
  {
    this.mListener = paramOnFilterEventListener;
  }

  public void setRightDisabled(boolean paramBoolean)
  {
    this.mRightDisabled = paramBoolean;
  }

  public void setRightItems(ArrayList<FilterWheelItem> paramArrayList, boolean paramBoolean)
  {
    System.out.println("setRightItems."+paramArrayList.size());

	this.mRightGallery.setItems(paramArrayList, paramBoolean);
    cancelDrag();
    this.mListener.onRightItemSelectionChange(this.mRightGallery.getSelectedIndex());
    invalidate();
  }

  public void setRightSelectedPosition(int paramInt)
  {
    if (getRightSelectedPosition() != paramInt)
    {
      this.mRightGallery.setSelectedIndex(paramInt);
      cancelDrag();
      invalidate();
    }
  }

  public void setRightText(int paramInt)
  {
    this.mRightText = getResources().getString(paramInt);
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    if (super.verifyDrawable(paramDrawable)) {}
    while ((paramDrawable == this.mWheelGraphic) || (paramDrawable == this.mUpArrowNormal) || (paramDrawable == this.mUpArrowPressed) || (paramDrawable == this.mDownArrowNormal) || (paramDrawable == this.mDownArrowPressed) || (paramDrawable == this.mLeftButtonPressed) || (paramDrawable == this.mLeftButtonToggled) || (paramDrawable == this.mRightButtonPressed) || (paramDrawable == this.mRightButtonToggled)) {
      return true;
    }
    return false;
  }

  public static class FilterWheelItem
  {
    private final Bitmap bitmapNormal;
    private final Bitmap bitmapSelected;
    private final String text;
    private final Type type;

    private FilterWheelItem(Type paramType, String paramString, Bitmap paramBitmap1, Bitmap paramBitmap2)
    {
      this.type = paramType;
      this.text = paramString;
      this.bitmapNormal = paramBitmap1;
      this.bitmapSelected = paramBitmap2;
    }

    public static FilterWheelItem makeBitmapItem(Bitmap paramBitmap1, Bitmap paramBitmap2)
    {
      return new FilterWheelItem(Type.BITMAP, "", paramBitmap1, paramBitmap2);
    }

    public static FilterWheelItem makeTextItem(String paramString)
    {
      return new FilterWheelItem(Type.TEXT, paramString, null, null);
    }

    public static enum Type
    {
      BITMAP,  TEXT;
    }
  }

  private static class ItemView
  {
    public float angleFromCenter;
    public boolean isSelected;
    public FilterWheel.FilterWheelItem item;
    public int x;
    public float y;
  }

  private class ItemsGallery
  {
    private ArrayList<FilterWheel.FilterWheelItem> mItems;
    private int mObtainIndex = 0;
    private int mSelectedIndex = -1;
    private final ArrayList<FilterWheel.ItemView> mViews = new ArrayList();

    private ItemsGallery() {}

    public void beginLayout()
    {
      this.mObtainIndex = 0;
    }

    public void endLayout()
    {
      if (this.mObtainIndex != this.mViews.size()) {
        throw new IllegalStateException("We don't support variable numbers of items being drawn currently");
      }
    }

    public FilterWheel.FilterWheelItem getItem(int paramInt)
    {
      return (FilterWheel.FilterWheelItem)this.mItems.get(paramInt);
    }

    public int getItemCount()
    {
      return this.mItems.size();
    }

    public int getRelativeItemIndex(int paramInt1, int paramInt2)
    {
      int i = (paramInt1 + paramInt2) % this.mItems.size();
      if (i < 0) {
        i += this.mItems.size();
      }
      return i;
    }

    public int getSelectedIndex()
    {
      return this.mSelectedIndex;
    }

    public FilterWheel.ItemView getViewAt(int paramInt)
    {
      return (FilterWheel.ItemView)this.mViews.get(paramInt);
    }

    public int getViewCount()
    {
      return this.mViews.size();
    }

    public boolean isEmpty()
    {
      return this.mItems.isEmpty();
    }

    public FilterWheel.ItemView obtainView()
    {
      if (this.mObtainIndex == this.mViews.size()) {
        this.mViews.add(new FilterWheel.ItemView());
      }
      ArrayList localArrayList = this.mViews;
      int i = this.mObtainIndex;
      this.mObtainIndex = (i + 1);
      return (FilterWheel.ItemView)localArrayList.get(i);
    }

    public void setItems(ArrayList<FilterWheel.FilterWheelItem> arraylist, boolean flag)
    {
        mItems = arraylist;
        int i = arraylist.size();
        int j = mSelectedIndex;
        int k = -1;
        if(flag)
            if(j >= i)
                k = i;
            else
                k = j;
        if(k == -1)
            if(i == 0)
                k = -1;
            else
                k = 0;
        mSelectedIndex = k;
    }

    public void setSelectedIndex(int paramInt)
    {
      this.mSelectedIndex = paramInt;
    }
  }

  public static abstract interface OnFilterEventListener
  {
    public abstract void onCollapse();

    public abstract void onExpand();

    public abstract void onLeftItemSelectionChange(int paramInt);

    public abstract void onLeftSelected();

    public abstract void onRightItemSelectionChange(int paramInt);

    public abstract void onRightSelected();
  }
}



