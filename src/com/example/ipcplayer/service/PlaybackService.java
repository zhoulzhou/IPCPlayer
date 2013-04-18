package com.example.ipcplayer.service;

import java.lang.ref.WeakReference;

import com.example.ipcplayer.R;
import com.example.ipcplayer.convert.ConvertToMusicFile;
import com.example.ipcplayer.object.MusicFile;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class PlaybackService extends Service{
    private static final String TAG = PlaybackService.class.getSimpleName();
	private LocalPlayer mLocalPlayer;
	
	private int mPlayState = 0;
	private static final int STATE_PLAY = 1;
	private static final int STATE_PAUSE = 2;
	private static final int STATE_STOP = 3;
	private static final int STATE_IDLE = 0;
	private static final int STATE_ERROR = -1;
	
	private static final int REFRESH_LYRIC = 1;
	
	private MusicFile mMusicFile = new MusicFile();
	
	private static final int LYRIC_NO_DATA = 0;
	private static final int LYRIC_LOADING = 1;
	private static final int LYRIC_READY = 2;
	private int mLyricState = LYRIC_NO_DATA;
	
	public boolean isPlaying(){
		return mPlayState == STATE_PLAY;
	}
	
	public boolean isStop(){
		return mPlayState == STATE_STOP;
	}
	
	public boolean isPaused(){
		return mPlayState == STATE_PAUSE;
	}
	
	public boolean isError(){
		return mPlayState == STATE_ERROR;
	}
	
	public boolean isIdle(){
		return mPlayState == STATE_IDLE;
	}
	
	public void setPlayState(int state){
		mPlayState = state;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		initPlayers();
	}
	
	private void initPlayers(){
		mLocalPlayer = new LocalPlayer(this);
//		try {
//			mLocalPlayer.setDataSource(R.raw.believe);
//		} catch (Exception e) {
//			LogUtil.d(TAG + " setDataSource exception e: " );
//			e.printStackTrace();
//		}
		mLocalPlayer.setOnCompletionListener(mOnCompleteListener);
		mLocalPlayer.setOnErrorListener(mOnErrorListener);
		mLocalPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
		mLocalPlayer.setOnPreparedListener(mOnPreparedListener);
	}

	private LocalPlayer.OnCompletionListener mOnCompleteListener = new LocalPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion() {
			next();
		}
	};
	
	private LocalPlayer.OnErrorListener mOnErrorListener = new LocalPlayer.OnErrorListener() {
		
		@Override
		public void onError() {
			
		}
	};
		
	private LocalPlayer.OnPreparedListener mOnPreparedListener = new LocalPlayer.OnPreparedListener() {
		
		@Override
		public void onPrepared() {
			start();
		}
	};
	
	private LocalPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new LocalPlayer.OnSeekCompleteListener() {
		
		@Override
		public void onSeekComplete() {
			
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
	
	public void setDataSource(String path){
		if(mLocalPlayer == null){
			return ;
		}
		
		mLocalPlayer.setDataSource(path);
	}
	
	public void openCurrent(long id){
		if(id <= 0){
			return ;
		}
		mMusicFile = ConvertToMusicFile.getInstance(this).idToMusicFile(id);
		if(mMusicFile == null){
			LogUtil.d(TAG + " MusicFile is null");
			return ;
		}
		String path = mMusicFile.getPath();
		setDataSource(path);
		loadMusicLyric(false);
	}
	
	public void start(){
		if(mLocalPlayer == null){
			return ;
		}
		if (mLocalPlayer.isInitialized()) {
			mLocalPlayer.start();
			setPlayState(STATE_PLAY);
		}else {
			setPlayState(STATE_ERROR);
		}
		
	}

	public void pause(){
		if(mLocalPlayer == null){
			return ;
		}
		mLocalPlayer.pause();
		setPlayState(STATE_PAUSE);
	}

	public void stop(){
		if(mLocalPlayer == null){
			return ;
		}
		mLocalPlayer.stop();
		setPlayState(STATE_STOP);
	}

	public void release(){
		if(mLocalPlayer == null){
			return ;
		}
		mLocalPlayer.release();
	}

	public void previous(){
		
	}

	public void next(){
		
	}

	public int getId(){
		return 0;
	}

	public String getTitle(){
		return null;
	}

	public String getArtist(){
		return null;
	}

	public String getAlbumn(){
		return null;
	}

	public long getDuration(){
		long duration = 0;
		if(mLocalPlayer == null){
			LogUtil.d(TAG + " getDuration: mLocalPlayer is null");
			return 0;
		}
		if(!mLocalPlayer.isInitialized()){
			LogUtil.d(TAG + " getDuration: mLocalPlayer is not initialized");
			return 0;
		}
		try{
			duration = mLocalPlayer.getDuration();
		}catch(Exception e){
			LogUtil.d(TAG + " getDuration exception: ");
			e.printStackTrace();
		}
		return duration;
	}

	public long getCurrentTime(){
		if(mLocalPlayer == null){
			return 0;
		}
		if(!mLocalPlayer.isInitialized()){
			return 0;
		}
		return mLocalPlayer.getCurrentPosition();
	}

	public void seekTo(long position){
		if(mLocalPlayer == null){
			return ;
		}
		if(!mLocalPlayer.isInitialized()){
			return ;
		}
		mLocalPlayer.seek(position);
	}
	
	public void loadMusicLyric(boolean isOnline) {
		if(mMusicFile == null){
			LogUtil.d(TAG + " loadMusicLyric  MusicFile is null");
			return ;
		}
		
		mLyricState = LYRIC_LOADING;
		notifyChange(REFRESH_LYRIC);
		
		
	}
	
	private void notifyChange(int what){
		
	}
	
	
	private Binder binder = new ServiceStub(this);
    private final class ServiceStub extends IPlayback.Stub{
    	WeakReference<PlaybackService> mService;
    	
    	public ServiceStub(PlaybackService service){
    		mService = new WeakReference<PlaybackService>(service);
    	}

		@Override
		public void start(){
			mService.get().start();
		}

		@Override
		public void pause(){
			mService.get().pause();
		}

		@Override
		public void stop(){
			mService.get().stop();
		}

		@Override
		public void release(){
			mService.get().release();
		}

		@Override
		public void previous(){
			mService.get().previous();
		}

		@Override
		public void next(){
			mService.get().next();
		}

		@Override
		public int getId(){
			return mService.get().getId();
		}

		@Override
		public String getTitle(){
			return mService.get().getTitle();
		}

		@Override
		public String getArtist(){
			return mService.get().getArtist();
		}

		@Override
		public String getAlbumn(){
			return mService.get().getAlbumn();
		}

		@Override
		public long getDuration(){
			return mService.get().getDuration();
		}

		@Override
		public long getCurrentTime(){
			return mService.get().getCurrentTime();
		}

		@Override
		public boolean isPlaying(){
			return mService.get().isPlaying();
		}

		@Override
		public boolean isPaused(){
			return mService.get().isPaused();
		}

		@Override
		public void seekTo(long position) throws RemoteException {
			mService.get().seekTo(position);
			
		}

		@Override
		public void setDataSource(String path) throws RemoteException {
	        mService.get().setDataSource(path);		
		}

		@Override
		public void loadMusicLyric(boolean isOnline) throws RemoteException {
			mService.get().loadMusicLyric(isOnline);
		}

		@Override
		public void openCurrent(long id) throws RemoteException {
			mService.get().openCurrent(id);
		}
	
	}
	

}