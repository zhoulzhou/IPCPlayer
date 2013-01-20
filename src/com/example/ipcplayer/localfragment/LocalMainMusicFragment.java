package com.example.ipcplayer.localfragment;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.activity.LocalMainMusicActivity;
import com.example.ipcplayer.adapter.GridAdapter;
import com.example.ipcplayer.manager.LocalMusicManager;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class LocalMainMusicFragment extends BaseFragment{
	private LocalMainMusicActivity mLocalMainMusicActivity = null;
	private GridAdapter mAdapter ;
	private GridView mLocalGrid;
	private ArrayList<ItemData> mItemDatas = new ArrayList<ItemData>();
	private LocalMusicManager mLocalMusicManager ;
	private Context mContext ;
	private OnItemClickListener mOnItemClickListener ;
	
	private static String TAG = LocalMainMusicFragment.class.getSimpleName();
	
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
		mContext = getActivity().getBaseContext();
		mLocalMusicManager = new LocalMusicManager(mContext);
		mItemDatas = mLocalMusicManager.getLocalMusicItems();
		mAdapter = new GridAdapter(mContext);
		mLocalGrid.setAdapter(mAdapter);
		mLocalGrid.setOnItemClickListener(mItemClickListener);
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
	
	public void setItemClickListener(OnItemClickListener itemClickListener){
		LogUtil.d(TAG + " setItemClickListener ");
		mOnItemClickListener = itemClickListener;
	}
	
	private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			LogUtil.d(TAG + " onItemClick ");
			if(mItemDatas == null){
				LogUtil.d(TAG + " mItemDatas in null ");
			}
			if(mOnItemClickListener == null ){
				LogUtil.d(TAG + " mOnItemClickListener is null");
			}
			if(mItemDatas == null || mOnItemClickListener == null){
				LogUtil.d(TAG + " mItemData or mOnItemClickListener is null");
				return ;
			}
			LogUtil.d(TAG + " positon = " + position);
			
			ItemData itemData = mItemDatas.get(position);
			LogUtil.d(TAG + " itemData = "+ itemData.toString() + " itemData.mType" + itemData.mType);
			
			switch(itemData.mType){
			case ItemData.DATATYPE_ALLSONG_LIST :
				LogUtil.d(TAG + " ItemData.DATATYPE_ALLSONG_LIST ");
				mOnItemClickListener.onAllSongList();
				break ;
			default:
				
				break ;
			}
		}
	};
	
}