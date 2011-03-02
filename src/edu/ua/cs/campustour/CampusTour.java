package edu.ua.cs.campustour;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class CampusTour extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}