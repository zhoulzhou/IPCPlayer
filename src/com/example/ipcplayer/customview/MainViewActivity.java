package com.example.ipcplayer.customview;

import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;

public class MainViewActivity extends Activity{
	private static final String TAG = MainViewActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG + " oncreateview before");
		setContentView(R.layout.custom_view);
		LogUtil.d(TAG + " oncreateview after");
		
	}
	
}