package com.example.ipcplayer.manager;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;

public class LocalMusicPlayManager{
	private static String TAG = LocalMusicPlayManager.class.getSimpleName();
	private Context mContext ;
	
	public void LocalMusicPlayManager(Context context){
		LogUtil.d(TAG + " init this object");
		mContext = context ;
	}
}