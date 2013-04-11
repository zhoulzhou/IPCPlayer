/*
 * Copyright (C) 2008 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.ipcplayer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 单个屏幕布局
 * 
 * @author yuankai
 * @version 1.0
 */
public class CellLayout extends RelativeLayout
{
	/**
	 * 
	 * @param context
	 *            context
	 */
	public CellLayout(Context context)
	{
		this(context, null);
	}
	
	/**
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            xml属性集
	 */
	public CellLayout(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	/**
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            xml属性集
	 * @param defStyle
	 *            默认属性
	 */
	public CellLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setAlwaysDrawnWithCacheEnabled(false);
//		buildDrawingCache(true);
	}
	
	// ADW: make dispatchDraw available to Launcher for creating previews
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		final int count = getChildCount();
		final long drawingTime = getDrawingTime();
		for (int i = 0; i < count; i++)
		{
			final View childView = getChildAt(i);
			if (childView != null && childView.getVisibility() == VISIBLE)
			{
				drawChild(canvas, childView, drawingTime);
			}
		}
	}
	
	@Override
	public void requestChildFocus(View child, View focused)
	{
		super.requestChildFocus(child, focused);
		if (child != null)
		{
			Rect r = new Rect();
			child.getDrawingRect(r);
			requestRectangleOnScreen(r);
		}
	}
	
	@Override
	public void setChildrenDrawingCacheEnabled(boolean enabled)
	{
		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			final View view = getChildAt(i);
			// TODO 设置缓存图片质量
			view.setDrawingCacheEnabled(enabled);
			view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
			// Update the drawing caches
			// TODO 此处时间花得比较久
			view.buildDrawingCache(true);
		}
	}
	
	@Override
	public void setChildrenDrawnWithCacheEnabled(boolean enabled)
	{
		super.setChildrenDrawnWithCacheEnabled(enabled);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	synchronized void recycleBitmap()
	{
		destroyDrawingCache();
		setDrawingCacheEnabled(false);
	}
	
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
	{
		return p instanceof CellLayout.LayoutParams;
	}
	
	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p)
	{
		return new CellLayout.LayoutParams(p);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
