package com.example.ipcplayer.lyric;

public class LyricSentence{
	String startTime;
	String endTime;
	String sentence;
	Long time;
	
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	@Override
	public String toString() {
		return "StartTime= " + getStartTime() + "  time= " + getTime() + "  Sentence= " + getSentence();
	}
	
	
	
}