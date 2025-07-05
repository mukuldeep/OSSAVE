package tech.johnnn.ossave.utils;

import android.content.Context;
import android.util.TypedValue;

import java.util.Locale;

public class OtherUtils {

    public static String byteToReadableSize(String sBytes, String sDigitsAfterDecimal){
        String ans = "0 B";
        try{
            long nBytes = Long.parseLong(sBytes);
            ans = byteToReadableSize(nBytes,sDigitsAfterDecimal);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ans;
    }
    public static String byteToReadableSize(long nBytes, String significantDigitAfterDecimal){
        if(nBytes<1024){
            return nBytes+" B";
        }
        String ans="";
        Double currFactor = nBytes/1024.0;
        if(currFactor<1024){
            ans = String.format(Locale.ENGLISH,"%."+significantDigitAfterDecimal+"f",currFactor)+" KB";
            return ans;
        }

        currFactor = currFactor/1024;
        if(currFactor<1024){
            ans = String.format(Locale.ENGLISH,"%."+significantDigitAfterDecimal+"f",currFactor)+" MB";
            return ans;
        }

        currFactor = currFactor/1024;
        if(currFactor<1024){
            ans = String.format(Locale.ENGLISH,"%."+significantDigitAfterDecimal+"f",currFactor)+" GB";
            return ans;
        }

        currFactor = currFactor/1024;
        ans = String.format(Locale.ENGLISH,"%."+significantDigitAfterDecimal+"f",currFactor)+" TB";
        return ans;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static int pxToDp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }
    public static float spToPx(Context context, float sp){
        float pxValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return pxValue;
    }

    public static int dpToDisplayMatrix(Context context,int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public static int dp2dm(Context context,int dp){
        return dpToDisplayMatrix(context,dp);
    }



}
