package edu.ua.cs.campustour;
import android.provider.BaseColumns;


public interface DbConstants extends BaseColumns{
	public static final String DATABASE_NAME = "buildingdb";
	public static final int DATABASE_VERSION = 1;
	
	// Constants for building table
	public static final String BUILDING_TABLE = "building";
	public static final String BUILDING_NAME = "name";
	public static final String GPS_LAT = "lat";
	public static final String GPS_LONG = "long";
	public static final String GPS_RADIUS = "radius";
	public static final String TEXT_INFO = "text_info";
	public static final String TEXT_BG = "text_bg";
	public static final String MAP_SMALL_THUMBNAIL = "small_thumb";
	public static final String MAP_BIG_THUMBNAIL = "big_thumb";
	
	// Constants for image table
	public static final String IMAGE_TABLE = "image";
	public static final String IMAGE_NAME = "name";
	public static final String IMAGE_BUILDING_ID = "building_id";
	public static final String IMAGE_RESOURCE = "resource";
	
	// Constants for audio table
	public static final String AUDIO_TABLE = "audio";
	public static final String AUDIO_NAME = "name";
	public static final String AUDIO_BUILDING_ID = "building_id";
	public static final String AUDIO_URI = "uri";
	
	// Constants for video table
	public static final String VIDEO_TABLE = "video";
	public static final String VIDEO_NAME = "name";
	public static final String VIDEO_BUILDING_ID = "building_id";
	public static final String VIDEO_URI = "uri";
	
}
