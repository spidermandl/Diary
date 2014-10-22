package com.diary.goal.setting.activity.base;

import android.content.Context;
import android.content.Intent;

/**
 * Activity植入新功能接口 
 * @author Desmond Duan
 *
 */
public interface BaseActivityInterface {

	void onCreate(Context context);
	void onResume();
	void onPause();
	void onActivityResult(int requestCode, int resultCode, Intent data);
}
