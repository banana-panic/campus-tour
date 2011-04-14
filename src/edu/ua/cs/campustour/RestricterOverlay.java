package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.BOTTOM_LAT;
import static edu.ua.cs.campustour.MapConstants.BOUNDARY_FILL;
import static edu.ua.cs.campustour.MapConstants.BOUNDARY_WIDTH_PERCENT;
import static edu.ua.cs.campustour.MapConstants.LEFT_LONG;
import static edu.ua.cs.campustour.MapConstants.RIGHT_LONG;
import static edu.ua.cs.campustour.MapConstants.TOP_LAT;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Region;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RestricterOverlay extends Overlay {
	private CampusTour ctx;
	private MapView map;
	private MapController mc;
	private int left;
	private int top;
	private int right;
	private int bottom;
	private int min;
	private int max;
	private Point a;
	private Point b;
	
	public RestricterOverlay(CampusTour context, int leftlong, int toplat, int rightlong, int botlat, int minzoom, int maxzoom) {
		ctx = context;
		left = leftlong;
		top = toplat;
		right = rightlong;
		bottom = botlat;
		min = minzoom;
		max = maxzoom;
		a = new Point();
		b = new Point();
	}
	
	public void draw(Canvas canvas, MapView aMap, boolean shadow) {
		if(!shadow){
			map = aMap;
			mc = map.getController();
			
			canvas.save();
			Projection proj = aMap.getProjection();
			int h = aMap.getHeight();
			int w = aMap.getWidth();
			float width_off = w*(0.5f-BOUNDARY_WIDTH_PERCENT);
			float height_off = (h*0.5f-w*BOUNDARY_WIDTH_PERCENT);
			proj.toPixels(new GeoPoint(TOP_LAT, LEFT_LONG), a);
			proj.toPixels(new GeoPoint(BOTTOM_LAT, RIGHT_LONG), b);
			canvas.clipRect(a.x-width_off, a.y-height_off,
							b.x+width_off, b.y+height_off, Region.Op.DIFFERENCE);
			canvas.drawColor(BOUNDARY_FILL);
			canvas.restore();
			
			restrictZoom();
			restrictPan();
		}
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
			if (ctx.getFollow()) ctx.setFollow(false);
			mc.setCenter(new GeoPoint(validLat, validLong));
	}
}
