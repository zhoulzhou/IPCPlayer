package com.example.ipcplayer.interf;

import com.example.ipcplayer.utils.LogUtil;

public class Caller{
	private ICallback callback;
	
	public void doCallback(){
		LogUtil.d("call interface method");
		callback.callback();
	}
	
	public void setCallback(ICallback callback){
		LogUtil.d("set interface");
		this.callback = callback;
	}
}