package com.example.ipcplayer.homeview;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.activity.MainActivity;
import com.example.ipcplayer.adapter.GridAdapter;
import com.example.ipcplayer.controller.IUICallBack;
import com.example.ipcplayer.controller.UICallBackController;
import com.example.ipcplayer.localfragment.ItemData;
import com.example.ipcplayer.manager.LocalMusicManager;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class LocalMainView extends BaseHomeView{
	private static final String TAG = LocalMainView.class.getSimpleName();
	private GridView mLocalGrid;
	private View mBackupView;
	
	private LocalMusicManager mLocalMusicManager;
	private ArrayList<ItemData> mItemDatas;
	private GridAdapter mAdapter;
	private MainActivity mMainActivity;

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
		mMainActivity = (MainActivity) context;
		View view =LayoutInflater.from(context).inflate(R.layout.local_music_grid,this);
		mLocalGrid = (GridView) view.findViewById(R.id.localmusicgird);
		mBackupView = (View) view.findViewById(R.id.backup);
		mBackupView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				LogUtil.d(TAG + " click mBackupView " );
			}
			
		});
		
		mLocalMusicManager = new LocalMusicManager(context);
		mItemDatas = mLocalMusicManager.getLocalMusicItems();
		mAdapter = new GridAdapter(context);
		mLocalGrid.setClickable(true);
		mLocalGrid.setFocusable(true);
		mLocalGrid.setAdapter(mAdapter);
		mLocalGrid.setOnItemClickListener(mGridItemClickListener);
		LogUtil.d(TAG + " setOnItemClickListener " );
	}

	@Override
	protected void onRelease() {
		
	}
	
	// do this or don't ?
	public void setCallBack(IUICallBack callback){
		
	}
	
	private final AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			LogUtil.d(TAG + " onItemClick ");
			if(mItemDatas == null){
				LogUtil.d(TAG + " mItemDatas in null ");
			}
			
			if(mItemDatas == null){
				LogUtil.d(TAG + " mItemData or mOnItemClickListener is null");
				return ;
			}
			LogUtil.d(TAG + " positon = " + position);
			
			ItemData itemData = mItemDatas.get(position);
			LogUtil.d(TAG + " itemData = "+ itemData.toString() + " itemData.mType" + itemData.mType);
			
			switch(itemData.mType){
			case ItemData.DATATYPE_ALLSONG_LIST :
				LogUtil.d(TAG + " ItemData.DATATYPE_ALLSONG_LIST ");
				onAllSongListClick();
				break ;
			default:
				
				break ;
			}
		}
	};
	
	private void onAllSongListClick(){
		LogUtil.d(TAG + " onAllSongListClick() ");
		UICallBackController.showAllSongListFragment(mMainActivity);
	}
}