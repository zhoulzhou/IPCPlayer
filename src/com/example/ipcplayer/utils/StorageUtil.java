package com.example.ipcplayer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.StatFs;

public class StorageUtil{
	
	   //¼àÌýsdcard×´Ì¬¹ã²¥  
	   private BroadcastReceiver mExternalStorageReceiver;  
	   //sdcard¿ÉÓÃ×´Ì¬  
	   private static boolean mExternalStorageAvailable = false;  
	   //sdcard¿ÉÐ´×´Ì¬  
	   private static boolean mExternalStorageWriteable = false;  
	   private static String TAG = StorageUtil.class.getSimpleName();
	  
	   public static void updateExternalStorageState() {  
		   LogUtil.d(TAG + " updateExternalStorageState ");
	       //»ñÈ¡sdcard¿¨×´Ì¬  
	       String state = Environment.getExternalStorageState();  
	       if (Environment.MEDIA_MOUNTED.equals(state)) {  
	           mExternalStorageAvailable = mExternalStorageWriteable = true;  
	       } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {  
	           mExternalStorageAvailable = true;  
	           mExternalStorageWriteable = false;  
	       } else {  
	           mExternalStorageAvailable = mExternalStorageWriteable = false;  
	       }  
	         
	   }  
	   
	   public static boolean isExternalStorageAvailable(){
		   LogUtil.d(TAG + " isExternalStorageAvailable ");
		   String state = Environment.getExternalStorageState();
		   if(Environment.MEDIA_MOUNTED.equals(state)){
			   return true ;
		   }else {
			   return false ;
		   }
	   }
	   
	   public static StatFs getStatFs(){
		   LogUtil.d(TAG + " getStatFs ");
		   StatFs statFs ;
		   if(isExternalStorageAvailable()){
			   statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		   }else{
			   statFs = new StatFs(Environment.getDataDirectory().getPath());
		   }
		   return statFs ;
	   }
	   
	   public static long getTotalSpace(){
		   LogUtil.d(TAG + " getTotalSpace ");
		   long blockSize = getStatFs().getBlockSize();
		   long totalCount = getStatFs().getBlockCount();
		   return blockSize * totalCount;
		}
	   
	   public static long getFreeSpace(){
		   LogUtil.d(TAG + " getFreeSpace ");
		   long blockSize = getStatFs().getBlockSize();
		   long availableBlock = getStatFs().getAvailableBlocks();
		   return blockSize * availableBlock;
	   }
	   
	   //¿ªÊ¼¼àÌý  
//	   void startWatchingExternalStorage() {  
//	       mExternalStorageReceiver = new BroadcastReceiver() {  
//	           @Override  
//	           public void onReceive(Context context, Intent intent) {  
//	               LogUtil.d(TAG + "Storage: " + intent.getData());  
//	               updateExternalStorageState();  
//	           }  
//	       };  
//	       IntentFilter filter = new IntentFilter();  
//	       filter.addAction(Intent.ACTION_MEDIA_MOUNTED);  
//	       filter.addAction(Intent.ACTION_MEDIA_REMOVED);  
//	       registerReceiver(mExternalStorageReceiver, filter);  
//	       updateExternalStorageState();  
//	   }  
	   //Í£Ö¹¼àÌý  
//	   void stopWatchingExternalStorage() {  
//	       unregisterReceiver(mExternalStorageReceiver);  
//	   }  
	
}