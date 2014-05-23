package com.diary.goal.setting.invalid;

import java.util.Date;

import com.diary.goal.setting.tools.Constant.SudoType;

public class DateModel {

	private Date date;//current date
	private SudoType type;//9 sudo main type
	private int category;//category index
	private int category_type;//category visual type
	private String category_name;//category name
	private String text;//content of the record
	private int configId;//static id of config table
	private int categorySubIndex;//sub content of the category,-1 if there is no sub list 
	
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
	public int getCategorySubIndex() {
		return categorySubIndex;
	}
	public void setCategorySubIndex(int categorySubIndex) {
		this.categorySubIndex = categorySubIndex;
	}
	
}
