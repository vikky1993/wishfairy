package com.example.wishfairy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class creates the relation with the SQLite Database Helper
 * through which queries can be SQL called. 		
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	// The database name and version
	private static final String DB_NAME = "wishfairyDB";
	private static final int DB_VERSION = 1;

	/**
	 * Database Helper constructor. 
	 * @param context
	 */
	public DatabaseHelper(Context c) {
		super(c, DB_NAME, null, DB_VERSION);
	}
	
	/**
	 * Handles the table version and the drop of a table.   
	 */			
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading databse from version" + oldVersion + "to " 
				+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS user");
		onCreate(database);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
	}
}

