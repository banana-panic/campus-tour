package edu.ua.cs.campustour;

import static edu.ua.cs.campustour.MapConstants.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

public class MyLocationWithCompassOverlay extends MyLocationOverlay {
	
	private CampusTour ctx;
	private MapView mv;
	private MapController mc;
	private Point currentLocationPoint;
	private Drawable arrow;
	private float lastBearing;
	private float lastAccuracy;
	private Paint circlePaint = new Paint();
	private boolean accChanged = false;
	private int radius = 0;

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
		Projection proj = mapView.getProjection();
		proj.toPixels(myLocation, currentLocationPoint);
		lastAccuracy = lastFix.getAccuracy();
		drawAccuracy(canvas, currentLocationPoint, proj,lastFix.getLatitude());
		drawAt(canvas, arrow, currentLocationPoint.x, currentLocationPoint.y, false);
		if(ctx.getFollow()) mc.setCenter(myLocation);
	}
	
	@Override
	public void drawCompass(Canvas canvas, float bearing) {
		arrow.setLevel(Math.round((bearing / 360) * 10000));
		if (Math.abs(bearing - lastBearing) > 2)
			mv.invalidate();
		lastBearing = bearing;
	}
	
	public void drawAccuracy(Canvas canvas, Point p, Projection proj, double lat) {
		if(accChanged) {
			radius = (int) Math.round(proj.metersToEquatorPixels(lastAccuracy)/
										Math.cos(Math.toRadians(lat)));
			accChanged = false;
		}
		circlePaint.setAntiAlias(true);
		circlePaint.setColor(ACCURACY_FILL);
		circlePaint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(p.x, p.y, radius, circlePaint);
		circlePaint.setColor(ACCURACY_BORDER);
		circlePaint.setStyle(Paint.Style.STROKE);
		circlePaint.setStrokeWidth(2);
		canvas.drawCircle(p.x, p.y, radius, circlePaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me, MapView mv){
		super.onTouchEvent(me, mv);
		if (ctx.getFollow() && me.getAction() == MotionEvent.ACTION_MOVE)
			ctx.setFollow(false);
		return false;
	}
	
	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		super.onAccuracyChanged(sensor, accuracy);
		accChanged = true;
	}
	
}