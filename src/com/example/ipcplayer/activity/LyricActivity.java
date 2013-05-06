package com.example.ipcplayer.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.ipcplayer.R;
import com.example.ipcplayer.download.DownloadInfo;
import com.example.ipcplayer.download.DownloadListener;
import com.example.ipcplayer.download.DownloadRunnable;
import com.example.ipcplayer.lyric.LyricGetter;
import com.example.ipcplayer.lyric.LyricSentence;
import com.example.ipcplayer.lyric.LyricView;
import com.example.ipcplayer.thread.ThreadF;
import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class LyricActivity extends Activity implements DownloadListener{
	private static final String TAG = LyricActivity.class.getSimpleName();
	private final static int DLFINISH = 3;
	private final static int DLERROR = 0;
	private final static int DLING = 1;
	
	private final static int LYRIC_REFRESH = 4;
	private final static int LYRIC_READY = 2;
	
	
	private TextView lyricTV;
	
	private int mIndex = 0;

	private ArrayList<LyricSentence> mSentences = new ArrayList<LyricSentence>();
	
	private  Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch(what){
			case DLING:
				LogUtil.d(TAG + " handleMessage  DLING");
				break;
			case DLERROR:
				LogUtil.d(TAG + " handleMessage  DLERROR");
				break;
			case DLFINISH:
				LogUtil.d(TAG + " handleMessage  DLFINISH");
				getSentences();
				mHandler.sendEmptyMessage(LYRIC_REFRESH);
				break;
			case LYRIC_REFRESH:
				LogUtil.d(TAG + " handleMessage  REFRESH");
//				if(lyricTV != null){
//					LogUtil.d(TAG + " updateLyric");
//					lyricTV.updateLyric(LYRIC_READY);
//					mHandler.sendMessageDelayed(msg, 10000);
//				}
				setLyircView();
				mHandler.sendEmptyMessageDelayed(LYRIC_REFRESH, 10000);
				break;
			}
			
		}
			
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyric_view);
		lyricTV = (TextView) findViewById(R.id.lyric);
		downloadLyricFile();
//		getSentences();
//		setLyircView();
	}
	
	
	private void get(){
		String lyricFileName = "try.lrc";
		System.out.println(" zzz lyricFileName= " + lyricFileName);
		String path = FileUtil.getIPCLyricDir().getAbsolutePath() + File.separator + lyricFileName;
		String lyricRows = null;
		try {
			lyricRows = FileUtil.readSDFile(path);
			System.out.println(" zzz lyricRows= " + lyricRows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("static-access")
	private void getSentences(){
		LogUtil.d(TAG + "  getSentences");
		LyricGetter lyricGetter = new LyricGetter();
		String lyricFileName = "try.lrc";
	    mSentences = lyricGetter.get(lyricFileName);
	    LogUtil.d(TAG + " get size=  " + mSentences.size());
	}
	
	private void setLyircView(){
		LogUtil.d(TAG + "  setLyircView");
		LyricSentence sentence = new LyricSentence();
		int size = mSentences.size();
		LogUtil.d(TAG + " size=  " + size);
		
		LogUtil.d(TAG + " mIndex=  " + mIndex);
		sentence = mSentences.get(mIndex);
		if(mIndex < size){
			mIndex += 1;
		}
		
//		Iterator iterator = mSentences.iterator();
//		if(iterator.hasNext()){
//			sentence = (LyricSentence) iterator.next();
//			mSentences.iterator();
//		}
//		LogUtil.d(TAG + " sentence=  " + sentence.toString());
		String string = sentence.getSentence();
//		LogUtil.d(TAG + " string=  " + string);
		String time = sentence.getStartTime();
//		LogUtil.d(TAG + " time=  " + time);
		LogUtil.d(TAG + " text=  " + time + "  " + string);
		lyricTV.setText(time + "  " + string);
	}
	
	private void getSongList(){
		//热歌榜 url
		String url= "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=3.3.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=50";
	}
	
	private void downloadLyricFile(){
		String url = "http://music.baidu.com/data2/lrc/13890839/13890839.lrc";
		String path = FileUtil.getIPCLyricDir().getAbsolutePath();
		LogUtil.d(TAG + " path= " +path);
		DownloadRunnable mDownloadRunnable = new DownloadRunnable(this,url,path);
		mDownloadRunnable.setDownloadListener(this);
		ThreadF.getInstance().submit(mDownloadRunnable);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mHandler != null){
			if(mHandler.hasMessages(LYRIC_REFRESH)){
				mHandler.removeMessages(LYRIC_REFRESH);
			}
		}
	}

	@Override
	public void updateProgress(DownloadInfo downloadInfo) {
		LogUtil.d(TAG + " donwload update ");
		LogUtil.d(TAG + " donwload size: " + downloadInfo.getmDownloadSize());
		mHandler.sendEmptyMessage(DLING);
	}

	@Override
	public void errorDownload(DownloadInfo downloadInfo) {
		LogUtil.d(TAG + " donwload error ");
		mHandler.sendEmptyMessage(DLERROR);
	}

	@Override
	public void finishDownload(DownloadInfo downloadInfo) {
		LogUtil.d(TAG + " donwload finish ");
		mHandler.sendEmptyMessage(DLFINISH);
	}

	@Override
	public void preDownload(DownloadInfo downloadInfo) {
		
	}
	
}