package com.example.ipcplayer.homeview;

import com.example.ipcplayer.R;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicHomeCenterItemView extends BaseHomeView{
	private ViewGroup mTopView;
	private ViewGroup mCenterView;
	
	private TextView mTopTv;
	private ImageView mCenterIv;
	private TextView mCenterTv;

	public MusicHomeCenterItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public MusicHomeCenterItemView(Context context, AttributeSet attrs){
		super(context,attrs);
	}
	
	public MusicHomeCenterItemView(Context context){
		super(context);
	}

	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		View v = LayoutInflater.from(context).inflate(R.layout.music_home_center_item_view, this);
		setClickable(true);
		setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
		
		mTopView = (ViewGroup) v.findViewById(R.id.home_center_item_top);
		mCenterView = (ViewGroup) v.findViewById(R.id.home_center_item_center);
		
		mTopTv = (TextView) v.findViewById(R.id.home_center_item_info);
		mCenterIv = (ImageView) v.findViewById(R.id.home_center_item_image);
		mCenterTv = (TextView) v.findViewById(R.id.home_center_item_name);
		setFakeBold(mCenterTv);
	}

	@Override
	protected void onRelease() {
		
	}
	
	public void setTitle(String title){
		mCenterTv.setText(title);
	}
	
	public void setInfo(String info){
		mTopTv.setText(info);
	}
	
	public void setTitle(int resId){
		mCenterTv.setText(getResources().getString(resId));
	}
	
	public void setInfo(int resId){
		mTopTv.setText(getResources().getString(resId));
	}
	
	public void setIcon(int resId){
		mCenterIv.setImageResource(resId);
	}
	
	private static void setFakeBold(final TextView tv){
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(true);
	}
}