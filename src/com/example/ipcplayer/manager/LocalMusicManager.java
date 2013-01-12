package com.example.ipcplayer.manager;

import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.provider.MusicDBHelper;
import com.example.ipcplayer.provider.MusicDBManager;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.database.Cursor;

public class LocalMusicManager{
	private Context mContext;
	private static String TAG = LocalMusicManager.class.getSimpleName();
	private MusicDBManager mMusicDBManager ;
	
	public LocalMusicManager(Context context){
		LogUtil.d(TAG + " init object ");
		mContext = context;
		mMusicDBManager = new MusicDBManager(mContext);
	}
	
	public Cursor getAllSongCursor(){
		LogUtil.d(TAG + " getAllSongCurso ");
		String table = MusicDBHelper.TABLE_MUSICINFO;
		String where = MusicDB.MusicInfoColumns._ID + "> 0 ";
		String[] columns = new String[] {
				MusicDB.MusicInfoColumns.MUSICNAME,
				MusicDB.MusicInfoColumns.ARTIST
		};
		Cursor c = null ;
		try {
			c = mMusicDBManager.query(table, columns, where, null, null, null,
					MusicDB.MusicInfoColumns._ID);
		} catch (Exception e) {
			LogUtil.d(TAG + " getAllSongCursor query error !");
			e.printStackTrace();
		}
		return c;
	}
}