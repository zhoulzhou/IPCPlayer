package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.localfragment.AllSongListFragment;
import com.example.ipcplayer.localfragment.LocalMainMusicFragment;
import com.example.ipcplayer.localfragment.OnItemClickListener;
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

public class LocalMainMusicActivity extends FragmentActivity implements OnItemClickListener{
	private static String TAG = LocalMainMusicActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.d(TAG + " onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_main);
		LocalMainMusicFragment localMainMusicFragment = (LocalMainMusicFragment) getSupportFragmentManager().findFragmentById(R.id.localmainframent);
		localMainMusicFragment.setItemClickListener(this);
		
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		LogUtil.d(TAG + " onAttachFragment ");
		super.onAttachFragment(fragment);
	}

	@Override
	public FragmentManager getSupportFragmentManager() {
		LogUtil.d(TAG + " getSupportFragmentManager ");
		return super.getSupportFragmentManager();
	}

	@Override
	public LoaderManager getSupportLoaderManager() {
		LogUtil.d(TAG + " getSupportLoaderManager ");
		return super.getSupportLoaderManager();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		LogUtil.d(TAG + " onCreateView ");
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onPause() {
		LogUtil.d(TAG + " onPause ");
		super.onPause();
	}

	@Override
	protected void onResume() {
		LogUtil.d(TAG + " onResume ");
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		LogUtil.d(TAG + " onSaveInstanceState ");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		LogUtil.d(TAG + " onStart ");
		super.onStart();
	}

	@Override
	protected void onStop() {
		LogUtil.d(TAG + " onStop ");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogUtil.d(TAG + " onDestroy ");
		super.onDestroy();
	}

	private boolean isMultiPane(){
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	@Override
	public void onAllSongList() {
		LogUtil.d(TAG + " onAllSongList ");
		if(isMultiPane()){
			AllSongListFragment allSongListFragment = (AllSongListFragment) getSupportFragmentManager().findFragmentById(R.id.local_container);
			if(allSongListFragment == null){
				allSongListFragment = new AllSongListFragment();
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.replace(R.id.local_container, allSongListFragment);
				ft.addToBackStack("allSongListFragment");
				ft.commit();
			}
			
		}else {
			Intent intent = new Intent();
			intent.setClass(this, AllSongListActivity.class);
			startActivity(intent);
		}
	}
	
	
}