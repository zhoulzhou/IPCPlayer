package com.example.ipcplayer.activity;

import com.example.ipcplayer.cache.CacheData;
import com.example.ipcplayer.cache.CacheEntity;
import com.example.ipcplayer.cache.CacheExpiredException;
import com.example.ipcplayer.cache.CacheUncachedException;
import com.example.ipcplayer.cache.DataCache;

import android.app.Activity;
import android.os.Bundle;

public class DataCacheActivity extends Activity{

	private CacheData mData = new CacheData();
	private CacheEntity mEntity = new CacheEntity();
	String key ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		key = "key+1";
		mEntity.setKey(key);
		mEntity.setCacheable(mData);
		try {
			mData = (CacheData) DataCache.getInstance(this).get(key,mEntity);
		} catch (CacheExpiredException e) {
			e.printStackTrace();
		} catch (CacheUncachedException e) {
			e.printStackTrace();
			DataCache.getInstance(this).put(key, mData, 10000);
		}
	}
	
}