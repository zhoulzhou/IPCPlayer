package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.service.IPlayback;
import com.example.ipcplayer.service.PlaybackService;
import com.example.ipcplayer.utils.LogUtil;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.view.View;

public class PlayingActivity extends Activity implements View.OnClickListener
	{
    private static final String TAG = PlayingActivity.class.getSimpleName();
	private TextView titleTV,artistTV;
	private TextView currentTime,totalTime;
	private SeekBar seekBar;
	private Button preBtn;
	private Button playControlBtn;
	private Button nextBtn;
	private String path;
	
	private Context mContext;
	private final static int UPDATE = 1;
	private IPlayback service = null;
	
	private long duration;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
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
        
        path = getIntent().getStringExtra("path");
        LogUtil.d(TAG + " path: " + path);
        
        Intent mServiceIntent = new Intent(IPlayback.class.getName());
        bindService(mServiceIntent,conn,BIND_AUTO_CREATE);
        Intent intent = new Intent(this,PlaybackService.class);
        startService(intent);
    }
    
    @Override
	protected void onDestroy() {
		if (service != null) {
			try {
				service.stop();
				service.release();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			mContext.unbindService(conn);
			service = null;
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           }
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}
    	
    };
    
    private void update(){
    	try{
    		if(service != null){
    			long pos = service.getCurrentTime();
//    			LogUtil.d(TAG + " position = " + pos);
    			seekBar.setProgress((int)(1000*pos/duration));
    			currentTime.setText(pos+"");
    			totalTime.setText(duration + "");
    			
    		}
    		handler.sendMessageDelayed(handler.obtainMessage(UPDATE),300);
    	}catch(RemoteException e){
    		e.printStackTrace();
    	}
    }   
    
    private ServiceConnection conn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			// TODO Auto-generated method stub
			service = IPlayback.Stub.asInterface(binder);
			try {
				service.setDataSource(path);
				service.start();
				duration = service.getDuration();
				LogUtil.d(TAG + " duration = " + duration );
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendMessage(handler.obtainMessage(UPDATE));
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			service = null;
		}
    	
    };

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	
	}
