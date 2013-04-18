package com.example.ipcplayer.application;

import com.example.ipcplayer.controller.LocalMusicController;

import android.app.Application;
import android.content.Context;

public class IPCApplication extends Application{
	private static IPCApplication mIPCApplication;
	private static Context mContext;
	
	public IPCApplication(){
		mIPCApplication = this ;
	}
	
	public static IPCApplication getInstance(){
		return mIPCApplication ;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		LocalMusicController.getInstance(mContext).checkSDCardFolder();
	}

	public Context getApplicationContext() {
		return mContext;
	}
	
	
}