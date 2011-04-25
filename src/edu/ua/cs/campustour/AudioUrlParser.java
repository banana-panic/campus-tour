package edu.ua.cs.campustour;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.ua.cs.campustour.AvList.AudioInfo;
import edu.ua.cs.campustour.AvList.AudioType;
import edu.ua.cs.campustour.AvList.VideoInfo;

public class AudioUrlParser {
	private AudioUrlParser() {
	}
	
	public static LinkedList<AudioInfo> parse(String audioJson) throws JSONException {
		LinkedList<AudioInfo> r = new LinkedList<AudioInfo>();
		JSONArray arr = (new JSONObject(new JSONTokener(audioJson))).getJSONArray("urls");
		for (int rem = arr.length() - 1; rem >= 0; rem-- ) {
			AudioInfo vi = makeAudioInfo(arr.getJSONObject(rem));
			r.add(vi);
		}
		return r;
	}
	
	private static AudioInfo makeAudioInfo(JSONObject jo) throws JSONException {
		return new AudioInfo(jo.getString("name"), AudioType.REMOTE, jo.getString("url"));
	}
	
}
