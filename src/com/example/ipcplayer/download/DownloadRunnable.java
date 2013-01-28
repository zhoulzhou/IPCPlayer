package com.example.ipcplayer.download;

import com.example.ipcplayer.utils.StorageUtil;

import android.content.Context;

public class DownloadRunnable implements Runnable{
	private String mUrl ;
	private Context mContext;
	private String mDownloadPath;
	
	public DownloadRunnable(Context context, String url ,String filepath){
		
	}

	public DownloadRunnable(Context context, String url){
		mContext = context;
		mUrl = url;
	}
	
	public DownloadRunnable(){
		mUrl = DownloadConfig.sUrls[0];
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(!StorageUtil.isExternalStorageAvailable()){
			showToast();
		}
		startDownload();
	}
	
	
	private void startDownload(){
		
	}
}