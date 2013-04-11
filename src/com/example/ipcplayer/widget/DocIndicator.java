package com.example.ipcplayer.widget;

import com.example.ipcplayer.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 帮助界面指示器
 * 
 * @author yuankai
 * @version 1.0
 * @date 2011-4-6
 */
public class DocIndicator extends View {
	private Drawable mDark;
	private Drawable mLight;
	private int mTotal;
	private int mCurrent;
	private int mDotWidth; // 每个点的宽度

	/**
	 * 
	 * @param context
	 *            context
	 */
	public DocIndicator(Context context) {
		this(context, null);
	}

	/**
	 * 
	 * @param context
	 *            context
	 * @param att
	 *            属性集
	 */
	public DocIndicator(Context context, AttributeSet att) {
		this(context, att, 0);
	}

	/**
	 * @param context
	 *            context
	 * @param att
	 *            属性集
	 * @param defStyle
	 *            默认风格
	 */
	public DocIndicator(Context context, AttributeSet att, int defStyle) {
		super(context, att, defStyle);
		TypedArray a = context.obtainStyledAttributes(att,
				R.styleable.DocIndicator);
		int lightImgId = a.getResourceId(R.styleable.DocIndicator_lightImage,
				-1);
		if (lightImgId != -1) {
			mLight = getContext().getResources().getDrawable(lightImgId);
		}

		int darkImgId = a.getResourceId(R.styleable.DocIndicator_darkImage, -1);
		if (darkImgId != -1) {
			mDark = getContext().getResources().getDrawable(darkImgId);
		}

		mDotWidth = a.getDimensionPixelSize(R.styleable.DocIndicator_dotWidth,
				14);
		mTotal = a.getInt(R.styleable.DocIndicator_total, 0);
		mCurrent = a.getInt(R.styleable.DocIndicator_current, 0);
	}

	/**
	 * 设置圆点图片资源
	 * 
	 * @param lightId
	 *            高亮图标
	 * @param darkId
	 *            普通图标
	 */
	public void setDotsImage(int lightId, int darkId) {
		mLight = getContext().getResources().getDrawable(lightId);
		mDark = getContext().getResources().getDrawable(darkId);
		postInvalidate();
	}

	/**
	 * 设置每个点宽度
	 * 
	 * @param width
	 *            宽度
	 */
	public void setDotWidth(int width) {
		mDotWidth = width;
	}

	/**
	 * 设置指示器圆点总数
	 * 
	 * @param total
	 *            圆点总数
	 */
	public void setTotal(int total) {
		mTotal = total;
		// this.set
		LayoutParams parmas = this.getLayoutParams();
		parmas.width = total * mDotWidth;
		this.setLayoutParams(parmas);
		postInvalidate();
	}

	/**
	 * 当前减
	 */
	public void decrease() {
		if (mTotal > 0) {
			if (mCurrent == 0) {
				mCurrent = mTotal - 1;
			} else {
				mCurrent--;
			}
		}

		postInvalidate();
	}

	/**
	 * 当前加
	 */
	public void increase() {
		if (mTotal > 0) {
			if (mCurrent == mTotal - 1) {
				mCurrent = 0;
			} else {
				mCurrent++;
			}
		}
		postInvalidate();
	}

	/**
	 * 设置当前圆点
	 * 
	 * @param current
	 *            当前圆点索引
	 */
	public void setCurrent(int current) {
		mCurrent = current;
		postInvalidate();
	}

	public int getCurrent() {
		return mCurrent;
	}

	public int getTotal() {
		return mTotal;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mTotal <= 0 || mLight == null || mDark == null) {
			return;
		}

		final int width = getWidth();
		final int height = getHeight();
		final int realWidth = mDotWidth * mTotal;
		int offset = (width - realWidth) / 2;

		for (int i = 0; i < mTotal; i++) {
			mDark.setBounds(offset, 0, offset + mDotWidth, height);
			mDark.draw(canvas);

			if (i == mCurrent) {
				mLight.setBounds(offset, 0, offset + mDotWidth, height);
				mLight.draw(canvas);
			}
			offset += mDotWidth;
		}
	}
}
