package com.example.ipcplayer.cache.db;

import android.provider.BaseColumns;

final class CacheDBConfig{
	private static final String TAG = CacheDBConfig.class.getSimpleName();
	
	public static final String DB_NAME = "cache.db";
	
	public static final int DB_VERSION = 1;
	
	static final class Cache implements BaseColumns{
		public static final String TABLE_NAME = "cache";
		public static final String KEY = "key";
		public static final String DATA = "data";
		public static final String ENTER_TIME = "enter_time";
		public static final String LAST_USED_TIME = "last_used_time";
		public static final String VALID_TIME = "valid_time";
	}
	
}