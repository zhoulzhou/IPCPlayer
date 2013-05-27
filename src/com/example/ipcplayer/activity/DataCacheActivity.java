package com.example.ipcplayer.activity;

import com.example.ipcplayer.cache.CacheEntity;
import com.example.ipcplayer.cache.CacheExpiredException;
import com.example.ipcplayer.cache.CacheUncachedException;
import com.example.ipcplayer.cache.DataCache;
import com.example.ipcplayer.model.Artist;
import com.example.ipcplayer.utils.LogUtil;

import android.R;
import android.app.Activity;
import android.os.Bundle;

public class DataCacheActivity extends Activity{
    private static final String TAG = DataCacheActivity.class.getSimpleName();
	private Artist mArtist = new Artist();
	private CacheEntity mEntity = new CacheEntity();
	private long validTime = 1000000;
	String key ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		for(int i = 0 ; i < 100; i ++){
		mArtist.setName("jielun *** " + i);
		mArtist.setDetail("a;d adkjfal s *** " + i);
		key = "key+1";
		mEntity.setKey(key);
		mEntity.setCacheable(mArtist);
		//获取数据
		try {
			mArtist = (Artist) DataCache.getInstance(this).get(key,mEntity);
			if(mArtist != null){
				LogUtil.d(TAG + " mArst = " + mArtist.toString());
			}else{
				LogUtil.d(TAG + " mArtist is null ");
			}
		} catch (CacheExpiredException e) {
			e.printStackTrace();
			DataCache.getInstance(this).put(key, mArtist, validTime);
		} catch (CacheUncachedException e) {
			e.printStackTrace();
			//在此解析json 并存在内存中 ，这样下次在内存中就是解析后的数据了
			DataCache.getInstance(this).put(key, mArtist, validTime);
			System.out.println("put int cache ");
		}
	}
	}
}