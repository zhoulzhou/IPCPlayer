package com.tencent.weibo.ui;

import com.example.ipcplayer.manager.LogicController;
import com.example.ipcplayer.setting.SettingManager;
import com.example.ipcplayer.setting.SettingManagerFactory;

import android.content.Context;
import android.os.Handler;

/**
 * @author licong
 * @version 1.0
 * @data 2012-10-10
 */
public class ShareTencentController extends LogicController {
	private SettingManager mSettingManager = null;
	
	/**
	 * @param context
	 * @param uiHandler
	 */
	public ShareTencentController(Context context, Handler uiHandler) {
		super(context, uiHandler);
		mSettingManager = (SettingManager) SettingManagerFactory.getComponent(context);
	}
	
	public void setAppForegroundTime(long addedTime) {
		if (addedTime > 0) {
			mSettingManager.setAppForegroundTime(
					mSettingManager.getAppForegroundTime() + addedTime);			
		}
	}
}