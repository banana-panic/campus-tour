package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.BOTTOM_LAT;
import static edu.ua.cs.campustour.MapConstants.LEFT_LONG;
import static edu.ua.cs.campustour.MapConstants.MIN_ZOOM_LEVEL;
import static edu.ua.cs.campustour.MapConstants.RIGHT_LONG;
import static edu.ua.cs.campustour.MapConstants.TOP_LAT;
import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RestricterOverlay extends Overlay {
	MapView map;
	MapController mc;
	
	public void draw(Canvas canvas, MapView aMap, boolean shadow) {
		map = aMap;
		mc = map.getController();
		restrictZoom();
		restrictPan();
	}
	
	private void restrictZoom() {
		if (map.getZoomLevel() < MIN_ZOOM_LEVEL)
			mc.zoomIn();
	}
	
	private void restrictPan() {
		GeoPoint currentCenter = map.getMapCenter();
		int currentLat  = currentCenter.getLatitudeE6();
		int currentLong = currentCenter.getLongitudeE6();
		int validLat  = currentLat;
		int validLong = currentLong;
		if (currentLat  > TOP_LAT) 	  validLat = TOP_LAT;
		if (currentLat  < BOTTOM_LAT) validLat = BOTTOM_LAT;
		if (currentLong < LEFT_LONG)  validLong = LEFT_LONG;
		if (currentLong > RIGHT_LONG) validLong = RIGHT_LONG;
		if (currentLat != validLat || currentLong != validLong) 
			mc.setCenter(new GeoPoint(validLat, validLong));
	}
}
