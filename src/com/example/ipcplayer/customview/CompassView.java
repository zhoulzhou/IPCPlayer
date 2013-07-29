package com.example.ipcplayer.customview;

import com.example.ipcplayer.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View{
	private Paint mMarkerPaint;
	private Paint mTextPaint;
	private Paint mCirclePaint;
	private String mNorthString;
	private String mSouthString;
	private String mEastString;
	private String mWestString;
	private int mTextHeight;
	private float mBearing;
	

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCompassView();
	}
	
	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCompassView();
	}
	public CompassView(Context context) {
		super(context);
		initCompassView();
	}
	
	public float getBearing(){
		return mBearing;
	}
	
	public void setBearing(float bearing){
		this.mBearing = bearing;
	}
	
	private void initCompassView(){
		Resources r = this.getResources();
		mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCirclePaint.setColor(r.getColor(R.color.background_color));
		mCirclePaint.setStrokeWidth(1);
		mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mSouthString = r.getString(R.string.cardinal_south);
		mNorthString = r.getString(R.string.cardinal_north);
		mEastString = r.getString(R.string.cardinal_east);
		mWestString = r.getString(R.string.cardinal_west);
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(r.getColor(R.color.text_color));
		mTextHeight = (int) mTextPaint.measureText("yY");
		mMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mMarkerPaint.setColor(r.getColor(R.color.marker_color));
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int px = getMeasuredWidth()/2;
		int py = getMeasuredHeight()/2;
		int radius = Math.min(px, py);
		
		canvas.drawCircle(px, py, radius, mCirclePaint);
		canvas.rotate(-mBearing,px,py);
		
		int textWidth = (int) mTextPaint.measureText("W");
		int cardinalX = px - textWidth/2;
		int cardinalY = py-radius+mTextHeight;
//		canvas.drawText(mWestString, cardinalX, cardinalY, mTextPaint);
		
		for(int i=0;i<24;i++){
			canvas.drawLine(px, py-radius, px, py-radius+10, mMarkerPaint);
			canvas.save();
			canvas.translate(0, mTextHeight);

	         if (i % 6 == 0) {
	            String dirString = "";
	            switch (i) {
	            case (0): {
	                dirString = mNorthString;
	                int arrowY = 2 * mTextHeight;
	                canvas.drawLine(px, arrowY, px - 5, 3 * mTextHeight,
	                      mMarkerPaint);
	                canvas.drawLine(px, arrowY, px + 5, 3 * mTextHeight,
	                      mMarkerPaint);
	                break;
	            }
	            case (6):
	                dirString = mEastString;
	                break;
	            case (12):
	                dirString = mSouthString;
	                break;
	            case (18):
	                dirString = mWestString;
	                break;
	            }
	            canvas.drawText(dirString, cardinalX, cardinalY, mTextPaint);
	         } else if (i % 3 == 0) {
	 
	            String angle = String.valueOf(i * 15);
	            float angleTextWidth = mTextPaint.measureText(angle);
	            int angleTextX = (int) (px - angleTextWidth / 2);
	            int angleTextY = py - radius + mTextHeight;
	            canvas.drawText(angle, angleTextX, angleTextY, mTextPaint);
	         }
	         canvas.restore();
	         canvas.rotate(15, px, py);
	      }
	      canvas.restore();
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = measure(widthMeasureSpec);
		int measureHeight = measure(heightMeasureSpec);
		int d = Math.min(measureWidth, measureHeight);
		setMeasuredDimension(d, d);
	}
	
	private int measure(int measureSpec){
		int result = 0;
		int model = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		if(model == MeasureSpec.UNSPECIFIED){
			result = 200;
		}else{
			result = specSize;
		}
		
		return  result;
	}
	
	
}