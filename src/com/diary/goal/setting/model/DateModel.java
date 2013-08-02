package com.diary.goal.setting.model;

import java.util.Date;

import com.diary.goal.setting.tools.Constant.SudoType;

public class DateModel {

	private Date date;
	private SudoType type;
	private int category;
	private int category_type;
	private String category_name;
	private String text;
	private int configId;
	
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
	public int getCategory_type() {
		return category_type;
	}
	public void setCategory_type(int category_type) {
		this.category_type = category_type;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	
}
