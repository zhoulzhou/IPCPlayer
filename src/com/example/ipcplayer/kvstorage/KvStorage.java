package com.example.ipcplayer.kvstorage;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.ipcplayer.setting.ComInterface;

public class KvStorage implements IKvStorage{
	private KvStorage mInstance;
	private SharedPreferences mSharedPreferences;
	private Editor mEditor ;
	private static volatile int mRef = 0 ;
	
	public KvStorage(Context context){
		mSharedPreferences = context.getSharedPreferences("ipc", Context.MODE_PRIVATE); 
		mEditor = mSharedPreferences.edit();
		synchronized(this){
			if(mInstance == null){
				mInstance = this;
			}
		}
		++ mRef ;
	}

	@Override
	public void release() {
		mInstance = null;
		mRef = 0 ;
	}

	@Override
	public ComInterface getInstance() {
		return mInstance;
	}

	@Override
	public boolean putString(String key, String value) {
		return mEditor.putString(key, value) != null;
	}

	@Override
	public boolean putBoolean(String key, boolean value) {
		return mEditor.putBoolean(key, value) != null;
	}

	@Override
	public boolean putInt(String key, int value) {
		return mEditor.putInt(key, value) != null;
	}

	@Override
	public boolean putFloat(String key, float value) {
		return mEditor.putFloat(key, value) != null;
	}

	@Override
	public boolean putLong(String key, long value) {
		return mEditor.putLong(key, value) != null;
	}

	@Override
	public boolean remove(String key) {
		return mEditor.remove(key) != null ;
	}

	@Override
	public boolean contains(String key) {
		return mSharedPreferences.contains(key);
	}

	@Override
	public Map<String, ?> getAll() {
		return mSharedPreferences.getAll();
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		return mSharedPreferences.getFloat(key, defValue);
	}

	@Override
	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	@Override
	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	@Override
	public boolean clear() {
		return mEditor.clear() != null ;
	}

	@Override
	public boolean commit() {
		return mEditor.commit();
	}
	
}