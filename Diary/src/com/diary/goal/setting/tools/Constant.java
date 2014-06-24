package com.diary.goal.setting.tools;

import java.util.HashMap;
import java.util.Map;

import com.diary.goal.setting.R;
/**
 * 统一管理应用常量类
 * @author Desmond.duan
 *
 */
public class Constant {
	/**
	 * ======================================server================================================
	 */
	/**
	 * 服务器域名
	 */
	public static final String SERVER_DOMAIN="http://114.215.120.47:3000/";
	/**
	 *  登录
	 */
	public static final String LOGIN="users/login";
	/**
	 * 注册
	 */
	public static final String REGISTER="users/register";
	/**
	 * 获取用户日记
	 */
	public static final String DIARY_LIST="diarys/list";
	/**
	 * 创建用户日记
	 */
	public static final String DIARY_CREATE="diarys/create";
	/**
	 * 更新用户日记
	 */
	public static final String DIARY_UPDATE="diarys/update";
	/**
	 * ==================服务器返回字段
	 */
	public static final String SERVER_SUCCESS="success";
	public static final String SERVER_FAIL="fail";
	public static final String SERVER_SESSION_ID="session_id";
	public static final String SERVER_USER_ID="user_id";
	public static final String SERVER_DIARY_LIST="diarys";
	/**
	 * =======================================client================================================
	 */

	public static String FLURRY_KEY="YMY4T7WMFCYVP3R5SS7H";
	/**
	 * 大标题排列顺序键
	 */
	public static final String MAIN_SEQUENCE_ORDER="main_order";
	/**
	 * 小标题排列顺序键
	 */
	public static final String SUB_SEQUENCE_ORDER="sub_order";
	/**
	 * 大标题状态指数键
	 */
	public static final String MAIN_STATUS="index";
	/**
	 * 默认模板
	 */
    public static final String TAMPLATE="{" +
    		"\""+MAIN_SEQUENCE_ORDER+"\":[\"健康\",\"修养\",\"心灵\",\"工作\",\"人脉\",\"财富\",\"创意\",\"MIT\"]," +
    		"\"健康\":[\"跑步\",\"身体\",\"饮食\",\"感悟\"]," +
    		"\"修养\":[\"学习\",\"读书\",\"外语\",\"感悟\"]," +
    		"\"心灵\":[\"感恩\",\"成功\",\"感悟\"]," +
    		"\"工作\":[\"效率\",\"进步\"]," +
    		"\"人脉\":[\"朋友\",\"家人\",\"恋人\"]," +
    		"\"财富\":[\"记账\",\"理财\",\"感悟\"]," +
    		"\"创意\":[\"想法\",\"行动\"]," +
    		"\"MIT\":[\"第一件事\",\"第二件事\",\"第三件事\"]" +
    		"}";
    /**
     * preference 字段
     */
    public static final String PREFERENCE_NAME="diary_preference";
    public static final String P_ACCOUNT="account";
    public static final String P_PASSWORD="password";
    public static final String P_USER_ID = "user_id";
    public static final String P_EMAIL="email";
    public static final String P_SESSION="session_series";
    public static final String P_CREATED_AT="created_at";
    public static final String P_UPDATED_AT="updated_at";
    public static final String P_CONTENT="content";
    
	/**
	 * 9 sudo enum config
	 */
	public static enum SudoType {
		SUDO_0(0),
		SUDO_1(1), 
		SUDO_2(2), 
		SUDO_3(3), 
		SUDO_4(4), 
		SUDO_5(5), 
		SUDO_6(6), 
		SUDO_7(7), 
		SUDO_8(8), 
		SUDO_9(9);

		int sudo_type = 0;

		SudoType(int type) {
			sudo_type = type;
		}
		
		public int getType(){
			return sudo_type;
		}
		
		public static SudoType getSudoType(int type){
			switch(type){
			case 1:
				return SUDO_1;
			case 2:
				return SUDO_2;
			case 3:
				return SUDO_3;
			case 4:
				return SUDO_4;
			case 5:
				return SUDO_5;
			case 6:
				return SUDO_6;
			case 7:
				return SUDO_7;
			case 8:
				return SUDO_8;
			case 9:
				return SUDO_9;
			default:
				return SUDO_0;
			}
		}
		public int getResString(){
			switch(sudo_type){
			case 1:
				return R.string.healthy;
			case 2:
				return R.string.personal;
			case 3:
				return R.string.soul;
			case 4:
				return R.string.work;
			case 5:
				return R.string.date;
			case 6:
				return R.string.family;
			case 7:
				return R.string.finance;
			case 8:
				return R.string.innovation;
			case 9:
				return R.string.mit;
			default:
				return 0;
			}
		}
		
		public int getResDrawable(boolean bool){
			switch(sudo_type){
			case 1:
				if(bool)
					return R.drawable.health_activated;
				else
					return R.drawable.health_null;
			case 2:
				if(bool)
					return R.drawable.personal_activated;
				else
					return R.drawable.personal_null;
			case 3:
				if(bool)
					return R.drawable.soul_activated;
				else
					return R.drawable.soul_null;
			case 4:
				if(bool)
					return R.drawable.work_activated;
				else
					return R.drawable.work_null;
			case 5:
				if(bool)
					return R.drawable.date_activated;
				else
					return R.drawable.date_null;
			case 6:
				if(bool)
					return R.drawable.family_activated;
				else
					return R.drawable.family_null;
			case 7:
				if(bool)
					return R.drawable.finance_activated;
				else
					return R.drawable.finance_null;
			case 8:
				if(bool)
					return R.drawable.innovation_activated;
				else
					return R.drawable.innovation_null;
			case 9:
				if(bool)
					return R.drawable.mit_activated;
				else
					return R.drawable.mit_null;
			default:
				return 0;
			}
		}
		
		public static String getTypeString(SudoType type){
			if(type==SUDO_1){
				return "HEALTH";
			}
			if(type==SUDO_2){
				return "PERSONAL";
			}
			if(type==SUDO_3){
				return "SOUL";
			}
			if(type==SUDO_4){
				return "WORK";
			}
			if(type==SUDO_5){
				return "DATE";
			}
			if(type==SUDO_6){
				return "FAMILY";
			}
			if(type==SUDO_7){
				return "FINANCE";
			}
			if(type==SUDO_8){
				return "INNOVATION";
			}
			if(type==SUDO_9){
				return "MIT";
			}
			return "";
		}
		
		public static SudoType forTypeValue(String type) {
			if (type == null) {
				return SUDO_0;
			}
			if ("HEALTH".equalsIgnoreCase(type)) {
				return SUDO_1;
			}
			if ("PERSONAL".equalsIgnoreCase(type)) {
				return SUDO_2;
			}
			if ("SOUL".equalsIgnoreCase(type)) {
				return SUDO_3;
			}
			if ("WORK".equalsIgnoreCase(type)) {
				return SUDO_4;
			}
			if ("DATE".equalsIgnoreCase(type)) {
				return SUDO_5;
			}
			if ("FAMILY".equalsIgnoreCase(type)) {
				return SUDO_6;
			}
			if ("FINANCE".equalsIgnoreCase(type)) {
				return SUDO_7;
			}
			if ("INNOVATION".equalsIgnoreCase(type)) {
				return SUDO_8;
			}
			if ("MIT".equalsIgnoreCase(type)) {
				return SUDO_9;
			}
			throw new IllegalArgumentException(type);
		}
	}
	
	/**string Dic*/
	public final static  Map<String, Integer> stringDict=new HashMap<String, Integer>(){{
		put(SudoType.getTypeString(SudoType.SUDO_1), SudoType.SUDO_1.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_2), SudoType.SUDO_2.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_3), SudoType.SUDO_3.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_4), SudoType.SUDO_4.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_5), SudoType.SUDO_5.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_6), SudoType.SUDO_6.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_7), SudoType.SUDO_7.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_8), SudoType.SUDO_8.getResString());
		put(SudoType.getTypeString(SudoType.SUDO_9), SudoType.SUDO_9.getResString());
	}
	};
	
	public static final int getWeekDay(int weekDay){
		switch(weekDay){
		case 1:
			return R.string.week_1;
		case 2:
			return R.string.week_2;
		case 3:
			return R.string.week_3;
		case 4:
			return R.string.week_4;
		case 5:
			return R.string.week_5;
		case 6:
			return R.string.week_6;
		case 7:
			return R.string.week_7;
		default:
			return R.string.week_1;
		}
	}
	
	
}
