package com.example.ipcplayer.activity;
  
  
import com.example.ipcplayer.R;

import android.app.Activity;  
import android.os.Bundle;  
import android.os.Handler;  
import android.os.Message;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.animation.AnimationUtils;  
import android.widget.Button;  
import android.widget.ImageView;  
import android.widget.TextView;  
import android.widget.ViewFlipper;  
public class ViewFlipperActivity extends Activity {  
  
    public final static int VIEW_TEXT = 0;  
    public final static int VIEW_IMAGE = 1;  
    boolean autoflag  =  true;  
    Button previous,next,autoStart;  
    ViewFlipper flipper;  
      
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.animation);  
          
        initiaView();  
        //添加视图  
        flipper.addView(addTextByText("动画开始....."));  
        flipper.addView(addImageById(R.drawable.welcome_1));  
        flipper.addView(addImageById(R.drawable.welcome_2));  
        flipper.addView(addImageById(R.drawable.welcome_3));  
        flipper.addView(addImageById(R.drawable.welcome_4));   
        flipper.addView(addTextByText("动画结束....."));  
    }  
      
      
    private void initiaView(){  
//        previous = (Button) findViewById(R.id.start);  
//        next = (Button) findViewById(R.id.nextButton);  
        autoStart = (Button) findViewById(R.id.start);  
          
        flipper = (ViewFlipper) findViewById(R.id.flipper);  
        flipper.setInAnimation(AnimationUtils.loadAnimation(this,  
                android.R.anim.fade_in));  
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,  
                android.R.anim.fade_out));  
          
//        previous.setOnClickListener(listener);  
//        next.setOnClickListener(listener);  
        autoStart.setOnClickListener(listener);  
    }  
      
    private OnClickListener listener = new OnClickListener(){  
        public void onClick(View v) {  
            // TODO Auto-generated method stub  
            switch(v.getId()){  
//            case R.id.previousButton:  
//                flipper.showPrevious();  
//                break;  
                  
//            case R.id.nextButton:  
//                flipper.showNext();  
//                break;  
            case R.id.start:  
                  
                if(autoflag){  //自动播放  
                    flipper.setAutoStart(autoflag);  
                    flipper.startFlipping();  
                    autoStart.setText("停止");  
                    autoflag = false;  
                }else{      //取消自动播放  
                    flipper.stopFlipping();  
                    autoStart.setText("开始");  
                    autoflag = true;  
                }  
                break;    
            }//switch  
        }//onClick  
    };//OnClickListener  
      
    public View addTextByText(String text){  
            TextView tv = new TextView(this);  
            tv.setText(text);  
            tv.setGravity(1);  
            return tv;  
    }  
      
    public View addImageById(int id){  
        ImageView iv = new ImageView(this);  
        iv.setImageResource(id);  
          
        return iv;  
    }  
}  