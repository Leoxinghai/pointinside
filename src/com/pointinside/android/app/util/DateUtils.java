// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.pointinside.android.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class DateUtils
{

    public DateUtils()
    {
    }

    public static String formatRelativeDate(Context context, long l, long l1)
    {
        Time time = new Time();
        time.set(l);
        Time time1 = new Time();
        time1.set(l1);
        int i = Time.getJulianDay(l, time.gmtoff);
        int j = Math.abs(Time.getJulianDay(l1, time1.gmtoff) - i);
        boolean flag;
        Resources resources;
        if(l1 > l)
            flag = true;
        else
            flag = false;
        resources = context.getResources();
        if(j == 0)
            return resources.getString(0x7f06005f);
        if(!flag)
        {
            if(j == 1)
                return resources.getString(0x7f060060);
            if(j < 7)
                return android.text.format.DateUtils.formatDateTime(context, l, 2);
        }
        return android.text.format.DateUtils.formatDateTime(context, l, 16);
    }

    public static String getFormattedTime(String s)
    {
        java.util.Date date;
        try
        {
            date = TIME_PARSER.parse(s);
        }
        catch(ParseException parseexception)
        {
            return s;
        }
        return TIME_FORMATTER.format(date);
    }

    public static String getLongDate(String s)
    {
        java.util.Date date;
        try
        {
            date = DB_FORMATTER.parse(s);
        }
        catch(ParseException parseexception)
        {
            return s;
        }
        return LONG_DATE_FORMATTER.format(date);
    }

    public static String getShortDate(String s)
    {
        java.util.Date date;
        try
        {
            date = DB_FORMATTER.parse(s);
        }
        catch(ParseException parseexception)
        {
            return s;
        }
        return SHORT_DATE_FORMATTER.format(date);
    }

    public static String getTimeFromMinutes(int i)
    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        gregoriancalendar.set(11, 0);
        gregoriancalendar.set(12, i);
        return TIME_FORMATTER.format(gregoriancalendar.getTime());
    }

    public static final SimpleDateFormat DB_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final SimpleDateFormat DEAL_PARSER = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat LONG_DATE_FORMATTER = new SimpleDateFormat("M/d/yy h:mm a");
    public static final SimpleDateFormat SHORT_DATE_FORMATTER = new SimpleDateFormat("M/d/yy");
    public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("h:mm a");
    public static final SimpleDateFormat TIME_PARSER = new SimpleDateFormat("hh:mm:ss");

}
