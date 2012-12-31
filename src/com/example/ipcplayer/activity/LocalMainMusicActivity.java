package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.util.AttributeSet;
import android.view.View;

public class LocalMainMusicActivity extends FragmentActivity{
	private static String TAG = LocalMainMusicActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_main);
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onAttachFragment ");
		super.onAttachFragment(fragment);
	}

	@Override
	public FragmentManager getSupportFragmentManager() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getSupportFragmentManager ");
		return super.getSupportFragmentManager();
	}

	@Override
	public LoaderManager getSupportLoaderManager() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getSupportLoaderManager ");
		return super.getSupportLoaderManager();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreateView ");
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onPause ");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onResume ");
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onSaveInstanceState ");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onStart ");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onStop ");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDestroy ");
		super.onDestroy();
	}

	private boolean isMultiPane(){
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	
}