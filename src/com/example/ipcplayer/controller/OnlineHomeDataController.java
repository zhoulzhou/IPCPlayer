package com.example.ipcplayer.controller;

import java.util.ArrayList;

import com.example.ipcplayer.R;
import com.example.ipcplayer.application.IPCApplication;
import com.example.ipcplayer.homeview.HomeDescriptionItem;

import android.content.res.Resources;


public class OnlineHomeDataController{
	
	
	
	public static ArrayList<HomeDescriptionItem> getOnlineMusicHomepage() {

		ArrayList<HomeDescriptionItem> arrayList = new ArrayList<HomeDescriptionItem>();
		HomeDescriptionItem HomeDescriptionItem = null;
		Resources res = IPCApplication.getInstance().getApplicationContext().getResources();

		HomeDescriptionItem = new HomeDescriptionItem();
		HomeDescriptionItem.mId = "1";
		HomeDescriptionItem.mTitle = "热门榜单"; // 新歌榜
		HomeDescriptionItem.mDes = "最In歌曲照单全收";
		HomeDescriptionItem.mIcon = res
				.getDrawable(R.drawable.online_icon_list_normal);
		arrayList.add(HomeDescriptionItem);

		HomeDescriptionItem = new HomeDescriptionItem();
		HomeDescriptionItem.mId = "7";
		HomeDescriptionItem.mTitle = "电台"; // 热歌榜
		HomeDescriptionItem.mDes = "打开就有好音乐";
		HomeDescriptionItem.mIcon = res
				.getDrawable(R.drawable.online_icon_radio_normal);
		arrayList.add(HomeDescriptionItem);

		HomeDescriptionItem = new HomeDescriptionItem();
		HomeDescriptionItem.mId = "4";
		HomeDescriptionItem.mTitle = "精选专题"; // 自选辑
		HomeDescriptionItem.mDes = "专业音乐编辑专题推荐";
		HomeDescriptionItem.mIcon = res
				.getDrawable(R.drawable.online_icon_subject_normal);
		arrayList.add(HomeDescriptionItem);

		HomeDescriptionItem = new HomeDescriptionItem();
		HomeDescriptionItem.mId = "6";
		HomeDescriptionItem.mTitle = "歌手"; // 热门歌手
		HomeDescriptionItem.mDes = "人气歌手一网打尽";
		HomeDescriptionItem.mIcon = res
				.getDrawable(R.drawable.online_icon_singer_normal);
		arrayList.add(HomeDescriptionItem);

		HomeDescriptionItem = new HomeDescriptionItem();
		HomeDescriptionItem.mId = "5";
		HomeDescriptionItem.mTitle = "新碟上架"; // 专辑
		HomeDescriptionItem.mDes = "正版大碟海量收藏";
		HomeDescriptionItem.mIcon = res
				.getDrawable(R.drawable.online_icon_special_normal);
		arrayList.add(HomeDescriptionItem);

		HomeDescriptionItem = new HomeDescriptionItem();
		HomeDescriptionItem.mId = "3";
		HomeDescriptionItem.mTitle = "新歌速递"; // 新歌速递
		HomeDescriptionItem.mDes = "最新歌曲抢鲜试听";
		HomeDescriptionItem.mIcon = res
				.getDrawable(R.drawable.online_icon_newsong_normal);
		arrayList.add(HomeDescriptionItem);

		return arrayList;
	}
}