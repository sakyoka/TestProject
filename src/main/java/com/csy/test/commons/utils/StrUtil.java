package com.csy.test.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 描述:
 * @author csy
 * @date 2021年1月23日 下午7:19:49
 */
public class StrUtil extends cn.hutool.core.util.StrUtil{
	
	/**
	 * 描述：去掉所有换行符
	 * @author csy 
	 * @date 2021年2月25日 下午3:48:46
	 * @param contents
	 * @return contents
	 */
	public static String removeAllLineBreaks(String contents){
		if (Objects.isNull(contents))
			return contents;
		
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
		Matcher m = p.matcher(contents);  
		return m.replaceAll("");
	}
}
