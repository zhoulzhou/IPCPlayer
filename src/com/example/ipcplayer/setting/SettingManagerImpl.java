package com.example.ipcplayer.setting;

import com.example.ipcplayer.kvstorage.KvFactory;
import com.example.ipcplayer.kvstorage.KvStorage;
import com.example.ipcplayer.kvstorage.IKvStorage;

import android.content.Context;

public class SettingManagerImpl implements SettingManager{
	private Context mContext ;
	private SettingManagerImpl mInstance = null;
	private static volatile int mRef = 0;
	private IKvStorage mKvStorage = null;
	private boolean mFirstBoot;
	
	private final static String KEY_FIRST_BOOT = "app_first_boot";
	private final static String KEY_SINA_TOKEN = "sina_token";
	private final static String KEY_SINA_EXPIRE_IN = "sina_expire_in";
	private final static String KEY_SINA_UID = "sina_uid";
	
	private final static String KEY_TENCENT_TOKEN = "tencent_token";
	private final static String KEY_TENCENT_OPEN_ID = "tencent_openid";
	private final static String KEY_TENCENT_OPEN_KEY = "tencent_open_key";
	
	private final static String KEY_APP_FOREGROUND_TIME = "key_app_foreground_time";
	
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
		mKvStorage = (IKvStorage) KvFactory.createInterface(mContext);
		mFirstBoot = mKvStorage.getBoolean(KEY_FIRST_BOOT, true);
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
		mRef = 0 ;
		mInstance = null ;
		mFirstBoot = false ;
	}

	@Override
	public ComInterface getInstance() {
		return mInstance;
	}

	@Override
	public void setTencentAccessToken(String token) {
		mKvStorage.putString(KEY_TENCENT_TOKEN, token);
		mKvStorage.commit();
	}

	@Override
	public void setTencentOpenId(String openId) {
		mKvStorage.putString(KEY_TENCENT_OPEN_ID, openId);
		mKvStorage.commit();
	}

	@Override
	public void setTencentOpenKey(String openKey) {
		mKvStorage.putString(KEY_TENCENT_OPEN_KEY, openKey);
		mKvStorage.commit();
	}

	@Override
	public void setAppForegroundTime(long peroid) {
		mKvStorage.putLong(KEY_APP_FOREGROUND_TIME, peroid);
		mKvStorage.commit();
	}

	@Override
	public long getAppForegroundTime() {
		return mKvStorage.getLong(KEY_APP_FOREGROUND_TIME,0);
	}

	@Override
	public String getTencentAccessToken() {
		return mKvStorage.getString(KEY_TENCENT_TOKEN, null);
	}

	@Override
	public String getTencentOpenKey() {
		return mKvStorage.getString(KEY_TENCENT_OPEN_KEY, null);
	}

	@Override
	public String getTencentOpenId() {
		return mKvStorage.getString(KEY_TENCENT_OPEN_ID, null);
	}

	@Override
	public String getSinaAccessToken() {
		return mKvStorage.getString(KEY_SINA_TOKEN, null);
	}

	@Override
	public String getSinaExpireIn() {
		return mKvStorage.getString(KEY_SINA_EXPIRE_IN, null);
	}

	@Override
	public void setSinaAccessToken(String token) {
		mKvStorage.putString(KEY_SINA_TOKEN, token);
		
	}

	@Override
	public void setSinaUid(String uid) {
		mKvStorage.putString(KEY_SINA_UID, uid);
		
	}

	@Override
	public String getSinaUid() {
		return mKvStorage.getString(KEY_SINA_UID, null);
	}

	@Override
	public void setSinaExpireIn(String expire) {
		mKvStorage.putString(KEY_SINA_EXPIRE_IN, expire);
		
	}
}