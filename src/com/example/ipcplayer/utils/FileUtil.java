package com.example.ipcplayer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class FileUtil{
	public static String DIR_HOME = "IPC";
	public static String DIR_MUSIC = "music";
	public static String DIR_DOWNLOAD = "download";
	public static String DIR_LYRIC = "lyric";
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
	
	public static File getIPCLyricDir(){
		LogUtil.d(TAG + " getIPCLyricDir ");
		return new File(getIPCHomeDir(),DIR_LYRIC);
	}
	
	@SuppressWarnings("resource")
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
	
	public static ArrayList<File> getAllDirectory(){
		ArrayList<File> dirList = new ArrayList<File>();
		dirList.add(getIPCHomeDir());
		dirList.add(getIPCMusicDir());
		dirList.add(getIPCDownloadDir());
		dirList.add(getIPCLyricDir());
		return dirList;
	}
	
	public static boolean isDirectory(File file){
		return file.exists() && file.isDirectory();
	}
	
	public static boolean createDiretory(File file){
		if(file.exists() && file.isDirectory()){
			return false;
		}else{
			return file.mkdirs();
		}
		
	}
	
	public static String readSDFile(String fileName) throws Exception{
		if(fileName == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while (br.readLine() != null) {
			line = br.readLine();
			sb.append(line);
			sb.append("\n");
		}
		br.close();
		isr.close();
		fis.close();

		return sb.toString();

	}
	
	
}