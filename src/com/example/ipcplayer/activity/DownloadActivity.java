package com.example.ipcplayer.activity;

import java.io.File;
import java.net.URL;

import com.example.ipcplayer.R;
import com.example.ipcplayer.download.DownloadConfig;
import com.example.ipcplayer.download.DownloadInfo;
import com.example.ipcplayer.download.DownloadListener;
import com.example.ipcplayer.download.DownloadRunnable;
import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private int mStatus ;
	private static final int REFRESH = 0;
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
				progressBar.setProgress((int) (mDownloadSize / mTotalSize));
				mStatusTv.setText(" status : " +mStatus);
				mTotalSizeTv.setText(mTotalSize + "");
				mDownloadSizeTv.setText(mDownloadSize + "");
				break;
			default:
				break;
			}
		}
		
	};
	
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
		mCancelBtn = (Button) findViewById(R.id.cancelbtn);
		mTotalSizeTv = (TextView) findViewById(R.id.totalsize);
		mDownloadSizeTv = (TextView) findViewById(R.id.downloadsize);
		
		mDownloadInfo = new DownloadInfo();
		mUrl = DownloadConfig.sUrls[0];
		LogUtil.d(TAG + " mUrl = " + mUrl);
		mDownloadFile = FileUtil.getIPCDownloadDir() + File.separator+"first.mp3";
//		File downloadFile = new File(mDownloadFile);
		LogUtil.d(TAG + " mDownloadFile = " + mDownloadFile);
//		URL url = new URL(mUrl);
		mDownloadRunnable = new DownloadRunnable(this,mUrl,mDownloadFile);
		mDownloadRunnable.setDownloadListener(this);
		Thread downloadThread = new Thread(mDownloadRunnable);
		downloadThread.start();
		
	}

	@Override
	public void updateProgress(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " updateProgress ");
		Message msg = new Message();
		msg.what = REFRESH;
		msg.obj = downloadInfo;
		mHandler.sendMessageDelayed(msg, 10);
	}

	@Override
	public void errorDownload(DownloadInfo downloadInof) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		
	}
	
}