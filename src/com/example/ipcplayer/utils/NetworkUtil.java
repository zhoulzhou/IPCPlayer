package com.example.ipcplayer.utils;

import com.example.ipcplayer.application.IPCApplication;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil{
	private static String TAG = NetworkUtil.class.getSimpleName();
	
	public static boolean isWifiAvailable() {
		LogUtil.d(TAG + " isWifiAvailable ");
		// ����������ӷ���
		ConnectivityManager connManager = (ConnectivityManager) IPCApplication
				.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		// ��ȡWIFI��������״̬
		android.net.NetworkInfo.State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		// �ж��Ƿ�����ʹ��WIFI����
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
		// ��ȡMOBLE��������״̬
		android.net.NetworkInfo.State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		// �ж��Ƿ�����ʹ��MOBILE����
		if (android.net.NetworkInfo.State.CONNECTED == state) {
			return true;
		} else {
			return false;
		}
	}
}