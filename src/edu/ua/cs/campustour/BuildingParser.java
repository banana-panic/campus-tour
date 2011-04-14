package edu.ua.cs.campustour;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BuildingParser {
	
	public static ArrayList<Building> parse(String s) throws JSONException {
		JSONObject json = new JSONObject(new JSONTokener(s));
		JSONArray arr = json.getJSONArray("buildings");
		ArrayList<Building> arrayList = new ArrayList<Building>();
		for (int rem = arr.length() - 1; rem >= 0; rem--) {
			arrayList.add(makeBuilding(arr.getJSONObject(rem)));
		}
		return arrayList;
	}
	
	private static Building makeBuilding(JSONObject json) throws JSONException {
		return new Building(json.getString("id"),
				json.getString("name"),
				(float) json.getDouble("lat"),
				(float) json.getDouble("long"));
	}
}
