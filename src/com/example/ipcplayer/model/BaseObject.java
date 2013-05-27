package com.example.ipcplayer.model;

import java.io.Serializable;

import com.example.ipcplayer.cache.Cacheable;

public class BaseObject implements Cacheable, Serializable, Cloneable{

	/**
	 *default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected BaseObject clone() throws CloneNotSupportedException {
		BaseObject obj = null;
		try {
			obj = (BaseObject) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	//子类实现此方法，各自解析自己的json数据
	protected void parse(String json){
		
	}
	
	public String getJson(){
		String json = null;
		return json = " json";
	}
	
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
		return true;
	}
	
}