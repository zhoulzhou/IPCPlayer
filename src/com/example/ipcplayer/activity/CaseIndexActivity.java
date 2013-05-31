package com.example.ipcplayer.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.ipcplayer.R;
import com.example.ipcplayer.caseindex.Content;
import com.example.ipcplayer.caseindex.MyAdapter;
import com.example.ipcplayer.caseindex.SideBar;
import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class CaseIndexActivity extends Activity {
    private static boolean DEBUG = true;
	private ListView mListView;
	private SideBar indexBar;
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private View head;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_example);
		
		mListView = (ListView) this.findViewById(R.id.list);
		indexBar = (SideBar) findViewById(R.id.sideBar);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
		head = LayoutInflater.from(this).inflate(R.layout.head, null);
		mListView.addHeaderView(head);
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		//初始化数据
		List<Content> list = new ArrayList<Content>();
		for (int i = 0; i < 10; i++) {
			Content m;
			if (i < 3)
				m = new Content("A", "选项" + i);
			else if (i < 6)
				m = new Content("F", "选项" + i);
			else
				m = new Content("D", "选项" + i);
			list.add(m);
		}
		//根据a-z进行排序
		Collections.sort(list, new PinYinComparator());
		// 实例化自定义内容适配类		
		MyAdapter adapter = new MyAdapter(this, list);
		// 为listView设置适配
		mListView.setAdapter(adapter);
		//设置SideBar的ListView内容实现点击a-z中任意一个进行定位
	    indexBar.setListView(mListView);		
	}
	
	public class PinYinComparator implements Comparator<Content>{

		@Override  
	    public int compare(Content o1, Content o2) {  
	        // TODO Auto-generated method stub  
	        String py1 =  o1.getPinyin();  
	        String py2 = o2.getPinyin();  
	        // 判断是否为空""  
	        if (isEmpty(py1) && isEmpty(py2))  
	            return 0;  
	        if (isEmpty(py1))  
	            return -1;  
	        if (isEmpty(py2))  
	            return 1;  
	        String str1 = "";  
	        String str2 = "";  
	        try {  
	            str1 = ((o1.getPinyin()).toUpperCase()).substring(0, 1);  
	            str2 = ((o2.getPinyin()).toUpperCase()).substring(0, 1);  
	        } catch (Exception e) {  
	            System.out.println("某个str为\" \" 空");  
	        }  
	        return str1.compareTo(str2);  
	    }  
	  
	    private boolean isEmpty(String str) {  
	        return "".equals(str.trim());  
	    }

		
	}

}