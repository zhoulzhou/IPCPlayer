package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class DownloadActivity extends Activity{

	TextView mFileNameTv;
	TextView mUrlTv;
	SeekBar seekBar;
	Button mPauseBtn;
	Button mCancelBtn;
	TextView mTotalSizeTv;
	TextView mDownloadSizeTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		
		mFileNameTv = (TextView) findViewById(R.id.filename);
		mUrlTv = (TextView) findViewById(R.id.url);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		mPauseBtn = (Button) findViewById(R.id.pausebtn);
		mCancelBtn = (Button) findViewById(R.id.cancelbtn);
		mTotalSizeTv = (TextView) findViewById(R.id.totalsize);
		mDownloadSizeTv = (TextView) findViewById(R.id.downloadsize);
		
	}
	
}