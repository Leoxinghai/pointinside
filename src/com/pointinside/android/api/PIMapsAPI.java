package com.pointinside.android.api;

import android.net.Uri;

public abstract interface PIMapsAPI
{
  public static final boolean DEBUG = false;
  public static final String DIR_PLACE_IMAGE = "place_image";
  public static final String DIR_PROMO_IMAGE = "promo_image";
  public static final String DIR_VENUE_IMAGE = "venue_image";
  public static final String DIR_VENUE_PDEMAP = "venue_pdemap";
  public static final String DIR_ZONE_IMAGE = "zone_images";
  public static final boolean INCLUDE_ILC = true;
  public static final Uri ROOT_HOST = Uri.parse("https://smartmaps.pointinside.com/android/v2_0");
  public static final String TAG = "PIMaps";
}

