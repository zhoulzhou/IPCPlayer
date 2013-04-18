package com.example.ipcplayer.download;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
	private static final String DOWNLOADIDLE = "downloadidle";
	private static final String DOWNLOADING = "downloading";
	private static final String DOWNLOADERROR = "downloaderror";
	private static final String DOWNLOADFINISH = "downloadfinish";
	private static final String DOWNLOADPAUSE = "downloadpasue";
	private static final String DOWNLOADCANCEL = "downloadcancel";
	private String mDownloadState = DOWNLOADIDLE;
	private DownloadListener mDownloadListener;
	private static final int STATUS_HTTP_FORBIDDEN = 12;
	private static final int STATUS_HTTP_EXCEPTION = 13;
	private static final int STATUS_HTTP_UNAVLIABLE = 14;
	private File mDownloadFile ;
	private File mTempFile;
	private String mFileName;
	private long mPreviousFileSize;
	private String mFilePath ;
	
	public DownloadRunnable(Context context, String url ,String filePath){
		LogUtil.d(TAG + " init object ");
		mContext = context;
		mUrl = url;
		mDownloadPath = FileUtil.getIPCDownloadDir().getAbsolutePath();
		mFileName = getFileName();
		mFilePath = mDownloadPath + File.separator + mFileName;
		mDownloadFile = new File(mDownloadPath,mFileName);
		mTempFile = new File(mDownloadPath,mFileName + DownloadConfig.TEMP_TYPE);
		mDownloadInfo = new DownloadInfo();
		mDownloadInfo.setmUrl(mUrl);
		mDownloadInfo.setmFilePath(mDownloadPath);
		
	}
	
	private String getFileName() {
		// TODO Auto-generated method stub
		return "try.mp3";
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
			createDownloadFile();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.d(TAG + " create file fail ");
			return ;
		}

		if(NetworkUtil.isNetworkAvailable()){
			if(NetworkUtil.isMobileAvailble()){
				//pop the notification
				LogUtil.d(TAG + " mobile network is connected ");
			}
			LogUtil.d(TAG + " wifi is connected ");
			initFileAndSize();
			startDownload(mUrl,mDownloadPath);
		}else{
			//handle this error
			mDownloadInfo.setmDownloadState(DOWNLOADERROR);
			mDownloadInfo.setmErrorCode(STATUS_HTTP_UNAVLIABLE);
			handleError();
			LogUtil.d(TAG + " network is disconnected ");
			return ;
		}
		
	}
	
	

	private void createDownloadFile() {
		LogUtil.d(TAG + " createDownloadFile ");
		String path = FileUtil.getIPCDownloadDir().getAbsolutePath();
		LogUtil.d(TAG + " create dir path = " + path);
		File dir = new File(path.trim());
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				LogUtil.d(TAG + " create dir successfully ");
			} else {
				LogUtil.d(TAG + " create dir failed ");
			}
		}
		
		if (!mTempFile.exists()) {
			try {
				if (mTempFile.createNewFile()) {
					LogUtil.d(TAG + " create file successfully ");
				} else {
					LogUtil.d(TAG + " create file failed ");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogUtil.d(TAG + " create file failed exception = ");
				e.printStackTrace();
			}
		}
	}

	public DownloadInfo getDownloadInfo(){
		LogUtil.d(TAG + " getDownloadInfo ");
		return mDownloadInfo;
	}
	
	private void initFileAndSize() {
		LogUtil.d(TAG + " initFileAndSize ");
		mDownloadSize = mTempFile.length();
	}

	private void startDownload(String url,String downloadPath){
		LogUtil.d(TAG + " startDownload ");
		HttpResponse response ;
		FileOutputStream out = null ;
		InputStream in = null;
		DefaultHttpClient client ;
		HttpGet request ;
		HttpEntity reEntity;
		
		try{
			request = new HttpGet(url);
			client = HttpApi.getDefaultHttpClientSimple();
			response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if(HttpStatus.SC_OK != statusCode && HttpStatus.SC_PARTIAL_CONTENT != statusCode){
				mDownloadInfo.setmErrorCode(statusCode);
				handleHttpError(statusCode);
				return ;
			}
			reEntity = response.getEntity();
			long len = reEntity.getContentLength();
			mTotalSize = len ;
//			mTotalSize = mDownloadSize + len ;

			in = reEntity.getContent();
			out = new FileOutputStream(downloadPath+File.separator + mFileName + DownloadConfig.TEMP_TYPE ,true);
			if (mDownloadFile.exists()) {
				if (getDownloadFileSize() >= mTotalSize) {
					mDownloadInfo.setmDownloadState(DOWNLOADFINISH);
					handleFinish();
					LogUtil.d(TAG + " file already exists then return ; ");
					return;
				}
			}else if(mTempFile.exists()) {
				request.setHeader("Range" , "bytes="+mTempFile.length()+"-");
				mPreviousFileSize = mTempFile.length();
				
				client = HttpApi.getDefaultHttpClientSimple();
				response = client.execute(request);
				reEntity = response.getEntity();
				in = reEntity.getContent();
			}
			
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
						LogUtil.d(TAG + " mTempFile size = " + FileUtil.getFileSize(mTempFile));
						mTempFile.renameTo(mDownloadFile);
						mDownloadInfo.setmDownloadState(DOWNLOADFINISH);
						handleFinish();
					}else {
						//handle cancel and error
					}
					break ;
				}
			}
			if(isCanceled()){
				//update status 
				mDownloadInfo.setmDownloadState(DOWNLOADCANCEL);
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
	
	private long getDownloadFileSize(){
		long size = 0;
		try{
			size = FileUtil.getFileSize(mDownloadFile);
		}catch(Exception e){
			LogUtil.d(TAG + " getDownloadFileSize error ");
			e.printStackTrace();
		}
		return size;
	}
	
	private void handleError() {
		// TODO Auto-generated method stub
		mDownloadListener.errorDownload(mDownloadInfo);
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
		return DOWNLOADCANCEL.equals(mDownloadInfo.getmDownloadState());
	}
	
	public void cancelDownload(){
		mDownloadInfo.setmDownloadState(DOWNLOADCANCEL);
	}
	
	public void resumeDownload(){
		mDownloadInfo.setmDownloadState(DOWNLOADING);
	}

	public boolean isDownloading(){
		return DOWNLOADING.equals(mDownloadInfo.getmDownloadState());
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