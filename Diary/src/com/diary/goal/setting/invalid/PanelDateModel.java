package com.diary.goal.setting.invalid;

import java.util.Date;
import java.util.HashMap;

import com.diary.goal.setting.tools.Constant;

public class PanelDateModel {

	private Date date;
	private HashMap<Constant.SudoType, Boolean> padStatus;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public HashMap<Constant.SudoType, Boolean> getPadStatus() {
		return padStatus;
	}
	public void setPadStatus(HashMap<Constant.SudoType, Boolean> padStatus) {
		this.padStatus = padStatus;
	}
	
	
}
