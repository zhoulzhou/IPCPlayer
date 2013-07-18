package com.example.ipcplayer.widget;

import java.util.ArrayList;

import com.example.ipcplayer.utils.OutOfMemoryHandle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 普通模式下的屏幕容器，管理各个屏幕
 * 
 * @author yuankai
 * @version 1.0
 */
public class OnlineWorkspace extends ViewGroup implements OnClickListener,
		OnLongClickListener {
	private final static String TAG = "OnlineWorkspace";
	private static final int INVALID_SCREEN = -1;
	private final static float BOUNCE_RATIO = 0.1f;

	private IWorkspaceListener mListener = null;

	/**
	 * Fling 切换到下一个屏幕的最小速度
	 */
	private static final int SNAP_VELOCITY = 150;

	// 触屏状态
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	private int mTouchState = TOUCH_STATE_REST;

	// 设置滚动速度
	private int mScrollingDuration = 400;

	// 默认屏幕
	private int mMainScreen;
	// 当前屏幕
	protected int mCurrentScreen;
	private int mNextScreen = INVALID_SCREEN;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	// 切换屏幕无反弹效果，用于标识从预览界面进入桌面的切换
	private boolean mSnapWhthNoElastic = true;

	// Wysie: Values taken from CyanogenMod (Donut era) Browser
	private static final double ZOOM_SENSITIVITY = 1.6;
	private static final double ZOOM_LOG_BASE_INV = 1.0 / Math
			.log(2.0 / ZOOM_SENSITIVITY);

	private float mLastMotionX;
	private float mLastMotionY;

	// lock 标志，为ture时不响应触屏事件
	private boolean mLocked;
	private boolean mAllowLongPress;

	// private boolean mIsPressed;

	private MotionEvent mCurrentDownEvent;
	private int mTouchSlop;
	private int mMaximumVelocity;

	private boolean mLongClickView = false;

	// 弹性特效
	private int mScrollingBounce = 20;

	// 边缘弹性长度与屏幕宽度的比例
	private static final float EDGE_BOUNCE_MAX_RATIO = 0.17f;
	private int mEdgeBounceMaxLength;

	private boolean mPreventFC = true;
	private boolean mScrollRight = true;
	private final int MSG_SCROLL_START = 0;
	private final int MSG_SCROLL_RESTART = 1;

	private boolean mStoped = false;
	private Handler mTimerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SCROLL_START:
				// 检测滑动方向
				int nextScreen = mScrollRight ? mCurrentScreen + 1
						: mCurrentScreen - 1;
				if (nextScreen >= getChildCount())
					mScrollRight = false;
				if (nextScreen < 0)
					mScrollRight = true;
				// 获取目标屏幕索引
				nextScreen = mScrollRight ? mCurrentScreen + 1
						: mCurrentScreen - 1;
				if (mStoped)
					return;
				// 滑动操作
				snapToScreen(nextScreen, mScrollingDuration * 5, true);
				mTimerHandler.sendEmptyMessageDelayed(MSG_SCROLL_START, 6000);
				break;
			case MSG_SCROLL_RESTART:
				mStoped = false;
				mTimerHandler.sendEmptyMessageDelayed(MSG_SCROLL_START, 6000);
				break;
			}
		}
	};

	/**
	 * 开启计时滚动
	 */
	public void startScroll() {
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {

				if (isPressed(ev)) {// 当前WorkSpace控件被按下，则停止计时滑动，1秒后重新开启
					mStoped = true;
					mTimerHandler.removeMessages(MSG_SCROLL_START, null);
					mTimerHandler.removeMessages(MSG_SCROLL_RESTART, null);
					mTimerHandler.sendEmptyMessageDelayed(MSG_SCROLL_RESTART,
							500);
				}
				return false;
			}
		});
		mStoped = false;
		mTimerHandler.sendEmptyMessageDelayed(MSG_SCROLL_START, 6000);
	}

	/**
	 * 停止计时滚动
	 */
	public void stopScroll() {
		mStoped = true;
		mTimerHandler.removeMessages(MSG_SCROLL_START, null);
	}

	/**
	 * 
	 * @param context
	 *            context
	 */
	public OnlineWorkspace(Context context) {
		this(context, null);
	}

	/***
	 * 
	 * @param context
	 *            Context
	 * @param att
	 *            属性集
	 */
	public OnlineWorkspace(Context context, AttributeSet att) {
		super(context, att);
		mCurrentScreen = mMainScreen;
		setBounceAmount(mScrollingBounce);

		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int count = getChildCount();
		final int height = b - t;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth, height);
				childLeft += childWidth;
			}
		}
		// 重新计算边界弹性最大长度
		mEdgeBounceMaxLength = (int) ((r - l) * EDGE_BOUNCE_MAX_RATIO);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Workspace can only be used in EXACTLY mode.");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Workspace can only be used in EXACTLY mode.");
		}

		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			if (view != null) {
				view.measure(widthMeasureSpec, heightMeasureSpec);
			}
		}
	}

	public boolean isWidgetAtLocationScrollable(int x, int y) {
		return false;
	}

	public void unbindWidgetScrollableViews() {
	}

	public void unbindWidgetScrollableViewsForWidget(int widgetId) {
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		final int screenNum = getChildCount();
		if (screenNum <= 0) {
			return;
		}

		boolean fastDraw = mTouchState != TOUCH_STATE_SCROLLING
				&& mNextScreen == INVALID_SCREEN;

		// If we are not scrolling or flinging, draw only the current screen
		View childView = null;
		final long drawingTime = getDrawingTime();
		if (fastDraw) {
			childView = getChildAt(mCurrentScreen);
			if (childView.getVisibility() == VISIBLE) {
				drawChild(canvas, childView, drawingTime);
			}
		} else {
			// If we are flinging, draw only the current screen and the target
			// screen
			if (mNextScreen >= 0 && mNextScreen < screenNum
					&& Math.abs(mCurrentScreen - mNextScreen) == 1) {
				childView = getChildAt(mCurrentScreen);
				if (childView.getVisibility() == VISIBLE) {
					drawChild(canvas, childView, drawingTime);
				}
				childView = getChildAt(mNextScreen);
				if (childView.getVisibility() == VISIBLE) {
					drawChild(canvas, childView, drawingTime);
				}
			} else {
				// If we are scrolling, draw all of our children
				final int min = (mCurrentScreen - 1) < 0 ? 0
						: (mCurrentScreen - 1);
				final int max = (mCurrentScreen + 1) < screenNum ? (mCurrentScreen + 1)
						: screenNum - 1;
				for (int i = min; i <= max; i++) {
					childView = getChildAt(i);
					if (childView.getVisibility() == VISIBLE) {
						drawChild(canvas, childView, drawingTime);
					}
				}
				// for (int i = 0; i < screenNum; i++)
				// {
				// childView = getChildAt(i);
				// if(childView.getVisibility() == VISIBLE)
				// {
				// drawChild(canvas, childView, drawingTime);
				// }
				// }
			}
		}
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		int saveCount = canvas.save();
		super.drawChild(canvas, child, drawingTime);
		canvas.restoreToCount(saveCount);
		return true;
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (direction == View.FOCUS_LEFT) {
			if (getCurrentScreen() > 0) {
				snapToScreen(getCurrentScreen() - 1, false);
				return true;
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (getCurrentScreen() < getChildCount() - 1) {
				snapToScreen(getCurrentScreen() + 1, false);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	/**
	 * 设置翻屏时间
	 * 
	 * @param duration
	 *            翻屏时间
	 */
	public void setScrollDuration(int duration) {
		mScrollingDuration = duration;
	}

	/**
	 * 设置弹性特效参数
	 * 
	 * @param bounce
	 *            屏幕边缘可以拉伸的距离
	 */
	public void setBounceAmount(int bounce) {
		mScrollingBounce = bounce;
		Interpolator interpolator = new ElasticInterpolator(mScrollingBounce
				* BOUNCE_RATIO);
		// Interpolator interpolator = new
		// OvershootInterpolator(mScrollingBounce * BOUNCE_RATIO);
		mScroller = new Scroller(getContext(), interpolator);
	}

	@Override
	public void addView(View child) {
		if (!(child instanceof CellLayout)) {
			throw new IllegalArgumentException(
					"A Workspace can only have CellLayout children.");
		}
		initCellLayout((CellLayout) child);
		super.addView(child);
	}

	@Override
	public void addView(View child, int index) {
		if (!(child instanceof CellLayout)) {
			throw new IllegalArgumentException(
					"A Workspace can only have CellLayout children.");
		}
		initCellLayout((CellLayout) child);
		super.addView(child, index);
	}

	@Override
	public void addView(View child, int index, LayoutParams params) {
		if (!(child instanceof CellLayout)) {
			throw new IllegalArgumentException(
					"A Workspace can only have CellLayout CellLayout.");
		}
		initCellLayout((CellLayout) child);
		super.addView(child, index, params);
	}

	@Override
	public void addView(View child, int width, int height) {
		if (!(child instanceof CellLayout)) {
			throw new IllegalArgumentException(
					"A Workspace can only have CellLayout children.");
		}
		initCellLayout((CellLayout) child);
		super.addView(child, width, height);
	}

	@Override
	public void addView(View child, LayoutParams params) {
		if (!(child instanceof CellLayout)) {
			throw new IllegalArgumentException(
					"A Workspace can only have CellLayout children.");
		}
		initCellLayout((CellLayout) child);
		super.addView(child, params);
	}

	protected void initCellLayout(CellLayout screen) {
		screen.setOnLongClickListener(this);
	}

	/**
	 * 添加屏幕
	 * 
	 * @param screen
	 *            屏幕
	 * @param position
	 *            添加位置
	 */
	public void addScreen(CellLayout screen, int position) {
		addView(screen, position);
		if (mListener != null) {
			mListener.onUpdateTotalNum(getChildCount());
		}
	}

	/**
	 * 删除屏幕
	 * 
	 * @param screen
	 *            屏幕id
	 */
	public void removeScreen(int screen) {
		if (screen < 0 || screen >= getChildCount()) {
			return;
		}

		final CellLayout layout = (CellLayout) getChildAt(screen);
		if (layout == null) {
			return;
		}

		removeView(layout);

		int currentScreen = mCurrentScreen;
		boolean refresh = false;
		if (screen < currentScreen) {
			currentScreen -= 1;
			refresh = true;
		} else if (screen == currentScreen) {
			currentScreen = 0;
			refresh = true;
		}
		mCurrentScreen = Math.max(0,
				Math.min(currentScreen, getChildCount() - 1));
		if (refresh) {
			setCurrentScreen(mCurrentScreen);
		}

		if (screen <= mMainScreen) {
			int mainScreen = mMainScreen;
			if (screen < mMainScreen) {
				mainScreen -= 1;
			} else if (screen == mMainScreen) {
				mainScreen = 0;
			}
			mMainScreen = Math
					.max(0, Math.min(mainScreen, getChildCount() - 1));
		}

		if (mListener != null) {
			mListener.onUpdateTotalNum(getChildCount());
		}
	}

	/**
	 * 指定屏是否有子元素
	 * 
	 * @param screenId
	 *            屏ID
	 * @return 是否有
	 */
	public boolean hasChildElement(int screenId) {
		CellLayout child = (CellLayout) getChildAt(screenId);
		if (child != null) {
			return child.getChildCount() > 0;
		} else {
			return false;
		}
	}

	/**
	 * Unlocks the SlidingDrawer so that touch events are processed.
	 * 
	 * @see #lock()
	 */
	public void unlock() {
		mLocked = false;
	}

	/**
	 * Locks the SlidingDrawer so that touch events are ignores.
	 * 
	 * @see #unlock()
	 */
	public void lock() {
		mLocked = true;
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			if (i >= mCurrentScreen - 1 || i <= mCurrentScreen + 1) {
				final CellLayout layout = (CellLayout) getChildAt(i);
				layout.setChildrenDrawingCacheEnabled(true);
				layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
				layout.setChildrenDrawnWithCacheEnabled(true);
			}
		}
	}

	void clearChildrenCache() {
		clearChildrenCache(false);
	}

	void clearChildrenCache(boolean needGc) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final CellLayout layout = (CellLayout) getChildAt(i);
			layout.setChildrenDrawnWithCacheEnabled(false);
		}

		if (needGc) {
			OutOfMemoryHandle.gcIfAllocateOutOfHeapSize();
		}
	}

	/**
	 * 设置默认屏幕
	 * 
	 * @param screen
	 *            屏幕
	 */
	public void setMainScreen(int screen) {
		if (screen >= getChildCount() || screen < 0) {
			// Log.i(LOG_TAG, "Cannot reset default screen to " + screen);
			return;
		}
		mMainScreen = screen;
	}

	/**
	 * 
	 * @return 当前主屏
	 */
	public int getMainScreen() {
		return mMainScreen;
	}

	/**
	 * @return 当前是否显示默认屏幕
	 */
	public boolean isMainScreenShowing() {
		return mCurrentScreen == mMainScreen;
	}

	/**
	 * 获取当前显示的屏幕id
	 * 
	 * @return 当前屏幕.
	 */
	public int getCurrentScreen() {
		return mCurrentScreen;
	}

	/**
	 * 设置当前屏幕
	 * 
	 * @param currentScreen
	 *            当前屏幕id.
	 */
	public void setCurrentScreen(int currentScreen) {
		mCurrentScreen = Math.max(0,
				Math.min(currentScreen, getChildCount() - 1));
		mNextScreen = mCurrentScreen;
		scrollTo(mCurrentScreen * getWidth(), 0);

		// 更新指示器
		if (mListener != null) {
			mListener.onUpdateCurrent(currentScreen);
		}
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

			postInvalidate();
		} else {
			if (mSnapWhthNoElastic) {
				mSnapWhthNoElastic = false;
				setBounceAmount(mScrollingBounce);
			}

			if (mNextScreen != INVALID_SCREEN) {
				mCurrentScreen = Math.max(0,
						Math.min(mNextScreen, getChildCount() - 1));
				mNextScreen = INVALID_SCREEN;

				// 更新点状指示器
				if (mListener != null) {
					mListener.onUpdateCurrent(mCurrentScreen);
				}
				clearChildrenCache(mPreventFC);
			}
		}
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setWorkspaceListener(IWorkspaceListener listener) {
		mListener = listener;
	}

	@Override
	public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
			boolean immediate) {
		int screen = indexOfChild(child);
		if (screen != mCurrentScreen || !mScroller.isFinished()) {
			if (!mLocked) {
				snapToScreen(screen, true);
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {
		int focusableScreen;
		if (mNextScreen != INVALID_SCREEN) {
			focusableScreen = mNextScreen;
		} else {
			focusableScreen = mCurrentScreen;
		}

		if (focusableScreen < getChildCount()) {
			getChildAt(focusableScreen).requestFocus(direction,
					previouslyFocusedRect);
		}
		return false;
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		if (mCurrentScreen >= getChildCount()) {
			return;
		}

		getChildAt(mCurrentScreen).addFocusables(views, direction);
		if (direction == View.FOCUS_LEFT) {
			if (mCurrentScreen > 0) {
				getChildAt(mCurrentScreen - 1).addFocusables(views, direction);
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (mCurrentScreen < getChildCount() - 1) {
				getChildAt(mCurrentScreen + 1).addFocusables(views, direction);
			}
		}
	}
	boolean DEBUG = true;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		if (mLocked) {
			return true;
		}

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mLastMotionX = x;
			mLastMotionY = y;
			mAllowLongPress = true;
			mLongClickView = false;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			mTouchState = TOUCH_STATE_SCROLLING;

			// 记录down事件
			if (mCurrentDownEvent != null) {
				mCurrentDownEvent.recycle();
			}
			mCurrentDownEvent = MotionEvent.obtain(ev);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);

			boolean xMoved = xDiff > mTouchSlop;
			boolean yMoved = yDiff > mTouchSlop;

			if ((xMoved || yMoved)) {
				if (mLongClickView) {
					mLongClickView = false;
				} else if (xDiff > yDiff) {
					mTouchState = TOUCH_STATE_SCROLLING;
					enableChildrenCache();
				} else {
				}

				if (mAllowLongPress) {
					mAllowLongPress = false;
					final View currentScreen = getChildAt(mCurrentScreen);
					currentScreen.cancelLongPress();
				}
			}
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			clearChildrenCache();
			mTouchState = TOUCH_STATE_REST;
			mAllowLongPress = false;
			break;
		}

		default:
			break;
		}

		//
//		if(DEBUG){
//			return true;
//		}else
			return mTouchState != TOUCH_STATE_REST;
	}

	/**
	 * 根据Touch事件ev检测当前View是否被按下
	 * 
	 * @param ev
	 * @return
	 */
	private boolean isPressed(MotionEvent ev) {
		Rect workspaceRect = new Rect();
		getHitRect(workspaceRect);
		Rect r = new Rect();
		r.left = r.right = (int) ev.getX();
		r.bottom = r.top = (int) ev.getY();
		return workspaceRect.intersects(r.left, r.top, r.right, r.bottom);
	}
	float x1 = 0;
	float y1 = 0;
	float tempX = 0;
	float tempY = 0;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if (mLocked) {
			return true;
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!isPressed(ev))// 按下区域不在当前View，不响应该事件
				break;
			
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			if(getParent()!=null){
				if(getParent().getParent()!=null)
				getParent().getParent().requestDisallowInterceptTouchEvent(true);
			}
			x1 = ev.getX();
			y1 = ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			if (!isPressed(ev))// 按下区域不在当前View，不响应该事件
				break;
			if(getParent()!=null){
				if(getParent().getParent()!=null)
				getParent().getParent().requestDisallowInterceptTouchEvent(true);
			}
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				// Scroll to follow the motion event
				final int deltaX = (int) (mLastMotionX - x);
				final int scrollX = getScrollX();
				final int width = getWidth();

				mLastMotionX = x;
				if (deltaX < 0) {
					if (scrollX > -mEdgeBounceMaxLength) {
						scrollBy(Math.min(deltaX, mEdgeBounceMaxLength), 0);
					}
				} else if (deltaX > 0) {
					final int lastIndex = getChildCount() - 1;
					if (lastIndex >= 0) {
						final int lastRight = getChildAt(lastIndex).getRight();
						final int availableToScroll = lastRight - scrollX
								- width + mEdgeBounceMaxLength;
						if (availableToScroll > 0) {
							scrollBy(deltaX, 0);
						}
					}
				}

				final int screenWidth = getWidth();
				final int whichScreen = (getScrollX() + (screenWidth / 2))
						/ screenWidth;
				if (whichScreen != mCurrentScreen && mListener != null) {
					mListener.onUpdateCurrent(whichScreen);
				}
				
			}
			break;

		case MotionEvent.ACTION_UP:
			mLongClickView = false;
			tempX = ev.getX();
			tempY = ev.getY();
			if(Math.abs(x1 - tempX) < 5 && Math.abs(y1 - tempY) < 5){
				snapToDestination();
				mListener.onWorkspaceClick(mCurrentScreen);
				break;
			}
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				final int velocityX = (int) velocityTracker.getXVelocity();
				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					// Fling hard enough to move left
					snapToScreen(mCurrentScreen - 1, false);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurrentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					snapToScreen(mCurrentScreen + 1, false);
				} else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			mTouchState = TOUCH_STATE_REST;
			break;

		case MotionEvent.ACTION_CANCEL:
			// 此处代码同MotionEvent.ACTION_UP 是一样的为了解决listView在上下滑动的时候容易卡住问题。
			mLongClickView = false;
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				final int velocityX = (int) velocityTracker.getXVelocity();
				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					// Fling hard enough to move left
					snapToScreen(mCurrentScreen - 1, false);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurrentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					snapToScreen(mCurrentScreen + 1, false);
				} else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	/**
	 * 重载方法，应对在横竖屏时getWidth()不准确的问题。
	 * 
	 * @param whichScreen
	 * @param elastic
	 * @param width
	 */
	private void snapToScreen(int whichScreen, boolean noElastic, int width,
			int duration) {
		if (mScroller == null || width <= 0) {
			return;
		}

		if (noElastic) {
			mSnapWhthNoElastic = true;
			mScroller = new Scroller(getContext());
		}

		if (!mScroller.isFinished()) {
			return;
		}

		enableChildrenCache();

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		boolean changingScreens = whichScreen != mCurrentScreen;

		mNextScreen = whichScreen;
		if (mListener != null) {
			mListener.onUpdateCurrent(mNextScreen);
		}

		View focusedChild = getFocusedChild();
		if (focusedChild != null && changingScreens
				&& focusedChild == getChildAt(mCurrentScreen)) {
			focusedChild.clearFocus();
		}

		final int newX = whichScreen * width;
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
		invalidate();
	}

	/**
	 * 重载方法，应对在横竖屏时getWidth()不准确的问题。
	 * 
	 * @param whichScreen
	 * @param elastic
	 * @param width
	 */
	private void snapToScreen(int whichScreen, boolean noElastic, int width) {
		snapToScreen(whichScreen, noElastic, width, mScrollingDuration);
	}

	public void snapToScreen(int whichScreen, boolean noElastic) {
		if (whichScreen >= 0 && whichScreen < getChildCount()) {
			snapToScreen(whichScreen, noElastic, getWidth());
		}
	}

	public void snapToScreen(int whichScreen, int duration, boolean noElastic) {
		if (whichScreen >= 0 && whichScreen < getChildCount()) {
			snapToScreen(whichScreen, noElastic, getWidth(), duration);
		}
	}

	private void snapToDestination() {
		final int screenWidth = getWidth();
		int whichScreen = (getScrollX() + (screenWidth / 2)) / screenWidth;
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		snapToScreen(whichScreen, false);
	}

	/**
	 * 查找组件所在屏幕索引
	 * 
	 * @param v
	 *            组件
	 * @return 屏幕索引
	 */
	public int getScreenForView(View v) {
		int result = -1;
		if (v != null) {
			ViewParent vp = v.getParent();
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				if (vp == getChildAt(i)) {
					return i;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @return 当前屏幕View
	 */
	public CellLayout getCurrentScreenView() {
		return (CellLayout) getChildAt(mCurrentScreen);
	}

	/**
	 * 监听器
	 * 
	 * @author yuankai
	 * @version 1.0
	 * @date 2011-6-4
	 */
	public interface IWorkspaceListener {
		/**
		 * 更新总屏幕数时回调
		 * 
		 * @param total
		 *            总数
		 */
		public void onUpdateTotalNum(int total);

		/**
		 * 更新当前屏幕索引时回调
		 * 
		 * @param current
		 */
		public void onUpdateCurrent(int current);
		
		/**
		 * 点击事件回调
		 * */
		
		public void onWorkspaceClick(int current);
	}
	
}
