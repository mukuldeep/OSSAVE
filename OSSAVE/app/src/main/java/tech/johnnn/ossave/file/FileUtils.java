package tech.johnnn.ossave.file;

import android.content.Context;
import android.net.Uri;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import tech.johnnn.ossave.Log.Log;

/*
 Used for video edit activity to save video to internal storage
 */
public class FileUtils {


    //todo should run in background for larger files otherwise fail
    public static boolean saveFileFromUriToInternalStorage(Context context, Uri fileUri, String subDir, String fileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = context.getContentResolver().openInputStream(fileUri);
            if (inputStream == null) {
                Log.e("FileUtils", "Failed to open input stream for the URI.");
                return false;
            }

            File internalDir = context.getFilesDir();
            File postsFolder = new File(internalDir, subDir);
            File outputFile = new File(postsFolder, fileName);

            outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[512*1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            return true; // Successfully saved the video to internal storage.
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("FileUtils", "Error saving file to internal storage: " + e.getMessage());
            return false; // Error occurred while saving the video.
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                Log.e("FileUtils", "Error closing streams: " + e.getMessage());
            }
        }
    }
}
