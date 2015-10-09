package com.nice.db.database;

import android.database.sqlite.SQLiteDatabase;

import com.nice.db.db.Table;

import java.util.ArrayList;
import java.util.List;


public class TableFactory implements ITableFactory {
    private List<Table> mListTables = new ArrayList<Table>();
    
    public TableFactory() {
        configTables();
    }
    
    /**  在这里添加要注册的表格     */
    private void configTables() {
//        mListTables.add(new UserTable());
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Table table:mListTables) {
            table.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(Table table:mListTables) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
