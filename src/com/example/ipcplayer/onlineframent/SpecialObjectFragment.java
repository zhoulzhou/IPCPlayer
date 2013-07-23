package com.example.ipcplayer.onlineframent;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.object.MusicFile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SpecialObjectFragment extends BaseOnlineFragment{
	private ListView mList;
	private TextView mEmptyTV;
	private ArrayAdapter mAdapter;
	private Context mContext;
	private ArrayList<MusicFile> mDatas = new ArrayList<MusicFile>();
	private SpecialObjectFragment mInstance;
	
	public SpecialObjectFragment getInstance(){
		if(mInstance == null){
			mInstance = new SpecialObjectFragment();
		}
		return mInstance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_online_list, null);
		mList = (ListView) v.findViewById(R.id.online_home_list);
		mEmptyTV = (TextView) v.findViewById(R.id.online_list_empty);
		MusicFile object = new MusicFile();
		mDatas.add(object);
		mAdapter = new SpecialObjectAdapter(mContext, R.layout.specialobject_item, 0, mDatas);
		mList.setAdapter(mAdapter);
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
}