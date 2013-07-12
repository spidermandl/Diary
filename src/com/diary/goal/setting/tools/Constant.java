package com.diary.goal.setting.tools;

public class Constant {

	/**
	 * 
	 * @author duanlei
	 * 
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
			if ("OTHER".equalsIgnoreCase(type)) {
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
	// public static HashMap<Integer, String> SudoIndex=new HashMap<Integer,
	// String>(map)
}
