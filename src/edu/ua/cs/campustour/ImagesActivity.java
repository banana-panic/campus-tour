package edu.ua.cs.campustour;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class ImagesActivity extends Activity implements ImageSwitcher.ViewFactory {
	
	private ImageAdapter ia;
	private Gallery gallery;
	private ImageSwitcher switcher;
	private String id;
	private final String TAG = "ImagesActivity";
	private DisplayMetrics dm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		this.id = this.getIntent().getExtras().getString("id");
		this.setContentView(R.layout.image_gallery);
		
        dm = this.getResources().getDisplayMetrics();
		
		switcher = (ImageSwitcher) this.findViewById(R.id.gallery_switcher);
		gallery = (Gallery) this.findViewById(R.id.gallery_filmstrip);
		
		switcher.setFactory(this);
		
		initImageAdapter();
		gallery.setAdapter(ia);
		gallery.setOnItemSelectedListener(ia);
	}
	
	public int scale(float dp) {
		return Math.round(dm.density * dp);
	}
	
	private void initImageAdapter() {
		ia = new ImageAdapter(id);
	}
	
	private class ImageAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {
		private Vector<Bitmap> bitmaps;
		private Bitmap placeholder = null;
		
		public ImageAdapter(String buildingId) {
			Log.d(TAG, "Constructing Adapter");
			AssetManager am = getAssets();
			String [] paths = null;
			try {
				paths = am.list("landmarks/" + buildingId + "/images");
				bitmaps = new Vector<Bitmap>(paths.length);
				for (int i=0; i < paths.length; i++) {
					bitmaps.add(null);
				}
			} catch (IOException e) {
				bitmaps = new Vector<Bitmap>();
				e.printStackTrace();
			}
	
			Log.d(TAG, "Path count: " + paths.length);

			for (int i = 0; i < paths.length; i++) {
				Log.d(TAG, "path: " + paths[i]);
				new Thread(new DiskImageFetcher(i, paths[i])).start();
			}
	
			try {
				placeholder = BitmapFactory.decodeStream(am.open("placeholder"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(TAG, "Finshed Constructing Adapter");
	
		}
		
		private class SetImageTask extends AsyncTask<Integer, Object, BitmapDrawable> {

			@Override
			protected BitmapDrawable doInBackground(Integer... params) {
				int index = params[0];
				Bitmap bm = null;
				try {
					for(int i = 0; i < 10 && bm == null; Thread.sleep(15), i++) bm = bitmaps.get(index);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bm = (bm == null? placeholder : bm);
				return new BitmapDrawable(bm);
			}
			
			@Override
			protected void onPostExecute(BitmapDrawable drawable) {
				switcher.setImageDrawable(drawable);
			}
			
		}

		public int getCount() {
			Log.d(TAG, "size: " + bitmaps.size());
			return bitmaps.size();
		}

		public Object getItem(int position) {
			return bitmaps.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d(TAG, "In getView");
			if (convertView == null) {
				convertView = new ImageView(ImagesActivity.this);
			}
			ImageView i = (ImageView) convertView;
			Bitmap bm = bitmaps.get(position);
			i.setImageBitmap(bm == null ? placeholder : bm);
            i.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, scale(60)));
            i.setAdjustViewBounds(true);
			Log.d(TAG, "getView Returning");
			return i;
		}
	
		private class DiskImageFetcher implements Runnable {
			private int position;
			String path;
	
			public DiskImageFetcher(int position, String path) {
				this.path = path;
				this.position = position;
			}

			public void run() {
				String fullPath = "landmarks/" + id + "/images/" + path;
				try {
					Log.d(TAG, "Begin loading bitmap: " + fullPath);
					Bitmap bm = BitmapFactory.decodeStream(getAssets().open(fullPath));
					runOnUiThread(new ImagePoster(position, bm));
					Log.d(TAG, "Finished loading bitmap: " + fullPath);
				} catch (IOException e) {
					e.printStackTrace();
					Log.d(TAG, "failed loading bitmap: " + fullPath);
				}
			}
	
		}
	
		private class NetImageFetcher implements Runnable {
	
			public void run() {
				
			}
			
		}
		
		private class ImagePoster implements Runnable {
			
			private Bitmap bm;
			private int position;
			
			public ImagePoster(int position, Bitmap bm) {
				this.bm = bm;
				this.position = position;
			}
			
			public void run() {
				bitmaps.set(position, bm);
				ImageAdapter.this.notifyDataSetChanged();
			}
			
		}

		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			Bitmap bm = bitmaps.get(position);
			if (bm == null) {
				new SetImageTask().execute(position);
			}
			else {
				BitmapDrawable drawable = new BitmapDrawable(bm);
				switcher.setImageDrawable(drawable);
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			
		}
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				ImageSwitcher.LayoutParams.FILL_PARENT, ImageSwitcher.LayoutParams.FILL_PARENT));
		i.setAdjustViewBounds(true);
		return i;
	}
	
}
