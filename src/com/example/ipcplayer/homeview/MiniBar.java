package com.example.ipcplayer.homeview;

import com.example.ipcplayer.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MiniBar extends BaseHomeView implements OnClickListener, OnTouchListener{
	private Context mContext;
	private LayoutInflater mInflater;
	
	private ImageView mMusicAlbumnIV;
	private TextView mMusicTitleTV;
	private ProgressBar mMusicProgressPB;
	private ImageButton mMusicPauseIB;
	private ImageButton mMusicNextIB;
	
	private MiniBarCallback mControlCallback;
	
	public MiniBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public MiniBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MiniBar(Context context) {
		super(context);
	}
	
	public static interface MiniBarCallback {

		public void onNextAction(MiniBar miniBar);

		public void onPlayAction(MiniBar miniBar);

		public void onClickAction(MiniBar miniBar);
	}

	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mInflater.inflate(R.layout.mini_bar, this,true);
		
		initViews();
	}
	
	private void initViews(){
		mMusicAlbumnIV = (ImageView) findViewById(R.id.music_image);
        mMusicAlbumnIV.setOnClickListener(this);
		mMusicTitleTV = (TextView) findViewById(R.id.music_name);
		mMusicProgressPB = (ProgressBar) findViewById(R.id.music_bar);
		mMusicPauseIB = (ImageButton) findViewById(R.id.music_pause);
		mMusicPauseIB.setOnClickListener(this);
		mMusicNextIB = (ImageButton) findViewById(R.id.music_next);
		mMusicNextIB.setOnClickListener(this);
	}
	
	public void setMiniBarCallBack(MiniBarCallback callBack){
		mControlCallback = callBack;
	}

	@Override
	protected void onRelease() {
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.music_image:
			if (mControlCallback != null) {
				mControlCallback.onClickAction(this);
			}
			break;
		case R.id.music_bar:
			if(mControlCallback != null){
				mControlCallback.onClickAction(this);
			}
			break;
		case R.id.music_pause:
			if(mControlCallback != null){
				mControlCallback.onPlayAction(this);
			}
			break;
		case R.id.music_next:
			if(mControlCallback != null){
				mControlCallback.onNextAction(this);
			}
			break;
		default :
			break ;
			
		}
	}
	
	public void onNextButtonClick(){
		
	}
	
	public void onControlButtonClick(){
		
	}
	
	public void onMiniBarClick(){
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
}