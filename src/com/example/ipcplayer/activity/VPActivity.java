package com.example.ipcplayer.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.adapter.ViewPagerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class VPActivity extends Activity{
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	private List<View> mViewList = new ArrayList<View>();
	private List<String> mTitles = new ArrayList<String>();
    private ViewPagerAdapter adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vp_main_1);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_1);
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.vp_1_title);
		
		LayoutInflater inflate = LayoutInflater.from(this);
		View view1 = inflate.inflate(R.layout.vp_1_view1, null);
		View view2 = inflate.inflate(R.layout.vp_1_view2, null);
		View view3 = inflate.inflate(R.layout.vp_1_view3, null);
		
		mViewList.add(view1);
		mViewList.add(view2);
		mViewList.add(view3);
		
		mTitles.add("tab_1");
		mTitles.add("tab_2");
		mTitles.add("tab_3");
		
		adapter = new ViewPagerAdapter(mViewList,mTitles);
		mViewPager.setAdapter(adapter);
	}
	
}