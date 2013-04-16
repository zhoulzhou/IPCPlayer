package com.example.ipcplayer.widget;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.activity.PlayingActivity;
import com.example.ipcplayer.object.LyricDecodingInfo;
import com.example.ipcplayer.utils.ConvertUtil;
import com.example.ipcplayer.utils.LogUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 歌词界面
 * @author zhoulu
 * @since 2012-10-16-下午9:31:33
 * @version 1.0
 */
public class LyricView extends View implements OnGestureListener {

	private static final String TAG = "LyricView";
	/**字体大小*/
	private static final int FONT_TEXT_SIZE = 16;
	/**高亮字体大小*/
	private static final int HIGH_LIGHT_FONT_TEXT_SIZE = 16;
	/**字体垂直间距*/
	private static final int FONT_TEXT_VERTICAL_SPAC = 3;
	/**高亮字体的颜色*/
	private static final int FONT_TEXT_HIGHLIGHT_COLOR = Color.rgb(30, 149, 210);
	/**普通字体的颜色*/
	private static final int FONT_TEXT_NORMAL_COLOR = Color.WHITE;
	/**中心线和左右两边时间的间隔*/
	private static final int LEFT_RIGHT_LINE_SPAC = 5;
	
	private static final int BASE_TIME = 50;
	
	private static final float MIN_SCROLL = 2f;
	private static final float MAX_SCROLL = 4f;
	
	/*************************************/
	private Paint mHighlightPaint;//高亮歌词画笔
	private Paint mNormalPaint;//普通歌词画笔
	private Paint mTimePaint;//时间画笔
	private Paint mCenterLinePaint;//中心线画笔
	private Paint mClearPaint;//清屏画笔
	
	private Paint mDrawPaint;//绘制使用的画笔
//	private List<String> mData;
	/** 通过解析之后，得到的歌词数据 */
	private List<LyricDataSentence> mSentencesData;
	private SparseArray<List<LyricDataSentence>> mSentencesDataByDecodeIndex;//通过解析之后，根据原来歌词索引分组的歌词数据
	
	private float mVerticalSpac;//行间距
	private float mCenterX;//中心点X
	private float mCenterY;//中心点Y
	private float mVisibleWidth;//歌词可见区域的宽度
	private float mVisibleHeight;//歌词可见区域的高度
	
	private float mRealTextHeight;//歌词总长度
	
	private float mScrollOffset; //滚动一次的偏移量
	private float mOldScrollOffset;//在暂停的时候，存储滚动的偏移量
	private float mTextAutoScrollOffsetY; //文本滚动的偏移量Y
	
	private float mFontHeight;//字体高
	private String mLoadingTip = "";//在没有歌词的时候展示的字符串
	
	private FontMetrics mFontMetrics;

	private Resources mResources;
	private DisplayMetrics mMetrics;
	
	private Rect mDirtyRect;
	
	/*************************************/
	private boolean mIsBeingDrag;//判断正在拖动中
	private boolean mIsClicked;
	private float mLastMotionY;
	private float mTextScrollDeltaY;//手指滚动的距离
	
	private int mTouchSlop;//
	
	private String mLeftTimeText;
	private String mRightTimeText;
	
	private float mLeftTimeX;//左时间X坐标
	private float mRightTimeX;//右时间X坐标
	
	private float mLeftTimeWidth;//左时间字符长度
	private float mRightTimeWidth;//右时间字符长度
	
	private float mLeftRightPaddingWithLine;//中心线条和左右两边的间隔
	
	/*************************************/
	
	private LyricDecodingInfo mLyricDecoder;
	private boolean mIsLoadingLyric = true;//是否正在加载歌词中
	private boolean mIsFirstShow = true;//第一次显示歌词
	private boolean isEnd;//播放到最后一行
	
	private int mCurrentLyricShowIndex;//当前中心歌词的显示索引
	private int mCurrentLyricIndex;//当前中心歌词的解析索引
	private long mCurrentLyricTime;//当前server播放的时间
	
	private long mSongDuration;//总歌词持续时间
	private GestureDetector mGestureDetector = new GestureDetector(this);
	/*************************************/
	
	private OnMoveListener mOnMoveListener;
	private Handler mHandler = new Handler(){
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				autoScrollByDelay();
				if(!mIsBeingDrag){
					invalidate(mDirtyRect);
				}
				mHandler.sendEmptyMessageDelayed(0, BASE_TIME);
			}
		}
	};
	
	
	/*************************************/

//	private Animation mHighlightFadeIn;
	/*************************************/
//	private Canvas mDrawCanvas;
//	private Bitmap mCacheImage;

	private boolean mIsShowView;
	public LyricView(Context context) {
		super(context);
		init();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setOnMoveListener(OnMoveListener onMoveListener) {
		this.mOnMoveListener = onMoveListener;
	}
	
	public LyricDecodingInfo getLyricDecoder() {
		return mLyricDecoder;
	}

	public void setLyricDecoder(LyricDecodingInfo lyricDecoder) {
		this.mLyricDecoder = lyricDecoder;
		mLyricDecoder.setOnLryicResetListener(new LyricDecodingInfo.OnLyricResetListener() {
			
			@Override
			public void onReset() {
				decodeAllData();
			}
		});
	}
	
	public void setParentFocus(int current) {
		if(current == 1){
			mIsShowView = true;
		} else {
			mIsShowView = false;
		}
	}
	
	private void init(){
		LogUtil.d(TAG, "++++++++ init....");
		//初始化视图
		initView();
		//初始化画笔
		initPaint();
		//初始化字体数据
		initFont();
		//初始化动画
		initAniamtions();
	}

	private void initAniamtions() {
//		mHighlightFadeIn = new AlphaAnimation(0.1f, 1.0f);
	}

	private void initView() {
		mResources = getResources();
		mMetrics = mResources.getDisplayMetrics();
		
	}

	private void initFont() {
		
		ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
		mTouchSlop = viewConfiguration.getScaledTouchSlop();
		mVerticalSpac = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FONT_TEXT_VERTICAL_SPAC, mMetrics);
		
		mFontMetrics = mDrawPaint.getFontMetrics();
		mFontHeight = mFontMetrics.bottom - mFontMetrics.top;
		
		//300 是预设的屏幕宽度 320-2*10
		mVisibleWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, mMetrics);
		
	}

	private void initPaint() {
		
		mHighlightPaint = new Paint(Paint.DITHER_FLAG);
		mHighlightPaint.setAntiAlias(true);
		mHighlightPaint.setColor(FONT_TEXT_HIGHLIGHT_COLOR);
		float highLightTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, HIGH_LIGHT_FONT_TEXT_SIZE, mMetrics);
		mHighlightPaint.setTextSize(highLightTextSize);
		mHighlightPaint.setTextAlign(Align.CENTER);
		mHighlightPaint.setFakeBoldText(true);
		
		mNormalPaint = new Paint(Paint.DITHER_FLAG);
		mNormalPaint.setAntiAlias(true);
		mNormalPaint.setColor(FONT_TEXT_NORMAL_COLOR);
		float normalTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, FONT_TEXT_SIZE, mMetrics);
		mNormalPaint.setTextSize(normalTextSize);
		mNormalPaint.setTextAlign(Align.CENTER);
		
		float timeTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, mMetrics);
		mTimePaint = new Paint(Paint.DITHER_FLAG);
		mTimePaint.setAntiAlias(true);
		mTimePaint.setTextAlign(Align.CENTER);
		mTimePaint.setTextSize(timeTextSize);
		mTimePaint.setFakeBoldText(true);
		mTimePaint.setColor(Color.WHITE);
		
		mLeftRightPaddingWithLine = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LEFT_RIGHT_LINE_SPAC, mMetrics);
		mCenterLinePaint = new Paint(Paint.DITHER_FLAG);
		mCenterLinePaint.setAntiAlias(true);
		mCenterLinePaint.setStrokeWidth(2);
		mCenterLinePaint.setColor(0xFF279CE7);
		
		mClearPaint = new Paint(Paint.DITHER_FLAG);
		mClearPaint.setAntiAlias(true);
		mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		
		mDrawPaint = new Paint();
		
		changePaint(mNormalPaint);
	}

	/**
	 * 更新歌词
	 */
	public boolean updateLyric(int state){
//		LogUtil.d(TAG, "++++++ updateLyric state = " + state);
//		LogUtil.d(TAG, "++++++ updateLyric mIsLoadingLyric = " + mIsLoadingLyric);
//		LogUtil.d(TAG, "++++++ updateLyric state = " + state);
		switch(state){
		case PlayingActivity.NO_LYRIC:
			mLoadingTip = mResources.getString(R.string.slogan);
			mIsLoadingLyric = true;
			return true;
		case PlayingActivity.SEARCH_LYRIC:
			mLoadingTip = mResources.getString(R.string.lyric_loading_message);
			mIsLoadingLyric = true;
			return true;
		case PlayingActivity.LYRIC_READY:
			if(mIsLoadingLyric){
				LogUtil.i(TAG, "updateLyric, clear All");
				clearScrollData();
				decodeAllData();
				if(mSentencesData != null){
					if(mHandler != null){
						
						if(mSentencesData != null){
							mRealTextHeight = (mFontHeight + mVerticalSpac) * mSentencesData.size() - mVerticalSpac;
						}
						mHandler.sendEmptyMessage(0);
					}
				}
			}
			mIsLoadingLyric = false;
			return false;
		}
		return false;
	}
	
	/**
	 * 设置开关, 3.2.0
	 */
	public void setIsLoadingLyric(boolean mIsLoadingLyric){
		LogUtil.d(TAG, "setIsLoadingLyric, mIsLoadingLyric=" + mIsLoadingLyric);
		this.mIsLoadingLyric = mIsLoadingLyric;
	}
	
	public void setPlayPause(boolean isPaused){
		if(isPaused){
			mOldScrollOffset = mScrollOffset;
			mScrollOffset = 0;
		} else {
			mScrollOffset = mOldScrollOffset;
		}
	}
	
	private void clearScrollData() {
		LogUtil.d(TAG, "#zl +++++++ clearScrollData");
		mTextAutoScrollOffsetY = 0;
		mTextScrollDeltaY = 0;
		mCurrentLyricIndex = 0;
		mCurrentLyricShowIndex = 0;
		mCurrentLyricTime = 0;
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(mHandler.hasMessages(0)){
			mHandler.removeMessages(0);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		LogUtil.d("@@@onSizeChanged");
		initDynamicSize(w, h);
	}
	
	/**
	 *  更新当前索引 
	 * @param index
	 * @param time
	 */
	public void updateLyricIndex(boolean force, int index, long time, long totalTime){
		if(!mIsBeingDrag){
//			LogUtil.d(TAG, "++++++++++++++ updateLyricIndex");
			mCurrentLyricTime = time;
			mCurrentLyricIndex = index;
			mSongDuration = totalTime;
			
			if(mSentencesDataByDecodeIndex == null
					|| mSentencesDataByDecodeIndex.size() == 0){
				return;
			}
			//获得单行歌词的时间延时
			List<LyricDataSentence> list = mSentencesDataByDecodeIndex.get(mCurrentLyricIndex);
			int whereToLineIndex = 0;
			long lyricTimeLeave = 0;
			if(list != null){
				for (LyricDataSentence lyricDataSentence : list) {
					if(lyricDataSentence.mStartTime <= mCurrentLyricTime && lyricDataSentence.mEndTime > mCurrentLyricTime){
						lyricTimeLeave = lyricDataSentence.mEndTime - mCurrentLyricTime;
						whereToLineIndex = lyricDataSentence.mShowListIndex;
						break;
					}
				}
			}
			
			mCurrentLyricShowIndex = whereToLineIndex;
			boolean isLoop = updateNext();
			if(mIsFirstShow || force || (force && lyricTimeLeave == 0) || isLoop || !mIsShowView){
				mIsFirstShow = false;
				mTextAutoScrollOffsetY = mCurrentLyricShowIndex * (mFontHeight + mVerticalSpac);
			} else if(lyricTimeLeave != 0){
				calculateLineFeedOffset(lyricTimeLeave);
			}
			
			//清理手动滚动的距离
			mTextScrollDeltaY = 0;
			
			invalidate(mDirtyRect);
		}
	}

	/**
	 * 计算换行位移
	 * @param lyricTimeLeave
	 */
	private void calculateLineFeedOffset(long lyricTimeLeave) {
		//自动滚动和指定位置的位移补偿值
		float offset = mTextAutoScrollOffsetY - mCurrentLyricShowIndex * (mFontHeight + mVerticalSpac);
		
		//计算偏移
		/**
		 *	leaveTime			realOffsetY
		 *------------- = ---------------------
		 *	baseTime		cellScrollOffsetY
		 *	 
		 *	cellScrollOffsetY = realOffsetY * baseTime / leaveTime;
		 */
		mScrollOffset = (mFontHeight + mVerticalSpac) * BASE_TIME / lyricTimeLeave;
		
		
		
		if(offset > 0 && offset < (mFontHeight + mVerticalSpac)){
			mScrollOffset -= (offset * BASE_TIME / lyricTimeLeave);
		} else if(offset < 0 && offset > -(mFontHeight + mVerticalSpac)){
			mScrollOffset += (offset * BASE_TIME / lyricTimeLeave);
		}
		
		mTextAutoScrollOffsetY += mTextScrollDeltaY;
		
		
		LogUtil.d(TAG, "========  mCurrentLyricShowIndex " + mCurrentLyricShowIndex);
		LogUtil.d(TAG, "========  offset " + offset);
		LogUtil.d(TAG, "========  mScrollOffset " + mScrollOffset);
		LogUtil.d(TAG, "========  mTextAutoScrollOffsetY " + mTextAutoScrollOffsetY);
	}
	
	private void initDynamicSize(int width, int height) {
		LogUtil.i(TAG, "@@@ width = " + width + " height =  " + height);
		
		mCenterX = width >> 1;
		mCenterY = height >> 1;
		
		mVisibleWidth = width - getPaddingLeft() - getPaddingRight();
		mVisibleHeight = height - getPaddingTop() - getPaddingBottom();
		
		mDirtyRect = new Rect((int)(mCenterX - mVisibleWidth / 2), (int)(mCenterY - mVisibleHeight / 2), 
				(int)(mCenterX + mVisibleWidth / 2), (int)(mCenterY + mVisibleHeight / 2));
		
		if(mSentencesData != null){
			mRealTextHeight = (mFontHeight + mVerticalSpac) * mSentencesData.size() - mVerticalSpac;
		}
		
//		mCacheImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//		mDrawCanvas = new Canvas(mCacheImage);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mGestureDetector.onTouchEvent(event);
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = event.getY();
			
			mIsClicked = true;
			if(mOnMoveListener != null){
				mOnMoveListener.onMoveStart();
			}
			
			break;
		case MotionEvent.ACTION_MOVE:
			final float y = event.getY();
			final float offsetY = (int) (mLastMotionY - y);
			
			if(!mIsBeingDrag){
				//大于触摸最小移动值才开始滑动
				if(Math.abs(offsetY) > mTouchSlop && mLyricDecoder.getLines() != 0){
					mIsBeingDrag = true;
					mIsClicked = false;
				}
			}
			
			if(mIsBeingDrag && !mIsClicked){
				mTextScrollDeltaY += offsetY;
				if(offsetOverSide()){
					mTextScrollDeltaY -= offsetY;
				} else {
					if(mOnMoveListener != null){
						mOnMoveListener.onMoving();
					}
					mLastMotionY = y;
					//计算
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if(mIsClicked){
				
				mIsClicked = false;
				break;
			}
			if(mIsBeingDrag){
				mIsBeingDrag = false;
				//及时将中心线去掉
				invalidate();
				if(mOnMoveListener != null){
					
					long toTime = getChildSentenceStartTime();
					if(toTime != -1){
						mOnMoveListener.onMoveEnd(toTime);
					}
					mLastMotionY = 0;
				}
			}
			break;
		}
		return true;
	}

	private long getChildSentenceStartTime() {
		if(mSentencesDataByDecodeIndex == null || mSentencesDataByDecodeIndex.size() == 0){
			return -1;
		}
		List<LyricDataSentence> list = mSentencesDataByDecodeIndex.get(mCurrentLyricIndex);
		for (LyricDataSentence lyricDataSentence : list) {
			if(mCurrentLyricShowIndex == lyricDataSentence.mShowListIndex){
				return lyricDataSentence.mStartTime;
			}
		}
		
		return -1;
	}
	
	/**
	 * 是否滚动越界
	 * @return
	 */
	private boolean offsetOverSide(){
		return ((mTextAutoScrollOffsetY + mTextScrollDeltaY <= mRealTextHeight - mFontHeight / 2)
				&& (mTextAutoScrollOffsetY + mTextScrollDeltaY >= - mFontHeight / 2)) ? false : true;
	}
	
	/**
	 * 内部刷新线程更新当前显示行索引
	 */
	private void updateIndexInternal(){
		if(mSentencesData == null || mSentencesData.size() == 0){
			return;
		}
		int oldShowIndex = mCurrentLyricShowIndex;
		float totalOffsetY = mTextAutoScrollOffsetY + mTextScrollDeltaY + (mFontHeight + mVerticalSpac) / 2;
		float singleLineTextHeight = mFontHeight + mVerticalSpac;
		if(totalOffsetY > mRealTextHeight){
			mCurrentLyricShowIndex = mSentencesData.size() - 1; 
		} else if(totalOffsetY <= 0){
			mCurrentLyricShowIndex = 0; 
		} else {
			mCurrentLyricShowIndex = (int) (totalOffsetY / singleLineTextHeight);
		}
		try{
			mCurrentLyricIndex = mSentencesData.get(mCurrentLyricShowIndex).mDecoderIndex;
		}catch (Exception e) {
//			LogUtil.e(TAG, " ++++++++++++updateIndexInternal ");
		}
		
		if(!mIsBeingDrag && oldShowIndex != mCurrentLyricShowIndex){
			//计算换行偏移
			long duration = mSentencesData.get(mCurrentLyricShowIndex).mDuration;
			calculateLineFeedOffset(duration);
		}
	}

	/**
	 * 判断滚动到中间之后，停止滚动
	 */
	private void updateCenterStopScroll(){
		float totalOffset = mTextAutoScrollOffsetY + mTextScrollDeltaY;
		float currentLineOffset = mCurrentLyricShowIndex * (mFontHeight + mVerticalSpac) + mVerticalSpac;//当前行离第一行的偏移大小
		if(totalOffset >= currentLineOffset){
			mScrollOffset = 0;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		Canvas canvas = mDrawCanvas;
		if(mIsBeingDrag){
			updateIndexInternal();
			calculateTime();
		} else {
			updateCenterStopScroll();
		}
		
		if((mIsLoadingLyric || (mSentencesData != null && mSentencesData.size() != 0))
				&& canvas != null){
//			canvas.drawColor(0, Mode.CLEAR);
			super.onDraw(canvas);
			
			if(mIsLoadingLyric){
				//绘制载入中
				changePaint(mNormalPaint);
				canvas.drawText(mLoadingTip, mCenterX, mCenterY, mDrawPaint);
			} else {
				//歌词
				drawLyric(canvas);
				//中心线
				drawCenterLineAndTime(canvas);
			}
		}
		
//		mDrawPaint.setFlags(Paint.DITHER_FLAG);
//		mcanvas.drawBitmap(mCacheImage, 0, 0, mDrawPaint);
		
	}
	
	private void autoScrollByDelay() {
		if(!offsetOverSide() && !mIsBeingDrag && mScrollOffset > 0){
			if(mScrollOffset < MIN_SCROLL){
				mScrollOffset = MIN_SCROLL;
			} else if(mScrollOffset > MAX_SCROLL){
				mScrollOffset = MAX_SCROLL;
			}
			
			mTextAutoScrollOffsetY += mScrollOffset;
		}
	}
	
	private void drawLyric(Canvas canvas) {
		
		canvas.save();
		canvas.translate(0, - mTextAutoScrollOffsetY - mTextScrollDeltaY);
		float lineOffsetY = mVerticalSpac;
		
		LyricDataSentence sentence = null;
		for (int dataIdx = 0; dataIdx < mSentencesData.size(); dataIdx++) {
			sentence = mSentencesData.get(dataIdx);
			if (sentence == null)
				continue;
			String drawStr = sentence.mLyricText;
			int index = sentence.mDataIndex;
			if(dataIdx != 0){
				lineOffsetY += mFontHeight + mVerticalSpac;
			}
			if(isCenter(index)){
				changePaint(mHighlightPaint);
			} else {
				changePaint(mNormalPaint);
			}
			canvas.drawText(drawStr, mCenterX, mCenterY + lineOffsetY, mDrawPaint);
//			canvas.drawText(drawStr, mCenterX, mCenterY + lineOffsetY - mTextAutoScrollOffsetY - mTextScrollDeltaY, mDrawPaint);
		}
		canvas.restore();
	}

	private void drawCenterLineAndTime(Canvas canvas) {
		if(mIsBeingDrag){
			//left time
			changePaint(mTimePaint);
			canvas.drawText(mLeftTimeText, mLeftTimeX, mCenterY, mDrawPaint);
			//right time
			canvas.drawText(mRightTimeText, mRightTimeX, mCenterY, mDrawPaint);
			//center line
			changePaint(mCenterLinePaint);
			canvas.drawLine(mLeftTimeX + mLeftTimeWidth / 2 + mLeftRightPaddingWithLine, mCenterY,
					mRightTimeX - mRightTimeWidth / 2 - mLeftRightPaddingWithLine, mCenterY, mDrawPaint);
		}
	}
	
	/**
	 * 解析歌词数据 
	 */
	private void decodeAllData() {
		LogUtil.i(TAG, "mVisibleWidth = " + mVisibleWidth);
		
		if(mLyricDecoder == null || mLyricDecoder.getLines() == 0){
			return;
		}
		
		List<String> sentences = new ArrayList<String>();
		sentences.addAll(mLyricDecoder.mSentencesList);
		if(mSentencesData != null){
			mSentencesData.clear();
			mSentencesData = null;
		}
		if(mSentencesDataByDecodeIndex != null){
			mSentencesDataByDecodeIndex.clear();
			mSentencesDataByDecodeIndex = null;
		}
		mSentencesData = new ArrayList<LyricDataSentence>();
		mSentencesDataByDecodeIndex = new SparseArray<List<LyricDataSentence>>();
		int decodeIndex = 0;
		for (int dataIdx = 0; dataIdx < sentences.size(); dataIdx++) {
			String text = sentences.get(dataIdx);
			List<String> breakTexts = breakText(text, mVisibleWidth, mDrawPaint);
			LogUtil.i(TAG, "原歌词["+dataIdx+"]：" + text);
			
			List<LyricDataSentence> useForDecodeIndexValue = new ArrayList<LyricDataSentence>(); 
			
			int count = breakTexts.size();
			if(breakTexts != null && count > 1){
				long startTime = mLyricDecoder.getSentenceStartTime(decodeIndex);
				for (int breakTextIdx = 0; breakTextIdx < breakTexts.size(); breakTextIdx++) {
					
					String childSentence = breakTexts.get(breakTextIdx);
					
					int len = childSentence.length();
					long childDuration = mLyricDecoder.getChildSentenceDuration(decodeIndex, len);
					LyricDataSentence lyricDataSentence = new LyricDataSentence();
					lyricDataSentence.mDecoderIndex = decodeIndex;
					lyricDataSentence.mDataIndex = dataIdx;					
					lyricDataSentence.mShowListIndex = dataIdx + breakTextIdx;
					lyricDataSentence.mDecoderCount = count;
					lyricDataSentence.mStartTime = startTime;
					lyricDataSentence.mEndTime = startTime + childDuration;
					lyricDataSentence.mDuration = childDuration;
					lyricDataSentence.mLyricText = childSentence;
					LogUtil.i(TAG, "子句["+(dataIdx + breakTextIdx)+"]：" + childSentence + " =====>" + lyricDataSentence.toString());
					mSentencesData.add(lyricDataSentence);
					
					useForDecodeIndexValue.add(lyricDataSentence);
					
					//移动开始时间的游标
					startTime = lyricDataSentence.mEndTime + 1;
				}
				//将打散的字符串加入到歌词数据中
				sentences.remove(dataIdx);
				sentences.addAll(dataIdx, breakTexts);
				
				dataIdx += count - 1;
			} else {
				LyricDataSentence lyricDataSentence = new LyricDataSentence();
				lyricDataSentence.mDecoderIndex = decodeIndex;
				lyricDataSentence.mDataIndex = dataIdx;
				lyricDataSentence.mShowListIndex = dataIdx;
				lyricDataSentence.mDecoderCount = 1;
				lyricDataSentence.mStartTime = mLyricDecoder.getSentenceStartTime(decodeIndex);
				lyricDataSentence.mEndTime = mLyricDecoder.getSentenceEndTime(decodeIndex);
				lyricDataSentence.mDuration = mLyricDecoder.getSentenceDuration(decodeIndex);
				lyricDataSentence.mLyricText = text;
				
				LogUtil.i(TAG, "子句["+dataIdx+"] ===> " + lyricDataSentence.toString());
				
				mSentencesData.add(lyricDataSentence);
				
				useForDecodeIndexValue.add(lyricDataSentence);
			}
			mSentencesDataByDecodeIndex.put(decodeIndex, useForDecodeIndexValue);
			decodeIndex++;
			
		}
		
		LogUtil.i(TAG, "原歌词行数 ：" + mLyricDecoder.mSentencesList.size());
		LogUtil.i(TAG, "索引存储的行数 ：" + mSentencesDataByDecodeIndex.size());
		LogUtil.i(TAG, "解码之后的行数 ：" + mSentencesData.size());
		
	}
	
	private void changePaint(Paint newPaint){
		mDrawPaint.reset();
		mDrawPaint.set(newPaint);
	}
	
	private boolean isCenter(int lyricIndex) {
		return lyricIndex == mCurrentLyricShowIndex;
	}

	/**
	 * 打散文本，将文本按照显示宽度width分段
	 * 
	 * @param text
	 * @param width
	 * @return
	 */
	private List<String> breakText(String text, float width, Paint paint) {
		List<String> list = new ArrayList<String>();
		int count = paint.breakText(text, true, width, null);
		String preText = text.substring(0, count);
		String lastText = text.substring(count);
		if (preText.length() >= text.length()) {
			list.add(preText);
			return list;
		}
		int splitIndex = getLastSplitPosition(preText);
		if (splitIndex > 0 && splitIndex < preText.length()) {
			preText = text.substring(0, splitIndex);
			lastText = text.substring(splitIndex);
		}

		while (!TextUtils.isEmpty(preText)) {
			list.add(preText);
			if (TextUtils.isEmpty(lastText))
				break;
			count = paint.breakText(lastText, true, width, null);
			if (count >= lastText.length()) {
				list.add(lastText);
				return list;
			}
			if (count <= 0)
				preText = null;
			else {
				preText = lastText.substring(0, count);
				text = lastText;
				if (count < 0 || count >= lastText.length())
					lastText = null;
				else
					lastText = lastText.substring(count);
				splitIndex = getLastSplitPosition(preText);
				if (splitIndex > 0 && splitIndex < preText.length()) {
					preText = text.substring(0, splitIndex);
					lastText = text.substring(splitIndex);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取文本中最后一个分割符的位置
	 * 
	 * @param text
	 * @return
	 */
	private int getLastSplitPosition(String text) {
		if (TextUtils.isEmpty(text))
			return -1;
		int pos = -1;
		int posBlank = text.lastIndexOf(" ");
		int posComma = text.lastIndexOf(",");
		int posSemicolon = text.lastIndexOf(";");
		pos = Math.max(posBlank, posComma);
		pos = Math.max(pos, posSemicolon);
		return pos;
	}
	
	private void calculateTime() {
		//中心线和时间
		mLeftTimeText = ConvertUtil.makeTimeString(getContext(), getChildSentenceStartTime() / 1000);
		mRightTimeText = ConvertUtil.makeTimeString(getContext(), (mSongDuration + 500) / 1000);
		mLeftTimeWidth = mTimePaint.measureText(mLeftTimeText);
		mRightTimeWidth = mTimePaint.measureText(mRightTimeText);
		
		mLeftTimeX = getPaddingLeft() + mLeftTimeWidth / 2;
		mRightTimeX = getWidth() - getPaddingRight() - mRightTimeWidth / 2;
		
	}
	
	/**
	 * 更新进入下一次播放
	 * @return
	 */
	private boolean updateNext(){
		//判断是否到了最后一行
		if(!isEnd && mSentencesData.size() == mCurrentLyricShowIndex + 1){
			isEnd = true;
		}
		//从最后一行进入下一次播放
		if(isEnd && mSentencesData.size() > mCurrentLyricShowIndex + 1){
			isEnd = false;
			mTextAutoScrollOffsetY = 0;
			mTextScrollDeltaY = 0;
			return true;
		}
		return false;
	}
	
	public interface OnMoveListener{
		public void onMoveStart();
		public void onMoving();
		public void onMoveEnd(long toTime);
	}

	class LyricDataSentence {
		int mDecoderIndex;//解析数据的索引
		int mDecoderCount;//属于同一个索引的个数
		long mStartTime;
		long mEndTime;
		long mDuration;//持续时间
		
		int mShowListIndex;//显示数据的索引
		/** 数据位置索引 */
		int mDataIndex;
		String mLyricText;//单行歌词
		
		@Override
		public String toString() {
			return "LyricDataSentence [mDecoderIndex=" + mDecoderIndex + ", mDecoderCount=" + mDecoderCount
					+ ", mStartTime=" + mStartTime + ", mEndTime=" + mEndTime + ", mDuration=" + mDuration
					+ ", mShowListIndex=" + mShowListIndex + ", mLyricText=" + mLyricText + "]";
		}
		
	}
	
	static class DelayScrollTask extends HandlerThread {

		public DelayScrollTask(String name) {
			super(name);
		}

		public Handler getHandler(Handler.Callback callback) {
			return new Handler(getLooper(), callback);
		}

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO nothing.
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO nothing.
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO nothing.
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO nothing.
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO nothing.
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		performClick();
		return true;
	}
}
