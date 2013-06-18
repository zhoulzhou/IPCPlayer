package com.example.ipcplayer.module;

import android.support.v4.widget.CursorAdapter;

/**
 * 抽象一个ListView工厂，输入listview相关的公共方法
 *子类实现抽象方法
 */
public abstract class ListViewAbstractFactory{
	
	protected AbstractLoaderCallback mLoaderCallback;
	
	public abstract int getTitle();
	
	public abstract String getFooterText(int count);
	
	public abstract void customView();
	
	public abstract CursorAdapter createAdapter();
	
	//子类实现。自定义ID以区分不同的cursorLoader
	public abstract int getLoaderCallbackId();
	
	public abstract AbstractLoaderCallback createLoaderCallback();
	
	public AbstractLoaderCallback getLoaderCallback(){
		if(mLoaderCallback == null){
			mLoaderCallback = createLoaderCallback();
		}
		return mLoaderCallback;
	}

	
}