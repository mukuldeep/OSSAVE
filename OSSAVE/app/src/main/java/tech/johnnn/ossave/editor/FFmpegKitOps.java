package tech.johnnn.ossave.editor;


import android.content.Context;
import android.graphics.Rect;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Session;

import java.util.ArrayList;
import java.util.Locale;

import tech.johnnn.ossave.Log.Log;
import tech.johnnn.ossave.file.InternalFileHandler;

public class FFmpegKitOps {
    private static String TAG = "FFmpegKitOps";
    InternalFileHandler ifh;

    public FFmpegKitOps(Context context){
        ifh = new InternalFileHandler(context);
    }

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


    /**
     *
     * @param inputPath full path of input video file
     * @param outputPath full path of output video file
     * @param cropWidth cropping frame width
     * @param cropHeight cropping frame height
     * @param xOffset x coordinate of top left corner of cropping frame
     * @param yOffset y coordinate of top left corner of cropping frame
     * @return operation status i.e.  1: success, 0: failure, -1: otherwise
     */
    public int cropVideo(String inputPath, String outputPath, int cropWidth,int cropHeight,int xOffset,int yOffset){
        Log.d(TAG,"in crop!");
        int rc = -1;
        String cropArea = cropWidth+":"+cropHeight+":"+xOffset+":"+yOffset;
        String ffmpegCommand = String.format(Locale.ENGLISH,"-i %s -c:v libx264 -avoid_negative_ts make_zero -c:a aac -preset ultrafast -crf 23 -vf crop=%s -f mpegts %s",
                inputPath,
                cropArea,
                outputPath
        );

        // Execute FFmpeg command synchronously
        Session session = FFmpegKit.execute(ffmpegCommand);

        ReturnCode returnCode = session.getReturnCode();
        if (returnCode.isValueSuccess()) {
            rc = 1;
            Log.d(TAG,"crop successfully!");
        } else if (returnCode.isValueError()) {
            rc = 0;
            Log.e(TAG,"Error copy cropping: " + session.getAllLogsAsString());
        }

        //System.gc();  // Optional: only if doing many ops back-to-back
        return rc;
    }


    /**
     * Concats all the video as per index in the list
     * @param projectFolderName project folder where a text file to be created
     * @param videoFilePaths list of all video file full paths
     * @param outputPath full path of the output video
     * @return success or error code
     */
    public int concatVideos(String projectFolderName, ArrayList<String> videoFilePaths, String outputPath){
        Log.d(TAG,"concat called!");
        int rc = -1;

        String video_txt_content="";
        for (String videoFilePath : videoFilePaths){
            video_txt_content += "file '"+videoFilePath+"'\n";
        }

        ifh.createSubdirectoryInInternalStorage(ifh.DIR_PROJECT,projectFolderName);
        ifh.writeToFile(ifh.DIR_PROJECT+"/"+projectFolderName,"videos.txt", video_txt_content);

        String videos_file_path = ifh.getInternalDirPath()+"/"+ifh.DIR_PROJECT+"/"+projectFolderName+"/videos.txt";

        String ffmpegCommand = String.format(Locale.ENGLISH,"-f concat -safe 0 -i %s -c:v libx264 -c:a aac -avoid_negative_ts make_zero -preset ultrafast %s", videos_file_path, outputPath);

        // Execute FFmpeg command synchronously
        Session session = FFmpegKit.execute(ffmpegCommand);

        // Check the return code for success or error
        ReturnCode returnCode = session.getReturnCode();
        if (returnCode.isValueSuccess()) {
            rc = 1;
            Log.d(TAG,"concat successful!");
        } else if (returnCode.isValueError()) {
            rc = 0;
            Log.e(TAG,"Error concat: " + session.getAllLogsAsString());
        }

        return rc;
    }


    /**
     * Replaces video audio track with given audio
     * @param videoFilePath full path of input video
     * @param backgroundAudioPath full path of input audio
     * @param outputVideoPath full path for output video
     * @return success or error code
     */
    public int addBackgroundAudio(String videoFilePath, String backgroundAudioPath,String outputVideoPath){
        Log.d(TAG,"adding bg audio!");
        int rc = -1;

        String ffmpegCommand = String.format(
                "-i %s -i %s -map 0:v:0 -map 1:a:0 -c:v libx264 -preset ultrafast -c:a aac -b:a 192k %s",
                videoFilePath, backgroundAudioPath, outputVideoPath
        );

        // Execute FFmpeg command synchronously
        Session session = FFmpegKit.execute(ffmpegCommand);

        // Check the return code for success or error
        ReturnCode returnCode = session.getReturnCode();
        if (returnCode.isValueSuccess()) {
            rc = 1;
            Log.d(TAG,"bg add successful!");
        } else if (returnCode.isValueError()) {
            rc = 0;
            Log.e(TAG,"Error while adding bg music: " + session.getAllLogsAsString());
        }

        return rc;

    }



}
