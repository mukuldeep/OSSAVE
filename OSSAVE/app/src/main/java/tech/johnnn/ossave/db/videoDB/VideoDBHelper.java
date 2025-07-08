package tech.johnnn.ossave.db.videoDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoDBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "OSSAVE.DB.VIDEO";
    static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "video";

    public static final String VIDEO_ID = "video_id";
    public static final String SHORT_DESC = "short_desc";
    public static final String SELECT_TIMESTAMP = "select_ts";
    public static final String PROCESS_TIMESTAMP = "process_ts";
    public static final String STATUS = "status";
    public static final String SIZE = "size";
    public static final String TYPE = "type";
    public static final String TAG_MODEL = "tag_model";


    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + VIDEO_ID + " TEXT PRIMARY KEY, "
            + SHORT_DESC + " TEXT , "
            + SELECT_TIMESTAMP + " TEXT , "
            + PROCESS_TIMESTAMP + " TEXT , "
            + TAG_MODEL +" TEXT , "
            + SIZE +" TEXT , "
            + TYPE +" TEXT , "
            + STATUS + " TEXT NOT NULL "
            +");";

    public VideoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void createNewTable(){
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            //do nothing
        }
    }
}
