package com.example.ipcplayer.lyric;

import java.util.ArrayList;

import com.example.ipcplayer.utils.LogUtil;

public class LyricDecode{
	private final static String TAG = LyricDecode.class.getSimpleName();
	
	static ArrayList<LyricSentence	> mLyricSentenceList = new ArrayList<LyricSentence>();
	
	
	public static ArrayList<LyricSentence> convetToLyricSentences(String lyricRow){
		LyricSentence sentence = new LyricSentence();
		sentence = convertToSentence(lyricRow);
		mLyricSentenceList.add(sentence);
		return mLyricSentenceList;
	}
	
	private static LyricSentence convertToSentence(String lyricRow){
		if(lyricRow == null){
			return null;
		}
		LyricSentence sentence = new LyricSentence();
		int left = lyricRow.indexOf("[");
		int right = lyricRow.indexOf("]");
		
		String time = lyricRow.substring(left+1, right);
		sentence.setStartTime(convertToTime(time));
		sentence.setTime(convertToLong(time));
		
		String content = lyricRow.substring(right+1);
		sentence.setSentence(content);
		LogUtil.d(TAG + "sentence= " + sentence.toString());
		return sentence;
	}
	
	private static String convertToTime(String time){
		String standardTime = "";
		int index = time.indexOf(".");
		standardTime = time.substring(0,index);
		return standardTime ;
	}
	
	private static long convertToLong(String time){
		int index = time.indexOf(":");
		String min = time.substring(0,index);
		String sec = time.substring(index+1);
		
		long min1 = Long.parseLong(min);
		long sec1 = Long.parseLong(sec);
		long time1 = min1*60 + sec1;
		
		return time1;
	}
}