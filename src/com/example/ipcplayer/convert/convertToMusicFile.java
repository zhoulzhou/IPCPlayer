package com.example.ipcplayer.convert;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.utils.MusicFile;
import com.example.ipcplayer.utils.StringUtil;

public class convertToMusicFile{
	private Context mContext;
	
	public convertToMusicFile(Context context){
		mContext = context;
	}
	
	public MusicFile idToMusicFile(long id){
		if(id < 1){
			return null;
		}
		
		String[] cols = new String[]{
				MusicDB.MusicInfoColumns._ID,
				MusicDB.MusicInfoColumns.DATA,
				MusicDB.MusicInfoColumns.ARTIST,
				MusicDB.MusicInfoColumns.ALBUMN,
				MusicDB.MusicInfoColumns.MUSICNAME
				
		};
		
		Uri uri = ContentUris.withAppendedId(MusicDB.MusicInfoColumns.getContentUri(), id);
		Cursor cursor = mContext.getContentResolver().query(uri, cols, null, null, null);
		try {
			MusicFile musicFile = new MusicFile();
			musicFile.id = id;
			musicFile.path = cursor.getString(1);
			String musicName = cursor.getString(4);
			if (StringUtil.isEmpty(musicName)) {
				musicFile.musicName = "";
			} else {
				musicFile.musicName = musicName;
			}
			
			String artistName = cursor.getString(4);
			if (StringUtil.isEmpty(artistName)) {
				musicFile.artistName = "";
			} else {
				musicFile.artistName = musicName;
			}
			
			String albumnName = cursor.getString(4);
			if (StringUtil.isEmpty(albumnName)) {
				musicFile.albumnName = "";
			} else {
				musicFile.albumnName = musicName;
			}
			
			return musicFile;
		} finally {
			if(cursor != null){
				cursor.close();
				cursor = null;
			}

		}
	}
}