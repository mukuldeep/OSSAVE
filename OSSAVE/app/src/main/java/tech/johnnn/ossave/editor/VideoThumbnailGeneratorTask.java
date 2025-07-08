package tech.johnnn.ossave.editor;

import android.os.AsyncTask;
import android.widget.ImageView;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;

import java.util.Locale;

import tech.johnnn.ossave.Log.Log;

public class VideoThumbnailGeneratorTask extends AsyncTask<String, Void, Integer> {
    private OnVideoThumbGenerate listener;
    private static final String TAG = "VideoThumbnailGeneratorTask";

    String inputFilePath;
    String outputFilePath;

    ImageView imageView;

    public VideoThumbnailGeneratorTask(){

    }
    public VideoThumbnailGeneratorTask(final ImageView imageView, OnVideoThumbGenerate listener){
        this.imageView = imageView;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(String... params) {
        int rc=-1;

        try {
            inputFilePath = params[0];
            outputFilePath = params[1];

            rc = generateThumb(inputFilePath,outputFilePath);
        }catch (Exception e){
            e.printStackTrace();
        }

        return rc;
    }

    private void notifyResult(String res){
        if (listener != null) {
            listener.onVideoThumbGenerate(res);
        }
    }

    @Override
    protected void onPostExecute(Integer rc) {
        if (rc == 1) {
            // Success
            Log.d(TAG, "Command execution completed successfully.");
            notifyResult("success");

        } else if (rc == -1) {
            // Cancelled
            Log.d(TAG, "Command execution cancelled.");
            notifyResult("failed");
        } else {
            // Failure
            Log.d(TAG, String.format(Locale.ENGLISH,"Command execution failed with returnCode=%d.", rc));
            notifyResult("failed");
        }

    }

    public int generateThumbKnownDuration(double duration, String inputVideoPath, String outputImagePath){
        int rc = 0;
        double middleTime = duration / 2.0;
        String middleTimeStr = String.format(Locale.ENGLISH,"%.2f", middleTime);

        //String hwMediaCodecAcc = " -hwaccel mediacodec ";
        String command = String.format(Locale.ENGLISH,
                "-i %s -ss %s -vf crop=min(iw\\,ih):min(iw\\,ih):(iw-min(iw\\,ih))/2:(ih-min(iw\\,ih))/2 -vframes 1 -preset ultrafast %s",
                inputVideoPath,
                middleTimeStr,
                outputImagePath
        );

        FFmpegSession session = FFmpegKit.execute(command);

        if (session.getReturnCode().isValueSuccess()) {
            rc =1;
            Log.d(TAG, "Thumbnail generated successfully");
        } else if (session.getReturnCode().isValueCancel()) {
            Log.d(TAG, "Thumbnail generation cancelled");
            rc = -1;
        } else {
            Log.d(TAG, "Thumbnail generation failed with return code " + session.getReturnCode());
            rc = -1;
        }

        return rc;
    }
    public int  generateThumb(String inputVideoPath, String outputImagePath) {
        int rc=0;
        try {
            rc = generateThumbKnownDuration(2, inputVideoPath, outputImagePath);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return rc;
    }

}
