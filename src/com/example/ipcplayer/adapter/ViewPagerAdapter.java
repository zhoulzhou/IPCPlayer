package com.example.ipcplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter{
	private List<View> mViewList = new ArrayList<View>();
	private List<String> mTitles = new ArrayList<String>();

    public ViewPagerAdapter(List<View> views, List<String> titles){
    	mViewList = views;
    	mTitles = titles;
    }	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView(mViewList.get(position));
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub
		super.finishUpdate(container);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		// TODO Auto-generated method stub
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return mTitles.get(position);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(mViewList.get(position), 0);
        return mViewList.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		// TODO Auto-generated method stub
		super.restoreState(state, loader);
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return super.saveState();
	}

	@Override
	public void setPrimaryItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void startUpdate(View container) {
		// TODO Auto-generated method stub
		super.startUpdate(container);
	}

	@Override
	public void startUpdate(ViewGroup container) {
		// TODO Auto-generated method stub
		super.startUpdate(container);
	}
	
}