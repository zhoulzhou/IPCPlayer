package com.example.ipcplayer.manager;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;

public class LocalMusicDisplayManager{
	private static String TAG = LocalMusicDisplayManager.class.getSimpleName();
	private Context mContext ;
	
	public void LocalMusicDisplayManager(Context context ){
		LogUtil.d(TAG + " init this object");
		mContext = context;
		
	}
}