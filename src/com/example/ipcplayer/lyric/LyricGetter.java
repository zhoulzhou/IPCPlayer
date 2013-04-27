package com.example.ipcplayer.lyric;

import java.io.File;

import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

public class LyricGetter{
	private static final String TAG = LyricGetter.class.getSimpleName();
	private File mLyricFile;
	
	public void get(String lyricFileName){
		String path = FileUtil.getIPCLyricDir().getAbsolutePath() + File.separator + lyricFileName;
		String lyric = null;
		try {
			lyric = FileUtil.readSDFile(path);
		} catch (Exception e) {
			LogUtil.d(TAG + " readLyricFile Exception : ");
			e.printStackTrace();
		}
		if(lyric == null){
			return ;
		}
		
	}
}