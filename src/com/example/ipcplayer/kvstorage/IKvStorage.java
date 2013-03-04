package com.example.ipcplayer.kvstorage;

import java.util.Map;

import com.example.ipcplayer.setting.ComInterface;

/**
 * K-V存储组件对外接口，单实例组件，负责将K-V数据存储到APP内部
 * 
 * @version 1.0
 * @data 2012-7-10
 */
public interface IKvStorage extends ComInterface
{
	public boolean putString(String key, String value);
	public boolean putBoolean(String key, boolean value);
	public boolean putInt(String key, int value);
	public boolean putFloat(String key, float value);
	public boolean putLong(String key, long value);
	public boolean remove(String key);
	public boolean contains(String key);
	public Map<String, ?> getAll();
	public boolean getBoolean(String key, boolean defValue);
	public float getFloat(String key, float defValue);
	public int getInt(String key, int defValue);
	public long getLong(String key, long defValue);
	public String getString(String key, String defValue);
	public boolean clear();
	public boolean commit();
}