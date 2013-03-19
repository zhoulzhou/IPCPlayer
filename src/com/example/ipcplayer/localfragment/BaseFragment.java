package com.example.ipcplayer.localfragment;

import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment{
    private final static String TAG = BaseFragment.class.getSimpleName();
    
	@Override
	public LoaderManager getLoaderManager() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getLoaderManager()");
		return super.getLoaderManager();
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getView()");
		return super.getView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onActivityResult()");
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onAttach()");
		super.onAttach(activity);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onContextItemSelected()");
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreate()");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreateContextMenu()");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreateOptionsMenu()");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onCreateView()");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDestroyOptionsMenu() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDestroyOptionsMenu()");
		super.onDestroyOptionsMenu();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onDetach()");
		super.onDetach();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onOptionsItemSelected()");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onOptionsMenuClosed()");
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onPause()");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onResume()");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onStart()");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onStop()");
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " onViewCreated()");
		super.onViewCreated(view, savedInstanceState);
	}
	
}