package com.diary.goal.setting.activity.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activiy生命周期功能植入
 * @author Desmond Duan
 *
 */
public class BaseActivity extends Activity {

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
	
}
