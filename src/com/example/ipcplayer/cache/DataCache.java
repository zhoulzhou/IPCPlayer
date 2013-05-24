package com.example.ipcplayer.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.example.ipcplayer.cache.db.CacheDBHelper;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

import android.content.Context;
import android.util.LruCache;

public class DataCache{
	private final String TAG = DataCache.class.getSimpleName();
	private static DataCache mInstance;
	private Context mContext;
	
	/** 缓存体*/
	private LruCache<String, CacheEntity> mCache;
	
	/** 缓存体的最大空间  （1M）*/
	private long mMaxSpace = 1*1024*1024;
	
	/** 已使用的缓存空间*/
	private long mCachedSpace = 0;
	
	/** 击中次数*/
	private int mHitCount = 0;
	
	/** 丢失次数*/
	private int mMissCount = 0;
	
	/** 待缓存的数据集合  */
	private ConcurrentHashMap<Integer, CacheEntity> mUpdateSet = new ConcurrentHashMap<Integer, CacheEntity>();
	
	/** 缓存的最大阀值  达到此值后就存入数据库 */
	private final int UPDATE_THRESHOLD = 10;
	
	private DataCache(Context context){
		mContext = context;
		mCache = new LruCache<String, CacheEntity>(2){
			//缓存数据的处理
//			@Override
//			public void recycleEldestEntry(CacheEntity eldestRef) {
//				mCachedSpace -= eldestRef.caculateMemSize();
//				validate();
//			}
		};
		
	}
	
	public DataCache getInstance(Context context){
		if (mInstance != null)
			return mInstance;
		
		synchronized (DataCache.class) {
			if (mInstance == null)
				mInstance = new DataCache(context);
		}
		
		return mInstance;
	}
	
	/**
	 * 设置内存缓存的最大存储空间
	 * @param maxSpace
	 */
	public void setMaxSpace(long maxSpace){
		mMaxSpace = maxSpace;
	}
	
	/**
	 * 设置数据库的最多缓存条数
	 * @param count
	 */
	public void setDBMaxRowCount(int count){
		
	}
	
	/**
	 * 从缓存模块中读取缓存的数据
	 * @param key
	 * @param entity
	 * @return
	 * @throws CacheExpiredException
	 * @throws CacheUncachedException
	 */
	public Cacheable get(String key, CacheEntity entity) 
	        throws CacheExpiredException, CacheUncachedException {
		if(StringUtil.isEmpty(key) || entity == null){
			return null;
		}
		
		CacheEntity cacheEntity = mCache.get(key);
		LogUtil.d(TAG, "readFromMemCache entry : " + cacheEntity);
		
		if(cacheEntity == null){
			cacheEntity = readFromDatabase(key);
		}
		
		Cacheable cacheObject = null;
		if(cacheEntity != null){
			cacheEntity.setLastUsedTime(System.currentTimeMillis());
			mUpdateSet.put(cacheEntity.hashCode(), cacheEntity);
			if(mUpdateSet.size() > UPDATE_THRESHOLD){
				updateDB();
			}
			cacheObject = cacheEntity.getCacheable();
		}
		
		if(cacheObject == null){
			mMissCount ++ ;
		}else{
			mHitCount ++;
		}
//		cacheEntity.setObject(cacheObject);//有用吗？
		if(cacheObject == null){
			throw new CacheUncachedException();
		}
		if(cacheEntity.isExpired()){
			throw new CacheExpiredException();
		}
		
		return cacheObject;
	}
	
	/**
	 * 从数据库读取缓存数据
	 * @param key
	 * @return CacheEntity
	 */
	private CacheEntity readFromDatabase(String key){
		CacheEntity entity = new  CacheEntity();
		entity = CacheDBHelper.getInstance(mContext).get(key);
		putIntoMemCache(key, entity);
		return entity;
	}
	
	/**
	 * 数据放入缓存模块
	 * @param key
	 * @param obj
	 * @param valideTime
	 * @return
	 */
	public Cacheable put(String key, Cacheable obj, long valideTime){
		if(StringUtil.isEmpty(key) || obj == null || !obj.isCacheable() || valideTime < 0){
			return null;
		}
		 
		long cacheTime = System.currentTimeMillis();
		
		CacheEntity entity = new CacheEntity(key, obj, cacheTime, valideTime);
		putIntoMemCache(key,entity);
		validate();
		saveToDB(entity);
		return obj;
	}
	
	/**
	 * 更新数据库中缓存数据
	 */
	private void updateDB() {
		Set<Integer> keys = mUpdateSet.keySet();
		CacheEntity entity = null;
		for (Integer key : keys) {
			entity = mUpdateSet.remove(key);
			CacheDBHelper.getInstance(mContext).update(entity);
		}
	}
	
	/**
	 * 缓存数据放入内存缓存
	 * @param key
	 * @param entity
	 */
	private void putIntoMemCache(String key, CacheEntity entity){
		if(StringUtil.isEmpty(key) || entity == null){
			return ;
		}
		mCache.put(key, entity);
		mCachedSpace += entity.calculateMemSize();// 计算缓存所占空间
	}
	
	/**
	 * 检查缓存空间
	 */
	private void validate(){
		if(mCachedSpace > mMaxSpace){
			LogUtil.d(TAG + " cache space is expired ");
		}
	}
	
	/**
	 * 缓存数据保存到数据库
	 * @param entity
	 */
	private void saveToDB(CacheEntity entity){
		if(entity == null){
			return ;
		}
		
		CacheDBHelper.getInstance(mContext).insert(entity);
	}
	
	/**
	 * 获取命中率
	 * 
	 * @return 命中率(%)
	 */
	public int getHitRate() {
		int accesses = mHitCount + mMissCount;
		int hitPercent = accesses != 0 ? (100 * mHitCount / accesses) : 0;
		return hitPercent;
	}
	
	/**
	 * 清空内存缓存
	 */
	public void clearMemSpace(){
		updateDB();
		if(mCache != null){
			mCache.evictAll();
		}
		mCachedSpace = 0;
	}
}