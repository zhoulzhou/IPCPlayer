package com.example.ipcplayer.activity;

import com.example.ipcplayer.R;
import com.example.ipcplayer.merge.CIVAdapter;
import com.example.ipcplayer.merge.CTVAdapter;
import com.example.ipcplayer.merge.MergeAdapter;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MergeActivity extends Activity{
	private ListView mList;
	private MergeAdapter mMergeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merge_list);
		
		mList = (ListView) findViewById(R.id.list);
		mMergeAdapter = new MergeAdapter();
		
		CTVAdapter adapter1 = new CTVAdapter(this);
		CIVAdapter adapter2 = new CIVAdapter(this);
		
		TextView view1 = new TextView(this);
		view1.setText("textview");
		TextView view2 = new TextView(this);
		view2.setText("imageview");
		mMergeAdapter.addView(view1);
		mMergeAdapter.addAdapter(adapter1);
		mMergeAdapter.addView(view2);
		mMergeAdapter.addAdapter(adapter2);
		
		mList.setAdapter(mMergeAdapter);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}