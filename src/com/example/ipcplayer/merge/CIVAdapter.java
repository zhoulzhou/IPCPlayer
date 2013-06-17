package com.example.ipcplayer.merge;

import com.example.ipcplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CIVAdapter extends BaseAdapter{
	LayoutInflater mInflater;

	public CIVAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.merge_item2, null);
	    ImageView iv = (ImageView) convertView.findViewById(R.id.im);
		return convertView;
	}
	
}