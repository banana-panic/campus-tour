package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.BOTTOM_LAT;
import static edu.ua.cs.campustour.MapConstants.CENTER_LAT;
import static edu.ua.cs.campustour.MapConstants.CENTER_LONG;
import static edu.ua.cs.campustour.MapConstants.LEFT_LONG;
import static edu.ua.cs.campustour.MapConstants.MAX_ZOOM;
import static edu.ua.cs.campustour.MapConstants.MIN_ZOOM;
import static edu.ua.cs.campustour.MapConstants.RIGHT_LONG;
import static edu.ua.cs.campustour.MapConstants.START_ZOOM;
import static edu.ua.cs.campustour.MapConstants.TOP_LAT;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class CampusTour extends MapActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "CampusTourActivity";
	private MapView map;
	private RestricterOverlay restricter = new RestricterOverlay(LEFT_LONG, TOP_LAT, RIGHT_LONG, BOTTOM_LAT, MIN_ZOOM, MAX_ZOOM);
    private GeoPoint mapCenter = new GeoPoint(CENTER_LAT, CENTER_LONG);
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        initMapView();
        initMyLocationOverlay();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void initMapView() {
		map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
		map.getOverlays().add(restricter);
		MapController mc = map.getController();
		mc.setCenter(mapCenter);
		mc.setZoom(START_ZOOM);
	}
	
	public void initMyLocationOverlay() {
		final MyLocationWithCompassOverlay overlay = new MyLocationWithCompassOverlay(this, map);
		map.getOverlays().add(overlay);
		overlay.enableMyLocation();
		overlay.enableCompass();
	}
}
