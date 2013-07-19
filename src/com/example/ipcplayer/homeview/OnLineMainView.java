package com.example.ipcplayer.homeview;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.adapter.MusicHomeOnlineListAdapter;
import com.example.ipcplayer.controller.OnlineHomeDataController;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.widget.CellLayout;
import com.example.ipcplayer.widget.DocIndicator;
import com.example.ipcplayer.widget.OnlineWorkspace;
import com.example.ipcplayer.widget.Workspace;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OnLineMainView extends BaseHomeView implements OnlineWorkspace.IWorkspaceListener{
	private static final int TOTAL_HEAD_IMAGES_SHOW_COUNT = 5;
	private LayoutInflater mInflater;
	private LinearLayout mRecommand;
	private OnlineWorkspace mWorkspace;
	private DocIndicator mDocIndicator;
	private ListView mList;
	private TextView mEmptyTV;
	private ArrayList<HomeDescriptionItem> mDatas;
	private MusicHomeOnlineListAdapter mAdapter;
	private Context mContext ;

	public OnLineMainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public OnLineMainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public OnLineMainView(Context context) {
		super(context);
	}

	@Override
	protected void onCreateView(Context context, AttributeSet attrs) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mRecommand = (LinearLayout) mInflater.inflate(R.layout.main_online, null,false);
		mWorkspace = (OnlineWorkspace) mRecommand.findViewById(R.id.work);
		mWorkspace.setWorkspaceListener(this);
		mDocIndicator = (DocIndicator) mRecommand.findViewById(R.id.indicator);
		
		View v = mInflater.inflate(R.layout.main_online_list, this);
		mList = (ListView) v.findViewById(R.id.online_home_list);
		mEmptyTV = (TextView) v.findViewById(R.id.online_list_empty);
		mList.addHeaderView(mRecommand);
		
		initHomeDescription();
		initWorkspace();
	}
	
	private void initWorkspace() {
		int total = TOTAL_HEAD_IMAGES_SHOW_COUNT;
		mDocIndicator.setTotal(total);
//		mDocIndicator.setVisibility(View.GONE);
		CellLayout cellLayout = null;
		for (int i = 0; i < total; i++) {
			cellLayout = (CellLayout) mInflater.inflate(
					R.layout.recommand_item, null);
			mWorkspace.addView(cellLayout);
		}
	}
	
	
	/**
	 * 初始化在线描述数据
	 */
	private void initHomeDescription() {
		ArrayList<HomeDescriptionItem> datas = OnlineHomeDataController
				.getOnlineMusicHomepage();
		setDecriptionData(datas);
	}
	
	private void setDecriptionData(ArrayList<HomeDescriptionItem> datas) {
		if (mDatas != null) {
			mDatas.clear();
		}
		mDatas = datas;
		mAdapter = new MusicHomeOnlineListAdapter(mContext,
				R.layout.main_online_list_item, 0, mDatas);
		mList.setAdapter(mAdapter);

	}


	@Override
	protected void onRelease() {
		
	}

	@Override
	public void onUpdateTotalNum(int total) {
		// TODO Auto-generated method stub
		mDocIndicator.setTotal(total);
	}

	@Override
	public void onUpdateCurrent(int current) {
		// TODO Auto-generated method stub
		mDocIndicator.setCurrent(current);
	}

	@Override
	public void onWorkspaceClick(int current) {
		// TODO Auto-generated method stub
		LogUtil.d("onclick workspace");
	}
	
}