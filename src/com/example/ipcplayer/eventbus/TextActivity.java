package com.example.ipcplayer.eventbus;

import com.example.ipcplayer.utils.LogUtil;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.ThreadMode;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TextActivity extends Activity{

	private static final int SHOW = 0;
	public Handler mHandler = new Handler(){
			public void handleMessage(Message msg){
				int what = msg.what;
				switch(what){
				case SHOW:
					LogUtil.d("show String ");
					break;
				default :
					break;
				}
			}
			};
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		EventBus.getDefault().register(this);
		
		EventBus.getDefault().register(this, "MainThread");
		
		Event.sendEvent();
		
	}
	
	public void onEvent(MyEvent event){
		
		String str = event.getString();
		LogUtil.d("event message str= " + str);
		mHandler.sendEmptyMessage(SHOW);
	}
	
}