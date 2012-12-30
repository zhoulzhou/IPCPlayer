package com.example.ipcplayer.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import com.example.ipcplayer.R;
import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.provider.MusicDBManager;

public class MainDbActivity extends Activity{
	private MusicDBManager mDBManager ;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);
        
        mDBManager = new MusicDBManager(this);
        mDBManager.insertLocalData();
	}
	
}
