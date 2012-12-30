package com.example.ipcplayer.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class MusicDB {
	public static final String AUTHORITY = "com.muisc";
	public static final Uri CONTENT_URI = Uri.parse("content://com.music");
	private static final String CONTENT_AUTHORITY = "content://" + AUTHORITY;
	
	private MusicDB(){
		
	}
	
	public static final class MusicInfoColumns implements BaseColumns{
		public static Uri getContentUri(){
			return Uri.parse(CONTENT_AUTHORITY + "/musicinfo");
		}
		
		public static final String MUSICNAME = "_display_name";
		public static final String ALBUMN = "albumn";
		public static final String ARTIST = "artist";
		public static final String IMAGE = "image";
		public static final int SIZE = 0;
	}
	
	public static final class DownloadInfoColumns implements BaseColumns{
		public static Uri getContentUri(){
			return Uri.parse(CONTENT_AUTHORITY + "/downloadinfo");
		}
		
		public static final String MUSICNAME = "_display_name";
		public static final String URL = "url";
		//后面需要时再加
	}

}
