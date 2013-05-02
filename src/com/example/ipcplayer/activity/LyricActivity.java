package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.download.DownloadInfo;
import com.example.ipcplayer.download.DownloadListener;
import com.example.ipcplayer.download.DownloadRunnable;
import com.example.ipcplayer.lyric.LyricGetter;
import com.example.ipcplayer.thread.ThreadF;
import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LyricActivity extends Activity implements DownloadListener{
	private static final String TAG = LyricActivity.class.getSimpleName();
	
	private TextView lyricTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyric_view);
		lyricTV = (TextView) findViewById(R.id.lyric);
		downloadLyricFile();
		LyricGetter lyricGetter = new LyricGetter();
		String lyricFileName = "try.lrc";
		lyricGetter.get(lyricFileName);
		
	}
	
	private void getSongList(){
		//热歌榜 url
		String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=3.3.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=50";
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
	}

	@Override
	public void updateProgress(DownloadInfo downloadInfo) {
		LogUtil.d(TAG + " donwload update ");
		LogUtil.d(TAG + " donwload size: " + downloadInfo.getmDownloadSize());
	}

	@Override
	public void errorDownload(DownloadInfo downloadInfo) {
		LogUtil.d(TAG + " donwload error ");
	}

	@Override
	public void finishDownload(DownloadInfo downloadInfo) {
		LogUtil.d(TAG + " donwload finish ");
	}

	@Override
	public void preDownload(DownloadInfo downloadInfo) {
		
	}
	
}