package edu.ua.cs.campustour;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class BuildingItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
	private Context context;
	
	public BuildingItemizedOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}
	
	public BuildingItemizedOverlay(Drawable defaultMarker, Context ctx) {
		this(defaultMarker);
		context = ctx;
	}
	
	//called by populate();
	@Override
	protected OverlayItem createItem(int i) { return items.get(i); }
	
	@Override
	public int size() { return items.size(); }
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
	}
	
	//Dialog stuff from a tutorial, we'll add popup call here?
	@Override
	protected boolean onTap(int i) {
		OverlayItem item = items.get(i);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
	
	public void addBuilding(Building added) {
		items.add(new BuildingOverlayItem(added));
		populate();
	}
}