package com.example.ipcplayer.lyric;

import java.io.File;
import java.util.ArrayList;

import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

public class LyricGetter{
	private static final String TAG = LyricGetter.class.getSimpleName();
	private File mLyricFile;
	
	public void get(String lyricFileName){
		String path = FileUtil.getIPCLyricDir().getAbsolutePath() + File.separator + lyricFileName;
		ArrayList<String> lyricRows = new ArrayList<String>();
		try {
			lyricRows = FileUtil.readSDFile1(path);
			for(String lyricRow : lyricRows){
				if (lyricRow != null) {
					LogUtil.d(TAG + " lyricRow : " + lyricRow);
					LyricDecode.convetToLyricSentences(lyricRow);
				}
			}
		
		} catch (Exception e) {
			LogUtil.d(TAG + " readLyricFile Exception : ");
			e.printStackTrace();
		}
		if(lyricRows == null || lyricRows.size() == 0){
			LogUtil.d(TAG + " lyric is null");
			return ;
		}
		
	}
}