package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.ARROW_SIZE;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyLocationWithCompassOverlay extends MyLocationOverlay {
	
	private static String TAG = "MyLocationOverlay";
	private Context ctx;
	private MapView mv;
	private Point currentLocationPoint;
	private Drawable arrow;
	private float lastBearing;
	private final float scale;

	public MyLocationWithCompassOverlay(Context context, MapView aMapView) {
		this(context, aMapView, R.drawable.rotatable_my_location_arrow);
		lastBearing = 0;
	}
	
	public MyLocationWithCompassOverlay(Context context, MapView aMapView, int arrowResId) {
		super(context, aMapView);
		scale = context.getResources().getDisplayMetrics().density;
		ctx = context;
		mv = aMapView;
		currentLocationPoint = new Point();
		int dp = Math.round(ARROW_SIZE * scale);

		arrow = ctx.getResources().getDrawable(arrowResId);
		arrow.setBounds(-dp, -dp, dp, dp);
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
		if (Math.abs(bearing - lastBearing) > 2)
			mv.invalidate();
		lastBearing = bearing;
	}
	
}
