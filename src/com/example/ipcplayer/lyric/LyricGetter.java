package com.example.ipcplayer.lyric;

import java.io.File;
import java.util.ArrayList;

import com.example.ipcplayer.utils.FileUtil;
import com.example.ipcplayer.utils.LogUtil;

public class LyricGetter{
	private static final String TAG = LyricGetter.class.getSimpleName();
	private File mLyricFile;
	private static ArrayList<LyricSentence> mLyricSentenceList = new ArrayList<LyricSentence>();
	
	public static ArrayList<LyricSentence> get(String lyricFileName){
		String path = FileUtil.getIPCLyricDir().getAbsolutePath() + File.separator + lyricFileName;
		ArrayList<String> lyricRows = new ArrayList<String>();
		try {
			lyricRows = FileUtil.readSDFile1(path);
			LogUtil.d(TAG + " lyricRow size  : " + lyricRows.size());
			for(String lyricRow : lyricRows){
				if (lyricRow != null) {
					LogUtil.d(TAG + " lyricRow : " + lyricRow);
					mLyricSentenceList = LyricDecode.convetToLyricSentences(lyricRow);
				}
			}
		
		} catch (Exception e) {
			LogUtil.d(TAG + " readLyricFile Exception : ");
			e.printStackTrace();
		}
		if(lyricRows == null || lyricRows.size() == 0){
			LogUtil.d(TAG + " lyric is null");
			return null ;
		}
		
		return mLyricSentenceList;
		
	}
	
}