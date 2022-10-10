package com.csy.test.commons.utils;

/**
 * 
 * 描述：控制台打印信息
 * @author csy
 * @date 2022年1月13日 上午11:05:55
 */
public class PrintUtils {
	
	private PrintUtils(){}
	
	public static String getFormatString(String format, Object ...args){
		return String.format(format, args);
	}
	
	public static void println(String format, Object ...args){
		System.out.println(getFormatString(format, args));
	}
	
	public static void print(String format, Object ...args){
		System.out.print(getFormatString(format, args));
	}
	
	public static void errorPrintln(String format, Object ...args){
		System.err.println(getFormatString(format, args));
	}
}
