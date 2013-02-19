package com.example.ipcplayer.activity;

import java.io.File;
import java.net.URL;

import com.example.ipcplayer.R;
import com.example.ipcplayer.download.DownloadConfig;
import com.example.ipcplayer.download.DownloadInfo;
import com.example.ipcplayer.download.DownloadListener;
import com.example.ipcplayer.download.DownloadRunnable;
import com.example.ipcplayer.thread.ThreadF;
import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class DownloadActivity extends Activity implements DownloadListener{
    private static String TAG = DownloadActivity.class.getSimpleName();
	TextView mFileNameTv;
	TextView mUrlTv;
	TextView mStatusTv;
	ProgressBar progressBar;
	Button mPauseBtn;
	Button mCancelBtn;
	TextView mTotalSizeTv;
	TextView mDownloadSizeTv;
	DownloadRunnable mDownloadRunnable ;
	String mUrl ;
	String mDownloadFile ;
	private long mDownloadSize;
	private long mTotalSize;
	private String mStatus ;
	private static final int REFRESH = 0;
	private static final int FINISH = 1;
	private static final int ERROR = 11;
	private static final int ERROR_HTTP_FORBID = 12;
	private static final int ERROR_HTTP_EXCEPTION = 13;
	private static final int ERROR_HTTP_UNAVAILABLE = 14;
	private Thread mDownloadThread;
	private DownloadInfo mDownloadInfo;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			LogUtil.d(TAG + " handleMessage ");
			if(mDownloadInfo == null){
				mDownloadInfo = new DownloadInfo();
			}
			mDownloadInfo = (DownloadInfo) msg.obj;
			mDownloadSize = mDownloadInfo.getmDownloadSize();
			LogUtil.d(TAG + " mDownloadSize = " + mDownloadSize);
			mTotalSize = mDownloadInfo.getmTotalSize();
			LogUtil.d(TAG + " mTotalSize = " + mTotalSize);
			mStatus = mDownloadInfo.getmDownloadState();
			LogUtil.d(TAG + " mStatus = " + mStatus);
			
			switch (msg.what) {
			case REFRESH:
				LogUtil.d(TAG + " REFRESH ");
				progressBar.setProgress((int) (mDownloadSize*100 / mTotalSize));
				mStatusTv.setText(" status : " +mStatus);
				mTotalSizeTv.setText(mTotalSize + "");
				mDownloadSizeTv.setText(mDownloadSize + "");
				break;
			case FINISH:
				mStatusTv.setText(" status : " +mStatus);
				break ;
			case ERROR:
				int errorCode = mDownloadInfo.getmErrorCode();
				if(ERROR_HTTP_FORBID == errorCode){
					mStatusTv.setText(" status: http forbided ");
				}else if(ERROR_HTTP_EXCEPTION == errorCode){
					mStatusTv.setText(" status: http exception 	");
				}else if(ERROR_HTTP_UNAVAILABLE == errorCode){
					mStatusTv.setText(" stattus: network unavailable ");
				}
			default:
				break;
			}
		}
		
	};
	private String mDownloadPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG + " onCreate ");
		setContentView(R.layout.download);
		
		mFileNameTv = (TextView) findViewById(R.id.filename);
		mUrlTv = (TextView) findViewById(R.id.url);
		mStatusTv = (TextView) findViewById(R.id.status);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		mPauseBtn = (Button) findViewById(R.id.pausebtn);
		mPauseBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mDownloadRunnable.isDownloading()){
					mDownloadRunnable.cancelDownload();
					mPauseBtn.setText("Go");
				}else {
					mDownloadRunnable.resumeDownload();
					ThreadF.getInstance().submit(mDownloadRunnable);
					mPauseBtn.setText("Pause");
				}
				
			}
			   
		});
		mCancelBtn = (Button) findViewById(R.id.cancelbtn);
		mCancelBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            mDownloadRunnable.cancelDownload();			
			}
			
		});
		mTotalSizeTv = (TextView) findViewById(R.id.totalsize);
		mDownloadSizeTv = (TextView) findViewById(R.id.downloadsize);
		
		mDownloadInfo = new DownloadInfo();
		mUrl = DownloadConfig.sUrls[0];
		mUrlTv.setText(mUrl);
		LogUtil.d(TAG + " mUrl = " + mUrl);
//		mDownloadFile = FileUtil.getIPCDownloadDir() + File.separator+"first.mp3";
		mDownloadPath = FileUtil.getIPCDownloadDir();
		mFileNameTv.setText(mDownloadFile);
//		File downloadFile = new File(mDownloadFile);
		LogUtil.d(TAG + " mDownloadFile = " + mDownloadFile);
//		URL url = new URL(mUrl);
		mDownloadRunnable = new DownloadRunnable(this,mUrl,mDownloadPath);
		mDownloadRunnable.setDownloadListener(this);
		ThreadF.getInstance().submit(mDownloadRunnable);
	}

	@Override
	public void updateProgress(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " updateProgress ");
		Message msg = new Message();
		msg.what = REFRESH;
		msg.obj = downloadInfo;
		mHandler.sendMessageDelayed(msg, 1);
	}

	@Override
	public void errorDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = ERROR;
		msg.obj = downloadInfo;
		mHandler.sendMessage(msg);
	}

	@Override
	public void finishDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = FINISH;
		msg.obj = downloadInfo;
		mHandler.sendMessage(msg);
	}

	@Override
	public void preDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		
	}
	
}