package com.example.ipcplayer.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class Main5Activity extends Activity{
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		 // ����һ�������ù̶��߳������̳߳�
//        ExecutorService pool = Executors.newFixedThreadPool(3);
        // ����ʵ����Runnable�ӿڶ���Thread����ȻҲʵ����Runnable�ӿ�
        for(int i = 0; i < 5; i++){
            // ���̷߳�����н���ִ��
//            pool.execute(new MyThread(i));
            ThreadF.getInstance().submit(new MyThread(i));
        }
        // �ر��̳߳أ���ֹ����Runnable����
//        pool.shutdown();
        ThreadF.getInstance().ShutDownAll();
        
        
        Thread thread = Thread.currentThread();
        System.out.println("Thread-"+ thread.getId() + thread.getName() + thread.toString());
        thread.interrupt();
        thread.isInterrupted();
        
        
        Thread thread100 = new Thread(new DeamonThread(200){

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				 System.out.println("Thread-"+threadId+" ֹͣ����");
				super.stop();
			}
        	
        });
        thread100.start();
        if(thread100.isAlive()){
        	try{
        		thread100.stop();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	
        }
        
        
	}

}

class MyThread extends Thread {
    int threadId = 0;
    public MyThread(int threadId){
        this.threadId = threadId;
        System.out.println("Thread-"+threadId+" ����");
    }
    @Override
    public void run() {
        System.out.println("Thread-"+threadId+" ������");
        int i = 0 ;
        while(i < 10){
        	i++;
        if(threadId == 100){
        	System.out.println("Thread-i "+ i);
        }
        }
    }
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("Thread-"+threadId+" ������");
		super.destroy();
	}
	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		System.out.println("Thread-"+threadId+" �ж�");
		super.interrupt();
	}
	@Override
	public boolean isInterrupted() {
		// TODO Auto-generated method stub
		System.out.println("Thread-"+threadId+" ���ж�");
		return super.isInterrupted();
	}
	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		System.out.println("Thread-"+threadId+" ��ʼ����");
		super.start();
	}
   
}

class DeamonThread implements Runnable{
    int threadId = 0;
	public DeamonThread(int threadId){
		System.out.println("Thread-"+threadId+" ����");
		this.threadId = threadId;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		  System.out.println("Thread-"+threadId+" ������");
	        int i = 0 ;
	        while(i < 10){
	        	i++;
	        if(threadId == 200){
	        	System.out.println("Thread-i "+ i);
	        }
	        }
	}
	public void stop(){
		
	}
}