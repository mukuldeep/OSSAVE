package tech.johnnn.ossave.db.videoDB;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import tech.johnnn.ossave.utils.TimeUtil;

public class VideoDBShim {
    private static String TAG = "videoDBShim";
    Context context;

    public VideoDBShim(Context context){
        this.context = context;
    }

    public void createTable(){
        VideoDBManager manager = new VideoDBManager(context);
        manager.open();
        manager.createTable();
        manager.close();
    }

    public ArrayList<String> getAllVideosSortBySelectionTime(){
        ArrayList<String> allVideos = new ArrayList<>();

        VideoDBManager videoDBManager = new VideoDBManager(context);
        videoDBManager.open();
        Cursor cursor = videoDBManager.fetchAllVideosSortBySelectionTime();//.fetchAllShortsSortByReceiveTime();
        if(cursor.moveToFirst()){
            do {
                String videoID = cursor.getString((int)cursor.getColumnIndex(VideoDBHelper.VIDEO_ID));
                allVideos.add(videoID);
            } while (cursor.moveToNext()); // Move to the next row, if available
        }
        cursor.close();
        videoDBManager.close();

        return allVideos;
    }

    public void addNewVideoToDB(String videoId,String shortDesc,String selectionTS, String processTS,String size, String tag_model,String type , String status){
        VideoDBManager videoDBManager = new VideoDBManager(context);
        videoDBManager.open();

        videoDBManager.insert1Video(videoId,shortDesc,selectionTS,processTS,type,size,status,tag_model);

        videoDBManager.close();
    }

    public int updateTagModel(String videoId,String tag_model){
        VideoDBManager videoDBManager = new VideoDBManager(context);
        videoDBManager.open();

        int res = videoDBManager.updateTagModel(videoId,tag_model);

        videoDBManager.close();

        return res;
    }

    public String getTagModel(String videoId){
        VideoDBManager videoDBManager = new VideoDBManager(context);
        videoDBManager.open();

        String tagModel = videoDBManager.getTagModel(videoId);

        videoDBManager.close();

        return tagModel;
    }
    public void addNewVideoOnSelect(String videoId, String shortDesc){
        String nowTS = TimeUtil.getTimeStampMillisString();
        addNewVideoToDB(videoId,shortDesc,nowTS,"0","0","{}","video","saved");
    }

    public boolean deleteAVideo(String videoID){
        VideoDBManager videoDBManager = new VideoDBManager(context);
        videoDBManager.open();
        boolean isDeleted = videoDBManager.deleteAVideo(videoID);
        videoDBManager.close();
        return isDeleted;
    }

}
