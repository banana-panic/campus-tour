package edu.ua.cs.campustour;

import com.google.android.maps.GeoPoint;

public interface MapConstants {
	public static final GeoPoint CENTER = new GeoPoint(33210553,-87541559);
	public static final int TOP_LAT = 33221283;
	public static final int LEFT_LONG = -87552756;
	public static final GeoPoint TOP_LEFT_BOUNDS = new GeoPoint(TOP_LAT, LEFT_LONG);
	public static final int BOTTOM_LAT = 33200852;
	public static final int RIGHT_LONG = -87525998;
	public static final GeoPoint BOTTOM_RIGHT_BOUNDS = new GeoPoint(BOTTOM_LAT, RIGHT_LONG);
	public static final int MIN_ZOOM_LEVEL = 16;
	public static final int START_ZOOM_LEVEL = 17;
}
