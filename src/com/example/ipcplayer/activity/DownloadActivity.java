package com.example.ipcplayer.activity;

import java.net.URL;

import com.example.ipcplayer.R;
import com.example.ipcplayer.download.DownloadConfig;
import com.example.ipcplayer.download.DownloadInfo;
import com.example.ipcplayer.download.DownloadListener;
import com.example.ipcplayer.download.DownloadRunnable;
import com.example.ipcplayer.utils.FileUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class DownloadActivity extends Activity implements DownloadListener{

	TextView mFileNameTv;
	TextView mUrlTv;
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
	private static final int REFRESH = 0;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH:
				progressBar.setProgress((int) (mDownloadSize / mTotalSize));
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
		setContentView(R.layout.download);
		
		mFileNameTv = (TextView) findViewById(R.id.filename);
		mUrlTv = (TextView) findViewById(R.id.url);
		progressBar = (ProgressBar) findViewById(R.id.progressbar);
		progressBar.setProgress(0);
		progressBar.setMax(100);
		mPauseBtn = (Button) findViewById(R.id.pausebtn);
		mCancelBtn = (Button) findViewById(R.id.cancelbtn);
		mTotalSizeTv = (TextView) findViewById(R.id.totalsize);
		mDownloadSizeTv = (TextView) findViewById(R.id.downloadsize);
		
		mUrl = DownloadConfig.sUrls[0];
		mDownloadFile = FileUtil.getIPCDownloadDir() + "\first.mp3";
//		URL url = new URL(mUrl);
		mDownloadRunnable = new DownloadRunnable(this,mUrl,mDownloadFile);
		Thread downloadThread = new Thread(mDownloadRunnable);
		downloadThread.start();
		
	}

	@Override
	public void updateProgress(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = REFRESH;
		this.handler.sendMessageDelayed(msg, 100);
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