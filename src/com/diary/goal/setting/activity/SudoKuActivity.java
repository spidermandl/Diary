package com.diary.goal.setting.activity;


import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class SudoKuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DiaryApplication.getInstance().setOrientation(
				this.getResources().getConfiguration().orientation);
		setContentView(R.layout.nine_panel_frame);
		//DiaryApplication.getInstance().setOrientation(newConfig.orientation);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//if(newConfig.orientation==Configuration)
		super.onConfigurationChanged(newConfig);
	}
}
