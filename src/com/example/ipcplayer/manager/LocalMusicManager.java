package com.example.ipcplayer.manager;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;

public class LocalMusicManager{
	private Context mContext;
	private static String TAG = LocalMusicManager.class.getSimpleName();
	
	public void LocalMusicManager(Context context){
		LogUtil.d(TAG + " init object ");
		mContext = context;
	}
}