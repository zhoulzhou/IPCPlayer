package com.example.ipcplayer.utils;

import android.text.TextUtils;

public class StringUtil{
	
	public static boolean isEmpty(String str){
		if(str == null){
			return true;
		}
		str = str.trim();
		return TextUtils.isEmpty(str);
	}
	
	public static String generateMusicFileName(String songName , String artistName){
		return songName + "-" + artistName + ".mp3";
	}
	
	public static String generateLyricFileName(String songName, String artistName){
		return songName + "-" + artistName + ".lyric";
	}
	
	public static String gernerateTempFileName(String songName, String artistName){
		return songName + "-" + artistName + ".tmp";
	}
}