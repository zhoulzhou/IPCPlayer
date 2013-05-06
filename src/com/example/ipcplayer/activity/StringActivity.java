package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class StringActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyric_view);
		
		String demo = "[00:25.66]我实在不愿轻易让眼泪留下";
		stringconvert(demo);
		
		String demo1 = "[00:15:00]";
		stringconvert(demo1);
		
		String demo2 = "00:25.66";
		timeConvert(demo2);
		
		String demo3 = "22:33.44";
		longConvert(demo3);
		
	}
	
	private void stringconvert(String demo)
	{
		int left = demo.indexOf("[");
		System.out.println("left= " + left);
		int right = demo.indexOf("]");
		System.out.println("right= " + right);
		
		String time = demo.substring(left+1, right);
		System.out.println("Time= " + time);
		
		String content = demo.substring(right+1);
		System.out.println("COntent= " + content);
	}
	
	private void timeConvert(String time){
		int index = time.indexOf(".");
		System.out.println("index= " + index);
		String t1 = time.substring(0,index);
		System.out.println("t1= " + t1);
		
	}
	
	private void longConvert(String time){
		System.out.println("time= " + time);
		int index = time.indexOf(":");
		System.out.println("index= " + index);
		int index1 = time.indexOf(".");
		System.out.println("index1= " + index1);
		String min = time.substring(0,index);
		System.out.println("min= " + min);
		String sec = time.substring(index+1,index1);
		System.out.println("sec= " + sec);
		String sec1 = time.substring(index1+1);
		System.out.println("sec1= " + sec1);
		
		
		long min1 = Long.parseLong(min);
		System.out.println("min1= " + min1);
		long sec2 = Long.parseLong(sec);
		System.out.println("sec2= " + sec2);
		long sec3 = Long.parseLong(sec1);
		System.out.println("sec3= " + sec3);
		long time1 = min1*60 + sec2 + sec3;
		System.out.println("time1= " + time1);
		
	}
	
}