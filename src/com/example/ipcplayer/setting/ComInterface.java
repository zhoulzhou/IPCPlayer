package com.example.ipcplayer.setting;

public interface ComInterface{
	/**
	 * 释放组件资源
	 */
	public void release();
	
	/**
	 * 获取组件接口对象
	 * @return 对象
	 */
	public ComInterface getInstance();
}