package com.csy.test.webui.taskmanage.enums;

import lombok.Getter;

/**
 * 
 * 描述：执行状态
 * @author csy
 * @date 2022年10月8日 下午3:57:38
 */
@Getter
public enum ExecuteStatusEnum {
	
	STOP(0, "停止"),
	
	PROGRESS(1, "执行中的"),
	
	UNSUCCESS(2, "执行失败");
	
	private Integer type;
	
	private String desc;
	
	ExecuteStatusEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
}
