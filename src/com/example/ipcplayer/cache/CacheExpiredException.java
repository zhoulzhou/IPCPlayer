package com.example.ipcplayer.cache;

@SuppressWarnings("serial")
public class CacheExpiredException extends Exception{

	@Override
	public String getMessage() {
		return " cache has exspired ... ";
	}
	
}