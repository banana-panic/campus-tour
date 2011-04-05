package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.*;
import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RestricterOverlay extends Overlay {
	MapView map;
	GeoPoint currentCenter;
	int currentLat;
	int currentLong;
	
	public void draw(Canvas canvas, MapView aMap, boolean shadow) {
		map = aMap;
		restrictZoom();
		restrictPan();
	}
	
	private void restrictZoom() {
		MapController mc = map.getController();
		if (map.getZoomLevel() < MIN_ZOOM_LEVEL) {
			mc.setZoom(MIN_ZOOM_LEVEL);
		}
	}
	
	private void restrictPan() {
		MapController mc = map.getController();
		getCenter();
		if (currentLat > TOP_LAT) {
			mc.setCenter(new GeoPoint(TOP_LAT,currentLong));
			getCenter();
		}
		if (currentLong < LEFT_LONG){
			mc.setCenter(new GeoPoint(currentLat, LEFT_LONG));
			getCenter();
		}
		if (currentLat < BOTTOM_LAT) {
			mc.setCenter(new GeoPoint(BOTTOM_LAT,currentLong));
			getCenter();
		}
		if (currentLong > RIGHT_LONG){
			mc.setCenter(new GeoPoint(currentLat, RIGHT_LONG));
			getCenter();
		}
	}
		
	private void getCenter() {
		currentCenter = map.getMapCenter();
		currentLat = currentCenter.getLatitudeE6();
		currentLong = currentCenter.getLongitudeE6();
	}
}
