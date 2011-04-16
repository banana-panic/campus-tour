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

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import edu.ua.cs.campustour.Building.ButtonState;

public class CampusTour extends MapActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "CampusTourActivity";
	private MapView map;
	private RestricterOverlay restricter = new RestricterOverlay(this, LEFT_LONG, TOP_LAT, RIGHT_LONG, BOTTOM_LAT, MIN_ZOOM, MAX_ZOOM);
	private GeoPoint mapCenter = new GeoPoint(CENTER_LAT, CENTER_LONG);
    private MyLocationWithCompassOverlay mlo;
	private BuildingItemizedOverlay bio;
	private DisplayMetrics dm;
	private boolean follow = false;
	private Map<String, Building> buildingMap;
	private View popup;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = this.getResources().getDisplayMetrics();
        setContentView(R.layout.map);
        
        try {
			buildingMap = BuildingParser.parse(getResources().getString(R.string.buildings));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        initMapView();
        initMyLocationOverlay();
        initMyItemizedOverlay();
        initPopup();
    }
    
    public boolean showPopup(String id) {
    	Building building = buildingMap.get(id);
    	popup.setVisibility(View.GONE);
    	if (building == null) return false;
    	
    	TextView t = (TextView) popup.findViewById(R.id.popup_name);
    	t.setText(building.name);
    	
    	
    	int[] buttonIds = {R.id.popup_textinfo, R.id.popup_image_gallery, R.id.popup_av_gallery};
    	ButtonState[] states = {building.textInfoState, building.imagesState, building.avState};
    	
    	for (int i = 0; i < buttonIds.length; ++i) {
	    	View button = popup.findViewById(buttonIds[i]);
	    	switch (states[i]) {
		    	case HIDDEN:
		    		button.setVisibility(View.GONE);
		    		break;
		    	case ENABLED:
		    		button.setVisibility(View.VISIBLE);
			    	button.setEnabled(true);
			    	break;
		    	case DISABLED:
		    		button.setVisibility(View.VISIBLE);
			    	button.setEnabled(false);
			    	break;
	    	}
    	}
    	
    	
    	ImageView thumb = (ImageView) popup.findViewById(R.id.popup_thumb);
    	if (building.showThumbnail) {
	    	try {
				Bitmap bm = BitmapFactory.decodeStream(this.getAssets().open("landmarks/"
						+ building.id + "/thumbnail"));
				thumb.setImageBitmap(bm);
				thumb.setVisibility(View.VISIBLE);
			} catch (IOException e) {
				e.printStackTrace();
				thumb.setVisibility(View.GONE);
			}
    	}
    	else {
    		thumb.setVisibility(View.GONE);
    	}
    	
    	LayoutParams p = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
    			LayoutParams.WRAP_CONTENT,
    			building.geoPoint,
    			MapView.LayoutParams.BOTTOM | MapView.LayoutParams.CENTER_HORIZONTAL);
    	
    	popup.setLayoutParams(p);
    	popup.setVisibility(View.VISIBLE);
    	
    	return true;
    }
    
    public void hidePopup() {
    	popup.setVisibility(View.GONE);
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
		for (Building b : buildingMap.values()) {
			bio.addBuilding(b);
		}
		map.getOverlays().add(bio);
	}
	
	public void initPopup() {
		this.popup = View.inflate(this, R.layout.map_popup, null);
		this.popup.setVisibility(View.GONE);
		map.addView(popup);
	}
	
	public int scale(float dp) {
		return Math.round(dm.density * dp);
	}
	
	public boolean getFollow() { return follow; }
	
	public void setFollow(boolean bool) { follow = bool; }
}
