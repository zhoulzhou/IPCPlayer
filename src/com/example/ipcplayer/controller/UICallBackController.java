package com.example.ipcplayer.controller;

import android.support.v4.app.Fragment;

import com.example.ipcplayer.localfragment.AllSongListFragment;
import com.example.ipcplayer.module.SampleFragment;

public class UICallBackController{
	
	public static void showAllSongListFragment(IUICallBack callback){
		Fragment fragment = new SampleFragment();
		callback.onShow(fragment, true, null);
	}
	
	public static void showSpecialObjectFragment(IUICallBack callback){
		
	}
}