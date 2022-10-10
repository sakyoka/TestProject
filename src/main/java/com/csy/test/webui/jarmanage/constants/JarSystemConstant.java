package com.csy.test.webui.jarmanage.constants;

/**
 * 
 * 描述：jar系统常量
 * @author csy
 * @date 2022年2月24日 下午3:48:12
 */
public class JarSystemConstant {
	
	/**
	 * 系统标识
	 */
	public static final String SYSTEM_REMARK = "system";
	
	/**文件类型*/
	public static final String FILE_TYPE_JAR = "jar";
	
	public static boolean isSystemType(String type){
		return SYSTEM_REMARK.equals(type);
	}
}
