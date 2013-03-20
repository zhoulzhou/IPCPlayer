package com.example.ipcplayer.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface IUICallBack{
	public void onShow(Fragment fragment, boolean saveToStack, Bundle data);
	public void onBack(Fragment fragment, Bundle data);
}