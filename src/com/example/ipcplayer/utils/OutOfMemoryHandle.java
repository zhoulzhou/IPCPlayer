package com.example.ipcplayer.utils;

import android.os.Debug;

/**
 * 内存溢出处理器
 * 
 * @version 1.0
 */
public class OutOfMemoryHandle
{	
	/**
	 * 回收阈值，目前定为13M左右
	 */
	public final static long TRESHOLD_HEAP_SIZE = 20400000L;
	
	/**
	 * 判断当前所分配的空间，是否达到一个阈值，如果是，则调用GC回收
	 */
	public static void gcIfAllocateOutOfHeapSize()
	{
		if((Debug.getNativeHeapAllocatedSize() / 1024) >= TRESHOLD_HEAP_SIZE)
		{
			handle();
		}
	}
	
	/**
	 * 内存溢出统一处理类，在此方法中加入对异常的处理
	 */
	public static void handle()
	{
		System.gc();
	}
}
