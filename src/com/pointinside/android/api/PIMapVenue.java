package com.pointinside.android.api;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import com.pointinside.android.api.content.Downloads;
import com.pointinside.android.api.content.PIContentManager;
import com.pointinside.android.api.dao.PIDownloadDataCursor;
import com.pointinside.android.api.dao.PIFileDataCursor;
import com.pointinside.android.api.dao.PIMapAddressDataCursor;
import com.pointinside.android.api.dao.PIMapAreaDataCursor;
import com.pointinside.android.api.dao.PIMapDealDataCursor;
import com.pointinside.android.api.dao.PIMapEventDataCursor;
import com.pointinside.android.api.dao.PIMapImageDataCursor;
import com.pointinside.android.api.dao.PIMapItemDataCursor;
import com.pointinside.android.api.dao.PIMapItemDataCursor.PIMapItem;
import com.pointinside.android.api.dao.PIMapOperationMinutesDataCursor;
import com.pointinside.android.api.dao.PIMapPlaceCategoriesDataCursor;
import com.pointinside.android.api.dao.PIMapPlaceDataCursor;
import com.pointinside.android.api.dao.PIMapPlacesWithCategoryDataCursor;
import com.pointinside.android.api.dao.PIMapPolygonZoneDataCursor;
import com.pointinside.android.api.dao.PIMapPromotionDataCursor;
import com.pointinside.android.api.dao.PIMapServiceDataCursor;
import com.pointinside.android.api.dao.PIMapServiceGroupedDataCursor;
import com.pointinside.android.api.dao.PIMapVenueDataCursor;
//import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor.PIMapVenueSummary;
import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;
import com.pointinside.android.api.dao.PIMapWormholeDataCursor;
import com.pointinside.android.api.dao.PIMapZoneDataCursor;
import com.pointinside.android.api.dao.PISQLiteHelper;
import com.pointinside.android.api.dao.PIVenue;
import com.pointinside.android.api.dao.PIVenueDataset;
import com.pointinside.android.api.dao.PixelCoordinateDataCursor;
import java.io.File;

public class PIMapVenue
{
  private static final String TAG = "PIMapVenue";
  private static DatasetDownloadObserver sDatasetObserver;
  private static PISQLiteHelper sPISQLiteHelper;
  private static PDEMapDownloadObserver sPdemapObserver;
  private static PlaceImageDownloadObserver sPlaceImageObserver;
  private static PromoImageDownloadObserver sPromoImageObserver;
  private static VenueImageDownloadObserver sVenueImageObserver;
  private static ZoneImageDownloadObserver sZoneImageObserver;
  private Context mContext;
  private DefaultDownloadObserver mDefaultDownloadObserver = new DefaultDownloadObserver();
  private boolean mDownloadPlaceImages = false;
  private boolean mDownloadPromoImages = false;
  private boolean mDownloadVenueImages = false;
  private final Handler mHandler;
  private volatile boolean mIsLoaded = false;
  private PIMapVenueSummaryDataCursor.PIMapVenueSummary mPIMapVenueSummary;
  private PIVenueAccess mPIVenueAccess;
  private PIVenueDownloadObserver mPIVenueDownloadObserver;
  private PIVenueContentManager mVenueContentManager;
private PIDownloadDataCursor pidownloaddatacursor1;

  PIMapVenue(Context paramContext, PIMapVenueSummaryDataCursor.PIMapVenueSummary paramPIMapVenueSummary, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Handler paramHandler)
  {
    if (paramContext == null) {
      throw new IllegalArgumentException("The Context is null.");
    }
    if (paramPIMapVenueSummary == null) {
      throw new IllegalArgumentException("The PIMapVenueSummary is null.");
    }
    this.mHandler = paramHandler;
    this.mContext = paramContext;
    this.mPIMapVenueSummary = paramPIMapVenueSummary;
    this.mDownloadPlaceImages = paramBoolean1;
    this.mDownloadPromoImages = paramBoolean2;
    this.mDownloadVenueImages = paramBoolean3;
    this.mVenueContentManager = new PIVenueContentManager();
    this.mVenueContentManager.validateVenueDownload();
    if ((this.mVenueContentManager.venueDatasetExists()) && (this.mVenueContentManager.venueZoneImagesExists())) {
      loadVenueDataset();
    }
  }

  public static Context getContext()
  {
    return PIMapReference.sPIMapReference.getContext();
  }

  private boolean loadVenueDataset()
  {
      PIFileDataCursor pifiledatacursor;
      mIsLoaded = false;
      pifiledatacursor = mVenueContentManager.getVenueFileItem();
      if(pifiledatacursor != null) {
	      if(sPISQLiteHelper == null) {
	    	  sPISQLiteHelper = new PISQLiteHelper(pifiledatacursor.getFileUri());
	      }
	      mPIVenueAccess = new PIVenueAccess(new PIVenueDataset(sPISQLiteHelper));
	      mIsLoaded = true;
	      pifiledatacursor.close();
	      sPISQLiteHelper.close();
	      return mIsLoaded;
      } else {
	      return mIsLoaded;
      }
  }


  private boolean shouldUpdate(int paramInt, String paramString1, String paramString2)
  {
    if (Downloads.shouldUpdate(paramInt)) {}
    while (!paramString1.equals(paramString2)) {
      return true;
    }
    return false;
  }

  public void cancelVenueDownload()
  {
    if (sDatasetObserver != null) {
      sDatasetObserver.cancelDownload();
    }
    if (sZoneImageObserver != null) {
      sZoneImageObserver.cancelDownload();
    }
  }

  public void checkForUpdates(PIVenueDownloadObserver pivenuedownloadobserver)
  {
      PIDownloadDataCursor pidownloaddatacursor;
      if(pivenuedownloadobserver != null)
          mPIVenueDownloadObserver = pivenuedownloadobserver;
      mDefaultDownloadObserver.reset();
      mDefaultDownloadObserver.venue = this;
      pidownloaddatacursor = mVenueContentManager.getVenueDownloadItem();
      if(pidownloaddatacursor != null) {
	      if(shouldUpdate(pidownloaddatacursor.getStatus(), mPIMapVenueSummary.getVenueDatasetMD5(), pidownloaddatacursor.getIdentifier())) {
		      sDatasetObserver = new DatasetDownloadObserver(mHandler, pidownloaddatacursor.getUri());
		      mContext.getContentResolver().registerContentObserver(pidownloaddatacursor.getUri(), false, sDatasetObserver);
		      mDefaultDownloadObserver.datasetNeedsUpdate();
		      mVenueContentManager.updateVenueDataset();
	      } else
	    	  mDefaultDownloadObserver.datasetReady();
	      pidownloaddatacursor.close();
      } else
    	  mDefaultDownloadObserver.datasetFailedWithError(new Exception("Unable to download venue."));

      PIDownloadDataCursor pidownloaddatacursor1 = mVenueContentManager.getVenueZoneImagesItem();
	  if(pidownloaddatacursor1 != null) {
	      if(shouldUpdate(pidownloaddatacursor1.getStatus(), mPIMapVenueSummary.getVenueDatasetMD5(), pidownloaddatacursor1.getIdentifier())) {
		      sZoneImageObserver = new ZoneImageDownloadObserver(mHandler, pidownloaddatacursor1.getUri());
		      mContext.getContentResolver().registerContentObserver(pidownloaddatacursor1.getUri(), false, sZoneImageObserver);
		      mDefaultDownloadObserver.mapImagesNeedsUpdate();
		      mVenueContentManager.updateVenueZoneImages();
	      } else
	          mDefaultDownloadObserver.mapImagesReady();

	      pidownloaddatacursor1.close();
	  } else
		  mDefaultDownloadObserver.mapImagesFailedWithError(new Exception("Unable to download venue map images."));

	  if(mPIMapVenueSummary.getVenuePDEMapFile() != null) {
	      PIDownloadDataCursor pidownloaddatacursor2 = mVenueContentManager.getVenuePDEMapItem();
	      if(pidownloaddatacursor2 != null) {
		      if(shouldUpdate(pidownloaddatacursor2.getStatus(), mPIMapVenueSummary.getVenuePDEMapMD5(), pidownloaddatacursor2.getIdentifier())) {
			      sPdemapObserver = new PDEMapDownloadObserver(mHandler, pidownloaddatacursor2.getUri());
			      mContext.getContentResolver().registerContentObserver(pidownloaddatacursor2.getUri(), false, sPdemapObserver);
			      mDefaultDownloadObserver.pdemapNeedsUpdate();
			      mVenueContentManager.updateVenuePDEMap();
		      }
      		} else
      			mDefaultDownloadObserver.pdemapReady();
	      pidownloaddatacursor2.close();

	   }

  }


  public void checkForUpdates(PIVenueDownloadObserver paramPIVenueDownloadObserver, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.mDownloadPlaceImages = paramBoolean1;
    this.mDownloadPromoImages = paramBoolean2;
    this.mDownloadVenueImages = paramBoolean3;
    checkForUpdates(paramPIVenueDownloadObserver);
  }

  public void close()
  {
    if (this.mPIVenueAccess != null) {
      this.mPIVenueAccess.close();
    }
  }

  void deleteVenueDownload()
  {
    this.mVenueContentManager.deleteVenueDownload();
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    String str;
      if ((paramObject == null) || (getClass() != paramObject.getClass())) {
        return false;
      }
      str = (String)paramObject;
      if (getVenueUUID() == null) {
        return false;
      }
    if(getVenueUUID().equals(str)){
    	return true;
    }
    return false;
  }

  public PIMapAddressDataCursor getAddress(long paramLong)
  {
    return this.mPIVenueAccess.getAddress(paramLong);
  }

  public PIMapAddressDataCursor getAddresses()
  {
    return this.mPIVenueAccess.getAddresses();
  }

  public PIMapPlaceDataCursor getAllMapPlaces()
  {
    return this.mPIVenueAccess.getAllMapPlaces();
  }

  public int getAllPromotionsCount()
  {
    return this.mPIVenueAccess.getAllPromotionsCount();
  }

  public PIMapPlaceCategoriesDataCursor getCategoriesForPlace(long paramLong)
  {
    return this.mPIVenueAccess.getCategoriesForPlace(paramLong);
  }

  public int getDealCount()
  {
    return this.mPIVenueAccess.getDealCount();
  }

  public PIMapDealDataCursor getDeals()
  {
    return this.mPIVenueAccess.getDeals();
  }

  public PIMapDealDataCursor getDealsForPlace(long paramLong)
  {
    return this.mPIVenueAccess.getDealsForPlace(paramLong);
  }

  public PIMapDealDataCursor getDealsForPlace(boolean paramBoolean, long paramLong)
  {
    return this.mPIVenueAccess.getDealsForPlace(paramBoolean, paramLong);
  }

  public int getEventCount()
  {
    return this.mPIVenueAccess.getEventCount();
  }

  public PIMapPromotionDataCursor getEvents()
  {
    return this.mPIVenueAccess.getEvents();
  }

  public PIMapEventDataCursor getEventsForPlace(long paramLong)
  {
    return this.mPIVenueAccess.getEventsForPlace(paramLong);
  }

  public PIMapEventDataCursor getEventsForPlace(boolean paramBoolean, long paramLong)
  {
    return this.mPIVenueAccess.getEventsForPlace(paramBoolean, paramLong);
  }

  public int getIndexOfZoneWithUUID(String paramString)
  {
    PIMapZoneDataCursor localPIMapZoneDataCursor = getVenueZones();
    try
    {
      boolean bool;
      do
      {
        if (localPIMapZoneDataCursor.getZoneUUID().equals(paramString))
        {
          int i = localPIMapZoneDataCursor.getZoneIndex();
          return i;
        }
        bool = localPIMapZoneDataCursor.moveToNext();
      } while (bool);
      return -1;
    }
    finally
    {
      localPIMapZoneDataCursor.close();
    }
  }

  public Location getLocation()
  {
    if (this.mPIMapVenueSummary != null)
    {
      Location localLocation = new Location("PIMapVenue");
      localLocation.setLatitude(this.mPIMapVenueSummary.getLatitude());
      localLocation.setLongitude(this.mPIMapVenueSummary.getLongitude());
      return localLocation;
    }
    return null;
  }

  public PIMapDealDataCursor getMapDealSearchForName(String paramString)
  {
    return this.mPIVenueAccess.getMapDealSearchForName(paramString);
  }

  public PIMapPromotionDataCursor getMapEventSearchForName(String paramString)
  {
    return this.mPIVenueAccess.getMapEventSearchForName(paramString);
  }

  public PIMapItemDataCursor getMapItem(long paramLong)
  {
    return this.mPIVenueAccess.getMapItem(paramLong);
  }

  public PIMapItemDataCursor getMapItemForIlcCode(String paramString)
  {
    return this.mPIVenueAccess.getMapItemForIlcCode(paramString);
  }

  public PIMapItemDataCursor.PIMapItem getMapItemForPromotion(long paramLong)
  {
    PIMapItemDataCursor localPIMapItemDataCursor = getMapPlaceForPromotion(paramLong);
    if (localPIMapItemDataCursor != null) {
      try
      {
        PIMapItemDataCursor.PIMapItem localPIMapItem = localPIMapItemDataCursor.getPIMapItem();
        return localPIMapItem;
      }
      finally
      {
        localPIMapItemDataCursor.close();
      }
    }
    return null;
  }

  public PIMapItemDataCursor getMapItemForUUID(String paramString)
  {
    return this.mPIVenueAccess.getMapItemForUUID(paramString);
  }

  public PIMapItemDataCursor getMapItemNameStartsWith(String paramString)
  {
    return this.mPIVenueAccess.getMapItemNameStartsWith(paramString);
  }

  public PIMapItemDataCursor getMapItemSearchForName(String paramString)
  {
    return this.mPIVenueAccess.getMapItemSearchForName(paramString);
  }

  public PIMapItemDataCursor getMapItems()
  {
    return this.mPIVenueAccess.getMapItems();
  }

  public PIMapPlaceDataCursor getMapPlace(int paramInt)
  {
    return this.mPIVenueAccess.getMapPlace(paramInt);
  }

  public PIMapAreaDataCursor getMapPlaceAreaForPlace(long paramLong)
  {
    return this.mPIVenueAccess.getMapPlaceAreaForPlace(paramLong);
  }

  public PIMapAreaDataCursor getMapPlaceAreas()
  {
    return this.mPIVenueAccess.getMapPlaceAreas();
  }

  public PIMapAreaDataCursor getMapPlaceAreas(long paramLong)
  {
    return this.mPIVenueAccess.getMapPlaceAreas(paramLong);
  }

  public PIMapItemDataCursor getMapPlaceForPromotion(long paramLong)
  {
    return this.mPIVenueAccess.getMapPlaceForPromotion(paramLong);
  }

  public PIMapServiceDataCursor getMapPlaceForServiceTypeUUID(String paramString)
  {
    return this.mPIVenueAccess.getMapPlaceForServiceTypeUUID(paramString);
  }

  public PIMapPlaceDataCursor getMapPlaceForUUID(String paramString)
  {
    return this.mPIVenueAccess.getMapPlaceForUUID(paramString);
  }

  public PIMapPlaceDataCursor getMapPlaceSearchForName(String paramString)
  {
    return this.mPIVenueAccess.getMapPlaceSearchForName(paramString);
  }

  public PIMapPlaceDataCursor getMapPlaces()
  {
    return this.mPIVenueAccess.getMapPlaces();
  }

  public PIMapPlaceDataCursor getMapPlaces(boolean paramBoolean)
  {
    return this.mPIVenueAccess.getMapPlaces(paramBoolean);
  }

  public PIMapPlaceDataCursor getMapPlacesForUUIDs(String... paramVarArgs)
  {
    return this.mPIVenueAccess.getMapPlacesForUUIDs(paramVarArgs);
  }

  public PIMapItemDataCursor getMapPlacesWithPromotion()
  {
    return this.mPIVenueAccess.getMapPlacesWithPromotion();
  }

  public PIMapPromotionDataCursor getMapPromotionSearchForName(String paramString, long... paramVarArgs)
  {
    return this.mPIVenueAccess.getMapPromotionSearchForName(paramString, paramVarArgs);
  }

  public PIMapServiceDataCursor getMapService(String paramString)
  {
    return this.mPIVenueAccess.getMapService(paramString);
  }

  public PIMapServiceDataCursor getMapServiceSearchForName(String paramString)
  {
    return this.mPIVenueAccess.getMapServiceSearchForName(paramString);
  }

  public PIMapServiceGroupedDataCursor getMapServiceSearchForNameGrouped(String paramString)
  {
    return this.mPIVenueAccess.getMapServiceSearchForNameGrouped(paramString);
  }

  public PIMapServiceDataCursor getMapServices()
  {
    return this.mPIVenueAccess.getMapServices();
  }

  public PIMapServiceGroupedDataCursor getMapServicesGrouped()
  {
    return this.mPIVenueAccess.getMapServicesGrouped();
  }

  public PIMapWormholeDataCursor getMapWormhole(long paramLong)
  {
    return this.mPIVenueAccess.getMapWormhole(paramLong);
  }

  public PIMapWormholeDataCursor getMapWormholeForType(long paramLong)
  {
    return this.mPIVenueAccess.getMapWormholeForType(paramLong);
  }

  public PIMapWormholeDataCursor getMapWormholes()
  {
    return this.mPIVenueAccess.getMapWormholes();
  }

  public PIMapOperationMinutesDataCursor getOperationMinutes()
  {
    return this.mPIVenueAccess.getOperationMinutes();
  }

  public PIMapOperationMinutesDataCursor getOperationMinutes(long paramLong)
  {
    return this.mPIVenueAccess.getOperationMinutes(paramLong);
  }

  public File getPDEMapFile()
  {
    PIFileDataCursor localPIFileDataCursor = this.mVenueContentManager.getVenuePDEMapFileItem();
    if (localPIFileDataCursor != null) {
      try
      {
        File localFile = localPIFileDataCursor.getFilePath();
        return localFile;
      }
      finally
      {
        localPIFileDataCursor.close();
      }
    }
    return null;
  }

  public PixelCoordinateDataCursor getPixelCoordinates()
  {
    return this.mPIVenueAccess.getPixelCoordinates();
  }

  public PixelCoordinateDataCursor getPixelCoordinatesForArea(long paramLong)
  {
    return this.mPIVenueAccess.getPixelCoordinatesForArea(paramLong);
  }

  public int getPlaceEventCount()
  {
    return this.mPIVenueAccess.getPlaceEventCount();
  }

  public PIMapEventDataCursor getPlaceEvents()
  {
    return this.mPIVenueAccess.getPlaceEvents();
  }

  public Bitmap getPlaceImage(long paramLong)
  {
    PIMapImageDataCursor localPIMapImageDataCursor = this.mPIVenueAccess.getImage(paramLong);
    if (localPIMapImageDataCursor != null) {
      try
      {
        Bitmap localBitmap = this.mVenueContentManager.getPlaceImageFile(localPIMapImageDataCursor.getImageName());
        return localBitmap;
      }
      finally
      {
        localPIMapImageDataCursor.close();
      }
    }
    return null;
  }

  public Bitmap getPlaceImage(String paramString)
  {
    return this.mVenueContentManager.getPlaceImageFile(paramString);
  }

  public PIMapPlacesWithCategoryDataCursor getPlacesByCategory()
  {
    return this.mPIVenueAccess.getPlacesByCategory();
  }

  public PIMapPolygonZoneDataCursor getPolygonZones()
  {
    return this.mPIVenueAccess.getPolygonZones();
  }

  public Bitmap getPromoImage(long paramLong)
  {
    PIMapImageDataCursor localPIMapImageDataCursor = this.mPIVenueAccess.getImage(paramLong);
    if (localPIMapImageDataCursor != null) {
      try
      {
        Bitmap localBitmap = this.mVenueContentManager.getPromoImageFile(localPIMapImageDataCursor.getImageName());
        return localBitmap;
      }
      finally
      {
        localPIMapImageDataCursor.close();
      }
    }
    return null;
  }

  public Bitmap getPromoImage(String paramString)
  {
    return this.mVenueContentManager.getPromoImageFile(paramString);
  }

  public PIMapPromotionDataCursor getPromotion(long paramLong)
  {
    return this.mPIVenueAccess.getPromotion(paramLong);
  }

  public int getPromotionCount()
  {
    return this.mPIVenueAccess.getPromotionCount();
  }

  public PIMapPromotionDataCursor getPromotions(boolean paramBoolean, long... paramVarArgs)
  {
    return this.mPIVenueAccess.getPromotions(paramBoolean, paramVarArgs);
  }

  public PIMapPromotionDataCursor getPromotions(long... paramVarArgs)
  {
    return this.mPIVenueAccess.getPromotions(paramVarArgs);
  }

  public PIMapVenueDataCursor getVenue()
  {
    return this.mPIVenueAccess.getVenue(getVenueUUID());
  }

  public long getVenueId()
  {
    if (this.mPIMapVenueSummary == null) {
      return 0L;
    }
    return this.mPIMapVenueSummary.getVenueId();
  }

  public Bitmap getVenueImage(long paramLong)
  {
    PIMapImageDataCursor localPIMapImageDataCursor = this.mPIVenueAccess.getImage(paramLong);
    if (localPIMapImageDataCursor != null) {
      try
      {
        Bitmap localBitmap = this.mVenueContentManager.getVenueImageFile(localPIMapImageDataCursor.getImageName());
        return localBitmap;
      }
      finally
      {
        localPIMapImageDataCursor.close();
      }
    }
    return null;
  }

  public Bitmap getVenueImage(String paramString)
  {
    return this.mVenueContentManager.getVenueImageFile(paramString);
  }

  public String getVenueName()
  {
    if (this.mPIMapVenueSummary == null) {
      return null;
    }
    return this.mPIMapVenueSummary.getVenueName();
  }

  public PIMapPromotionDataCursor getVenuePromotion(long paramLong)
  {
    return this.mPIVenueAccess.getVenuePromotion(paramLong);
  }

  public PIMapPromotionDataCursor getVenuePromotions()
  {
    return this.mPIVenueAccess.getVenuePromotions();
  }

  public int getVenueType()
  {
    PIMapVenueDataCursor localPIMapVenueDataCursor = getVenue();
    if (localPIMapVenueDataCursor != null) {
      try
      {
        int i = localPIMapVenueDataCursor.getVenueTypeId();
        return i;
      }
      finally
      {
        localPIMapVenueDataCursor.close();
      }
    }
    return -1;
  }

  public String getVenueUUID()
  {
    if (this.mPIMapVenueSummary == null) {
      return null;
    }
    return this.mPIMapVenueSummary.getVenueUUID();
  }

  public PIMapZoneDataCursor getVenueZone(long paramLong)
  {
    return this.mPIVenueAccess.getVenueZone(paramLong);
  }

  public PIMapZoneDataCursor getVenueZones()
  {
    return this.mPIVenueAccess.getVenueZones();
  }

  public boolean hasOverview()
  {
    return this.mPIVenueAccess.hasOverview();
  }

  public boolean hasPDEMap()
  {
    return getPDEMapFile() != null;
  }

  public int hashCode()
  {
    if (getVenueUUID() != null) {
      return getVenueUUID().hashCode();
    }
    return 0;
  }

  public boolean isLoaded()
  {
    return this.mIsLoaded;
  }

  public boolean isSDCardAvailable()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }

  public boolean isVenueOnSDCard()
  {
    return this.mVenueContentManager.isVenueOnSDCard();
  }

  public void loadOrUpdatePromotionImages(PIVenueDownloadObserver paramPIVenueDownloadObserver)
  {
    if (paramPIVenueDownloadObserver != null) {
      this.mPIVenueDownloadObserver = paramPIVenueDownloadObserver;
    }
    if (this.mPIMapVenueSummary.getVenuePromotionsImagesFile() == null)
    {
      this.mDefaultDownloadObserver.promoImagesReady();
      return;
    }
    PIDownloadDataCursor localPIDownloadDataCursor = this.mVenueContentManager.getPromotionImagesItem();
    if (localPIDownloadDataCursor != null) {
      try
      {
        if (shouldUpdate(localPIDownloadDataCursor.getStatus(), this.mPIMapVenueSummary.getVenueDatasetMD5(), localPIDownloadDataCursor.getIdentifier()))
        {
          sPromoImageObserver = new PromoImageDownloadObserver(this.mHandler, localPIDownloadDataCursor.getUri());
          this.mContext.getContentResolver().registerContentObserver(localPIDownloadDataCursor.getUri(), false, sPromoImageObserver);
          this.mDefaultDownloadObserver.promoImagesNeedsUpdate();
          this.mVenueContentManager.updatePromotionImages();
        }
        this.mDefaultDownloadObserver.promoImagesReady();
        localPIDownloadDataCursor.close();

      } catch(Exception ex) {
          this.mDefaultDownloadObserver.promoImagesFailedWithError(new Exception("Unable to download promotion images."));
      }
    } else
        this.mDefaultDownloadObserver.promoImagesFailedWithError(new Exception("Unable to download promotion images."));


  }

  public void loadOrUpdateVenueImages(PIVenueDownloadObserver paramPIVenueDownloadObserver)
  {
    if (paramPIVenueDownloadObserver != null) {
      this.mPIVenueDownloadObserver = paramPIVenueDownloadObserver;
    }
    if (this.mPIMapVenueSummary.getVenueImagesFile() == null)
    {
      this.mDefaultDownloadObserver.venueImagesReady();
      return;
    }
    PIDownloadDataCursor localPIDownloadDataCursor = this.mVenueContentManager.getVenueImagesItem();
    if (localPIDownloadDataCursor != null) {
      try
      {
        if (shouldUpdate(localPIDownloadDataCursor.getStatus(), this.mPIMapVenueSummary.getVenueDatasetMD5(), localPIDownloadDataCursor.getIdentifier()))
        {
          sVenueImageObserver = new VenueImageDownloadObserver(this.mHandler, localPIDownloadDataCursor.getUri());
          this.mContext.getContentResolver().registerContentObserver(localPIDownloadDataCursor.getUri(), false, sVenueImageObserver);
          this.mDefaultDownloadObserver.venueImagesNeedsUpdate();
          this.mVenueContentManager.updateVenueImages();
        } else
          this.mDefaultDownloadObserver.venueImagesReady();

        localPIDownloadDataCursor.close();
      }
      finally
      {
        localPIDownloadDataCursor.close();
      }
    } else
        this.mDefaultDownloadObserver.venueImagesFailedWithError(new Exception("Unable to download venue images."));

  }

  public void loadOrUpdateVenuePlaceImages(PIVenueDownloadObserver paramPIVenueDownloadObserver)
  {
    if (paramPIVenueDownloadObserver != null) {
      this.mPIVenueDownloadObserver = paramPIVenueDownloadObserver;
    }
    if (this.mPIMapVenueSummary.getVenuePlaceImagesFile() == null)
    {
      this.mDefaultDownloadObserver.placeImagesReady();
      return;
    }
    PIDownloadDataCursor localPIDownloadDataCursor = this.mVenueContentManager.getPlaceImagesItem();
    if (localPIDownloadDataCursor != null) {
      try
      {
        if (shouldUpdate(localPIDownloadDataCursor.getStatus(), this.mPIMapVenueSummary.getVenueDatasetMD5(), localPIDownloadDataCursor.getIdentifier()))
        {
          sPlaceImageObserver = new PlaceImageDownloadObserver(this.mHandler, localPIDownloadDataCursor.getUri());
          this.mContext.getContentResolver().registerContentObserver(localPIDownloadDataCursor.getUri(), false, sPlaceImageObserver);
          this.mDefaultDownloadObserver.placeImagesNeedsUpdate();
          this.mVenueContentManager.updatePlaceImages();
        } else
            this.mDefaultDownloadObserver.placeImagesReady();

        localPIDownloadDataCursor.close();
      }
      finally
      {
        localPIDownloadDataCursor.close();
      }
    } else
        this.mDefaultDownloadObserver.placeImagesFailedWithError(new Exception("Unable to download venue place images."));

  }

  public void setDownloadPlaceImages(boolean paramBoolean)
  {
    this.mDownloadPlaceImages = paramBoolean;
  }

  public void setDownloadPromoImages(boolean paramBoolean)
  {
    this.mDownloadPromoImages = paramBoolean;
  }

  public void setDownloadVenueImages(boolean paramBoolean)
  {
    this.mDownloadVenueImages = paramBoolean;
  }

  public void unRegisterPIVenueDownloadObserver()
  {
    this.mPIVenueDownloadObserver = null;
  }

  private class DatasetDownloadObserver
    extends ContentObserver
  {
    private Uri uri;

    public DatasetDownloadObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
//      PIMapVenue.PIVenueContentManager.access$2(PIMapVenue.this.mVenueContentManager, this.uri);
//      PIMapVenue.this.mDefaultDownloadObserver.datasetDownloadCanceled();

      mVenueContentManager.cancelDownload(uri);
      mDefaultDownloadObserver.datasetDownloadCanceled();
    }

    public void onChange(boolean paramBoolean)
    {
        PIDownloadDataCursor pidownloaddatacursor = mVenueContentManager.getDownloadItem(uri);
        if(pidownloaddatacursor != null) {
	        int i =0 ;
	        mDefaultDownloadObserver.datasetBytesToReceive(pidownloaddatacursor.getTotalBytes());
	        mDefaultDownloadObserver.datasetDataReceived(pidownloaddatacursor.getCurrentBytes());
	        i = pidownloaddatacursor.getStatus();
	        if(Downloads.isStatusCompleted(i)) {
		        if(Downloads.isStatusSuccess(i)) {
			        if(pidownloaddatacursor.isFileExtracted())
			        {
			            mDefaultDownloadObserver.datasetDidUpdate();
			            mDefaultDownloadObserver.datasetReady();
			        }
	        	} else if (Downloads.isStatusError(i)) {
		        	 if(pidownloaddatacursor.isExtractionError()) {
				        	mDefaultDownloadObserver.datasetFailedWithError(new Exception("Unable to extract venue dataset."));
				        	return;
		        	 } else {
		                 mDefaultDownloadObserver.datasetFailedWithError(new Exception((new StringBuilder("Unable to retrieve venue dataset. Response from server: ")).append(i).toString()));
		                 mVenueContentManager.delete(uri);
		        	 }
			    }
	        }
	        pidownloaddatacursor.close();
        }
    }
  }

  private class DefaultDownloadObserver
    extends PIMapVenue.PIVenueDownloadObserver
  {
    private int mDatasetCurrentBytes = 0;
    private boolean mDatasetDidUpdate = false;
    private boolean mDatasetNeedsUpdate = false;
    private boolean mDatasetReady = false;
    private int mDatasetTotalBytes = 0;
    private int mPDEMapCurrentBytes = 0;
    private boolean mPDEMapDidUpdate = false;
    private boolean mPDEMapNeedUpdate = false;
    private boolean mPDEMapReady = false;
    private int mPDEMapTotalBytes = 0;
    private int mPlaceImageCurrentBytes = 0;
    private int mPlaceImageTotalBytes = 0;
    private boolean mPlaceImagesDidUpdate = false;
    private boolean mPlaceImagesNeedUpdate = false;
    private boolean mPlaceImagesReady = false;
    private int mPromoImageCurrentBytes = 0;
    private int mPromoImageTotalBytes = 0;
    private boolean mPromoImagesDidUpdate = false;
    private boolean mPromoImagesNeedUpdate = false;
    private boolean mPromoImagesReady = false;
    private int mVenueImageCurrentBytes = 0;
    private int mVenueImageTotalBytes = 0;
    private boolean mVenueImagesDidUpdate = false;
    private boolean mVenueImagesNeedUpdate = false;
    private boolean mVenueImagesReady = false;
    private int mZoneImageCurrentBytes = 0;
    private int mZoneImageTotalBytes = 0;
    private boolean mZoneImagesDidUpdate = false;
    private boolean mZoneImagesNeedUpdate = false;
    private boolean mZoneImagesReady = false;
    boolean needsUpdate = false;
    private PIMapVenue venue;

    private DefaultDownloadObserver() {}

    private void calculateCurrentBytes()
    {
      dataReceived(this.mDatasetCurrentBytes + this.mZoneImageCurrentBytes + this.mPlaceImageCurrentBytes + this.mPromoImageCurrentBytes + this.mVenueImageCurrentBytes + this.mPDEMapCurrentBytes);
    }

    private void calculateTotalBytes()
    {
      bytesToReceive(this.mDatasetTotalBytes + this.mZoneImageTotalBytes + this.mPlaceImageTotalBytes + this.mPromoImageTotalBytes + this.mVenueImageTotalBytes + this.mPDEMapTotalBytes);
    }

    private void reset()
    {
      this.needsUpdate = false;
      this.mDatasetNeedsUpdate = false;
      this.mDatasetDidUpdate = false;
      this.mZoneImagesNeedUpdate = false;
      this.mZoneImagesDidUpdate = false;
      this.mPromoImagesNeedUpdate = false;
      this.mPromoImagesDidUpdate = false;
      this.mPlaceImagesNeedUpdate = false;
      this.mPlaceImagesDidUpdate = false;
      this.mVenueImagesNeedUpdate = false;
      this.mVenueImagesDidUpdate = false;
      this.mPDEMapNeedUpdate = false;
      this.mPDEMapDidUpdate = false;
      this.mDatasetReady = false;
      this.mZoneImagesReady = false;
      this.mPromoImagesReady = false;
      this.mPlaceImagesReady = false;
      this.mVenueImagesReady = false;
      this.mPDEMapReady = false;
      this.mDatasetCurrentBytes = 0;
      this.mDatasetTotalBytes = 0;
      this.mZoneImageCurrentBytes = 0;
      this.mZoneImageTotalBytes = 0;
      this.mPlaceImageCurrentBytes = 0;
      this.mPlaceImageTotalBytes = 0;
      this.mPromoImageCurrentBytes = 0;
      this.mPromoImageTotalBytes = 0;
      this.mVenueImageCurrentBytes = 0;
      this.mVenueImageTotalBytes = 0;
      this.mPDEMapCurrentBytes = 0;
      this.mPDEMapTotalBytes = 0;
    }

    private void validatePlaceImages()
    {
      if ((this.mPlaceImagesDidUpdate) && (this.mPlaceImagesReady)) {
        placeImagesDidUpdate();
      }
      if (this.mPlaceImagesReady) {
        placeImagesReady();
      }
    }

    private void validatePromotionImages()
    {
      if ((this.mPromoImagesDidUpdate) && (this.mPromoImagesReady)) {
        promoImagesDidUpdate();
      }
      if (this.mPromoImagesReady) {
        promoImagesReady();
      }
    }

    private void validateVenueDownload()
    {
    	/*
      if (((this.mDatasetNeedsUpdate) || (this.mZoneImagesNeedUpdate) || (this.mPDEMapNeedUpdate)) && (!this.needsUpdate))
      {
        venueNeedsUpdate();
        this.needsUpdate = true;
      }
      if (((this.mDatasetDidUpdate) || (this.mZoneImagesDidUpdate) || (this.mPDEMapDidUpdate)) && (this.mDatasetReady) && (this.mZoneImagesReady) && (this.mPDEMapReady)) {
        venueDidUpdate();
      }
      if ((this.mDatasetReady) && (this.mZoneImagesReady) && (this.mPDEMapReady) && (this.venue != null) && (this.venue.loadVenueDataset())) {
        venueReady();
      }
      */
        if (((this.mDatasetNeedsUpdate) || (this.mZoneImagesNeedUpdate)) && (!this.needsUpdate))
        {
          venueNeedsUpdate();
          this.needsUpdate = true;
        }
        if (((this.mDatasetDidUpdate) || (this.mZoneImagesDidUpdate) ) && (this.mDatasetReady) && (this.mZoneImagesReady) && (this.mPDEMapReady)) {
          venueDidUpdate();
        }
        if ((this.mDatasetReady) && (this.mZoneImagesReady) && (this.venue != null) && (this.venue.loadVenueDataset())) {
          venueReady();
        }
    }

    private void validateVenueImages()
    {
      if ((this.mVenueImagesDidUpdate) && (this.mVenueImagesReady)) {
        venueImagesDidUpdate();
      }
      if (this.mVenueImagesReady) {
        venueImagesReady();
      }
    }

    public void bytesToReceive(int paramInt)
    {
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.bytesToReceive(paramInt);
      }
    }

    public void dataReceived(int paramInt)
    {
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.dataReceived(paramInt);
      }
    }

    public void datasetBytesToReceive(int paramInt)
    {
      this.mDatasetTotalBytes = paramInt;
      calculateTotalBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.datasetBytesToReceive(paramInt);
      }
    }

    public void datasetDataReceived(int paramInt)
    {
      this.mDatasetCurrentBytes = paramInt;
      calculateTotalBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.datasetDataReceived(paramInt);
      }
    }

    public void datasetDidUpdate()
    {
      this.mDatasetDidUpdate = true;
      validateVenueDownload();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.datasetDidUpdate();
      }
    }

    public void datasetFailedWithError(Exception paramException)
    {
      super.datasetFailedWithError(paramException);
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.datasetFailedWithError(paramException);
      }
    }

    public void datasetNeedsUpdate()
    {
      this.mDatasetNeedsUpdate = true;
      validateVenueDownload();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.datasetNeedsUpdate();
      }
    }

    public void datasetReady()
    {
      if (!this.mDatasetReady)
      {
        datasetFinished();
        this.mDatasetReady = true;
        validateVenueDownload();
      }
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.datasetReady();
      }
    }

    public void failedWithError(Exception paramException)
    {
      super.failedWithError(paramException);
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.failedWithError(paramException);
      }
    }

    protected void finish()
    {
      super.finish();
      this.venue = null;
    }

    public void mapImagesBytesToReceive(int paramInt)
    {
      this.mZoneImageTotalBytes = paramInt;
      calculateTotalBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.mapImagesBytesToReceive(paramInt);
      }
    }

    public void mapImagesDataReceived(int paramInt)
    {
      this.mZoneImageCurrentBytes = paramInt;
      calculateCurrentBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.mapImagesDataReceived(paramInt);
      }
    }

    public void mapImagesDidUpdate()
    {
      this.mZoneImagesDidUpdate = true;
      validateVenueDownload();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.mapImagesDidUpdate();
      }
    }

    public void mapImagesFailedWithError(Exception paramException)
    {
      super.mapImagesFailedWithError(paramException);
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.mapImagesFailedWithError(paramException);
      }
    }

    public void mapImagesNeedsUpdate()
    {
      this.mZoneImagesNeedUpdate = true;
      validateVenueDownload();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.mapImagesNeedsUpdate();
      }
    }

    public void mapImagesReady()
    {
      if (!this.mZoneImagesReady)
      {
        mapImagesFinished();
        this.mZoneImagesReady = true;
        validateVenueDownload();
      }
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.mapImagesReady();
      }
    }

    public void pdemapBytesToReceive(int paramInt)
    {
      this.mPDEMapTotalBytes = paramInt;
      calculateTotalBytes();
    }

    public void pdemapDataReceived(int paramInt)
    {
      this.mPDEMapCurrentBytes = paramInt;
      calculateTotalBytes();
    }

    public void pdemapDidUpdate()
    {
      this.mPDEMapDidUpdate = true;
      validateVenueDownload();
    }

    public void pdemapFailedWithError(Exception paramException)
    {
      super.pdemapFailedWithError(paramException);
    }

    public void pdemapNeedsUpdate()
    {
      this.mPDEMapNeedUpdate = true;
      validateVenueDownload();
    }

    public void pdemapReady()
    {
      if (!this.mPDEMapReady)
      {
        pdemapFinished();
        this.mPDEMapReady = true;
        validateVenueDownload();
      }
    }

    public void placeImagesBytesToReceive(int paramInt)
    {
      this.mPlaceImageTotalBytes = paramInt;
      calculateTotalBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.placeImagesBytesToReceive(paramInt);
      }
    }

    public void placeImagesDataReceived(int paramInt)
    {
      this.mPlaceImageCurrentBytes = paramInt;
      calculateCurrentBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.placeImagesDataReceived(paramInt);
      }
    }

    public void placeImagesDidUpdate()
    {
      this.mPlaceImagesDidUpdate = true;
      validatePlaceImages();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.placeImagesDidUpdate();
      }
    }

    public void placeImagesFailedWithError(Exception paramException)
    {
      super.placeImagesFailedWithError(paramException);
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.placeImagesFailedWithError(paramException);
      }
    }

    public void placeImagesNeedsUpdate()
    {
      this.mPlaceImagesNeedUpdate = true;
      validatePlaceImages();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.placeImagesNeedsUpdate();
      }
    }

    public void placeImagesReady()
    {
      if (!this.mPlaceImagesReady)
      {
        placeImagesFinished();
        this.mPlaceImagesReady = true;
        validatePlaceImages();
      }
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.placeImagesReady();
      }
    }

    public void promoImagesBytesToReceive(int paramInt)
    {
      this.mPromoImageTotalBytes = paramInt;
      calculateTotalBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.promoImagesBytesToReceive(paramInt);
      }
    }

    public void promoImagesDataReceived(int paramInt)
    {
      this.mPromoImageCurrentBytes = paramInt;
      calculateCurrentBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.promoImagesDataReceived(paramInt);
      }
    }

    public void promoImagesDidUpdate()
    {
      this.mPromoImagesDidUpdate = true;
      validatePromotionImages();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.promoImagesDidUpdate();
      }
    }

    public void promoImagesFailedWithError(Exception paramException)
    {
      super.promoImagesFailedWithError(paramException);
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.promoImagesFailedWithError(paramException);
      }
    }

    public void promoImagesNeedsUpdate()
    {
      this.mPromoImagesNeedUpdate = true;
      validatePromotionImages();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.promoImagesNeedsUpdate();
      }
    }

    public void promoImagesReady()
    {
      if (!this.mPromoImagesReady)
      {
        promoImagesFinished();
        this.mPromoImagesReady = true;
        validatePromotionImages();
      }
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.promoImagesReady();
      }
    }

    public void venueDidUpdate()
    {
      super.venueDidUpdate();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueDidUpdate();
      }
    }

    public void venueImagesBytesToReceive(int paramInt)
    {
      this.mVenueImageTotalBytes = paramInt;
      calculateTotalBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueImagesBytesToReceive(paramInt);
      }
    }

    public void venueImagesDataReceived(int paramInt)
    {
      this.mVenueImageCurrentBytes = paramInt;
      calculateCurrentBytes();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueImagesDataReceived(paramInt);
      }
    }

    public void venueImagesDidUpdate()
    {
      this.mVenueImagesDidUpdate = true;
      validateVenueImages();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueImagesDidUpdate();
      }
    }

    public void venueImagesFailedWithError(Exception paramException)
    {
      super.venueImagesFailedWithError(paramException);
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueImagesFailedWithError(paramException);
      }
    }

    public void venueImagesNeedsUpdate()
    {
      this.mVenueImagesNeedUpdate = true;
      validateVenueImages();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueImagesNeedsUpdate();
      }
    }

    public void venueImagesReady()
    {
      if (!this.mVenueImagesReady)
      {
        venueImagesFinished();
        this.mVenueImagesReady = true;
        validateVenueImages();
      }
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueImagesReady();
      }
    }

    public void venueNeedsUpdate()
    {
      super.venueNeedsUpdate();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueNeedsUpdate();
      }
    }

    public void venueReady()
    {
      super.venueReady();
      if (PIMapVenue.this.mPIVenueDownloadObserver != null) {
        PIMapVenue.this.mPIVenueDownloadObserver.venueReady();
      }
      PIMapVenue.this.mHandler.postDelayed(new Runnable()
      {
        public void run()
        {
          if (PIMapVenue.this.mDownloadPlaceImages) {
            PIMapVenue.this.loadOrUpdateVenuePlaceImages(null);
          }
          if (PIMapVenue.this.mDownloadPromoImages) {
            PIMapVenue.this.loadOrUpdatePromotionImages(null);
          }
          if (PIMapVenue.this.mDownloadVenueImages) {
            PIMapVenue.this.loadOrUpdateVenueImages(null);
          }
        }
      }, 1500L);
    }
  }

  private class PDEMapDownloadObserver
    extends ContentObserver
  {
    private Uri uri;

    public PDEMapDownloadObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
        mVenueContentManager.cancelDownload(uri);
        mDefaultDownloadObserver.mapImagesDownloadCanceled();
    }

    public void onChange(boolean paramBoolean)
    {
        PIDownloadDataCursor pidownloaddatacursor = mVenueContentManager.getDownloadItem(uri);
        if(pidownloaddatacursor != null) {
	        int i;
	        mDefaultDownloadObserver.pdemapBytesToReceive(pidownloaddatacursor.getTotalBytes());
	        mDefaultDownloadObserver.pdemapDataReceived(pidownloaddatacursor.getCurrentBytes());
	        i = pidownloaddatacursor.getStatus();
	        if(Downloads.isStatusCompleted(i)) {
		        if(Downloads.isStatusError(i)) {
			        mDefaultDownloadObserver.pdemapFailedWithError(new Exception((new StringBuilder("Unable to retrieve PDE map. Response from server: ")).append(i).toString()));
			        mVenueContentManager.delete(uri);
		        } else if(pidownloaddatacursor.isFileExtracted()) {
		            mDefaultDownloadObserver.pdemapDidUpdate();
		            mDefaultDownloadObserver.pdemapReady();
		        } else if(pidownloaddatacursor.isExtractionError()) {
		        	mDefaultDownloadObserver.pdemapFailedWithError(new Exception("Unable to extract PDE map."));
		        }
	        }

	    	pidownloaddatacursor.close();
        }
    }
  }

  public class PIVenueAccess
  {
    private PIVenue mDelegate;
    private PIVenueDataset mPIVenueDataset;

    private PIVenueAccess(PIVenueDataset paramPIVenueDataset)
    {
      if (paramPIVenueDataset == null) {
        throw new IllegalArgumentException("The PIVenueDataset is null.");
      }
      this.mDelegate = new PIVenue(this);
      this.mPIVenueDataset = paramPIVenueDataset;
    }

    private void close()
    {
      if (this.mPIVenueDataset != null) {
        this.mPIVenueDataset.close();
      }
    }

    private PIMapAddressDataCursor getAddress(long paramLong)
    {
      return this.mDelegate.getAddress(this.mPIVenueDataset, paramLong);
    }

    private PIMapAddressDataCursor getAddresses()
    {
      return this.mDelegate.getAddresses(this.mPIVenueDataset);
    }

    private PIMapPlaceDataCursor getAllMapPlaces()
    {
      return this.mDelegate.getAllMapPlaces(this.mPIVenueDataset);
    }

    private int getAllPromotionsCount()
    {
      return this.mDelegate.getAllPromotionsCount(this.mPIVenueDataset);
    }

    private PIMapPlaceCategoriesDataCursor getCategoriesForPlace(long paramLong)
    {
      return this.mDelegate.getCategoriesForPlace(this.mPIVenueDataset, paramLong);
    }

    private int getDealCount()
    {
      return this.mDelegate.getDealCount(this.mPIVenueDataset);
    }

    private PIMapDealDataCursor getDeals()
    {
      return this.mDelegate.getDeals(this.mPIVenueDataset);
    }

    private PIMapDealDataCursor getDealsForPlace(long paramLong)
    {
      return this.mDelegate.getDealsForPlace(this.mPIVenueDataset, paramLong);
    }

    private PIMapDealDataCursor getDealsForPlace(String paramString)
    {
      return this.mDelegate.getDealsForPlace(this.mPIVenueDataset, paramString);
    }

    private PIMapDealDataCursor getDealsForPlace(boolean paramBoolean, long paramLong)
    {
      return this.mDelegate.getDealsForPlace(this.mPIVenueDataset, paramBoolean, paramLong);
    }

    private int getEventCount()
    {
      return this.mDelegate.getEventCount(this.mPIVenueDataset);
    }

    private PIMapPromotionDataCursor getEvents()
    {
      return this.mDelegate.getEvents(this.mPIVenueDataset);
    }

    private PIMapEventDataCursor getEventsForPlace(long paramLong)
    {
      return this.mDelegate.getEventsForPlace(this.mPIVenueDataset, paramLong);
    }

    private PIMapEventDataCursor getEventsForPlace(String paramString)
    {
      return this.mDelegate.getEventsForPlace(this.mPIVenueDataset, paramString);
    }

    private PIMapEventDataCursor getEventsForPlace(boolean paramBoolean, long paramLong)
    {
      return this.mDelegate.getEventsForPlace(this.mPIVenueDataset, paramBoolean, paramLong);
    }

    private PIMapImageDataCursor getImage(long paramLong)
    {
      return this.mDelegate.getImage(this.mPIVenueDataset, paramLong);
    }

    private PIMapImageDataCursor getImages()
    {
      return this.mDelegate.getImages(this.mPIVenueDataset);
    }

    private PIMapDealDataCursor getMapDealSearchForName(String paramString)
    {
      return this.mDelegate.getDealSearchForName(this.mPIVenueDataset, paramString);
    }

    private PIMapPromotionDataCursor getMapEventSearchForName(String paramString)
    {
      return this.mDelegate.getEventSearchForName(this.mPIVenueDataset, paramString);
    }

    private PIMapItemDataCursor getMapItem(long paramLong)
    {
      return this.mDelegate.getMapItem(this.mPIVenueDataset, paramLong);
    }

    private PIMapItemDataCursor getMapItemForIlcCode(String paramString)
    {
      return this.mDelegate.getMapItemForIlcCode(this.mPIVenueDataset, paramString);
    }

    private PIMapItemDataCursor getMapItemForUUID(String paramString)
    {
      return this.mDelegate.getMapItemForUUID(this.mPIVenueDataset, paramString);
    }

    private PIMapItemDataCursor getMapItemNameStartsWith(String paramString)
    {
      return this.mDelegate.getMapItemNameStartsWith(this.mPIVenueDataset, paramString);
    }

    private PIMapItemDataCursor getMapItemSearchForName(String paramString)
    {
      return this.mDelegate.getMapItemSearchForName(this.mPIVenueDataset, paramString);
    }

    private PIMapItemDataCursor getMapItems()
    {
      return this.mDelegate.getMapItems(this.mPIVenueDataset);
    }

    private PIMapPlaceDataCursor getMapPlace(int paramInt)
    {
      return this.mDelegate.getMapPlace(this.mPIVenueDataset, paramInt);
    }

    private PIMapAreaDataCursor getMapPlaceAreaForPlace(long paramLong)
    {
      return this.mDelegate.getMapPlaceAreaForPlace(this.mPIVenueDataset, paramLong);
    }

    private PIMapAreaDataCursor getMapPlaceAreas()
    {
      return this.mDelegate.getMapPlaceAreas(this.mPIVenueDataset);
    }

    private PIMapAreaDataCursor getMapPlaceAreas(long paramLong)
    {
      return this.mDelegate.getMapPlaceAreas(this.mPIVenueDataset, paramLong);
    }

    private PIMapItemDataCursor getMapPlaceForPromotion(long paramLong)
    {
      return this.mDelegate.getMapPlaceForPromotion(this.mPIVenueDataset, paramLong);
    }

    private PIMapServiceDataCursor getMapPlaceForServiceTypeUUID(String paramString)
    {
      return this.mDelegate.getMapPlaceForServiceTypeUUID(this.mPIVenueDataset, paramString);
    }

    private PIMapPlaceDataCursor getMapPlaceForUUID(String paramString)
    {
      return this.mDelegate.getMapPlaceForUUID(this.mPIVenueDataset, paramString);
    }

    private PIMapPlaceDataCursor getMapPlaceSearchForName(String paramString)
    {
      return this.mDelegate.getMapPlaceSearchForName(this.mPIVenueDataset, paramString);
    }

    private PIMapPlaceDataCursor getMapPlaces()
    {
      return this.mDelegate.getMapPlaces(this.mPIVenueDataset);
    }

    private PIMapPlaceDataCursor getMapPlaces(boolean paramBoolean)
    {
      return this.mDelegate.getMapPlaces(this.mPIVenueDataset, paramBoolean);
    }

    private PIMapPlaceDataCursor getMapPlacesForUUIDs(String... paramVarArgs)
    {
      return this.mDelegate.getMapPlacesForUUIDs(this.mPIVenueDataset, paramVarArgs);
    }

    private PIMapItemDataCursor getMapPlacesWithPromotion()
    {
      return this.mDelegate.getMapPlacesWithPromotion(this.mPIVenueDataset);
    }

    private PIMapPromotionDataCursor getMapPromotionSearchForName(String paramString, long... paramVarArgs)
    {
      return this.mDelegate.getPromotionsSearchForName(this.mPIVenueDataset, paramString, paramVarArgs);
    }

    private PIMapServiceDataCursor getMapService(String paramString)
    {
      return this.mDelegate.getMapService(this.mPIVenueDataset, paramString);
    }

    private PIMapServiceDataCursor getMapServiceSearchForName(String paramString)
    {
      return this.mDelegate.getMapServiceSearchForName(this.mPIVenueDataset, paramString);
    }

    private PIMapServiceGroupedDataCursor getMapServiceSearchForNameGrouped(String paramString)
    {
      return this.mDelegate.getMapServiceSearchForNameGrouped(this.mPIVenueDataset, paramString);
    }

    private PIMapServiceDataCursor getMapServices()
    {
      return this.mDelegate.getMapServices(this.mPIVenueDataset);
    }

    private PIMapServiceGroupedDataCursor getMapServicesGrouped()
    {
      return this.mDelegate.getMapServicesGrouped(this.mPIVenueDataset);
    }

    private PIMapWormholeDataCursor getMapWormhole(long paramLong)
    {
      return this.mDelegate.getMapWormhole(this.mPIVenueDataset, paramLong);
    }

    private PIMapWormholeDataCursor getMapWormholeForType(long paramLong)
    {
      return this.mDelegate.getMapWormholeForType(this.mPIVenueDataset, paramLong);
    }

    private PIMapWormholeDataCursor getMapWormholes()
    {
      return this.mDelegate.getMapWormholes(this.mPIVenueDataset);
    }

    private PIMapOperationMinutesDataCursor getOperationMinutes()
    {
      return this.mDelegate.getOperationMinutes(this.mPIVenueDataset);
    }

    private PIMapOperationMinutesDataCursor getOperationMinutes(long paramLong)
    {
      return this.mDelegate.getOperationMinutes(this.mPIVenueDataset, paramLong);
    }

    private PixelCoordinateDataCursor getPixelCoordinates()
    {
      return this.mDelegate.getPixelCoordinates(this.mPIVenueDataset);
    }

    private PixelCoordinateDataCursor getPixelCoordinatesForArea(long paramLong)
    {
      return this.mDelegate.getPixelCoordinatesForArea(this.mPIVenueDataset, paramLong);
    }

    private int getPlaceEventCount()
    {
      return this.mDelegate.getPlaceEventCount(this.mPIVenueDataset);
    }

    private PIMapEventDataCursor getPlaceEvents()
    {
      return this.mDelegate.getPlaceEvents(this.mPIVenueDataset);
    }

    private PIMapPlacesWithCategoryDataCursor getPlacesByCategory()
    {
      return this.mDelegate.getPlacesByCategory(this.mPIVenueDataset);
    }

    private PIMapPolygonZoneDataCursor getPolygonZones()
    {
      return this.mDelegate.getPolygonZones(this.mPIVenueDataset);
    }

    private PIMapPromotionDataCursor getPromotion(long paramLong)
    {
      return this.mDelegate.getPromotion(this.mPIVenueDataset, paramLong);
    }

    private int getPromotionCount()
    {
      return this.mDelegate.getPromotionCount(this.mPIVenueDataset);
    }

    private PIMapPromotionDataCursor getPromotions(boolean paramBoolean, long... paramVarArgs)
    {
      return this.mDelegate.getPromotions(this.mPIVenueDataset, paramBoolean, paramVarArgs);
    }

    private PIMapPromotionDataCursor getPromotions(long... paramVarArgs)
    {
      return this.mDelegate.getPromotions(this.mPIVenueDataset, paramVarArgs);
    }

    private PIMapVenueDataCursor getVenue(String paramString)
    {
      return this.mDelegate.getVenue(this.mPIVenueDataset, paramString);
    }

    private PIMapPromotionDataCursor getVenuePromotion(long paramLong)
    {
      return this.mDelegate.getVenuePromotion(this.mPIVenueDataset, paramLong);
    }

    private PIMapPromotionDataCursor getVenuePromotions()
    {
      return this.mDelegate.getVenuePromotions(this.mPIVenueDataset);
    }

    private PIMapZoneDataCursor getVenueZone(long paramLong)
    {
      return this.mDelegate.getVenueZone(this.mPIVenueDataset, paramLong);
    }

    private PIMapZoneDataCursor getVenueZones()
    {
      return this.mDelegate.getVenueZones(this.mPIVenueDataset);
    }

    private boolean hasOverview()
    {
      return this.mDelegate.hasOverview(this.mPIVenueDataset);
    }
  }

  public class PIVenueContentManager
  {
    private PIContentManager mDelegate = new PIContentManager(this);

    private PIVenueContentManager() {}

    private void cancelDownload(Uri paramUri)
    {
      this.mDelegate.cancelDownload(PIMapVenue.this.mContext, paramUri);
    }

    private void delete(Uri paramUri)
    {
      this.mDelegate.delete(PIMapVenue.this.mContext, paramUri);
    }

    private void deleteVenueDownload()
    {
      this.mDelegate.deleteVenueDownload(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary.getVenueUUID());
    }

    private PIDownloadDataCursor getDownloadItem(Uri paramUri)
    {
      return this.mDelegate.getDownloadItem(PIMapVenue.this.mContext, paramUri);
    }

    private Bitmap getPlaceImageFile(String paramString)
    {
      return this.mDelegate.getPlaceImageFile(PIMapVenue.this.mContext, PIMapVenue.this.getVenueUUID(), paramString);
    }

    private PIDownloadDataCursor getPlaceImagesItem()
    {
      return this.mDelegate.getOrCreatePlaceImagesDownloadItem(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private Bitmap getPromoImageFile(String paramString)
    {
      return this.mDelegate.getPromoImageFile(PIMapVenue.this.mContext, PIMapVenue.this.getVenueUUID(), paramString);
    }

    private PIDownloadDataCursor getPromotionImagesItem()
    {
      return this.mDelegate.getOrCreatePromotionImagesDownloadItem(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private PIDownloadDataCursor getVenueDownloadItem()
    {
      return this.mDelegate.getOrCreateVenueDatasetDownloadItem(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private PIFileDataCursor getVenueFileItem()
    {
      return this.mDelegate.getVenueDatasetFile(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary.getVenueUUID());
    }

    private Bitmap getVenueImageFile(String paramString)
    {
      return this.mDelegate.getVenueImageFile(PIMapVenue.this.mContext, PIMapVenue.this.getVenueUUID(), paramString);
    }

    private PIDownloadDataCursor getVenueImagesItem()
    {
      return this.mDelegate.getOrCreateVenueImagesDownloadItem(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private PIFileDataCursor getVenuePDEMapFileItem()
    {
      return this.mDelegate.getVenuePDEMapFile(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary.getVenueUUID());
    }

    private PIDownloadDataCursor getVenuePDEMapItem()
    {
      return this.mDelegate.getOrCreateVenuePDEMapDownloadItem(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private PIDownloadDataCursor getVenueZoneImagesItem()
    {
      return this.mDelegate.getOrCreateVenueZoneImagesDownloadItem(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private boolean isVenueOnSDCard()
    {
      return this.mDelegate.isVenueOnSDCard(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary.getVenueUUID());
    }

    private void updatePlaceImages()
    {
      this.mDelegate.updatePlaceImages(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private void updatePromotionImages()
    {
      this.mDelegate.updatePromotionImages(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private void updateVenueDataset()
    {
      this.mDelegate.updateVenueDatasetFile(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private void updateVenueImages()
    {
      this.mDelegate.updateVenueImages(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private void updateVenuePDEMap()
    {
      this.mDelegate.updateVenuePDEMap(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private void updateVenueZoneImages()
    {
      this.mDelegate.updateVenueZoneImages(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private void validateVenueDownload()
    {
      this.mDelegate.validateVenueDownload(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary.getVenueUUID());
    }

    private void validateVenueDownloads()
    {
      this.mDelegate.validateVenueDownloads(PIMapVenue.this.mContext);
    }

    private boolean venueDatasetExists()
    {
      return this.mDelegate.venueDatasetFileExists(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary);
    }

    private boolean venueZoneImagesExists()
    {
      return this.mDelegate.venueZoneImagesExists(PIMapVenue.this.mContext, PIMapVenue.this.mPIMapVenueSummary.getVenueUUID());
    }
  }

  public static class PIVenueDownloadObserver
  {
    public void bytesToReceive(int paramInt) {}

    public void dataReceived(int paramInt) {}

    public void datasetBytesToReceive(int paramInt) {}

    public void datasetDataReceived(int paramInt) {}

    public void datasetDidUpdate() {}

    public void datasetDownloadCanceled()
    {
      datasetFinished();
    }

    public void datasetFailedWithError(Exception paramException)
    {
      datasetFinished();
      failedWithError(paramException);
    }

    protected void datasetFinished()
    {
      if ((PIMapVenue.getContext() != null) && (PIMapVenue.sDatasetObserver != null))
      {
        PIMapVenue.getContext().getContentResolver().unregisterContentObserver(PIMapVenue.sDatasetObserver);
        PIMapVenue.sDatasetObserver = null;
      }
    }

    public void datasetNeedsUpdate() {}

    public void datasetReady() {}

    public void failedWithError(Exception paramException)
    {
      finish();
    }

    @Deprecated
    public void fileDidUpdate() {}

    @Deprecated
    public void fileNeedsUpdate() {}

    @Deprecated
    public void fileReady() {}

    protected void finish() {}

    public void mapImagesBytesToReceive(int paramInt) {}

    public void mapImagesDataReceived(int paramInt) {}

    public void mapImagesDidUpdate() {}

    public void mapImagesDownloadCanceled()
    {
      mapImagesFinished();
    }

    public void mapImagesFailedWithError(Exception paramException)
    {
      mapImagesFinished();
      failedWithError(paramException);
    }

    protected void mapImagesFinished()
    {
      if ((PIMapVenue.getContext() != null) && (PIMapVenue.sZoneImageObserver != null))
      {
        PIMapVenue.getContext().getContentResolver().unregisterContentObserver(PIMapVenue.sZoneImageObserver);
        PIMapVenue.sZoneImageObserver = null;
      }
    }

    public void mapImagesNeedsUpdate() {}

    public void mapImagesReady() {}

    public void pdemapFailedWithError(Exception paramException)
    {
      pdemapFinished();
      failedWithError(paramException);
    }

    protected void pdemapFinished()
    {
      if ((PIMapVenue.getContext() != null) && (PIMapVenue.sPdemapObserver != null))
      {
        PIMapVenue.getContext().getContentResolver().unregisterContentObserver(PIMapVenue.sPdemapObserver);
        PIMapVenue.sPdemapObserver = null;
      }
    }

    public void placeImagesBytesToReceive(int paramInt) {}

    public void placeImagesDataReceived(int paramInt) {}

    public void placeImagesDidUpdate() {}

    public void placeImagesFailedWithError(Exception paramException)
    {
      placeImagesFinished();
    }

    protected void placeImagesFinished()
    {
      if ((PIMapVenue.getContext() != null) && (PIMapVenue.sPlaceImageObserver != null))
      {
        PIMapVenue.getContext().getContentResolver().unregisterContentObserver(PIMapVenue.sPlaceImageObserver);
        PIMapVenue.sPlaceImageObserver = null;
      }
    }

    public void placeImagesNeedsUpdate() {}

    public void placeImagesReady() {}

    public void promoImagesBytesToReceive(int paramInt) {}

    public void promoImagesDataReceived(int paramInt) {}

    public void promoImagesDidUpdate() {}

    public void promoImagesFailedWithError(Exception paramException)
    {
      promoImagesFinished();
    }

    protected void promoImagesFinished()
    {
      if ((PIMapVenue.getContext() != null) && (PIMapVenue.sPromoImageObserver != null))
      {
        PIMapVenue.getContext().getContentResolver().unregisterContentObserver(PIMapVenue.sPromoImageObserver);
        PIMapVenue.sPromoImageObserver = null;
      }
    }

    public void promoImagesNeedsUpdate() {}

    public void promoImagesReady() {}

    public void venueDidUpdate()
    {
      fileDidUpdate();
    }

    public void venueImagesBytesToReceive(int paramInt) {}

    public void venueImagesDataReceived(int paramInt) {}

    public void venueImagesDidUpdate() {}

    public void venueImagesFailedWithError(Exception paramException)
    {
      venueImagesFinished();
    }

    protected void venueImagesFinished()
    {
      if ((PIMapVenue.getContext() != null) && (PIMapVenue.sVenueImageObserver != null))
      {
        PIMapVenue.getContext().getContentResolver().unregisterContentObserver(PIMapVenue.sVenueImageObserver);
        PIMapVenue.sVenueImageObserver = null;
      }
    }

    public void venueImagesNeedsUpdate() {}

    public void venueImagesReady() {}

    public void venueNeedsUpdate()
    {
      fileNeedsUpdate();
    }

    public void venueReady()
    {
      finish();
      fileReady();
    }
  }

  private class PlaceImageDownloadObserver
    extends ContentObserver
  {
    private Uri uri;

    public PlaceImageDownloadObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
      PIMapVenue.this.mVenueContentManager.cancelDownload(this.uri);
    }

    public void onChange(boolean flag)
    {
        PIDownloadDataCursor pidownloaddatacursor = mVenueContentManager.getDownloadItem(uri);
        if(pidownloaddatacursor == null) 
        	return;
        try {
        int i;
        mDefaultDownloadObserver.placeImagesBytesToReceive(pidownloaddatacursor.getTotalBytes());
        mDefaultDownloadObserver.placeImagesDataReceived(pidownloaddatacursor.getCurrentBytes());
        i = pidownloaddatacursor.getStatus();
        if(Downloads.isStatusCompleted(i)) {
	        if(!Downloads.isStatusError(i)) {
	        	_L6:
	                if(!pidownloaddatacursor.isFileExtracted()) {
	                        if(pidownloaddatacursor.isExtractionError()) {
	                        	mDefaultDownloadObserver.placeImagesFailedWithError(new Exception("Unable to extract place images."));
	                        }
	                } else {
		                mDefaultDownloadObserver.placeImagesReady();
	                }
	        	
	        } else {
		        mDefaultDownloadObserver.placeImagesFailedWithError(new Exception((new StringBuilder("Unable to retrieve place images. Response from server: ")).append(i).toString()));
		        mVenueContentManager.delete(uri);
	        }
        }
        pidownloaddatacursor.close();
        return;
        } catch(Exception exception) {
        	exception.printStackTrace();
        }
//        pidownloaddatacursor.close();
//        throw exception;
    }
  }

  private class PromoImageDownloadObserver
    extends ContentObserver
  {
    private Uri uri;

    public PromoImageDownloadObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
      PIMapVenue.this.mVenueContentManager.cancelDownload(this.uri);
    }

    public void onChange(boolean paramBoolean)
    {
      PIDownloadDataCursor localPIDownloadDataCursor = PIMapVenue.this.mVenueContentManager.getDownloadItem(this.uri);
      if (localPIDownloadDataCursor != null) {}
      for (;;)
      {
        try
        {
          PIMapVenue.this.mDefaultDownloadObserver.promoImagesBytesToReceive(localPIDownloadDataCursor.getTotalBytes());
          PIMapVenue.this.mDefaultDownloadObserver.promoImagesDataReceived(localPIDownloadDataCursor.getCurrentBytes());
          int i = localPIDownloadDataCursor.getStatus();
          if (Downloads.isStatusCompleted(i))
          {
            if (Downloads.isStatusError(i))
            {
              PIMapVenue.this.mDefaultDownloadObserver.promoImagesFailedWithError(new Exception("Unable to retrieve promotion images. Response from server: " + i));
              PIMapVenue.this.mVenueContentManager.delete(this.uri);
            }
          }
          else {
            return;
          }
          if (localPIDownloadDataCursor.isFileExtracted())
          {
            PIMapVenue.this.mDefaultDownloadObserver.promoImagesReady();
            continue;
          }
          if (!localPIDownloadDataCursor.isExtractionError()) {
            continue;
          }
        }
        finally
        {
          localPIDownloadDataCursor.close();
        }
        PIMapVenue.this.mDefaultDownloadObserver.promoImagesFailedWithError(new Exception("Unable to extract promotion images."));
      }
    }
  }

  private class VenueImageDownloadObserver
    extends ContentObserver
  {
    private Uri uri;

    public VenueImageDownloadObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
      PIMapVenue.this.mVenueContentManager.cancelDownload(this.uri);
    }

    public void onChange(boolean paramBoolean)
    {
      PIDownloadDataCursor localPIDownloadDataCursor = PIMapVenue.this.mVenueContentManager.getDownloadItem(this.uri);
      if (localPIDownloadDataCursor != null) {}
      for (;;)
      {
        try
        {
          PIMapVenue.this.mDefaultDownloadObserver.venueImagesBytesToReceive(localPIDownloadDataCursor.getTotalBytes());
          PIMapVenue.this.mDefaultDownloadObserver.venueImagesDataReceived(localPIDownloadDataCursor.getCurrentBytes());
          int i = localPIDownloadDataCursor.getStatus();
          if (Downloads.isStatusCompleted(i))
          {
            if (Downloads.isStatusError(i))
            {
              PIMapVenue.this.mDefaultDownloadObserver.venueImagesFailedWithError(new Exception("Unable to retrieve venue images. Response from server: " + i));
              PIMapVenue.this.mVenueContentManager.delete(this.uri);
            }
          }
          else {
            return;
          }
          if (localPIDownloadDataCursor.isFileExtracted())
          {
            PIMapVenue.this.mDefaultDownloadObserver.venueImagesReady();
            continue;
          }
          if (!localPIDownloadDataCursor.isExtractionError()) {
            continue;
          }
        }
        finally
        {
          localPIDownloadDataCursor.close();
        }
        PIMapVenue.this.mDefaultDownloadObserver.venueImagesFailedWithError(new Exception("Unable to extract venue images."));
      }
    }
  }

  private class ZoneImageDownloadObserver
    extends ContentObserver
  {
    private Uri uri;

    public ZoneImageDownloadObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
      PIMapVenue.this.mVenueContentManager.cancelDownload(this.uri);
      PIMapVenue.this.mDefaultDownloadObserver.mapImagesDownloadCanceled();
    }

    public void onChange(boolean flag)
    {
        try {
    	PIDownloadDataCursor pidownloaddatacursor = mVenueContentManager.getDownloadItem(uri);
        if(pidownloaddatacursor == null)
        	return;
        int i;
        mDefaultDownloadObserver.mapImagesBytesToReceive(pidownloaddatacursor.getTotalBytes());
        mDefaultDownloadObserver.mapImagesDataReceived(pidownloaddatacursor.getCurrentBytes());
        i = pidownloaddatacursor.getStatus();
        if(Downloads.isStatusCompleted(i)) {
	
	        if(Downloads.isStatusError(i)) {
//		        mDefaultDownloadObserver.mapImagesFailedWithError(new Exception((new StringBuilder("Unable to retrieve map images. Response from server: ")).append(i).toString()));
//		        mVenueContentManager.delete(uri);
	        } else {
	
		        if(!pidownloaddatacursor.isFileExtracted()) {
//		            if(pidownloaddatacursor.isExtractionError()) 
//		            	mDefaultDownloadObserver.mapImagesFailedWithError(new Exception("Unable to extract map images."));
		        	
			        mDefaultDownloadObserver.mapImagesDidUpdate();
			        mDefaultDownloadObserver.mapImagesReady();
		        } else {
			        mDefaultDownloadObserver.mapImagesDidUpdate();
			        mDefaultDownloadObserver.mapImagesReady();
		        }
	        }
	    }        
        pidownloaddatacursor.close();
    
        return;
            
    } catch(Exception exception){
        exception.printStackTrace();
//        pidownloaddatacursor.close();
//        throw exception;
    }
    }
  }
}

