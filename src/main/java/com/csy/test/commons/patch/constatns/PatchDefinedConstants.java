package com.csy.test.commons.patch.constatns;


public class PatchDefinedConstants {

	/**
	 * /WEB-INF/classes/
	 */
	public static final String CLASS_PATH_SUFFIX = "/WEB-INF/classes/";
	
	/**
	 * /target/classes/
	 */
	public static final String TARGET_CLASS = "/target/classes/";
	
	/**
	 * 数据源：resources文件
	 */
	public static final String SRC_RESOURCES_SUFFIX_ORIGIN = "src/main/resources";
	
	/**
	 * 数据源：static文件
	 */
	public static final String SRC_STATIC_SUFFIX_ORIGIN = "src/main/webapp/static";
	
	/**
	 * 数据源java文件java
	 */
	public static final String SRC_JAVA_SUFFIX_ORIGIN = "src/main/java";
	
	/**
	 * 数据源：web_inf文件
	 */
	public static final String SRC_WEB_INF_SUFFIX_ORIGIN = "src/main/webapp/WEB-INF";
	
	/**
	 * 数据源：web_app文件
	 */
	public static final String SRC_WEB_APP_SUFFIX_ORIGIN = "src/main/webapp/";
	
	/**
	 * 关键字：static
	 */
	public static final String KEY_STATIC = "static";
	
	/**
	 * 关键字：WEB-INF
	 */
	public static final String KEY_WEB_INF = "WEB-INF";
	
	/**
	 * 关键字：java
	 */
	public static final String KEY_JAVA = "java";
	
	/**
	 * 关键字：class
	 */
	public static final String KEY_CLASS = "class";
	
	/**
	 * 空串
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * 正斜杠
	 */
	public static final String  FORWARD_SLASH = "/";
	
	/**
	 * 相反操作系统路径分隔符
	 */
	public static String SYSTEM_REVERSE_SEPARATOR = "/";
	
	static{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")){
			SYSTEM_REVERSE_SEPARATOR  = "/";
		}else{
			SYSTEM_REVERSE_SEPARATOR = "\\";
		}	
	}
}
