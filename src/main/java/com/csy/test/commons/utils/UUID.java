package com.csy.test.commons.utils;

public class UUID {
	
	private UUID(){}
	
	/**
	 * 描述：获取随机字符串
	 * @author csy 
	 * @date 2020年6月12日
	 * @return String
	 */
	public static String getString(){
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
}
