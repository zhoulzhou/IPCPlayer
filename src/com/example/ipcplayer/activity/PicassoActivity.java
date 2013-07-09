package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class PicassoActivity extends Activity{
	private String[] datas = {
			"http://f.hiphotos.baidu.com/album/w%3D2048/sign=0a14cf4a7c1ed21b79c929e59956dcc4/79f0f736afc3793187d5d891eac4b74542a911cc.jpg",
			"http://g.hiphotos.baidu.com/album/h%3D900%3Bcrop%3D0%2C0%2C1440%2C900/sign=d8228b16ca134954611ee4646675f12a/91ef76c6a7efce1b1bebfb5dae51f3deb58f6560.jpg",	
			"http://e.hiphotos.baidu.com/album/h%3D900%3Bcrop%3D0%2C0%2C1440%2C900/sign=068c657ef3d3572c79e290dcba280055/8ad4b31c8701a18b8b8b20d89f2f07082938fec0.jpg",
			"http://e.hiphotos.baidu.com/album/h%3D900%3Bcrop%3D0%2C0%2C1440%2C900/sign=77acc65afd039245beb5ed0fb7afc7b0/241f95cad1c8a7867d757a996609c93d71cf50cd.jpg",
			"http://c.hiphotos.baidu.com/album/h%3D900%3Bcrop%3D0%2C0%2C1440%2C900/sign=352cfe7bd000baa1a52c4bbb772bda60/b03533fa828ba61e02c2fbb84034970a304e597c.jpg",
		    "http://d.hiphotos.baidu.com/album/h%3D900%3Bcrop%3D0%2C0%2C1440%2C900/sign=3dc1ad74fc1f4134ff37097e1524f6b8/8cb1cb1349540923468b53e99358d109b2de4942.jpg"
	};
	
	private ImageView im ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		im = (ImageView) findViewById(R.id.imageview1);
		boolean run = false;
		if (run) {
			Picasso.with(this).load(datas[0])
			// .error(R.drawable.ic_launcher)
					.into(im);
		}
		
		if (!run) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					this).enableLogging().denyCacheImageMultipleSizesInMemory()
					.build();
			ImageLoader loader = ImageLoader.getInstance();
			loader.init(config);
			loader.displayImage(datas[3], im);
		}
	
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}