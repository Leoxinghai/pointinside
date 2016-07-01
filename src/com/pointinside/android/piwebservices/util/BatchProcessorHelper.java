package com.pointinside.android.piwebservices.util;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BatchProcessorHelper
{
  private static final boolean DEBUG = false;
  private static final int DEFAULT_BATCH_THRESHOLD = 50;
  private static final int DEFAULT_TIME_THRESHOLD = 1500;
  private static final String TAG = BatchProcessorHelper.class.getSimpleName();
  private final String mAuthority;
  private final int mBatchSize;
  private long mLastChangeTime;
  private final ArrayList<ContentProviderOperation> mOperations;
  private final WeakReference<ContentResolver> mResolver;
  private final int mTimeThreshold;

  public BatchProcessorHelper(ContentResolver paramContentResolver, String paramString)
  {
    this(paramContentResolver, paramString, 50, 1500);
  }

  public BatchProcessorHelper(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
  {
    if (paramInt1 <= 0) {
      throw new IllegalArgumentException("batch size must be greater than zero");
    }
    if (paramInt2 <= 0) {
      throw new IllegalArgumentException("time threshould should be greater than zero");
    }
    this.mAuthority = paramString;
    this.mResolver = new WeakReference(paramContentResolver);
    this.mBatchSize = paramInt1;
    this.mTimeThreshold = paramInt2;
    this.mOperations = new ArrayList();
  }

  public void add(ContentProviderOperation paramContentProviderOperation)
  {
    this.mOperations.add(paramContentProviderOperation);
    this.mLastChangeTime = System.currentTimeMillis();
  }

  public ContentProviderResult[] runBatch()
    throws RemoteException, OperationApplicationException
  {
    ContentProviderResult[] arrayOfContentProviderResult = (ContentProviderResult[])null;
    if (this.mOperations.size() > 0)
    {
      arrayOfContentProviderResult = ((ContentResolver)this.mResolver.get()).applyBatch(this.mAuthority, this.mOperations);
      this.mOperations.clear();
    }
    return arrayOfContentProviderResult;
  }

  public ContentProviderResult[] runWhenThresholdReached()
    throws RemoteException, OperationApplicationException
  {
    if ((this.mLastChangeTime > 0L) && ((this.mOperations.size() > this.mBatchSize) || (System.currentTimeMillis() - this.mLastChangeTime > this.mTimeThreshold))) {
      return runBatch();
    }
    return null;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.util.BatchProcessorHelper
 * JD-Core Version:    0.7.0.1
 */