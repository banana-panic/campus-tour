package edu.ua.cs.campustour;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class CampusTour extends MapActivity {
	private MapView map;
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
	}
}