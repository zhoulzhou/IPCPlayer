package com.example.ipcplayer.thread;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

/**
 Handler+ExecutorService(线程池)+MessageQueue模式
  能开线程的个数毕竟是有限的，我们总不能开很多线程，对于手机更是如此。
  这个例子是使用线程池。Android拥有与Java相同的ExecutorService实现，我们就来用它。
  线程池的基本思想还是一种对象池的思想，开辟一块内存空间，里面存放了众多(未死亡)的线程，池中线程执行调度由池管理器来处理。当有线程任务时，从池中取一个，执行完成后线程对象归池，这样可以避免反复创建线程对象所带来的性能开销，节省了系统的资源。
  线程池的信息可以参看这篇文章：Java&Android的线程池－ExecutorService 下面的演示例子是创建一个可重用固定线程数的线程池。
  这里我们象第一步一样使用了 handler.post(new Runnable() {  更新前段显示当然是在UI主线程，我们还有 executorService.submit(new Runnable() { 来确保下载是在线程池的线程中。
**/

public class Main3Activity extends Activity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main1);
                loadImage3("http://www.baidu.com/img/baidu_logo.gif", R.id.imageview1);
                loadImage3("http://www.chinatelecom.com.cn/images/logo_new.gif",
                                R.id.imageview2);
                loadImage3("http://cache.soso.com/30d/img/web/logo.gif",
                                R.id.imageview3);
                loadImage3("http://csdnimg.cn/www/images/csdnindex_logo.gif",
                                R.id.imageview4);
                loadImage3("http://images.cnblogs.com/logo_small.gif",
                                R.id.imageview5);
        }
 
        private Handler handler = new Handler();
 
        private ExecutorService executorService = Executors.newFixedThreadPool(5);
 
        // 引入线程池来管理多线程
        private void loadImage3(final String url, final int id) {
                executorService.submit(new Runnable() {
                        public void run() {
                                try {
                                        final Drawable drawable = Drawable.createFromStream(
                                                        new URL(url).openStream(), "image.png");
                                        // 模拟网络延时
                                        SystemClock.sleep(2000);
                                        handler.post(new Runnable() {
                                                public void run() {
                                                        ((ImageView) Main3Activity.this.findViewById(id))
                                                                        .setImageDrawable(drawable);
                                                }
                                        });
                                } catch (Exception e) {
                                        throw new RuntimeException(e);
                                }
                        }
                });
        }
}