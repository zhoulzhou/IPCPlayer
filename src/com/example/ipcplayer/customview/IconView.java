package com.example.ipcplayer.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ipcplayer.R;
import com.example.ipcplayer.homeview.BaseHomeView;
import com.example.ipcplayer.utils.LogUtil;

//this view also can extends framelayout or linearlayout or relativelayout
public class IconView extends BaseHomeView{
	private static final String TAG = IconView.class.getSimpleName();
	private TextView tv;
	private ImageView iv;
	private LayoutInflater inflater;
	
	public IconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LogUtil.d(TAG + " IconView(Context context, AttributeSet attrs, int defStyle)");
		onCreateView(context,attrs);
	}

	public IconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LogUtil.d(TAG + " IconView(Context context, AttributeSet attrs)");
		onCreateView(context, attrs);
	}
	
	public IconView(Context context) {
		super(context);
		LogUtil.d(TAG + " IconView(Context context)");
		onCreateView(context,null);
	}

	protected void onCreateView(Context context, AttributeSet attrs) {
		LogUtil.d(TAG + "  onCreateView(Context context, AttributeSet attrs)");
		
		//pay attention to inflater(resId , rootView); 
		inflater.from(context).inflate(R.layout.icon_view, this);
		
		tv = (TextView) findViewById(R.id.text);
//		tv.setText(" customview successful	");
		iv = (ImageView) findViewById(R.id.image);
//		iv.setBackgroundResource(R.drawable.ic_launcher);
	}

	protected void onRelease() {
		
	}
	
}