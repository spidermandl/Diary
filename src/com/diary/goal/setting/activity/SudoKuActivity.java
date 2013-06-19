package com.diary.goal.setting.activity;


import java.util.Date;
import java.util.HashMap;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

public class SudoKuActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DiaryApplication.getInstance().setOrientation(
				this.getResources().getConfiguration().orientation);
		init();
		setContentView(R.layout.nine_panel_frame);
		//DiaryApplication.getInstance().setOrientation(newConfig.orientation);
		super.onCreate(savedInstanceState);
	}
	
	private void init(){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		model.setDate(new Date());
		Cursor c=DiaryApplication.getInstance().getDbHelper().getTodayPad(model.getDate());
        HashMap<Constant.SudoType, Boolean> status=DiaryApplication.getInstance().getPadStatus();
		if(c!=null){
			while(c.moveToNext()){
				status.put(Constant.SudoType.getSudoType(c.getInt(0)), true);
			}
			c.close();
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//if(newConfig.orientation==Configuration)
		super.onConfigurationChanged(newConfig);
	}
}
