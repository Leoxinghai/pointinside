package com.pointinside.android.api;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import com.pointinside.android.api.content.DownloadReceiver;
import com.pointinside.android.api.content.Downloads;
import com.pointinside.android.api.content.PIContentManager;
import com.pointinside.android.api.dao.PIDownloadDataCursor;
import com.pointinside.android.api.dao.PIFileDataCursor;
import com.pointinside.android.api.dao.PIMapGeoCityDataCursor;
import com.pointinside.android.api.dao.PIMapGeoCountryDataCursor;
import com.pointinside.android.api.dao.PIMapGeoCountrySubdivisionDataCursor;
import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;
//import com.pointinside.android.api.dao.PIMapVenueSummaryDataCursor;
import com.pointinside.android.api.dao.PIReference;
import com.pointinside.android.api.dao.PIReferenceDataset;
import com.pointinside.android.api.dao.PISQLiteHelper;
import org.apache.http.auth.Credentials;

public final class PIMapReference
{
  private static Uri sBaseUri;
  private static Credentials sCredentials;
  private static DownloadReceiver sDownloadReceiver;
  static final PIMapReference sPIMapReference = new PIMapReference();
  private static PISQLiteHelper sPISQLiteHelper;
  private Context mContext;
  private final Handler mHandler = new Handler();
  private volatile boolean mIsInitialized = false;
  private volatile boolean mIsLoaded = false;
  private MyDownloadContentObserver mObserver;
  private PIMapVenue mPIMapVenue;
  private PIReferenceAccess mPIReferenceAccess;
  private PIReferenceDownloadObserver mPIReferenceObserver;
  private SharedPreferences mPrefs;
  private PIReferenceContentManager mReferenceContentManager;
  private String mReferenceETag;

  public static Uri getBaseUri()
  {
    return sBaseUri;
  }

  public static String getBaseUrl()
  {
    return sBaseUri.toString();
  }

  public static Credentials getCredentials()
  {
    return sCredentials;
  }

  public static PIMapReference getInstance()
    throws PIMapReference.NotInitializedException
  {
    if (sPIMapReference.mIsInitialized) {
      return sPIMapReference;
    }
    throw new NotInitializedException("PIMapReference is not initialized. Please call newInstance(android.content.Context, org.apache.http.auth.UsernamePasswordCredentials).");
  }

  public static PIMapReference getInstance(Context paramContext, String paramString, Credentials paramCredentials)
  {
      try
      {
        if (sPIMapReference.mIsInitialized)
        {
          return sPIMapReference;
        } else {
            if (paramContext.getApplicationContext() != paramContext) {
                throw new IllegalArgumentException("PIMapReference expects an application context, did you use context.getApplicationContext()?");
              }
            PIMapReference localPIMapReference = newInstance(paramContext, Uri.parse(paramString), paramCredentials);
            PIMapReference localObject2 = localPIMapReference;
            return localObject2;
        }
      } catch(Exception ex) {
    	  ex.printStackTrace();
      }
      return sPIMapReference;
  }

  private void init(Context paramContext)
  {
    sPIMapReference.mContext = paramContext;
    sPIMapReference.mPrefs = paramContext.getSharedPreferences("PIMaps", 0);
    PIMapReference localPIMapReference1 = sPIMapReference;
    PIMapReference localPIMapReference2 = sPIMapReference;
    localPIMapReference2.getClass();
    localPIMapReference1.mReferenceContentManager = new PIReferenceContentManager();

    sPIMapReference.validateVenueDownloads();
    sPIMapReference.removeCachedZipFiles();
    if (sPIMapReference.validateReferenceDownload()) {
      sPIMapReference.loadReferenceDataset();
    }
    if (sDownloadReceiver != null) {
      sPIMapReference.mContext.unregisterReceiver(sDownloadReceiver);
    }
    sDownloadReceiver = new DownloadReceiver();
    DownloadReceiver.initNetworkStatus(paramContext);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    localIntentFilter.addAction("pointinside.intent.action.DOWNLOAD_WAKEUP");
    localIntentFilter.addAction("pointinside.intent.action.DOWNLOAD_NEW");
    localIntentFilter.addAction("pointinside.intent.action.DOWNLOAD_CANCEL");
    localIntentFilter.addAction("pointinside.intent.action.DOWNLOAD_COMPLETE");
    sPIMapReference.mContext.registerReceiver(sDownloadReceiver, localIntentFilter);
  }

  private boolean loadReferenceDataset()
  {
      PIFileDataCursor pifiledatacursor;
      mIsLoaded = false;
      pifiledatacursor = mReferenceContentManager.getReferenceFileItem();
      if(pifiledatacursor != null) {
	      if(sPISQLiteHelper == null) {
	    	  sPISQLiteHelper = new PISQLiteHelper(pifiledatacursor.getFileUri());
	      }
	      sPIMapReference.mPIReferenceAccess = new PIReferenceAccess(new PIReferenceDataset(sPISQLiteHelper));

	      mIsLoaded = true;
	      sPISQLiteHelper.close();
	      pifiledatacursor.close();
      } else {
    	  if(pifiledatacursor !=null)
    		  pifiledatacursor.close();
      }
      return mIsLoaded;
  }


  private static PIMapReference newInstance(Context paramContext, Uri paramUri, Credentials paramCredentials)
  {
    if (paramContext == null) {
      throw new IllegalArgumentException("Context is null.");
    }
    if (paramCredentials == null) {
      throw new IllegalArgumentException("UsernamePasswordCredentials is null.");
    }
    sBaseUri = paramUri;
    sCredentials = paramCredentials;
    sPIMapReference.init(paramContext);
    sPIMapReference.mIsInitialized = true;
    return sPIMapReference;
  }

  private void removeCachedZipFiles()
  {
    new Thread()
    {
      public void run()
      {
//        Process.setThreadPriority(10);
//        PIMapReference.PIReferenceContentManager.access$9(PIMapReference.sPIMapReference.mReferenceContentManager);
        Process.setThreadPriority(10);
        PIMapReference.sPIMapReference.mReferenceContentManager.removeCachedZipFiles();

      }
    }.start();
  }

  private boolean validateReferenceDownload()
  {
    return this.mReferenceContentManager.validateReferenceDownload();
  }

  private void validateVenueDownloads()
  {
    new Thread()
    {
      public void run()
      {
//        Process.setThreadPriority(10);
//        PIMapReference.PIReferenceContentManager.access$7(PIMapReference.sPIMapReference.mReferenceContentManager);
        Process.setThreadPriority(10);
        PIMapReference.sPIMapReference.mReferenceContentManager.validateVenueDownloads();
      }
    }.start();
  }

  public void cancelDownload()
  {
    if (this.mObserver != null) {
      this.mObserver.cancelDownload();
    }
  }

  public void checkForUpdates(PIReferenceDownloadObserver pireferencedownloadobserver)
  {
      PIDownloadDataCursor pidownloaddatacursor;
      Uri uri;
      try {
	      if(pireferencedownloadobserver != null)
	          mPIReferenceObserver = pireferencedownloadobserver;
	      else
	          mPIReferenceObserver = new PIReferenceDownloadObserver();
	      System.out.println("PIMapReference.checkForUpdates");
	      
	      pidownloaddatacursor = mReferenceContentManager.getReferenceDownloadItem();
	      if(pidownloaddatacursor == null) {
	    	  mPIReferenceObserver.failedWithError(new Exception("Unable to download reference file."));
	    	  return;
	      }
	      uri = pidownloaddatacursor.getUri();
	      mReferenceETag = pidownloaddatacursor.getIdentifier();
	      mObserver = new MyDownloadContentObserver(mHandler, uri);
	      mContext.getContentResolver().registerContentObserver(uri, false, mObserver);
	      mReferenceContentManager.refreshFile();
	      pidownloaddatacursor.close();
	      return;
      } catch(Exception exception) {
//    	  pidownloaddatacursor.close();
    	  exception.printStackTrace();
      }      
      return;
  }

  public void deleteVenueDownload(String paramString)
  {
    this.mReferenceContentManager.deleteVenueDownload(paramString);
  }

  public PIMapGeoCityDataCursor getCities()
  {
    return this.mPIReferenceAccess.getGeoCities();
  }

  public PIMapGeoCityDataCursor getCitiesForSubdivision(long paramLong)
  {
    return this.mPIReferenceAccess.getGeoCitiesForSubdivision(paramLong);
  }

  public PIMapVenueSummaryDataCursor getClosestVenueToLocation(Location paramLocation)
  {
    Log.d("PIMaps", "getClosestVenueToLocation Not Implemented!!!!");
    return null;
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public PIMapGeoCountryDataCursor getCountries()
  {
    return this.mPIReferenceAccess.getGeoCountries();
  }

  public PIMapGeoCountrySubdivisionDataCursor getGeoCountrySubdivisions()
  {
    return this.mPIReferenceAccess.getGeoCountrySubdivisions();
  }

  public PIMapGeoCountrySubdivisionDataCursor getGeoCountrySubdivisionsForCountry(long paramLong)
  {
    return this.mPIReferenceAccess.getGeoCountrySubdivisionsForCountry(paramLong);
  }

  public PIMapVenue getLoadedVenue()
  {
    return this.mPIMapVenue;
  }

  public PIMapVenueSummaryDataCursor getVenueForId(long paramLong)
  {
    return this.mPIReferenceAccess.getVenueSummary(paramLong);
  }

  public PIMapVenueSummaryDataCursor getVenueForUUID(String paramString)
  {
    return this.mPIReferenceAccess.getVenueSummary(paramString);
  }

  public PIMapVenueSummaryDataCursor getVenueSearchForName(String paramString)
  {
    return this.mPIReferenceAccess.getVenueSummarySearchForName(paramString);
  }

  public PIMapVenueSummaryDataCursor getVenueSearchForText(String paramString)
  {
    return getVenueSearchForText(paramString, null);
  }

  public PIMapVenueSummaryDataCursor getVenueSearchForText(String paramString, Location paramLocation)
  {
    return this.mPIReferenceAccess.getVenueSummarySearchForText(paramString, paramLocation);
  }

  public PIMapVenueSummaryDataCursor getVenues()
  {
    return getVenues(null);
  }

  public PIMapVenueSummaryDataCursor getVenues(Location paramLocation)
  {
    return this.mPIReferenceAccess.getVenueSummaries(paramLocation);
  }

  public PIMapVenueSummaryDataCursor getVenuesForCity(long paramLong)
  {
    return this.mPIReferenceAccess.getVenueSummariesForCity(paramLong);
  }

  public boolean isLoaded()
  {
    return this.mIsLoaded;
  }

  public boolean isNetworkAvailable()
  {
    if (sDownloadReceiver == null) {
      return false;
    }
    return sDownloadReceiver.isNetworkAvailable();
  }

  public PIMapVenue loadVenue(PIMapVenueSummaryDataCursor.PIMapVenueSummary paramPIMapVenueSummary)
  {
    return loadVenue(paramPIMapVenueSummary, false, false, false);
  }

  public PIMapVenue loadVenue(PIMapVenueSummaryDataCursor.PIMapVenueSummary paramPIMapVenueSummary, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramPIMapVenueSummary == null) {
      throw new IllegalArgumentException("PIMapVenueSummary is null");
    }
    if ((this.mPIMapVenue == null) || (!this.mPIMapVenue.getVenueUUID().equals(paramPIMapVenueSummary.getVenueUUID()))) {
      this.mPIMapVenue = new PIMapVenue(this.mContext, paramPIMapVenueSummary, paramBoolean1, paramBoolean2, paramBoolean3, this.mHandler);
    }
    return this.mPIMapVenue;
  }

  public PIMapVenue loadVenue(String paramString)
  {
    return loadVenue(paramString, false, false, false);
  }

  public PIMapVenue loadVenue(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("venueUUID is null");
    }
    PIMapVenueSummaryDataCursor localPIMapVenueSummaryDataCursor;
    if ((this.mPIMapVenue == null) || (!this.mPIMapVenue.getVenueUUID().equals(paramString)))
    {
      localPIMapVenueSummaryDataCursor = getVenueForUUID(paramString);
      if (localPIMapVenueSummaryDataCursor == null) {}
      else {
    	    try
    	    {
//    	    	if(localPIMapVenueSummaryDataCursor.moveToFirst())
    	    		loadVenue(localPIMapVenueSummaryDataCursor.getPIMapVenueSummary(), paramBoolean1, paramBoolean2, paramBoolean3);
    	    }
    	    finally
    	    {
    	      localPIMapVenueSummaryDataCursor.close();
    	    }
      }
    }
    return this.mPIMapVenue;
  }

  public void unRegisterPIReferenceDownloadObserver()
  {
    this.mPIReferenceObserver = new PIReferenceDownloadObserver();
  }

  private class MyDownloadContentObserver
    extends ContentObserver
  {
    private boolean needsUpdateFired = false;
    private Uri uri;

    public MyDownloadContentObserver(Handler paramHandler, Uri paramUri)
    {
      super(paramHandler);
      this.uri = paramUri;
    }

    private void cancelDownload()
    {
        mReferenceContentManager.cancelDownload(uri);
        mPIReferenceObserver.downloadCanceled();
    }

    public void onChange(boolean paramBoolean)
    {
    	if(mPIReferenceObserver != null) {
	        PIDownloadDataCursor pidownloaddatacursor = mReferenceContentManager.getDownloadItem(uri);
	        if(pidownloaddatacursor != null) {
		        int i;
		        if(!needsUpdateFired && (mReferenceETag == null || !mReferenceETag.equals(pidownloaddatacursor.getIdentifier())))
		        {
		            mPIReferenceObserver.fileNeedsUpdate();
		            needsUpdateFired = true;
		        }
		        mPIReferenceObserver.bytesToReceive(pidownloaddatacursor.getTotalBytes());
		        mPIReferenceObserver.dataReceived(pidownloaddatacursor.getCurrentBytes());
		        i = pidownloaddatacursor.getStatus();
		        if(Downloads.isStatusCompleted(i)) {
			        if(Downloads.isStatusSuccess(i)) {
			        	if(!pidownloaddatacursor.isFileExtracted()) {
					        mPIReferenceObserver.fileDidUpdate();
					        if(PIMapReference.sPIMapReference.loadReferenceDataset()) {
					        	mPIReferenceObserver.fileReady();
					        }
			        	}
			        } else {
			            if(Downloads.isStatusError(i)) {
			            	mPIReferenceObserver.failedWithError(new Exception((new StringBuilder("Unable to retrieve file. Response from server: ")).append(i).toString()));
			    	        pidownloaddatacursor.close();
			    	        return;
			            }
			        }
		        } else {
		            if(Downloads.isStatusError(i)) {
		            	mPIReferenceObserver.failedWithError(new Exception((new StringBuilder("Unable to retrieve file. Response from server: ")).append(i).toString()));
		    	        pidownloaddatacursor.close();
		    	        return;
		            }
			        pidownloaddatacursor.close();
		        }
	        } else {
	            if(pidownloaddatacursor.isExtractionError()) {
	            	mPIReferenceObserver.failedWithError(new Exception("Unable to extract reference file."));
	    	        pidownloaddatacursor.close();
	    	        return;
	            }

	        }
       } else {
         return;
       }


  }

  }

  public static class NotInitializedException
    extends Exception
  {
    private static final long serialVersionUID = -4698129909645940909L;

    public NotInitializedException() {}

    public NotInitializedException(String paramString)
    {
      super();
    }

    public NotInitializedException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }

    public NotInitializedException(Throwable paramThrowable)
    {
      super();
    }
  }

  public class PIReferenceAccess
  {
    private PIReference mDelegate;
    private PIReferenceDataset mPIReferenceDataset;

    private PIReferenceAccess(PIReferenceDataset paramPIReferenceDataset)
    {
      if (paramPIReferenceDataset == null) {
        throw new IllegalArgumentException("The PIReferenceDataset is null.");
      }
      this.mDelegate = new PIReference(this);
      this.mPIReferenceDataset = paramPIReferenceDataset;
    }

    private PIMapGeoCityDataCursor getGeoCities()
    {
      return this.mDelegate.getGeoCities(this.mPIReferenceDataset);
    }

    private PIMapGeoCityDataCursor getGeoCitiesForSubdivision(long paramLong)
    {
      return this.mDelegate.getGeoCitiesForSubdivision(this.mPIReferenceDataset, paramLong);
    }

    private PIMapGeoCountryDataCursor getGeoCountries()
    {
      return this.mDelegate.getGeoCountries(this.mPIReferenceDataset);
    }

    private PIMapGeoCountrySubdivisionDataCursor getGeoCountrySubdivisions()
    {
      return this.mDelegate.getGeoCountrySubdivisions(this.mPIReferenceDataset);
    }

    private PIMapGeoCountrySubdivisionDataCursor getGeoCountrySubdivisionsForCountry(long paramLong)
    {
      return this.mDelegate.getGeoCountrySubdivisionsForCountry(this.mPIReferenceDataset, paramLong);
    }

    private PIMapVenueSummaryDataCursor getVenueSummaries(Location paramLocation)
    {
      return this.mDelegate.getVenueSummaries(this.mPIReferenceDataset, paramLocation);
    }

    private PIMapVenueSummaryDataCursor getVenueSummariesForCity(long paramLong)
    {
      return this.mDelegate.getVenueSummariesForCity(this.mPIReferenceDataset, paramLong);
    }

    private PIMapVenueSummaryDataCursor getVenueSummary(long paramLong)
    {
      return this.mDelegate.getVenueSummary(this.mPIReferenceDataset, paramLong);
    }

    private PIMapVenueSummaryDataCursor getVenueSummary(String paramString)
    {
      return this.mDelegate.getVenueSummary(this.mPIReferenceDataset, paramString);
    }

    private PIMapVenueSummaryDataCursor getVenueSummarySearchForName(String paramString)
    {
      return this.mDelegate.getVenueSummarySearchForName(this.mPIReferenceDataset, paramString);
    }

    private PIMapVenueSummaryDataCursor getVenueSummarySearchForText(String paramString, Location paramLocation)
    {
      return this.mDelegate.getVenueSummarySearchForText(this.mPIReferenceDataset, paramString, paramLocation);
    }
  }

  public class PIReferenceContentManager
  {
    private PIContentManager mDelegate = new PIContentManager(this);

    public PIReferenceContentManager() {}

    private void cancelDownload(Uri paramUri)
    {
      this.mDelegate.cancelDownload(mContext, paramUri);
    }

    private boolean deleteReference()
    {
      return this.mDelegate.deleteReferenceFile(PIMapReference.this.mContext);
    }

    private void deleteVenueDownload(String paramString)
    {
      this.mDelegate.deleteVenueDownload(PIMapReference.this.mContext, paramString);
    }

    private boolean fileExists()
    {
      return this.mDelegate.referenceFileExists(PIMapReference.this.mContext);
    }

    private PIDownloadDataCursor getDownloadItem(Uri paramUri)
    {
      return this.mDelegate.getDownloadItem(PIMapReference.this.mContext, paramUri);
    }

    private PIDownloadDataCursor getReferenceDownloadItem()
    {
      return this.mDelegate.getOrCreateReferenceDownloadItem(PIMapReference.this.mContext);
    }

    private PIFileDataCursor getReferenceFileItem()
    {
      return this.mDelegate.getReferenceFile(PIMapReference.this.mContext);
    }

    private void refreshFile()
    {
      this.mDelegate.refreshReferenceFile(PIMapReference.this.mContext);
    }

    private void removeCachedZipFiles()
    {
      PIContentManager.removeCachedZipFiles(PIMapReference.this.mContext);
    }

    private boolean validateReferenceDownload()
    {
      return this.mDelegate.validateReferenceDownload(PIMapReference.this.mContext);
    }

    private void validateVenueDownloads()
    {
      this.mDelegate.validateVenueDownloads(PIMapReference.this.mContext);
    }
  }

  public static class PIReferenceDownloadObserver
  {
    private void finished()
    {
      PIMapReference.sPIMapReference.mContext.getContentResolver().unregisterContentObserver(PIMapReference.sPIMapReference.mObserver);
      PIMapReference.sPIMapReference.unRegisterPIReferenceDownloadObserver();
      PIMapReference.sPIMapReference.mReferenceETag = null;
    }

    public void bytesToReceive(int paramInt) {}

    public void dataReceived(int paramInt) {}

    public void downloadCanceled()
    {
      finished();
    }

    public void failedWithError(Exception paramException)
    {
      finished();
    }

    public void fileDidUpdate() {}

    public void fileNeedsUpdate() {}

    public void fileReady()
    {
      finished();
    }
  }
}

