package com.tencent.weibo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcplayer.R;
import com.example.ipcplayer.setting.SettingManager;
import com.example.ipcplayer.setting.SettingManagerFactory;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

/**
 * 腾讯微博分享界面
 * 
 * @version 1.0
 * @data 2012-8-22
 */
public class ShareTencentActivity extends Activity implements TextWatcher, OnClickListener
{
	private final static String TAG = "ShareTencentActivity";
	/**
	 * 新闻文本内容
	 */
	public static final String CONTENT = "content";
	
	/**
	 * 图片url，是ArrayList，可以包含多张本地图的路径
	 */
    public static final String EXTRA_PIC_URI = "pic.uri";
    
    /**
     * 标题
     */
    public static final String EXTRA_TITLE = "title";
    
    /**
     * 新闻url
     */
    public static final String EXTRA_URL = "url";
    
    /*
     * 申请APP KEY的具体介绍，可参见 
     * http://wiki.open.t.qq.com/index.php/应用接入指引
     * http://wiki.open.t.qq.com/index.php/腾讯微博移动应用接入规范#.E6.8E.A5.E5.85.A5.E6.B5.81.E7.A8.8B
     */
    //!!!请根据您的实际情况修改!!!      认证成功后浏览器会被重定向到这个url中  必须与注册时填写的一致
    private String redirectUri="http://app.news.baidu.com";                   
    //!!!请根据您的实际情况修改!!!      换为您为自己的应用申请到的APP KEY
    private String clientId = "801250859"; 
    //!!!请根据您的实际情况修改!!!      换为您为自己的应用申请到的APP SECRET
    private String clientSecret="1831bf8cb20b3fadd173340170a50e80";
    
    private final static int REQUEST_AUTH = 2;
    
    /**
     * 微博发送成功
     */
    private final static int MSG_SHARE_SUCCESS = -1;
    /**
     * 微博分享失败
     */
    private final static int MSG_SHARE_FAIL = -2;
    
    private SettingManager mSettingManager = null;
    
    private TextView mNameTxt = null;
    private EditText mContentTxt = null;
    private LinearLayout mImageLayout = null;
    private TextView mNumTxt = null;
    private Button mCloseBtn = null;
    private Button mShareBtn = null;
    private LoadingView mLoadingView = null;
    private TextView mTitleTxt = null;
    private TextView mShareToWeiboTxt = null;
    
    private AsyncTask mAddTask = null;
    private AsyncTask mAddPicTask = null;
    
    private OAuthV2 oAuthV2;
    
    private ArrayList<String> mPicPaths = new ArrayList<String>();
    private ArrayList<String> mShowPaths = new ArrayList<String>();
    private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();
    private HashMap<String, RelativeLayout> mHashMap = new HashMap<String, RelativeLayout>();
    private String mContent = "";
    private String mTitle = "";
    private String mUrl = "";
    
    private int mSelected = 0;		// 默认选择第一个
    
    private boolean mAutoSend = false;
    
    private int mPreviewImageWidth = 0;
    private int mPreviewImageHeight = 0;
    private int mPreviewImageGap = 0;
    private int mPreviewImageCardWidth = 0;
    private int mPreviewImageCardHeight = 0;
    
    private static final int WEIBO_MAX_LENGTH = 140;
    private static final int MAX_IMAGE_SIZE = 5;	// 最多显示5个图片
    private long mForegroundStartTime = 0;
	private ShareTencentController mShareTencentController;
	private boolean mIsBackFromAuthed = false;
	private Handler mHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			
		}
	};
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.share_tencent);
    	
    	getWindow().setBackgroundDrawableResource(R.color.list_bg_color);
    	
    	mPreviewImageWidth = getResources().getDimensionPixelSize(R.dimen.share_image_preview_width);
    	mPreviewImageHeight = getResources().getDimensionPixelSize(R.dimen.share_image_preview_height);
    	mPreviewImageGap = getResources().getDimensionPixelSize(R.dimen.share_image_preview_gap);
    	
    	mPreviewImageCardWidth = getResources().getDimensionPixelSize(R.dimen.share_image_preview_card_width);
    	mPreviewImageCardHeight = getResources().getDimensionPixelSize(R.dimen.share_image_preview_card_height);
    	
    	Intent in = this.getIntent();
    	mPicPaths = in.getStringArrayListExtra(EXTRA_PIC_URI);
        mContent = in.getStringExtra(CONTENT);
        mUrl = in.getStringExtra(EXTRA_URL);
        mTitle = in.getStringExtra(EXTRA_TITLE);
    	
    	// 读取设置
        mSettingManager = (SettingManager) (SettingManager) SettingManagerFactory.getComponent(getApplicationContext());
    	String token = mSettingManager.getTencentAccessToken();
    	
    	oAuthV2=new OAuthV2(redirectUri);
    	oAuthV2.setClientId(clientId);
    	oAuthV2.setClientSecret(clientSecret);
    	
    	//关闭OAuthV2Client中的默认开启的QHttpClient。
        OAuthV2Client.getQHttpClient().shutdownConnection();
    	
    	setupViews();
    	
    	mContentTxt.setText(buildWeiboContent());
		mContentTxt.requestFocus();
		mContentTxt.setSelection(0);
    	
    	if(!StringUtil.isEmpty(token))
    	{
    		OAuthV2Client.parseAccessTokenAndOpenId(token, oAuthV2);
    		
    		if(oAuthV2.getStatus() != 0)
    		{
    			goAuth();
    		}
    	}
    	else
    	{
    		goAuth();
    	}
    	
    	if(mPicPaths.size() == 0)
    	{
    		mImageLayout.setVisibility(View.INVISIBLE);
    	}
    	else
    	{
    		int index = 0;
    		mImageLayout.setVisibility(View.VISIBLE);
    		int size = Math.min(MAX_IMAGE_SIZE, mPicPaths.size());
            for(int i = 0;i < size; ++i)
            {
            	final String localPath = mPicPaths.get(i);
            	LogUtil.d("localPath = " + localPath);
            	if(!StringUtil.isEmpty(localPath))
            	{
	            	Bitmap bitmap = createBitmap(localPath);
	            	if(bitmap != null)
	            	{
	            		mBitmaps.add(bitmap);
	            		final View view = buildViewByPath(localPath, bitmap, index == mSelected);
	            		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(mPreviewImageCardWidth, mPreviewImageCardHeight);
	            		llp.rightMargin = mPreviewImageGap;
	            		view.setLayoutParams(llp);
	            		mImageLayout.addView(view);
	            		mShowPaths.add(localPath);
	            		
	            		index ++;
	            	}
            	}
            }
    	}
    		mShareTencentController = new ShareTencentController(getApplicationContext(), mHandler);
    }
    
	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG,"onResume....mIsBackFromAuthed:  "+mIsBackFromAuthed);
		
		if(mIsBackFromAuthed){
			mContentTxt.setSelection(0);
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(mContentTxt, 0); //显示软键盘
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软键盘		
			mIsBackFromAuthed = false;
		}
		mForegroundStartTime = System.currentTimeMillis();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mShareTencentController != null) {
			mShareTencentController.setAppForegroundTime(System.currentTimeMillis() - mForegroundStartTime);			
		}
	}    
    
    private void goAuth()
    {
    	Intent intent = new Intent(this, AuthTencentActivity.class);//创建Intent，使用WebView让用户授权
        intent.putExtra("oauth", oAuthV2);
        startActivityForResult(intent, REQUEST_AUTH); 
    }
    
    private View buildViewByPath(String localPath, Bitmap bitmap, boolean selected)
    {
    	RelativeLayout viewGroup = (RelativeLayout)getLayoutInflater().inflate(R.layout.share_preview_image, null);
    	viewGroup.setTag(localPath);
    	viewGroup.setClickable(true);
    	viewGroup.setOnClickListener(this);
    	ImageView imageView = (ImageView)viewGroup.findViewById(R.id.image);
    	imageView.setImageBitmap(bitmap);
    	ImageView picView = (ImageView)viewGroup.findViewById(R.id.pic);
    	picView.setVisibility(selected ? View.VISIBLE : View.GONE);
    	mHashMap.put(localPath, viewGroup);
    	return viewGroup;
    }
    
    private Bitmap createBitmap(String localPath)
    {
    	Options options = new Options();
    	options.outWidth = mPreviewImageWidth;
    	options.outHeight = mPreviewImageHeight;
    	return BitmapFactory.decodeFile(localPath, options);
    }
    
    private void setupViews()
    {
    	mTitleTxt = (TextView)findViewById(R.id.title);
//    	mTitleTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());
    	
    	mShareToWeiboTxt = (TextView)findViewById(R.id.share_to_weibo);
//    	mShareToWeiboTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());
    	
    	mCloseBtn = (Button)findViewById(R.id.close);
    	mCloseBtn.setOnClickListener(this);
    	
    	mShareBtn = (Button)findViewById(R.id.share);
    	mShareBtn.setOnClickListener(this);
    	
    	mNameTxt = (TextView)findViewById(R.id.name);
//    	mNameTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());
    	mNumTxt = (TextView)findViewById(R.id.num);
//    	mNumTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());
    	mImageLayout = (LinearLayout)findViewById(R.id.gallery);
    	
    	mContentTxt = (EditText)findViewById(R.id.content);
//    	mContentTxt.setTypeface(TypefaceHelper.getInstance(getApplicationContext()).getTypeface());
    	mContentTxt.addTextChangedListener(this);
    	
		mLoadingView = (LoadingView)findViewById(R.id.loading);
		mLoadingView.setDefaultImageViewVisible(View.GONE);
		mLoadingView.setLoadingText(getString(R.string.weibo_sharing));
		mLoadingView.setVisibility(View.GONE);
    }
    
    private class AddTask extends AsyncTask<Void, Void, String>
    {
    	private String mContent = null;
    	public AddTask(String content)
    	{
    		mContent = content;
    	}
    	@Override
    	protected void onPreExecute()
    	{
    		super.onPreExecute();
    		mLoadingView.startLoading();
    	}
    	
    	@Override
    	protected String doInBackground(Void... params)
    	{
    		String content = mContent;
    		TAPI tAPI= new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
    		String response = null;
            try {
                response=tAPI.add(oAuthV2, "json", content, "127.0.0.1");
                handleAddReponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tAPI.shutdownConnection();
    		return response;
    	}
    	
    	@Override
    	protected void onPostExecute(String result)
    	{
    		super.onPostExecute(result);
    		mLoadingView.startLoading();
    		if(StringUtil.isEmpty(result))
    		{
    			Toast.makeText(getApplicationContext(),
    					R.string.network_exception, Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		try
			{
				handleAddReponse(result);
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mAddTask = null;
    	}
    }
    
    private void add(String content)
    {
    	if(mAddTask != null)
    	{
    		mAddTask.cancel(false);
    	}
    	mAddTask = new AddTask(content);
    	Void[] o = null;
    	mAddTask.execute(o);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	if(keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		if(mLoadingView.getVisibility() == View.VISIBLE)
    		{
    			if(mAddTask != null)
    			{
    				mAddTask.cancel(false);
    			}
    			if(mAddPicTask != null)
    			{
    				mAddPicTask.cancel(false);
    			}
    			mLoadingView.stopLoading();
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
    	return false;
    }
    
    private void handleAddReponse(String response) throws JSONException
    {
    	JSONObject json = new JSONObject(response);
    	String errorCode = json.optString("errcode");
    	final int code = Integer.parseInt(errorCode);
    	final String msg = json.optString("msg");
    	final String ret = json.optString("ret");
    	final int retCode = Integer.parseInt(ret);
    	if(retCode == 0)
    	{
    		// 微博发送成功
    		runOnUiThread(new Runnable() 
    		{
                @Override
                public void run() {
                    Toast.makeText(ShareTencentActivity.this, R.string.share_weibo_success, Toast.LENGTH_LONG).show();
                }
            });

            this.finish();
    	}
    	else if(retCode == 3)
    	{
    		// 鉴权失败,需要重新授权
    		runOnUiThread(new Runnable() 
    		{
                @Override
                public void run() {
					Toast.makeText(ShareTencentActivity.this, ShareTencentActivity.this.getString(R.string.need_reauth),
							Toast.LENGTH_LONG).show();
					mAutoSend = true;
					goAuth();
                }
            });
    	}
    	else
    	{
    		// 服务端错误
    		runOnUiThread(new Runnable() 
    		{
                @Override
                public void run() 
                {
        			Toast.makeText(ShareTencentActivity.this, ShareTencentActivity.this.getString(R.string.share_weibo_fail),
        					Toast.LENGTH_LONG).show();
                }
            });
    	}
    }
    
    private class AddPicTask extends AsyncTask<Void, Void, String>
    {
    	private String mContent = null;
    	private String mFilePath = null;
    	
    	public AddPicTask(String content, String filePath)
    	{
    		mContent = content;
    		mFilePath = filePath;
    	}
    	
    	@Override
    	protected void onPreExecute()
    	{
    		super.onPreExecute();
    		mLoadingView.startLoading();
    	}
    	
    	@Override
    	protected String doInBackground(Void... params)
    	{
    		String content = mContent;
    		String filePath = mFilePath;
    		String response = null;
    		TAPI tAPI= new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
            try {
                response = tAPI.addPic(oAuthV2,  "json",  content, "127.0.0.1", filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tAPI.shutdownConnection();
    		return response;
    	}
    	
    	@Override
    	protected void onPostExecute(String result)
    	{
    		super.onPostExecute(result);
    		mLoadingView.stopLoading();
    		if(StringUtil.isEmpty(result))
    		{
    			// 网络异常
    			Toast.makeText(getApplicationContext(),
    					R.string.network_exception, Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		try
			{
				handleAddReponse(result);
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mAddPicTask = null; 
    	}
    }
    
    private void addPic(String content, String filePath)
    {
    	if(mAddPicTask != null)
    	{
    		mAddPicTask.cancel(false);
    	}
    	mAddPicTask = new AddPicTask(content, filePath);
    	Void[] o = null;
    	mAddPicTask.execute(o);
    }
    
    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
    	
    	// 回收图片
    	if(mBitmaps != null && mBitmaps.size() > 0)
    	{
    		final int size = mBitmaps.size();
    		for(int i = 0;i < size; i++)
    		{
    			Bitmap bitmap = mBitmaps.get(i);
    			if(!bitmap.isRecycled())
    			{
    				bitmap.recycle();
    			}
    		}
    	}
    	mBitmaps.clear();
    }
    
    private String buildWeiboContent()
    {
    	if(StringUtil.isEmpty(mContent))
    	{
    		return " 【" + mTitle +  "】" + " " + mUrl + " " + getString(R.string.weibo_share_source);
    	}
    	else
    	{
    		String suffix = " 【" + mTitle +  "】" + " " + mUrl + " " + getString(R.string.weibo_share_source);
    		int len = suffix.length();
    		int contentLen = mContent.length();
    		int diff = len + contentLen - WEIBO_MAX_LENGTH;
    		if(diff > 0)
    		{
    			return mContent.substring(0, contentLen - diff) + suffix;
    		}
    		else
    		{
    			return mContent + suffix;
    		}
    	}
    }

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		String mText = mContentTxt.getText().toString();
		int len = mText.length();
		int color = Color.BLACK;
		if (len > WEIBO_MAX_LENGTH) {
			color = Color.RED;
		} 
		len = WEIBO_MAX_LENGTH - len;
		mNumTxt.setTextColor(color);
		mNumTxt.setText(String.valueOf(len));
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		// TODO Auto-generated method stub
		
	}
	
	private void shareWeibo()
	{
		mLoadingView.startLoading();
        String content = mContentTxt.getText().toString();
        if (mShowPaths.size() > 0 && mSelected > -1 && mSelected < mPicPaths.size() &&
        		!TextUtils.isEmpty(mShowPaths.get(mSelected))) 
        {
            addPic(content, mShowPaths.get(mSelected));
        } 
        else 
        {
            // Just update a text weibo!
            add(content);
        }	   
        mAutoSend = false;
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.close:
				finish();
				break;
				
			case R.id.share:
			{
				if(mContentTxt.getText().length() > WEIBO_MAX_LENGTH){
					Toast toast = Toast.makeText(getApplicationContext(),
							getString(R.string.content_more_than_140), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					shareWeibo();
				}
			}
				break;
				
			default:
				if(v instanceof RelativeLayout)
				{
					if(v.getTag() == null)break;
					final String localPath = v.getTag().toString();
					for(int i = 0;i < mShowPaths.size(); ++i)
					{
						final String path = mShowPaths.get(i);
						RelativeLayout viewGroup = mHashMap.get(path);
						if(path.equals(localPath))
						{
							if(mSelected == i)
							{
								mSelected = -1;
								viewGroup.findViewById(R.id.pic).setVisibility(View.GONE);
							}
							else
							{
								mSelected = i;
								viewGroup.findViewById(R.id.pic).setVisibility(View.VISIBLE);
							}
						}
						else
						{
							viewGroup.findViewById(R.id.pic).setVisibility(View.GONE);
						}
					}
				}
				break;
		}
	}
	
	/*
     * 通过读取OAuthV2AuthorizeWebView返回的Intent，获取用户授权信息
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   
    {
    	mIsBackFromAuthed = true; 
    	LogUtil.d(TAG, "onActivityResult.....mIsBackFromAuthed: "+mIsBackFromAuthed);
        if (requestCode==REQUEST_AUTH) 
        {
            if (resultCode == AuthTencentActivity.RESULT_CODE)    
            {
                oAuthV2=(OAuthV2) data.getExtras().getSerializable("oauth");
                if(oAuthV2.getStatus()!=0)
                {
                	//Toast.makeText(getApplicationContext(),"Auth error : " + oAuthV2.getMsg(), Toast.LENGTH_LONG).show();
                	finish();
                }
                else
                {
                	Toast.makeText(getApplicationContext(), getString(R.string.auth_weibo_success), Toast.LENGTH_LONG).show();
                	// 绑定成功
                	//Toast.makeText(getApplicationContext(),"Auth success : access_token = " + oAuthV2.getAccessToken() +", expires = " + oAuthV2.getExpiresIn(), Toast.LENGTH_LONG).show();
                	if(mAutoSend)
                	{
                		shareWeibo();
                		mAutoSend = false;
                	}
                }
            }
            else
            {
            	//Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
            	finish();
            }
        }
    }
    
}
