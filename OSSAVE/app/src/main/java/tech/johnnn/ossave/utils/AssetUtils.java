package tech.johnnn.ossave.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class AssetUtils {
    public static String readAssetFile(Context context, String fileName) {
        String content = "";
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            content = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void copyAssetToInternalStorage(Context context, String assetFileName) {
        try {
            File file = new File(context.getFilesDir(), assetFileName);
            if (!file.exists()) {
                InputStream in = context.getAssets().open(assetFileName);
                OutputStream out = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }

                in.close();
                out.flush();
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //return file;
    }
}
