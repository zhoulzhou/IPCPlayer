package com.example.ipcplayer.utils;

import java.io.File;
import java.io.FileInputStream;

import android.os.Environment;

public class FileUtil{
	public static String DIR_HOME = "IPC";
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
	
	public static String getIPCHomeDir(){
		LogUtil.d(TAG + " getIPCHomeDir ");
		return new File(getStoragePath(),DIR_HOME).getAbsolutePath();
	}
	
	public static String getIPCMusicDir(){
		LogUtil.d(TAG + " getIPCMusicDir ");
		return new File(getIPCHomeDir(),DIR_MUSIC).getAbsolutePath();
	}
	
	public static String getIPCDownloadDir(){
		LogUtil.d(TAG + " getIPCDownloadDir ");
		return new File(getIPCHomeDir(),DIR_DOWNLOAD).getAbsolutePath();
	}
	
	public static long getFileSize(File f) throws Exception{
		long s = 0;
		 if (f.exists()) {
	            FileInputStream fis = null;
	            fis = new FileInputStream(f);
	           s= fis.available();
	           LogUtil.d(TAG + " file size = " + s);
	        } else {
	            LogUtil.d(TAG + " file didn't exist	");
	        }
	        return s;
	}
}