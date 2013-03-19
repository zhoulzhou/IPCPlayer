package com.example.ipcplayer.homeview;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class BaseHomeView extends FrameLayout{
	private static final String TAG = BaseHomeView.class.getSimpleName();

	public BaseHomeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public BaseHomeView(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	
	public BaseHomeView(Context context){
		super(context);
	}
	
	public final void Release(){
		onRelease();
	}
	
	private void initializeViews(Context context, AttributeSet attrs) {
		LogUtil.d(TAG + " initializeViews()");
		onCreateView(context, attrs);
	}

	/**
	 *  子类覆写此方法，执行View初始化工作
	 * @param context
	 * @param attrs
	 */
	protected abstract void onCreateView(Context context, AttributeSet attrs);

	protected void onPause() {
		LogUtil.d(TAG + " onPause() ");
	}

	protected void onResume() {
		LogUtil.d(TAG + " onResume() ");
	}

	protected void onStop() {
		LogUtil.d(TAG + " onStop() ");
	}

	protected void onStart() {
		LogUtil.d(TAG + " onStart() ");
	}

	/**
	 *  子类覆写此方法，执行资源释放工作
	 */
	protected abstract void onRelease();
}