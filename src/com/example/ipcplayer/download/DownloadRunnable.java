package com.example.ipcplayer.download;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.ipcplayer.http.HttpApi;
import com.example.ipcplayer.utils.NetworkUtil;
import com.example.ipcplayer.utils.StorageUtil;
import com.example.ipcplayer.utils.ToastUtil;

import android.content.Context;

public class DownloadRunnable implements Runnable{
	private String mUrl ;
	private Context mContext;
	private String mDownloadPath;
	private String mFileName ;
	private long mTotalSize = 0;
	private long mDownloadSize = 0;
	private static final int KB = 1024;
	private static final int BUFFER_SIZE = 10* KB;
	
	public DownloadRunnable(Context context, String url ,String filepath){
		
	}

	public DownloadRunnable(Context context, String url){
		mContext = context;
		mUrl = url;
	}
	
	public DownloadRunnable(Context context){
		mContext = context ;
		mUrl = DownloadConfig.sUrls[0];
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(!StorageUtil.isExternalStorageAvailable()){
			ToastUtil.showShortToast(mContext, " SD Card is disable! ");
		}
		
		if(NetworkUtil.isNetworkAvailable()){
			if(NetworkUtil.isMobileAvailble()){
				ToastUtil.showShortToast(mContext, " connecting mobile network ");
			}
			startDownload(mUrl,mFileName);
		}else{
			ToastUtil.showShortToast(mContext, " network is not connected! ");
		}
		
	}
	
	
	private void startDownload(String url,String fileName){
		HttpResponse response ;
		FileOutputStream out = null ;
		InputStream in = null;
		
		try{
			HttpGet request = new HttpGet(url);
			DefaultHttpClient client = HttpApi.getDefaultHttpClientSimple();
			response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if(HttpStatus.SC_OK != statusCode && HttpStatus.SC_PARTIAL_CONTENT != statusCode){
				handleHttpError(statusCode);
				return ;
			}
			HttpEntity reEntity = response.getEntity();
			long len = reEntity.getContentLength();
			mTotalSize = mDownloadSize + len ;
			
			in = reEntity.getContent();
			out = new FileOutputStream(fileName,true);
			
			byte[] buf = new byte[BUFFER_SIZE];
			int size ;
			while(!isCanceled()){
				size = in.read(buf);
				if(size > 0){
					out.write(buf, 0, size);
					mDownloadSize += size;
					updateProgress();
					updateFlowManagerment(size);
				}else {
					in.close();
					out.close();
					if(mDownloadSize == mTotalSize){
						//update state to successful status
					}else {
						//handle cancel and error
					}
					break ;
				}
			}
			if(isCanceled()){
				//update status 
			}
		}catch(Exception e){
			e.printStackTrace();
			//handle error 
		}finally{
			try{
				if(in != null){
					in.close();
				}
				
				if(out != null){
					out.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void updateFlowManagerment(int size) {
		
	}

	private void updateProgress() {
		
	}

	private boolean isCanceled() {
		return false;
	}

	private void handleHttpError(int statusCode) {
		if(HttpStatus.SC_FORBIDDEN == statusCode){
			
		}else {
			
		}
	}

	private HttpGet getRequest(String url) {
		HttpGet httpGet = new HttpGet();
		
		return httpGet;
	}
}