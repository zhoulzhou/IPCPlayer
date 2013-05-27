package com.example.ipcplayer.cache;

import com.example.ipcplayer.utils.LogUtil;

/**
 *data used to  test cache module
 */
public class CacheData implements Cacheable{
	private static final String TAG = CacheData.class.getSimpleName();

	@Override
	public long calculateMemSize() {
		return 0;
	}

	@Override
	public String buildCacheData() {
		return getJson();
	}

	@Override
	public Cacheable parseCacheData(String data) {
		parse(data);
		return this;
	}

	@Override
	public boolean isCacheable() {
		//获取到的数据可用且不为空
		return true;
	}
	
	public String getJson(){
		return "json";
	}
	
	public void parse(String json){
		LogUtil.d(TAG  + " parse json");
	}
}
