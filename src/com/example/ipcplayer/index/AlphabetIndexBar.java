package com.example.ipcplayer.index;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义view------字母索引bar zds 3.21
 */

public class AlphabetIndexBar extends LinearLayout {

	private Context mContext = null;

	public final int ORIENTATION = LinearLayout.VERTICAL;

	private LinearLayout mLayoutIndex = null;
	private AlphabetIndexView[] mIndexViews = null;

	private AlphabetIndexView mCurrentView = null;

	private AlphabetIndexView mPreviousView = null;

	private ArrayList<OnAlphabetIndexBar> mCallbackList = null;

	public AlphabetIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;

		setContentView();
		setupViews();

	}

	/**
	 * setContentView
	 * 
	 * @param layoutId
	 * @param lp
	 */
	public void setContentView() {

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		LinearLayout layout = (LinearLayout) ((Activity) mContext)
				.getLayoutInflater().inflate(R.layout.alphabetindexbar, null);

		lp.gravity = Gravity.CENTER;

		this.addView(layout, lp);
	}

	/**
	 * setupViews
	 */
	private void setupViews() {
		mLayoutIndex = (LinearLayout) this.findViewById(R.id.layout_index);
		mLayoutIndex.setOrientation(ORIENTATION);

		mLayoutIndex.setOnTouchListener(layoutIndexTouchListener);
	}

	/**
	 * initialization
	 * 
	 * @param weights
	 */
	public void initialization(List<String> tags) {
		final int length = tags.size();

		mLayoutIndex.removeAllViews();

		mIndexViews = new AlphabetIndexView[length];

		LinearLayout.LayoutParams lp = null;
		for (int i = 0; i < length; i++) {
			AlphabetIndexView indexView = new AlphabetIndexView(mContext,
					tags.get(i));
			lp = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			lp.gravity = Gravity.CENTER;

			mLayoutIndex.addView(indexView, lp);

			mIndexViews[i] = indexView;
		}
	}

	/**
	 * setCurrentItem
	 * 
	 * @param currentTag
	 */
	public void setCurrentItem(String currentTag) {

		if (!isShown())
			return;

		// 相同的不处理
		if (mCurrentView != null) {
			String temp = mCurrentView.getTagText();
			if (currentTag != null && currentTag.equals(temp)) {
				return;
			}
		}
		if (mIndexViews == null)
			return;

		int length = mIndexViews.length;

		AlphabetIndexView indexView = null;

		// 找到tag对应的indexview
		for (int i = 0; i < length; i++) {
			indexView = mIndexViews[i];

			String tag = indexView.getTagText();
			if (tag != null && currentTag.equals(tag)) {
				break;
			}
		}

		if (indexView == null) {
			return;
		}

		mPreviousView = mCurrentView;
		mCurrentView = indexView;

		if (mPreviousView != null)
			mPreviousView.setNormalColor();
		if (mCurrentView != null)
			mCurrentView.setSelectedColor();

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		resetIndex();
	}

	private OnTouchListener layoutIndexTouchListener = new RelativeLayout.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final int touchX = (int) event.getX();
			final int touchY = (int) event.getY();

			AlphabetIndexView indexView = getIndexView(touchX, touchY);

			indexView = (indexView == null) ? mCurrentView : indexView;

			if (indexView != null) {
				onIndexBarTouch(indexView, event);

				if (indexView != mCurrentView) {
					// 回调
					onIndexChange(indexView);
					// 设置选中颜色
					setCurrentItem(indexView.getTagText());
				}

				// do nothing now...
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_OUTSIDE:
					break;
				}
			}

			return true;
		}
	};

	/**
	 * getIndexView
	 * 
	 * @param index
	 * @return
	 */
	private AlphabetIndexView getIndexView(int index) {
		
		AlphabetIndexView ret = null;
		
		if(mIndexViews == null) return ret ;
		
		int length = mIndexViews.length;

		for (int i = 0; i < length; i++) {
			AlphabetIndexView indexView = mIndexViews[i];

			if (indexView.getIndex() == index) {
				ret = indexView;
				break;
			}
		}

		return ret;
	}

	/**
	 * getIndexView
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	@SuppressWarnings("unused")
	private AlphabetIndexView getIndexView(float touchX, float touchY) {
		AlphabetIndexView ret = null;
		
		if(mIndexViews == null) return ret ;
		
		int length = mIndexViews.length;

		for (int i = 0; i < length; i++) {
			AlphabetIndexView indexView = mIndexViews[i];
			if(indexView == null) continue;
			
			if (ORIENTATION == LinearLayout.HORIZONTAL) {
				if ((indexView.getIndex() - indexView.getWidth() / 2) <= touchX
						&& touchX < (indexView.getIndex() + indexView
								.getWidth() / 2)) {
					ret = indexView;
					break;
				}
			} else if (ORIENTATION == LinearLayout.VERTICAL) {
				if ((indexView.getIndex() - indexView.getHeight() / 2) <= touchY
						&& touchY < (indexView.getIndex() + indexView
								.getHeight() / 2)) {
					ret = indexView;
					break;
				}
			}
		}

		return ret;
	}

	public AlphabetIndexView[] getIndexViews() {
		return mIndexViews;
	}

	/**
	 * resetIndex
	 */
	@SuppressWarnings("unused")
	public void resetIndex() {

		if (mIndexViews == null)
			return;

		final int length = mIndexViews.length;
		int margin = 0;

		for (int i = 0; i < length; i++) {
			if (ORIENTATION == LinearLayout.HORIZONTAL) {
				margin += mIndexViews[i].getWidth();
				mIndexViews[i].setIndex(margin - mIndexViews[i].getWidth() / 2);
			} else if (ORIENTATION == LinearLayout.VERTICAL) {
				margin += mIndexViews[i].getHeight();
				mIndexViews[i]
						.setIndex(margin - mIndexViews[i].getHeight() / 2);
			}
		}
	}

	public void onTouch(MotionEvent event) {
		
		if(!isShown()) return;
		
		if (layoutIndexTouchListener != null) {
			layoutIndexTouchListener.onTouch(this, event);
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////
	//
	// interface
	// 字母索引bar回调
	// zds
	//
	// //////////////////////////////////////////////////////////////////////////////////

	public interface OnAlphabetIndexBar {
		void onIndexBarTouch(AlphabetIndexView indexView, MotionEvent event);

		void onIndexChange(AlphabetIndexView indexView);
	};

	public boolean registerCallback(OnAlphabetIndexBar callback) {
		boolean ret = false;

		if (mCallbackList == null) {
			mCallbackList = new ArrayList<OnAlphabetIndexBar>();
		}

		ret = mCallbackList.add(callback);
		return ret;
	}

	public boolean unregisterCallback(OnAlphabetIndexBar callback) {
		boolean ret = false;

		if (mCallbackList != null) {
			ret = mCallbackList.remove(mCallbackList);
		}
		return ret;
	}

	private void onIndexBarTouch(AlphabetIndexView indexView, MotionEvent event) {

		if (mCallbackList != null) {
			int size = mCallbackList.size();
			for (int i = 0; i < size; i++) {
				mCallbackList.get(i).onIndexBarTouch(indexView, event);
			}
		}
	}

	private void onIndexChange(AlphabetIndexView indexView) {

		if (mCallbackList != null) {
			int size = mCallbackList.size();
			for (int i = 0; i < size; i++) {
				mCallbackList.get(i).onIndexChange(indexView);
			}
		}
	}

}
