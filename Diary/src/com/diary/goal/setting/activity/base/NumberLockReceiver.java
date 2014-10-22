package com.diary.goal.setting.activity.base;

import com.diary.goal.setting.activity.NumberLockActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 屏幕数字解锁Receiver
 * @author Desmond Duan
 *
 */
public class NumberLockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setClass(context, NumberLockActivity.class);
		intent.putExtra(NumberLockActivity.TYPE, NumberLockActivity.LOGIN_PASSWORD);
		context.startActivity(intent);
	}
	

}
