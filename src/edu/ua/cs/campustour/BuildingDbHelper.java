package edu.ua.cs.campustour;

import java.io.IOException;

import android.database.sqlite.SQLiteDatabase;

public class BuildingDbHelper {
	private static final String PACKAGE = "edu.ua.cs.campustour";
	private static final String DB_NAME = "buildingdb.db";
	private static final String DB_PATH = "/data/data/" + PACKAGE + "/databases/" + DB_NAME;
	private static SQLiteDatabase db;
	
	BuildingDbHelper() throws IOException {
		db = getDb();
		
		if (db == null) {
			db = createDb();
		}
	}
	
	private void  getDb() {
		SQLiteDatabase.open(DB_PATH, null);
	}
}
