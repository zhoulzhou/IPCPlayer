package com.example.ipcplayer.provider;

import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MusicDBHelper extends SQLiteOpenHelper{
	private final static String DB_NAME = "MusicDB.db";
	private final static int DB_VERSION = 1;
	public final static String TABLE_MUSICINFO = "musicInfo";
	public final static String TABLE_DOWNLOADINFO = "downloadInfo";
	private static String TAG = "MusicDBHelper";
	private SQLiteDatabase mDatabase = null ;
	private static MusicDBHelper mInstance ;

	public MusicDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		LogUtil.d(TAG + "MusicDBHelper initialized");
		openDatabase();
	}
	
	public static synchronized MusicDBHelper getInstance(Context context){
		if(mInstance == null ){
			LogUtil.d(TAG + " getInstance ");
			mInstance = new MusicDBHelper(context);
		}
		return mInstance ;
	}

	private void openDatabase(){
		try{
			LogUtil.d(TAG + " openDatabase ");
			mDatabase = getWritableDatabase();
		}catch(Exception e){
			LogUtil.d(TAG + " openDatebase error ");
			mDatabase = null;
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createTable(db);
		LogUtil.d(TAG + " table's successfully created !");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		dropTable(db);
		LogUtil.d(TAG + "drop table ok! ");
		createTable(db);
		LogUtil.d(TAG + "create table ok !");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
//		db.isOpen();
	}
	
	public void closeDatabase(){
		if(mDatabase != null && mDatabase.isOpen())
		{
			try{
				LogUtil.d(TAG + " closeDatabase ");
				mDatabase.close();
			}catch(Exception e){
				LogUtil.d(TAG + " close Database error ");
				e.printStackTrace();
			}
		}
	}
	
	public void createTable(SQLiteDatabase db){
		try{
			String sql1 = "CREATE TABLE IF NOT EXISTS " + TABLE_MUSICINFO +
					" ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
					" _count INTEGER," +
					" musicname TEXT," + 
					" artistname TEXT," +
					" albumnname TEXT," +
					" size INTEGER" +
					
					" )" ;
			LogUtil.d(TAG + "create sql1 "+ sql1);
			if(db == null){
				LogUtil.d(TAG + "tmd db is null");
			}
			db.execSQL(sql1);
			
			String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_DOWNLOADINFO +
					" ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					" url TEXT," +
					" musicname TEXT," +
					" artistname TEXT," +
					" last_modify INTEGER," + 
					" total_size INTEGER," +
					" download_size INTEGER" +
					
					" )";
			LogUtil.d(TAG + "create sql2 "+ sql2);
			
			db.execSQL( sql2);
			
		}catch(Exception e ){
			LogUtil.e(TAG +" CREATE TABLE ERROR !");
			e.printStackTrace();
		}
	}
	
	public void dropTable(SQLiteDatabase db){
		try{
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSICINFO);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADINFO);
			
		}catch(Exception e){
			e.printStackTrace();
			Log.e(TAG,"drop table error");
		}
		
	}

}
