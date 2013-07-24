package com.example.ipcplayer.onlineframent;

import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.activity.MainActivity;
import com.example.ipcplayer.application.IPCApplication;
import com.example.ipcplayer.object.MusicFile;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecialObjectAdapter extends ArrayAdapter<MusicFile>{
	private LayoutInflater mInflater;
	private int mResource;
	private int mWidth;
	private int mHeight;
	private Activity mActivity;
	private MusicFile mmf;
	private List<MusicFile> mDatas = new ArrayList<MusicFile>();

	public SpecialObjectAdapter(Context context, int resource,
			int textViewResourceId, List<MusicFile> objects) {
		super(context, resource, textViewResourceId, objects);
		mInflater = LayoutInflater.from(context);
		mResource = resource;
		mActivity = (Activity) context;
		mDatas = objects;
	}
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}



	@Override
	public MusicFile getItem(int position) {
		return mmf = mDatas.get(position);
	}
	
	/**
	 * 计算图片展示尺寸
	 */
	private void computeImageWidth() {
		WindowManager wm = (WindowManager) IPCApplication.getInstance().getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int tenDip = (int) IPCApplication.getInstance().getApplicationContext().getResources()
				.getDimension(R.dimen.ten_dip);
		mWidth = screenWidth - 2 * tenDip;
		mHeight = (int) ((screenWidth) / 2.5);
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
		
		holder.mItem_bg.setOnClickListener(holder);
		
		return convertView;
	}
	
	private void redirect(){
		Fragment fragment = new SpecialObjectDetailFragment(mmf);
		((MainActivity) mActivity).onShow(fragment,true,null);
	}
	
	public class ViewHolder implements OnClickListener{
		public ImageView mItem_im;
		public ImageView mItem_bg;
		public TextView mTitle;
		public TextView mTime;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch(id){
			case R.id.item_bg:
				redirect();
				break;
			case R.id.play:
				//通过线程获取网络上的信息，获取到信息后通过接口传递数据到展示界面或播放歌曲
				break;
			default:
				break;
			}
		}
	}
	
}