package com.example.ipcplayer.module;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SampleFragment extends ListViewBaseFragment{

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mFactory = new SongListViewFactory();//初始化 要实现的工厂
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	public class SongListViewFactory extends ListViewAbstractFactory{

		@Override
		public int getTitle() {
			return 0;
		}

		@Override
		public String getFooterText(int count) {
			return null;
		}

		@Override
		public void customView() {
			
		}

		@Override
		public CursorAdapter createAdapter() {
			return null;
		}

		@Override
		public int getLoaderCallbackId() {
			return 0;
		}

		@Override
		public AbstractLoaderCallback createLoaderCallback() {
			return new SongLoaderCallback();
		}
		
	}
	
	public class SongLoaderCallback extends AbstractLoaderCallback{

		@Override
		public CursorLoader createCursorLoader() {
			return null;
		}

		@Override
		public void onDataChanged(Cursor cursor) {
			
		}

		@Override
		public void onNoData() {
			
		}

		@Override
		public void onDataReset() {
			
		}
		
	}
	
}