package com.csy.test.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 描述:字符串工具类
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
	
	/**
	 * 描述：获取英文缩写
	 * @author csy 
	 * @date 2022年7月5日 上午9:33:08
	 * @param string
	 * @param split
	 * @return 缩写英文单词
	 */
	public static String getShort(String string, String split){
		String[] strings = string.split(split);
		StringBuilder stringBuilder = new StringBuilder();
		for (String str:strings){
			stringBuilder.append(lowerFirst(str).charAt(0));
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 描述：获取英文缩写
	 * @author csy 
	 * @date 2022年7月5日 上午9:33:40
	 * @param string
	 * @return 缩写英文单词
	 */
	public static String getShort(String string){
		return getShort(string, "_");
	}
}
