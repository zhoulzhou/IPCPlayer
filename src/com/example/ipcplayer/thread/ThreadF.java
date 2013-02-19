package com.example.ipcplayer.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.ipcplayer.utils.LogUtil;

public class ThreadF{
	private final static String TAG = ThreadF.class.getSimpleName();
	private final static int THREADSIZE = 5;
	private final static int PRIORITY = Thread.NORM_PRIORITY;
	private static ThreadF sInstance ;
	private static ExecutorService mExecutor;
	
	public static ThreadF getInstance(){
		if(sInstance == null){
			synchronized(ThreadF.class){
				sInstance = new ThreadF();
			}
			mExecutor = createExecutor();
		}
		return sInstance;
	}
	
	private static  ExecutorService createExecutor(){
		//use FIFO queue
		//    LIFO LIFOLinkedBlockingQueue<Runnable>();
		BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
		ThreadFactory threadFactory = new ThreadFactory(){
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setPriority(PRIORITY);
					return t;
				}
		};
		return new ThreadPoolExecutor(THREADSIZE, THREADSIZE, 0L, TimeUnit.MILLISECONDS, taskQueue, threadFactory);
	}
	
	public void submit(Runnable task){
		LogUtil.d(TAG + " submit task ");
		mExecutor.submit(task);
	}
	
	public void ShutDownAll(){
		LogUtil.d(TAG + " shutdown all thread ");
		mExecutor.shutdown();
//		mExecutor.shutdownNow();
	}
}