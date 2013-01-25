package com.example.ipcplayer.utils;

import com.example.ipcplayer.application.IPCApplication;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil{
	private static String TAG = NetworkUtil.class.getSimpleName();
	
	public static boolean isWifiAvailable() {
		LogUtil.d(TAG + " isWifiAvailable ");
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) IPCApplication
				.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		// 获取WIFI网络连接状态
		android.net.NetworkInfo.State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		// 判断是否正在使用WIFI网络
		if (android.net.NetworkInfo.State.CONNECTED == state) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isMobileAvailble() {
		LogUtil.d(TAG + " isMobileAvailable ");
		ConnectivityManager connManager = (ConnectivityManager) IPCApplication
				.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		// 获取MOBLE网络连接状态
		android.net.NetworkInfo.State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		// 判断是否正在使用MOBILE网络
		if (android.net.NetworkInfo.State.CONNECTED == state) {
			return true;
		} else {
			return false;
		}
	}
}