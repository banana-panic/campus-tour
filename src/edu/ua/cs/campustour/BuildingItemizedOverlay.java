package edu.ua.cs.campustour;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class BuildingItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<BuildingOverlayItem> items = new ArrayList<BuildingOverlayItem>();
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
	protected BuildingOverlayItem createItem(int i) { return items.get(i); }
	
	@Override
	public int size() { return items.size(); }
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
	}
	
	//Dialog stuff from a tutorial, we'll add popup call here?
	@Override
	protected boolean onTap(int i) {
		BuildingOverlayItem item = items.get(i);
		/*
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		*/
		Intent intent = new Intent(context, TextInfo.class); 
		intent.putExtra("id", item.getBuilding().id);
		intent.putExtra("name", item.getTitle());
		context.startActivity(intent);
		return true;
	}
	
	public void addBuilding(Building added) {
		items.add(new BuildingOverlayItem(added));
		populate();
	}
}
