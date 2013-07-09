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
		OTHER(6), 
		HEALTHY(7), 
		PERSONAL(8), 
		SOUL(9);

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
				return OTHER;
			case 7:
				return HEALTHY;
			case 8:
				return PERSONAL;
			case 9:
				return SOUL;
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
			if(type==OTHER){
				return "OTHER";
			}
			if(type==HEALTHY){
				return "HEALTHY";
			}
			if(type==PERSONAL){
				return "PERSONAL";
			}
			if(type==SOUL){
				return "SOUL";
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
			if ("OTHER".equalsIgnoreCase(type)) {
				return OTHER;
			}
			if ("HEALTHY".equalsIgnoreCase(type)) {
				return HEALTHY;
			}
			if ("PERSONAL".equalsIgnoreCase(type)) {
				return PERSONAL;
			}
			if ("SOUL".equalsIgnoreCase(type)) {
				return SOUL;
			}
			throw new IllegalArgumentException(type);
		}
	}
	
	public static String FLURRY_KEY="YMY4T7WMFCYVP3R5SS7H";
	// public static HashMap<Integer, String> SudoIndex=new HashMap<Integer,
	// String>(map)
}
