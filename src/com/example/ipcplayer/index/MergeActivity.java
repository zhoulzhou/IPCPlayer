package com.example.ipcplayer.index;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MergeActivity extends Activity{
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merge_list);
		
		mListView = (ListView) findViewById(R.id.list);
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}