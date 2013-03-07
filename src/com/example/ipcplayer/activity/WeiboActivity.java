package com.example.ipcplayer.activity;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.ui.AuthTencentActivity;
import com.tencent.weibo.ui.ShareTencentActivity;
import com.weibo.net.ShareWeiboActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WeiboActivity extends Activity{
	private Button mBind_Sina;
	private Button mBind_Tencent;
	private Context mContext;

	private String redirectUri="http://news.baidu.com";                   
    private String clientId = "801225441"; 
    private String clientSecret="c00863592e4e35dd751b8b94836694682429410262";	
	
    private final static int REQUEST_AUTH = 200;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_main);
		
		mContext = this;
		mBind_Sina = (Button) findViewById(R.id.sina_bind);
		mBind_Sina.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ArrayList<String> picUrls =new ArrayList<String>();
				picUrls.add("icp pic url 1");
				picUrls.add("ipc pic url 2");
				Intent intent = new Intent(mContext, ShareWeiboActivity.class);
				intent.putExtra(ShareWeiboActivity.CONTENT, "ipc share content");
				intent.putExtra(ShareWeiboActivity.EXTRA_PIC_URI, picUrls);
				intent.putExtra(ShareWeiboActivity.EXTRA_TITLE, "ipc title");
				intent.putExtra(ShareWeiboActivity.EXTRA_URL, "ipc url");
				startActivity(intent);
			}
			
		});
		mBind_Tencent = (Button) findViewById(R.id.tencent_bind);
		mBind_Tencent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ArrayList<String> picUrls =new ArrayList<String>();
				picUrls.add("icp pic url 1");
				picUrls.add("ipc pic url 2");
				Intent intent = new Intent(mContext, ShareTencentActivity.class);
				intent.putExtra(ShareTencentActivity.CONTENT, "ipc share content");
				intent.putExtra(ShareTencentActivity.EXTRA_PIC_URI, picUrls);
				intent.putExtra(ShareTencentActivity.EXTRA_TITLE, "ipc title");
				intent.putExtra(ShareTencentActivity.EXTRA_URL, "ipc url");
				startActivity(intent);
			}
			
		});
	}
	
}