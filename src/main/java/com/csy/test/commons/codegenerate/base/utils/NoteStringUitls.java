package com.csy.test.commons.codegenerate.base.utils;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.csy.test.commons.codegenerate.constants.LineConstants;

public class NoteStringUitls {
	
	private NoteStringUitls(){}
	
	/**
	 * 描述：类注释
	 * @author csy 
	 * @date 2021年1月29日 下午3:22:01
	 * @param stringBuilder
	 * @param desc 描述
	 * @return StringBuilder
	 */
	public static StringBuilder appendClassNote(StringBuilder stringBuilder , String author, String desc) {
		stringBuilder.append(LineConstants.WRAP)
		             .append("/**").append(LineConstants.WRAP)
		             .append(" * 描述：").append(desc == null ? "" : desc).append(LineConstants.WRAP)
		             .append(" * @author ").append(author).append(LineConstants.WRAP)
		             .append(" * @date ").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append(LineConstants.WRAP)
		             .append(" */").append(LineConstants.WRAP);
		return stringBuilder;
	}
	
	/**
	 * 描述：方法注释
	 * @author csy 
	 * @date 2021年1月29日 下午3:22:12
	 * @param stringBuilder
	 * @param desc 描述
	 * @param params 参数集合
	 * @param returnParam 返回值
	 * @return StringBuilder
	 */
	public static StringBuilder appendMethodNote(StringBuilder stringBuilder , String author , String desc , Map<String, String> params , String returnParam) {
		stringBuilder.append(LineConstants.WRAP)
		             .append(LineConstants.BLANK_SPACE_FOUR)
		             .append("/**").append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR)
		             .append(" * 描述：").append(desc == null ? "" : desc).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR)
		             .append(" * @author ").append(author).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR)
		             .append(" * @date ").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR);
		
		if (params != null){
			params.forEach((k , v) -> {
				if (k != null)
				    stringBuilder
				     .append(" * @param ").append(k).append(" ").append(v != null ? v : "").append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR);
			});
		}
		if (returnParam != null){
		    stringBuilder
		             .append(" * @return ").append(returnParam).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR);			
		}
		stringBuilder.append(" */").append(LineConstants.WRAP);
		return stringBuilder;
	}
}
