package com.example.ipcplayer.service;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

public class LocalPlayer{
	private final static String TAG = LocalPlayer.class.getSimpleName();
	private int mPlayState = 0;
	private static final int STATE_PLAY = 1;
	private static final int STATE_STOP = 3;
	private static final int STATE_PAUSE = 2;
	private static final int STATE_IDLE = 0;
	private static final int STATE_ERROR = -1;
	
	private boolean mIsInitialized = false ;
	
	private Context mContext;
	private String mPath ;
	private MediaPlayer  mMediaPlayer = new MediaPlayer();
	
	public LocalPlayer(Context context){
		mContext = context;
		mMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
	}
	
	public boolean isPlaying(){
		return mPlayState == STATE_PLAY;
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
	
	public boolean isInitialized(){
		return mIsInitialized;
	}
	
	public void setPlayState(int state){
		mPlayState = state;
	}
	
	MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			
		}
	};
	
	MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}
	};
	
	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			
		}
	};
	
	MediaPlayer.OnSeekCompleteListener mSeekListener = new 	MediaPlayer.OnSeekCompleteListener() {
		
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			
		}
	};
	
	public void setDataSource(String path) {
		mPath = path;
		if (StringUtil.isEmpty(mPath)) {
			return;
		}

		try {
			mMediaPlayer.reset();
			mMediaPlayer.setOnPreparedListener(null);
			if (mPath.startsWith("content:\\")){
				mMediaPlayer.setDataSource(mContext, Uri.parse(mPath));
			}else {
				mMediaPlayer.setDataSource(mPath);
			}
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			mIsInitialized = false ;
			e.printStackTrace();
			return ;
		} catch (SecurityException e) {
			mIsInitialized = false ;
			e.printStackTrace();
			return ;
		} catch (IllegalStateException e) {
			mIsInitialized = false ;
			e.printStackTrace();
			return ;
		} catch (IOException e) {
			mIsInitialized = false ;	
			e.printStackTrace();
			return ;
		}
		mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
		mIsInitialized = true;
		setPlayState(STATE_IDLE);
	}
	
	public void pause() {
		mMediaPlayer.pause();
		setPlayState(STATE_PAUSE);
	}
	
	public void resume(){
		if(!isPause()){
			return ;
		}
		
		try {
			start();
		} catch (Exception e) {
			LogUtil.d(TAG + " resume exception e:");
			e.printStackTrace();
		}
	}
	
	public void start(){
		mMediaPlayer.start();
		setPlayState(STATE_PLAY);
	}
	
	public void stop(){
		mMediaPlayer.reset();
		mIsInitialized = false ;
		setPlayState(STATE_STOP);
	}
	
	public long seek(long position){
		mMediaPlayer.seekTo((int) position);
		start();
		return position;
	}
	
	public void setVolume(float vol){
		mMediaPlayer.setVolume(vol, vol);
	}
	
	
}