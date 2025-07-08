package tech.johnnn.ossave.db.videoDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import tech.johnnn.ossave.Log.Log;

public class VideoDBManager {
    String TAG = "VideoDBManager";

    private VideoDBHelper videoDBHelper;
    private Context context;
    private SQLiteDatabase database;

    public VideoDBManager(Context context) {
        this.context = context;
    }

    public VideoDBManager open() throws SQLException {
        videoDBHelper = new VideoDBHelper(context);
        database = videoDBHelper.getWritableDatabase();
        return this;
    }

    public void createTable(){
        videoDBHelper.createNewTable();
    }

    public void close() {
        videoDBHelper.close();
    }

    public void insert1Video(String videoId,String shortDesc, String selectTS, String processTS, String type,String size, String status, String tag_model) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(VideoDBHelper.VIDEO_ID,videoId);
        contentValue.put(VideoDBHelper.SHORT_DESC,shortDesc);
        contentValue.put(VideoDBHelper.SELECT_TIMESTAMP,selectTS);
        contentValue.put(VideoDBHelper.PROCESS_TIMESTAMP,processTS);
        contentValue.put(VideoDBHelper.SIZE,size);
        contentValue.put(VideoDBHelper.TYPE,type);
        contentValue.put(VideoDBHelper.STATUS,status);
        contentValue.put(VideoDBHelper.TAG_MODEL,tag_model);

        long rowChanged = database.insert(VideoDBHelper.TABLE_NAME, null, contentValue);

        Log.d(TAG,"data inserted! "+rowChanged);
    }

    public Cursor fetchAllVideosSortBySelectionTime() {
        String rawQuery = "SELECT * FROM "+VideoDBHelper.TABLE_NAME
                +" ORDER BY "+VideoDBHelper.SELECT_TIMESTAMP+" DESC ";
        String[] selArgs={};
        Cursor cursor = database.rawQuery(rawQuery,selArgs);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateTagModel(String vidID, String tagModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VideoDBHelper.TAG_MODEL, tagModel);
        String whereClause = VideoDBHelper.VIDEO_ID + " = ? ";
        String[] whereArgs = {vidID};
        int i = database.update(VideoDBHelper.TABLE_NAME, contentValues, whereClause, whereArgs);
        return i;
    }

    public String getTagModel(String videoID){
        String rawQuery = "SELECT "+VideoDBHelper.TAG_MODEL+" FROM "+VideoDBHelper.TABLE_NAME
                +" WHERE "+VideoDBHelper.VIDEO_ID+" = ? ";
        String[] args = {videoID};
        Cursor cursor = database.rawQuery(rawQuery,args);
        String tagModel = "";
        if (cursor != null && cursor.moveToFirst()) {
            tagModel = cursor.getString((int)cursor.getColumnIndex(VideoDBHelper.TAG_MODEL));

            cursor.close();
        }
        return tagModel;
    }

    public boolean deleteAVideo(String videoID) {
        String[] wArgs = {videoID};
        int nDeletedRow = database.delete(VideoDBHelper.TABLE_NAME, VideoDBHelper.VIDEO_ID + " = ? ", wArgs);
        return nDeletedRow > 0;
    }

}
