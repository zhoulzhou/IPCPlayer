package com.tencent.weibo.ui;

import com.example.ipcplayer.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingView extends RelativeLayout{
	private ImageView mLoadingProgress = null ;
	private ImageView mLoadingImageDefault = null ;
	private TextView mLoadingText = null ;
	private AnimationDrawable mAnimationLoadingDrawable = null ;

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs){
		super(context,attrs);
		init();
	}
	
	public LoadingView(Context context){
		super(context);
		init();
	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.loading, null);
		mLoadingProgress = (ImageView) viewGroup.findViewById(R.id.empty_progress_bar);
		mLoadingImageDefault = (ImageView) viewGroup.findViewById(R.id.empty_progress_bar);
		mLoadingText = (TextView) viewGroup.findViewById(R.id.empty_prompt_text_view); 
		mLoadingText.setText(R.string.pull_up_to_refresh_refreshing_label);
		mLoadingProgress.setBackgroundResource(R.drawable.refresh_loading);
		mAnimationLoadingDrawable = (AnimationDrawable) mLoadingProgress.getBackground();
	}
	
	public void setLoadingText(String text){
		mLoadingText.setText(text);
	}
	
	public void setLoadingTextVisible(int visibility){
		mLoadingText.setVisibility(visibility);
	}
	
	public void setDefaultImageViewVisible(int visibility){
		mLoadingImageDefault.setVisibility(visibility);
	}
	
	public void startLoading(){
		setVisibility(View.VISIBLE);
		if(mLoadingProgress.getVisibility() == View.INVISIBLE){
			mLoadingProgress.setVisibility(View.VISIBLE);
		}
		
		mLoadingText.setText(R.string.pull_up_to_refresh_refreshing_label);
		mLoadingText.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAnimationLoadingDrawable = (AnimationDrawable) mLoadingProgress.getBackground();
				mAnimationLoadingDrawable.start();
			}});
	}
	
	public void stopLoading(){
		setVisibility(View.GONE);
		if(mLoadingProgress.getVisibility() == View.VISIBLE){
			mLoadingProgress.setVisibility(View.GONE);
		}
		mLoadingText.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAnimationLoadingDrawable = (AnimationDrawable) mLoadingProgress.getBackground();
				mAnimationLoadingDrawable.stop();
			}});
	}
	
	public void onError(String errorString){
		mLoadingText.setText(errorString);
		mAnimationLoadingDrawable.stop();
		mLoadingProgress.setVisibility(View.GONE);
	}
}