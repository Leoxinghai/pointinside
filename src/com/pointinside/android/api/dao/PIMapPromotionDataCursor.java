package com.pointinside.android.api.dao;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import com.pointinside.android.api.PIMapVenue;

public class PIMapPromotionDataCursor
  extends AbstractDataCursor
{
  private static final AbstractDataCursor.Creator<PIMapPromotionDataCursor> CREATOR = new AbstractDataCursor.Creator()
  {
    public PIMapPromotionDataCursor init(Cursor paramAnonymousCursor)
    {
      return new PIMapPromotionDataCursor(paramAnonymousCursor);
    }
  };
  private int mColumnActiveFriday;
  private int mColumnActiveMonday;
  private int mColumnActiveSaturday;
  private int mColumnActiveSunday;
  private int mColumnActiveThursday;
  private int mColumnActiveTuesday;
  private int mColumnActiveWednesday;
  private int mColumnCodeId;
  private int mColumnContactInformation;
  private int mColumnDescription;
  private int mColumnDisplayEndDate;
  private int mColumnDisplayStartDate;
  private int mColumnEndDate;
  private int mColumnFrequency;
  private int mColumnImageId;
  private int mColumnLocation;
  private int mColumnOtherInformation;
  private int mColumnPromotionTypeId;
  private int mColumnStartDate;
  private int mColumnTitle;

  protected PIMapPromotionDataCursor(Cursor paramCursor)
  {
    super(paramCursor);
    this.mColumnTitle = paramCursor.getColumnIndex("title");
    this.mColumnDescription = paramCursor.getColumnIndex("description");
    this.mColumnPromotionTypeId = paramCursor.getColumnIndex("promotion_type_id");
    this.mColumnImageId = paramCursor.getColumnIndex("image_id");
    this.mColumnCodeId = paramCursor.getColumnIndex("code_id");
    this.mColumnFrequency = paramCursor.getColumnIndex("frequency");
    this.mColumnDisplayStartDate = paramCursor.getColumnIndex("display_start_datetime");
    this.mColumnDisplayEndDate = paramCursor.getColumnIndex("display_end_datetime");
    this.mColumnStartDate = paramCursor.getColumnIndex("start_datetime");
    this.mColumnEndDate = paramCursor.getColumnIndex("end_datetime");
    this.mColumnActiveSunday = paramCursor.getColumnIndex("active_sunday");
    this.mColumnActiveMonday = paramCursor.getColumnIndex("active_monday");
    this.mColumnActiveTuesday = paramCursor.getColumnIndex("active_tuesday");
    this.mColumnActiveWednesday = paramCursor.getColumnIndex("active_wednesday");
    this.mColumnActiveThursday = paramCursor.getColumnIndex("active_thursday");
    this.mColumnActiveFriday = paramCursor.getColumnIndex("active_friday");
    this.mColumnActiveSaturday = paramCursor.getColumnIndex("active_saturday");
    this.mColumnLocation = paramCursor.getColumnIndex("location");
    this.mColumnContactInformation = paramCursor.getColumnIndex("contact_information");
    this.mColumnOtherInformation = paramCursor.getColumnIndex("other_information");
  }

  public static PIMapPromotionDataCursor getInstance(Cursor paramCursor)
  {
    return (PIMapPromotionDataCursor)CREATOR.newInstance(paramCursor);
  }

  public static PIMapPromotionDataCursor getInstance(PIMapDataset paramPIMapDataset, Uri paramUri)
  {
    return (PIMapPromotionDataCursor)CREATOR.newInstance(paramPIMapDataset, paramUri);
  }

  public int getActiveFriday()
  {
    return this.mCursor.getInt(this.mColumnActiveFriday);
  }

  public int getActiveMonday()
  {
    return this.mCursor.getInt(this.mColumnActiveMonday);
  }

  public int getActiveSaturday()
  {
    return this.mCursor.getInt(this.mColumnActiveSaturday);
  }

  public int getActiveSunday()
  {
    return this.mCursor.getInt(this.mColumnActiveSunday);
  }

  public int getActiveThursday()
  {
    return this.mCursor.getInt(this.mColumnActiveThursday);
  }

  public int getActiveTuesday()
  {
    return this.mCursor.getInt(this.mColumnActiveTuesday);
  }

  public int getActiveWednesday()
  {
    return this.mCursor.getInt(this.mColumnActiveWednesday);
  }

  public String getCodeId()
  {
    return this.mCursor.getString(this.mColumnCodeId);
  }

  public String getContactInformation()
  {
    return this.mCursor.getString(this.mColumnContactInformation);
  }

  public String getDescription()
  {
    return this.mCursor.getString(this.mColumnDescription);
  }

  public String getDisplayEndDate()
  {
    return this.mCursor.getString(this.mColumnDisplayEndDate);
  }

  public String getDisplayStartDate()
  {
    return this.mCursor.getString(this.mColumnDisplayStartDate);
  }

  public String getEndDate()
  {
    return this.mCursor.getString(this.mColumnEndDate);
  }

  public String getFrequency()
  {
    return this.mCursor.getString(this.mColumnFrequency);
  }

  public Bitmap getImage(PIMapVenue paramPIMapVenue)
  {
    if (paramPIMapVenue != null) {
      return paramPIMapVenue.getPromoImage(getImageName().longValue());
    }
    return null;
  }

  public Long getImageName()
  {
    return Long.valueOf(this.mCursor.getLong(this.mColumnImageId));
  }

  public String getLocation()
  {
    return this.mCursor.getString(this.mColumnLocation);
  }

  public String getOtherInformation()
  {
    return this.mCursor.getString(this.mColumnOtherInformation);
  }

  public int getPromotionTypeId()
  {
    return this.mCursor.getInt(this.mColumnPromotionTypeId);
  }

  public String getStartDate()
  {
    return this.mCursor.getString(this.mColumnStartDate);
  }

  public String getTitle()
  {
    return this.mCursor.getString(this.mColumnTitle);
  }

  public Uri getUri()
  {
    return PIVenue.getMapPromotionUri(getId());
  }

  public boolean isActiveOn(int paramInt)
  {
    int i = 1;
    boolean flag = true;
    switch (paramInt)
    {
    case 1:
      if(getActiveSunday() != 1)
          return false;
      break;
    case 2:
      if(getActiveMonday() != 1)
          return false;
      break;
    case 3:
      if(getActiveTuesday() != 1)
          return false;
      break; /* Loop/switch isn't completed */
    case 4:
      if(getActiveWednesday() != 1)
          return false;
      break;
    case 5:
      if(getActiveThursday() != 1)
          return false;
      break; /* Loop/switch isn't completed */
    case 6:
      if(getActiveFriday() != 1)
          return false;
      break; /* Loop/switch isn't completed */
    case 7:
      if(getActiveSaturday() != 1)
          return false;
      break;
    default:
        i = 0;
        return false;
    }
    return true;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PIMapPromotionDataCursor
 * JD-Core Version:    0.7.0.1
 */