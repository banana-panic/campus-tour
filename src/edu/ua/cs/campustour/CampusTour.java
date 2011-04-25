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
import java.util.HashMap;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;

import edu.ua.cs.campustour.Building.ButtonState;

public class CampusTour extends MapActivity {
	public static final int SearchRequestCode = 1;
	private static final String TAG = "CampusTourActivity";
	private MapView map;
	private MapController mc;
	private RestricterOverlay restricter = new RestricterOverlay(this, LEFT_LONG, TOP_LAT, RIGHT_LONG, BOTTOM_LAT, MIN_ZOOM, MAX_ZOOM);
	private GeoPoint mapCenter = new GeoPoint(CENTER_LAT, CENTER_LONG);
    private MyLocationWithCompassOverlay mlo;
	private BuildingItemizedOverlay bio;
	private DisplayMetrics dm;
	private boolean follow = false;
	private HashMap<String, Building> buildingMap;
	private View popup;
	private Building popupBuilding;
	
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
    	this.popupBuilding = building;
    	popup.setVisibility(View.GONE);
    	if (building == null) return false;
    	
    	TextView t = (TextView) popup.findViewById(R.id.popup_name);
    	t.setText(building.name);
    	
    	int[] buttonIds = {R.id.popup_textinfo, R.id.popup_image_gallery, R.id.popup_av_gallery};
    	ButtonState[] states = {building.textInfoState, building.imagesState, building.avState};
    	
    	for (int i = 0; i < buttonIds.length; ++i) {
	    	ImageButton button = (ImageButton) popup.findViewById(buttonIds[i]);
	    	ViewGroup.LayoutParams lp = button.getLayoutParams();
	    	switch (states[i]) {
		    	case HIDDEN:
		    		lp.width = 0;
		    		button.setVisibility(View.INVISIBLE);
		    		break;
		    	case ENABLED:
			    	lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		    		button.setVisibility(View.VISIBLE);
			    	button.setEnabled(true);
			    	break;
		    	case DISABLED:
			    	lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
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
				thumb.getLayoutParams().width = scale(80);
				thumb.setImageBitmap(bm);
				thumb.setVisibility(View.VISIBLE);
			} catch (IOException e) {
				e.printStackTrace();
				thumb.getLayoutParams().width = 0;
				thumb.setVisibility(View.INVISIBLE);
			}
    	}
    	else {
			thumb.getLayoutParams().width = 0;
    		thumb.setVisibility(View.INVISIBLE);
    	}
    	
    	LayoutParams p = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
    			LayoutParams.WRAP_CONTENT,
    			building.getGeoPoint(),
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
		Log.d(TAG, "In onMenuItemSelected()");
		switch (item.getItemId()) {
		case R.id.followbutton:
			setFollow(true);
			break;
		case R.id.exitbutton:
			mlo.disableMyLocation();
			mlo.disableCompass();
			finish();
			break;
		case R.id.searchbutton:
			Log.d(TAG, "In case searchbutton");
			launchSearchableActivity(false);
			break;
		default:
			Log.d(TAG, "Unexpected MenuItem");
		}
			
		return true;
	}
	
	@Override
	public boolean onSearchRequested() {
		Log.d(TAG, "In onSearchRequested()");
		launchSearchableActivity(true);
		return true;
	}
	
	public void launchSearchableActivity(boolean searchBox) {
		Intent intent = new Intent(this, SearchableActivity.class);
		intent.putExtra(SearchableActivity.ShowSearchBox, searchBox);
		intent.putExtra(SearchableActivity.Map, buildingMap);
		startActivityForResult(intent, CampusTour.SearchRequestCode);
	}
	
	public void onActivityResult(int request, int result, Intent intent) {
		if (result != Activity.RESULT_CANCELED) {
			String id = intent.getStringExtra("id");
			centerOn(id);
			showPopup(id);
		}
	}
	
	public void centerOn(String id) {
		setFollow(false);
		mc.setCenter(buildingMap.get(id).getGeoPoint());
	}
	
	public void initMapView() {
		map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
		map.getOverlays().add(restricter);
		mc = map.getController();
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
		ImageButton b = (ImageButton) popup.findViewById(R.id.popup_textinfo);
		b.setOnClickListener(new TextInfoButtonListener());
		b = (ImageButton) popup.findViewById(R.id.popup_image_gallery);
		b.setOnClickListener(new ImagesButtonListener());
		b = (ImageButton) popup.findViewById(R.id.popup_av_gallery);
		b.setOnClickListener(new AvButtonListener());
		map.addView(popup);
	}
	
	public int scale(float dp) {
		return Math.round(dm.density * dp);
	}
	
	public boolean getFollow() { return follow; }
	
	public void setFollow(boolean bool) { follow = bool; }
	
	private class TextInfoButtonListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(CampusTour.this, TextInfo.class);
			intent.putExtra("id", popupBuilding.id);
			startActivity(intent);
		}
		
	}
	
	private class ImagesButtonListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(CampusTour.this, ImagesActivity.class);
			intent.putExtra("id", popupBuilding.id);
			startActivity(intent);
		}
		
	}
	
	private class AvButtonListener implements OnClickListener {

		public void onClick(View v) {
			Intent intent = new Intent(CampusTour.this, AvList.class);
			intent.putExtra("id", popupBuilding.id);
			startActivity(intent);
		}
		
	}
}
