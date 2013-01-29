package com.example.ipcplayer.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.ipcplayer.http.HttpApi;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.NetworkUtil;
import com.example.ipcplayer.utils.StorageUtil;
import com.example.ipcplayer.utils.ToastUtil;

import android.content.Context;

public class DownloadRunnable implements Runnable{
	private static String TAG = DownloadRunnable.class.getSimpleName();
	private String mUrl ;
	private Context mContext;
	private String mDownloadPath;
	private long mTotalSize = 0;
	private long mDownloadSize = 0;
	private static final int KB = 1024;
	private static final int BUFFER_SIZE = 10* KB;
	
	public DownloadRunnable(Context context, String url ,String filePath){
		LogUtil.d(TAG + " init object ");
		mContext = context;
		mUrl = url;
		mDownloadPath = filePath;
	}
	
	@Override
	public void run() {
		LogUtil.d(TAG + " run ");
		// TODO Auto-generated method stub
		if(!StorageUtil.isExternalStorageAvailable()){
			ToastUtil.showShortToast(mContext, " SD Card is disable! ");
		}
		
		if(NetworkUtil.isNetworkAvailable()){
			if(NetworkUtil.isMobileAvailble()){
				ToastUtil.showShortToast(mContext, " connecting mobile network ");
			}
			initFileAndSize(mDownloadPath);
			startDownload(mUrl,mDownloadPath);
		}else{
			ToastUtil.showShortToast(mContext, " network is not connected! ");
		}
		
	}
	
	private void initFileAndSize(String filepath) {
		LogUtil.d(TAG + " initFileAndSize ");
		mDownloadSize = new File(filepath).length();
	}

	private void startDownload(String url,String downloadPath){
		LogUtil.d(TAG + " startDownload ");
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
			out = new FileOutputStream(downloadPath,true);
			
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
		LogUtil.d(TAG + " updateFlowManagerment ");
		
	}

	private void updateProgress() {
		LogUtil.d(TAG + " updateProgress ");
		
	}

	private boolean isCanceled() {
		LogUtil.d(TAG + " isCanceled ");
		return false;
	}

	private void handleHttpError(int statusCode) {
		LogUtil.d(TAG + " handleHttpError ");
		if(HttpStatus.SC_FORBIDDEN == statusCode){
			
		}else {
			
		}
	}

	private HttpGet getRequest(String url) {
		LogUtil.d(TAG + " getRequest ");
		HttpGet httpGet = new HttpGet();
		
		return httpGet;
	}
}