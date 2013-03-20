package com.example.ipcplayer.controller;

import android.support.v4.app.Fragment;

import com.example.ipcplayer.localfragment.AllSongListFragment;

public class UICallBackController{
	
	public static void showAllSongListFragment(IUICallBack callback){
		Fragment fragment = new AllSongListFragment();
		callback.onShow(fragment, true, null);
	}
}