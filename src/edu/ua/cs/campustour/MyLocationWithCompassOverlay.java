package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.ARROW_SIZE;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyLocationWithCompassOverlay extends MyLocationOverlay {
	
	private static String TAG = "MyLocationOverlay";
	private CampusTour ctx;
	private MapView mv;
	private MapController mc;
	private Point currentLocationPoint;
	private Drawable arrow;
	private float lastBearing;

	public MyLocationWithCompassOverlay(CampusTour context, MapView aMapView) {
		this(context, aMapView, R.drawable.rotatable_my_location_arrow);
		lastBearing = 0;
	}
	
	public MyLocationWithCompassOverlay(CampusTour context, MapView aMapView, int arrowResId) {
		super(context, aMapView);
		ctx = context;
		mv = aMapView;
		mc = mv.getController();
		currentLocationPoint = new Point();
		int px = context.scale(ARROW_SIZE);

		arrow = ctx.getResources().getDrawable(arrowResId);
		arrow.setBounds(-px, -px, px, px);
	}
	
	@Override
	public void drawMyLocation(Canvas canvas, MapView mapView, Location lastFix,
			GeoPoint myLocation, long when) {
		if(ctx.getFollow()) mc.setCenter(myLocation);
		mapView.getProjection().toPixels(myLocation, currentLocationPoint);
		drawAt(canvas, arrow, currentLocationPoint.x, currentLocationPoint.y, false);
	}
	
	@Override
	public void drawCompass(Canvas canvas, float bearing) {
		arrow.setLevel(Math.round((bearing / 360) * 10000));
		if (Math.abs(bearing - lastBearing) > 2)
			mv.invalidate();
		lastBearing = bearing;
	}
	
}
