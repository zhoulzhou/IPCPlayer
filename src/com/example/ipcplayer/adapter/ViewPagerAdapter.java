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
	private ViewPager mViewPager;

    public ViewPagerAdapter(List<View> views, List<String> titles){
    	mViewList = views;
    	mTitles = titles;
    }
    
    public void addView(View view){
    	mViewList.add(view);
    }
    
    public void clearViews(){
    	if(mViewPager != null){
    		mViewPager.removeAllViews();
    		mViewList.clear();
    	}
    }
	
    private View findView(int position){
    	View view = mViewList.get(position);
    	return view;
    }
    
	@Override
	public int getCount() {
		return mViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ViewPager viewPager = (ViewPager) container;
		View view = findView(position);
		viewPager.removeView(view);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewPager viewPager = (ViewPager) container;
		if(mViewPager != viewPager){
			mViewPager = viewPager;
		}
		View view  = findView(position);
		viewPager.addView(view);
		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		super.restoreState(state, loader);
	}

	@Override
	public Parcelable saveState() {
		return super.saveState();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}
	
}