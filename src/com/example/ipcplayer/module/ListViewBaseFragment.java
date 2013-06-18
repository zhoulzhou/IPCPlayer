package com.example.ipcplayer.module;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ipcplayer.R;
import com.example.ipcplayer.localfragment.BaseFragment;

/**
 * 总体框架  
 * 子类实现具体的界面
 */
public abstract class ListViewBaseFragment extends BaseFragment{

	protected Context mContext;
	
	protected ListViewAbstractFactory mFactory;
	
	protected View mContentView;
	protected View mHeadView;
	protected View mFootView;
	
	protected ListView mListView;
	protected CursorAdapter mAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		mContentView = inflater.from(mContext).inflate(R.layout.listview_base, null);
		mListView = (ListView) mContentView.findViewById(R.id.list_base);
		
		mAdapter = mFactory.createAdapter();
		mListView.setAdapter(mAdapter);
		
		getLoaderManager().initLoader(mFactory.getLoaderCallbackId(), null,
				mFactory.getLoaderCallback());
		
		//显示加载界面
		showLoadingDialog();
		
		return mContentView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//刷新列表  可能需要条件
		refreshListView();
	}
	
	protected void showLoadingDialog(){
		
	}
	
	protected void refreshListView(){
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
}