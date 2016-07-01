package com.pointinside.android.api.content;

import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.BaseColumns;
import com.pointinside.android.api.PIMapReference;
import com.pointinside.android.api.net.MyHttpClient;

public final class Downloads
{
  public static final String ACTION_CANCEL_DOWNLOAD = "pointinside.intent.action.DOWNLOAD_CANCEL";
  public static final String ACTION_DOWNLOAD_COMPLETE = "pointinside.intent.action.DOWNLOAD_COMPLETE";
  public static final String ACTION_NEW_DOWNLOAD = "pointinside.intent.action.DOWNLOAD_NEW";
  public static final String ACTION_RETRY_DOWNLOAD = "pointinside.intent.action.DOWNLOAD_WAKEUP";
  public static final Uri CONTENT_URI = Uri.parse("content://com.pointinside.android.api.content/downloads");
  public static final int CONTROL_PAUSED = 1;
  public static final int CONTROL_RUN = 0;
  public static final int FILE_EXTRACTION_ERROR = 3;
  public static final int FILE_STATUS_EXTRACTED = 1;
  public static final int FILE_STATUS_EXTRACTING = 2;
  public static final int FILE_STATUS_WAITING = 0;
  public static final int STATUS_BAD_REQUEST = 400;
  public static final int STATUS_CANCELED = 490;
  public static final int STATUS_FILE_ERROR = 492;
  public static final int STATUS_HTTP_DATA_ERROR = 495;
  public static final int STATUS_HTTP_EXCEPTION = 496;
  public static final int STATUS_HTTP_NOT_MODIFIED = 304;
  public static final int STATUS_LENGTH_REQUIRED = 411;
  public static final int STATUS_NOT_ACCEPTABLE = 406;
  public static final int STATUS_PENDING = 190;
  public static final int STATUS_PENDING_PAUSED = 191;
  public static final int STATUS_PRECONDITION_FAILED = 412;
  public static final int STATUS_RUNNING = 192;
  public static final int STATUS_RUNNING_PAUSED = 193;
  public static final int STATUS_SUCCESS = 200;
  public static final int STATUS_TOO_MANY_REDIRECTS = 497;
  public static final int STATUS_UNHANDLED_HTTP_CODE = 494;
  public static final int STATUS_UNHANDLED_REDIRECT = 493;
  public static final int STATUS_UNKNOWN_ERROR = 491;
  static final String STORE = "downloads";

  public static Uri getDownloadUri(long paramLong)
  {
    return CONTENT_URI.buildUpon().appendPath(String.valueOf(paramLong)).build();
  }

  public static boolean isStatusClientError(int paramInt)
  {
    return (paramInt >= 400) && (paramInt < 500);
  }

  public static boolean isStatusCompleted(int paramInt)
  {
    return ((paramInt >= 200) && (paramInt < 300)) || ((paramInt >= 400) && (paramInt < 600)) || (paramInt == 304);
  }

  public static boolean isStatusError(int paramInt)
  {
    return (paramInt >= 400) && (paramInt < 600);
  }

  public static boolean isStatusInformational(int paramInt)
  {
    return (paramInt >= 100) && (paramInt < 200);
  }

  public static boolean isStatusServerError(int paramInt)
  {
    return (paramInt >= 500) && (paramInt < 600);
  }

  public static boolean isStatusSuccess(int paramInt)
  {
    return ((paramInt >= 200) && (paramInt < 300)) || (paramInt == 304);
  }

  public static boolean isStatusSuspended(int paramInt)
  {
    return (paramInt == 191) || (paramInt == 193);
  }

  public static boolean shouldUpdate(int paramInt)
  {
    return (paramInt != 200) && (paramInt != 192);
  }

  public static abstract interface DownloadColumns
    extends BaseColumns
  {
    public static final String COLUMN_CACHING_IDENTIFIER = "download_identifier";
    public static final String COLUMN_CONTENT_TYPE = "content_type";
    public static final String COLUMN_CONTROL = "control";
    public static final String COLUMN_CURRENT_BYTES = "current_bytes";
    public static final String COLUMN_FILE_EXTRACTED = "extracted";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_FILE_PURGED = "purged";
    public static final String COLUMN_LAST_MODIFICATION = "lastmod";
    public static final String COLUMN_REMOTE_URI = "remote_uri";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TOTAL_BYTES = "total_bytes";
    public static final String COLUMN_UPDATED_IDENTIFIER = "updated_identifier";
    public static final String COLUMN_VENUE_UUID = "venue_uuid";
  }

  public static class DownloadConstants
  {
    private static final Uri BASE_URI;
    static
    {
      BASE_URI = PIMapReference.getBaseUri();
      REFERENCE_URI = BASE_URI.buildUpon().appendPath("datasets").appendPath("reference.zip").build();
      DATASET_URI = BASE_URI.buildUpon().appendPath("datasets").build();
      ZONE_IMAGES_URI = BASE_URI.buildUpon().appendPath("images").appendPath("zones").build();
      PDEMAP_URI = BASE_URI.buildUpon().appendPath("datasets").build();
      VENUE_IMAGE_URI = BASE_URI.buildUpon().appendPath("images").appendPath("venues").build();
      PLACE_IMAGES_URI = BASE_URI.buildUpon().appendPath("images").appendPath("places").build();
      PROMOTION_IMAGES_URI = BASE_URI.buildUpon().appendPath("images").appendPath("promotions").build();
    }

    static final int BUFFER_SIZE = 4096;
    private static final String DATASET_PATH = "datasets";
    static final Uri DATASET_URI;
    static final String DEFAULT_DL_SUBDIR = "/PI";
    public static final String DEFAULT_USER_AGENT = "";
    static final String EXTRA_DOWNLOAD_ID = "download_id";
    static final String EXTRA_VENUE_CODE = "venue_code";
    public static final String FAILED_CONNECTIONS = "numfailed";
    private static final String FEEDBACK_PATH = "feedback";
    static final Uri FEEDBACK_URI = BASE_URI.buildUpon().appendPath("feedback").build();
    private static final String IMAGES_PATH = "images";
    static final String KNOWN_SPURIOUS_FILENAME = "lost+found";
    static final int MAX_REDIRECTS = 5;
    static final int MAX_RETRIES = 1;
    static final int MAX_RETRY_AFTER = 43200;
    static final int MIN_PROGRESS_STEP = 500;
    static final long MIN_PROGRESS_TIME = 500L;
    static final int MIN_RETRY_AFTER = 30;
    private static final String PDEMAP_PATH = "pdemap";
    static final Uri PDEMAP_URI;
    private static final String PLACES_PATH = "places";
    static final Uri PLACE_IMAGES_URI;
    private static final String PROMOTIONS_PATH = "promotions";
    static final Uri PROMOTION_IMAGES_URI;
    static final String RECOVERY_DIRECTORY = "recovery";
    static final Uri REFERENCE_URI;
    private static final String REFERNECE_ZIP_NAME = "reference.zip";
    public static final String RETRY_AFTER_X_REDIRECT_COUNT = "retry_after";
    static final int RETRY_FIRST_DELAY = 2;
    private static final String TOUCHSTREAM_PATH = "touchstream";
    static final Uri TOUCHSTREAM_URI = BASE_URI.buildUpon().appendPath("touchstream").build();
    private static final String VENUES_PATH = "venues";
    static final Uri VENUE_IMAGE_URI;
    private static final String ZONES_PATH = "zones";
    static final Uri ZONE_IMAGES_URI;


    private static String getUserAgent()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append("PIMapsAPI/").append("1.7.0");
      localStringBuffer.append(" (");
      localStringBuffer.append(MyHttpClient.getHostIdentifierString());
      localStringBuffer.append(")");
      return localStringBuffer.toString();
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.Downloads
 * JD-Core Version:    0.7.0.1
 */