package com.example.ipcplayer.homeview;

import com.example.ipcplayer.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicHomeView extends BaseHomeView{
	private TextView mHomeTopTv;
	private MusicHomeCenterView mHomeCenterView;
	private ImageView mHomeBottomIv;

	public MusicHomeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MusicHomeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MusicHomeView(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		LayoutInflater inflate = LayoutInflater.from(context);
		inflate.inflate(R.layout.music_home_view, null);
		
		initViews();
	}

	private void initViews() {
		mHomeTopTv = (TextView) findViewById(R.id.home_top_view);
		mHomeCenterView = (MusicHomeCenterView) findViewById(R.id.home_center_view);
		mHomeBottomIv = (ImageView) findViewById(R.id.home_bottom_view);
	}

	@Override
	protected void onRelease() {
		
	}
	
}