package com.diary.goal.setting.tools;

import com.diary.goal.setting.DiaryApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreference {
	private static MyPreference myPrefs;//私有化
	private SharedPreferences sp;
	//提供私有的构造方法
	private MyPreference(){}
	/**
	 * 对外提供的初始化方法
	 * @return
	 */
	public static MyPreference getInstance(){
		//初始化自身对象
		if(myPrefs == null){
			myPrefs = new MyPreference();
			myPrefs.initSharedPreferences();
		}
		return myPrefs;
	}
	
	/**
	 * 初始化SharedPreferences对象
	 * @param context
	 */
	public MyPreference initSharedPreferences(){
		//获取SharedPreferences对象
		if(sp == null){
			sp = DiaryApplication.getInstance().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
		return myPrefs;
	}
	
	/**
	 * 向SharedPreferences中写入String类型的数据
	 * @param text
	 */
	public void writeString(String key, String value){
		//获取编辑器对象
		Editor editor = sp.edit();
		//写入数据
		editor.putString(key, value);
		editor.commit();//提交写入的数据
	}
	
	/**
	 * 根据key读取SharedPreferences中的String类型的数据
	 * @param key
	 * @return
	 */
	public String readString(String key){
		return sp.getString(key, "");
	}
}
