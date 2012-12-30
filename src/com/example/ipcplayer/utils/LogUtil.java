package com.example.ipcplayer.utils;

import android.util.Log;

public class LogUtil{
	private static  boolean DEBUG = Config.DEBUG_MODE;
	private static final String DEFAULT_TAG = "[IPC'ready for coming]";
	
	public static void d(String str){
		if(DEBUG){
			Log.d(DEFAULT_TAG, str);
		}
	}
	
	public static void e(String str){
		if(DEBUG){
			Log.e(DEFAULT_TAG, str);
		}
	}
	
	public static void v(String str){
		if(DEBUG){
			Log.v(DEFAULT_TAG, str);
		}
	}
}