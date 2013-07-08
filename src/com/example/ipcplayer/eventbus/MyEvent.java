package com.example.ipcplayer.eventbus;

public class MyEvent {
	public String mString;
	
	public MyEvent(String string){
		mString = string;
	}
	
	public String getString(){
		return mString;
	}
	
	
}