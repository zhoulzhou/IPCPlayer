package com.example.ipcplayer.onlineframent;

import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.object.MusicFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecialObjectAdapter extends ArrayAdapter<MusicFile>{
	private LayoutInflater mInflater;
	private int mResource;

	public SpecialObjectAdapter(Context context, int resource,
			int textViewResourceId, List<MusicFile> objects) {
		super(context, resource, textViewResourceId, objects);
		mInflater = LayoutInflater.from(context);
		mResource = resource;
	}

	@Override
	public MusicFile getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
      final	ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.mItem_im = (ImageView) convertView.findViewById(R.id.item_im);
			holder.mItem_bg = (ImageView) convertView.findViewById(R.id.item_bg);
			holder.mTitle = (TextView) convertView.findViewById(R.id.title);
			holder.mTime = (TextView) convertView.findViewById(R.id.time);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	public class ViewHolder{
		public ImageView mItem_im;
		public ImageView mItem_bg;
		public TextView mTitle;
		public TextView mTime;
	}
	
}