package tech.johnnn.ossave.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class other {

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
