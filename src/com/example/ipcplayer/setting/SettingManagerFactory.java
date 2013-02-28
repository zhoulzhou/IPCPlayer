package com.example.ipcplayer.setting;

import android.content.Context;

public class SettingManagerFactory extends ComFactory{
	private static ComInterface sCom;
	
	public synchronized static ComInterface getComponent(Context context){
		if(context != null){
			if(sCom == null){
				sCom = new SettingManagerImpl(context);
			}
			return sCom;
		}else {
			return null;
		}

	}
}