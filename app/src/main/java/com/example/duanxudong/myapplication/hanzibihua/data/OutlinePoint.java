package com.example.duanxudong.myapplication.hanzibihua.data;

import java.util.List;

/**
 * 每个轮廓点的信息 
 * */
public class OutlinePoint {
	 public List<Point> outLinePointList;
	 private String word;
	 private int mWordHeight;
	 private int mWordWidth;
	public List<Point> getOutLinePointList() {
		return outLinePointList;
	}
	public void setOutLinePointList(List<Point> outLinePointList) {
		this.outLinePointList = outLinePointList;
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
	 
}
