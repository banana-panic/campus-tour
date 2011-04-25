package edu.ua.cs.campustour;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
	private int lock;
	private Thread switcherThread = null;
	
	public static enum ImageType {LOCAL, REMOTE};
	
	private class OrientationState {
		public final Object adapterState;
		public final String id;
		public OrientationState(Object adapterState, String id) {
			this.adapterState = adapterState;
			this.id = id;
		}
	}
	
	public class ImageInfo {
		public final String name;
		public final String path;
		public final ImageType type;
		
		public ImageInfo(String name, ImageType type, String path) {
			this.name = name;
			this.path = path;
			this.type = type;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.image_gallery);
		switcher = (ImageSwitcher) this.findViewById(R.id.gallery_switcher);
		gallery = (Gallery) this.findViewById(R.id.gallery_filmstrip);
		switcher.setFactory(this);
        dm = this.getResources().getDisplayMetrics();
		
		OrientationState os = (OrientationState) getLastNonConfigurationInstance();
		if (os != null) {
			this.id = os.id;
			ia = new ImageAdapter(os.adapterState);
		}
		else {
			this.id = this.getIntent().getExtras().getString("id");
			initImageAdapter();
		}
		gallery.setAdapter(ia);
		gallery.setOnItemSelectedListener(ia);
	}
	
	public int scale(float dp) {
		return Math.round(dm.density * dp);
	}
	
	private void initImageAdapter() {
		ia = new ImageAdapter();
	}
	
	private class ImageAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {
		private Bitmap placeholder = null;
		private List<String> names;
		private Map<String, ImageInfo> sources;
		private Map<String, Bitmap> thumbnails;
		
		public ImageAdapter() {
			AssetManager am = getAssets();
			String[] paths = null;
			try {
				paths = am.list("landmarks/" + id + "/images");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String urlJson = readJson();
			
			List<ImageInfo> urlInfos = ImageUrlParser.parse(urlJson);
			
			int imageCount = urlInfos.size() + paths.length;
			names = new ArrayList<String>(imageCount);
			sources = new HashMap<String, ImageInfo>(imageCount);
			thumbnails = new HashMap<String, Bitmap>(imageCount);
			
			String[] fullPaths = new String[paths.length];
			
			for (int i = 0; i < paths.length; i++) {
				String path = paths[i];
				String fullPath = "landmarks/" + id + "/images/" + path;
				fullPaths[i] = fullPath;
				names.add(path);
				sources.put(path, new ImageInfo(path, ImageType.LOCAL, fullPath));
			}
			
			for (ImageInfo i : urlInfos) {
				names.add(i.name);
				sources.put(i.name, i);
			}
			
			Collections.sort(names);
	
			try {
				placeholder = BitmapFactory.decodeStream(am.open("placeholder"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			new Thread(new DiskThumbnailBuilder(paths, fullPaths)).start();
	
		}
		
		public ImageAdapter(Object state) {
			OrientationState os = (OrientationState) state; 
			this.placeholder = os.placeholder;
			this.thumbnails = os.thumbnails;
			this.names = os.names;
			this.sources = os.sources;
		}
		
		public Object getOrientationState() {
			return new OrientationState(placeholder, names, sources, thumbnails);
		}
		
		private class OrientationState {
			public final Bitmap placeholder;
			public final List<String> names;
			public final Map<String, ImageInfo> sources;
			public final Map<String, Bitmap> thumbnails;
			
			public OrientationState(Bitmap placeholder, List<String> names,
					Map<String, ImageInfo> sources, Map<String, Bitmap> thumbnails) {
				this.placeholder = placeholder;
				this.names = names;
				this.sources = sources;
				this.thumbnails = thumbnails;
			}
		}
		
		
		public int getCount() {
			return thumbnails.entrySet().size();
		}

		public Object getItem(int position) {
			String name = names.get(position);
			return thumbnails.get(name);
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
			String name = names.get(position);
			Bitmap bm = thumbnails.get(name);
			i.setImageBitmap(bm == null ? placeholder : bm);
            i.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, scale(60)));
            i.setAdjustViewBounds(true);
			return i;
		}
		
		private class DiskThumbnailBuilder implements Runnable {
			private String[] names;
			private String[] paths;
			
			public DiskThumbnailBuilder(String[] names, String[] paths) {
				this.names = names;
				this.paths = paths;
			}
			public void run() {
				AssetManager am = getAssets();
				for(int i = 0; i < names.length; i++) {
					try {
						Bitmap orig = null;
						Bitmap scaled = null;
						InputStream is = am.open(paths[i]);
						orig = BitmapFactory.decodeStream(is);
						is.close();
						int width = Math.round(orig.getWidth() * (float) scale(60) / orig.getHeight());
						scaled = Bitmap.createScaledBitmap(orig, width * 2, scale(60) * 2, true);
						orig.recycle();
						runOnUiThread(new ThumbnailPoster(names[i], scaled));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
		private class ThumbnailPoster implements Runnable {
			
			private String name;
			private Bitmap bm;
			
			public ThumbnailPoster(String name, Bitmap bm) {
				this.name = name;
				this.bm = bm;
			}
			
			public void run() {
				thumbnails.put(name, bm);
				ImageAdapter.this.notifyDataSetChanged();
			}
			
		}

		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String name = names.get(position);
			ImageInfo i = sources.get(name);
			String path = i.path;
			Bitmap thumbnail = thumbnails.get(name);
			lock++;
			if (ImagesActivity.this.switcherThread != null) {
				ImagesActivity.this.switcherThread.interrupt();
			}
			ImagesActivity.this.switcherThread =
				new Thread(new DiskSwitcherSetter(path, lock, thumbnail));
			ImagesActivity.this.switcherThread.start();
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
	
	private String readJson() {
		return "";
	}
	
	private class DiskSwitcherSetter implements Runnable {
		private String path;
		private int lock;
		Bitmap thumbnail;
		
		public DiskSwitcherSetter(String path, int lock, Bitmap thumbnail) {
			this.path = path;
			this.lock = lock;
			this.thumbnail = thumbnail;
		}
		
		public void run() {
			Bitmap fullSize = null;
			runOnUiThread(new SwitcherPoster(thumbnail, lock, false));
			try {
				InputStream is = getAssets().open(path);
				fullSize = BitmapFactory.decodeStream(is);
				is.close(); 
				
				int scaledHeight = 0;
				int scaledWidth = 0;
				for (int i = 0; i < 5 && scaledHeight == 0 && scaledWidth == 0; i++, Thread.sleep(15)) {
					int viewHeight = switcher.getHeight();
					int viewWidth = switcher.getWidth();
					
					int bmHeight = fullSize.getHeight();
					int bmWidth = fullSize.getWidth();
				
					boolean port = (viewHeight / (float) bmHeight) * bmWidth < viewWidth;
					scaledHeight = (port ? viewHeight :
						Math.round(bmHeight * viewWidth / (float) bmWidth));
					scaledWidth = (port ? Math.round(bmWidth * viewHeight / (float) bmHeight) :
						viewWidth);
				}
				
				Bitmap resized = null;
				if (scaledHeight != 0 && scaledWidth != 0)
					resized = Bitmap.createScaledBitmap(fullSize, scaledWidth, scaledHeight, true);
				
				fullSize.recycle();
				
				if (resized != null )runOnUiThread(new SwitcherPoster(resized, lock, true));
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
				if (fullSize != null) fullSize.recycle();
			}
		}
		
	}
	
	private class SwitcherPoster implements Runnable {
		private Bitmap bm;
		private int lock;
		private boolean canRecycle;

		public SwitcherPoster(Bitmap bm, int lock, boolean canRecyle) {
			this.bm = bm;
			this.lock = lock;
			this.canRecycle = canRecyle;
		}
		public void run() {
			if (ImagesActivity.this.lock == lock) {
				switcher.setImageDrawable(new BitmapDrawable(bm));
			}
			else if (canRecycle){
				bm.recycle();
			}
		}
		
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object adapterState = ia.getOrientationState();
		return new OrientationState(adapterState, id);
	}	
}
