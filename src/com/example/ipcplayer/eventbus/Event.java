package com.example.ipcplayer.eventbus;

import de.greenrobot.event.EventBus;

public class Event{
	
	public static void sendEvent(){
		EventBus.getDefault().post(new MyEvent(" something happened ！"));
	}
}