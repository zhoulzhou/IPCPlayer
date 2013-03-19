package com.example.ipcplayer.homeview;

import android.content.Context;
import android.util.AttributeSet;

public class LocalMainView extends BaseHomeView{


	public LocalMainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public LocalMainView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public LocalMainView(Context context){
		super(context);
	}

	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		
	}

	@Override
	protected void onRelease() {
		
	}
	
}