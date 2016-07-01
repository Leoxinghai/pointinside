// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.piwebservices.provider;

import android.content.*;
import android.database.sqlite.*;
import android.net.Uri;
import java.util.ArrayList;

public abstract class SQLiteContentProvider extends ContentProvider
    implements SQLiteTransactionListener
{

    public SQLiteContentProvider()
    {
    }

    private boolean applyingBatch()
    {
        return mApplyingBatch.get() != null && ((Boolean)mApplyingBatch.get()).booleanValue();
    }


    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arraylist)
        throws OperationApplicationException
    {
        int i;
        int j;
        i = 0;
        j = 0;
        mDb = mOpenHelper.getWritableDatabase();
        mDb.beginTransactionWithListener(this);
        int k;
        ContentProviderResult acontentproviderresult[];
        mApplyingBatch.set(Boolean.valueOf(true));
        k = arraylist.size();
        acontentproviderresult = new ContentProviderResult[k];
        int l = 0;
        
        try {
        for(;l < k;) {
	        if(++j >= 500)
	        	throw new OperationApplicationException("Too many content provider operations between yield points. The maximum number of operations per yield point is 500", i);
	        
	        ContentProviderOperation contentprovideroperation = (ContentProviderOperation)arraylist.get(l);
	        if(l > 0 && contentprovideroperation.isYieldAllowed()) {
	        	boolean flag;
		        flag = mDb.yieldIfContendedSafely(4000L);
		        j = 0;
		        if(flag)
		            i++;
	        }
	        acontentproviderresult[l] = contentprovideroperation.apply(this, acontentproviderresult, l);
	        l++;
		}
        mDb.setTransactionSuccessful();
        mApplyingBatch.set(Boolean.valueOf(false));
        mDb.endTransaction();
        onEndTransaction();
        
        } catch(Exception exception) {
	        mApplyingBatch.set(Boolean.valueOf(false));
	        mDb.endTransaction();
	        onEndTransaction();
	        throw new OperationApplicationException(exception.toString());
        }

        
        return acontentproviderresult;
    }
    protected void beforeTransactionCommit()
    {
    }

    public int bulkInsert(Uri paramUri, ContentValues[] paramArrayOfContentValues)
    {
      int i = paramArrayOfContentValues.length;
      this.mDb = this.mOpenHelper.getWritableDatabase();
      this.mDb.beginTransactionWithListener(this);
      for (int j = 0;; j++)
      {
        try
        {
            if (j >= i) {
		          this.mDb.setTransactionSuccessful();
		          this.mDb.endTransaction();
		          onEndTransaction();
		          return i;
            } else {
                if (insertInTransaction(paramUri, paramArrayOfContentValues[j]) != null) {
                    this.mNotifyChange = true;
                  }
                this.mDb.yieldIfContendedSafely();
            }
        }
        finally
        {
          this.mDb.endTransaction();
        }
      }
//      return i;
    }

    protected abstract SQLiteOpenHelper createDatabaseHelper(Context context);

    public int delete(Uri uri, String s, String as[])
    {
        try {
        	int i =0;
    	if(!applyingBatch()) {
	        mDb = mOpenHelper.getWritableDatabase();
	        mDb.beginTransactionWithListener(this);
	        i = deleteInTransaction(uri, s, as);
	        if(i <= 0)
	        	mNotifyChange = false;
	        else
	        	mNotifyChange = true;
	        mDb.setTransactionSuccessful();
	        mDb.endTransaction();
	        onEndTransaction();
        } else {
            i = deleteInTransaction(uri, s, as);
            if(i > 0)
            {
                mNotifyChange = true;
                return i;
            }
        }
        return i;
    } catch(Exception exception) {
        exception.printStackTrace();
        mDb.endTransaction();
        //throw exception;
    }
        return 0;
    }

    protected abstract int deleteInTransaction(Uri uri, String s, String as[]);

    protected SQLiteOpenHelper getDatabaseHelper()
    {
        return mOpenHelper;
    }

    public Uri insert(Uri uri, ContentValues contentvalues)
    {
    	Uri uri1 = null;
    try {
    	if(!applyingBatch()) {
	        mDb = mOpenHelper.getWritableDatabase();
	        mDb.beginTransactionWithListener(this);
	        uri1 = insertInTransaction(uri, contentvalues);
	        if(uri1 == null)
	        	mNotifyChange = false;
	        else
	        	mNotifyChange = true;

	        mDb.setTransactionSuccessful();
	        mDb.endTransaction();
	        onEndTransaction();
        } else {
            uri1 = insertInTransaction(uri, contentvalues);
            if(uri1 != null)
            {
                mNotifyChange = true;
                return uri1;
            }

        }
        return uri1;
    } catch(Exception exception) {
        exception.printStackTrace();
//        mDb.endTransaction();
//        throw exception;
    }
    return uri1;
    }

    protected abstract Uri insertInTransaction(Uri uri, ContentValues contentvalues);

    protected abstract void notifyChange();

    public void onBegin()
    {
        onBeginTransaction();
    }

    protected void onBeginTransaction()
    {
    }

    public void onCommit()
    {
        beforeTransactionCommit();
    }

    public boolean onCreate()
    {
        mOpenHelper = createDatabaseHelper(getContext());
        return true;
    }

    protected void onEndTransaction()
    {
        if(mNotifyChange)
        {
            mNotifyChange = false;
            notifyChange();
        }
    }

    public void onRollback()
    {
    }

    public int update(Uri uri, ContentValues contentvalues, String s, String as[])
    {
        try {
        	int i;
	    	if(!applyingBatch()) {

		        mDb = mOpenHelper.getWritableDatabase();
		        mDb.beginTransactionWithListener(this);
		        i = updateInTransaction(uri, contentvalues, s, as);
		        if(i <= 0)
			        mNotifyChange = false;
		        else
		        	mNotifyChange = true;
		        mDb.setTransactionSuccessful();
		        mDb.endTransaction();
		        onEndTransaction();
	        } else {
	            i = updateInTransaction(uri, contentvalues, s, as);
	            if(i > 0)
	            {
	                mNotifyChange = true;
	                return i;
	            }
	        }
        return i;
    } catch(Exception exception) {
        exception.printStackTrace();
        //mDb.endTransaction();
        //throw exception;
    }
        return 0;
    }

    protected abstract int updateInTransaction(Uri uri, ContentValues contentvalues, String s, String as[]);

    private static final int MAX_OPERATIONS_PER_YIELD_POINT = 500;
    private static final int SLEEP_AFTER_YIELD_DELAY = 4000;
    private static final String TAG = "SQLiteContentProvider";
    private final ThreadLocal mApplyingBatch = new ThreadLocal();
    protected SQLiteDatabase mDb;
    private volatile boolean mNotifyChange;
    private SQLiteOpenHelper mOpenHelper;
}
