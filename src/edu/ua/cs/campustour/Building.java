package edu.ua.cs.campustour;

public class Building {
	public final float lat;
	public final float lng;
	public final String name;
	
	Building(String name, float lat, float lng) {
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
}
