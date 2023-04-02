package com.monitor.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public final static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public final static ThreadLocal<DateFormat> threadLocal_ = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            return df;
        }
    };


    public static Date parse(String dateStr,  ThreadLocal<DateFormat>tl) throws ParseException {
        return tl.get().parse(dateStr);
    }


    public static String format(Date date,  ThreadLocal<DateFormat>tl) {
        return tl.get().format(date);
    }



    public static String doubleInterval( String interval){
        int p=0;
        char[] chs = interval.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<chs.length;i++){
            switch (chs[i]){
                case 's':
                    sb.append( Long.parseLong(interval.substring(p,i)) * 2) ;
                    sb.append("s");
                    p=i;
                    p++;
                    break;
                case 'm' :
                    sb.append( Long.parseLong(interval.substring(p,i)) * 2) ;
                    sb.append("m");
                    p=i;
                    p++;
                    break;
                case 'h' :
                    sb.append( Long.parseLong(interval.substring(p,i)) * 2) ;
                    sb.append("h");
                    p=i;
                    p++;
                    break;
                case 'd' :
                    sb.append( Long.parseLong(interval.substring(p,i)) * 2) ;
                    sb.append("d");
                    p=i;
                    p++;
                    break;
                default :

                    break;
            }
        }
        return sb.toString();

    }

    public static long stringToLong( String timeStr){
        long res =0;
        int p=0;
        char[] chs = timeStr.toCharArray();
        for(int i=0;i<chs.length;i++){
            switch (chs[i]){
                case 's':
                    res += Long.parseLong(timeStr.substring(p,i)) * 1000 ;
                    p=i;
                    p++;
                    break;
                case 'm' :
                    res += Long.parseLong(timeStr.substring(p,i)) * 60 * 1000 ;
                    p=i;
                    p++;
                    break;
                case 'h' :
                    res += Long.parseLong(timeStr.substring(p,i)) * 60 * 60 * 1000 ;
                    p=i;
                    p++;
                    break;
                case 'd' :
                    res += Long.parseLong(timeStr.substring(p,i)) * 24 * 60 * 60 * 1000 ;
                    p=i;
                    p++;
                    break;
                default :

                    break;
            }
        }
        return res;
    }

}
