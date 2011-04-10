package edu.ua.cs.campustour;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BuildingOverlayItem extends OverlayItem {

	private Building building;
	
	public BuildingOverlayItem(Building building) {
		super(
				new GeoPoint( Math.round(building.lat * 1000000), Math.round(building.lng * 1000000)),
				building.name, String.valueOf(building.lat) + ", " +
				String.valueOf(building.lng));
		this.building = building;
	}
	
	public Building getBuilding() {
		return building;
	}

}
