package edu.ua.cs.campustour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AvList extends Activity {
	public static enum VideoType { REMOTE, LOCAL };
	public static enum AudioType { REMOTE, LOCAL };
	private ListView lv;
	private ArrayList<VideoInfo> videoInfos;
	private ArrayList<AudioInfo> audioInfos;
	private String id;
	private final String TAG = "AvList";

	public static class VideoInfo implements Comparable<VideoInfo>{
		final public String name;
		final public VideoType type;
		final public String path;

		public VideoInfo(String name, VideoType type, String path) {
			this.name = name;
			this.type = type;
			this.path = path;
		}

		public int compareTo(VideoInfo another) {
			return name.compareTo(another.name);
		}
	}

	public static class AudioInfo implements Comparable<AudioInfo>{
		final public String name;
		final public AudioType type;
		final public String path;

		public AudioInfo(String name, AudioType type, String path) {
			this.name = name;
			this.type = type;
			this.path = path;
		}

		public int compareTo(AudioInfo another) {
			return name.compareTo(another.name);
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		id = extras.getString("id");
		List<VideoInfo> vi = getRemoteVideos();
		vi.addAll(getLocalVideos());
		this.videoInfos = new ArrayList<VideoInfo>(vi.size());
		videoInfos.addAll(vi);
		Collections.sort(videoInfos);

		List<AudioInfo> ai = getRemoteAudio();
		ai.addAll(getLocalAudio());
		this.audioInfos = new ArrayList<AudioInfo>(ai.size());
		audioInfos.addAll(ai);
		Collections.sort(audioInfos);

		setContentView(R.layout.avlist);
		lv = (ListView) findViewById(R.id.avlist);
		AvListAdapter adapter = new AvListAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(adapter);
	}

	private class AvListAdapter extends BaseAdapter implements ListAdapter, AdapterView.OnItemClickListener {

		public int getCount() {
			return videoInfos.size() + audioInfos.size() + 2;
		}

		public Object getItem(int position) {
			Object o;
			if (position == 0) {
				o = "Audio";
			}
			else if (position == videoInfos.size()) {
				o = "Video";
			}
			else if (position <= videoInfos.size()) {
				o = videoInfos.get(position - 1);
			} else {
				o = audioInfos.get(position - 2 - videoInfos.size());
			}
			return o;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				if (convertView == null)
					convertView = View.inflate(AvList.this, R.layout.list_divider, null);
				((TextView) convertView).setText("Video");
			} else if (position == videoInfos.size() + 1) {
				if (convertView == null) 
					convertView = View.inflate(AvList.this, R.layout.list_divider, null);
				((TextView) convertView).setText("Audio");
			} else if (position <= videoInfos.size()) {
				if (convertView == null)
					convertView = View.inflate(AvList.this, R.layout.list_item, null);
				TextView t = (TextView) convertView.findViewById(R.id.list_item_text);
				t.setText(videoInfos.get(position - 1).name);
			} else {
				if (convertView == null)
					convertView = View.inflate(AvList.this, R.layout.list_item, null);
				TextView t = (TextView) convertView.findViewById(R.id.list_item_text);
				t.setText(audioInfos.get(position - 2 - videoInfos.size()).name);
				
			}
			return convertView;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return (position != 0 && (position != videoInfos.size() + 1));
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d(TAG, "clicked item " + position);
			if (position > 0 && position <= videoInfos.size()) {
				VideoInfo vi = videoInfos.get(position - 1);
				if (vi.type == VideoType.LOCAL) {
					Log.i(TAG, "Operation Not Supported");
				}
				else {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(vi.path)));
				}
			} else if (position > videoInfos.size() + 1) {
				AudioInfo ai = audioInfos.get(position - 2 - videoInfos.size());
				if (ai.type == AudioType.LOCAL) {
					Log.i(TAG, "Operation Not Supported");
				}
				else {
					Uri data = Uri.parse(ai.path);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(data, "audio");
					startActivity(intent);
				}
			}
		}
		
	}
	
	private String readStream(InputStream is) throws IOException {
		StringBuffer stringBuf = new StringBuffer(1024);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = br.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			stringBuf.append(readData);
			buf = new char[1024];
		}
		return stringBuf.toString();
	}

	private List<VideoInfo> getRemoteVideos() {
		LinkedList<VideoInfo> videoInfos = null;
		try {
			AssetManager am = getAssets();
			InputStream is;
			String videoUrls = null;
			is = am.open("landmarks/" + id + "/v");
			videoUrls = readStream(is);
			is.close();
			videoInfos = VideoUrlParser.parse(videoUrls);
		} catch (IOException e) {
			videoInfos = new LinkedList<VideoInfo>();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (videoInfos == null) videoInfos = new LinkedList<VideoInfo>();
		return videoInfos;
	}

	private List<VideoInfo> getLocalVideos() {
		LinkedList<VideoInfo> videoInfos = null;
		AssetManager am = getAssets();
		try {
			String[] paths = am.list("landmarks/)" + id + "/video");
			videoInfos = new LinkedList<VideoInfo>(); 
			for (String path : paths) {
				String fullPath = "landmanrks/" + id + "/video/" + path;
				VideoInfo vi = new VideoInfo(path, VideoType.LOCAL, fullPath);
				videoInfos.add(vi);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return videoInfos;
	}

	private List<AudioInfo> getRemoteAudio() {
		LinkedList<AudioInfo> audioInfos = null;
		try {
			AssetManager am = getAssets();
			InputStream is;
			String audioUrls = null;
			is = am.open("landmarks/" + id + "/a");
			audioUrls = readStream(is);
			is.close();
			audioInfos = AudioUrlParser.parse(audioUrls);
		} catch (IOException e) {
			audioInfos = new LinkedList<AudioInfo>();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (audioInfos == null) audioInfos = new LinkedList<AudioInfo>();
		return audioInfos;
	}

	private List<AudioInfo> getLocalAudio() {
		LinkedList<AudioInfo> audioInfos = null;
		AssetManager am = getAssets();
		try {
			String[] paths = am.list("landmarks/)" + id + "/audio");
			audioInfos = new LinkedList<AudioInfo>(); 
			for (String path : paths) {
				String fullPath = "landmanrks/" + id + "/audio/" + path;
				AudioInfo vi = new AudioInfo(path, AudioType.LOCAL, fullPath);
				audioInfos.add(vi);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return audioInfos;
	}
	
}
