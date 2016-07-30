package com.example.duanxudong.myapplication.hanzibihua.data;

import java.util.List;

/**
 * 每个笔画点的信息 
 * */
public class BihuaPoint { 
	
	private List<Stroke> storkeList;
	private String word;
	private int mWordHeight;
	private int mWordWidth;
	private String spell;
 
	public List<Stroke> getStorkeList() {
		return storkeList;
	}
	public void setStorkeList(List<Stroke> storkeList) {
		this.storkeList = storkeList;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getmWordHeight() {
		return mWordHeight;
	}
	public void setmWordHeight(int mWordHeight) {
		this.mWordHeight = mWordHeight;
	}
	public int getmWordWidth() {
		return mWordWidth;
	}
	public void setmWordWidth(int mWordWidth) {
		this.mWordWidth = mWordWidth;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	
}
