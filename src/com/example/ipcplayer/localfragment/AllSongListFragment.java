package com.example.ipcplayer.localfragment;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.adapter.AllSongListAdapter;
import com.example.ipcplayer.controller.LocalMusicController;
import com.example.ipcplayer.manager.LocalMusicManager;
import com.example.ipcplayer.object.MusicFile;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AllSongListFragment extends ListFragment{
	private static String TAG  = AllSongListFragment.class.getSimpleName();
 
	private static String sCurChoicePosition = "curChoicePosition";
	private int mCurChoicePosition = 0;
	private Context mContext ;
	private ListView mLv ;
	private TextView mTv;
	private LocalMusicManager mLocalMusicManager;
	private Cursor mCursor;
	private AllSongListAdapter mAllSongListAdapter;
	private ArrayList<MusicFile> mPlayList = null;
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		LogUtil.d(TAG + " onSavedInstanceState ");
		savedInstanceState.putInt(sCurChoicePosition, mCurChoicePosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		LogUtil.d(TAG + " onListItemClick ");
		LogUtil.d(TAG + " onListItemClick position: " + position + " id: " + id);
		mCurChoicePosition = position ;
		LocalMusicController.getInstance(mContext).playMusic(id,mCursor);
	}

	
	@Override
	public void onAttach(Activity activity) {
		mContext = getActivity();
		super.onAttach(activity);
		LogUtil.d(TAG + " onAttach ");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG + " onCreate ");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG + " onCreateView ");
		View v = inflater.inflate(R.layout.all_song_list,container, false);
//		mLv = (ListView) v.findViewById(R.id.list);
//		mLv = getListView();
//		LogUtil.d(TAG + " mLv = " + mLv);
		mTv = (TextView) v.findViewById(R.id.empty);
		
		return v ;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.d(TAG + " onActivityCreated ");
		if(savedInstanceState != null){
			mCurChoicePosition = savedInstanceState.getInt(sCurChoicePosition,0);
		}
		
		mLocalMusicManager = new LocalMusicManager(mContext);
		mCursor = mLocalMusicManager.getAllSongCursor();
		if(mCursor == null){
			LogUtil.d(TAG + " mCursor is null");
		}else {
			LogUtil.d(TAG + " mCursor = " + mCursor);
		}
		mAllSongListAdapter = new AllSongListAdapter(mContext,mCursor,R.layout.all_song_list_item);
		if(mAllSongListAdapter == null){
			LogUtil.d(TAG + " mAllSongListAdapter is null ");
		}
		if(mLv == null){
			LogUtil.d(TAG + " mLv is null ");
		}
		getListView().setAdapter(mAllSongListAdapter);
		
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG + " onStart ");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG + " onResume ");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG + " onPause ");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG + " onStop ");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.d(TAG + " onDestroyView ");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG + " onDestroy ");
		LocalMusicController.releaseInstance();
		if(mCursor != null){
			mCursor.close();
			mCursor = null ;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG + " onDetach ");
	}
	
}