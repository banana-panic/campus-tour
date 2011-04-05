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

	public MyLocationWithCompassOverlay(Context context, MapView aMapView) {
		super(context, aMapView);
		ctx = context;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
		Log.d(TAG, "isMyLocationEnabled=" + isMyLocationEnabled());
		return super.draw(canvas, mv, shadow, when);
	}
	
	@Override
	public void drawMyLocation(Canvas canvas, MapView mapView, Location lastFix,
			GeoPoint myLocation, long when) {
		Point point = new Point();
		Drawable arrow = ctx.getResources().getDrawable(R.drawable.my_location_arrow);
		super.drawMyLocation(canvas, mapView, lastFix, myLocation, when);
		mapView.getProjection().toPixels(myLocation, point);
		drawAt(canvas, arrow, point.x, point.y, false);
	}

}
