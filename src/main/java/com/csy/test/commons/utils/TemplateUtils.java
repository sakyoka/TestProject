package com.csy.test.commons.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * 描述:模板
 * @author csy
 * @date 2021年1月23日 下午10:49:57
 */
public class TemplateUtils {

	/**
	 * 
	 * 描述：contentMa内容填充模板
	 * @author csy
	 * @date 2021年1月23日 下午10:50:07
	 * @param contentMap 填充内容
	 * @param template 模板
	 * @param reg 占位符表达式c
	 * @return 最终内容
	 */
	public static String fillTemplate(Map<String, String> contentMap , String template , String reg) {
		Matcher m = Pattern.compile(reg).matcher(template);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
	        String param = m.group();
	        Object value = contentMap.get(param.substring(2, param.length() - 1));
	        m.appendReplacement(sb , value == null ? "" : value.toString());
		}
		m.appendTail(sb);
		return sb.toString(); 
	}
	
	/**
	 * 
	 * 描述：contentMa内容填充模板
	 * @author csy
	 * @date 2021年1月23日 下午11:42:43
	 * @param contentMap 填充内容
	 * @param template 模板
	 * @return 最终内容
	 */
	public static String fillTemplate(Map<String, String> contentMap , String template) {
		return fillTemplate(contentMap , template , "\\$\\{\\w+\\}");
	}
}
