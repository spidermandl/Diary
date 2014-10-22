package com.diary.goal.setting.tools;

import java.util.HashMap;

import com.diary.goal.setting.DiaryApplication;

/**
 * 内存缓存
 * @author Desmond Duan
 *
 */
public class CacheMap extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9054576910734319547L;

	@Override
	public Object get(Object key) {
		Object obj=super.get(key);
		if(obj==null&&key.toString().equals(Constant.SERVER_USER_ID)){
			obj=String.valueOf(DiaryApplication.getInstance().getDbHelper().lastestLoginTime());
		}
		return obj;
	}
}
