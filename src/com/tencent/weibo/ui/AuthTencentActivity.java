package com.tencent.weibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatActivity;
import com.baidu.news.setting.SettingManager;
import com.baidu.news.setting.SettingManagerFactory;
import com.baidu.news.ui.SettingController;
import com.baidu.news.ui.TypefaceHelper;
import com.baidu.news.ui.widget.LoadingView;
import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.LogUtil;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.weibo.net.WeiboAuthListener;
import com.weibo.net.WeiboException;

/**
 * 腾讯微博授权界面
 * 
 * @author yuankai
 * @version 1.0
 * @data 2012-8-23
 */
public class AuthTencentActivity extends Activity implements OnClickListener
{
	public final static int RESULT_CODE = 2;
	
	private static final String TAG = "OAuthV2AuthorizeWebView";
    private OAuthV2 oAuth;
    
    private SettingManager mSettingManager;
    private String mUrl;
    private WeiboAuthListener mListener;
    private LoadingView mLoadingView;
    private ImageButton mCloseBtn;
    private ImageButton mRefreshBtn;
    private WebView mWebView;
    private TextView mTitleTxt;
    private long mForegroundStartTime = 0;
	private AuthTencentController mAuthTencentController;
	private boolean mIsFirstLoading = true;
	private Handler mHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auth_tencent);
		
		getWindow().setBackgroundDrawableResource(R.color.list_bg_color);
		
		mSettingManager = (SettingManager) (SettingManager) SettingManagerFactory.getComponent(getApplicationContext());
		mSettingManager.setTencentAccessToken(null);
        mSettingManager.setTencentOpenId(null);
        mSettingManager.setTencentOpenKey(null);
		
		Intent intent = this.getIntent();
        oAuth = (OAuthV2) intent.getExtras().getSerializable("oauth");
        mUrl = OAuthV2Client.generateImplicitGrantUrl(oAuth);
        mIsFirstLoading = true;
		setupViews();
		mAuthTencentController = new AuthTencentController(getApplicationContext(), mHandler);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mForegroundStartTime = System.currentTimeMillis();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mAuthTencentController != null) {
			mAuthTencentController.setAppForegroundTime(System.currentTimeMillis() - mForegroundStartTime);			
		}
	}		
	
	private void setupViews()
	{
		mTitleTxt = (TextView)findViewById(R.id.title);
//		mTitleTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());
		mLoadingView = (LoadingView)findViewById(R.id.loading);
		mLoadingView.setOnClickListener(this);
		
		mCloseBtn = (ImageButton)findViewById(R.id.close);
		mCloseBtn.setOnClickListener(this);
		
		mRefreshBtn = (ImageButton)findViewById(R.id.refresh);
		mRefreshBtn.setOnClickListener(this);
		
		mWebView = (WebView)findViewById(R.id.webview);
		mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WeiboWebViewClient());
        mWebView.loadUrl(mUrl);
        mWebView.requestFocus();
        mWebView.setVisibility(View.VISIBLE);
        mLoadingView.startLoading();
	}
	
	private class WeiboWebViewClient extends WebViewClient {
		 @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            LogUtil.d(TAG, "Redirect URL: " + url);
	            // 待后台增加对默认重定向地址的支持后修改下面的逻辑
	            if (url.indexOf("access_token=") != -1) {
	                int start=url.indexOf("access_token=");
	                String responseData=url.substring(start);
	                LogUtil.d("responseData = " + responseData);
	                OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuth);
	                mSettingManager.setTencentAccessToken(responseData);
	                Intent intent = new Intent();
	                intent.putExtra("oauth", oAuth);
	                setResult(RESULT_CODE, intent);
	                view.destroyDrawingCache();
	                view.destroy();
	                finish();
	                return true;
	            }else if(url.indexOf("checkType=error") != -1){
	        		if(mListener != null){
						mListener.onCancel();
					}
	        		finish();
	            }else{
	            	mWebView.loadUrl(mUrl);
	            }
	            return true;
	        }
		/**
         * 回调方法，当页面开始加载时执行
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	LogUtil.i(TAG, "WebView onPageStarted...");
        	LogUtil.i(TAG, "URL = " + url);
            if (url.indexOf("access_token=") != -1) {
                int start=url.indexOf("access_token=");
                String responseData=url.substring(start);
                LogUtil.d("responseData = " + responseData);
                OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuth);
                mSettingManager.setTencentAccessToken(responseData);
                mSettingManager.setTencentOpenId(oAuth.getOpenid());
                mSettingManager.setTencentOpenKey(oAuth.getOpenkey());
                Intent intent = new Intent();
                intent.putExtra("oauth", oAuth);
                setResult(RESULT_CODE, intent);
                view.destroyDrawingCache();
                view.destroy();
                mLoadingView.stopLoading();
                finish();
                return;
            }else{
            	if(!mIsFirstLoading){
                	mLoadingView.setLoadingTextVisable(View.GONE);
                	mLoadingView.setDefaultImageViewVisable(View.GONE);
                	mLoadingView.startLoading();
            	}
            	if(mIsFirstLoading){
            		mIsFirstLoading = false;
            	}
            }
            super.onPageStarted(view, url, favicon);
        }
        
        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mLoadingView.onError(getString(R.string.load_fail));
            
			if(mListener != null){
				mListener.onWeiboException(new WeiboException(errorCode));
			}			
        }
        
        @Override
        public void onPageFinished(WebView view, String url)
        {
        	// TODO Auto-generated method stub
        	super.onPageFinished(view, url);
            mLoadingView.stopLoading();
            mWebView.setVisibility(View.VISIBLE);
        }

        /*
         * TODO Android2.2及以上版本才能使用该方法 
         * 目前https://open.t.qq.com中存在http资源会引起sslerror，待网站修正后可去掉该方法
         */
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if ((null != view.getUrl()) && (view.getUrl().startsWith("https://open.t.qq.com"))) {
                handler.proceed();// 接受证书
            } else {
                handler.cancel(); // 默认的处理方式，WebView变成空白页
            }
            mLoadingView.onError(getString(R.string.load_fail));
            // handleMessage(Message msg); 其他处理
        }

    }

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.close:
				if(mListener != null)
				{
					mListener.onCancel();
				}
				finish();
				break;
				
			case R.id.refresh:
			case R.id.empty_view:
				mWebView.loadUrl(mUrl);
				break;
		}
	}
}
