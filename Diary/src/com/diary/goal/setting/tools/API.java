package com.diary.goal.setting.tools;


import java.util.HashMap;

import org.json.JSONObject;

public class API {
	/**
	 * 登录
	 * @param user
	 * @param passwd
	 * @return
	 */
	public static JSONObject login(String user,String passwd){
//		String params="username="+user+
//				      "&passwd="+passwd;
//		return HTTPTools.javaHttpPost(Constant.SERVER_DOMAIN+Constant.LOGIN, params);
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("username", user);
		params.put("passwd", passwd);
		return HTTPTools.connectPost(Constant.SERVER_DOMAIN+Constant.LOGIN, params);
	}
	/**
	 * 注册
	 * @param user
	 * @param email
	 * @param passwd
	 * @return
	 */
	public static JSONObject register(String user,String email,String passwd){
//		String params="username="+user+
//			      "&passwd="+passwd+
//			      "&email="+email;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", user);
		params.put("passwd", passwd);
		params.put("email", email);
		return HTTPTools.connectPost(Constant.SERVER_DOMAIN+Constant.REGISTER, params);
	}
	
	/**
	 * 获得用户日记
	 */
	public static JSONObject fetchDiarys(String session_id){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session_id", session_id);
		return HTTPTools.connectPost(Constant.SERVER_DOMAIN+Constant.DIARY_LIST, params);
	}
	
	/**
	 * 创建用户日记
	 * @param session_id
	 * @param created_time
	 * @param content
	 * @return
	 */
	public static JSONObject createDiary(String session_id,String created_time,String content){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session_id", session_id);
		params.put("created_time", created_time);
		params.put("content", content);
		return HTTPTools.connectPost(Constant.SERVER_DOMAIN+Constant.DIARY_CREATE, params);
	}
	/**
	 * 更新用户日记
	 * @param session_id
	 * @param created_time
	 * @param updated_time
	 * @param content
	 * @return
	 */
	public static JSONObject updateDiary(String session_id,String created_time,String updated_time,String content){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session_id", session_id);
		params.put("created_time", created_time);
		params.put("updated_time", updated_time);
		params.put("content", content);
		return HTTPTools.connectPost(Constant.SERVER_DOMAIN+Constant.DIARY_UPDATE, params);
	}
}
