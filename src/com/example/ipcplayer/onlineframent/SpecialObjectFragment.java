package com.example.ipcplayer.onlineframent;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SpecialObjectFragment extends BaseOnlineFragment{
	private ListView mList;
	private TextView mEmptyTV;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_online_list, null);
		mList = (ListView) v.findViewById(R.id.online_home_list);
		mEmptyTV = (TextView) v.findViewById(R.id.online_list_empty);
		
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