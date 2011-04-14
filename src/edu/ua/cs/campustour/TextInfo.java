package edu.ua.cs.campustour;

import java.io.IOException;
import java.io.InputStream;

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
            
            int size = is.available();
            
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            
            String text = new String(buffer);
            
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
	
	
}
