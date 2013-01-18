package com.example.ipcplayer.manager;

import java.util.ArrayList;

import com.example.ipcplayer.localfragment.ItemData;
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
		LogUtil.d(TAG + " table = " + table);
		String where = MusicDB.MusicInfoColumns._ID + "> 0 ";
		LogUtil.d(TAG + " where = " + where);
		String[] columns = new String[] {
			    MusicDB.MusicInfoColumns._ID,
				MusicDB.MusicInfoColumns.MUSICNAME,
				MusicDB.MusicInfoColumns.ARTIST
		};
		LogUtil.d(TAG + " columns = " + columns.toString());
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
	
	public ArrayList<ItemData> getLocalMusicItems(){
		LogUtil.d(TAG + " getLocalMusicItems ");
		ArrayList<ItemData> arrayList = new ArrayList<ItemData>();
		ItemData itemData = null ;
		
		itemData = new ItemData();
		itemData.mType = ItemData.DATATYPE_ALLSONG_LIST;
		arrayList.add(itemData);
		return arrayList;
	}
}