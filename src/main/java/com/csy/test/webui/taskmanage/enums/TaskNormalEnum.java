package com.csy.test.webui.taskmanage.enums;

import lombok.Getter;

/**
 * 
 * 描述：任务的常规状态值
 * @author csy
 * @date 2022年10月8日 下午3:59:10
 */
@Getter
public enum TaskNormalEnum {
	
	ENABLE(1, "可用的"),
	
	UNENABLE(0, "不可用的");
	
	private Integer type;
	
	private String desc;
	
	TaskNormalEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
}
