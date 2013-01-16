package com.example.ipcplayer.utils;

import android.text.TextUtils;

public class StringUtil{
	
	public static boolean isEmpty(String str){
		if(str == null){
			return true;
		}
		str = str.trim();
		return TextUtils.isEmpty(str);
	}
	
}