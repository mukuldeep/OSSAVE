package tech.johnnn.ossave.Log;

import android.util.Log;
public class ActualLog {

    private static final boolean isEnable = true;//true iff logging enabled

    public static void d(String tag,String message){
        if(isEnable){
            Log.d(tag,message);
        }
    }
    public static void v(String tag,String message){
        if(isEnable){
            Log.v(tag,message);
        }
    }
    public static void e(String tag,String message){
        if(isEnable){
            Log.e(tag,message);
        }
    }
}
