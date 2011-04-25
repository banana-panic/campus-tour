package edu.ua.cs.campustour;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BuildingOverlayItem extends OverlayItem {

	private Building building;
	
	public BuildingOverlayItem(Building building) {
		super(
				building.getGeoPoint(),
				building.name, String.valueOf(building.lat) + ", " +
				String.valueOf(building.lng));
		this.building = building;
	}
	
	public Building getBuilding() {
		return building;
	}

}
