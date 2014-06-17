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
		String params="username="+user+
			      "&passwd="+passwd+
			      "&email="+email;
		return HTTPTools.javaHttpPost(Constant.SERVER_DOMAIN+Constant.REGISTER, params);
	}
}
