package com.example.ipcplayer.interf;


import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;

public class TestMainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Caller caller = new Caller();
		LogUtil.d("execute interface method");
		caller.setCallback(new ICallback(){

			@Override
			public void callback() {
				LogUtil.d("call ++++++++ back ");
				
			}
			
		}
				);
		caller.doCallback();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}