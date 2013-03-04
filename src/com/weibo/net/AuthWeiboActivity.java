package com.weibo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ipcplayer.R;
import com.example.ipcplayer.setting.SettingManager;
import com.example.ipcplayer.setting.SettingManagerFactory;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;
import com.tencent.weibo.ui.LoadingView;
import com.tencent.weibo.ui.ShareTencentController;

/**
 * 新浪微博授权界面
 * 
 * @version 1.0
 * @data 2012-8-21
 */
public class AuthWeiboActivity extends Activity implements OnClickListener
{
	public final static String KEY_URL = "url";

	private Weibo mWeibo;
	private String mUrl;
	private WeiboAuthListener mListener;
	private ImageView mCloseBtn;
	private ImageButton mRefreshBtn;
	private WebView mWebView;
	private TextView mTitleTxt;
	private LoadingView mLoadingView = null;

	private SettingManager mSettingManager;
	private long mForegroundStartTime = 0;
	private AuthWeiboController mAuthWeiboController;
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
		setContentView(R.layout.auth_weibo);

		getWindow().setBackgroundDrawableResource(R.color.list_bg_color);

		mSettingManager = (SettingManager) SettingManagerFactory.getComponent(getApplicationContext());
		mSettingManager.setSinaAccessToken(null);
		mSettingManager.setSinaExpireIn(null);
		mSettingManager.setSinaUid(null);

		mUrl = getIntent().getStringExtra(KEY_URL);
		mWeibo = Weibo.getInstance();
		mListener = mWeibo.getAuthListener();
		if(mWeibo == null || mListener == null || StringUtil.isEmpty(mUrl))
		{
			finish();
			return;
		}

		setupViews();
		mAuthWeiboController = new AuthWeiboController(getApplicationContext(), mHandler);
		mIsFirstLoading = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		mForegroundStartTime = System.currentTimeMillis();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mAuthWeiboController != null) {
			mAuthWeiboController.setAppForegroundTime(System.currentTimeMillis() - mForegroundStartTime);			
		}
	} 	

	private void setupViews()
	{
		mTitleTxt = (TextView)findViewById(R.id.title);
		//		mTitleTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());

		mCloseBtn = (ImageView)findViewById(R.id.close);
		mCloseBtn.setOnClickListener(this);

		mRefreshBtn = (ImageButton)findViewById(R.id.refresh);
		mRefreshBtn.setOnClickListener(this);

		mWebView = (WebView)findViewById(R.id.webview);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WeiboWebViewClient());
		mLoadingView = (LoadingView)findViewById(R.id.loading);
		mLoadingView.setOnClickListener(this);
		mWebView.loadUrl(mUrl);
		mWebView.setVisibility(View.VISIBLE);
		mLoadingView.startLoading();
	}

	private class WeiboWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtil.d("weibo", "shouldOverrideUrlLoading URL: " + url);
            if (url.startsWith(mWeibo.getRedirectUrl())) {
                handleRedirectUrl(view, url);
                finish();
                return true;
            }
			/*
			if(url.startsWith(Weibo.URL_OAUTH2_CALLBACK_CODE)){
				new TokenTask().execute(url);
			}else if(url.indexOf("error_code=21330") != -1){
				if(mListener != null){
					mListener.onCancel();
				}
				finish();	
			}else{
				//mWebView.loadUrl(mUrl);
			}*/
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description,
				String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			LogUtil.d("weibo", "onReceivedError..");
			mListener.onError(new DialogError(description, errorCode, failingUrl));
			mLoadingView.onError(getString(R.string.load_fail));
			if(mListener != null){
				mListener.onWeiboException(new WeiboException(errorCode));
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			LogUtil.d("weibo","onPageStarted URL: " + url);
			super.onPageStarted(view, url, favicon);
			 if (url.startsWith(mWeibo.getRedirectUrl())) {
	            	LogUtil.d("handleRedirectUrl = " + url);
	                handleRedirectUrl(view, url);
	                mLoadingView.stopLoading();
	                finish();
	                return;
	         }
			/*
			if(url.startsWith(Weibo.URL_OAUTH2_CALLBACK_CODE)){
				new TokenTask().execute(url);
			}
			*/
			if(!mIsFirstLoading){
            	mLoadingView.setLoadingTextVisible(View.GONE);
            	mLoadingView.setDefaultImageViewVisible(View.GONE);
            	mLoadingView.startLoading();
        	}
        	if(mIsFirstLoading){
        		mIsFirstLoading = false;
        	}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			LogUtil.d( "onPageFinished URL: " + url);
			super.onPageFinished(view, url);
			mLoadingView.stopLoading();
			mWebView.setVisibility(View.VISIBLE);
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			mLoadingView.onError(getString(R.string.load_fail));
			handler.proceed();
		}
	}

	private static String getHtml(String urlString) {
		try {
			StringBuffer html = new StringBuffer();
			SocketAddress sa = new InetSocketAddress("10.75.0.103", 8093);
			Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP, sa);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp);
			}
			br.close();
			isr.close();
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
		case R.id.loading:
			mWebView.loadUrl(mUrl);
			break;
		}
	}

	@Override
	public boolean	onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if(mListener != null){
				mListener.onCancel();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private String requestToken(String callbackCodeUrl) throws Exception{
		LogUtil.d("weibo", "getToken..");
		HttpPost httppost = new HttpPost(Weibo.URL_OAUTH2_ACCESS_TOKEN); 
		Bundle data = Utility.parseUrl(callbackCodeUrl);
		String code = data.getString("code");
		List <NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id", Weibo.getAppKey()));
		params.add(new BasicNameValuePair("client_secret", Weibo.getAppSecret()));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("redirect_uri", mWeibo.getRedirectUrl()));
		params.add(new BasicNameValuePair("code",code));
		httppost.setEntity(new UrlEncodedFormEntity(params)); 
		HttpResponse response=new DefaultHttpClient().execute(httppost); 
		String result = EntityUtils.toString(response.getEntity());
		LogUtil.d("weibo", "result: "+result);
		return result; 
	}

	private class TokenTask extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params){
			try{
				return requestToken(params[0]);
			}catch(Exception e){
				LogUtil.d("weibo", "exception: "+e.toString());
				return null;
			}
		}
		@Override
		protected void onPostExecute(String result){
			parseToken(result);
			mLoadingView.stopLoading();
			finish();
		}
	}
	
	private void parseToken(String responseStr){
		if(responseStr == null){
			mListener.onCancel();
		}
		try{
			LogUtil.d("weibo", "reponseStr: "+responseStr);
			JSONObject jsonObj = new JSONObject(responseStr);
			String error = jsonObj.optString("error",null);
			String error_code = jsonObj.optString("error_code", null);
			if (error == null && mListener != null) {
				Bundle values = new Bundle();
				values.putString("access_token", jsonObj.getString("access_token"));
				values.putString("expires_in", jsonObj.getString("expires_in"));
				values.putString("remind_in", jsonObj.getString("uid"));
				values.putString("uid", jsonObj.getString("remind_in"));		
				mListener.onComplete(values);
			} else if (error.equals("access_denied")) {
				mListener.onCancel();
			} else {
				if(error_code != null){
					error_code = "10001";
				}
				mListener.onWeiboException(new WeiboException(error, Integer.parseInt(error_code)));
			}
		}catch(JSONException e){
			mListener.onWeiboException(new WeiboException(e.toString()));
		}
	}
	
	 private void handleRedirectUrl(WebView view, String url) {
	        Bundle values = Utility.parseUrl(url);

	        String error = values.getString("error");
	        String error_code = values.getString("error_code");

	        if (error == null && error_code == null && mListener != null) {
	            mListener.onComplete(values);
	        } else if (error.equals("access_denied")) {
	            // 用户或授权服务器拒绝授予数据访问权限
	            mListener.onCancel();
	        } else {
	            mListener.onWeiboException(new WeiboException(error, Integer.parseInt(error_code)));
	        }
	    }

}
