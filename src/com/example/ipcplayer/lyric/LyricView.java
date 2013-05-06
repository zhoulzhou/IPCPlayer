package com.example.ipcplayer.lyric;

import java.util.ArrayList;

import com.example.ipcplayer.activity.PlayingActivity;
import com.example.ipcplayer.utils.LogUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;

public class LyricView extends View {
	private final static String TAG = LyricView.class.getSimpleName();
	
	private final static int FONT_TEXT_SIZE = 16;
	private final static int HIGH_LIGHT_FONT_TEXT_SIZE = 16;
	private final static int FONT_TEXT_VERTICAL_SPAC = 3;
	private final static int LYRIC_TEXT_VERTICAL_SPAC = 150;
	
	private Resources mResources;
	private DisplayMetrics mMetrics;
	
	private Paint mNormalPaint;
	private Paint mHighLightPaint;
	private Paint mDrawPaint;

	private ArrayList<LyricSentence> mLyricSentenceList = new ArrayList<LyricSentence>();
	private boolean mIsLoadingLyric = false;
	
	private float mRowSpace;//歌词行间距
	private float mCenterX;
	private float mCenterY;
	private float mFontHeight;
	private float mVisibleWidth;//歌词可见区域的宽度
	private float mVisibleHeight;//歌词可见区域的高度
	private FontMetrics mFontMetrics;
	
	private Rect mDirtyRect;
	
	@SuppressLint("HandlerLeak")
	private  Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if (what == 0) {
				LogUtil.d(TAG + " handle msg");
				invalidate(mDirtyRect);
				mHandler.sendEmptyMessageDelayed(0, 1000);
			}
		}
		
	};
	
	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LyricView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		initView();
		initPaint();
		initFont();
	}
	
	private void initView(){
		mResources = getResources();
		mMetrics = mResources.getDisplayMetrics();
	}
	
	private void initPaint(){
		mNormalPaint = new Paint(Paint.DITHER_FLAG);
		mNormalPaint.setAntiAlias(true);
		mNormalPaint.setColor(Color.WHITE);
		float normalTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, FONT_TEXT_SIZE, mMetrics);
		mNormalPaint.setTextSize(normalTextSize);
		mNormalPaint.setTextAlign(Align.CENTER);
		
		mHighLightPaint = new Paint(Paint.DITHER_FLAG);
		mHighLightPaint.setAntiAlias(true);
		mHighLightPaint.setColor(Color.BLUE);
		float highLightTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, HIGH_LIGHT_FONT_TEXT_SIZE, mMetrics);
		mHighLightPaint.setTextSize(highLightTextSize);
		mHighLightPaint.setTextAlign(Align.CENTER);
		mHighLightPaint.setFakeBoldText(true);
		
		mDrawPaint = new Paint();
		changePaint(mNormalPaint);
	}
	
	private void initFont(){
//		ViewConfiguration viewConfig = ViewConfiguration.get(getContext());
		
		mRowSpace= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FONT_TEXT_VERTICAL_SPAC, mMetrics);
		
		mFontMetrics = mDrawPaint.getFontMetrics();
		mFontHeight = mFontMetrics.top - mFontMetrics.bottom;
		//300 是预设的屏幕宽度 320-2*10
		mVisibleWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, mMetrics);
	}

	private void changePaint(Paint paint){
		mDrawPaint.reset();
		mDrawPaint.set(paint);
	}
	
	public void updateLyric(int state){
		switch(state){
		case PlayingActivity.NO_LYRIC:
			
			break;
		case PlayingActivity.SEARCH_LYRIC:
			
			break;
		case PlayingActivity.LYRIC_READY:
			if (mHandler != null) {
				LogUtil.d(TAG + " lyric ready send msg");
				mHandler.sendEmptyMessage(0);
			}
			break;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		LogUtil.d(TAG + " draw lyric");
		if(mIsLoadingLyric){
			
		}else{
			drawLyric(canvas);
		}
	}
	
	private void drawLyric(Canvas canvas){
		LogUtil.d(TAG + " start draw lyric");
		canvas.save();
		canvas.translate(0, 0);
		float lineOffsetY = mRowSpace;
		
		LyricSentence sentence = new LyricSentence();
		mLyricSentenceList = LyricGetter.get("try.lrc");
		LogUtil.d(TAG + " get lyric sentence");
		for(int dataIdx = 0; dataIdx < mLyricSentenceList.size(); dataIdx ++){
			LogUtil.d(TAG + " get dataIdx= 	" + dataIdx);
			sentence = mLyricSentenceList.get(dataIdx);
			LogUtil.d(TAG + " get sentence= " + sentence.toString());
			if(sentence == null)
				continue;
			String drawString = sentence.getSentence();
			int index = 0 ;
			if(dataIdx != 0){
				lineOffsetY += mFontHeight + mRowSpace;
			}
			if(isCenter(index)){
				changePaint(mHighLightPaint);
			}else{
				changePaint(mNormalPaint);
			}
			
			canvas.drawText(drawString, mCenterX, mCenterY + lineOffsetY, mDrawPaint);
		}
		canvas.restore();
	}

	private boolean isCenter(int index){
		return false;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initDynamicSize(w,h);
	}
	
	private void initDynamicSize(int width, int height){
		mCenterX = width >> 1;
		mCenterY = height >> 1;
		
		mVisibleWidth = width - getPaddingLeft() - getPaddingRight();
		mVisibleHeight = height - getPaddingTop() - getPaddingBottom();
		
		//handle dirtyRect
		mDirtyRect = new Rect((int)(mCenterX - mVisibleWidth / 2), (int)(mCenterY - mVisibleHeight / 2), 
				(int)(mCenterX + mVisibleWidth / 2), (int)(mCenterY + mVisibleHeight / 2));
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	@Override
	public void layout(int l, int t, int r, int b) {
		super.layout(l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}