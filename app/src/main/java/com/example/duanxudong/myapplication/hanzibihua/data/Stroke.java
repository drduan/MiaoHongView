package com.example.duanxudong.myapplication.hanzibihua.data;

import java.util.List;

/**
 * 每个笔画的描红点信息
 * 
 * */
public class Stroke {
	// 第几笔
	private int strokeIndex = 0;
	// 该笔画的读音，也可以改成笔画音频的文件名，在需要播报音频的时候才去读取音频buffer
	private byte[] strokeVoiceBuffer = null; 
	// 笔画点信息列表
	private List<Point> bihuaPointList = null;

	public int getStrokeIndex() {
		return strokeIndex;
	}

	public void setStrokeIndex(int strokeIndex) {
		this.strokeIndex = strokeIndex;
	}

	public byte[] getStrokeVoiceBuffer() {
		return strokeVoiceBuffer;
	}

	public void setStrokeVoiceBuffer(byte[] strokeVoiceBuffer) {
		this.strokeVoiceBuffer = strokeVoiceBuffer;
	} 
	
	public List<Point> getBihuaPointList() {
		return bihuaPointList;
	}

	public void setBihuaPointList(List<Point> bihuaPointList) {
		this.bihuaPointList = bihuaPointList;
	}
}
