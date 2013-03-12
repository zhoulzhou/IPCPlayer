package com.example.ipcplayer.convert;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.provider.MusicDBHelper;
import com.example.ipcplayer.provider.MusicDBManager;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.MusicFile;
import com.example.ipcplayer.utils.StringUtil;

public class ConvertToMusicFile{
	private static final String TAG = ConvertToMusicFile.class.getSimpleName();
	private Context mContext;
	private static ConvertToMusicFile mInstance;
	
	public ConvertToMusicFile(Context context){
		mContext = context;
	}
	
	public static ConvertToMusicFile getInstance(Context context){
		if(mInstance == null){
			mInstance = new ConvertToMusicFile(context);
		}
		return mInstance;
	}
	
	public MusicFile idToMusicFile(long id){
		LogUtil.d(TAG + " idToMusicFile id: " + id);
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
		LogUtil.d(TAG + " idToMusicFile cols: " + cols.toString());
		
		//there is some problem with ContentProvider and need to test it;
//		Uri uri = ContentUris.withAppendedId(MusicDB.MusicInfoColumns.getContentUri(), id);
//		LogUtil.d(TAG + " idToMusicFile uri: " + uri.toString());
//		Cursor cursor = mContext.getContentResolver().query(uri, cols, null, null, null);
		String where = MusicDB.MusicInfoColumns._ID + " = " + id;
		LogUtil.d(TAG + " idToMusicFile where: " + where);
		Cursor cursor = MusicDBManager.getInstance(mContext).query(MusicDBHelper.TABLE_MUSICINFO, cols, where, null, null, null, null);
		LogUtil.d(TAG + " idToMusicFile cursor: " + cursor.toString());
		MusicFile musicFile = new MusicFile();
		try {
			musicFile.id = id;
			LogUtil.d(TAG + " idToMusicFile id: " + id);
			if (cursor.moveToFirst()) {
				musicFile.path = cursor.getString(cursor
						.getColumnIndexOrThrow(MusicDB.MusicInfoColumns.DATA));
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
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if(cursor != null){
				cursor.close();
				cursor = null;
			}

		}
		return musicFile;
	}
}