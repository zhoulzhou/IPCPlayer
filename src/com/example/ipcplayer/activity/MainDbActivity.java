package com.example.ipcplayer.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.ipcplayer.R;
import com.example.ipcplayer.manager.LocalMusicManager;
import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.provider.MusicDBManager;
import com.example.ipcplayer.utils.LogUtil;

public class MainDbActivity extends Activity{
	private MusicDBManager mDBManager ;
	private static String TAG = MainDbActivity.class.getSimpleName();
	private LocalMusicManager mLocalMusicManager;
	private Cursor mAllSongCursor;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);
        
        mDBManager = new MusicDBManager(this);
        mLocalMusicManager = new LocalMusicManager(this);
        mDBManager.insertLocalData();
        
		try {
			mAllSongCursor = mLocalMusicManager.getAllSongCursor();
			// you's better not use this method
			// and return arraylist<>
			// managedCursor(mAllSongCursor);
			if (mAllSongCursor != null && mAllSongCursor.getCount() != 0) {
				mAllSongCursor.moveToFirst();
				while (mAllSongCursor.moveToNext()) {
					LogUtil.d(TAG + " mAllSongCursor = "
							+ mAllSongCursor.toString());
				}
			}
		} finally {
			if (mAllSongCursor != null) {
				mAllSongCursor.close();
				mAllSongCursor = null;
			}
		}
	}
	
	private void query(){
		String[] PROJECTION = new String[] {
				MusicDB.MusicInfoColumns.MUSICNAME,
				MusicDB.MusicInfoColumns.ARTIST
		};
		LogUtil.d(TAG + " PROJECTION = " + PROJECTION.toString());
		LogUtil.d(TAG + " Uri  = " + MusicDB.MusicInfoColumns.getContentUri().toString());
		
		Cursor c = managedQuery(MusicDB.MusicInfoColumns.getContentUri(), PROJECTION, null, null, null);
		try {
			if (c != null) {
				c.moveToFirst();
				while (c.moveToNext()) {
					String musicName = c.getString(1);
					String artist = c.getString(2);
					LogUtil.d(TAG + " musicname = " + musicName + " artist = "
							+ artist);
				}
			} else {
				LogUtil.e(TAG + "  OH no ! c is null ");
			}
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
	}
}
