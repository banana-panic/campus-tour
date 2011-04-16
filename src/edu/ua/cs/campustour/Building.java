package edu.ua.cs.campustour;

import com.google.android.maps.GeoPoint;

public class Building {
	
	public static enum ButtonState {
		ENABLED, DISABLED, HIDDEN;
		
	}
	
	public final float lat;
	public final float lng;
	public final String name;
	public final String id;
	public final boolean showThumbnail;
	public final ButtonState textInfoState;
	public final ButtonState imagesState;
	public final ButtonState avState;
	public final GeoPoint geoPoint;
	
	Building(String name, String id, float lat, float lng,
			boolean showThumbnail, ButtonState textInfoState, ButtonState imagesState, ButtonState avState) {
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.id = id;
		this.showThumbnail = showThumbnail;
		this.textInfoState = textInfoState;
		this.imagesState = imagesState;
		this.avState = avState;
		this.geoPoint = new GeoPoint( Math.round(lat * 1000000), Math.round(lng * 1000000));
	}
	
}
