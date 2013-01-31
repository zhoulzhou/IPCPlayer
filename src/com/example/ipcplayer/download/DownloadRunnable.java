package com.example.ipcplayer.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.ipcplayer.http.HttpApi;
import com.example.ipcplayer.utils.FileUtil;
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
	private DownloadInfo mDownloadInfo ;
	private static final int DOWNLOADING = 1;
	private static final int DOWNLOADERROR = 2;
	private static final int DWONLOADFINISHI = 3;
	private static final int DOWNLOADPAUSE = 4;
	private static final int DWONLOADCANCEL = 5;
	private int mDownloadState ;
	private DownloadListener mDownloadListener;
	private static final int STATUS_HTTP_FORBIDDEN = 10;
	private static final int STATUS_HTTP_EXCEPTION = 11;
	private File mDownloadFile ;
	
	public DownloadRunnable(Context context, String url ,String filePath){
		LogUtil.d(TAG + " init object ");
		mContext = context;
		mUrl = url;
		mDownloadPath = filePath;
		mDownloadFile = new File(mDownloadPath);
		mDownloadInfo = new DownloadInfo();
		mDownloadInfo.setmUrl(mUrl);
		mDownloadInfo.setmFilePath(mDownloadPath);
		
	}
	
	@Override
	public void run() {
		LogUtil.d(TAG + " run ");
		// TODO Auto-generated method stub
		if(!StorageUtil.isExternalStorageAvailable()){
			//do not show toast inside thread ,it'll crash because the looper is null 
//			ToastUtil.showShortToast(mContext, " SD Card is disable! ");
			mDownloadInfo.setmDownloadState(DOWNLOADERROR);
			LogUtil.d(TAG + " sdcard is disabled");
			return ;
		}

		try {
			createDownloadFile(mDownloadPath);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.d(TAG + " create file fail ");
		}

		if(NetworkUtil.isNetworkAvailable()){
			if(NetworkUtil.isMobileAvailble()){
				//pop the notification
				LogUtil.d(TAG + " mobile network is connected ");
			}
			LogUtil.d(TAG + " wifi is connected ");
			initFileAndSize(mDownloadPath);
			startDownload(mUrl,mDownloadPath);
		}else{
			//handle this error
			mDownloadInfo.setmDownloadState(DOWNLOADERROR);
			LogUtil.d(TAG + " network is disconnected ");
			return ;
		}
		
	}
	
	private void createDownloadFile(String fileName) {
		File file = new File(fileName);
		try {
			String path = FileUtil.getIPCDownloadDir();
			File dir = new File(path);
			if (!dir.exists()) {
				try {
					if(dir.mkdir()){
						LogUtil.d(TAG + " create dir successfully ");
					}else{
						LogUtil.d(TAG + " create dir failed ");
					}
				} catch (Exception e) {
					LogUtil.d(TAG + " create dir fail excepiton = ");
					e.printStackTrace();
				}
			}
			if(file.createNewFile()){
				LogUtil.d(TAG + " create file successfully ");
			}else {
				LogUtil.d(TAG + " create file failed ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.d(TAG + " create file fail excepiton = ");
			e.printStackTrace();
		}
	}

	public DownloadInfo getDownloadInfo(){
		LogUtil.d(TAG + " getDownloadInfo ");
		return mDownloadInfo;
	}
	
	private void setDownloadInfo(){
		LogUtil.d(TAG + " setDownloadInfo ");
		mDownloadInfo.setmDownloadSize(mDownloadSize);
		mDownloadInfo.setmTotalSize(mTotalSize);
		mDownloadInfo.setmFilePath(mDownloadPath);
		mDownloadInfo.setmDownloadState(mDownloadState);
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
				mDownloadInfo.setmErrorCode(statusCode);
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
				mDownloadInfo.setmDownloadState(DOWNLOADING);
				size = in.read(buf);
				if(size > 0){
					out.write(buf, 0, size);
					mDownloadSize += size;
					mDownloadInfo.setmTotalSize(mTotalSize);
					mDownloadInfo.setmDownloadSize(mDownloadSize);
					updateProgress();
					updateFlowManagerment(size);
				}else {
					in.close();
					out.close();
					if(mDownloadSize == mTotalSize){
						//update state to successful status
						mDownloadInfo.setmDownloadState(DWONLOADFINISHI);
						handleFinish();
					}else {
						//handle cancel and error
					}
					break ;
				}
			}
			if(isCanceled()){
				//update status 
				mDownloadInfo.setmDownloadState(DWONLOADCANCEL);
			}
		}catch(Exception e){
			e.printStackTrace();
			//handle error 
			mDownloadInfo.setmDownloadState(DOWNLOADERROR);
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
	
	private void handleFinish() {
		mDownloadListener.finishDownload(mDownloadInfo);
	}

	private void updateFlowManagerment(int size) {
		LogUtil.d(TAG + " updateFlowManagerment ");
		
	}
	
	public void setDownloadListener(DownloadListener listener){
		mDownloadListener = listener ;
	}

	private void updateProgress() {
		LogUtil.d(TAG + " updateProgress ");
		mDownloadListener.updateProgress(mDownloadInfo);
	}

	private boolean isCanceled() {
		LogUtil.d(TAG + " isCanceled ");
		return false;
	}

	private void handleHttpError(int statusCode) {
		LogUtil.d(TAG + " handleHttpError ");
		if(HttpStatus.SC_FORBIDDEN == statusCode){
			mDownloadInfo.setmErrorCode(STATUS_HTTP_FORBIDDEN);
			mDownloadListener.errorDownload(mDownloadInfo);
		}else {
			mDownloadInfo.setmErrorCode(STATUS_HTTP_EXCEPTION);
			mDownloadListener.errorDownload(mDownloadInfo);
		}
	}

	private HttpGet getRequest(String url) {
		LogUtil.d(TAG + " getRequest ");
		HttpGet httpGet = new HttpGet();
		
		return httpGet;
	}
}