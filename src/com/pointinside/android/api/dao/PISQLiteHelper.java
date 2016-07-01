package com.pointinside.android.api.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class PISQLiteHelper
{
  private SQLiteDatabase mDatabase = null;
  private String mDbPath;
  private boolean mIsInitializing = false;

  public PISQLiteHelper(String paramString)
  {
    this.mDbPath = paramString;
  }

  private SQLiteDatabase openDatabase(int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory)
    throws SQLiteException
  {
    return SQLiteDatabase.openDatabase(this.mDbPath, paramCursorFactory, 0);
  }

  public void close()
  {
    try
    {
      if (this.mIsInitializing) {
        throw new IllegalStateException("Closed during initialization");
      }
    }
    finally {}
    if ((this.mDatabase != null) && (this.mDatabase.isOpen()))
    {
      this.mDatabase.close();
      this.mDatabase = null;
    }
  }

  public synchronized SQLiteDatabase getReadableDatabase()
	        throws SQLiteException
	    {
			try {  
	  		SQLiteDatabase sqlitedatabase1 = null;      
			  if(mDatabase == null || !mDatabase.isOpen()) {
	    	        if(mIsInitializing)
	    	            throw new IllegalStateException("getReadableDatabase called recursively");
		        SQLiteDatabase sqlitedatabase = null;
		        mIsInitializing = true;
		        sqlitedatabase = openDatabase(1, null);
		        mDatabase = sqlitedatabase;
		        sqlitedatabase1 = mDatabase;
		        mIsInitializing = false;
		        if(sqlitedatabase != null) {
		        	if(sqlitedatabase != mDatabase) 
		        		sqlitedatabase.close();
		        }
	        	
	        } else {
	        	sqlitedatabase1 = mDatabase;
	        }
	        
	        return sqlitedatabase1;
			}  catch(Exception ex) {
		        mIsInitializing = false;
				ex.printStackTrace();
		        Log.e("PIMaps", "Error getting readable database", ex);
		        throw new SQLiteException(ex.toString());
			}
			/*
	        mIsInitializing = false;
	        if(sqlitedatabase == null)
		        if(sqlitedatabase != mDatabase)
		            sqlitedatabase.close();
		            */
	    }
  }


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.dao.PISQLiteHelper
 * JD-Core Version:    0.7.0.1
 */