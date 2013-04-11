package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.provider.MusicDBManager;
import com.example.ipcplayer.utils.StorageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends Activity{
	private Context mContext;
	private StartTask mLoadingTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,       
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);   
		setContentView(R.layout.welcome);
		mContext = this;
		
		initActivity();
	}
	
	private void initActivity(){
		mLoadingTask = new StartTask();
		mLoadingTask.execute("loading local data ...");
	}
	
	private void showHelporMain(){
		if(shouldShowHelp()){
			Intent intent = new Intent(this,HelpActivity.class);
			mContext.startActivity(intent);
		}else{
			Intent intent = new Intent(this,MainActivity.class);
			mContext.startActivity(intent);
		}
		finish();
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
		protected Boolean doInBackground(String... arg0) {
			if (StorageUtil.isExternalStorageAvailable()) {
				MusicDBManager.getInstance(mContext).insertLocalData();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			showHelporMain();
		}
		
	}
	
	
}