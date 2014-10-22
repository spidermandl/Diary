package com.diary.goal.setting.activity.base;

import java.util.List;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.MyPreference;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 
 * Activity植入新功能实现
 * @author Desmond Duan
 *
 */
public class BaseActivityConcrete implements BaseActivityInterface {

	private final static String NUMBERLOCK_ACTION="numberlock_action";
	
	Context context;
	NumberLockReceiver numberLockReceiver;
	
	
	@Override
	public void onCreate(Context context) {
		this.context=context;
		numberLockReceiver = new NumberLockReceiver();

	}

	@Override
	public void onResume() {
		IntentFilter filter = new IntentFilter();
        filter.addAction(NUMBERLOCK_ACTION);
        context.registerReceiver(numberLockReceiver, filter);
        String p_numberlock_code=MyPreference.getInstance().readString(Constant.P_NUMBER_LOCK);
        if(p_numberlock_code!=null&&!p_numberlock_code.equals("")){
        	Object activated=DiaryApplication.getInstance().getMemCache().get(Constant.P_NUMBER_LOCK_ACTIVATED);
        	if(activated==null||((Boolean)activated)){
	        	Intent intent=new Intent();
	        	intent.setAction(NUMBERLOCK_ACTION);
	        	context.sendBroadcast(intent);
        	}
        }
		
	}
	
	@Override
	public void onPause() {
		context.unregisterReceiver(numberLockReceiver);
		if(isApplicationBroughtToBackground()){
			DiaryApplication.getInstance().getMemCache().put(Constant.P_NUMBER_LOCK_ACTIVATED, Boolean.TRUE);
		}

	}
	
	private boolean isApplicationBroughtToBackground() {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	        ComponentName topActivity = tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(context.getPackageName())) {
	            return true;
	        }
	    }
	    return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}



}
