package com.example.ipcplayer.controller;

import android.content.Context;

public class LogController{
	private Context mContext;
	private static LogController mInstance;
	
	public LogController(Context context){
		mContext = context;
	}
	
	public static LogController getInstance(Context context){
		if(mInstance == null){
			mInstance = new LogController(context);
		}
		
		return mInstance;
	}
}