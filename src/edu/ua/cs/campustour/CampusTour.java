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

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CampusTour extends MapActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "CampusTourActivity";
	private MapView map;
	private RestricterOverlay restricter = new RestricterOverlay(LEFT_LONG, TOP_LAT, RIGHT_LONG, BOTTOM_LAT, MIN_ZOOM, MAX_ZOOM);
    private GeoPoint mapCenter = new GeoPoint(CENTER_LAT, CENTER_LONG);
	private MyLocationWithCompassOverlay mlo;
	private DisplayMetrics dm;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = this.getResources().getDisplayMetrics();
        setContentView(R.layout.map);
        initMapView();
        initMyLocationOverlay();
        initMyItemizedOverlay();
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mapmenu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exitbutton:
			mlo.disableMyLocation();
			mlo.disableCompass();
			finish();
			break;
		default:
			Log.d(TAG, "Unexpected MenuItem");
		}
			
		return true;
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
		mlo = new MyLocationWithCompassOverlay(this, map);
		map.getOverlays().add(mlo);
		mlo.enableMyLocation();
		mlo.enableCompass();
	}
	
	public void initMyItemizedOverlay() {
		List<Overlay> overlays = map.getOverlays();
		Drawable marker = this.getResources().getDrawable(R.drawable.marker);
		MyItemizedOverlay mio = new MyItemizedOverlay(marker, this);
		OverlayItem center = new OverlayItem(mapCenter, "Testing", "Test test");
		mio.addItem(center);
		overlays.add(mio);
	}
	
	public int scale(float dp) {
		return Math.round(dm.density * dp);
	}
		
}
