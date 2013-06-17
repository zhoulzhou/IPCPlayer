package com.example.ipcplayer.merge;

import com.example.ipcplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CTVAdapter extends BaseAdapter{
	LayoutInflater mInflater;

	public CTVAdapter(Context context){
		mInflater = LayoutInflater.from(context);
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
		convertView = mInflater.inflate(R.layout.merge_item1, null);
	    TextView iv = (TextView) convertView.findViewById(R.id.tx1);
	    iv.setText("merge example");
		return convertView;
	}



	@Override
	public int getCount() {
		return 3;
	}
	
}