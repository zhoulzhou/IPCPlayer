package com.example.ipcplayer.activity;

import java.io.File;
import java.io.IOException;

import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DirActivity extends Activity{

	private static final String TAG = DirActivity.class.getSimpleName();
	private TextView mText1;
	private TextView mText3;
	private TextView mText5;
	private TextView mText7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dir);
		
	    mText1 = (TextView) findViewById(R.id.text1);
	    mText3 = (TextView) findViewById(R.id.text3);
	    mText5 = (TextView) findViewById(R.id.text5);
	    mText7 = (TextView) findViewById(R.id.text7);
	    
	    String path = FileUtil.getIPCDownloadDir().getAbsolutePath();
	    mText1.setText(" path = " + path);
	    File dir = new File(path);
	    if(dir.canRead()){
	    	mText7.setText(" dir read-only ");
	    }
	    
	    if(dir.canWrite()){
	    	mText7.setText(" dir can write  ");
	    }
	    
	    if(!dir.exists()){
	    	//if create one-deep directory use mkdir 
	    	//if create dulti-deep directory use mkdirs
	    	if(dir.mkdirs()){
	    		mText3.setText(" create dir successfully ");
	    	}else {
	    		mText3.setText(" create dir fail ");
	    	}
	    }
	    
	    File file = new File(path + File.separator + "text.ini");
	    if(!file.exists()){
	    	try {
				if(file.createNewFile()){
					mText3.setText(" create file successfully ");
				}else {
					mText3.setText(" create file fail ");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				Toast.makeText(this, " file fail ", Toast.LENGTH_LONG).show();
				e.printStackTrace();
				mText5.setText(" create file fail exception = " + e);
			}
	    }
	}
	
}