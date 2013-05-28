package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class AnimationActivity extends Activity implements OnClickListener{
	private static final String TAG  = AnimationActivity.class.getSimpleName();
	private ImageView anima;
	private Button start, stop;
	private AnimationDrawable animaD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation);
		anima = (ImageView) findViewById(R.id.myAnimation);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		
		anima.setBackgroundResource(R.anim.anima);
		 
		animaD = (AnimationDrawable) anima.getBackground();
		
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.start:
			animaD.start();
			break;
		case R.id.stop:
			animaD.stop();
			break;
		default:
			
			break;
		}
	}
	
}