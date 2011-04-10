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

import java.util.ArrayList;

import org.json.JSONException;

import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CampusTour extends MapActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "CampusTourActivity";
	private MapView map;
	private RestricterOverlay restricter = new RestricterOverlay(this, LEFT_LONG, TOP_LAT, RIGHT_LONG, BOTTOM_LAT, MIN_ZOOM, MAX_ZOOM);
    private MapTouchListenerOverlay mtlo;
	private GeoPoint mapCenter = new GeoPoint(CENTER_LAT, CENTER_LONG);
    private MyLocationWithCompassOverlay mlo;
	private MyItemizedOverlay mio;
	private DisplayMetrics dm;
	private boolean follow = false;
	private ArrayList<Building> buildingList;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mtlo = new MapTouchListenerOverlay(this);
        dm = this.getResources().getDisplayMetrics();
        setContentView(R.layout.map);
        
        try {
			buildingList = BuildingParser.parse(getResources().getString(R.string.buildings));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
		case R.id.followbutton:
			setFollow(true);
			break;
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
		map.getOverlays().add(mtlo);
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
		Drawable marker = this.getResources().getDrawable(android.R.drawable.radiobutton_off_background);
		bio = new BuildingItemizedOverlay(marker, this);
		OverlayItem center = new OverlayItem(mapCenter, "Testing", "Test test");
		for (Building b : buildingList) {
			bio.addBuilding(b);
		}
		map.getOverlays().add(bio);
	}
	
	public int scale(float dp) {
		return Math.round(dm.density * dp);
	}
	
	public boolean getFollow() { return follow; }
	
	public void setFollow(boolean bool) { follow = bool; }
}
