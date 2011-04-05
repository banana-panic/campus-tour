package edu.ua.cs.campustour;

import static android.provider.BaseColumns._ID;
import static edu.ua.cs.campustour.DbConstants.AUDIO_BUILDING_ID;
import static edu.ua.cs.campustour.DbConstants.AUDIO_NAME;
import static edu.ua.cs.campustour.DbConstants.AUDIO_TABLE;
import static edu.ua.cs.campustour.DbConstants.AUDIO_URI;
import static edu.ua.cs.campustour.DbConstants.BUILDING_NAME;
import static edu.ua.cs.campustour.DbConstants.BUILDING_TABLE;
import static edu.ua.cs.campustour.DbConstants.DATABASE_NAME;
import static edu.ua.cs.campustour.DbConstants.DATABASE_VERSION;
import static edu.ua.cs.campustour.DbConstants.GPS_LAT;
import static edu.ua.cs.campustour.DbConstants.GPS_LONG;
import static edu.ua.cs.campustour.DbConstants.GPS_RADIUS;
import static edu.ua.cs.campustour.DbConstants.IMAGE_BUILDING_ID;
import static edu.ua.cs.campustour.DbConstants.IMAGE_NAME;
import static edu.ua.cs.campustour.DbConstants.IMAGE_RESOURCE;
import static edu.ua.cs.campustour.DbConstants.IMAGE_TABLE;
import static edu.ua.cs.campustour.DbConstants.MAP_BIG_THUMBNAIL;
import static edu.ua.cs.campustour.DbConstants.MAP_SMALL_THUMBNAIL;
import static edu.ua.cs.campustour.DbConstants.TEXT_BG;
import static edu.ua.cs.campustour.DbConstants.TEXT_INFO;
import static edu.ua.cs.campustour.DbConstants.VIDEO_BUILDING_ID;
import static edu.ua.cs.campustour.DbConstants.VIDEO_NAME;
import static edu.ua.cs.campustour.DbConstants.VIDEO_TABLE;
import static edu.ua.cs.campustour.DbConstants.VIDEO_URI;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuildingDbHelper extends SQLiteOpenHelper {

	public BuildingDbHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + BUILDING_TABLE + "(" + _ID +
				" integer primary key autoincrement, " + BUILDING_NAME
				+ " text, " + GPS_LAT + " float, " + GPS_LONG + " float, "
				+ GPS_RADIUS + " float, " + TEXT_INFO + " text, "
				+ TEXT_BG + " integer, " + MAP_SMALL_THUMBNAIL
				+ " text, " + MAP_BIG_THUMBNAIL + " text)");
		db.execSQL("create table " + IMAGE_TABLE + "(" + _ID + 
				" integer primary key autoincrement, " + IMAGE_NAME
				+ " text, " + IMAGE_BUILDING_ID + " integer references"
				+ BUILDING_TABLE + "(" + _ID + "), " + IMAGE_RESOURCE
				+ " integer)");
		db.execSQL("create table " + VIDEO_TABLE + "(" + _ID + 
				" integer primary key autoincrement, " + VIDEO_NAME
				+ " text, " + VIDEO_BUILDING_ID + " integer references"
				+ BUILDING_TABLE + "(" + _ID + "), " + VIDEO_URI
				+ " text)");
		db.execSQL("create table " + AUDIO_TABLE + "(" + _ID + 
				" integer primary key autoincrement, " + AUDIO_NAME
				+ " text, " + AUDIO_BUILDING_ID + " integer references"
				+ BUILDING_TABLE + "(" + _ID + "), " + AUDIO_URI
				+ " text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + BUILDING_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + AUDIO_TABLE);
	}

}
