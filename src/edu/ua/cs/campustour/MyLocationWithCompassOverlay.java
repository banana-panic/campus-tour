package edu.ua.cs.campustour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyLocationWithCompassOverlay extends MyLocationOverlay {
	
	private static String TAG = "MyLocationOverlay";
	private Context ctx;
	private Point currentLocationPoint;
	private Drawable arrow;
	private float bearing;

	public MyLocationWithCompassOverlay(Context context, MapView aMapView) {
		super(context, aMapView);
		ctx = context;
		currentLocationPoint = new Point();
		arrow = ctx.getResources().getDrawable(R.drawable.rotatable_my_location_arrow);
		arrow.setBounds(-25, -25, 25, 25);
	}
	
	@Override
	public void drawMyLocation(Canvas canvas, MapView mapView, Location lastFix,
			GeoPoint myLocation, long when) {
		mapView.getProjection().toPixels(myLocation, currentLocationPoint);
		drawAt(canvas, arrow, currentLocationPoint.x, currentLocationPoint.y, false);
	}
	
	@Override
	public void drawCompass(Canvas canvas, float bearing) {
		arrow.setLevel(Math.round((bearing / 360) * 10000));
	}
	
}
