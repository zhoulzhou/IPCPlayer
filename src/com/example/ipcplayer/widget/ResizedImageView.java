package com.example.ipcplayer.widget;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class ResizedImageView extends ImageView {
	public ResizedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LogUtil.d("create image object");
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable d = getDrawable();
		LogUtil.d("widthMeasureSpec= " + widthMeasureSpec + " heightMeasureSpec" + heightMeasureSpec);

		if (d != null) {
			// ceil not round - avoid thin vertical gaps along the left/right
			// edges
			int width = MeasureSpec.getSize(widthMeasureSpec);
			LogUtil.d("width= " + width);
			int height = (int) Math.ceil((float) width
					* (float) d.getIntrinsicHeight()
					/ (float) d.getIntrinsicWidth());
			LogUtil.d("height= " + height);
		// TODO Auto-generated method stub
			setMeasuredDimension(width, height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}