package com.example.ipcplayer.homeview;

import com.example.ipcplayer.R;
import com.example.ipcplayer.activity.MainActivity;
import com.example.ipcplayer.controller.UICallBackController;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicHomeCenterView extends BaseHomeView{
	private static final String TAG = MusicHomeCenterView.class.getSimpleName();
	private ViewGroup mHomeCenterUp;
	private ViewGroup mHomeCenterDown;
	
	private MusicHomeCenterItemView mLocalMusicView;
	private MusicHomeCenterItemView mCloudMusicView;
	private MusicHomeCenterItemView mDownloadMusicView;
	private MusicHomeCenterItemView mOtherMusicView;
	
	private MainActivity mMainActivity;

	public MusicHomeCenterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public MusicHomeCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MusicHomeCenterView(Context context) {
		super(context);
	}

	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		mMainActivity = (MainActivity) context;
	    LayoutInflater inflate = LayoutInflater.from(context);	
	    inflate.inflate(R.layout.music_home_center_view, this);
	    
		mHomeCenterUp = (ViewGroup) findViewById(R.id.home_center_up);
		mHomeCenterDown = (ViewGroup) findViewById(R.id.home_center_down);
		
		initLocalMusicView();
		initCloudMusicView();
		initDownloadMusicView();
		initOtherMusicView();
	}

	private void initLocalMusicView() {
		mLocalMusicView = (MusicHomeCenterItemView) findViewById(R.id.home_center_local_music);
		if(mLocalMusicView == null){
			LogUtil.d(TAG + " mLocalMusicView is null");
		}
		
		mLocalMusicView.setIcon(R.drawable.ic_launcher);
		mLocalMusicView.setTitle("本地音乐");
		mLocalMusicView.setInfo("暂空");
		mLocalMusicView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				onAllSongListClick();
			}
			
		});
	}

	private void initDownloadMusicView() {
		mDownloadMusicView = (MusicHomeCenterItemView) findViewById(R.id.home_center_download_music);
		mDownloadMusicView.setIcon(R.drawable.ic_launcher);
		mDownloadMusicView.setTitle("下载管理");
		mDownloadMusicView.setInfo("暂白");
		mDownloadMusicView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}
			
		});
	}

	private void initCloudMusicView() {
		mCloudMusicView = (MusicHomeCenterItemView) findViewById(R.id.home_center_cloud_music);
		mCloudMusicView.setIcon(R.drawable.ic_launcher);
		mCloudMusicView.setTitle("我的云收藏	");
		mCloudMusicView.setInfo("暂无");
		mCloudMusicView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}
			
		});
	}

	private void initOtherMusicView() {
		mOtherMusicView = (MusicHomeCenterItemView) findViewById(R.id.home_center_other_music);
		mOtherMusicView.setIcon(R.drawable.ic_launcher);
		mOtherMusicView.setTitle("未定");
		mOtherMusicView.setInfo("空");
		mOtherMusicView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}
			
		});
	}

	private void onAllSongListClick(){
		LogUtil.d(TAG + " onAllSongListClick() ");
		UICallBackController.showAllSongListFragment(mMainActivity);
	}
	
	
	@Override
	protected void onRelease() {
		
	}
	
}