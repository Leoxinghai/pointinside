package com.pointinside.android.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.pointinside.android.app.R;

public class DealBar
  extends RelativeLayout
{
  private View mButtonContainer;
  private int mCheckedButtonId;
  private Animation mFadeIn;
  private Animation mFadeOut;
  private View.OnClickListener mListBtnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (!DealBar.this.mListButton.isChecked()) {
        DealBar.this.setChecked(2131623951);
      }
    }
  };
  private CheckableImageView mListButton;
  private View.OnClickListener mMapBtnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (!DealBar.this.mMapButton.isChecked()) {
        DealBar.this.setChecked(2131623950);
      }
    }
  };
  private CheckableImageView mMapButton;
  private ProgressBar mProgressBar;
  private ToggleViewListener mToggleViewListener;

  public DealBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public DealBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public int getChecked()
  {
    return this.mCheckedButtonId;
  }

  public void hide()
  {
    if (getVisibility() == 0)
    {
      startAnimation(this.mFadeOut);
      setVisibility(8);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mFadeIn = AnimationUtils.loadAnimation(getContext(), 2130968581);
    this.mFadeOut = AnimationUtils.loadAnimation(getContext(), 2130968582);
    this.mProgressBar = ((ProgressBar)findViewById(2131623952));
    this.mButtonContainer = findViewById(2131623949);
    this.mMapButton = ((CheckableImageView)findViewById(2131623950));
    this.mMapButton.setOnClickListener(this.mMapBtnClickListener);
    this.mListButton = ((CheckableImageView)findViewById(2131623951));
    this.mListButton.setOnClickListener(this.mListBtnClickListener);
  }

  public void setBackgroundColor(int paramInt)
  {
    this.mButtonContainer.setBackgroundColor(paramInt);
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    this.mButtonContainer.setBackgroundDrawable(paramDrawable);
  }

  public void setChecked(int i)
  {
      mCheckedButtonId = i;
      switch(i) {
//      JVM INSTR tableswitch 2131623951 2131623951: default 24
  //                   2131623951 59;
//         goto _L1 _L2
default:
      mMapButton.setChecked(true);
      mListButton.setChecked(false);
      if(mToggleViewListener != null)
          mToggleViewListener.onToggle(0x7f0e000e);
      break;
case R.id.toggle_btn_list:
      mMapButton.setChecked(false);
      mListButton.setChecked(true);
      if(mToggleViewListener != null)
      {
          mToggleViewListener.onToggle(0x7f0e000f);
          return;
      }
      break;
      }
  }
  public void setToggleViewListener(ToggleViewListener paramToggleViewListener)
  {
    this.mToggleViewListener = paramToggleViewListener;
  }

  public void show()
  {
    if (getVisibility() != 0)
    {
      setVisibility(0);
      startAnimation(this.mFadeIn);
    }
  }

  public void showProgress(boolean paramBoolean)
  {
    ProgressBar localProgressBar = this.mProgressBar;
    if (paramBoolean) {}
    for (int i = 0;; i = 4)
    {
      localProgressBar.setVisibility(i);
      return;
    }
  }

  public static abstract interface ToggleViewListener
  {
    public abstract void onToggle(int paramInt);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.widget.DealBar
 * JD-Core Version:    0.7.0.1
 */