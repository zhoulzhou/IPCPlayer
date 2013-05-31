package com.example.ipcplayer.utils;

import com.example.ipcplayer.setting.Config;

import android.util.Log;

public class LogUtil{
	private static  boolean DEBUG = Config.DEBUG_MODE;
	private static final String DEFAULT_TAG = "[IPC's ready for coming]";
	
	public static void trace(String log ) {    
        String lineFormat = "%s--%s--%d";    
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];    
        String logText = String.format(lineFormat, traceElement.getFileName(),    
                traceElement.getMethodName(), traceElement.getLineNumber());    
        if(DEBUG){
			Log.d(DEFAULT_TAG, logText + " --> " + log);
		}   
    }    
	
	private static String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
 
		if (sts == null) {
			return null;
		}
 
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
 
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
 
//			if (st.getClassName().equals(this.getClass().getName())) {
//				continue;
//			}
 
			return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId()
					+ "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
			
		}
 
		return null;
	}
 
	private static String createMessage(String msg) {
		String functionName = getFunctionName();
		functionName = null;
		String message = (functionName == null ? msg : (functionName + " >>> " + msg));
		return message;
	}
	
	public static void d(String str){
		if(DEBUG){
			str = createMessage(str);
			Log.d(DEFAULT_TAG, str);
		}
	}
	
	public static void d(String tag ,String str){
		if(DEBUG){
			Log.d(DEFAULT_TAG, tag + " " +str);
		}
	}
	
	public static void e(String str){
		if(DEBUG){
			str = createMessage(str);
			Log.e(DEFAULT_TAG, str);
		}
	}
	
	public static void v(String str){
		if(DEBUG){
			str = createMessage(str);
			Log.v(DEFAULT_TAG, str);
		}
	}
	
	public static void i(String tag,String str){
		if(DEBUG){
			Log.i(DEFAULT_TAG, tag + " " + str);
		}
	}
}