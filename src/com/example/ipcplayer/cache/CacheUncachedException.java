package com.example.ipcplayer.cache;

@SuppressWarnings("serial")
public class CacheUncachedException extends Exception{

	@Override
	public String getMessage() {
		return " Data not cached ! ... ";
	}
	
}