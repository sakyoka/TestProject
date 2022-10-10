package com.csy.test.commons.utils;

/**
 * 
 * 描述：操作系统
 * @author csy
 * @date 2022年1月12日 上午9:33:54
 */
public class OsUtils {
	
	private OsUtils(){}
	
	private static final String WINDOW = "window";
	
	private static final String LINUX = "linux";
	
	public static boolean isWindow(){
		return isSystemOf(WINDOW);
	}
	
	public static boolean isLinux(){
		return isSystemOf(LINUX);
	}
	
	public static boolean isSystemOf(String systemName){
        String os = System.getProperties().getProperty("os.name");
        return (systemName != null && os != null && os.toLowerCase().indexOf(systemName.toLowerCase()) != -1);		
	}
}
