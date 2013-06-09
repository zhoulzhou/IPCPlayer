package com.example.ipcplayer.cursorloader;

import com.example.ipcplayer.R;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;

public class ListViewActivity extends FragmentActivity {

	private ListView riverListView;

	private SimpleCursorAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cursor_main);

		initLoader();
		setRiverListViewAdapter();
	}

	private void initLoader() {
		getSupportLoaderManager().initLoader(0, null,
				new LoaderCallbacks<Cursor>() {

					@Override
					public Loader<Cursor> onCreateLoader(int id, Bundle args) {
						Log.d("list", "on create loader");
						 CursorLoader cursorLoader=new CursorLoader(ListViewActivity.this,
								RiverContentProvider.CONTENT_URI, new String[] {
										RiverContentProvider._ID,
										RiverContentProvider.NAME,
										RiverContentProvider.INTRODUCTION },
								null, null, null);
						 //cursorLoader.setUpdateThrottle(1000);
						 return cursorLoader;
					}

					@Override
					public void onLoadFinished(Loader<Cursor> loader,
							Cursor cursor) {
						Log.d("list", "on loader finished");
						adapter.swapCursor(cursor);
					}

					@Override
					public void onLoaderReset(Loader<Cursor> loader) {
						Log.d("list", "on loader reset");
						adapter.swapCursor(null);
					}
				});
	}

	private void setRiverListViewAdapter() {
		riverListView = (ListView) this.findViewById(R.id.riverList);

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(RiverContentProvider.CONTENT_URI, null,
				null, null, null);
		adapter = new SimpleCursorAdapter(this, R.layout.cursor_item, cursor,
				new String[] { RiverContentProvider.NAME,
						RiverContentProvider.INTRODUCTION }, new int[] {
						R.id.riverName, R.id.riverIntroduction }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		riverListView.setAdapter(adapter);
	}
	
	
	/**
	 * 示例中有一个后台线程每隔3秒更新数据库的长江记录，将记录改为“长江”或“Long River”。ListView无需监控数据库变化，基于Loader，会自动更新。
	 * 实际上这里面是观察者模式，无非是系统自带了，只需调用即可，无需自己构造观察者。
                  这个示例也是完整的sqlite+content provider+cursor adapter+listview+loader组合示例。

                    编写前的准备类似编写兼容android1.6的fragment，需要导入jar包。
                     另外，2.3以前的Activity类没有提供一些Loader的帮助方法，需要让自己的Activity实现类继承FragmentActivity：
	 * 
	 * 
	 * 
	 * 主要是增加了initLoader方法。这里主要是实现了LoaderCallbacks接口。其中：
       onCreateLoader，在创建activity时跟着onCreate会调用一次
       onLoadFinished，每次改变和Loader相关的数据库记录后会调用一次
       onLoaderReset，在关闭Activity时调用，释放资源
                 然后，在Content provider中，要调用类似观察者模式中通知的方法，即，在update方法中通知观察者记录改变，
                  在query方法中注册观察者，这样通知来了可接收并处理。
      update方法：
       @Override     
      public int update(Uri uri, ContentValues values, String selection,      
        String[] selectionArgs) {      
         int returnValue = database.update("rivers", values, selection,      
            selectionArgs);      
         getContext().getContentResolver().notifyChange(uri, null);      
        return returnValue;      
     }
     query方法：
     public Cursor query(Uri uri, String[] projection, String selection,     
        String[] selectionArgs, String sortOrder) {      
        Cursor cursor = database.query("rivers", projection, selection,      
         selectionArgs, null, null, sortOrder);      
         cursor.setNotificationUri(getContext().getContentResolver(), uri);      
         return cursor;      
     }
            这里要注意，这个观察者模式是从sdk level 1就有的，也就是说，cursor可以接收通知来感知content provider数据变化，
            但是不能做到异步刷新界面。这次Loader机制通过官方支持实现了这个功能。
             另外，通过本示例也可观察到，当关闭Activity后，Content provider继续工作，它的后台线程还在不停的更新记录。
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * **/
}