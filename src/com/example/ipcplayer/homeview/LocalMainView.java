package com.example.ipcplayer.homeview;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.adapter.GridAdapter;
import com.example.ipcplayer.localfragment.ItemData;
import com.example.ipcplayer.manager.LocalMusicManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

public class LocalMainView extends BaseHomeView{
	private GridView mLocalGrid;
	private View mBackupView;
	
	private LocalMusicManager mLocalMusicManager;
	private ArrayList<ItemData> mItemDatas;
	private GridAdapter mAdapter;

	public LocalMainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public LocalMainView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public LocalMainView(Context context){
		super(context);
	}

	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		View view =LayoutInflater.from(context).inflate(R.layout.local_music_grid,this);
		mLocalGrid = (GridView) view.findViewById(R.id.localmusicgird);
		mBackupView = (View) view.findViewById(R.id.backup);
		
		mLocalMusicManager = new LocalMusicManager(context);
		mItemDatas = mLocalMusicManager.getLocalMusicItems();
		mAdapter = new GridAdapter(context);
		mLocalGrid.setAdapter(mAdapter);
	}

	@Override
	protected void onRelease() {
		
	}
	
}