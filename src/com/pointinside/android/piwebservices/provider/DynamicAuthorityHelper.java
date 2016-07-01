package com.pointinside.android.piwebservices.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;

public class DynamicAuthorityHelper
{
  private final String mAuthority;
  private final Uri mBaseUri;

  public DynamicAuthorityHelper(Context paramContext, Class<?> paramClass, String paramString)
  {
    String str1 = paramContext.getPackageName() + paramString;
    PackageManager localPackageManager = paramContext.getPackageManager();
    String str2 = paramClass.getName();
    ProviderInfo localProviderInfo = localPackageManager.resolveContentProvider(str1, 0);
    if ((localProviderInfo == null) || (!localProviderInfo.name.equals(str2))) {
//      throw new IllegalArgumentException("Missing provider with authority=" + str1 + " and name=" + str2);
    }
    this.mAuthority = str1;
    this.mBaseUri = Uri.parse("content://" + getAuthority());
  }

  public String getAuthority()
  {
    return this.mAuthority;
  }

  public Uri getBaseUri()
  {
    return this.mBaseUri;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.provider.DynamicAuthorityHelper
 * JD-Core Version:    0.7.0.1
 */