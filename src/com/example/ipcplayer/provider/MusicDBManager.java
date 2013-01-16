package com.example.ipcplayer.provider;

import com.example.ipcplayer.application.IPCApplication;
import com.example.ipcplayer.utils.LogUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicDBManager {
	private static String TAG = "MusicDBManager "; 
	private  MusicDBHelper mDBHelper ;
	private  SQLiteDatabase mDB = null;
	private Context mContext;
	
	public MusicDBManager(Context context){
		mContext = context;
		mDBHelper = MusicDBHelper.getInstance(context);
		mDB = mDBHelper.getWritableDatabase();
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		LogUtil.d(TAG + " query()");
		Cursor c = null ;
		if(isDBOpen()){
			c = mDB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		}else {
			LogUtil.d(TAG + " db is close ");
		}
		return c ;
	}
	
	public long insert(String table, String nullColumnHack, ContentValues values ){
		LogUtil.d(TAG + " insert()");
		long rowId = -1;
		mDB.beginTransaction();
		try{
			rowId = mDB.insert(table, nullColumnHack, values);
			mDB.setTransactionSuccessful();
		}catch(Exception e){
			LogUtil.d(TAG + " insert db error ");
			e.printStackTrace();
		}
		finally{
			mDB.endTransaction();
		}
		return rowId;
	}
	
	public int delete(String table, String whereClause, String[] whereArgs){
		LogUtil.d(TAG + " delete()");
		return mDB.delete(table, whereClause, whereArgs);
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
		LogUtil.d(TAG + " update()");
		return mDB.update(table, values, whereClause, whereArgs);
		
	}
	
	public boolean isDBOpen(){
		return mDB.isOpen();
	}
	
	public  void insertLocalData(){
		
		Cursor cursor = getCursorFromMediaStore();
		if(cursor == null && cursor.getCount() == 0){
			LogUtil.d(TAG+"cursor is null! try find reason ");
			return ;
		}
		int _IDIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
		int DISPLAY_NAMEIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
		int ARTISTIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
		int ALBUMIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
		int SIEZIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
		int DURATIONIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
		
		cursor.moveToFirst();
		
		
		if (mDB != null && mDB.isOpen()) {
			mDB.beginTransaction();
			do {
				long _Id = cursor.getLong(_IDIndex);
				String displayName = cursor.getString(DISPLAY_NAMEIndex);
				String artist = cursor.getString(ARTISTIndex);
				String albumn = cursor.getString(ALBUMIndex);
				long size = cursor.getLong(SIEZIndex);
				long duration = cursor.getLong(DURATIONIndex);
				
				StringBuilder sb = new StringBuilder();
				sb.append("insert into ");
				sb.append(MusicDBHelper.TABLE_MUSICINFO);
				sb.append("(_id,size,musicname,artistname,albumnname)");
				sb.append("values('");
				sb.append(_Id);
				sb.append("','");
				sb.append(size);
				sb.append("','");
				sb.append(displayName);
				sb.append("','");
				sb.append(artist);
				sb.append("','");
				sb.append(albumn);
				sb.append("')");
				try{
					LogUtil.d(TAG + "sb.toString = " + sb.toString());
					mDB.execSQL(sb.toString());
				}catch(Exception e){
//					LogUtil.e(TAG+"insert to db error! ");
//					e.printStackTrace();
				}
			} while (cursor.moveToNext());
			
			if(cursor != null || cursor.getCount() != 0){
				LogUtil.d(TAG+"close cursor it's great");
				cursor.close();
				cursor = null ;
			}
			
			mDB.setTransactionSuccessful();
			LogUtil.d(TAG+"wow insert db successful !");
			mDB.endTransaction();
		}
	}

	public Cursor getCursorFromMediaStore(){
		String[] projection = {
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DURATION
		};
		
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String sortOrder = MediaStore.Audio.Media._ID;
		Cursor cursor = null;
	    try{		
		    ContentResolver resolver = mContext.getContentResolver();
		    cursor = resolver.query(uri, projection, null, null, sortOrder);
		
		}catch(Exception e){
			LogUtil.e(TAG+"query mediastore error! " );
			e.printStackTrace();
			return cursor = null;
		}
		return cursor;
	}
	
}
