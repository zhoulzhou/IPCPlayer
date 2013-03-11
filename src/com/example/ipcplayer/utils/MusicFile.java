package com.example.ipcplayer.utils;

public class MusicFile {
	public String musicName;
	public String artistName;
	public String albumnName;
	public String albumnImage;
	public String path;
	public long id;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getAlbumnName() {
		return albumnName;
	}
	public void setAlbumnName(String albumnName) {
		this.albumnName = albumnName;
	}
	public String getAlbumnImage() {
		return albumnImage;
	}
	public void setAlbumnImage(String albumnImage) {
		this.albumnImage = albumnImage;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	

}
