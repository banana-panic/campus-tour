package edu.ua.cs.campustour;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

public class Building implements Comparable<Building>, Serializable{
	
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
	
	Building(String id, String name, float lat, float lng,
			boolean showThumbnail, ButtonState textInfoState, ButtonState imagesState, ButtonState avState) {
		this.id = id;		
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.showThumbnail = showThumbnail;
		this.textInfoState = textInfoState;
		this.imagesState = imagesState;
		this.avState = avState;
	}

	public int compareTo(Building another) {
		return name.compareTo(another.name);
	}
	
	public GeoPoint getGeoPoint() {
		return new GeoPoint( Math.round(lat * 1000000), Math.round(lng * 1000000));
	}
}
