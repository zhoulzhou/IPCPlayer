package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.lyric.LyricView;
import com.example.ipcplayer.service.IPlayback;
import com.example.ipcplayer.service.PlaybackService;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.widget.CellLayout;
import com.example.ipcplayer.widget.DocIndicator;
import com.example.ipcplayer.widget.Workspace;
import com.example.ipcplayer.widget.Workspace.IWorkspaceListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.view.View;

public class PlayingActivity extends Activity implements View.OnClickListener, IWorkspaceListener
	{
    private static final String TAG = PlayingActivity.class.getSimpleName();
	private TextView titleTV,artistTV;
	private TextView currentTime,totalTime;
	private SeekBar seekBar;
	private Button preBtn;
	private Button playControlBtn;
	private Button nextBtn;
	private Workspace mPlayerWorkspace;
	private DocIndicator mDocIndicator;
	private LyricView mLyricView;
	
	public static final int NO_LYRIC = 1;
	public static final int SEARCH_LYRIC = 0;
	public static final int LYRIC_READY = 2;
	
	private String path;
	private LayoutInflater mInflater;
	private Context mContext;
	private final static int UPDATE = 1;
	private IPlayback service = null;
	
	
	private long duration;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case UPDATE:
				update();
				break;
			default :
				break;
			}
		}
			
    };
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing);
        
        mContext = PlayingActivity.this;
        mInflater = LayoutInflater.from(mContext);
        
        initViews();
        
        path = getIntent().getStringExtra("path");
        LogUtil.d(TAG + " path: " + path);
        
        Intent mServiceIntent = new Intent(IPlayback.class.getName());
        bindService(mServiceIntent,conn,BIND_AUTO_CREATE);
        Intent intent = new Intent(this,PlaybackService.class);
        startService(intent);
    }
    
    private void initViews(){
    	mPlayerWorkspace = (Workspace) findViewById(R.id.player_workspace);
        mDocIndicator = (DocIndicator) findViewById(R.id.player_indicator);
        CellLayout playerImage = (CellLayout) mInflater.inflate(R.layout.playing_item_1, null);
        CellLayout playerLyric = (CellLayout) mInflater.inflate(R.layout.playing_item_2, null);
        mLyricView = (LyricView) playerLyric.findViewById(R.id.player2_music_lyric2);
        mPlayerWorkspace.addView(playerImage);
        mPlayerWorkspace.addView(playerLyric);
        mPlayerWorkspace.setWorkspaceListener(this);
        mDocIndicator.setTotal(mPlayerWorkspace.getChildCount());
        mDocIndicator.setCurrent(mPlayerWorkspace.getCurrentScreen());
        titleTV = (TextView) findViewById(R.id.title);
        artistTV = (TextView) findViewById(R.id.artist);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(1000);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(seekListener);
        currentTime = (TextView) findViewById(R.id.currenttime);
        totalTime = (TextView) findViewById(R.id.totaltime);
        preBtn = (Button) findViewById(R.id.prebtn);
        playControlBtn = (Button) findViewById(R.id.playcontrolbtn);
        playControlBtn.setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.nextbtn);
    }
    
    @Override
	protected void onDestroy() {
		if (service != null) {
			try {
				service.stop();
//				service.release();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

//			mContext.unbindService(conn);
//			service = null;
		}
		
		if(handler.hasMessages(UPDATE)){
			handler.removeMessages(UPDATE);
		}
    	
    	super.onDestroy();
	}

	private OnSeekBarChangeListener seekListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			Log.v("main","seekbar progress = "+progress);
	           if(fromUser){
	        	   try {
					service.seekTo((int)(progress*duration/1000));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
	           }
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
		}
    	
    };
    
    private void update(){
    	try{
    		if(service != null && service.isPlaying()){
    			long pos = service.getCurrentTime();
//    			LogUtil.d(TAG + " position = " + pos);
    			seekBar.setProgress((int)(1000*pos/duration));
    			currentTime.setText(pos+"");
    			totalTime.setText(duration + "");
    		
    		updateTV();
    		LogUtil.d(TAG + " lyric ready update");
    		//暂时未获取歌词
//    		updateLyric(LYRIC_READY);
    		handler.sendMessageDelayed(handler.obtainMessage(UPDATE),300);
    		}
    	}catch(RemoteException e){
    		e.printStackTrace();
    	}
    }   
    
    private void updateTV(){
		try {
			if (service != null) {
				artistTV.setText(service.getArtist());
				titleTV.setText(service.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void updateLyric(int state){
    	if(mLyricView != null) {
    		LogUtil.d(TAG + " updateLyric");
    		mLyricView.updateLyric(state);
    	}
    }
    
    private ServiceConnection conn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = IPlayback.Stub.asInterface(binder);
			try {
//				service.setDataSource(path);
//				service.start();
				duration = service.getDuration();
				LogUtil.d(TAG + " duration = " + duration );
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			handler.sendMessage(handler.obtainMessage(UPDATE));
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
    	
    };

	@Override
	public void onClick(View view) {
		if(view == playControlBtn){
			try {
				if(service == null){
					Log.v("main"," service is null ");
					return ;
				}
				
				if(service.isPlaying()){
					service.pause();
				}else {
					service.start();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onUpdateTotalNum(int total) {
		
	}

	@Override
	public void onUpdateCurrent(int current) {
		
	}
	
	}
