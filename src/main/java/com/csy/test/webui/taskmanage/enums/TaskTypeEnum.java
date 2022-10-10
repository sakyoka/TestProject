package com.csy.test.webui.taskmanage.enums;

import lombok.Getter;

/**
 * 
 * 描述：任务类型
 * @author csy
 * @date 2022年10月8日 下午3:51:31
 */
@Getter
public enum TaskTypeEnum {
	
	URL(0, "url请求方式"),
	
	JAR(1, "调度jar方法"),
	
	CLASS(2, "执行class方法");
	
	private Integer type;
	
	private String desc;
	
	TaskTypeEnum(Integer type, String desc){
		this.type = type;
		this.desc = desc;
	}
}
