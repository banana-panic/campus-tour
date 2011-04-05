package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.CENTER;
import static edu.ua.cs.campustour.MapConstants.MIN_ZOOM_LEVEL;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class CampusTour extends MapActivity {
	@SuppressWarnings("unused")
	private static String TAG = "CampusTourActivity";
	private MapView map;
	private RestricterOverlay restricter = new RestricterOverlay();
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
		map.getOverlays().add(restricter);
		MapController mc = map.getController();
		mc.setCenter(CENTER);
		mc.setZoom(MIN_ZOOM_LEVEL);
	}
}