package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.animation.RotateAndTranslateAnimation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class AnimationActivity extends Activity implements OnClickListener{
	private static final String TAG  = AnimationActivity.class.getSimpleName();
	private ImageView anima;
	private Button start, stop;
	private AnimationDrawable animaD;
	private Animation mAnima;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation1);
		anima = (ImageView) findViewById(R.id.myAnimation);
		anima.setBackgroundResource(R.drawable.runner);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		
//		anima.setBackgroundResource(R.anim.anima);
//		animaD = (AnimationDrawable) anima.getBackground();
		
//		mAnima = new AnimationUtils().loadAnimation(this, R.anim.rotate_anima);
		
		mAnima = new RotateAndTranslateAnimation(0, 300, 0, 300, 0, 300);
		mAnima.setDuration(5000);
		
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
			anima.startAnimation(mAnima);
			break;
		case R.id.stop:
			
			break;
		default:
			
			break;
		}
	}
	
}