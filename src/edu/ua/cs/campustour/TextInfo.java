package edu.ua.cs.campustour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class TextInfo extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textinfo);
		
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
        
		Bundle b = this.getIntent().getExtras();
		String id = b.getString("id");
		String rootdir = "landmarks/"+id+"/";

		ScrollView sv = (ScrollView)findViewById(R.id.infoview);
		ImageView bv = (ImageView)findViewById(R.id.infoviewbg);
		TextView title = (TextView)findViewById(R.id.title);
        TextView info = (TextView)findViewById(R.id.info);
		
		try {
            InputStream is = getAssets().open(rootdir+"info");
            String text = readStream(is);
            is.close();
            info.setText(text);
            
            is = getAssets().open(rootdir+"title");
            text = readStream(is);
            is.close();
            title.setText(text);
            
            int orientation = getResources().getConfiguration().orientation;
            String path = (orientation == Configuration.ORIENTATION_PORTRAIT) ? "port" : "land";
			bv.setImageBitmap(BitmapFactory.decodeStream((this.getAssets().open(rootdir+"bg-"+path))));
			
		} catch (IOException e) {
            throw new RuntimeException(e);
        }

        sv.setPadding(0, dm.heightPixels/8, 0, 0);
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
