package tech.johnnn.ossave.crypto;

import android.util.Base64;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;

public class CryptoUtils {
    private static String TAG = "CryptoUtils";
    public static byte[] intToBytes(int value) {
        // Create a ByteBuffer and put the int value into it
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(value);
        // Get the byte array from the ByteBuffer
        return buffer.array();
    }

    public static byte[] removePrependedNullBytes(byte[] bytes) {
        int startIdx = 0;
        while (startIdx < bytes.length && bytes[startIdx] == 0) {
            startIdx++;
        }
        return Arrays.copyOfRange(bytes, startIdx, bytes.length);
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        if(byteArray==null || byteArray.length==0)
            return "";
        StringBuilder hexStringBuilder = new StringBuilder(2 * byteArray.length);
        for (byte b : byteArray) {
            hexStringBuilder.append(String.format(Locale.ENGLISH,"%02X", b));
        }
        return hexStringBuilder.toString();
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] byteArray = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }

        return byteArray;
    }


    public static String byteArrayToBase64x(byte[] byteArray) {
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //Log.d(TAG,"base64-en:"+base64);
        base64 = base64.replace('/','_');
        base64 = base64.replace('+','.');

        int ind = base64.indexOf('=');
        String base64en = "";
        if(ind>=0){
            base64en = base64.substring(0,ind);
        }else{
            base64en = base64;
        }

        //Log.d(TAG,"base64-en:"+base64en);
        return base64en.trim();
    }

    public static byte[] base64xToByteArray(String base64) {
        base64=base64.trim();
        //Log.d(TAG,"base64-de:"+base64);
        int x = base64.length();

        int no_of_ek = (4-(x%4))%4; // UkRIa2MsYjxOd054SlU9MjU4ez9JJmFRR0Fj // ZD1Ba3NYJGomdkpFI0EyKA
        String ek = "";
        for(int i=0;i<no_of_ek;i++){
            ek+="=";
        }
        base64+=ek;
        base64 = base64.replace('_','/');
        base64 = base64.replace('.','+');
        //Log.d(TAG,"base64-de:"+x+":"+no_of_ek+":"+base64);
        return Base64.decode(base64, Base64.DEFAULT);
    }


    public static String base64ToBase64x( String base64) {
        //Log.d(TAG,"base64-en:"+base64);
        base64 = base64.replace('/','_');
        base64 = base64.replace('+','.');

        int ind = base64.indexOf('=');
        String base64x = "";
        if(ind>=0){
            base64x = base64.substring(0,ind);
        }else{
            base64x = base64;
        }
        return base64x.trim();
    }
    public static String base64xToBase64(String base64x) {
        base64x=base64x.trim();
        //Log.d(TAG,"base64-de:"+base64);
        int x = base64x.length();

        int no_of_ek = (4-(x%4))%4; // UkRIa2MsYjxOd054SlU9MjU4ez9JJmFRR0Fj // ZD1Ba3NYJGomdkpFI0EyKA
        String ek = "";
        for(int i=0;i<no_of_ek;i++){
            ek+="=";
        }
        base64x+=ek;
        base64x = base64x.replace('_','/');
        base64x = base64x.replace('.','+');
        //Log.d(TAG,"base64-de:"+x+":"+no_of_ek+":"+base64);
        return base64x;//Base64.decode(base64x, Base64.DEFAULT);
    }



//    public static String bigIntegerToHexString(BigInteger num){
//        return  byteArrayToHexString(num.toByteArray());
//    }
    public static String bigIntegerToBase64x(BigInteger num){
        return  byteArrayToBase64x(num.toByteArray());
    }

    public static BigInteger byteArrayToBigInteger(byte[] arr){
        return new BigInteger(1,arr);
    }

    public static byte[] Long2bytes(long value) {
        byte[] byteArray = ByteBuffer.allocate(Long.BYTES).putLong(value).array();
        return byteArray;
    }

}
