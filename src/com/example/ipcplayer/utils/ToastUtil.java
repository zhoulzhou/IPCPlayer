package com.example.ipcplayer.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil{
	
	public static void showShortToast(Context context, String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context context, String string){
		Toast.makeText(context, string,Toast.LENGTH_LONG).show();
	}
}