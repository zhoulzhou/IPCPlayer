package com.example.ipcplayer.thread;

import java.io.IOException;
import java.net.URL;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

/**
 [size=1.8em]Handler+Runnable模式
  我们先看一个并不是异步线程加载的例子，使用 Handler+Runnable模式。
  这里为何不是新开线程的原因请参看这篇文章：Android Runnable 运行在那个线程 这里的代码其实是在UI 主线程中下载图片的，而不是新开线程。
  我们运行下面代码时，会发现他其实是阻塞了整个界面的显示，需要所有图片都加载完成后，才能显示界面。
**/

//StrictMode is a developer tool which detects things you might be doing by accident and brings them to your attention so you can fix them.

//详见StrictMode文档 
//在android 2.3上设计的下载程序，在android 4.0上运行时报android.os.NetworkOnMainThreadException异常，原来在4.0中，访问网络不能在主程序中进行，



public class Main1Activity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main1);
            
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
            .detectDiskReads()  
            .detectDiskWrites()  
            .detectNetwork()   // or .detectAll() for all detectable problems  
            .penaltyLog()  
            .build());  
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
            .detectLeakedSqlLiteObjects()  
//            .detectLeakedClosableObjects()  
            .penaltyLog()  
            .penaltyDeath()  
            .build());  
            
            loadImage("http://www.baidu.com/img/baidu_logo.gif", R.id.imageview1);
            loadImage("http://cache.soso.com/30d/img/web/logo.gif", R.id.imageview3);
            loadImage("http://csdnimg.cn/www/images/csdnindex_logo.gif",
                            R.id.imageview4);
            loadImage("http://images.cnblogs.com/logo_small.gif",
                            R.id.imageview5);
    }

    private Handler handler = new Handler();

    private void loadImage(final String url, final int id) {
            handler.post(new Runnable() {
                    public void run() {
                            Drawable drawable = null;
                            try {
                                    drawable = Drawable.createFromStream(
                                                    new URL(url).openStream(), "image.gif");
                            } catch (IOException e) {
                                    Log.d("test", e.getMessage());
                            }
                            if (drawable == null) {
                                    Log.d("test", "null drawable");
                            } else {
                                    Log.d("test", "not null drawable");
                            }
                            // 为了测试缓存而模拟的网络延时 
                            SystemClock.sleep(2000); 
                            ((ImageView) Main1Activity.this.findViewById(id))
                                            .setImageDrawable(drawable);
                    }
            });
    }
}