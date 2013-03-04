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

	@Override
	public void setAppForegroundTime(long peroid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getAppForegroundTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTencentAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTencentOpenKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTencentOpenId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSinaAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSinaExpireIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSinaAccessToken(String token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSinaUid(String uid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSinaUid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSinaExpireIn(String expire) {
		// TODO Auto-generated method stub
		
	}
}