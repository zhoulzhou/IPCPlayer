package com.weibo.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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

import com.baidu.mobstat.StatActivity;
import com.baidu.news.R;
import com.baidu.news.log.ILog;
import com.baidu.news.log.LogFactory;
import com.baidu.news.setting.SettingManager;
import com.baidu.news.setting.SettingManagerFactory;
import com.baidu.news.ui.TypefaceHelper;
import com.baidu.news.ui.widget.LoadingView;
import com.baidu.news.util.LogUtil;
import com.baidu.news.util.Utils;
import com.weibo.net.AsyncWeiboRunner.RequestListener;

/**
 * 新浪微博分享界面
 * 
 * @author yuankai
 * @version 1.0
 * @data 2012-8-22
 */
public class ShareWeiboActivity extends StatActivity implements TextWatcher, OnClickListener, RequestListener
{
	private final static String TAG = "weibo";
	/**
	 * 新闻文本内容
	 */
	public static final String CONTENT = "com.weibo.android.content";

	/**
	 * 图片url，是ArrayList，可以包含多张本地图的路径
	 */
	public static final String EXTRA_PIC_URI = "com.weibo.android.pic.uri";

	/**
	 * 标题
	 */
	public static final String EXTRA_TITLE = "title";

	/**
	 * 新闻url
	 */
	public static final String EXTRA_URL = "url";

	private SettingManager mSettingManager = null;
	private ILog mLog = null;

	private TextView mNameTxt = null;
	private EditText mContentTxt = null;
	private LinearLayout mImageLayout = null;
	private TextView mNumTxt = null;
	private Button mCloseBtn = null;
	private Button mShareBtn = null;
	private LoadingView mLoadingView = null;
	private TextView mTitleTxt = null;
	private TextView mShareToWeiboTxt = null;

	private Weibo mWeibo = null;
	private ArrayList<String> mPicPaths = new ArrayList<String>();
	private ArrayList<String> mShowPaths = new ArrayList<String>();
	private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();
	private HashMap<String, RelativeLayout> mHashMap = new HashMap<String, RelativeLayout>();
	private String mContent = "";
	private String mAccessToken = "";
	private String mTokenSecret = "";
	private String mTitle = "";
	private String mUrl = "";

	private int mSelected = 0;		// 默认选择第一个

	private boolean mAutoSend = false;	// 是否自动发送微博

	private int mPreviewImageWidth = 0;
	private int mPreviewImageHeight = 0;
	private int mPreviewImageGap = 0;
	private int mPreviewImageCardWidth = 0;
	private int mPreviewImageCardHeight = 0;

	private static final int WEIBO_MAX_LENGTH = 140;
	private static final int MAX_IMAGE_SIZE = 5;	// 最多显示5个图片
	private long mForegroundStartTime = 0;
	private ShareWeiboController mShareWeiboController;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

		}
	}; 

	private WeiboAuthListener mAuthListener = new WeiboAuthListener()
	{
		@Override
		public void onComplete(Bundle values)
		{
			//Toast.makeText(getApplicationContext(),"Auth success : access_token = " + values.getString("access_token") + ", expires = " + values.getString("expires_in"), Toast.LENGTH_LONG).show();
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String uid = values.getString("uid");
			AccessToken accessToken = new AccessToken(token, Weibo.getAppSecret());
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
			mSettingManager.setSinaAccessToken(token);
			mSettingManager.setSinaExpireIn(expires_in);
			mSettingManager.setSinaUid(uid);
			mAccessToken = token;

			LogUtil.d("token = " + token);
			LogUtil.d("uid = " + uid);

			if(mAutoSend)
			{
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						shareWeibo();
					}
				});
				mAutoSend = false;
			}
		}

		@Override
		public void onError(DialogError e) {
			//Toast.makeText(getApplicationContext(),"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
			finish();
		}

		@Override
		public void onCancel() {
			//Toast.makeText(getApplicationContext(), "Auth cancel",Toast.LENGTH_LONG).show();
			finish();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			//Toast.makeText(getApplicationContext(),"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_weibo);
		
		getWindow().setBackgroundDrawableResource(R.color.list_bg_color);

		mLog = (ILog)LogFactory.createInterface(getApplicationContext());

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

		mWeibo = Weibo.getInstance();
		mTokenSecret = mWeibo.getAppSecret();

		// 读取设置
		mSettingManager = (SettingManager) (SettingManager) SettingManagerFactory.getComponent(getApplicationContext());
		AccessToken accessToken = new AccessToken(mSettingManager.getSinaAccessToken(), mTokenSecret);
		//    	accessToken.setExpiresIn(mConfigManager.getSinaExpireIn());
		mWeibo.setAccessToken(accessToken);

		setupViews();

		mContentTxt.setText(buildWeiboContent());
		mContentTxt.requestFocus();
		mContentTxt.setSelection(0);

		Token token = mWeibo.getAccessToken();
		if(token == null || Utils.isVoid(token.getToken()) || Utils.isVoid(token.getSecret()))
		{
			Weibo.getInstance().authorize(this, mAuthListener);
		}
		else
		{
			mAccessToken = token.getToken();
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
				if(!Utils.isVoid(localPath))
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
			mShareWeiboController = new ShareWeiboController(getApplicationContext(), mHandler);
		}

		LogUtil.d("access Token = " + mAccessToken);
	}
	@Override
	public void onResume() {
		super.onResume();
		mForegroundStartTime = System.currentTimeMillis();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mShareWeiboController != null) {
			mShareWeiboController.setAppForegroundTime(System.currentTimeMillis() - mForegroundStartTime);			
		}
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
		mLoadingView.setDefaultImageViewVisable(View.GONE);
		mLoadingView.setLoadingText(getString(R.string.weibo_sharing));
		mLoadingView.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

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
		if(Utils.isVoid(mContent))
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

	private String upload(Weibo weibo, String source, String file, String status, String lon,
			String lat) throws WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("pic", file);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
		weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);

		return rlt;
	}

	private String update(Weibo weibo, String source, String status, String lon, String lat)
			throws MalformedURLException, IOException, WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/update.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
		weiboRunner.request(this, url, bundle, Utility.HTTPMETHOD_POST, this);
		return rlt;
	}

	private void shareWeibo()
	{
		Weibo weibo = Weibo.getInstance();
		mLoadingView.startLoading();
		try {
			String content = mContentTxt.getText().toString();
			if (mShowPaths.size() > 0 && mSelected > -1 && mSelected < mPicPaths.size() &&
					!TextUtils.isEmpty(mShowPaths.get(mSelected))) 
			{
				upload(weibo, Weibo.getAppKey(), mShowPaths.get(mSelected), content, "", "");
			} 
			else 
			{
				// Just update a text weibo!
				update(weibo, Weibo.getAppKey(), content, "", "");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			mLoadingView.stopLoading();
		} catch (IOException e) {
			e.printStackTrace();
			mLoadingView.stopLoading();
		} catch (WeiboException e) {
			e.printStackTrace();
			mLoadingView.stopLoading();
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
			//			case R.id.preview_image:
				//			{
			//				if(v.getTag() == null)break;
			//				final String localPath = v.getTag().toString();
			//				for(int i = 0;i < mShowPaths.size(); ++i)
			//				{
			//					final String path = mShowPaths.get(i);
			//					RelativeLayout viewGroup = mHashMap.get(localPath);
			//					if(path.equals(localPath))
			//					{
			//						if(mSelected == i)
			//						{
			//							mSelected = -1;
			//							viewGroup.findViewById(R.id.pic).setVisibility(View.GONE);
			//						}
			//						else
			//						{
			//							mSelected = i;
			//							viewGroup.findViewById(R.id.pic).setVisibility(View.VISIBLE);
			//						}
			//					}
			//					else
			//					{
			//						viewGroup.findViewById(R.id.pic).setVisibility(View.GONE);
			//					}
			//				}
			//				
			//			}
			//				break;
		}
	}

	@Override
	public void onComplete(String response)
	{
		LogUtil.d("response = " + response);
		runOnUiThread(new Runnable() 
		{
			@Override
			public void run() {
				mLoadingView.stopLoading();
				Toast.makeText(ShareWeiboActivity.this, R.string.share_weibo_success, Toast.LENGTH_LONG).show();

				// log
			}
		});

		this.finish();
	}

	@Override
	public void onIOException(IOException e)
	{
		runOnUiThread(new Runnable() 
		{
			@Override
			public void run()
			{
				mLoadingView.stopLoading();
			}
		});
	}

	@Override
	public void onError(final WeiboException e)
	{
		runOnUiThread(new Runnable() 
		{
			@Override
			public void run() {
				mLoadingView.stopLoading();
				int code = e.getStatusCode();
				LogUtil.d(TAG,"error, code = " + code);
				LogUtil.d(TAG,"error, msg = " + e.getMessage());
				switch(code)
				{
				case 21301:
				case 21311:
				case 21312:
				case 21313:
				case 21314:
				case 21315:
				case 21316:
				case 21317:
				case 21318:
				case 21319:
				case 21321:
				{
					// 需要重新授权
					Toast.makeText(ShareWeiboActivity.this, ShareWeiboActivity.this.getString(R.string.need_reauth),
							Toast.LENGTH_LONG).show(); 
					mAutoSend = true;
					Weibo.getInstance().authorize(ShareWeiboActivity.this, mAuthListener);
				}
				break;

				case 20016:
					Toast.makeText(ShareWeiboActivity.this, ShareWeiboActivity.this.getString(R.string.share_weibo_too_frequently),
							Toast.LENGTH_LONG).show();
					break;
				
				case 20019:
					Toast.makeText(ShareWeiboActivity.this, ShareWeiboActivity.this.getString(R.string.share_weibo_repeat_content),
							Toast.LENGTH_LONG).show();
					break;
				
				default:
					Toast.makeText(ShareWeiboActivity.this, ShareWeiboActivity.this.getString(R.string.share_weibo_fail),
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
	}

}
