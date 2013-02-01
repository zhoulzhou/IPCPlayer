package com.example.ipcplayer.download;

public class DownloadInfo{
	private long mDownloadSize;
	private long mTotalSize ;
	private int mErrorCode ;
	private int mDownloadId ;
	private String mUrl;
	private String mFileName;
	private String mFilePath;
	private String mDownloadState;
	
	public String getmDownloadState() {
		return mDownloadState;
	}
	public void setmDownloadState(String mDownloadState) {
		this.mDownloadState = mDownloadState;
	}
	public long getmDownloadSize() {
		return mDownloadSize;
	}
	public void setmDownloadSize(long mDownloadSize) {
		this.mDownloadSize = mDownloadSize;
	}
	public long getmTotalSize() {
		return mTotalSize;
	}
	public void setmTotalSize(long mTotalSize) {
		this.mTotalSize = mTotalSize;
	}
	public int getmErrorCode() {
		return mErrorCode;
	}
	public void setmErrorCode(int mErrorCode) {
		this.mErrorCode = mErrorCode;
	}
	public int getmDownloadId() {
		return mDownloadId;
	}
	public void setmDownloadId(int mDownloadId) {
		this.mDownloadId = mDownloadId;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public String getmFileName() {
		return mFileName;
	}
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}
	public String getmFilePath() {
		return mFilePath;
	}
	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}
	
	
}