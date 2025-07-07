package tech.johnnn.ossave.utils;

import android.content.Context;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import tech.johnnn.ossave.R;

public class TimeUtil {

    public static boolean isIntervalExpired(long lastTsMs, long intervalMs){
        long now = getTimeStampMillis();
        return (now - lastTsMs) >= intervalMs;
    }
    public static String timestampToLocalTime(Long timestampMillis,String format){
        Date date = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());//"yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String localTime = sdf.format(date);

        return localTime;
    }
    public static String timestampToLocalTime(String timestampMillis,String format){
        return timestampToLocalTime(Long.parseLong(timestampMillis),format);
    }

    public static String humanReadableNormalizedTime(Context context, Long timestampMillis){
        Long nowTimestampMillis = System.currentTimeMillis();
        Long timediff = (nowTimestampMillis-timestampMillis)/1000;
        if(timediff<1){
            return context.getString(R.string.time_now);//"Now";
        }

        if(timediff<60){
            return timediff+" "+(timediff<=1? context.getString(R.string.time_sec_ago):context.getString(R.string.time_secs_ago));
        }
        timediff/=60;
        if(timediff<60){
            return timediff+" "+(timediff<=1?context.getString(R.string.time_min_ago):context.getString(R.string.time_mins_ago));
        }
        timediff/=60;
        if(timediff<24){
            return timediff+" "+(timediff<=1?context.getString(R.string.time_hr_ago):context.getString(R.string.time_hrs_ago));
        }
        /*timediff/=24;
        if(timediff<7){
            return timediff+" day"+(timediff<=1?"":"s")+" ago";
        }*/

        return TimeUtil.timestampToLocalTime(timestampMillis, "hh:mm a dd MMM yyyy");
    }
    public static String humanReadableNormalizedTime(Context context, String timestampMillis) {
        return humanReadableNormalizedTime(context,Long.parseLong(timestampMillis));
    }

    public static String humanReadableTime(Context context, Long timestampMillis){
        Long nowTimestampMillis = System.currentTimeMillis();
        Long timediff = (nowTimestampMillis-timestampMillis)/1000;
        if(timediff<0){
            return "";
        }
        if(timediff<60){
            return timediff+" "+(timediff<=1?context.getString(R.string.time_sec_ago):context.getString(R.string.time_secs_ago));
        }
        timediff/=60;
        if(timediff<60){
            return timediff+" "+(timediff<=1?context.getString(R.string.time_min_ago):context.getString(R.string.time_mins_ago));
        }
        timediff/=60;
        if(timediff<24){
            return timediff+" "+(timediff<=1?context.getString(R.string.time_hr_ago):context.getString(R.string.time_hrs_ago));
        }
        timediff/=24;
        if(timediff<7){
            return timediff+" "+(timediff<=1?context.getString(R.string.time_day_ago):context.getString(R.string.time_days_ago));
        }
        if(timediff<30){
            return timediff/7+" "+((timediff/7)<=1?context.getString(R.string.time_week_ago):context.getString(R.string.time_weeks_ago));
        }
        if(timediff<365){
            return timediff/30+" "+((timediff/30)<=1?context.getString(R.string.time_month_ago):context.getString(R.string.time_months_ago));
        }
        if(timediff<36500){
            return timediff/365+" "+((timediff/365)<=1?context.getString(R.string.time_year_ago):context.getString(R.string.time_years_ago));
        }
        return TimeUtil.timestampToLocalTime(timestampMillis, "hh:mm a dd MMM yyyy");
    }

    public static String humanReadableTime(Context context,String timestampMillis){
        return humanReadableTime(context,Long.parseLong(timestampMillis));
    }

    public static String humanReadableTimeSecondPrecision(Long timestampMillis){
        return TimeUtil.timestampToLocalTime(timestampMillis, "ss")+"s "+TimeUtil.timestampToLocalTime(timestampMillis, "mm:hh a dd MMM yyyy");
    }
    public static String humanReadableTimeSecondPrecision(String timestampMillis){
        if(timestampMillis == null || timestampMillis.equals("")){
            return "";
        }
        return humanReadableTimeSecondPrecision(Long.parseLong(timestampMillis));
    }

    public static Long getTimeStampMillis(){
        return System.currentTimeMillis();
    }
    public static Long getTimeStampNanos(){
        return System.nanoTime();
    }
    public static String getTimeStampMillisString(){
        return String.valueOf(getTimeStampMillis());
    }

    public static long convertDurationToMillis(String duration) {
        String[] parts = duration.split("[:.]"); // Split by ':' and '.'
        long millis = 0;

        if (parts.length == 4) { // hh:mm:ss.SS
            millis += Integer.parseInt(parts[0]) * 3600000L; // Hours to milliseconds
            millis += Integer.parseInt(parts[1]) * 60000L;   // Minutes to milliseconds
            millis += Integer.parseInt(parts[2]) * 1000L;    // Seconds to milliseconds
            millis += Integer.parseInt(parts[3]);            // Milliseconds
        } else if (parts.length == 3) { // mm:ss.SS
            millis += Integer.parseInt(parts[0]) * 60000L;   // Minutes to milliseconds
            millis += Integer.parseInt(parts[1]) * 1000L;    // Seconds to milliseconds
            millis += Integer.parseInt(parts[2]);            // Milliseconds
        }else if (parts.length == 32) { // mm:ss.SS
            millis += Integer.parseInt(parts[1]) * 1000L;    // Seconds to milliseconds
            millis += Integer.parseInt(parts[2]);            // Milliseconds
        }

        return millis;
    }

    public static String convertMillisToDuration(long millis,boolean isStrict) {
        long hours = millis / 3600000;
        long minutes = (millis % 3600000) / 60000;
        long seconds = (millis % 60000) / 1000;
        long milliseconds = millis % 1000;

        if (hours > 0 || isStrict) {
            return String.format(Locale.ENGLISH,"%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
        } else {
            return String.format(Locale.ENGLISH,"%02d:%02d.%03d", minutes, seconds, milliseconds );
        }
    }



}
