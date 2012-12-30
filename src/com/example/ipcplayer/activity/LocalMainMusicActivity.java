package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class LocalMainMusicActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_main);
	}
	
	private boolean isMultiPane(){
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	
}