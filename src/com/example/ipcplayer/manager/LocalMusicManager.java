package com.example.ipcplayer.manager;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.localfragment.ItemData;
import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.provider.MusicDBHelper;
import com.example.ipcplayer.provider.MusicDBManager;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.MusicFile;

import android.content.Context;
import android.database.Cursor;

public class LocalMusicManager{
	private Context mContext;
	private static String TAG = LocalMusicManager.class.getSimpleName();
	private MusicDBManager mMusicDBManager ;
//	private static LocalMusicManager mInstance;
	
	public LocalMusicManager(Context context){
		LogUtil.d(TAG + " init object ");
		mContext = context;
	}
	
//	public static LocalMusicManager getInstance(Context context){
//		synchronized(mInstance){
//			if(mInstance == null){
//				mInstance = new LocalMusicManager(context);
//			}
//		}
//		return mInstance;
//	}
	
	public String getPlayPath(MusicFile musicFile){
		return musicFile.path;
	}
	
	public String getPlayPath(long id){
		return null;
	}
	
	public long[]  getSongIdList(Cursor cursor){
		long[] list ;
		try {
			int length = cursor.getCount();
			list = new long[length];
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					cursor.moveToPosition(i);
					list[i] = cursor
							.getLong(cursor
									.getColumnIndexOrThrow(MusicDB.MusicInfoColumns._ID));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
            return null;
		}finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	} 
	
	public ArrayList<MusicFile> getPlayList(Cursor cursor){
		ArrayList<MusicFile> musicFiles = new ArrayList<MusicFile>();
		MusicFile musicFile = new MusicFile();
		try{
			int length = cursor.getCount();
			if(length > 0){
				for(int i=0; i<length; i++){
					musicFile.id = cursor.getLong(cursor.getColumnIndexOrThrow(MusicDB.MusicInfoColumns._ID));
					musicFile.artistName = cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.MusicInfoColumns.ARTIST));
					musicFile.musicName = cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.MusicInfoColumns.MUSICNAME));
					musicFiles.add(musicFile);
				}
			}
		}catch(Exception e)	{
			e.printStackTrace();
			musicFile = null ;
		}
		return musicFiles;
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
			c = MusicDBManager.getInstance(mContext).query(table, columns, where, null, null, null,
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
	    LogUtil.d(TAG + " itemData.mType = " + itemData.mType);
		arrayList.add(itemData);
		return arrayList;
	}
}