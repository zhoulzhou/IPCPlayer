package com.example.ipcplayer.controller;

import com.example.ipcplayer.activity.PlayingActivity;
import com.example.ipcplayer.convert.ConvertToMusicFile;
import com.example.ipcplayer.manager.LocalMusicManager;
import com.example.ipcplayer.service.LocalPlayer;
import com.example.ipcplayer.service.PlaybackService;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.MusicFile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LocalMusicController{
	private static final String TAG = LocalMusicController.class.getSimpleName();
	private Context mContext;
	private static LocalMusicController mInstance;
	private LocalMusicManager mLocalMusicManager;
	private LocalPlayer mLocalPlayer;
	private PlaybackService mService;
	
	private LocalMusicController(Context context){
		mContext = context;
		mLocalMusicManager = new LocalMusicManager(context);
		mLocalPlayer = new LocalPlayer(context);
		mService = new PlaybackService();
	}
	
	public static LocalMusicController getInstance(Context context){
			if (mInstance == null) {
				mInstance = new LocalMusicController(context);
			}
		return mInstance;
	}
	
	public void playMusic(long id){
		LogUtil.d(TAG + " playMusic id: " + id);
		MusicFile musicFile = ConvertToMusicFile.getInstance(mContext).idToMusicFile(id);
		LogUtil.d(TAG + " playMusic musicFile: " + musicFile.toString());
		String path = mLocalMusicManager.getPlayPath(musicFile);
		LogUtil.d(TAG + " playMusic path: " + path);
		mLocalPlayer.setDataSource(path);
		mService.start();
		Intent intent = new Intent(mContext,PlayingActivity.class);
		intent.putExtra("path", path);
		mContext.startActivity(intent);
	}
}