package com.pointinside.android.api.content;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.pointinside.android.api.*;
import com.pointinside.android.api.content.PIContentManager.ContentType;;

class ContentManagerUtils
{
  private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");
  static Random sRandom = new Random(SystemClock.uptimeMillis());

  public static boolean copyFile(File paramFile1, File paramFile2)
  {
    try
    {
		FileInputStream localFileInputStream = new FileInputStream(paramFile1);
		boolean bool = copyToFile(localFileInputStream, paramFile2);
		localFileInputStream.close();
		return bool;
    } catch (IOException localIOException) {
    	return false;
    }
  }

  public static boolean copyToFile(InputStream inputstream, File file)
  {
      FileOutputStream fileoutputstream;
      byte abyte0[];
      int i;
      try
      {
          fileoutputstream = new FileOutputStream(file);
          abyte0 = new byte[4096];
	      for(;;) {
		      i = inputstream.read(abyte0);
		      if(i < 0) {
			      fileoutputstream.close();
			      return true;
		      }
		      fileoutputstream.write(abyte0, 0, i);
	      }

      } catch(IOException ioexception)
      {
          return false;
	  }
  }

  public static boolean deleteDir(File paramFile)
  {
    String[] arrayOfString;
    if (paramFile.isDirectory()) {
      arrayOfString = paramFile.list();
		for (int i = 0;; i++)
		{
		  if (i >= arrayOfString.length) {
		    return paramFile.delete();
		  }
		  if (!deleteDir(new File(paramFile, arrayOfString[i]))) {
		    return false;
		  }
		}
    }
    return false;
  }

  static final boolean discardPurgeableFiles(Context paramContext, long paramLong)
  {
    return false;
  }

  public static boolean fileExists(String s)
  {
      File file = new File(s);
      return file.exists();

      /*
	  FileInputStream fileinputstream;
      try
      {
          fileinputstream = new FileInputStream(new File(s));
      }
      catch(FileNotFoundException filenotfoundexception)
      {
          IOException ioexception;
          Exception exception;
          if(false)
              try
              {
                  null.close();
              }
              catch(IOException ioexception2) { }
          return false;
      }
      if(fileinputstream != null)
          try
          {
              fileinputstream.close();
          }
          // Misplaced declaration of an exception variable
          catch(IOException ioexception) { }
      return true;
      exception;
      if(false)
          try
          {
              null.close();
          }
          catch(IOException ioexception1) { }
      throw exception;
 	*/
  }

  static DownloadFileInfo generateSaveFile(Context context, String s, PIContentManager.ContentType contenttype, int i)
	        throws FileNotFoundException
	    {
	        StatFs statfs;
	        File file1;
	        int j;
	        String s2;
	        DownloadFileInfo downloadfileinfo;
	        DownloadFileInfo downloadfileinfo1;

	        File file = context.getCacheDir();
	        statfs = new StatFs(file.getPath());
	        file1 = new File((new StringBuilder(String.valueOf(file.getPath()))).append(getFileDir(contenttype)).toString());
	        if(!file1.isDirectory() && !file1.mkdir())
	            file1 = context.getCacheDir();
	        j = statfs.getBlockSize();

        	int k = statfs.getAvailableBlocks();
	        if((long)j * ((long)k - 4L) < (long)i) {
		        if(!discardPurgeableFiles(context, (long)i - (long)j * ((long)k - 4L)))
		            return new DownloadFileInfo(null, null, 492);
		        statfs.restat(file1.getPath());
	        }
	        try
	        {
	            String s1 = Uri.parse(s).getLastPathSegment();
	            s2 = (new StringBuilder(String.valueOf(file1.getPath()))).append(File.separator).append(s1).toString();
		        if(s2 == null)
		        	downloadfileinfo = new DownloadFileInfo(s2, new FileOutputStream(s2), 0);
		        else
		            downloadfileinfo = new DownloadFileInfo(s2, new FileOutputStream(s2), 0);
		        	
		        return downloadfileinfo;
	        }
	        catch(Exception exception)
	        {
	            return new DownloadFileInfo(null, null, 492);
	        }
	    }

  static String getDownloadDirectory(Context paramContext, boolean paramBoolean)
  {
    if (paramBoolean) {
      return Environment.getExternalStorageDirectory().getPath() + "/PI";
    }
    return paramContext.getFilesDir().getPath() + "/PI";
  }

  private static String getFileDir(PIContentManager.ContentType paramContentType)
  {
    switch (paramContentType)
    {
    case VENUE_PDEMAP:
        return "/venue_pdemap";
    case DATASET:
        return "/dataset";
    case VENUE_IMAGE:
      return "/venue_image";
    case PLACE_IMAGE:
        return "/place_image";
    case VENUE_ZONE_IMAGE:
        return "/zone_images";
    case PROMOTION_IMAGE:
        return "/promo_image";
    case REFERENCE:
        return "/reference";
    case ALL_DATASETS:
    	return "/all_datasets";
    case UNKNOWN:
    default:
        throw new IllegalStateException("Unknown Type: " + paramContentType);
    }
  }

  static File getStorageDirectory(Context paramContext, String paramString, PIContentManager.ContentType paramContentType)
  {
    File localFile3;
    if (paramContentType == PIContentManager.ContentType.REFERENCE) {
      localFile3 = paramContext.getFilesDir();
    }

    if (Environment.getExternalStorageState().equals("mounted")) {}
      File localFile1;
      for (String str = Environment.getExternalStorageDirectory().getPath();; str = paramContext.getFilesDir().getPath())
      {
        localFile1 = new File(str + "/PI");
        if ((localFile1.isDirectory()) || (localFile1.mkdir())) {
          break;
        }
        return null;
      }
      File localFile2 = new File(localFile1.getAbsolutePath() + "/" + paramString);
      if ((!localFile2.isDirectory()) && (!localFile2.mkdir())) {
        return null;
      }
      localFile3 = new File(localFile2.getAbsolutePath() + getFileDir(paramContentType));
      if(((localFile3.isDirectory()) || (localFile3.mkdir())))
    	  return localFile3;
    return null;
  }

  static boolean isFilenameSafe(File paramFile)
  {
    return SAFE_FILENAME_PATTERN.matcher(paramFile.getPath()).matches();
  }

  static boolean isFilenameValid(Context paramContext, String paramString)
  {
    File localFile1 = new File(paramString).getParentFile();
    int i;
    if ((!localFile1.equals(paramContext.getFilesDir())) && (!localFile1.equals(new File(Environment.getExternalStorageDirectory() + "/PI"))) && (!localFile1.equals(paramContext.getCacheDir())))
    {
      i = 0;
      if (i == 0) {
    	    File localFile2;
    	      i = 1;
    	      localFile2 = localFile1.getParentFile();
    	    if((localFile2.equals(paramContext.getFilesDir())) || (localFile2.equals(new File(Environment.getExternalStorageDirectory() + "/PI"))) || (localFile2.equals(paramContext.getCacheDir())));
    	      return true;
      }
    }
    return false;
  }

  public static boolean isNetworkAvailable(Context paramContext)
  {
    return true;
  }

  public static boolean isNetworkRoaming(Context paramContext)
  {
    return false;
  }

  public static boolean isOnSDCard(String paramString)
  {
    if (paramString != null) {
      return paramString.contains(Environment.getExternalStorageDirectory().getPath());
    }
    return false;
  }

  public static boolean isSDCardAvailable()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }

  public static void removeCachedZipFiles(Context context)
  {
      File afile[] = context.getCacheDir().listFiles();
      if(afile == null) 
    	  return;
      
      HashSet hashset;
      int i;
      hashset = new HashSet();
      i = 0;
      for(;i<afile.length;i++) {
		  if(!afile[i].getName().equals("lost+found") && !afile[i].getName().equalsIgnoreCase("recovery"))
	          hashset.add(afile[i].getPath());
      }

      Iterator iterator = hashset.iterator();
      while(iterator.hasNext()) 
      {
          File file = new File((String)iterator.next());
          if(file.isDirectory())
              deleteDir(file);
          else
              file.delete();
      }
  }

  static boolean unzipFile(Context paramContext, String paramString, long paramLong, PIContentManager.ContentType paramContentType, InputStream paramInputStream, File paramFile)
  {
    if (paramInputStream == null) {
      return false;
    }
    if ((paramFile == null) || (!paramFile.isDirectory())) {
      return false;
    }
    long l = System.currentTimeMillis();

    ZipInputStream localZipInputStream;
    ZipEntry localZipEntry;
    byte[] arrayOfByte;
    String str;
    File localFile;
    int i;
    int j;

      try
      {
        localZipInputStream = new ZipInputStream(paramInputStream);
        localZipEntry = localZipInputStream.getNextEntry();
        if (localZipEntry == null)
        {
          localZipInputStream.close();
          return true;
        }

        arrayOfByte = new byte[4096];
        str = localZipEntry.getName();
        if ((str.contains(".sqlite")) && (!str.equals("reference.sqlite"))) {
          str = paramString;
        }
        localFile = new File(paramFile, str);
        FileOutputStream localObject6 = new FileOutputStream(localFile);
        i = 0;
        for(;;) {
	        j = localZipInputStream.read(arrayOfByte, 0, 4096);
	        if (j < 0)
	        {
	          PIContentManager.insertFile(paramContext, localFile.getPath(), str, paramString, i, paramLong, paramContentType, l);
	          if (localObject6 == null) {}
	          break;
	        }
	        i += j;
	        ((OutputStream)localObject6).write(arrayOfByte, 0, j);
        }
        ((OutputStream)localObject6).close();
        localZipInputStream.close();

        if (localObject6 != null) {
            ((OutputStream)localObject6).close();
          }
      }
      catch (IOException localException)
      {
   // 	    throw localException;
        return false;
      }
      return true;
  }

  static class DownloadFileInfo
  {
    String mFileName;
    int mStatus;
    FileOutputStream mStream;

    DownloadFileInfo(String paramString, FileOutputStream paramFileOutputStream, int paramInt)
    {
      this.mFileName = paramString;
      this.mStream = paramFileOutputStream;
      this.mStatus = paramInt;
    }
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.api.content.ContentManagerUtils
 * JD-Core Version:    0.7.0.1
 */