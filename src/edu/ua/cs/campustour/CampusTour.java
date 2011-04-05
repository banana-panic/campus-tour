package edu.ua.cs.campustour;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class CampusTour extends MapActivity {
	private final static GeoPoint CENTER = new GeoPoint(33210553,-87541559);
	private final static int MIN_ZOOM_LEVEL = 16;
	private MapView map;
	private MasterOverlay zoomRestricter = new MasterOverlay();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        initMapView();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void initMapView() {
		map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
		map.getOverlays().add(zoomRestricter);
		MapController mc = map.getController();
		mc.setCenter(CENTER);
		mc.setZoom(MIN_ZOOM_LEVEL);
	}
}