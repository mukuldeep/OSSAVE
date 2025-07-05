package tech.johnnn.ossave.db.kvDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KvDBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "OSSAVE.KV";
    static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "Kv_table";
    public static final String KEY = "key_str";
    public static final String VALUE = "value_str";
    public static final String EXTRA = "extra";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + KEY + " TEXT PRIMARY KEY, "
            + VALUE + " TEXT NOT NULL, "
            + EXTRA + " TEXT "
            +");";

    public KvDBHelper(Context context) {
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
            SQLiteDatabase db = getReadableDatabase();
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            //do nothing
        }
    }
}
