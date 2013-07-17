package com.diary.goal.setting.tools;

import java.util.HashMap;
import java.util.Map;

import com.diary.goal.setting.R;

public class Constant {

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
		public static String getTypeString(SudoType type){
			if(type==SUDO_1){
				return "WORK";
			}
			if(type==SUDO_2){
				return "FINANCE";
			}
			if(type==SUDO_3){
				return "FAMILY";
			}
			if(type==SUDO_4){
				return "INNOVATION";
			}
			if(type==SUDO_5){
				return "DATE";
			}
			if(type==SUDO_6){
				return "MIT";
			}
			if(type==SUDO_7){
				return "HEALTH";
			}
			if(type==SUDO_8){
				return "PERSONAL";
			}
			if(type==SUDO_9){
				return "SOUL";
			}
			return "";
		}
		
		public static SudoType forTypeValue(String type) {
			if (type == null) {
				return SUDO_0;
			}
			if ("WORK".equalsIgnoreCase(type)) {
				return SUDO_1;
			}
			if ("FINANCE".equalsIgnoreCase(type)) {
				return SUDO_2;
			}
			if ("FAMILY".equalsIgnoreCase(type)) {
				return SUDO_3;
			}
			if ("INNOVATION".equalsIgnoreCase(type)) {
				return SUDO_4;
			}
			if ("DATE".equalsIgnoreCase(type)) {
				return SUDO_5;
			}
			if ("MIT".equalsIgnoreCase(type)) {
				return SUDO_6;
			}
			if ("HEALTHY".equalsIgnoreCase(type)) {
				return SUDO_7;
			}
			if ("PERSONAL".equalsIgnoreCase(type)) {
				return SUDO_8;
			}
			if ("SOUL".equalsIgnoreCase(type)) {
				return SUDO_9;
			}
			throw new IllegalArgumentException(type);
		}
	}
	
	public static String FLURRY_KEY="YMY4T7WMFCYVP3R5SS7H";
	/**string Dic*/
	public final static  Map<String, Integer> stringDict=new HashMap<String, Integer>(){{
		put(SudoType.getTypeString(SudoType.SUDO_1), R.string.work);
		put(SudoType.getTypeString(SudoType.SUDO_2), R.string.finance);
		put(SudoType.getTypeString(SudoType.SUDO_3), R.string.family);
		put(SudoType.getTypeString(SudoType.SUDO_4), R.string.innovation);
		put(SudoType.getTypeString(SudoType.SUDO_5), R.string.date);
		put(SudoType.getTypeString(SudoType.SUDO_6), R.string.mit);
		put(SudoType.getTypeString(SudoType.SUDO_7), R.string.healthy);
		put(SudoType.getTypeString(SudoType.SUDO_8), R.string.personal);
		put(SudoType.getTypeString(SudoType.SUDO_9), R.string.soul);
		
		put("category1", R.string.category1);
		put("category2", R.string.category2);
		put("category3", R.string.category3);
		put("category4", R.string.category4);
		put("category5", R.string.category5);
		put("category6", R.string.category6);
		
		put("improvements on work", R.string.improvments_on_work);
		put("need to be improved", R.string.lack_on_work);
		put("finance reading", R.string.read_on_finance);
		put("finace accounting", R.string.accounts_on_finance);
		put("relatives", R.string.relatives_on_family);
		put("friends", R.string.friends_on_family);
		put("relationship", R.string.relationship_on_family);
		put("treasure management", R.string.treasure_on_innovation);
		put("time management", R.string.time_management_on_innovation);
		put("thinking pattern", R.string.thinking_on_innovation);
		put("physiological cycle", R.string.physiological_cycle_on_health);
		put("health abnormal", R.string.abnormal_on_health);
		put("excercise", R.string.excercise_on_health);
		put("exceretion", R.string.excretion_on_health);
		put("diet", R.string.diet_on_health);
		put("new concept", R.string.idea_on_personal);
		put("TMC", R.string.tmc_on_personal);
		put("five achievements", R.string.five_achievements_on_soul);
		put("thoughts on achievements", R.string.thoughts_on_achievements_on_soul);
	}
	};
	
	
}
