package com.example.ipcplayer.module;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * 对cursorloader回调的抽象
 * 子类实现抽象的方法
 *
 */
public abstract class AbstractLoaderCallback implements LoaderCallbacks<Cursor>{
	protected CursorLoader mCursorLoader;
	
	public abstract CursorLoader createCursorLoader();
	
	public abstract void onDataChanged(Cursor cursor);
	
	public abstract void onNoData();
	
	public abstract void onDataReset();

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		if(mCursorLoader == null){
			mCursorLoader = createCursorLoader();
		}
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor != null && cursor.getCount() != 0){
			onDataChanged(cursor);
		}else {
			onNoData();
		}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		onDataReset();
		mCursorLoader = null;
	}
	
}