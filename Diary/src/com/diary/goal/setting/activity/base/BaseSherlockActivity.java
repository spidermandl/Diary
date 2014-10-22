package com.diary.goal.setting.activity.base;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * BaseSherlockActivity生命周期功能植入
 * @author Desmond Duan
 *
 */
public abstract class BaseSherlockActivity extends SherlockActivity {

	BaseActivityInterface base=new BaseActivityConcrete();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		base.onCreate(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		base.onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		base.onPause();
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
