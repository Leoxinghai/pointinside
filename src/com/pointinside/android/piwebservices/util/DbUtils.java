package com.pointinside.android.piwebservices.util;

import android.text.TextUtils;

public class DbUtils
{
  public static String concatWhere(String... paramVarArgs)
  {
    int i = paramVarArgs.length;
    if (i > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      for (int j = 0;; j++)
      {
        if (j >= i) {
          return localStringBuilder.toString();
        }
        if (!TextUtils.isEmpty(paramVarArgs[j]))
        {
          localStringBuilder.append("(").append(paramVarArgs[j]).append(")");
          if ((i > 1) && (j < i - 1)) {
            localStringBuilder.append(" AND ");
          }
        }
      }
    }
    return null;
  }
}


/* Location:           D:\xinghai\dex2jar\classes-dex2jar.jar
 * Qualified Name:     com.pointinside.android.piwebservices.util.DbUtils
 * JD-Core Version:    0.7.0.1
 */