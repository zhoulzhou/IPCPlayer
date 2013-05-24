package com.example.ipcplayer.cache.db;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.cache.CacheEntity;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class CacheDBHelper extends SQLiteOpenHelper{
	private static final String TAG = CacheDBHelper.class.getSimpleName();
	private static CacheDBHelper mInstance = null;
	private SQLiteDatabase mDB = null;
	
	/** 最多存200条*/
	private static int MAX_ROW_COUNT = 200;

	public CacheDBHelper(Context context) {
		super(context, CacheDBConfig.DB_NAME, null, CacheDBConfig.DB_VERSION);
		openDB();
	}
	
	public static CacheDBHelper getInstance(Context context){
		if(mInstance != null){
			return mInstance;
		}
		synchronized (CacheDBHelper.class) {
			if(mInstance == null){
				mInstance = new CacheDBHelper(context);
			}
		}
		
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
		mDB = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db);
		createTable(db);
	}
	
	private void openDB(){
		try{
			mDB = getWritableDatabase();
		}catch(Exception e){
			e.printStackTrace();
			mDB = null;
		}
	}
	
	private void createTable(SQLiteDatabase db){
		try{
		String sql = " CRATE TABEL IF NOT EXISTS " 
				+ CacheDBConfig.Cache.TABLE_NAME + " (" + BaseColumns._ID 
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ CacheDBConfig.Cache.KEY + " TEXT UNIQUE,"
				+ CacheDBConfig.Cache.DATA + " TEXT NOT NULL,"
				+ CacheDBConfig.Cache.ENTER_TIME + " INTEGER NOT NULL,"
				+ CacheDBConfig.Cache.LAST_USED_TIME + " INTEGER NOT NULL," 
				+ CacheDBConfig.Cache.VALID_TIME + " INTEGER NOT NULL);";
	      db.execSQL(sql);
		}catch(Exception e){
			LogUtil.d(TAG + " create table error ");
			e.printStackTrace();
		}
	      
	}
	
	private void dropTable(SQLiteDatabase db){
		try{
			db.execSQL(" DROP TABLE IF EXISTS " + CacheDBConfig.Cache.TABLE_NAME);
		}catch(Exception e){
			LogUtil.d(TAG + " drop table error " );
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置最多存储数目
	 * @param count
	 */
	public void setMaxCount(int count){
		MAX_ROW_COUNT = count;
	}
	
	/**
	 * 插入缓存数据
	 * @param entity
	 */
	public void insert(CacheEntity entity){
		String key = entity.getKey();
		String data = entity.getData();
		long enterTime = entity.getEnterTime();
		long lastUsedTime = entity.getLastUsedTime();
		long validTime = entity.getValidTime();
		
		if(StringUtil.isEmpty(key) || StringUtil.isEmpty(data)){
			return ;
		}
		delete(key);
		ContentValues values = new ContentValues();
		values.put(CacheDBConfig.Cache.KEY, key);
		values.put(CacheDBConfig.Cache.DATA, data);
		values.put(CacheDBConfig.Cache.ENTER_TIME, enterTime);
		values.put(CacheDBConfig.Cache.LAST_USED_TIME, lastUsedTime);
		values.put(CacheDBConfig.Cache.VALID_TIME, validTime);
		
		try{
			mDB.insert(CacheDBConfig.Cache.TABLE_NAME, null, values);
			triggerDelete();
		}catch(Exception e){
			LogUtil.d(TAG + " insert error " );
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取key缓存对象
	 * @param key
	 * @return
	 */
	public CacheEntity get(String key){
		if(StringUtil.isEmpty(key)){
			LogUtil.d(TAG + " get error  key is null ");
			return null;
		}
		CacheEntity entity = new  CacheEntity();
		
		String selection = CacheDBConfig.Cache.KEY + " = ?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] = key;
		Cursor cursor = null;
		try{
			cursor = mDB.query(CacheDBConfig.Cache.TABLE_NAME, null, selection, selectionArgs, null, null, null);
			if(cursor == null ){
				return null;
			}
			if(cursor.getCount() == 0){
				cursor.close();
				return null;
			}
			cursor.moveToFirst();
			entity = getCacheEntityFromCursor(cursor);
		}catch(Exception e){
			e.printStackTrace();
			cursor = null;
		}finally{
			if(cursor != null){
				cursor.close();
				cursor = null;
			}
		}
		return entity;
	}
	
	/**
	 * 从cursor中读取缓存数据 ，构建成CacheEntity对象
	 * @param cursor
	 * @return
	 */
	private CacheEntity getCacheEntityFromCursor(Cursor cursor){
		if(cursor == null || cursor.getCount() == 0){
			return null;
		}
		CacheEntity entity = new CacheEntity();
		entity.setKey(cursor.getString(cursor.getColumnIndex(CacheDBConfig.Cache.KEY)));
		entity.setData(cursor.getString(cursor.getColumnIndex(CacheDBConfig.Cache.DATA)));
		entity.setEnterTime(cursor.getLong(cursor.getColumnIndex(CacheDBConfig.Cache.ENTER_TIME)));
		entity.setValidTime(cursor.getLong(cursor.getColumnIndex(CacheDBConfig.Cache.VALID_TIME)));
		entity.setLastUsedTime(cursor.getLong(cursor.getColumnIndex(CacheDBConfig.Cache.LAST_USED_TIME)));
		return entity;
	}
	
	/**
	 * 更新缓存数据的lastUsedTime validTime
	 * @param entity
	 */
	public void update(CacheEntity entity){
		if(entity == null){
			LogUtil.d(TAG + " update error entity is null");
			return ;
		}
		
		ContentValues values = new ContentValues();
		values.put(CacheDBConfig.Cache.LAST_USED_TIME, entity.getLastUsedTime());
		values.put(CacheDBConfig.Cache.VALID_TIME, entity.getValidTime());
		String whereClause = CacheDBConfig.Cache.KEY + " =?";
		String[] whereArgs = new String[1];
		whereArgs[0] = entity.getKey();
		mDB.update(CacheDBConfig.Cache.TABLE_NAME, values, whereClause, whereArgs);
	}
	
	/**
	 * 删除key 缓存数据
	 * @param key
	 */
	public void delete(String key){
		if(StringUtil.isEmpty(key)){
			LogUtil.d(TAG + " delete error key is null ");
			return ;
		}
		String[] whereArgs = new String[1];
		whereArgs[0] = key;
		mDB.delete(CacheDBConfig.Cache.TABLE_NAME, CacheDBConfig.Cache.KEY + " =?", whereArgs);
	}
	
	/**
	 * 删除大于最大存储数的数据
	 */
	private void triggerDelete(){
		String whereClause = " _id IN (SELECT _id FROM "
				+ CacheDBConfig.Cache.TABLE_NAME + " ORDER BY "
				+ CacheDBConfig.Cache.LAST_USED_TIME
				+ " LIMIT 0, max((SELECT COUNT(*) FROM CACHE)-" + MAX_ROW_COUNT
				+ ", 0))";
		LogUtil.d(TAG + " triggerDelete whereClause= " + whereClause);
		mDB.delete(CacheDBConfig.Cache.TABLE_NAME, whereClause, null);
	}
	
	/**
	 * 查询所有的缓存数据
	 * @return List<CacheEntity>
	 */
	public List<CacheEntity> queryAll(){
		List<CacheEntity> entityList = new ArrayList<CacheEntity>();
		Cursor cursor = null;
		try{
			cursor = mDB.query(CacheDBConfig.Cache.TABLE_NAME, null, null, null, null, null, null);
			if(cursor == null){
				return null;
			}
			if(cursor.getCount() == 0){
				cursor.close();
				return null;
			}
			while (cursor.moveToNext()) {
				CacheEntity entity = new CacheEntity();
				entity = getCacheEntityFromCursor(cursor);
				entityList.add(entity);
			}
		}catch(Exception e){
			e.printStackTrace();
			cursor = null;
		}finally{
			if(cursor != null){
				cursor.close();
				cursor = null;
			}
		}
		return entityList;
	}
	
	private void closeDB(){
		if(mDB != null && mDB.isOpen()){
			try{
			mDB.close();
			}catch(Exception e){
				LogUtil.d(TAG + " closeDB error ...");
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeDB();
	}
	
	
	
}
