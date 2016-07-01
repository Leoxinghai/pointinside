package com.pointinside.android.piwebservices.net;

import android.net.Uri;
import android.net.Uri.Builder;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

public class WebServiceDescriptor
{
  public final String apiKey;
  public final Uri baseUri;
  public final String password;
  public final String username;

  public WebServiceDescriptor(Uri paramUri)
  {
    this(paramUri, null, null);
  }

  public WebServiceDescriptor(Uri paramUri, String paramString)
  {
    this(paramUri, null, null, paramString);
  }

  public WebServiceDescriptor(Uri paramUri, String paramString1, String paramString2)
  {
    this(paramUri, paramString1, paramString2, null);
  }

  public WebServiceDescriptor(Uri paramUri, String paramString1, String paramString2, String paramString3)
  {
    this.baseUri = paramUri;
    this.username = paramString1;
    this.password = paramString2;
    this.apiKey = paramString3;
  }

  public AuthScope getAuthScope()
  {
    return new AuthScope(this.baseUri.getHost(), this.baseUri.getPort());
  }

  public Credentials getCredentials()
  {
    if ((this.username == null) || (this.password == null)) {
      return null;
    }
    return new UsernamePasswordCredentials(this.username, this.password);
  }

  public Uri.Builder getMethodUriBuilder(String paramString)
  {
    return this.baseUri.buildUpon().appendPath(paramString);
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.net.WebServiceDescriptor
 * JD-Core Version:    0.7.0.1
 */