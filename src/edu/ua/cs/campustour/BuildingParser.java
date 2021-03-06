package edu.ua.cs.campustour;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.ua.cs.campustour.Building.ButtonState;

public class BuildingParser {
	
	public static HashMap<String, Building> parse(String s) throws JSONException {
		JSONObject json = new JSONObject(new JSONTokener(s));
		JSONArray arr = json.getJSONArray("buildings");
		HashMap<String, Building> map = new HashMap<String, Building>();
		for (int rem = arr.length() - 1; rem >= 0; rem--) {
			Building b = makeBuilding(arr.getJSONObject(rem));
			map.put(b.id, b);
		}
		return map;
	}
	
	private static Building makeBuilding(JSONObject json) throws JSONException {
		return new Building(json.getString("id"),
				json.optString("name", "Unnamed Building"),
				(float) json.getDouble("lat"),
				(float) json.getDouble("long"),
				json.getBoolean("thumbnail"),
				ButtonState.valueOf(json.optString("textInfo", "hidden").toUpperCase()),
				ButtonState.valueOf(json.optString("images", "hidden").toUpperCase()),
				ButtonState.valueOf(json.optString("av", "hidden").toUpperCase()));
	}
}
