package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.gallery.GalleryFlow;
import com.example.ipcplayer.gallery.ImageAdapter;

import android.app.Activity;
import android.os.Bundle;
public class GalleryActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gallery);
        Integer[] images = { R.drawable.welcome_1, R.drawable.welcome_2,
                R.drawable.runner, R.drawable.welcome_3, R.drawable.welcome_4,
                R.drawable.welcome_1, R.drawable.welcome_2,
                R.drawable.runner, R.drawable.welcome_3, R.drawable.welcome_4,
        };  //定义图片数组
        ImageAdapter adapter = new ImageAdapter(this, images);
        adapter.createReflectedImages();
        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
        galleryFlow.setAdapter(adapter);   
  }
}