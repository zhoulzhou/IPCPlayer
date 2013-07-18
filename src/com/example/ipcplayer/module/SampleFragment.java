package com.example.ipcplayer.module;

import com.example.ipcplayer.R;
import com.example.ipcplayer.adapter.AllSongListAdapter;
import com.example.ipcplayer.controller.LocalMusicController;
import com.example.ipcplayer.eventbus.MusicEvent;
import com.example.ipcplayer.manager.LocalMusicManager;
import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.utils.LogUtil;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SampleFragment extends ListViewBaseFragment{
	private Cursor mCursor;
	private static int ALLSONG = 1;
	CursorAdapter mAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mFactory = new SongListViewFactory();//初始化 要实现的工厂
		LogUtil.d("init factory ");
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
//		View view = inflater.inflate(R.layout.list_sample, null);
//		mListView =  (ListView) view.findViewById(R.id.list);
		LogUtil.d("onCreateView");
		return mContentView;
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
	
	public void onEvent(MusicEvent event){
		long id = event.getId();
		LogUtil.d("get Event to play music");
		LocalMusicController.getInstance(mContext).playMusic(id,null);
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
			LogUtil.d("createAdapter");
			mAdapter = new AllSongListAdapter(mContext,null,R.layout.all_song_list_item);
			return mAdapter;
		}

		@Override
		public int getLoaderCallbackId() {
			return ALLSONG;
		}

		@Override
		public AbstractLoaderCallback createLoaderCallback() {
			return new SongLoaderCallback();
		}
		
	}
	
	public class SongLoaderCallback extends AbstractLoaderCallback{

		@Override
		public CursorLoader createCursorLoader() {
			String where = MusicDB.MusicInfoColumns._ID + "> 0 ";
			LogUtil.d(" where = " + where);
			String[] projection = new String[] {
				    MusicDB.MusicInfoColumns._ID,
					MusicDB.MusicInfoColumns.MUSICNAME,
					MusicDB.MusicInfoColumns.ARTIST
			};
			CursorLoader cursorLoader = new CursorLoader(mContext, MusicDB.MusicInfoColumns.getContentUri(), projection, where, null, null);
			LogUtil.d("get cursorLoader ");
			return cursorLoader;
		}

		@Override
		public void onDataChanged(Cursor cursor) {
			if(mAdapter != null){
				mAdapter.swapCursor(cursor);
				LogUtil.d("data changed and set cursor");
			}else{
				LogUtil.d("mAdapter is null ");
			}
		}

		@Override
		public void onNoData() {
			if(mAdapter != null){
				mAdapter.swapCursor(null);
			}
		}

		@Override
		public void onDataReset() {
			
		}
		
	}
	
}