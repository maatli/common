package com.nice.db.database;

import android.database.sqlite.SQLiteDatabase;

public interface ITableFactory {
    
    
    public void onCreate(SQLiteDatabase db);
    
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
