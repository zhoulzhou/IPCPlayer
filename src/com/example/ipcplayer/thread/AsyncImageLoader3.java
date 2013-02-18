package com.example.ipcplayer.thread;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;

/**
 * Handler+ExecutorService(线程池)+MessageQueue+缓存模式
      下面比起前一个做了几个改造:
      把整个代码封装在一个类中
      为了避免出现同时多次下载同一幅图的问题,使用了本地缓存
      封装的类:
 *说明：
   final参数是指当函数参数为final类型时，你可以读取使用该参数，但是无法改变该参数的值。参看：Java关键字final、static使用总结 
       这里使用SoftReference 是为了解决内存不足的错误（OutOfMemoryError）的，更详细的可以参看：内存优化的两个类:SoftReference 和 WeakReference
 */

public class AsyncImageLoader3 {
    // 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动）
    public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
     
    private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
    private final Handler handler = new Handler();

    /**
     * 
     * @param imageUrl
     *            图像url地址
     * @param callback
     *            回调接口
     * <a href="\"http://www.eoeandroid.com/home.php?mod=space&uid=7300\"" target="\"_blank\"">@return</a> 返回内存中缓存的图像，第一次加载返回null
     */
    public Drawable loadDrawable(final String imageUrl,
                    final ImageCallback callback) {
            // 如果缓存过就从缓存中取出数据
            if (imageCache.containsKey(imageUrl)) {
                    SoftReference<Drawable> softReference = imageCache.get(imageUrl);
                    if (softReference.get() != null) {
                            return softReference.get();
                    }
            }
            // 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
            executorService.submit(new Runnable() {
                    public void run() {
                            try {
                                    final Drawable drawable = loadImageFromUrl(imageUrl); 
                                             
                                    imageCache.put(imageUrl, new SoftReference<Drawable>(
                                                    drawable));

                                    handler.post(new Runnable() {
                                            public void run() {
                                                    callback.imageLoaded(drawable);
                                            }
                                    });
                            } catch (Exception e) {
                                    throw new RuntimeException(e);
                            }
                    }
            });
            return null;
    }

    // 从网络上取数据方法
    protected Drawable loadImageFromUrl(String imageUrl) {
            try {
                    // 测试时，模拟网络延时，实际时这行代码不能有
                    SystemClock.sleep(2000);

                    return Drawable.createFromStream(new URL(imageUrl).openStream(),
                                    "image.png");

            } catch (Exception e) {
                    throw new RuntimeException(e);
            }
    }

    // 对外界开放的回调接口
    public interface ImageCallback {
            // 注意 此方法是用来设置目标对象的图像资源
            public void imageLoaded(Drawable imageDrawable);
    }
}