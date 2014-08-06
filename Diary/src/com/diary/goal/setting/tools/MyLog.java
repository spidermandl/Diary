package com.diary.goal.setting.tools;

import android.util.Log;
/**
 * 封装Log
 * @author Desmond Duan
 *
 */
public class MyLog{
	
	public static void e(String tag, String msg) {
		if(Constant.DEBUG)
			Log.e(tag, msg);
    }
	
	public static void e(String tag, String msg, Throwable tr) {
		if(Constant.DEBUG)
			Log.e(tag, msg,tr);
	}
}
