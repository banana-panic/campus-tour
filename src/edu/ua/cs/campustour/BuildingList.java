package edu.ua.cs.campustour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BuildingList extends Activity {
	public static final String Map = "map";
	public static final String ShowSearchBox = "searchbox";
	private HashMap<String, Building> buildingMap;
	private Boolean searchBox;
	private final String TAG = "Search";
	private BuildingAdapter ba;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	    Log.d(TAG, "In BuildingList");

	    Intent intent = getIntent();
	    buildingMap = (HashMap<String, Building>) intent.getSerializableExtra(Map);
	    searchBox = intent.getBooleanExtra(ShowSearchBox, false);
	    
	    ListView lv = (ListView) findViewById(R.id.searchlist);
	    ba = new BuildingAdapter(buildingMap);
	    lv.setAdapter(ba);
	    lv.setOnItemClickListener(ba);
	    
	    if (searchBox) {
	    	onSearchRequested();
	    }
	    
	    handleIntent(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		Log.d(TAG, "handleAction");
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      ba.searchFor(query);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.buildinglist_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.buildinglist_reset:
			ba.searchFor("");
			break;
		case R.id.buildinglist_searchbutton:
			onSearchRequested();
			break;
		default:
			Log.w(TAG, "Unexpected MenuItem");
		}
		return true;
	}
	
	private class BuildingAdapter extends BaseAdapter implements ListAdapter, OnItemClickListener {
		
		private HashMap<String, Building> init;
		private ArrayList<Building> current;
		
		public BuildingAdapter(HashMap<String, Building> map) {
			init = map;
			current = new ArrayList<Building>(init.size());
			for (Building v : init.values()) {
				current.add(v);
			}
			Collections.sort(current);
		}
		
		public void searchFor(String query) {
			current = new ArrayList<Building>(init.size());
			for (Building v : init.values()) {
				if (v.name.toLowerCase().contains(query.toLowerCase())) {
					current.add(v);
				}
			}
			Collections.sort(current);
			notifyDataSetChanged();
		}
		
		public int getCount() {
			return current.size();
		}

		public Object getItem(int i) {
			return current.get(i);
		}

		public long getItemId(int position) {
			return System.identityHashCode(current.get(position));
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {
				tv = (TextView) View.inflate(BuildingList.this, R.layout.list_item, null);
			} else {
				tv = (TextView) convertView;
			}
			tv.setText(current.get(position).name); 
			return tv;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("id", current.get(position).id);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		
	}
}
