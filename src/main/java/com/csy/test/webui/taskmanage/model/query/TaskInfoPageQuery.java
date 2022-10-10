package com.csy.test.webui.taskmanage.model.query;

import lombok.Data;

/**
 * 
 * 描述：任务信息分页 查询对象
 * @author csy
 * @date 2022年9月30日 下午4:45:15
 */
@Data
public class TaskInfoPageQuery{

	/**任务名称*/
	private String taskName;
	
	/**任务描述*/
	private String taskDesc;

	/**任务id唯一标识*/
	private String taskId;
	
	/**任务Key标识*/
	private String taskKey;

	/**调度类型：0使用表达式，1使用到点执行*/
	private Integer dispatchType;
	
	/**是否有效0否，1*/
	private Integer enable;
}
