package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class TestImageActivity extends Activity{
	private ImageView im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_image);
		
		im = (ImageView) findViewById(R.id.image1);
		
		int width = im.getWidth();
		LogUtil.d("width= " + width);
		int height = im.getHeight();
		LogUtil.d("height= " + height);
		
//		getScreenDIP();
		
		DisplayMetrics displayMetrics = new DisplayMetrics();   
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);  
        height = displayMetrics.heightPixels;  
        width = displayMetrics.widthPixels;  
        LogUtil.d("height: "+height);  
        LogUtil.d( "width: "+width);  
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
//		params.setMargins(margin, 0, margin, 0);
//		params.rightMargin
//		params.addRule(LinearLayout.ALIGN_PARENT_TOP);
		im.setLayoutParams(params);
	}
	
	private void getScreenDIP(){
		// 获取屏幕密度（方法1）  
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();       // 屏幕宽（像素，如：480px）  
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();      // 屏幕高（像素，如：800p）  
		  
		LogUtil.d("  1----------  screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);  
		  
		  
		// 获取屏幕密度（方法2）  
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();  
		  
		float density  = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
		int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
		float xdpi = dm.xdpi;             
		float ydpi = dm.ydpi;  
		  
		LogUtil.d( "  2---------- xdpi=" + xdpi + "; ydpi=" + ydpi);  
		LogUtil.d( "  2---------- density=" + density + "; densityDPI=" + densityDPI);  
		  
		screenWidth  = dm.widthPixels;      // 屏幕宽（像素，如：480px）  
		screenHeight = dm.heightPixels;     // 屏幕高（像素，如：800px）  
		  
		LogUtil.d( "  2----------  screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);  
		  
		  
		  
		// 获取屏幕密度（方法3）  
		dm = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);  
		  
		density  = dm.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
		densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
		xdpi = dm.xdpi;           
		ydpi = dm.ydpi;  
		  
		LogUtil.d( " 3---------- xdpi=" + xdpi + "; ydpi=" + ydpi);  
		LogUtil.d( " 3---------- density=" + density + "; densityDPI=" + densityDPI);  
		  
		int screenWidthDip = dm.widthPixels;        // 屏幕宽（dip，如：320dip）  
		int screenHeightDip = dm.heightPixels;      // 屏幕宽（dip，如：533dip）  
		  
		LogUtil.d( "  3---------- screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);  
		  
		screenWidth  = (int)(dm.widthPixels * density + 0.5f);      // 屏幕宽（px，如：480px）  
		screenHeight = (int)(dm.heightPixels * density + 0.5f);     // 屏幕高（px，如：800px）  
		  
		LogUtil.d( "  3---------- screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);  
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
}