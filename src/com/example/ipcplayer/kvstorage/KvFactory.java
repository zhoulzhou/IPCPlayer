package com.example.ipcplayer.kvstorage;

import android.content.Context;

import com.example.ipcplayer.setting.ComFactory;
import com.example.ipcplayer.setting.ComInterface;

public class KvFactory extends ComFactory{
	
	/**
	 * 创建K-V组件接口方法
	 * @param object 创建对象所需参数，必须为Context
	 * @return 返回组件对象，如果参数不合法则返回null
	 */
	public static ComInterface createInterface(Context context){
		if(context != null){
			KvStorage instance = new KvStorage(context);
			return instance.getInstance();
		}else{
			return null;
		}
	}
}