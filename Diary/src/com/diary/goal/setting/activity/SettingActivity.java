package com.diary.goal.setting.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.diary.goal.setting.R;

/**
 * 用户设置界面
 * @author Desmond Duan
 *
 */
public class SettingActivity extends SherlockActivity{

	private TextView passwd_protect_status;
	private CheckBox passwd_protect_check;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.setting);
		
		final ActionBar ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayShowHomeEnabled(true);
		ab.setTitle(R.string.setting_title);
		
		super.onCreate(savedInstanceState);
	}
}
