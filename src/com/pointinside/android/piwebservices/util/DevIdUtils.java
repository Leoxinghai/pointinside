package com.pointinside.android.piwebservices.util;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DevIdUtils
{
  private static final String TAG = DevIdUtils.class.getSimpleName();
  private static String sHashedAndroidUUID;

  private static byte[] decodeHexString(String paramString)
  {
    int i = paramString.length();
    if ((i & 0x1) == 1)
    {
      paramString = "0" + paramString;
      i = paramString.length();
    }
    byte[] arrayOfByte = new byte[i / 2];
    for (int j = 0;; j += 2)
    {
      if (j >= i) {
        return arrayOfByte;
      }
      int k = Character.digit(paramString.charAt(j), 16) << 4;
      int m = Character.digit(paramString.charAt(j + 1), 16);
      arrayOfByte[(j / 2)] = ((byte)(k | m));
    }
  }

  private static String encodeHexString(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    StringBuilder localStringBuilder = new StringBuilder();
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return localStringBuilder.toString();
      }
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Byte.valueOf(paramArrayOfByte[j]);
      localStringBuilder.append(String.format("%02x", arrayOfObject));
    }
  }

  private static String getAndroidID(Context paramContext)
  {
    return Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
  }

  public static String getHashedUUID(Context paramContext)
  {
    String str;
    if (sHashedAndroidUUID == null)
    {
      str = makeHashedUUID(paramContext);
      if (str == null) {
          str = "";
      }
      sHashedAndroidUUID = str;
    }
      return sHashedAndroidUUID;
  }

  public static String getUUID(Context paramContext)
  {
    String str = getAndroidID(paramContext);
    if (str == null) {
      str = "emulator";
    }
    return str;
  }

  private static String makeHashedUUID(Context paramContext)
  {
    String str1 = getAndroidID(paramContext);
    if (str1 != null) {
      try
      {
        String str2 = encodeHexString(MessageDigest.getInstance("MD5").digest(decodeHexString(str1)));
        return str2;
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        Log.w(TAG, "Cannot hash device uuid", localNoSuchAlgorithmException);
      }
    }
    return null;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.util.DevIdUtils
 * JD-Core Version:    0.7.0.1
 */