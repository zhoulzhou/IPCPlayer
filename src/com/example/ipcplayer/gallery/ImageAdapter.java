package com.example.ipcplayer.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private Integer[] mImageIds;
    private ImageView[] mImages;
    
    public ImageAdapter(Context c, Integer[] ImageIds) {
       mContext = c;
       mImageIds = ImageIds;
       mImages = new ImageView[mImageIds.length];
    }
    
    /**
     * 创建倒影效果
     * @return
     */
    public boolean createReflectedImages() {
       //倒影图和原图之间的距离
       final int reflectionGap = 4;
       int index = 0;
       for (int imageId : mImageIds) {
    	   //返回原图解码之后的bitmap对象
           Bitmap originalImage = BitmapFactory.decodeResource(mContext
                  .getResources(), imageId);
           int width = originalImage.getWidth();
           int height = originalImage.getHeight();
           
           Matrix matrix = new Matrix();
           matrix.preScale(1, -1);//指定矩阵(x轴不变，y轴相反)
           
           //将矩阵应用到该原图之中，返回一个宽度不变，高度为原图1/2的倒影位图
           Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                  height / 2, width, height / 2, matrix, false);
           
           //创建一个宽度不变，高度为原图+倒影图高度的位图
           Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                  (height + height / 2), Config.ARGB_8888);
           
           //将上面创建的位图初始化到画布
           Canvas canvas = new Canvas(bitmapWithReflection);
           canvas.drawBitmap(originalImage, 0, 0, null);
           
           Paint deafaultPaint = new Paint();
           canvas.drawRect(0, height, width, height + reflectionGap,
                  deafaultPaint);
           canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
           
           Paint paint = new Paint();
           /**
            * 参数一:为渐变起初点坐标x位置，
            * 参数二:为y轴位置，
            * 参数三和四:分辨对应渐变终点，
            * 最后参数为平铺方式，
            * 这里设置为镜像Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变
            */
           LinearGradient shader = new LinearGradient(0,
                  originalImage.getHeight(), 0,
                  bitmapWithReflection.getHeight() + reflectionGap,
                  0x70ffffff, 0x00ffffff, TileMode.CLAMP);
           paint.setShader(shader);//设置阴影
           paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
           //用已经定义好的画笔构建一个矩形阴影渐变效果
           canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                  + reflectionGap, paint);
           
           //创建一个ImageView用来显示已经画好的bitmapWithReflection
           ImageView imageView = new ImageView(mContext);
           imageView.setImageBitmap(bitmapWithReflection);
           imageView.setLayoutParams(new GalleryFlow.LayoutParams(180, 240));
           mImages[index++] = imageView;
       }
       return true;
    }
 
    public int getCount() {
       return mImageIds.length;
    }
    
    public Object getItem(int position) {
       return position;
    }
    
    public long getItemId(int position) {
       return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
       return mImages[position];
    }
    
    public float getScale(boolean focused, int offset) {
       return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
    }
}