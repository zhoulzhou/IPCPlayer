package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends BaseFragmentActivity{
	private FrameLayout mHome_Container;
	private View mBottom_View;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	    setContentView(R.layout.main);
	    
	    mHome_Container = (FrameLayout) findViewById(R.id.home_container);
	    mBottom_View = (View) findViewById(R.id.bottom_view);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		super.startActivityFromFragment(fragment, intent, requestCode);
	}
	
}