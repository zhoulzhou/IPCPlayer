package com.example.ipcplayer.activity;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

public class BaseFragmentActivity extends FragmentActivity{
	private final static String TAG = BaseFragmentActivity.class.getSimpleName();

	@Override
	public FragmentManager getSupportFragmentManager() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getSupportFragmemntManager()");
		return super.getSupportFragmentManager();
	}

	@Override
	public LoaderManager getSupportLoaderManager() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getSupportLoaderManager()");
		return super.getSupportLoaderManager();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onActivityResult()");
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onAttachFragment()");
		super.onAttachFragment(fragment);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreate()");
		super.onCreate(arg0);
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreateView()");
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDestroy()");
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onKeyDown()");
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onMenuItemSelected()");
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onPause()");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onResume()");
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onStart()");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onStop()");
		super.onStop();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " startActivityForResult()");
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " startActivityFromFragment()");
		super.startActivityFromFragment(fragment, intent, requestCode);
	}
	
}