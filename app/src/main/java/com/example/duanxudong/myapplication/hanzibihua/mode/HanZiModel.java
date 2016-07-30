package com.example.duanxudong.myapplication.hanzibihua.mode;

import java.io.Serializable;

public class HanZiModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int pid;
	private String pinyin;
	private String hanzi;
	private String content;
	private String voice;
	private String hanzi_path;
	private String outline_path;
	private String fill_path;
	private String createtime;
	private String other;
	private String solution;
	private String menu;

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public String getHanzi() {
		return hanzi;
	}
	public void setHanzi(String hanzi) {
		this.hanzi = hanzi;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	public String getHanzi_path() {
		return hanzi_path;
	}
	public void setHanzi_path(String hanzi_path) {
		this.hanzi_path = hanzi_path;
	}
	public String getOutline_path() {
		return outline_path;
	}
	public void setOutline_path(String outline_path) {
		this.outline_path = outline_path;
	}
	public String getFill_path() {
		return fill_path;
	}
	public void setFill_path(String fill_path) {
		this.fill_path = fill_path;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
}
