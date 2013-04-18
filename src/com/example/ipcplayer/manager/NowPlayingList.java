package com.example.ipcplayer.manager;

import java.util.ArrayList;

import android.database.Cursor;

import com.example.ipcplayer.object.MusicFile;
import com.example.ipcplayer.provider.MusicDB;

public class NowPlayingList{
	private static final String TAG = NowPlayingList.class.getSimpleName();
	private static  NowPlayingList instance = null;
	private ArrayList<MusicFile> mPlayList = new ArrayList<MusicFile>();
	
	private NowPlayingList(){
		
	}
	
	public static NowPlayingList getInstance(){
		if(instance == null){
			instance = new NowPlayingList();
		}
		return instance ;
	}
	
	/**
	 * get play list
	 * @param cursor
	 * @return ArrayList<MusicFile>
	 */
	public ArrayList<MusicFile> setPlayList(Cursor cursor){
		mPlayList = null;
		MusicFile musicFile = new MusicFile();
		if(cursor == null){
			return null;
		}
		try{
			int length = cursor.getCount();
			if(length > 0){
				for(int i=0; i<length; i++){
					musicFile.id = cursor.getLong(cursor.getColumnIndexOrThrow(MusicDB.MusicInfoColumns._ID));
					musicFile.artistName = cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.MusicInfoColumns.ARTIST));
					musicFile.musicName = cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.MusicInfoColumns.MUSICNAME));
					mPlayList.add(musicFile);
				}
			}
		}catch(Exception e)	{
			e.printStackTrace();
			musicFile = null ;
		}
		return mPlayList;
	}
	
	public ArrayList<MusicFile> getPlayList(){
		return mPlayList;
	}
}