package tech.johnnn.ossave.utils;

import android.net.Uri;
import android.widget.ImageView;

import tech.johnnn.ossave.Log.Log;
import tech.johnnn.ossave.file.InternalFileHandler;
import tech.johnnn.ossave.editor.OnVideoThumbGenerate;
import tech.johnnn.ossave.editor.VideoThumbnailGeneratorTask;

public class ThumbnailFFmpeg implements OnVideoThumbGenerate {
    private static String TAG = "ThumbnailFFmpeg";
   InternalFileHandler ifh;
   String fileName;
   ImageView imageView;
    public void setThumbnail(InternalFileHandler ifh, ImageView imageView, String subDir, String fileName){
        this.ifh = ifh;
        this.fileName = fileName;
        this.imageView = imageView;

        if(!ifh.isExists(ifh.DIR_THUMBS,fileName+"_thumb.jpg")){
            Log.e(TAG,"set thumbnail executing not exist");
            String inputVideoPath = ifh.getFullPath(subDir,fileName);
            String outtputImagePath = ifh.internalStorageDir.getAbsolutePath()+"/"+ifh.DIR_THUMBS+"/"+fileName+"_thumb.jpg";
            VideoThumbnailGeneratorTask videoThumbnailGeneratorTask = new VideoThumbnailGeneratorTask(imageView,this);
            videoThumbnailGeneratorTask.execute(inputVideoPath,outtputImagePath);
            Log.e(TAG,"set thumbnail executed not exist");
        }else{
            String imagePath = ifh.getFullPath(ifh.DIR_THUMBS,fileName+"_thumb.jpg");
            imageView.setImageURI(Uri.parse(imagePath));
        }

    }

    @Override
    public void onVideoThumbGenerate(String res) {
        if(res.equals("success")){
            String imagePath = ifh.getFullPath(ifh.DIR_THUMBS,fileName+"_thumb.jpg");
            if(ifh.isExists(ifh.DIR_THUMBS,fileName+"_thumb.jpg")){
                try {
                    imageView.setImageURI(Uri.parse(imagePath));
                }catch (Exception e){
                    Log.e(TAG,"error while setting generated thumbnail");
                    e.printStackTrace();
                }
            }
        }
    }
}
