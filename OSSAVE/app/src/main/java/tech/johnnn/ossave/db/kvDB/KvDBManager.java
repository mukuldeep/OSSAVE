package tech.johnnn.ossave.db.kvDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import tech.johnnn.ossave.Log.Log;

public class KvDBManager {
    String TAG = "KvDBManager";

    private KvDBHelper kvDBHelper;
    private Context context;
    private SQLiteDatabase database;

    public KvDBManager(Context context) {
        this.context = context;
    }

    public KvDBManager open() throws SQLException {
        kvDBHelper = new KvDBHelper(context);
        database = kvDBHelper.getWritableDatabase();
        //settingDBHelper.createNewTable(database);
        return this;
    }

    public void createTable(){
        kvDBHelper.createNewTable();
    }

    public void close() {
        kvDBHelper.close();
    }


    public void insert(String key, String val) {

            ContentValues contentValue = new ContentValues();
            contentValue.put(KvDBHelper.KEY, key);
            contentValue.put(KvDBHelper.VALUE, val);

            database.insert(KvDBHelper.TABLE_NAME, null, contentValue);

            Log.d(TAG, "data inserted!");

    }

    public Cursor fetchVal(String key) {
        String rawQuery = "SELECT * FROM "+ KvDBHelper.TABLE_NAME
                +" WHERE ("+ KvDBHelper.KEY+"= ? )";
        String[] rawQueryArgs={key};
        Cursor cursor=null;
        try {
            cursor = database.rawQuery(rawQuery, rawQueryArgs);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateVal(String key, String val){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KvDBHelper.KEY, key);
        contentValues.put(KvDBHelper.VALUE, val);

        String[] whereArgs = new String[] { key  };

        int i = database.update(KvDBHelper.TABLE_NAME, contentValues, KvDBHelper.KEY + " = ? ", whereArgs);
        return i;
    }


    public boolean deleteKey(String key) {
        String[] wArgs = {key};
        int nDeletedRow = database.delete(KvDBHelper.TABLE_NAME, KvDBHelper.KEY + " = ? ", wArgs);
        return nDeletedRow > 0;
    }

    public boolean deleteKeysStartingWith(String prefix) {
        String whereClause = KvDBHelper.KEY + " LIKE ?";
        String[] whereArgs = { prefix+"%" };
        int nDeletedRows = database.delete(KvDBHelper.TABLE_NAME, whereClause, whereArgs);
        return nDeletedRows > 0;
    }

}
