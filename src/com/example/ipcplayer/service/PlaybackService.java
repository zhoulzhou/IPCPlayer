package com.example.ipcplayer.service;

import com.example.ipcplayer.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class PlaybackService extends Service{

	private int state = IDLE;
	private static final int IDLE = 0;
	private static final int PLAYING = 1;
	private static final int PAUSE = 2;
	private static final int STOP = 3;
	private MediaPlayer mPlayer;
	
	private int mPlayState = 0;
	private static final int STATE_PLAYING = 1;
	private static final int STATE_PAUSE = 2;
	private static final int STATE_STOP = 3;
	private static final int STATE_IDLE = 0;
	private static final int STATE_ERROR = -1;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mPlayer = MediaPlayer.create(this,R.raw.believe);
		mPlayer.setOnCompletionListener(completeListener);
		mPlayer.setOnErrorListener(errorListener);
		mPlayer.setOnPreparedListener(prepareListener);
		mPlayer.setOnSeekCompleteListener(seekListener);
	}

	private MediaPlayer.OnCompletionListener completeListener = new MediaPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			
		}
	};
		
	private MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}
	};
	
	private MediaPlayer.OnPreparedListener prepareListener = new MediaPlayer.OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			
		}
	};
	
	private MediaPlayer.OnSeekCompleteListener seekListener = new MediaPlayer.OnSeekCompleteListener() {
		
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			
		}
	};
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	
	private Binder binder = new IPlayback.Stub() {
		
		@Override
		public void stop() throws RemoteException {
			mPlayer.stop();
			state = STOP;
		}
		
		@Override
		public void start() throws RemoteException {
			if(state == STOP){
				try{
					mPlayer.prepare();
					mPlayer.start();
					state = PLAYING;
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				mPlayer.start();
				state = PLAYING;
			}
		}
		
		@Override
		public void release() throws RemoteException {
			mPlayer.release();
			mPlayer = null;
			state = IDLE;
		}
		
		@Override
		public void previous() throws RemoteException {
			
		}
		
		@Override
		public void pause() throws RemoteException {
			if(mPlayer.isPlaying()){
				mPlayer.pause();
				state = PAUSE;
			}
		}
		
		@Override
		public void next() throws RemoteException {
			
		}
		
		@Override
		public boolean isPlaying() throws RemoteException {
			return mPlayer.isPlaying();
		}
		
		@Override
		public boolean isPaused() throws RemoteException {
			return false;
		}
		
		@Override
		public String getTitle() throws RemoteException {
			return null;
		}
		
		@Override
		public int getId() throws RemoteException {
			return 0;
		}
		
		@Override
		public int getDuration() throws RemoteException {
			return mPlayer.getDuration();
		}
		
		@Override
		public String getArtist() throws RemoteException {
			return null;
		}
		
		@Override
		public String getAlbumn() throws RemoteException {
			return null;
		}

		@Override
		public int getCurrentTime() throws RemoteException {
			return mPlayer.getCurrentPosition();
		}

		@Override
		public void seekTo(int position) throws RemoteException {
			mPlayer.seekTo(position);
		}
	};

}