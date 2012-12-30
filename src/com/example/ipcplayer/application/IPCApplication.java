package com.example.ipcplayer.application;

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
		// TODO Auto-generated method stub
		super.onCreate();
		
		mContext = this;
	}

	@Override
	public  Context getApplicationContext() {
		// TODO Auto-generated method stub
		return mContext;
	}
	
	
}