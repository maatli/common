package com.nice.db.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	private ITableFactory mTableFactory = null;
	
	public DbOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    if (mTableFactory != null) {
            mTableFactory.onCreate(db);
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (mTableFactory != null) {
		    mTableFactory.onUpgrade(db, oldVersion, newVersion);
		}
	}
	
	/**
	 * 删除所有数据表
	 */
	public void dropDb(SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				db.execSQL("DROP TABLE " + cursor.getString(0));
			}
		}
		
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}
	
	
}

