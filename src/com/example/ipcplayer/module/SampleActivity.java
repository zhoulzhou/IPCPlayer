package com.example.ipcplayer.module;

import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.LogUtil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class SampleActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.sample_main);
		LogUtil.d("onCreate ");
		SampleFragment sampleFragment = new SampleFragment();
		if(sampleFragment != null){
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.replace(R.id.main, sampleFragment);
			ft.commit();
		}else {
			LogUtil.d("fragment is null");
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
}