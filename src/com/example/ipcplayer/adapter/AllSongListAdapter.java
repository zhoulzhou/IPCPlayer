package com.example.ipcplayer.adapter;

import com.example.ipcplayer.R;
import com.example.ipcplayer.provider.MusicDB;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AllSongListAdapter extends CursorAdapter{
	private LayoutInflater mInflater;
//	private ViewHolder mHolder ;
	private int mLayoutId;
	private static String TAG = AllSongListAdapter.class.getSimpleName();
	
	public AllSongListAdapter(Context context, Cursor c, int resource) {
		super(context, c, resource);
		// TODO Auto-generated constructor stub
		LogUtil.d(" create this class Object");
		mInflater = LayoutInflater.from(context);
//		mHolder = new ViewHolder();
		mLayoutId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getView ");
		View v = super.getView(position, convertView, parent);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " bindView ");
		final ViewHolder mHolder = (ViewHolder) view.getTag();
		String displayName = cursor.getString(cursor.getColumnIndex(MusicDB.MusicInfoColumns.MUSICNAME));
		String artstName = cursor.getString(cursor.getColumnIndex(MusicDB.MusicInfoColumns.ARTIST));
		if(mHolder == null){
			LogUtil.d(TAG + " mHolder is null ");
		}else {
			LogUtil.d(TAG + " mHolder = " + mHolder);
		}
		if(StringUtil.isEmpty(displayName)){
			LogUtil.d(TAG + " displayName is null ");
		}else {
			LogUtil.d(TAG + " displayName = " + displayName);
		}
		if(mHolder.mText1 == null){
			LogUtil.d(TAG + " mHolder.mText1 is null");
		}else {
			LogUtil.d(TAG + " mHolder.mText1 = " + mHolder.mText1);
		}
		mHolder.mText1.setText(displayName);
		mHolder.mText2.setText(artstName);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " newView ");
		LogUtil.d(TAG + " mLayoutId = " + mLayoutId);
		View v = mInflater.inflate(mLayoutId, null);
		ViewHolder mHolder = new ViewHolder();
		mHolder.mImage = (ImageView) v.findViewById(R.id.albumnimage);
		mHolder.mText1 = (TextView) v.findViewById(R.id.songname);
		LogUtil.d(TAG + " mHolder.mText1 = " + mHolder.mText1);
		mHolder.mText2 = (TextView) v.findViewById(R.id.artistname);
		v.setTag(mHolder);
		return v;
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " runQueryOnBackground");
		String[] cols = new String[] {
				MusicDB.MusicInfoColumns.MUSICNAME,
				MusicDB.MusicInfoColumns.ARTIST
		};
		StringBuilder  where = new StringBuilder();
		
//		return 
		
		return super.runQueryOnBackgroundThread(constraint);
	}
	
}