package edu.ua.cs.campustour;

import android.graphics.Canvas;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RestricterOverlay extends Overlay {
	private MapView map;
	private MapController mc;
	private int left;
	private int top;
	private int right;
	private int bottom;
	private int min;
	private int max;
	
	public RestricterOverlay(int leftlong, int toplat, int rightlong, int botlat, int minzoom, int maxzoom) {
		left = leftlong;
		top = toplat;
		right = rightlong;
		bottom = botlat;
		min = minzoom;
		max = maxzoom;
	}
	
	public void draw(Canvas canvas, MapView aMap, boolean shadow) {
		map = aMap;
		mc = map.getController();
		restrictZoom();
		restrictPan();
	}
	
	private void restrictZoom() {
		int zoom = map.getZoomLevel();
		if (zoom < min)
			mc.zoomIn();
		else if (zoom > max)
			mc.zoomOut();
	}
	
	private void restrictPan() {
		GeoPoint currentCenter = map.getMapCenter();
		int currentLat  = currentCenter.getLatitudeE6();
		int currentLong = currentCenter.getLongitudeE6();
		int validLat  = currentLat;
		int validLong = currentLong;
		if (currentLong < left)  validLong = left;
		if (currentLat  > top) 	  validLat = top;
		if (currentLong > right) validLong = right;
		if (currentLat  < bottom) validLat = bottom;
		if (currentLat != validLat || currentLong != validLong)
			mc.setCenter(new GeoPoint(validLat, validLong));
	}
}
