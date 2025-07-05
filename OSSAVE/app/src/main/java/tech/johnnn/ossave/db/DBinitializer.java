package tech.johnnn.ossave.db;

import android.content.Context;

import tech.johnnn.ossave.db.kvDB.KvDBShim;

public class DBinitializer {
    public DBinitializer(Context context){

        KvDBShim kvDBShim = new KvDBShim(context);
        kvDBShim.createTable();

    }
}
