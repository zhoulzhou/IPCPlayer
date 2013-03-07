package com.example.ipcplayer.service;

public class StreamPlayer{
	private int mPlayState = 0;
	private static final int STATE_PLAYING = 1;
	private static final int STATE_STOP = 3;
	private static final int STATE_PAUSE = 2;
	private static final int STATE_IDLE = 0;
	private static final int STATE_ERROR = -1;
	
	public boolean isPlaying(){
		return mPlayState == STATE_PLAYING;
	}
	
	public boolean isStop(){
		return mPlayState == STATE_STOP;
	}
	
	public boolean isPause(){
		return mPlayState == STATE_PAUSE;
	}
	
	public boolean isError(){
		return mPlayState == STATE_ERROR;
	}
	
	public boolean isIdle(){
		return mPlayState == STATE_IDLE;
	}
}