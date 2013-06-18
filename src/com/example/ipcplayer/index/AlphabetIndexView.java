package com.example.ipcplayer.index;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * 自定义view------字母索引bar里的单元view  比如说字母 "A"
 */

public class AlphabetIndexView extends LinearLayout {

	private Context 		mContext = null;

	// index 该单元view在屏幕的height作为索引
	private int 			index = 0;
	private TextView 		tvTag = null;


	public AlphabetIndexView(Context context, String tag) {
		super(context);
		
		mContext = context;
		setTag(tag);

		setContentView();
		setupViews();
	}

	
	/**
	 * setupViews
	 */
	private void setupViews() {
		
		tvTag = (TextView) this.findViewById(R.id.tv_tag);
		tvTag.setText(this.getTag().toString());
		tvTag.setGravity(Gravity.CENTER);
	}


	
	/**
	 * setContentView
	 */
	public void setContentView() {
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT);

		LinearLayout layout = (LinearLayout) ((Activity) mContext)
				.getLayoutInflater().inflate(R.layout.alphabetindexview, null);
		
		lp.gravity = Gravity.CENTER;
	
		this.addView(layout, lp);
	}


	/**
	 * setIndex
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	/**
	 * getIndex
	 */
	public int getIndex() {
		return index;
	}
	
	
	public TextView getTvTag(){
		return  tvTag;
	}
	
	
	/**
	 * 取得tag文本
	 * @return
	 */
	public String getTagText(){
		if(tvTag != null){
			return tvTag.getText().toString();
		}
		return null;
	}

	
	/**
	 * 设置选中状态的字体颜色
	 */
	public void setSelectedColor(){
		tvTag.setTextColor(0xff279de7);
	}
	
	/**
	 * 设置正常状态的字体颜色
	 */
	public void setNormalColor(){
		tvTag.setTextColor(0xffa3a3b1);
	}
	
	/**
	 * 设置透明状态的字体颜色
	 */
	public void setTransColor(){
		tvTag.setTextColor(0x00ffffff);
	}
	
}
