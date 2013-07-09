package com.diary.goal.setting.model;

import java.util.Date;

import com.diary.goal.setting.tools.Constant.SudoType;

public class DateModel {

	private Date date;
	private SudoType type;
	private int category;
	private String category_type;
	private int category_name;
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
	public String getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}
	public int getCategory_name() {
		return category_name;
	}
	public void setCategory_name(int category_name) {
		this.category_name = category_name;
	}
	
}
