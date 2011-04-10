package edu.ua.cs.campustour;

import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapTouchListenerOverlay extends Overlay {
	private CampusTour context;
	private String TAG = "MTLO";
	
	public MapTouchListenerOverlay(CampusTour ctx) {
		context = ctx;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me, MapView mv){
		if (context.getFollow()){
			if(me.getAction() == MotionEvent.ACTION_MOVE){
				context.setFollow(false);
    			Log.d(TAG, "unfollowing");
			} else Log.d(TAG, "following");
		} else {
			Log.d(TAG, "not following");
			return false;
		}
		return true;
	}

}
