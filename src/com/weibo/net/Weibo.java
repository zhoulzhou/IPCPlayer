/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.weibo.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.setting.SettingManager;
import com.example.ipcplayer.setting.SettingManagerFactory;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

/**
 * Encapsulation main Weibo APIs, Include: 1. getRquestToken , 2.
 * getAccessToken, 3. url request. Used as a single instance class. Implements a
 * weibo api as a synchronized way.
 * 
 * @author ZhangJie (zhangjie2@staff.sina.com.cn)
 */
public class Weibo {

    // public static String SERVER = "http://api.t.sina.com.cn/";
    public static String SERVER = "https://api.weibo.com/2/";
    public static String URL_OAUTH_TOKEN = "http://api.t.sina.com.cn/oauth/request_token";
    public static String URL_AUTHORIZE = "http://api.t.sina.com.cn/oauth/authorize";
    public static String URL_ACCESS_TOKEN = "http://api.t.sina.com.cn/oauth/access_token";
    public static String URL_AUTHENTICATION = "http://api.t.sina.com.cn/oauth/authenticate";

    
    public static String URL_OAUTH2_CALLBACK_CODE = "http://news.baidu.com/?code";
    public static String URL_OAUTH2_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";

    // public static String URL_OAUTH2_ACCESS_AUTHORIZE =
    // "http://t.weibo.com:8093/oauth2/authorize";
    public static String URL_OAUTH2_ACCESS_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";

    // 设置appkey及appsecret，如何获取新浪微博appkey和appsecret请另外查询相关信息，此处不作介绍
    private static String APP_KEY = "2274436633";
    private static String APP_SECRET = "9814ebbb61f33b1128509d4a4e538111";
    private static final String CALL_BACK_PAGE_URL = "http://news.baidu.com";

    private static Weibo mWeiboInstance = null;
    private Token mAccessToken = null;
    private RequestToken mRequestToken = null;
    private WeiboAuthListener mListener = null;

    private WeiboAuthListener mAuthDialogListener;
    
    private Context mApplicationContext;
    private SettingManager mSettingManager;
    private Intent mShareWeiboIntent;

    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32973;

    public static final String TOKEN = "access_token";
    public static final String EXPIRES = "expires_in";
    public static final String DEFAULT_REDIRECT_URI = "wbconnect://success";// 暂不支持
    public static final String DEFAULT_CANCEL_URI = "wbconnect://cancel";// 暂不支持

    private String mRedirectUrl;

    private Weibo() {
        Utility.setRequestHeader("Accept-Encoding", "gzip");
        Utility.setTokenObject(this.mRequestToken);
        mRedirectUrl = CALL_BACK_PAGE_URL;
    }

    public synchronized static Weibo getInstance() {
        if (mWeiboInstance == null) {
            mWeiboInstance = new Weibo();
        }
        return mWeiboInstance;
    }

    // 设置accessToken
    public void setAccessToken(AccessToken token) {
        mAccessToken = token;
    }

    public Token getAccessToken() {
        return this.mAccessToken;
    }

    public void setupConsumerConfig(String consumer_key, String consumer_secret) {
        Weibo.APP_KEY = consumer_key;
        Weibo.APP_SECRET = consumer_secret;
    }

    public static String getAppKey() {
        return Weibo.APP_KEY;
    }

    public static String getAppSecret() {
        return Weibo.APP_SECRET;
    }

    public void setRequestToken(RequestToken token) {
        this.mRequestToken = token;
    }

    public static String getSERVER() {
        return SERVER;
    }

    public static void setSERVER(String sERVER) {
        SERVER = sERVER;
    }

    // 设置oauth_verifier
    public void addOauthverifier(String verifier) {
        mRequestToken.setVerifier(verifier);
    }

    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public void setRedirectUrl(String mRedirectUrl) {
        this.mRedirectUrl = mRedirectUrl;
    }

    /**
     * Requst sina weibo open api by get or post
     * 
     * @param url
     *            Openapi request URL.
     * @param params
     *            http get or post parameters . e.g.
     *            gettimeling?max=max_id&min=min_id max and max_id is a pair of
     *            key and value for params, also the min and min_id
     * @param httpMethod
     *            http verb: e.g. "GET", "POST", "DELETE"
     * @throws IOException
     * @throws MalformedURLException
     * @throws WeiboException
     */
    public String request(Context context, String url, WeiboParameters params, String httpMethod,
            Token token) throws WeiboException {
        String rlt = Utility.openUrl(context, url, httpMethod, params, this.mAccessToken);
        return rlt;
    }

    /**/
    public RequestToken getRequestToken(Context context, String key, String secret,
            String callback_url) throws WeiboException {
        Utility.setAuthorization(new RequestTokenHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("oauth_callback", callback_url);
        String rlt;
        rlt = Utility.openUrl(context, Weibo.URL_OAUTH_TOKEN, "POST", postParams, null);
        RequestToken request = new RequestToken(rlt);
        this.mRequestToken = request;
        return request;
    }

    public AccessToken generateAccessToken(Context context, RequestToken requestToken)
            throws WeiboException {
        Utility.setAuthorization(new AccessTokenHeader());
        WeiboParameters authParam = new WeiboParameters();
        authParam.add("oauth_verifier", this.mRequestToken.getVerifier()/* "605835" */);
        authParam.add("source", APP_KEY);
        String rlt = Utility.openUrl(context, Weibo.URL_ACCESS_TOKEN, "POST", authParam,
                this.mRequestToken);
        AccessToken accessToken = new AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    public AccessToken getXauthAccessToken(Context context, String app_key, String app_secret,
            String usrname, String password) throws WeiboException {
        Utility.setAuthorization(new XAuthHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("x_auth_username", usrname);
        postParams.add("x_auth_password", password);
        postParams.add("oauth_consumer_key", APP_KEY);
        String rlt = Utility.openUrl(context, Weibo.URL_ACCESS_TOKEN, "POST", postParams, null);
        AccessToken accessToken = new AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    /**
     * 获取Oauth2.0的accesstoken
     * 
     * https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&
     * client_secret=YOUR_CLIENT_SECRET&grant_type=password&redirect_uri=
     * YOUR_REGISTERED_REDIRECT_URI&username=USER_NAME&pasword=PASSWORD
     * 
     * @param context
     * @param app_key
     * @param app_secret
     * @param usrname
     * @param password
     * @return
     * @throws WeiboException
     */
    public Oauth2AccessToken getOauth2AccessToken(Context context, String app_key,
            String app_secret, String usrname, String password) throws WeiboException {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("username", usrname);
        postParams.add("password", password);
        postParams.add("client_id", app_key);
        postParams.add("client_secret", app_secret);
        postParams.add("grant_type", "password");
        String rlt = Utility.openUrl(context, Weibo.URL_OAUTH2_ACCESS_TOKEN, "POST", postParams,
                null);
        Oauth2AccessToken accessToken = new Oauth2AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    /**
     * Share text content or image to weibo .
     * 
     */
    public boolean share2weibo(Activity activity, String content, String title, String url,
    		ArrayList<String> picPaths) throws Exception {
    	Token token = getToken(activity);
    	mApplicationContext = activity.getApplicationContext();
    	mShareWeiboIntent = new Intent(activity, ShareWeiboActivity.class);
    	mShareWeiboIntent.putExtra(ShareWeiboActivity.CONTENT, content);
    	mShareWeiboIntent.putExtra(ShareWeiboActivity.EXTRA_PIC_URI, picPaths);
    	mShareWeiboIntent.putExtra(ShareWeiboActivity.EXTRA_TITLE, title);
    	mShareWeiboIntent.putExtra(ShareWeiboActivity.EXTRA_URL, url);
    	mShareWeiboIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	if(token == null || StringUtil.isEmpty(token.getToken()) || StringUtil.isEmpty(token.getSecret())){
    		authorize(activity, mAuthListener);
    	}else{
            activity.startActivity(mShareWeiboIntent);	
    	}
        return true;
    }
    
    private WeiboAuthListener mAuthListener = new WeiboAuthListener()
	{
		@Override
		public void onComplete(Bundle values)
		{
			//Toast.makeText(mApplicationContext,"Auth success : access_token = " + values.getString("access_token") + ", expires = " + values.getString("expires_in"), Toast.LENGTH_LONG).show();
			Toast.makeText(mApplicationContext, mApplicationContext.getString(R.string.auth_weibo_success), Toast.LENGTH_LONG).show();
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String uid = values.getString("uid");
			AccessToken accessToken = new AccessToken(token, Weibo.getAppSecret());
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
			mSettingManager.setSinaAccessToken(token);
			mSettingManager.setSinaExpireIn(expires_in);
			mSettingManager.setSinaUid(uid);

			LogUtil.d("weibo", "token = " + token);
			LogUtil.d("weibo", "uid = " + uid);
			LogUtil.d("weibo", "expires_in = " + expires_in);
			mApplicationContext.startActivity(mShareWeiboIntent);	
		}

		@Override
		public void onError(DialogError e) {
			//Toast.makeText(mApplicationContext,"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			//Toast.makeText(mApplicationContext, "Auth cancel",Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			//Toast.makeText(mApplicationContext,"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	};
    
    private Token getToken(Activity activity){
		// 读取设置
    	mSettingManager= (SettingManager) (SettingManager) SettingManagerFactory.getComponent(activity.getApplicationContext());
		mAccessToken = new AccessToken(mSettingManager.getSinaAccessToken(), APP_SECRET);
		return mAccessToken;
    }

    private boolean startSingleSignOn(Activity activity, String applicationId,
            String[] permissions, int activityCode) {
        return false;
    }

    private void startDialogAuth(Activity activity, String[] permissions) {
        WeiboParameters params = new WeiboParameters();
        if (permissions.length > 0) {
            params.add("scope", TextUtils.join(",", permissions));
        }
        CookieSyncManager.createInstance(activity);
        dialog(activity, params, new WeiboAuthListener() {

            public void onComplete(Bundle values) {
            	LogUtil.d("weibo", "onComplete....");
                // ensure any cookies set by the dialog are saved
                CookieSyncManager.getInstance().sync();
                if (null == mAccessToken) {
                    mAccessToken = new Token();
                }
                mAccessToken.setToken(values.getString(TOKEN));
                mAccessToken.setExpiresIn(values.getString(EXPIRES));
                if (isSessionValid()) {
                	LogUtil.d("Weibo-authorize",
                            "Login Success! access_token=" + mAccessToken.getToken() + " expires="
                                    + mAccessToken.getExpiresIn());
                    mAuthDialogListener.onComplete(values);
                } else {
                	LogUtil.d("Weibo-authorize", "Failed to receive access token");
                    mAuthDialogListener.onWeiboException(new WeiboException(
                            "Failed to receive access token."));
                }
            }

            public void onError(DialogError error) {
            	LogUtil.d("Weibo-authorize", "Login failed: " + error);
                mAuthDialogListener.onError(error);
            }

            public void onWeiboException(WeiboException error) {
            	LogUtil.d("Weibo-authorize", "Login failed: " + error);
                mAuthDialogListener.onWeiboException(error);
            }

            public void onCancel() {
            	LogUtil.d("Weibo-authorize", "Login canceled");
                mAuthDialogListener.onCancel();
            }
        });
    }

    /**
     * User-Agent Flow
     * 
     * @param activity
     * 
     * @param listener
     *            授权结果监听器
     */
    public void authorize(Activity activity, final WeiboAuthListener listener) {
        authorize(activity, new String[] {}, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

    private void authorize(Activity activity, String[] permissions,
            final WeiboAuthListener listener) {
        authorize(activity, permissions, DEFAULT_AUTH_ACTIVITY_CODE, listener);
    }

    private void authorize(Activity activity, String[] permissions, int activityCode,
            final WeiboAuthListener listener) {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());

        boolean singleSignOnStarted = false;
        mAuthDialogListener = listener;

        // Prefer single sign-on, where available.
        if (activityCode >= 0) {
            singleSignOnStarted = startSingleSignOn(activity, APP_KEY, permissions, activityCode);
        }
        // Otherwise fall back to traditional dialog.
        if (!singleSignOnStarted) {
            startDialogAuth(activity, permissions);
        }
    }

    private void authorizeCallBack(int requestCode, int resultCode, Intent data) {

    }

    public void dialog(Context context, WeiboParameters parameters,
            final WeiboAuthListener listener) {
    	
    	parameters.add("with_offical_account","1");
    	parameters.add("display", "mobile");
    	//parameters.add("response_type", "code");
    	parameters.add("response_type", "token");
        parameters.add("redirect_uri", mRedirectUrl);
        parameters.add("client_id", APP_KEY);
       
        if (isSessionValid()) {
            parameters.add(TOKEN, mAccessToken.getToken());
        }
        
        String url = URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);
        LogUtil.d("Weibo", "url: "+url);
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Utility.showAlert(context, "Error",
                    "Application requires permission to access the Internet");
        } else {
        	mListener = listener;
        	Intent intent = new Intent(context, AuthWeiboActivity.class);
        	intent.putExtra(AuthWeiboActivity.KEY_URL, url);
        	context.startActivity(intent);
//            new WeiboDialog(this, context, url, listener).show();
        }
    }
    
    public WeiboAuthListener getAuthListener()
    {
    	return mListener;
    }

    public boolean isSessionValid() {
        if (mAccessToken != null) {
            return (!TextUtils.isEmpty(mAccessToken.getToken()) && (mAccessToken.getExpiresIn() == 0 || (System
                    .currentTimeMillis() < mAccessToken.getExpiresIn())));
        }
        return false;
    }
}
