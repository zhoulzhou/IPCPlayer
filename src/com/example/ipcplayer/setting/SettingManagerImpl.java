package com.example.ipcplayer.setting;

import android.content.Context;

public class SettingManagerImpl implements SettingManager{
	private Context mContext ;
	private SettingManagerImpl mInstance = null;
	private static volatile int mRef = 0;
	
	public SettingManagerImpl(Context context){
		mContext = context ;
		synchronized(this){
			if(mInstance == null){
				mInstance =this ;
			}
		}
		
		if(mRef == 0){
			init();
		}
		
		++mRef ;
	}

	private void init(){
		
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
		mRef = 0 ;
		mInstance = null ;
	}

	@Override
	public ComInterface getInstance() {
		return mInstance;
	}

	@Override
	public void setTencentAccessToken(String token) {
		
	}

	@Override
	public void setTencentOpenId(String openId) {
		
	}

	@Override
	public void setTencentOpenKey(String openKey) {
		
	}
}