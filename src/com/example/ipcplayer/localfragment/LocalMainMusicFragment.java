package com.example.ipcplayer.localfragment;

import com.example.ipcplayer.R;
import com.example.ipcplayer.activity.LocalMainMusicActivity;
import com.example.ipcplayer.adapter.GridAdapter;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class LocalMainMusicFragment extends BaseFragment{
	private LocalMainMusicActivity mLocalMainMusicActivity = null;
	private GridAdapter mAdapter ;
	private GridView mLocalGrid;
	
	private static String TAG = LocalMainMusicActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG + " onCreate ");
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		LogUtil.d(TAG + " onAttach ");
		mLocalMainMusicActivity = (LocalMainMusicActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG + " onCreateView ");
		// TODO Auto-generated method stub
		View view =inflater.inflate(R.layout.local_music_grid, container,false);
		mLocalGrid = (GridView) view.findViewById(R.id.localmusicgird);
		return view ;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		LogUtil.d(TAG + " onActivityCreated ");
		mAdapter = new GridAdapter(getActivity().getBaseContext());
		mLocalGrid.setAdapter(mAdapter);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.d(TAG + " onStart ");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.d(TAG + " onResume ");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.d(TAG + " onPause ");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.d(TAG + " onStop ");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		LogUtil.d(TAG + " onDestroyView ");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.d(TAG + " onDestroy ");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		LogUtil.d(TAG + " onDetach ");
	}
	
	
	
}