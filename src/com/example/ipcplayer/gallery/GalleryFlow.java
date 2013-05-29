package com.example.ipcplayer.gallery;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {
    private Camera mCamera = new Camera();
    /**
     * 最大转动角度
     */
    private int mMaxRotationAngle = 60;
    /**
     * 最大缩放值
     */
    private int mMaxZoom = -120;
    /**
     * 半径值
     */
    private int mCoveflowCenter;
    
    public GalleryFlow(Context context) {
            super(context);
            //支持转换 ,执行getChildStaticTransformation方法
            this.setStaticTransformationsEnabled(true);
    }
    
    public GalleryFlow(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setStaticTransformationsEnabled(true);
    }
    
    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.setStaticTransformationsEnabled(true);
    }
    
    public int getMaxRotationAngle() {
            return mMaxRotationAngle;
    }
    
    public void setMaxRotationAngle(int maxRotationAngle) {
            mMaxRotationAngle = maxRotationAngle;
    }
    
    public int getMaxZoom() {
            return mMaxZoom;
    }
    
    public void setMaxZoom(int maxZoom) {
            mMaxZoom = maxZoom;
    }
    
    /**
     * 获得gallery的中心值
     * @return int
     */
    private int getCenterOfCoverflow() {
            return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                            + getPaddingLeft();
    }
    
    /**
     * 获得view的中心值
     * @param view
     * @return int
     */
    private static int getCenterOfView(View view) {
            return view.getLeft() + view.getWidth() / 2;
    }
    
    /* 
     * 控制gallery中每个图片的旋转(重写的gallery中方法)
     * @see android.widget.Gallery#getChildStaticTransformation(android.view.View, android.view.animation.Transformation)
     */
    protected boolean getChildStaticTransformation(View child, Transformation t) {
    	    //取得当前子view的半径值
            final int childCenter = getCenterOfView(child);
            final int childWidth = child.getWidth();
            int rotationAngle = 0;//旋转角度
            t.clear(); //重置转换状态
            t.setTransformationType(Transformation.TYPE_MATRIX); //设置转换类型
            //如果图片位于中心位置不需要进行旋转
            if (childCenter == mCoveflowCenter) {
                    transformImageBitmap((ImageView) child, t, 0);
            } else {
            	    //根据图片在gallery中的位置来计算图片的旋转角度
                    rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
                    //如果旋转角度绝对值大于最大旋转角度返回（-mMaxRotationAngle或mMaxRotationAngle;）
                    if (Math.abs(rotationAngle) > mMaxRotationAngle) {
                            rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
                                            : mMaxRotationAngle;
                    }
                    transformImageBitmap((ImageView) child, t, rotationAngle);
            }
            return true;
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mCoveflowCenter = getCenterOfCoverflow();
            super.onSizeChanged(w, h, oldw, oldh);
    }
    
    private void transformImageBitmap(ImageView child, Transformation t,
                    int rotationAngle) {
            mCamera.save(); //对效果进行保存
            final Matrix imageMatrix = t.getMatrix();
            final int imageHeight = child.getLayoutParams().height;
            final int imageWidth = child.getLayoutParams().width;
            final int rotation = Math.abs(rotationAngle);
            // 在Z轴上正向移动camera的视角，实际效果为放大图片。
            // 在Y轴上移动，上下移动；X轴上左右移动。
            mCamera.translate(0.0f, 0.0f, 100.0f);
            if (rotation < mMaxRotationAngle) {
                    float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
                    mCamera.translate(0.0f, 0.0f, zoomAmount);
            }
            // 在Y轴上旋转，竖向向里翻转。
            // 在X轴上旋转，则横向向里翻转。
            mCamera.rotateY(rotationAngle);
            mCamera.getMatrix(imageMatrix);
            imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
            imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
            mCamera.restore();
    }
}