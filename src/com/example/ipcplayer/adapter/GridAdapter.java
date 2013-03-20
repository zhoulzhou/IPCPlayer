package com.example.ipcplayer.adapter;

import com.example.ipcplayer.R;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{
	private static String TAG = GridAdapter.class.getSimpleName();
    private Context mContext ;
	private Integer[] images = {
			R.drawable.ic_launcher,
			R.drawable.ic_launcher,
			R.drawable.ic_launcher,
			R.drawable.ic_launcher
	};
	
	private String[] texts = {
			"本地音乐",
			"云收藏",
			"最近播放",
			"预留区域"
	};
	
	public GridAdapter(Context context){
		LogUtil.d(TAG + " initialize this object ");
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getCount ");
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getItem ");
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getItemId ");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LogUtil.d(TAG + " getView ");
		ViewHolder viewHolder ;
		
		if(convertView == null){
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.local_music_grid_item, null);
			convertView.setTag(viewHolder);
			convertView.setPadding(15, 15, 15, 15);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.mImage = (ImageView) convertView.findViewById(R.id.localimage);
		viewHolder.mImage.setBackgroundResource(images[position]);
		viewHolder.mText1 = (TextView) convertView.findViewById(R.id.localname);
		viewHolder.mText1.setText(texts[position]);
		
		return convertView;
	}
	
}