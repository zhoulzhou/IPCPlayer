package com.example.ipcplayer.utils;

import java.io.File;

import android.os.Environment;

public class FileUtil{
	public static String DIR_HOME = "Ipc_ready";
	public static String DIR_MUSIC = "music";
	public static String DIR_DOWNLOAD = "download";
	private static String TAG = FileUtil.class.getSimpleName();
	
	public static String getStoragePath(){
		LogUtil.d(TAG + " getStoragePath ");
		if(StorageUtil.isExternalStorageAvailable()){
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}else {
			return Environment.getDataDirectory().getAbsolutePath();
		}
	}
	
	public static File getIPCHomeDir(){
		LogUtil.d(TAG + " getIPCHomeDir ");
		return new File(getStoragePath(),DIR_HOME);
	}
	
	public static File getIPCMusicDir(){
		LogUtil.d(TAG + " getIPCMusicDir ");
		return new File(getIPCHomeDir(),DIR_MUSIC);
	}
	
	public static File getIPCDownloadDir(){
		LogUtil.d(TAG + " getIPCDownloadDir ");
		return new File(getIPCHomeDir(),DIR_DOWNLOAD);
	}
	
	
}