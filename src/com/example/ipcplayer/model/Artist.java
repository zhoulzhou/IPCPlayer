package com.example.ipcplayer.model;

import com.example.ipcplayer.utils.LogUtil;

public class Artist extends BaseObject{
	private static final String TAG = Artist.class.getSimpleName();
	private String name ;
	private String detail;
	private int songCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getSongCount() {
		return songCount;
	}

	public void setSongCount(int songCount) {
		this.songCount = songCount;
	}

	@Override
	public long calculateMemSize() {
		return name.length() + detail.length();
	}

	@Override
	protected void parse(String json) {
		LogUtil.d(TAG + " parse artist ");
	}

	@Override
	public String toString() {
		return "Arst: name= "+ getName() + " detail =  " + getDetail() + " songCount = "+ getSongCount() ;
	}

}