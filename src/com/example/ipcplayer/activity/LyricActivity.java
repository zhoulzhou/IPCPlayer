package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.lyric.LyricGetter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LyricActivity extends Activity{
	private static final String TAG = LyricActivity.class.getSimpleName();
	
	private TextView lyricTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyric_view);
		
		lyricTV = (TextView) findViewById(R.id.lyric);
		LyricGetter lyricGetter = new LyricGetter();
//		lyricGetter.get(lyricFileName);
		
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