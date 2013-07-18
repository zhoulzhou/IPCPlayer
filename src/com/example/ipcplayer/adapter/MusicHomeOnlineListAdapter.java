package com.example.ipcplayer.adapter;

import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.homeview.HomeDescriptionItem;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicHomeOnlineListAdapter extends ArrayAdapter<HomeDescriptionItem>{
	private Context mContext;
	private List<HomeDescriptionItem> mDatas;
	private int mViewId;
	private LayoutInflater mInflater;

	public MusicHomeOnlineListAdapter(Context context, int resource,
			int textViewResourceId, List<HomeDescriptionItem> objects) {
		super(context, resource, textViewResourceId, objects);
		this.mContext = context;
		mDatas = objects;
		mViewId = resource;
		mInflater = LayoutInflater.from(context);
	}

	class ViewHolder{
		public ImageView mIconLeft;
		public TextView mLine1Text;
		public TextView mLine2Text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		HomeDescriptionItem item = (HomeDescriptionItem) mDatas
				.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_online_list_item, null);
			holder = new ViewHolder();
			holder.mLine1Text = (TextView) convertView
					.findViewById(R.id.text1);
//			MusicUtils.setFakeBoldText(holder.mLine1Text);
			holder.mLine2Text = (TextView) convertView
					.findViewById(R.id.text2);
			holder.mIconLeft = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(holder == null){
			LogUtil.d("holder is null");
		}
		
		if(item == null){
			LogUtil.d("item is null");
		}

		holder.mIconLeft.setImageDrawable(item.mIcon);
		holder.mLine1Text.setText(item.mTitle);
		holder.mLine2Text.setText(item.mDes);
		

		return convertView;
	}
	
	
	
	
}