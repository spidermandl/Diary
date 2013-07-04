package com.diary.goal.setting.tools;

public class Constant {

	/**
	 * 
	 * @author duanlei
	 * 
	 */
	public static enum SudoType {
		NO_TYPE(0),
		WORK(1), 
		SOCIAL(2), 
		FAMILY(3), 
		FINANCE(4), 
		DATE(5), 
		SQUARE_6(6), 
		HEALTHY(7), 
		SQUARE_8(8), 
		SQUARE_9(9);

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
				return WORK;
			case 2:
				return SOCIAL;
			case 3:
				return FAMILY;
			case 4:
				return FINANCE;
			case 5:
				return DATE;
			case 6:
				return SQUARE_6;
			case 7:
				return HEALTHY;
			case 8:
				return SQUARE_8;
			case 9:
				return SQUARE_9;
			default:
				return NO_TYPE;
			}
		}
		public static String getTypeString(SudoType type){
			if(type==WORK){
				return "WORK";
			}
			if(type==SOCIAL){
				return "SOCIAL";
			}
			if(type==FAMILY){
				return "FAMILY";
			}
			if(type==FINANCE){
				return "FINANCE";
			}
			if(type==SQUARE_6){
				return "SQUARE_6";
			}
			if(type==HEALTHY){
				return "HEALTHY";
			}
			if(type==SQUARE_8){
				return "SQUARE_8";
			}
			if(type==SQUARE_9){
				return "SQUARE_9";
			}
			return "";
		}
		
		public static SudoType forTypeValue(String type) {
			if (type == null) {
				return NO_TYPE;
			}
			if ("WORK".equalsIgnoreCase(type)) {
				return WORK;
			}
			if ("SOCIAL".equalsIgnoreCase(type)) {
				return SOCIAL;
			}
			if ("FAMILY".equalsIgnoreCase(type)) {
				return FAMILY;
			}
			if ("FINANCE".equalsIgnoreCase(type)) {
				return FINANCE;
			}
			if ("DATE".equalsIgnoreCase(type)) {
				return DATE;
			}
			if ("SQUARE_6".equalsIgnoreCase(type)) {
				return SQUARE_6;
			}
			if ("HEALTHY".equalsIgnoreCase(type)) {
				return HEALTHY;
			}
			if ("SQUARE_8".equalsIgnoreCase(type)) {
				return SQUARE_8;
			}
			if ("SQUARE_9".equalsIgnoreCase(type)) {
				return SQUARE_9;
			}
			throw new IllegalArgumentException(type);
		}
	}
	
	public static String FLURRY_KEY="YMY4T7WMFCYVP3R5SS7H";
	// public static HashMap<Integer, String> SudoIndex=new HashMap<Integer,
	// String>(map)
}
