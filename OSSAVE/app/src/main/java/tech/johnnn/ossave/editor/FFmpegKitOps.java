package tech.johnnn.ossave.editor;


import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Session;

import java.util.Locale;

import tech.johnnn.ossave.Log.Log;

public class FFmpegKitOps {
    private static String TAG = "FFmpegKitOps";

    public FFmpegKitOps(){}

    /**
     *
     * @param inputPath full path for a video file i.e. mp4 tested working
     * @param outputPath output path should be .ts file i.e. intermediate file for the renderer
     * @param fromTs From timestamp for trimming logic
     * @param toTs To timestamp for trimming logic
     * @param durationTs time duration for trimming logic
     * @return
     */
    public int trimVideoSync(String inputPath, String outputPath, String fromTs, String toTs, String durationTs){
        Log.d(TAG,"trim video called! ");
        int rc = -1;

        String ffmpegCommand = String.format(Locale.ENGLISH,
                "-i %s -ss %s -t %s -an -c:v libx264 -r 60 -preset ultrafast -crf 23 -movflags +faststart -avoid_negative_ts make_zero -f mpegts %s", //-crf 18 for quality control
                inputPath, fromTs,durationTs, outputPath);

        // Execute FFmpeg command synchronously
        Session session = FFmpegKit.execute(ffmpegCommand);

        // Check the return code for success or error
        ReturnCode returnCode = session.getReturnCode();
        if (returnCode.isValueSuccess()) {
            rc = 1;
            Log.d(TAG, "trimmed successfully! " + outputPath);
        } else if (returnCode.isValueError()) {
            rc = 0;
            Log.e(TAG, "Error copy trimming: " + session.getAllLogsAsString());
        }


        return rc;

    }


    /**
     *
     * @param inputPath full path for input audio file
     * @param outputPath full path for output audio file
     * @param fromTs from time stamp of trimming logic
     * @param toTs to time stamp of trimming logic
     * @param totalDuration total duration in seconds of trimming logic
     * @return
     */
    public int trimAudio(String inputPath, String outputPath, String fromTs, String toTs, String totalDuration){
        Log.d(TAG,"copy trim audio called!");
        int rc = -1;
        Log.d(TAG,"copyTrimAudio from:"+fromTs+" totalDuration:"+totalDuration);
        String ffmpegCommand = String.format(Locale.ENGLISH,
                "-i %s -af \"atrim=start=%s:duration=%s\" -c:a pcm_s16le %s",
                inputPath, fromTs, totalDuration, outputPath);

        // Execute FFmpeg command synchronously
        Session session = FFmpegKit.execute(ffmpegCommand);

        // Check the return code for success or error
        ReturnCode returnCode = session.getReturnCode();
        if (returnCode.isValueSuccess()) {
            rc = 1;
            Log.d(TAG,"trimmed audio successfully!");
        } else if (returnCode.isValueError()) {
            rc = 0;
            Log.e(TAG,"Error trimming audio: " + session.getAllLogsAsString());
        }

        return rc;
    }


}
