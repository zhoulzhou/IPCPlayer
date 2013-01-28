package com.example.ipcplayer.download;

public class DownloadConfig{
	public static String sMUSIC_PATH = "downloadPath";
	public static String[] sUrls = {
		"http://music.baidu.com/data/music/file?link=http://zhangmenshiting.baidu.com/data2/music/32669375/149508041359140461192.mp3?xcode=65156a3ab27a65445f13a00e1cf710fe"
	};
	
	public DownloadConfig(String downPath){
		sMUSIC_PATH = downPath ;
	}

	public static String getMUSIC_PATH() {
		return sMUSIC_PATH;
	}

	public static void setMUSIC_PATH(String mUSIC_PATH) {
		sMUSIC_PATH = mUSIC_PATH;
	}
	
}