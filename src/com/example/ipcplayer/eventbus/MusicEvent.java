package com.example.ipcplayer.eventbus;

public class MusicEvent{
	private long id ;
	private String path ;
	private static MusicEvent mInstance;
	
	public static MusicEvent getInstance(){
		if(mInstance == null){
			mInstance = new MusicEvent();
		}
		return mInstance;
	}
	
	public MusicEvent setId(long id){
		this.id = id;
		return mInstance;
	}
	
	public long getId(){
		return id;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
}