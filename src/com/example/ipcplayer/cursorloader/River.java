package com.example.ipcplayer.cursorloader;

public class River {
	private int id;

	private String name;

	private String introduction;

	private int length;

	public River() {
	}

	public River(String name, int length, String introduction) {
		this.name = name;
		this.length = length;
		this.introduction = introduction;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}