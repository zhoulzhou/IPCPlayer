package com.example.ipcplayer.download;

public interface DownloadListener{
	public void updateProgress(DownloadInfo downloadInfo);
	public void errorDownload(DownloadInfo downloadInof);
	public void finishDownload(DownloadInfo downloadInfo);
}