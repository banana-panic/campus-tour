package edu.ua.cs.campustour;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.ua.cs.campustour.AvList.VideoInfo;
import edu.ua.cs.campustour.AvList.VideoType;

public class VideoUrlParser {
	private VideoUrlParser() {
	}
	
	public static LinkedList<VideoInfo> parse(String videoJson) throws JSONException {
		LinkedList<VideoInfo> r = new LinkedList<VideoInfo>();
		JSONArray arr = (new JSONObject(new JSONTokener(videoJson))).getJSONArray("urls");
		for (int rem = arr.length() - 1; rem >= 0; rem-- ) {
			VideoInfo vi = makeVideoInfo(arr.getJSONObject(rem));
			r.add(vi);
		}
		return r;
	}
	
	private static VideoInfo makeVideoInfo(JSONObject jo) throws JSONException {
		return new VideoInfo(jo.getString("name"), VideoType.REMOTE, jo.getString("url"));
	}
	
}
