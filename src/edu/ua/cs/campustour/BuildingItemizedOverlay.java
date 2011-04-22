package edu.ua.cs.campustour;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class BuildingItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<BuildingOverlayItem> items = new ArrayList<BuildingOverlayItem>();
	private CampusTour tour;
	private float originX;
	private float originY;
	private float maxDist = 0;
	private final String TAG = "BuildingOverlay";
	
	public BuildingItemizedOverlay(Drawable defaultMarker, CampusTour tour) {
		super(boundCenter(defaultMarker));
		this.tour = tour;
	}
	
	//called by populate();
	@Override
	protected BuildingOverlayItem createItem(int i) { return items.get(i); }
	
	@Override
	public int size() { return items.size(); }
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
	}
	
	@Override
	protected boolean onTap(int i) {
		BuildingOverlayItem item = items.get(i);
		tour.showPopup(item.getBuilding().id);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me, MapView mv) {
		Log.d(TAG, String.valueOf(me.getEventTime()));
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN:
			originX = me.getX();
			originY = me.getY();
			maxDist = 0;
			Log.d(TAG, "Down");
			break;
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "Dist: " + maxDist);
			if (maxDist < 25) {
				tour.hidePopup();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float xDisp = me.getX() - originX;
			float yDisp = me.getY() - originY;
			float dist = (float) Math.sqrt((xDisp * xDisp) + (yDisp * yDisp));
			maxDist = Math.max(dist, maxDist);
			break;
		}
		return false;
	}
	
	public void addBuilding(Building added) {
		items.add(new BuildingOverlayItem(added));
		populate();
	}
	
}
