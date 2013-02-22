package com.example.ipcplayer.xml;

public class Song{
	private Integer songId;
	private String songName;
	private String artist;
	private String ablumn;
	private String lrclink;
	
	public Integer getSongId() {
		return songId;
	}
	public void setSongId(Integer songId) {
		this.songId = songId;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAblumn() {
		return ablumn;
	}
	public void setAblumn(String ablumn) {
		this.ablumn = ablumn;
	}
	public String getLrclink() {
		return lrclink;
	}
	public void setLrclink(String lrclink) {
		this.lrclink = lrclink;
	}
	
	
}