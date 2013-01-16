package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.localfragment.AllSongListFragment;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.util.AttributeSet;
import android.view.View;

public class AllSongListActivity extends FragmentActivity{
	private static String TAG = AllSongListActivity.class.getSimpleName();

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
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onAttachFragment ");
		super.onAttachFragment(fragment);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onConfigurationChanged ");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreate ");
		super.onCreate(arg0);
		setContentView(R.layout.all_song_list_main);
		
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		AllSongListFragment allSongListFragment = new AllSongListFragment();
//		fragmentTransaction.add(R.id.allsonglistfragment, allSongListFragment);
//		fragmentTransaction.commit();
//		setContentView(R.layout.all_song_list);
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreateView ");
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDestroy ");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onPause ");
		super.onPause();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onPostResume ");
		super.onPostResume();
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
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " startActivityFromFragment ");
		super.startActivityFromFragment(fragment, intent, requestCode);
	}
	
}