package tech.johnnn.ossave.Log;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

//import tech.johnnn.ossave.file.ExternalFileHandler;
//import tech.johnnn.ossave.file.FileOperation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//wrapper class on Log
public class Log {
    private static Context context;
    private static boolean isEnable = false;
    private static final String TAG = "FileLogger";
    private static final String LOG_FILE_NAME = "app_logs.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    public static  void setContext(Context c){
        context = c;
    }
    public static void writeToFile( String filename,String data){

        try {
            File file = new File(context.getFilesDir(), filename);
            FileOutputStream fos = new FileOutputStream(file,true);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void exportLog(){
//        try {
//            File src = new File(context.getFilesDir(), LOG_FILE_NAME);
//            File dst = new File(ExternalFileHandler.getPictureDirectory(), LOG_FILE_NAME+".svg");
//            FileOperation.copyFile(src,dst);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static void deleteLog(){
//        try {
//            //File src = new File(MainActivity.staticContext.getFilesDir(), LOG_FILE_NAME);
//            File dst = new File(ExternalFileHandler.getPictureDirectory(), LOG_FILE_NAME);
//            //src.delete();
//            dst.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    public static void L( String type, String tag,String message) {
        if(isEnable) {
            String formattedMessage = type + " " + dateFormat.format(new Date()) + " - " + tag + " - " + message + "\n";
            writeToFile(LOG_FILE_NAME, formattedMessage);
        }
    }

    public static void d(LinearLayout view, String tag, String message){
        ActualLog.d(tag,message);
        L("d",tag,message);

        if(view!=null) {
            String formattedMessage = "" + dateFormat.format(new Date()) + ":" + tag + ":" + message + "";

            TextView textView = new TextView(view.getContext());
            textView.setTextColor(Color.CYAN);
            textView.setText(formattedMessage);
            textView.setHorizontallyScrolling(true);
            view.addView(textView);
        }
    }
    public static void d(LinearLayout view,String tag,String subTag,String message){
        d(view,tag,subTag+":"+message);
    }

    public static void d(String tag,String message){
        ActualLog.d(tag,message);
//        L("D",tag,message);
    }

    public static void v(String tag,String message){
        ActualLog.v(tag,message);
        L("V",tag,message);
    }

    public static void e(String tag,String message){
        ActualLog.e(tag,message);
        L("E",tag,message);
    }
    public static void d(String tag,String subTag,String message){
        d(tag,subTag+":"+message);
    }

    public static void v(String tag,String subTag,String message){
        v(tag,subTag+":"+message);
    }

    public static void e(String tag,String subTag,String message){
        e(tag,subTag+":"+message);
    }

    public static void sp(String message){
        System.out.println(message);
    }
}
