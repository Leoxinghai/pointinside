package com.pointinside.android.app.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pointinside.android.piwebservices.util.BuildUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class WhatsNewHelper
{
  private static final String KEY_SHOWED_FOR_VERSION = "showed-for-version";
  private static final String TAG = WhatsNewHelper.class.getSimpleName();

  private static void bindItem(View paramView, CharSequence paramCharSequence, Drawable paramDrawable)
  {
    ((ImageView)paramView.findViewById(2131624047)).setImageDrawable(paramDrawable);
    ((TextView)paramView.findViewById(2131624049)).setText(paramCharSequence);
  }

  private static ArrayList<View> buildMessageItems(Context paramContext, ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    Resources localResources = paramContext.getResources();
    boolean bool = HolidayGameCache.getInstance(paramContext).shouldShowGame();
    int i = localResources.getInteger(2131165185);
    CharSequence[] arrayOfCharSequence = localResources.getTextArray(2131492864);
    TypedArray localTypedArray = localResources.obtainTypedArray(2131492865);
    ArrayList localArrayList = new ArrayList();
    int j = arrayOfCharSequence.length;
    for (int k = 0;; k++)
    {
      if (k >= j)
      {
        localTypedArray.recycle();
        return localArrayList;
      }
      if ((bool) || (i != k))
      {
        View localView = localLayoutInflater.inflate(2130903091, paramViewGroup, false);
        bindItem(localView, arrayOfCharSequence[k], localTypedArray.getDrawable(k));
        localArrayList.add(localView);
      }
    }
  }

  public static Dialog createDialog(Context paramContext)
  {
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
    SpannableString localSpannableString = new SpannableString(paramContext.getText(2131099821));
    localSpannableString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, localSpannableString.length(), 0);
    localBuilder.setTitle(localSpannableString);
    View localView = localLayoutInflater.inflate(2130903090, null);
    populateMessage(localView);
    localBuilder.setView(localView);
    localBuilder.setPositiveButton(17039370, null);
    return localBuilder.create();
  }

  public static boolean getAndSetShouldShow(Context paramContext)
  {
    SharedPreferences localSharedPreferences = getPrefs(paramContext);
    String str1 = localSharedPreferences.getString("showed-for-version", null);
    String str2 = BuildUtils.getAppVersionLabel(paramContext);
    if ((somethingToDisplay(paramContext)) && ((str1 == null) || (!str1.equals(str2)))) {}
    for (boolean bool = true;; bool = false)
    {
      if (bool) {
        localSharedPreferences.edit().putString("showed-for-version", str2).commit();
      }
      return bool;
    }
  }

  private static SharedPreferences getPrefs(Context paramContext)
  {
    return paramContext.getSharedPreferences(TAG, 0);
  }

  private static void populateMessage(View paramView)
  {
    ViewGroup localViewGroup = (ViewGroup)paramView.findViewById(2131624048);
    Iterator localIterator = buildMessageItems(paramView.getContext(), localViewGroup).iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      localViewGroup.addView((View)localIterator.next());
    }
  }

  private static boolean somethingToDisplay(Context paramContext)
  {
    return !buildMessageItems(paramContext, null).isEmpty();
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.app.util.WhatsNewHelper
 * JD-Core Version:    0.7.0.1
 */