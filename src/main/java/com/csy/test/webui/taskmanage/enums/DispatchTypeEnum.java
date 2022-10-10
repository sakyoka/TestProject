package com.csy.test.webui.taskmanage.enums;

import lombok.Getter;

/**
 * 
 * 描述：调度类型
 * @author csy
 * @date 2022年10月8日 下午3:54:04
 */
@Getter
public enum DispatchTypeEnum {
	
	CORN(0, "表达式"),
	
	APPOINT_TIME(1, "执行时间");
	
	private Integer type;
	
	private String desc;
	
	DispatchTypeEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
}
