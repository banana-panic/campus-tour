package edu.ua.cs.campustour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ScrollView;
import android.widget.TextView;

public class TextInfo extends Activity {
	private Drawable bg = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textinfo);
		
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		int h = dm.heightPixels;
		
		Bundle b = this.getIntent().getExtras();
		String id = b.getString("id");
		String titleText = b.getString("name");
		String rootdir = "landmarks/"+id+"/";

		ScrollView sv = (ScrollView)findViewById(R.id.infoview);
		TextView title = (TextView)findViewById(R.id.title);
        TextView info = (TextView)findViewById(R.id.info);
        title.setText(titleText);
		
		try {
            InputStream is = getAssets().open(rootdir+"info");
            
            String text = readStream(is);
            
            info.setText(text);
            if(bg == null) {
	            bg = new BitmapDrawable(BitmapFactory.decodeStream((this.getAssets().open(rootdir+"bg"))));
	            Drawable[] da = {bg, this.getResources().getDrawable(R.drawable.whitemask)};
	            LayerDrawable ld = new LayerDrawable(da);
            	sv.setBackgroundDrawable(ld);
            }
		} catch (IOException e) {
            throw new RuntimeException(e);
        }

        sv.setPadding(0, h/8, 0, 0);
	}
	
	String readStream(InputStream is) throws IOException {
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
	
	
}
