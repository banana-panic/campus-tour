package edu.ua.cs.campustour;

public class Building {
	public final float lat;
	public final float lng;
	public final String name;
	public final String id;
	
	Building(String id, String name, float lat, float lng) {
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
}
