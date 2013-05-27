package com.example.ipcplayer.cache;

import com.example.ipcplayer.utils.LogUtil;


public class CacheEntity {
    private static final String TAG = CacheEntity.class.getSimpleName();
	/** 被缓存的数据对象 */
	private Cacheable object;
	
	/** 缓存标识   根据URL及参数构造网络请求缓存key*/
	private String key;
	
	/**  进入缓存时间  （毫秒） */
	private long enterTime;
	
	/** 有效使用时间 */
	private long validTime;
	
	/** 上次使用时间 */
	private long lastUsedTime;

	public CacheEntity(Cacheable object, long enterTime, long validTime) {
		super();
		this.object = object;
		this.enterTime = enterTime;
		this.validTime = validTime;
	}

	public CacheEntity() {
		
	}

	public CacheEntity(String key, Cacheable obj, long enterTime, long validTime) {
		this.key = key;
		this.object = obj;
		this.enterTime = enterTime;
		this.validTime = validTime;
	}

	public CacheEntity(Cacheable obj) {
		this.object = obj;
	}
	
	
	public Cacheable getCacheable() {
		return object;
	}

	public void setCacheable(Cacheable cacheable) {
		this.object = cacheable;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getEnterTime() {
		return enterTime;
	}

	/**
	 * 设置进入缓存时间
	 * @param enterTime
	 */
	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public long getValidTime() {
		return validTime;
	}

	public void setValidTime(long validTime) {
		this.validTime = validTime;
	}

	/**
	 * 获取上次使用的时间
	 * @return long time
	 */
	public long getLastUsedTime() {
		return lastUsedTime;
	}

	/**
	 * 设置上次使用的时间
	 * @param lastUsedTime
	 */
	public void setLastUsedTime(long lastUsedTime) {
		this.lastUsedTime = lastUsedTime;
	}
	
	/**
	 * 判断是否过期
	 * @return boolean
	 */
	public boolean isExpired(){
		long time =System.currentTimeMillis();
		LogUtil.d(TAG + "currentTime= " + time +" (enterTime + validTime)= " +(enterTime + validTime));
		return time > (enterTime + validTime);
	}
	
	/**
	 * 获取缓存对象占用内存的大小
	 * @return long 缓存对象的大小
	 */
	public long calculateMemSize(){
		if(this.object == null){
			return 0;
		}
		return this.object.calculateMemSize();
	}
	
	public String getData(){
		if(object == null){
			return null;
		}
		return object.buildCacheData();
	}
	
	public void setData(String data){
		LogUtil.d(TAG + " setData, data = " + data);
		if(object == null){
			LogUtil.d(TAG + " setData, object is null ");
			return ;
		}
		object.parseCacheData(data);
	}

	@Override
	public String toString() {
		return "CacheEntry [object=" + object + ", key=" + key + ", enterTime="
				+ enterTime + ", validTime=" + validTime + ", lastUsedTime="
				+ lastUsedTime + "]";
	}
	
}