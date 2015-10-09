package com.nice.db.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class Table {
    
    public void addTempFields() {
        
    }
    
    public abstract void onCreate(SQLiteDatabase db);
    
    
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);   
    
}
