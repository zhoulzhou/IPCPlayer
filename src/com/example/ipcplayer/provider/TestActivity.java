package com.example.ipcplayer.provider;

import com.example.ipcplayer.utils.LogUtil;

import android.R.string;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class TestActivity extends Activity{
	private Uri mUir;
	private string[] mPorjection = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		insertDB();
//		updateOne();
		update();
//		delete();
		query();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void insertDB(){
		LogUtil.d("insertDB");
		for (int i = 0; i < 5; i++) {
			Uri uri = MusicDB.MusicInfoColumns.getContentUri();
			LogUtil.d("uri= " + uri);
			ContentValues values = new ContentValues();
			values.put(MusicDB.MusicInfoColumns.MUSICNAME, "name_" + i );
			this.getContentResolver().insert(uri, values);
		}
	}
	
	private void query(){
		Uri uri = MusicDB.MusicInfoColumns.getContentUri();
		String[] projection = {
			MusicDB.MusicInfoColumns.MUSICNAME
		};
		
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		String name = "";
		cursor.moveToFirst();
		for(int i=0; i<cursor.getCount();i++){
			cursor.moveToPosition(i);
			name = cursor.getString(cursor.getColumnIndex(MusicDB.MusicInfoColumns.MUSICNAME));
			LogUtil.d("name= " + name);
//			cursor.moveToNext();
		}
	}
	
	private void updateOne(){
		Uri uri = MusicDB.MusicInfoColumns.getContentUri();
		Uri newUri = ContentUris.withAppendedId(uri,2);//后面的数字指数据库中第几行
		ContentValues values = new ContentValues();
		values.put(MusicDB.MusicInfoColumns.MUSICNAME, "20xx");
		String where = MusicDB.MusicInfoColumns.MUSICNAME + " = " +"name_3 xx";
		LogUtil.d("where= " + where);
	    getContentResolver().update(newUri, values, where, null);
	}
	
	private void update() {
		Uri uri = MusicDB.MusicInfoColumns.getContentUri();
//		Uri newUri = ContentUris.withAppendedId(uri,MusicDBProvider.MUSICINFO_ITEM);
		ContentValues values = new ContentValues();
		values.put(MusicDB.MusicInfoColumns.MUSICNAME, "20xx");
		String where = MusicDB.MusicInfoColumns.MUSICNAME + " = " + "name_3";
		LogUtil.d("where= " + where);
		getContentResolver().update(uri, values, where, null);
	}
	
	private void delete(){
		Uri uri = MusicDB.MusicInfoColumns.getContentUri();
		getContentResolver().delete(uri, null, null);
	}
	
}