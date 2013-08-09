package com.diary.goal.setting.tools;

import java.util.HashMap;
import java.util.Map;

import com.diary.goal.setting.R;
/**
 * 
 * @author duanlei
 *
 */
public class Constant {

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
	
	public static String FLURRY_KEY="YMY4T7WMFCYVP3R5SS7H";
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
		
//		put("category1", R.string.category1);
//		put("category2", R.string.category2);
//		put("category3", R.string.category3);
//		put("category4", R.string.category4);
//		put("category5", R.string.category5);
//		put("category6", R.string.category6);
//		
//		put("improvements on work", R.string.improvments_on_work);
//		put("need to be improved", R.string.lack_on_work);
//		put("finance reading", R.string.read_on_finance);
//		put("finace accounting", R.string.accounts_on_finance);
//		put("relatives", R.string.relatives_on_family);
//		put("friends", R.string.friends_on_family);
//		put("relationship", R.string.relationship_on_family);
//		put("treasure management", R.string.treasure_on_innovation);
//		put("time management", R.string.time_management_on_innovation);
//		put("thinking pattern", R.string.thinking_on_innovation);
//		put("physiological cycle", R.string.physiological_cycle_on_health);
//		put("health abnormal", R.string.abnormal_on_health);
//		put("excercise", R.string.excercise_on_health);
//		put("exceretion", R.string.excretion_on_health);
//		put("diet", R.string.diet_on_health);
//		put("new concept", R.string.idea_on_personal);
//		put("TMC", R.string.tmc_on_personal);
//		put("five achievements", R.string.five_achievements_on_soul);
//		put("thoughts on achievements", R.string.thoughts_on_achievements_on_soul);
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
