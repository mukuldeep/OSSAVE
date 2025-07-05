package tech.johnnn.ossave.db.kvDB;

import android.content.Context;
import android.database.Cursor;

import tech.johnnn.ossave.Log.Log;

public class KvDBShim {
    static String TAG="kvDBShim";
    Context context;
    public KvDBShim(Context context){
        this.context = context;
    }

    public void createTable(){
        KvDBManager manager = new KvDBManager(context);
        manager.open();
        manager.createTable();
        manager.close();
    }

    public void setKeyVal(String key,String val){
        if (isKeyExist(key)) {
            updateVal(key,val);
        } else {
            KvDBManager kvDBManager = new KvDBManager(context);
            kvDBManager.open();
            kvDBManager.insert(key,val);
            kvDBManager.close();
        }
    }

    public String getVal(String key){
        String res = "";
        KvDBManager kvDBManager = new KvDBManager(context);
        kvDBManager.open();

        Cursor cursor = kvDBManager.fetchVal(key);
        if(cursor.moveToFirst()){
            do {
                String val = cursor.getString((int)cursor.getColumnIndex(KvDBHelper.VALUE));
                if(val!=null) {
                    res = val;
                }
                Log.d(TAG," "+key+":"+res);
            } while (cursor.moveToNext());
        }

        cursor.close();
        kvDBManager.close();
        return res;
    }

    public boolean isKeyExist(String key){
        boolean res = false;
        KvDBManager kvDBManager = new KvDBManager(context);
        kvDBManager.open();

        Cursor cursor = kvDBManager.fetchVal(key);
        if(cursor.moveToFirst()){
            res = true;
        }
        cursor.close();
        kvDBManager.close();
        return res;
    }

    public boolean deleteKey(String key){
        KvDBManager kvDBManager = new KvDBManager(context);
        kvDBManager.open();
        boolean isDeleted = kvDBManager.deleteKey(key);
        kvDBManager.close();
        return isDeleted;
    }

    public boolean deleteKeysStartingWith(String prefix) {
        KvDBManager kvDBManager = new KvDBManager(context);
        kvDBManager.open();
        boolean isDeleted = kvDBManager.deleteKeysStartingWith(prefix);
        kvDBManager.close();
        return isDeleted;
    }

    private void updateVal(String key, String val){
        KvDBManager kvDBManager = new KvDBManager(context);
        kvDBManager.open();
        int rowChanged = kvDBManager.updateVal(key,val);
        Log.d(TAG,"Update rowChanged:"+rowChanged);
        kvDBManager.close();
    }

    private void setDefaultVal(String key, String val){
        String existingVal = getVal(key);
        if(existingVal.equals("")){
            setKeyVal(key,val);
        }
    }


}
