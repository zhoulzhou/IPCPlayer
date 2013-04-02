package com.example.ipcplayer.localfragment;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.adapter.ViewPagerAdapter;
import com.example.ipcplayer.homeview.LocalMainView;
import com.example.ipcplayer.homeview.MusicHomeView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment{
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	private ViewPagerAdapter mAdapter;
	
	private Context mContext;
	
	private List<View> mViewList = new ArrayList<View>();
	private List<String> mTitles = new ArrayList<String>();
	
	public HomeFragment(){
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new ViewPagerAdapter(mViewList,mTitles);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(1);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.vp_main_1, null);
		mViewPager = (ViewPager) v.findViewById(R.id.vp_1);
		mPagerTitleStrip = (PagerTitleStrip) v.findViewById(R.id.vp_1_title);
		
		LayoutInflater inflate = LayoutInflater.from(mContext);
		View view1 = inflate.inflate(R.layout.vp_1_view1,null);
		View view2 = new MusicHomeView(mContext);
//		View view2 = inflate.inflate(R.layout.vp_1_view2, null);
		View view3 = inflate.inflate(R.layout.vp_1_view3, null);
		
		mViewList.add(view1);
		mViewList.add(view2);
		mViewList.add(view3);
		
		mTitles.add("tab_1");
		mTitles.add("tab_2");
		mTitles.add("tab_3");
		
		return v ;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(mAdapter != null){
			mAdapter.clearViews();
			mAdapter = null;
		}
		
//		if(view2 != null){
//			view2.release();
//		}
	}

	
	
}