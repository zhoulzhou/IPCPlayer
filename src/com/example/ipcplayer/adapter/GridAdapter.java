package com.example.ipcplayer.adapter;

import com.example.ipcplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{
    private Context mContext ;
	private Integer[] images = {
			R.drawable.ic_launcher
	};
	
	private String[] texts = {
			"È«²¿¸èÇú"
	};
	
	public GridAdapter(Context context){
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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