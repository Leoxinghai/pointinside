package com.pointinside.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class CenterWideChildLayout
  extends ViewGroup
{
  public CenterWideChildLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public CenterWideChildLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public CenterWideChildLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    View localView = getChildAt(0);
    int i = getMeasuredWidth();
    int j = localView.getMeasuredWidth();
    int k = localView.getMeasuredHeight();
    int m = (i - j) / 2;
    localView.layout(m, 0, m + j, 0 + k);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (getChildCount() != 1) {
      throw new IllegalStateException("This layout supports only 1 child");
    }
    View localView = getChildAt(0);
    ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
    localView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), getChildMeasureSpec(paramInt2, 0, localLayoutParams.height));
    setMeasuredDimension(resolveSize(localView.getMeasuredWidth(), paramInt1), resolveSize(localView.getMeasuredHeight(), paramInt2));
  }
}

