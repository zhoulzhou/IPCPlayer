package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class MusicPlayingActivity extends Activity{
	private static final String TAG = MusicPlayingActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_container);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
}