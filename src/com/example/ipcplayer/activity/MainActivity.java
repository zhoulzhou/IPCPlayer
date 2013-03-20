package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.controller.IUICallBack;
import com.example.ipcplayer.localfragment.HomeFragment;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends BaseFragmentActivity implements IUICallBack{
	private static final String TAG = MainActivity.class.getSimpleName();
	private FrameLayout mHome_Container;
	private View mBottom_View;
	
	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	    setContentView(R.layout.main);
	    
	    initView();
	    initManagers();
	    initHomeFragment();
	}
	
	private void initView(){
		 mHome_Container = (FrameLayout) findViewById(R.id.home_container);
		 mBottom_View = (View) findViewById(R.id.bottom_view);
	}
	
	private void initManagers(){
		mFragmentManager = getSupportFragmentManager();
	}

	private void initHomeFragment() {
		FragmentTransaction fragmentTransaction = mFragmentManager
				.beginTransaction();
		HomeFragment homeFragment = new HomeFragment();
		fragmentTransaction.add(R.id.home_container, homeFragment);
//		fragmentTransaction.addToBackStack(homeFragment.getClass()
//				.getSimpleName());
		fragmentTransaction.commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		super.startActivityFromFragment(fragment, intent, requestCode);
	}

	@Override
	public void onShow(Fragment fragment, boolean saveToStack, Bundle data) {
		doShowAction(fragment, saveToStack,data);
	}

	@Override
	public void onBack(Fragment fragment, Bundle data) {
		
	}
	
	private void doShowAction(Fragment fragment, boolean saveToStack, Bundle data){
		LogUtil.d(TAG + " doShowAction fragment: " + fragment.toString());
		
		if(fragment == null){
			LogUtil.d(TAG + " doShowAction fragment is null " );
		}
		
		if(data != null){
			Bundle extras = fragment.getArguments();
			extras.putAll(data);
		}
		
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		
		if(saveToStack){
			ft.replace(R.id.home_container, fragment, fragment.getClass().getSimpleName());
			ft.addToBackStack(fragment.getClass().getSimpleName());
		}else{
			ft.replace(R.id.home_container, fragment, fragment.getClass().getSimpleName());
			ft.addToBackStack(null);
		}
		
		ft.commitAllowingStateLoss();
	}
}