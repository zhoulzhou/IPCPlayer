package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.ui.AuthTencentActivity;
import com.tencent.weibo.ui.ShareTencentActivity;

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
				
			}
			
		});
		mBind_Tencent = (Button) findViewById(R.id.tencent_bind);
		mBind_Tencent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
//				OAuthV2 oAuthV2Tencent;
//	    		oAuthV2Tencent=new OAuthV2(redirectUri);
//	    		oAuthV2Tencent.setClientId(clientId);
//	    		oAuthV2Tencent.setClientSecret(clientSecret);	
//	    		
//	    		Intent intent = new Intent(mContext, AuthTencentActivity.class);
//				intent.putExtra("oauth", oAuthV2Tencent);
//				startActivityForResult(intent, REQUEST_AUTH);
				
				Intent intent = new Intent(mContext, ShareTencentActivity.class);
				intent.putExtra(ShareTencentActivity.CONTENT, "share ipc");
				intent.putExtra(ShareTencentActivity.EXTRA_PIC_URI, "pic url");
				intent.putExtra(ShareTencentActivity.EXTRA_TITLE, "title");
				intent.putExtra(ShareTencentActivity.EXTRA_URL, "news url");
				startActivity(intent);
			}
			
		});
	}
	
}