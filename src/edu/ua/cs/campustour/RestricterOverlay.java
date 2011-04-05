package edu.ua.cs.campustour;

import android.graphics.Canvas;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import static edu.ua.cs.campustour.MapConstants.*;

public class RestricterOverlay extends Overlay {
	public void draw(Canvas canvas, MapView map, boolean shadow) {
		MapController mc = map.getController();
		
		if (map.getZoomLevel() < MIN_ZOOM_LEVEL) {
			mc.setZoom(MIN_ZOOM_LEVEL);
		}
	}
}
