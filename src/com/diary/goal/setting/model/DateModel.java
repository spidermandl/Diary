package com.diary.goal.setting.model;

import java.util.Date;

import com.diary.goal.setting.tools.Constant.SudoType;

public class DateModel {

	private Date date;
	private SudoType type;
	private int category;
	private String text;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public SudoType getType() {
		return type;
	}
	public void setType(SudoType type) {
		this.type = type;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
