package com.pointinside.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.pointinside.android.app.R;

public class NavigationBar
  extends FrameLayout
{
  private final TextView mDistance;
  private final View mExitButton;
  private final TextView mTitle;

  public NavigationBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public NavigationBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, R.attr.navBarStyle);
  }

  public NavigationBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    LayoutInflater.from(paramContext).inflate(R.layout.nav_bar, this);
    this.mTitle = ((TextView)findViewById(R.id.title));
    this.mDistance = ((TextView)findViewById(R.id.distance));
    this.mExitButton = findViewById(R.id.exit);
  }

  public void setDistance(CharSequence paramCharSequence)
  {
    this.mDistance.setText(paramCharSequence);
  }

  public void setOnExitClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mExitButton.setOnClickListener(paramOnClickListener);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitle.setText(paramCharSequence);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.NavigationBar
 * JD-Core Version:    0.7.0.1
 */