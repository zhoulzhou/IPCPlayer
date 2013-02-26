package com.example.ipcplayer.utils;

import com.example.ipcplayer.setting.Config;

import android.util.Log;

public class LogUtil{
	private static  boolean DEBUG = Config.DEBUG_MODE;
	private static final String DEFAULT_TAG = "[IPC's ready for coming]";
	
	public static void d(String str){
		if(DEBUG){
			Log.d(DEFAULT_TAG, str);
		}
	}
	
	public static void d(String tag ,String str){
		if(DEBUG){
			Log.d(DEFAULT_TAG, tag + " " +str);
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
	
	public static void i(String tag,String str){
		if(DEBUG){
			Log.i(DEFAULT_TAG, tag + " " + str);
		}
	}
}