package com.example.ipcplayer.thread;

import java.io.IOException;
import java.net.URL;

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
 * Handler+Thread+Message模式
       这种模式使用了线程，所以可以看到异步加载的效果。
 * 这时候我们可以看到实现了异步加载， 界面打开时，五个ImageView都是没有图的，然后在各自线程下载完后才把图自动更新上去。
 *
 */

public class Main2Activity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main1);
            loadImage2("http://www.baidu.com/img/baidu_logo.gif", R.id.imageview1);
            loadImage2("http://www.chinatelecom.com.cn/images/logo_new.gif",
                            R.id.imageview2);
            loadImage2("http://cache.soso.com/30d/img/web/logo.gif", R.id.imageview3);
            loadImage2("http://csdnimg.cn/www/images/csdnindex_logo.gif",
                            R.id.imageview4);
            loadImage2("http://images.cnblogs.com/logo_small.gif",
                            R.id.imageview5);
    }

    final Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                    ((ImageView) Main2Activity.this.findViewById(msg.arg1))
                                    .setImageDrawable((Drawable) msg.obj);
            }
    };

    // 采用handler+Thread模式实现多线程异步加载
    private void loadImage2(final String url, final int id) {
            Thread thread = new Thread() {
                    @Override
                    public void run() {
                            Drawable drawable = null;
                            try {
                                    drawable = Drawable.createFromStream(
                                                    new URL(url).openStream(), "image.png");
                            } catch (IOException e) {
                                    Log.d("test", e.getMessage());
                            }

                            // 模拟网络延时
                            SystemClock.sleep(2000);

                            Message message = handler2.obtainMessage();
                            message.arg1 = id;
                            message.obj = drawable;
                            handler2.sendMessage(message);
                    }
            };
            thread.start();
            thread = null;
    }

}