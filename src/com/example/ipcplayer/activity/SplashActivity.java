package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.provider.MusicDBManager;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StorageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{
	private static final String TAG = SplashActivity.class.getSimpleName();
	private Context mContext;
	private StartTask mLoadingTask;
	private long mStartTime;
	private static final long DELAY_TIME = 1000 ;
	
	private Handler mHandler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,       
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);   
		setContentView(R.layout.welcome);
		mContext = this;
		
		loadDataAndInitActivity();
	}
	
	private void loadDataAndInitActivity(){
		mLoadingTask = new StartTask();
		mLoadingTask.execute("loading local data ...");
	}
	
	private void showHelporMain(boolean result){
		if (result) {
			if (shouldShowHelp()) {
				Intent intent = new Intent(this, HelpActivity.class);
				mContext.startActivity(intent);
			} else {
				Intent intent = new Intent(this, MainActivity.class);
				mContext.startActivity(intent);
			}
			finish();
		}
	}
	
	private boolean shouldShowHelp(){
		return true ;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mLoadingTask != null){
			mLoadingTask = null;
		}
	}



	private class StartTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mStartTime = System.currentTimeMillis();
			LogUtil.d(TAG + " start loading data time: " + mStartTime);
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			LogUtil.d(TAG + " loading data ...");
			if (StorageUtil.isExternalStorageAvailable()) {
				MusicDBManager.getInstance(mContext).insertLocalData();
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			LogUtil.d(TAG + " loading data result: " + result.toString());
			final boolean loadResult = result;
			long taskTime = System.currentTimeMillis() - mStartTime;
			LogUtil.d(TAG + " loading data needs time: " + taskTime);
			long delayTime = DELAY_TIME - taskTime;
			LogUtil.d(TAG + " delayTime: " + delayTime);
			if (delayTime > 0) {
				mHandler.postDelayed(new Runnable(){

					@Override
					public void run() {
						showHelporMain(loadResult);
					}
					
				}, delayTime);
			}else {
				showHelporMain(loadResult);
			}
		}
		
	}
	
	
}